package com.example.spacegoing.helenote;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spacegoing.helenote.data.NotesContract;

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
public class CreateNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_create);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.create_container, new CreateNoteFragment())
                    .commit();
        }
    }

    public static class CreateNoteFragment extends Fragment {

        long noteTime;
        boolean saveNote = true;
        private ShareActionProvider mShareActionProvider;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            // Create an empty note
            noteTime = System.currentTimeMillis();
            Uri uri = NotesContract.NoteEntry.buildNoteWithTime(noteTime);
            ContentValues value = new ContentValues();
            value.put(NotesContract.NoteEntry.COLUMN_TIME, noteTime);
            value.put(NotesContract.NoteEntry.COLUMN_CONTENT, "");
            getActivity().getContentResolver().insert(uri, value);

            return inflater.inflate(R.layout.fragment_note_create, container, false);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Set textView OnChangedListener
            TextView textView = (TextView) getActivity().findViewById(R.id.createText);
            textView.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (mShareActionProvider != null) {
                        mShareActionProvider.setShareIntent(createShareForecastIntent());
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });

            TextView dateView = (TextView) getActivity().findViewById(R.id.create_date);
            dateView.setText(formatDateTime(noteTime));
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.detail_menu, menu);


            // Retrieve the share menu item
            MenuItem menuItem = menu.findItem(R.id.action_share);

            // Get the provider and hold onto it to set/change the share intent.
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }

        }

        // Get Text from view and set to an implicit Intent.
        private Intent createShareForecastIntent() {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");

            TextView textView = (TextView) getActivity().findViewById(R.id.createText);
            String content = textView.getText().toString();
            shareIntent.putExtra(Intent.EXTRA_TEXT, content);
            return shareIntent;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_delete) {
                saveNote = false;
                getActivity().finish();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onPause() {
            super.onPause();

            Uri uri = NotesContract.NoteEntry.buildNoteWithTime(noteTime);
            if (saveNote) {
                // Update Note Content
                TextView textView = (TextView) getActivity().findViewById(R.id.createText);
                String content = textView.getText().toString();
                ContentValues value = new ContentValues();
                value.put(NotesContract.NoteEntry.COLUMN_CONTENT, content);

                TextView labelView = (TextView) getActivity().findViewById(R.id.create_label);
                String label = labelView.getText().toString();
                if(!label.equals("")){
                    value.put(NotesContract.NoteEntry.COLUMN_LABEL,label);
                }

                getActivity().getContentResolver().update(uri, value, null, null);
            } else {
                //Delete Note
                getActivity().getContentResolver().delete(uri, null, null);
            }
        }

        private static String formatDateTime(long dateInMillis) {
            Date date = new Date(dateInMillis);
            return DateFormat.getDateTimeInstance().format(date);
        }

    }
}
