package com.example.spacegoing.helenote.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.spacegoing.helenote.data.NotesContract.NoteEntry;
import com.example.spacegoing.helenote.data.NotesContract.RevisionEntry;


/**
 * Manages a local database for note data.
 */
public class NotesDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "note.db";

    public NotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold notes.
        final String SQL_CREATE_NOTE_TABLE = "CREATE TABLE " + NoteEntry.TABLE_NAME + " (" +
                NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NoteEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                NoteEntry.COLUMN_LABEL + " TEXT NOT NULL DEFAULT '" + NoteEntry.DEFAULT_LABEL +"' , " +
                NoteEntry.COLUMN_TIME + " INTEGER UNIQUE NOT NULL " +
                " );";

        final String SQL_CREATE_REVISION_TABLE = "CREATE TABLE " + RevisionEntry.TABLE_NAME + " (" +

                // It's reasonable to assume the user will want information
                // for a certain note and all versions *following*, so the version
                // should be sorted accordingly. So AUTOINCREMENT it.
                RevisionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the TIME of the revision entry associated with its note data
                RevisionEntry.COLUMN_TIME + " INTEGER NOT NULL, " +
                RevisionEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                RevisionEntry.COLUMN_EDITED_TIME + " INTEGER NOT NULL," +

                // Set up the time column as a foreign key to note_table.
                " FOREIGN KEY (" + RevisionEntry.COLUMN_TIME + ") REFERENCES " +
                NoteEntry.TABLE_NAME + " (" + NoteEntry.COLUMN_TIME + "), " +

                // To assure the application have just one note entry per time
                // per revision, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + RevisionEntry.COLUMN_TIME + ", " +
                RevisionEntry.COLUMN_EDITED_TIME + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_NOTE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVISION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Note that this only fires if change the version number for database.
        // It does NOT depend on the version number for application.
        // If want to update the schema without wiping data, commenting out the next 2 lines
        // should be top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NoteEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RevisionEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}