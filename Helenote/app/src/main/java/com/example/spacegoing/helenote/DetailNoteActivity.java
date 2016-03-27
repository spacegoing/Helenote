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
            final TextView textView = (TextView)getActivity().findViewById(R.id.editText);
            textView.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (mShareActionProvider != null) {
                        mShareActionProvider.setShareIntent(createShareForecastIntent());
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if(!s.toString().equals("")){
                        textChanged = true;
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
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

            TextView textView = (TextView)getActivity().findViewById(R.id.editText);
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

            return super.onOptionsItemSelected(item);
        }


        @Override
        public void onPause() {
            super.onPause();
            Uri uri = NotesContract.NoteEntry.buildNoteWithTime(noteTime);

            if (saveNote) {

                if (textChanged) {
                    TextView textView = (TextView) getActivity().findViewById(R.id.editText);
                    String content = textView.getText().toString();
                    ContentValues value = new ContentValues();
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
            TextView textView = (TextView) getActivity().findViewById(R.id.editText);

            if (data.moveToFirst()) {
                noteTime = data.getLong(NotesProvider.NOTE_COL_TIME_INDEX);

                noteContent = data.getString(NotesProvider.NOTE_COL_CONTENT_INDEX);
                textView.setText(noteContent);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }

    }

}
