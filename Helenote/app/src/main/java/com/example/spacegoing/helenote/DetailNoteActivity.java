package com.example.spacegoing.helenote;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.spacegoing.helenote.data.NotesContract;
import com.example.spacegoing.helenote.data.NotesProvider;

public class DetailNoteActivity extends AppCompatActivity {

    long noteTime;
    boolean saveNote = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        TextView textView = (TextView) findViewById(R.id.editText);
        Intent intent = getIntent();

        Uri uri = intent.getData();
        noteTime = Long.parseLong(uri.getLastPathSegment());
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        cursor.moveToFirst();
        textView.setText(cursor.getString(NotesProvider.NOTE_COL_CONTENT_INDEX));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_delete) {
            saveNote = false;
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Uri uri = NotesContract.NoteEntry.buildNoteWithTime(noteTime);

        if (saveNote) {
            TextView textView = (TextView) findViewById(R.id.editText);
            String content = textView.getText().toString();
            ContentValues value = new ContentValues();
            value.put(NotesContract.NoteEntry.COLUMN_CONTENT, content);
            getContentResolver().update(uri, value, null, null);
        } else {
            getContentResolver().delete(uri,null,null);
        }
    }

}
