package com.example.spacegoing.helenote;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.spacegoing.helenote.data.NotesContract;
import com.example.spacegoing.helenote.data.NotesProvider;

public class SearchNoteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int Notes_LOADER = 0;
    private NoteListAdapter mNotesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_note);
        getLoaderManager().initLoader(Notes_LOADER, null, this);

        mNotesAdapter = new NoteListAdapter(getApplicationContext(), null, 0);
        // Inflate the layout for this fragment
        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) findViewById(R.id.search_note_listview);
        listView.setAdapter(mNotesAdapter);

        // We'll call our MainActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    Intent intent = new Intent(SearchNoteActivity.this, DetailNoteActivity.class)
                            .setData(
                                    NotesContract.NoteEntry.buildNoteWithTime(
                                            cursor.getLong(NotesProvider.NOTE_COL_TIME_INDEX)
                                    ));
                    startActivity(intent);
                }
            }
        });

    }


    public void searchNoteByKeyWords(View view) {
        getLoaderManager().restartLoader(Notes_LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        EditText editText = (EditText) findViewById(R.id.search_input);
        String keyWords = editText.getText().toString();

        Uri NotesUri = NotesContract.NoteEntry.buildNoteWithKeyWords(keyWords);

        return new CursorLoader(this, NotesUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v("Results: ", "" + data.getCount());
        mNotesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNotesAdapter.swapCursor(null);
    }

}
