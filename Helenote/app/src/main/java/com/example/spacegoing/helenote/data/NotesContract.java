package com.example.spacegoing.helenote.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

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

/**
 * Defines table and column names for the note database.
 */
public class NotesContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.spacegoing.helenote";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_NOTE = "note";
    public static final String PATH_REVISION = "revision";

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
            return CONTENT_URI.buildUpon().appendPath("ID").
                    appendPath(Long.toString(id)).build();
        }

        public static Uri buildRevisionWithTime(long time) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(time)).build();
        }

        public static long getTimeFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }

        public static long getIDFromUri(Uri uri) {
            return Long.parseLong(uri.getLastPathSegment());
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


        public static Uri buildNoteUri() {
            return CONTENT_URI.buildUpon().build();
        }

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

        public static Uri buildNoteWithKeyWords(String keyWords) {
            return CONTENT_URI.buildUpon()
                    .appendPath("123").appendPath(keyWords).build();
        }

        public static String getLabelFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
        public static String getKeyWordsFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }

        public static long getTimeFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }

    }
}
