package com.example.spacegoing.helenote;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.spacegoing.helenote.data.NotesContract;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
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

        long noteTime = System.currentTimeMillis();
        Uri uri = NotesContract.NoteEntry.buildNoteWithTime(noteTime);
        ContentValues value = new ContentValues();
        value.put(NotesContract.NoteEntry.COLUMN_TIME, noteTime);
        value.put(NotesContract.NoteEntry.COLUMN_CONTENT,content);

        this.getContentResolver().insert(uri, value);

    }
}
