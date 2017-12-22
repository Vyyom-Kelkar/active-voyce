package com.example.vyyom.activevoyce.database.activevoyce;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Vyyom on 12/22/2017.
 *
 * This class creates the database from the schema and gives helper methods for
 * database operations.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 10;
    private static final String DATABASE_NAME = "activevoyce.db";

    public DatabaseHelper (Context context) {
        super (context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        // CREATE Users table
        db.execSQL("CREATE TABLE " +
                ActiveVoyceDatabaseSchema.Users.NAME + "(" +
                ActiveVoyceDatabaseSchema.Users.Cols.USER_NAME + " varchar(30) primary key, " +
                ActiveVoyceDatabaseSchema.Users.Cols.PASSWORD + " varchar(30) not null, " +
                ActiveVoyceDatabaseSchema.Users.Cols.HIGHSCORE + " integer not null default 0 " +
                ")"
        );

        // CREATE FileSize table
        db.execSQL("CREATE TABLE " +
                ActiveVoyceDatabaseSchema.FileSize.NAME + "(" +
                ActiveVoyceDatabaseSchema.FileSize.Cols.FILENAME + " varchar(30) primary key, " +
                ActiveVoyceDatabaseSchema.FileSize.Cols.SIZE + " integer not null default 0 " +
                ")"
        );

        // CREATE Verbs table
        db.execSQL("CREATE TABLE " +
                ActiveVoyceDatabaseSchema.Verbs.NAME + "(" +
                ActiveVoyceDatabaseSchema.Verbs.Cols.ID + " integer primary key autoincrement, " +
                ActiveVoyceDatabaseSchema.Verbs.Cols.VERB + " varchar(30) not null " +
                ")"
        );

        // CREATE Prepositions table
        db.execSQL("CREATE TABLE " +
                ActiveVoyceDatabaseSchema.Prepositions.NAME + "(" +
                ActiveVoyceDatabaseSchema.Prepositions.Cols.ID + " integer primary key autoincrement, " +
                ActiveVoyceDatabaseSchema.Prepositions.Cols.PREPOSITION + " varchar(30) not null " +
                ")"
        );

        // CREATE Words table
        db.execSQL("CREATE TABLE " +
                ActiveVoyceDatabaseSchema.Words.NAME + "(" +
                ActiveVoyceDatabaseSchema.Words.Cols.ID + " integer primary key autoincrement, " +
                ActiveVoyceDatabaseSchema.Words.Cols.WORD + " varchar(30) not null " +
                ")"
        );

        // CREATE Synonyms table
        db.execSQL("CREATE TABLE " +
                ActiveVoyceDatabaseSchema.Synonyms.NAME + "(" +
                ActiveVoyceDatabaseSchema.Synonyms.Cols.ID + " integer primary key autoincrement, " +
                ActiveVoyceDatabaseSchema.Synonyms.Cols.SYNONYM + " varchar(30) not null " +
                ")"
        );

        // CREATE WordCombinations table
        db.execSQL("CREATE TABLE " +
                ActiveVoyceDatabaseSchema.WordCombinations.NAME + "(" +
                ActiveVoyceDatabaseSchema.WordCombinations.Cols.VERBID + " integer not null, " +
                ActiveVoyceDatabaseSchema.WordCombinations.Cols.PREPOSITIONID + " integer not null, " +
                ActiveVoyceDatabaseSchema.WordCombinations.Cols.WORDID + " integer not null " +
                ")"
        );

        // CREATE WordSynonyms table
        db.execSQL("CREATE TABLE " +
                ActiveVoyceDatabaseSchema.WordSynonyms.NAME + "(" +
                ActiveVoyceDatabaseSchema.WordSynonyms.Cols.SYNONYMID + " integer not null, " +
                ActiveVoyceDatabaseSchema.WordSynonyms.Cols.WORDID + " integer not null " +
                ")"
        );
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        // DROP existing tables
        db.execSQL("DROP TABLE IF EXISTS " + ActiveVoyceDatabaseSchema.Users.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ActiveVoyceDatabaseSchema.FileSize.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ActiveVoyceDatabaseSchema.Verbs.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ActiveVoyceDatabaseSchema.Prepositions.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ActiveVoyceDatabaseSchema.Words.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ActiveVoyceDatabaseSchema.Synonyms.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ActiveVoyceDatabaseSchema.WordCombinations.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ActiveVoyceDatabaseSchema.WordSynonyms.NAME);

        // CREATE database from schema
        onCreate(db);
    }

    /*
    * TODO
    * Add methods to get User, FileSize, list of Verbs, list of Prepositions, list of synonyms,
    * list of words.
    */

    /*
    * TODO
    * Add method to set User highscore
    */
}
