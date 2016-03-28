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

import android.net.Uri;
import android.test.AndroidTestCase;

/*
    This is NOT a complete test for the NoteContract --- just for the functions
    that we expect to work.
 */
public class TestNoteContract extends AndroidTestCase {

    // intentionally includes a slash to make sure Uri is getting quoted correctly
    static final long FakeTimeSys = 1458813559350L;  // Fake Time String

    public void testBuildNoteUri() {
        Uri timeUri = NotesContract.NoteEntry.buildNoteWithTime(FakeTimeSys);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildWeatherLocation in " +
                        "WeatherContract.",
                timeUri);
        assertEquals("Error: Weather location not properly appended to the end of the Uri",
                Long.toString(FakeTimeSys), timeUri.getLastPathSegment());
    }
}
