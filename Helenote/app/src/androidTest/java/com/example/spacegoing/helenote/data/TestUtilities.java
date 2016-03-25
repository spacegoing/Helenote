package com.example.spacegoing.helenote.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.spacegoing.helenote.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/*
    These are functions and some test data to make it easier to test database and
    Content Provider.
 */
public class TestUtilities extends AndroidTestCase {

    static final long FakeTimeSys = 1458813559350L;  // Fake Time String

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            Log.v("columnName", columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            Log.v("expectedValue", expectedValue);
            Log.v("actualValue", valueCursor.getString(idx));

            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /*
        create some default note values for database tests.
     */
    static ContentValues createNoteValues() {
        ContentValues noteValues = new ContentValues();

        noteValues.put(NotesContract.NoteEntry.COLUMN_LABEL,"Blue");
        noteValues.put(NotesContract.NoteEntry.COLUMN_TIME, FakeTimeSys);
        noteValues.put(NotesContract.NoteEntry.COLUMN_CONTENT,"Just some test Strings");

        return noteValues;
    }

    static ContentValues[] createRevisionValues() {
        // Create a new map of values, where column names are the keys
        ContentValues noteValues1 = new ContentValues();
        ContentValues noteValues2 = new ContentValues();
        ContentValues noteValues3 = new ContentValues();


        noteValues1.put(NotesContract.RevisionEntry.COLUMN_EDITED_TIME,FakeTimeSys-1);
        noteValues1.put(NotesContract.RevisionEntry.COLUMN_TIME, FakeTimeSys);
        noteValues1.put(NotesContract.RevisionEntry.COLUMN_CONTENT,"Just some test Strings -1");

        noteValues2.put(NotesContract.RevisionEntry.COLUMN_EDITED_TIME,FakeTimeSys-2);
        noteValues2.put(NotesContract.RevisionEntry.COLUMN_TIME, FakeTimeSys);
        noteValues2.put(NotesContract.RevisionEntry.COLUMN_CONTENT,"Just some test Strings -2");

        noteValues3.put(NotesContract.RevisionEntry.COLUMN_EDITED_TIME,FakeTimeSys-3);
        noteValues3.put(NotesContract.RevisionEntry.COLUMN_TIME, FakeTimeSys);
        noteValues3.put(NotesContract.RevisionEntry.COLUMN_CONTENT,"Just some test Strings -3");

        ContentValues noteValuesArray[] = {noteValues1,noteValues2,noteValues3};

        return noteValuesArray;
    }

    /*
        TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that grabbed from the Android
        CTS tests.

        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
