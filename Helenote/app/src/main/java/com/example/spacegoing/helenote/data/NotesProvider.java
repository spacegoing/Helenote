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
 * Created by spacegoing on 3/23/16.
 */
public class NotesProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private NotesDbHelper mOpenHelper;

    static final int NOTE = 100;
    static final int NOTE_WITH_TIME = 101;
    static final int NOTE_WITH_LABEL = 102;

    static final int REVISION = 300;
    static final int REVISION_WITH_TIME = 301;

    // Define Projection and Index
    private static final String[] sNoteProjection = {
            NotesContract.NoteEntry._ID,
            NotesContract.NoteEntry.COLUMN_TIME,
            NotesContract.NoteEntry.COLUMN_CONTENT,
            NotesContract.NoteEntry.COLUMN_LABEL
    };

    static int NOTE_ID_INDEX = 0;
    static int NOTE_COL_TIME_INDEX = 1;
    static int NOTE_COL_CONTENT_INDEX = 2;
    static int NOTE_COL_LABEL_INDEX = 3;

    private static final String[] sRevisionProjection = {
            NotesContract.RevisionEntry._ID,
            NotesContract.RevisionEntry.COLUMN_TIME,
            NotesContract.RevisionEntry.COLUMN_EDITED_TIME,
            NotesContract.RevisionEntry.COLUMN_CONTENT
    };

    static int REVISION_ID_INDEX = 0;
    static int REVISION_COL_TIME_INDEX = 1;
    static int REVISION_COL_VERSION_INDEX = 2;
    static int REVISION_COL_CONTENT_INDEX = 3;

    //note_table.time = ?
    private static final String sNoteTimeSelection =
            NotesContract.NoteEntry.TABLE_NAME +
                    "." + NotesContract.NoteEntry.COLUMN_TIME + " = ?";
    //revision_table.time = ?
    private static final String sRevisionTimeSelection =
            NotesContract.RevisionEntry.TABLE_NAME +
                    "." + NotesContract.RevisionEntry.COLUMN_TIME + " = ?";

    //note_table.label = ?
    private static final String sNoteLabelSelection =
            NotesContract.NoteEntry.TABLE_NAME +
                    "." + NotesContract.NoteEntry.COLUMN_LABEL + " = ?";

    // Note: ORDER BY note_table.TIME DESC
    private static final String sNoteSortOrder = "ORDER BY " +
            NotesContract.NoteEntry.TABLE_NAME + "." +
            NotesContract.NoteEntry.COLUMN_TIME + "DESC";

    // Revision: ORDER BY revision_table._ID DESC
    private static final String sRevisionSortOrder = "ORDER BY " +
            NotesContract.RevisionEntry.TABLE_NAME + "." +
            NotesContract.RevisionEntry._ID + "DESC";


    private Cursor getNoteByTime(Uri uri) {
        long time = NotesContract.NoteEntry.getTimeFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sNoteTimeSelection;
        selectionArgs = new String[]{Long.toString(time)};

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

        return mOpenHelper.getReadableDatabase().query(NotesContract.NoteEntry.TABLE_NAME,
                sNoteProjection,
                selection,
                selectionArgs,
                null,
                null,
                sNoteSortOrder
        );
    }

    private Cursor getRevisionByTime(Uri uri) {
        long time = NotesContract.RevisionEntry.getTimeFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sNoteTimeSelection;
        selectionArgs = new String[]{Long.toString(time)};

        return mOpenHelper.getReadableDatabase().query(NotesContract.RevisionEntry.TABLE_NAME,
                sNoteProjection,
                selection,
                selectionArgs,
                null,
                null,
                sRevisionSortOrder
        );
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

        matcher.addURI(authority, NotesContract.PATH_REVISION + "/#", REVISION_WITH_TIME);


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
                return NotesContract.NoteEntry.CONTENT_TYPE;
            case NOTE_WITH_TIME:
                return NotesContract.NoteEntry.CONTENT_ITEM_TYPE;
            case NOTE:
                return NotesContract.NoteEntry.CONTENT_TYPE;
            case NOTE_WITH_LABEL:
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
                        projection,
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

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case NOTE: {
                normalizeDate(values);
                long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = WeatherContract.WeatherEntry.buildWeatherUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case NOTE_WITH_LABEL: {
                long _id = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = WeatherContract.LocationEntry.buildLocationUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case NOTE:
                rowsDeleted = db.delete(
                        WeatherContract.WeatherEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case NOTE_WITH_LABEL:
                rowsDeleted = db.delete(
                        WeatherContract.LocationEntry.TABLE_NAME, selection, selectionArgs);
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

    private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(WeatherContract.WeatherEntry.COLUMN_DATE)) {
            long dateValue = values.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE);
            values.put(WeatherContract.WeatherEntry.COLUMN_DATE, WeatherContract.normalizeDate(dateValue));
        }
    }

    @Override
    public int update(
            @NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case NOTE:
                normalizeDate(values);
                rowsUpdated = db.update(WeatherContract.WeatherEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case NOTE_WITH_LABEL:
                rowsUpdated = db.update(WeatherContract.LocationEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}
