
package com.example.spacegoing.helenote.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {

    // To start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(NotesDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    /*
        Test Create DB
     */
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(NotesContract.RevisionEntry.TABLE_NAME);
        tableNameHashSet.add(NotesContract.NoteEntry.TABLE_NAME);

        mContext.deleteDatabase(NotesDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new NotesDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        // if this fails, it means that database doesn't contain both the note entry
        // and revision entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + NotesContract.NoteEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> NoteColumnHashSet = new HashSet<String>();
        NoteColumnHashSet.add(NotesContract.NoteEntry._ID);
        NoteColumnHashSet.add(NotesContract.NoteEntry.COLUMN_TIME);
        NoteColumnHashSet.add(NotesContract.NoteEntry.COLUMN_CONTENT);
        NoteColumnHashSet.add(NotesContract.NoteEntry.COLUMN_LABEL);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            NoteColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required note entry columns",
                NoteColumnHashSet.isEmpty());
        db.close();
    }

    /*
        Here is where build code to test Note can insert and query the
        note database.
    */

    public void testNoteTable() {
        // First insert the location, and then use the NoteRowId to insert
        // the note. Make sure to cover as many failure cases as you can.

        long NoteRowId = insertNote();

        // Make sure we have a valid row ID.
        assertFalse("Error: Note Not Inserted Correctly", NoteRowId == -1L);

        SQLiteDatabase db = new NotesDbHelper(
                this.mContext).getWritableDatabase();
        // Query the database and receive a Cursor back
        // A cursor is primary interface to the query results.
        Cursor NoteCursor = db.query(
                NotesContract.NoteEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue("Error: No Records returned from note query", NoteCursor.moveToFirst());

        ContentValues NoteValues = TestUtilities.createNoteValues();
        // Fifth Step: Validate the location Query
        TestUtilities.validateCurrentRecord("testInsertReadDb noteEntry failed to validate",
                NoteCursor, NoteValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from note query",
                NoteCursor.moveToNext());

        // Sixth Step: Close cursor and database
        NoteCursor.close();
        db.close();
    }


    public long insertNote() {
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when try to get a writable database.
        NotesDbHelper dbHelper = new NotesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues
        ContentValues testValues = TestUtilities.createNoteValues();

        // Third Step: Insert ContentValues into database and get a row ID back
        long NoteRowId;
        NoteRowId = db.insert(NotesContract.NoteEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue(NoteRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                NotesContract.NoteEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue("Error: No Records returned from note query", cursor.moveToFirst());

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: note Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from note query",
                cursor.moveToNext());

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
        return NoteRowId;
    }
}
