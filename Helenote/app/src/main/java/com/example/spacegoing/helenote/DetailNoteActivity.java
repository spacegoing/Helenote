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
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TextView textView = (TextView) findViewById(R.id.editText);
        String content = textView.getText().toString();

        Uri uri = NotesContract.NoteEntry.buildNoteWithTime(noteTime);
        ContentValues value = new ContentValues();
        value.put(NotesContract.NoteEntry.COLUMN_CONTENT, content);

        this.getContentResolver().update(uri, value,null,null);

    }

}
