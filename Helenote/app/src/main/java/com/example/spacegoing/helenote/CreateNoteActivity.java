package com.example.spacegoing.helenote;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.spacegoing.helenote.data.NotesContract;

public class CreateNoteActivity extends AppCompatActivity {

    boolean saveNote = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_create);
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

        if (saveNote) {
            TextView textView = (TextView) findViewById(R.id.createText);
            String content = textView.getText().toString();

            long noteTime = System.currentTimeMillis();
            Uri uri = NotesContract.NoteEntry.buildNoteWithTime(noteTime);
            ContentValues value = new ContentValues();
            value.put(NotesContract.NoteEntry.COLUMN_TIME, noteTime);
            value.put(NotesContract.NoteEntry.COLUMN_CONTENT, content);

            this.getContentResolver().insert(uri, value);
        }

    }
}
