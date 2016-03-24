package com.example.spacegoing.helenote.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the weather database.
 */
public class NotesContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.android.spacegoing.helenote";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_NOTE = "note";
    public static final String PATH_REVISION = "revision";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /* Inner class that defines the table contents of the revision table */
    public static final class RevisionEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVISION).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVISION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVISION;

        // Table name
        public static final String TABLE_NAME = "revision_table";

        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_TIME = "time";

        // Version is stored in long
        public static final String COLUMN_EDITED_TIME = "last_edited_time";

        // Content String
        public static final String COLUMN_CONTENT = "content";

        public static Uri buildRevisionWithID(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildRevisionWithTime(long time) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(time)).build();
        }

        public static long getTimeFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }

    /* Inner class that defines the table contents of the note table */
    public static final class NoteEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTE;

        public static final String TABLE_NAME = "note_table";

        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_TIME = RevisionEntry.COLUMN_TIME;

        public static final String COLUMN_CONTENT = RevisionEntry.COLUMN_CONTENT;

        // Label String and Default label
        public static final String COLUMN_LABEL = "label";
        public static final String DEFAULT_LABEL = "no_label";

        public static Uri buildNoteWithID(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildNoteWithTime(long time) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(time)).build();
        }

        public static Uri buildNoteWithLabel(String label) {
            return CONTENT_URI.buildUpon()
                    .appendPath(label).build();
        }

        public static String getLabelFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static long getTimeFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }

    }
}
