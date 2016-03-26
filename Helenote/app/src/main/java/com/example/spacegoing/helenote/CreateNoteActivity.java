package com.example.spacegoing.helenote;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.spacegoing.helenote.data.NotesContract;

public class CreateNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_create);
    }

    @Override
    protected void onPause() {
        super.onPause();
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
