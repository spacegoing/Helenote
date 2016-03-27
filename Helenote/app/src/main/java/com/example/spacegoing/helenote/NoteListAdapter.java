package com.example.spacegoing.helenote;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spacegoing.helenote.data.NotesContract;
import com.example.spacegoing.helenote.data.NotesProvider;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by spacegoing on 3/24/16.
 */

public class NoteListAdapter extends CursorAdapter {
    public NoteListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }
    /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
    private String convertCursorRowToUXFormat(Cursor cursor) {
        String date = formatDate(cursor.getLong(NotesProvider.NOTE_COL_TIME_INDEX));

        String label;
        if(cursor.getColumnIndex(NotesContract.RevisionEntry.COLUMN_EDITED_TIME)==-1){
            label = cursor.getString(NotesProvider.NOTE_COL_LABEL_INDEX);
        }else{
            label = cursor.getString(NotesProvider.REVISION_COL_EDITED_TIME_INDEX);
        }

        return date + " - " + label;
    }

    static String formatDate(long dateInMillis) {
        Date date = new Date(dateInMillis);
        return DateFormat.getDateInstance().format(date);
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        TextView tv = (TextView)view;
        tv.setText(convertCursorRowToUXFormat(cursor));
    }
}