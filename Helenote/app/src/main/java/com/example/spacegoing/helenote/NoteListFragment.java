package com.example.spacegoing.helenote;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.spacegoing.helenote.data.NotesContract;
import com.example.spacegoing.helenote.data.NotesProvider;

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
public class NoteListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int Notes_LOADER = 0;
    private NoteListAdapter mNotesAdapter;



    public NoteListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_create) {
            Intent intent = new Intent(getActivity(), CreateNoteActivity.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.action_label){
            Intent intent = new Intent(getActivity(), LabelNoteActivity.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.action_search){
            Intent intent = new Intent(getActivity(), SearchNoteActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mNotesAdapter = new NoteListAdapter(getActivity(),null,0);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_note_list, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.notes_listView);
        listView.setAdapter(mNotesAdapter);

        // We'll call our MainActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    Intent intent = new Intent(getActivity(), DetailNoteActivity.class)
                            .setData(
                                    NotesContract.NoteEntry.buildNoteWithTime(
                                    cursor.getLong(NotesProvider.NOTE_COL_TIME_INDEX)
                            ));
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(Notes_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri NotesUri = NotesContract.NoteEntry.buildNoteUri();

        return new CursorLoader(getActivity(),
                NotesUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mNotesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNotesAdapter.swapCursor(null);
    }
}
