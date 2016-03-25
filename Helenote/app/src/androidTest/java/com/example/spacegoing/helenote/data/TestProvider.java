/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.spacegoing.helenote.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.spacegoing.helenote.data.NotesContract.NoteEntry;
import com.example.spacegoing.helenote.data.NotesContract.RevisionEntry;

/*
    Note: This is not a complete set of tests of the ContentProvider, but it does test
    that at least the basic functionality has been implemented correctly.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();
    static final long FakeTimeSys = 1458813559350L;  // Fake Time String
    static final int FakeID = 0;  // Fake Time String
    static final String FakeLabel = "Blue";  // Fake Time String

    /*
       This helper function deletes all records from both database tables using the ContentProvider.
       It also queries the ContentProvider to make sure that the database has been successfully
       deleted, so it cannot be used until the Query and Delete functions have been written
       in the ContentProvider.

     */
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                NoteEntry.CONTENT_URI,
                null,
                null
        );

        mContext.getContentResolver().delete(
                RevisionEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                NoteEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from note table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                RevisionEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Revision table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    /*
        This test checks to make sure that the content provider is registered correctly.
     */
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // Define the component name based on the package name from the context and the
        // NoteProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                NotesProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: NoteProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + NotesContract.CONTENT_AUTHORITY,
                    providerInfo.authority, NotesContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // The provider isn't registered correctly.
            assertTrue("Error: NoteProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    /*
            This test doesn't touch the database.  It verifies that the ContentProvider returns
            the correct type for each type of URI that it can handle.
         */
    public void testGetType() {
        String type = mContext.getContentResolver().getType(NoteEntry.CONTENT_URI);
        assertEquals("Error: the NoteEntry CONTENT_URI should return NoteEntry.CONTENT_TYPE",
                NoteEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(RevisionEntry.CONTENT_URI);
        assertEquals("Error: the RevisionEntry CONTENT_URI should return RevisionEntry.CONTENT_TYPE",
                RevisionEntry.CONTENT_TYPE, type);
    }


    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.
     */
    public void testBasicNoteQuery() {
        // insert our test records into the database
        NotesDbHelper dbHelper = new NotesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        // Add note
        ContentValues noteValues = TestUtilities.createNoteValues();

        long noteRowId = db.insert(NoteEntry.TABLE_NAME, null, noteValues);
        assertTrue("Unable to Insert NoteEntry into the Database", noteRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor noteCursor = mContext.getContentResolver().query(
                NoteEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicNoteQuery", noteCursor, noteValues);
    }

    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.
     */
    public void testBasicRevisionQueries() {
        // insert our test records into the database
        NotesDbHelper dbHelper = new NotesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        // Add note
        ContentValues[] revisionValues = TestUtilities.createRevisionValues();

        for (ContentValues c : revisionValues) {
            long revisionRowId = db.insert(RevisionEntry.TABLE_NAME, null, c);
            assertTrue("Unable to Insert RevisionEntry into the Database", revisionRowId != -1);
        }
        db.close();

        // Test the basic content provider query
        Cursor revisionCursor = mContext.getContentResolver().query(
                RevisionEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // and let's make sure they match the ones we created
        revisionCursor.moveToFirst();
        for (int i = 0; i < 3; i++, revisionCursor.moveToNext()) {
            TestUtilities.validateCurrentRecord("testRevision.  Error validating RevisionEntry " + i,
                    revisionCursor, revisionValues[i]);
        }
        revisionCursor.close();
    }

    /*
        This test uses the provider to insert and then update the data. \
     */
    public void testUpdateNote() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createNoteValues();

        Uri noteUri = mContext.getContentResolver().
                insert(NoteEntry.CONTENT_URI, values);
        long noteRowId = ContentUris.parseId(noteUri);

        // Verify we got a row back.
        assertTrue(noteRowId != -1);
        Log.d(LOG_TAG, "New row id: " + noteRowId);

        values = TestUtilities.createNoteValues();
        values.put(NoteEntry._ID, noteRowId);
        values.put(NoteEntry.COLUMN_CONTENT, "babababababababa");
        values.put(NoteEntry.COLUMN_LABEL, "redyellow");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor noteCursor = mContext.getContentResolver().query(NoteEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        noteCursor.registerContentObserver(tco);

        Uri uri = NoteEntry.buildNoteWithTime(values.getAsLong(NoteEntry.COLUMN_TIME));

        int count = mContext.getContentResolver().update(uri, values, null, null);
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        //
        // Students: If your code is failing here, it means that your content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        noteCursor.unregisterContentObserver(tco);
        noteCursor.close();

        values = TestUtilities.createNoteValues();
        values.put(NoteEntry._ID, noteRowId);
        values.put(NoteEntry.COLUMN_CONTENT, "babababababababa");
        values.put(NoteEntry.COLUMN_LABEL, "redyellow");
        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                uri,
                null,   // projection
                null,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateLocation.  Error validating location entry update.",
                cursor, values);

        cursor.close();
    }


//    // Make sure we can still delete after adding/updating stuff
//    //
//    // Student: Uncomment this test after you have completed writing the insert functionality
//    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
//    // query functionality must also be complete before this test can be used.
//    public void testInsertReadProvider() {
//        ContentValues testValues = TestUtilities.createNorthPoleLocationValues();
//
//        // Register a content observer for our insert.  This FakeTimeSys, directly with the content resolver
//        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
//        mContext.getContentResolver().registerContentObserver(LocationEntry.CONTENT_URI, true, tco);
//        Uri locationUri = mContext.getContentResolver().insert(LocationEntry.CONTENT_URI, testValues);
//
//        // Did our content observer get called?  Students:  If this fails, your insert location
//        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
//        tco.waitForNotificationOrFail();
//        mContext.getContentResolver().unregisterContentObserver(tco);
//
//        long locationRowId = ContentUris.parseId(locationUri);
//
//        // Verify we got a row back.
//        assertTrue(locationRowId != -1);
//
//        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
//        // the round trip.
//
//        // A cursor is your primary interface to the query results.
//        Cursor cursor = mContext.getContentResolver().query(
//                LocationEntry.CONTENT_URI,
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null  // sort order
//        );
//
//        TestUtilities.validateCursor("testInsertReadProvider. Error validating LocationEntry.",
//                cursor, testValues);
//
//        // Fantastic.  Now that we have a location, add some weather!
//        ContentValues weatherValues = TestUtilities.createWeatherValues(locationRowId);
//        // The TestContentObserver is a one-shot class
//        tco = TestUtilities.getTestContentObserver();
//
//        mContext.getContentResolver().registerContentObserver(WeatherEntry.CONTENT_URI, true, tco);
//
//        Uri weatherInsertUri = mContext.getContentResolver()
//                .insert(WeatherEntry.CONTENT_URI, weatherValues);
//        assertTrue(weatherInsertUri != null);
//
//        // Did our content observer get called?  Students:  If this fails, your insert weather
//        // in your ContentProvider isn't calling
//        // getContext().getContentResolver().notifyChange(uri, null);
//        tco.waitForNotificationOrFail();
//        mContext.getContentResolver().unregisterContentObserver(tco);
//
//        // A cursor is your primary interface to the query results.
//        Cursor weatherCursor = mContext.getContentResolver().query(
//                WeatherEntry.CONTENT_URI,  // Table to Query
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null // columns to group by
//        );
//
//        TestUtilities.validateCursor("testInsertReadProvider. Error validating WeatherEntry insert.",
//                weatherCursor, weatherValues);
//
//        // Add the location values in with the weather data so that we can make
//        // sure that the join worked and we actually get all the values back
//        weatherValues.putAll(testValues);
//
//        // Get the joined Weather and Location data
//        weatherCursor = mContext.getContentResolver().query(
//                WeatherEntry.buildWeatherLocation(TestUtilities.TEST_LOCATION),
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null  // sort order
//        );
//        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Weather and Location Data.",
//                weatherCursor, weatherValues);
//
//        // Get the joined Weather and Location data with a start date
//        weatherCursor = mContext.getContentResolver().query(
//                WeatherEntry.buildWeatherLocationWithStartDate(
//                        TestUtilities.TEST_LOCATION, TestUtilities.TEST_DATE),
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null  // sort order
//        );
//        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Weather and Location Data with start date.",
//                weatherCursor, weatherValues);
//
//        // Get the joined Weather data for a specific date
//        weatherCursor = mContext.getContentResolver().query(
//                WeatherEntry.buildWeatherLocationWithDate(TestUtilities.TEST_LOCATION, TestUtilities.TEST_DATE),
//                null,
//                null,
//                null,
//                null
//        );
//        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Weather and Location data for a specific date.",
//                weatherCursor, weatherValues);
//    }
//
//    // Make sure we can still delete after adding/updating stuff
//    //
//    // Student: Uncomment this test after you have completed writing the delete functionality
//    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
//    // query functionality must also be complete before this test can be used.
//    public void testDeleteRecords() {
//        testInsertReadProvider();
//
//        // Register a content observer for our location delete.
//        TestUtilities.TestContentObserver locationObserver = TestUtilities.getTestContentObserver();
//        mContext.getContentResolver().registerContentObserver(LocationEntry.CONTENT_URI, true, locationObserver);
//
//        // Register a content observer for our weather delete.
//        TestUtilities.TestContentObserver weatherObserver = TestUtilities.getTestContentObserver();
//        mContext.getContentResolver().registerContentObserver(WeatherEntry.CONTENT_URI, true, weatherObserver);
//
//        deleteAllRecordsFromProvider();
//
//        // Students: If either of these fail, you most-likely are not calling the
//        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
//        // delete.  (only if the insertReadProvider is succeeding)
//        locationObserver.waitForNotificationOrFail();
//        weatherObserver.waitForNotificationOrFail();
//
//        mContext.getContentResolver().unregisterContentObserver(locationObserver);
//        mContext.getContentResolver().unregisterContentObserver(weatherObserver);
//    }

}
