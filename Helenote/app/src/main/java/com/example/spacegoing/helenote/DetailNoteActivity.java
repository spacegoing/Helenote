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
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spacegoing.helenote.data.NotesContract;
import com.example.spacegoing.helenote.data.NotesProvider;

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
        private static final int DETAIL_LOADER = 0;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_note_detail, container, false);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.detail_menu, menu);
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
                TextView textView = (TextView) getActivity().findViewById(R.id.editText);
                String content = textView.getText().toString();
                ContentValues value = new ContentValues();
                value.put(NotesContract.NoteEntry.COLUMN_CONTENT, content);
                getActivity().getContentResolver().update(uri, value, null, null);
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
            TextView textView = (TextView) getActivity().findViewById(R.id.editText);

            if (data.moveToFirst()) {
                noteTime = data.getLong(NotesProvider.NOTE_COL_TIME_INDEX);

                String content = data.getString(NotesProvider.NOTE_COL_CONTENT_INDEX);
                textView.setText(content);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }

    }

}
