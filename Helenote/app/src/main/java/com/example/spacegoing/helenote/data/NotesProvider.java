package com.example.spacegoing.helenote.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by spacegoing on 3/30/16.
 *
 * The code uses some snippets from Udacity Course *Developing Android Apps*.
 * You can download the code through Github Link:
 * https://github.com/udacity/Sunshine-Version-2
 *
 * However all code are with significant modifications under the following
 * license:
 *
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

public class NotesProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private NotesDbHelper mOpenHelper;

    public static final int NOTE = 100;
    public static final int NOTE_WITH_TIME = 101;
    public static final int NOTE_WITH_LABEL = 102;
    public static final int NOTE_WITH_CONTENT_KEY_WORDS = 103;

    public static final int REVISION = 300;
    public static final int REVISION_WITH_TIME = 301;
    public static final int REVISION_WITH_ID = 302;

    // Define Projection and Index
    private static final String[] sNoteProjection = {
            NotesContract.NoteEntry._ID,
            NotesContract.NoteEntry.COLUMN_TIME,
            NotesContract.NoteEntry.COLUMN_CONTENT,
            NotesContract.NoteEntry.COLUMN_LABEL
    };

    public static int NOTE_ID_INDEX = 0;
    public static int NOTE_COL_TIME_INDEX = 1;
    public static int NOTE_COL_CONTENT_INDEX = 2;
    public static int NOTE_COL_LABEL_INDEX = 3;

    private static final String[] sRevisionProjection = {
            NotesContract.RevisionEntry._ID,
            NotesContract.RevisionEntry.COLUMN_TIME,
            NotesContract.RevisionEntry.COLUMN_CONTENT,
            NotesContract.RevisionEntry.COLUMN_EDITED_TIME,
    };

    public static int REVISION_ID_INDEX = 0;
    public static int REVISION_COL_TIME_INDEX = 1;
    public static int REVISION_COL_CONTENT_INDEX = 2;
    public static int REVISION_COL_EDITED_TIME_INDEX = 3;

    //note_table.time = ?
    private static final String sNoteTimeSelection =
            NotesContract.NoteEntry.TABLE_NAME +
                    "." + NotesContract.NoteEntry.COLUMN_TIME + " = ?";
    // note_table.content LIKE ?
    private static final String sKeyWordsSelection =
            NotesContract.NoteEntry.TABLE_NAME +
                    "." + NotesContract.NoteEntry.COLUMN_CONTENT + " LIKE ?";
    //revision_table.time = ?
    private static final String sRevisionTimeSelection =
            NotesContract.RevisionEntry.TABLE_NAME +
                    "." + NotesContract.RevisionEntry.COLUMN_TIME + " = ?";
    private static final String sRevisionIDSelection =
            NotesContract.RevisionEntry.TABLE_NAME +
                    "." + NotesContract.RevisionEntry._ID + " = ?";

    //note_table.label = ?
    private static final String sNoteLabelSelection =
            NotesContract.NoteEntry.TABLE_NAME +
                    "." + NotesContract.NoteEntry.COLUMN_LABEL + " = ?";

    // Note: ORDER BY note_table.TIME DESC
    private static final String sNoteSortOrder = NotesContract.NoteEntry.TABLE_NAME + "."
            + NotesContract.NoteEntry.COLUMN_TIME + " DESC";


    // Revision: ORDER BY revision_table._ID DESC
    private static final String sRevisionSortOrder = NotesContract.RevisionEntry.TABLE_NAME +
            "." + NotesContract.RevisionEntry._ID + " DESC";


    private Cursor getNoteByTime(Uri uri) {
        long time = NotesContract.NoteEntry.getTimeFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sNoteTimeSelection;
        selectionArgs = new String[]{Long.toString(time)};

        Cursor cursor = mOpenHelper.getReadableDatabase().query(NotesContract.NoteEntry.TABLE_NAME,
                sNoteProjection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );


        return mOpenHelper.getReadableDatabase().query(NotesContract.NoteEntry.TABLE_NAME,
                sNoteProjection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }


    private Cursor getNoteByLabel(Uri uri) {
        String labelFromUri = NotesContract.NoteEntry.getLabelFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sNoteLabelSelection;
        selectionArgs = new String[]{labelFromUri};

        Cursor cursor = mOpenHelper.getReadableDatabase().query(NotesContract.NoteEntry.TABLE_NAME,
                sNoteProjection,
                selection,
                selectionArgs,
                null,
                null,
                sNoteSortOrder
        );

        return cursor;
    }

    private Cursor getNoteByKeyWords(Uri uri) {
        String keyWords = NotesContract.NoteEntry.getKeyWordsFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sKeyWordsSelection;
        selectionArgs = new String[]{"%" + keyWords + "%"};

        Cursor cursor = mOpenHelper.getReadableDatabase().query(NotesContract.NoteEntry.TABLE_NAME,
                sNoteProjection,
                selection,
                selectionArgs,
                null,
                null,
                sNoteSortOrder
        );

        return cursor;
    }

    private Cursor getRevisionByTime(Uri uri) {
        long time = NotesContract.RevisionEntry.getTimeFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sRevisionTimeSelection;
        selectionArgs = new String[]{Long.toString(time)};

        return mOpenHelper.getReadableDatabase().query(NotesContract.RevisionEntry.TABLE_NAME,
                sRevisionProjection,
                selection,
                selectionArgs,
                null,
                null,
                sRevisionSortOrder
        );
    }

    private Cursor getRevisionByID(Uri uri) {
        long id = NotesContract.RevisionEntry.getIDFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sRevisionIDSelection;
        selectionArgs = new String[]{Long.toString(id)};

        return mOpenHelper.getReadableDatabase().query(NotesContract.RevisionEntry.TABLE_NAME,
                sRevisionProjection,
                selection,
                selectionArgs,
                null,
                null,
                sRevisionSortOrder
        );
    }

    private int deleteNoteByTime(Uri uri) {
        String[] selectionArgs;
        String selection;
        int rowsDeleted;

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long time = NotesContract.NoteEntry.getTimeFromUri(uri);

        selection = sNoteTimeSelection;
        selectionArgs = new String[]{Long.toString(time)};

        rowsDeleted = db.delete(NotesContract.NoteEntry.TABLE_NAME, selection, selectionArgs);

        selection = sRevisionTimeSelection;
        selectionArgs = new String[]{Long.toString(time)};
        rowsDeleted += db.delete(NotesContract.RevisionEntry.TABLE_NAME, selection, selectionArgs);

        return rowsDeleted;
    }

    private int updateNoteByTime(Uri uri, ContentValues values) {
        String[] selectionArgs;
        String selection;
        int rowsUpdated;

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long time = NotesContract.NoteEntry.getTimeFromUri(uri);

        selection = sNoteTimeSelection;
        selectionArgs = new String[]{Long.toString(time)};

        rowsUpdated = db.update(NotesContract.NoteEntry.TABLE_NAME, values, selection, selectionArgs);

        if (!values.containsKey(NotesContract.NoteEntry.COLUMN_TIME))
            values.put(NotesContract.NoteEntry.COLUMN_TIME, time);

        // Insert into revision table in the meantime
        ContentValues changedValues = changeRevisionValuesFromNoteValues(values);
        long revision_id = db.insert(NotesContract.RevisionEntry.TABLE_NAME, null, changedValues);
        if (revision_id <= 0)
            throw new android.database.SQLException("Failed to insert row into note_tabel" + uri);

        return rowsUpdated;
    }

    static UriMatcher buildUriMatcher() {

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NotesContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, NotesContract.PATH_NOTE, NOTE);
        matcher.addURI(authority, NotesContract.PATH_NOTE + "/#", NOTE_WITH_TIME);
        matcher.addURI(authority, NotesContract.PATH_NOTE + "/*", NOTE_WITH_LABEL);
        matcher.addURI(authority, NotesContract.PATH_NOTE + "/#/*", NOTE_WITH_CONTENT_KEY_WORDS);

        matcher.addURI(authority, NotesContract.PATH_REVISION + "/#", REVISION_WITH_TIME);
        matcher.addURI(authority, NotesContract.PATH_REVISION, REVISION);
        matcher.addURI(authority, NotesContract.PATH_REVISION + "/ID/#", REVISION_WITH_ID);


        return matcher;
    }

    /*
        Students: We've coded this for you.  We just create a new WeatherDbHelper for later use
        here.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new NotesDbHelper(getContext());
        return true;
    }

    /*
        Students: Here's where you'll code the getType function that uses the UriMatcher.  You can
        test this by uncommenting testGetType in TestProvider.

     */
    @Override
    public String getType(@NonNull Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case REVISION_WITH_TIME:
                return NotesContract.RevisionEntry.CONTENT_TYPE;
            case NOTE_WITH_TIME:
                return NotesContract.NoteEntry.CONTENT_ITEM_TYPE;
            case NOTE:
                return NotesContract.NoteEntry.CONTENT_TYPE;
            case NOTE_WITH_LABEL:
                return NotesContract.NoteEntry.CONTENT_TYPE;
            case REVISION:
                return NotesContract.RevisionEntry.CONTENT_TYPE;
            case NOTE_WITH_CONTENT_KEY_WORDS:
                return NotesContract.NoteEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "revision/#"
            case REVISION_WITH_TIME: {
                retCursor = getRevisionByTime(uri);
                break;
            }
            // "note/#"
            case NOTE_WITH_TIME: {
                retCursor = getNoteByTime(uri);
                break;
            }
            // "note"
            case NOTE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        NotesContract.NoteEntry.TABLE_NAME,
                        sNoteProjection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "note/*"
            case NOTE_WITH_LABEL: {
                retCursor = getNoteByLabel(uri);
                break;
            }

            case NOTE_WITH_CONTENT_KEY_WORDS: {
                retCursor = getNoteByKeyWords(uri);
                break;
            }

            case REVISION: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        NotesContract.RevisionEntry.TABLE_NAME,
                        sRevisionProjection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case REVISION_WITH_ID: {
                retCursor = getRevisionByID(uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        ability to insert Notes to the implementation of this function.
     */
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case NOTE_WITH_TIME: {
                long _id = db.insert(NotesContract.NoteEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = NotesContract.NoteEntry.buildNoteWithID(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into note_tabel" + uri);

                // Insert into revision table in the meantime
                ContentValues changedValues = changeRevisionValuesFromNoteValues(values);
                long revision_id = db.insert(NotesContract.RevisionEntry.TABLE_NAME, null, changedValues);
                if (revision_id <= 0)
                    throw new android.database.SQLException("Failed to insert row into note_tabel" + uri);

                break;
            }
            case NOTE: {
                long _id = db.insert(NotesContract.NoteEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = NotesContract.NoteEntry.buildNoteWithID(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into note_tabel" + uri);

                // Insert into revision table in the meantime
                ContentValues changedValues = changeRevisionValuesFromNoteValues(values);
                long revision_id = db.insert(NotesContract.RevisionEntry.TABLE_NAME, null, changedValues);
                if (revision_id <= 0)
                    throw new android.database.SQLException("Failed to insert row into note_tabel" + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        db.close();
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    // Read From Note Values. Remove Label. Insert Edited Time by system time.
    private ContentValues changeRevisionValuesFromNoteValues(ContentValues values) {
        values.remove(NotesContract.NoteEntry.COLUMN_LABEL);
        values.put(NotesContract.RevisionEntry.COLUMN_EDITED_TIME, System.currentTimeMillis());
        return values;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows (Included Revisions) return the number of rows deleted
        if (null == selection) selection = "1";

        switch (match) {
            case NOTE_WITH_TIME:
                rowsDeleted = deleteNoteByTime(uri);
                break;
            case REVISION:
                final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                rowsDeleted = db.delete(NotesContract.RevisionEntry.TABLE_NAME, null, null);
                db.close();
                break;
            case NOTE:
                final SQLiteDatabase notedb = mOpenHelper.getWritableDatabase();
                rowsDeleted = notedb.delete(NotesContract.NoteEntry.TABLE_NAME, null, null);
                notedb.close();
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            @NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case NOTE_WITH_TIME:
                rowsUpdated = updateNoteByTime(uri, values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    // This is a method specifically to assist the testing
    // framework in running smoothly.
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}
