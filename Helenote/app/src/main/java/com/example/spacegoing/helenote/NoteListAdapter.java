package com.example.spacegoing.helenote;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spacegoing.helenote.data.NotesContract;
import com.example.spacegoing.helenote.data.NotesProvider;

import java.text.DateFormat;
import java.util.Date;
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

public class NoteListAdapter extends CursorAdapter {
    public NoteListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /*
        This is format the string to display
     */
    private void convertCursorRowToUXFormat(View view, Cursor cursor) {

        TextView note_date_view = (TextView) view.findViewById(R.id.note_item_date);
        TextView note_content_view = (TextView) view.findViewById(R.id.note_item_textview);
        TextView note_label_view = (TextView) view.findViewById(R.id.note_item_label);

        String dateTime = formatDateTime(cursor.getLong(NotesProvider.NOTE_COL_TIME_INDEX));
        note_date_view.setText(dateTime);
        String content = cursor.getString(NotesProvider.NOTE_COL_CONTENT_INDEX);
        note_content_view.setText(content);

        String label;
        String edited_time;
        if (cursor.getColumnIndex(NotesContract.RevisionEntry.COLUMN_EDITED_TIME) == -1) {
            label = cursor.getString(NotesProvider.NOTE_COL_LABEL_INDEX);
            note_label_view.setText(label);
        } else {
            edited_time = "Last Edited Time: \n" + formatDateTime(
                    cursor.getLong(NotesProvider.REVISION_COL_EDITED_TIME_INDEX)
            );
            note_content_view.setText(edited_time);
        }

    }

    private static String formatDateTime(long dateInMillis) {
        Date date = new Date(dateInMillis);
        return DateFormat.getDateTimeInstance().format(date);
    }

    /*
        these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);

        return view;
    }

    /*
        This is where fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        convertCursorRowToUXFormat(view, cursor);
    }
}