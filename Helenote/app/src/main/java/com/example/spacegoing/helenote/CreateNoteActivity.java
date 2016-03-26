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
            return inflater.inflate(R.layout.fragment_note_create, container, false);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Set textView OnChangedListener
            TextView textView = (TextView)getActivity().findViewById(R.id.createText);
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
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
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

            TextView textView = (TextView)getActivity().findViewById(R.id.createText);
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

            if (saveNote) {
                TextView textView = (TextView) getActivity().findViewById(R.id.createText);
                String content = textView.getText().toString();

                long noteTime = System.currentTimeMillis();
                Uri uri = NotesContract.NoteEntry.buildNoteWithTime(noteTime);
                ContentValues value = new ContentValues();
                value.put(NotesContract.NoteEntry.COLUMN_TIME, noteTime);
                value.put(NotesContract.NoteEntry.COLUMN_CONTENT, content);

                getActivity().getContentResolver().insert(uri, value);
            }
        }

    }
}
