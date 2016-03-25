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

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

public class TestUriMatcher extends AndroidTestCase {
    static final long FakeTimeSys = 1458813559350L;  // Fake Time String
    static final int FakeID = 0;  // Fake Time String
    static final String FakeLabel = "Blue";  // Fake Time String


    private static final Uri TEST_NOTE_DIR = NotesContract.NoteEntry.CONTENT_URI;
    private static final Uri TEST_NOTE_WITH_LABEL_DIR = NotesContract.NoteEntry.buildNoteWithLabel(FakeLabel);
    private static final Uri TEST_NOTE_WITH_TIME_ITEM = NotesContract.NoteEntry.buildNoteWithTime(FakeTimeSys);

    private static final Uri TEST_REVISION_DIR = NotesContract.RevisionEntry.CONTENT_URI;
    private static final Uri TEST_REVISION_WITH_TIME_DIR = NotesContract.RevisionEntry.buildRevisionWithTime(FakeTimeSys);

    /*
        Students: This function tests that your UriMatcher returns the correct integer value
        for each of the Uri types that our ContentProvider can handle.  Uncomment this when you are
        ready to test your UriMatcher.
     */
    public void testUriMatcher() {
        UriMatcher testMatcher = NotesProvider.buildUriMatcher();

        assertEquals("Error: The NOTE URI was matched incorrectly.",
                testMatcher.match(TEST_NOTE_DIR), NotesProvider.NOTE);
        assertEquals("Error: The TEST_NOTE_WITH_LABEL URI was matched incorrectly.",
                testMatcher.match(TEST_NOTE_WITH_LABEL_DIR), NotesProvider.NOTE_WITH_LABEL);
        assertEquals("Error: The TEST_NOTE_WITH_TIME URI was matched incorrectly.",
                testMatcher.match(TEST_NOTE_WITH_TIME_ITEM), NotesProvider.NOTE_WITH_TIME);
        assertEquals("Error: The TEST_REVISION_WITH_TIME URI was matched incorrectly.",
                testMatcher.match(TEST_REVISION_WITH_TIME_DIR), NotesProvider.REVISION_WITH_TIME);
        assertEquals("Error: The TEST_REVISION URI was matched incorrectly.",
                testMatcher.match(TEST_REVISION_DIR), NotesProvider.REVISION);
    }
}
