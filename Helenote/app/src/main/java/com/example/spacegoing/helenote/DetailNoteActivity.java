package com.example.spacegoing.helenote;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
public class DetailNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, new DetailNoteFragment())
                    .commit();
        }
    }

    public static class DetailNoteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


        long noteTime;
        boolean saveNote = true;
        boolean textChanged = false;
        String noteContent;
        String prevLabel;

        private static final int DETAIL_LOADER = 0;

        private ShareActionProvider mShareActionProvider;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);

            // Set textView OnChangedListener
            final TextView textView = (TextView)getActivity().findViewById(R.id.detail_content);
            textView.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (mShareActionProvider != null) {
                        mShareActionProvider.setShareIntent(createShareForecastIntent());
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (!s.toString().equals("")) {
                        textChanged = true;
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_note_detail, container, false);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.detail_menu, menu);

            // Retrieve the share menu item
            MenuItem menuItem = menu.findItem(R.id.action_share);

            // Get the provider and hold onto it to set/change the share intent.
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
            if(mShareActionProvider!=null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }

        }

        private Intent createShareForecastIntent() {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");

            TextView textView = (TextView)getActivity().findViewById(R.id.detail_content);
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
            if(id == R.id.action_history){
                Intent intent = new Intent(getActivity(), HistoryNoteActivity.class)
                        .setData(
                                NotesContract.RevisionEntry.buildRevisionWithTime(noteTime)
                                );
                startActivity(intent);
            }
            if(id == R.id.action_undo){
                TextView textView =(TextView) getActivity().findViewById(R.id.detail_content);
                textView.setText(noteContent);
            }

            return super.onOptionsItemSelected(item);
        }


        @Override
        public void onPause() {
            super.onPause();
            Uri uri = NotesContract.NoteEntry.buildNoteWithTime(noteTime);

            if (saveNote) {
                ContentValues value = new ContentValues();

                TextView labelView = (TextView) getActivity().findViewById(R.id.detail_label);
                String label = labelView.getText().toString();
                if(!label.equals(prevLabel)){
                    value.put(NotesContract.NoteEntry.COLUMN_LABEL,label);
                    textChanged = true;
                }

                if (textChanged) {
                    TextView textView = (TextView) getActivity().findViewById(R.id.detail_content);
                    String content = textView.getText().toString();
                    value.put(NotesContract.NoteEntry.COLUMN_CONTENT, content);

                    getActivity().getContentResolver().update(uri, value, null, null);
                }

            } else {
                getActivity().getContentResolver().delete(uri, null, null);
            }
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Intent intent = getActivity().getIntent();
            if (intent == null) {
                return null;
            }

            return new CursorLoader(getActivity(), intent.getData(), null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data.moveToFirst()) {
                TextView contentView = (TextView) getActivity().findViewById(R.id.detail_content);
                TextView dateView = (TextView) getActivity().findViewById(R.id.detail_date);
                TextView labelView = (TextView) getActivity().findViewById(R.id.detail_label);

                noteTime = data.getLong(NotesProvider.NOTE_COL_TIME_INDEX);
                dateView.setText(formatDateTime(noteTime));

                noteContent = data.getString(NotesProvider.NOTE_COL_CONTENT_INDEX);
                contentView.setText(noteContent);

                if(data.getColumnIndex(NotesContract.NoteEntry.COLUMN_LABEL)!=-1){
                    prevLabel = data.getString(NotesProvider.NOTE_COL_LABEL_INDEX);
                    labelView.setText(prevLabel);
                }else{
                    prevLabel = "";
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }

        private static String formatDateTime(long dateInMillis) {
            Date date = new Date(dateInMillis);
            return DateFormat.getDateTimeInstance().format(date);
        }

    }

}
