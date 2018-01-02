package com.example.vyyom.activevoyce.database.activevoyce;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import com.example.vyyom.activevoyce.CSVHandler;
import com.example.vyyom.activevoyce.PasswordHash;
import com.example.vyyom.activevoyce.User;
import com.example.vyyom.activevoyce.WordCombinations;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vyyom on 12/22/2017.
 *
 * This class creates the database from the schema and gives helper methods for
 * database operations.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 10;
    private static final String DATABASE_NAME = "activevoyce.db";
    private static final int FALSE = 0;
    private static final int TRUE = 1;
    private static final ArrayList<String> wordList = new ArrayList<>();

    public DatabaseHelper (Context context) {
        super (context, DATABASE_NAME, null, VERSION);
    }
    public static final HashMap<String, Pair<String, String>> wordMap = new HashMap<>();
    public static final HashMap<String, String[]> synonymMap = new HashMap<>();

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

        // CREATE WordCombinations table
        db.execSQL("CREATE TABLE " +
                ActiveVoyceDatabaseSchema.WordCombinations.NAME + "(" +
                ActiveVoyceDatabaseSchema.WordCombinations.Cols.WORD + " varchar(30) primary key, " +
                ActiveVoyceDatabaseSchema.WordCombinations.Cols.VERB + " varchar(30) not null, " +
                ActiveVoyceDatabaseSchema.WordCombinations.Cols.PREPOSITION + " varchar(30) not null, " +
                ActiveVoyceDatabaseSchema.WordCombinations.Cols.SYNONYM1 + " varchar(30), " +
                ActiveVoyceDatabaseSchema.WordCombinations.Cols.SYNONYM2 + " varchar(30) " +
                ")"
        );

        // CREATE Completions table
        db.execSQL("CREATE TABLE " +
                ActiveVoyceDatabaseSchema.Completions.NAME + "(" +
                ActiveVoyceDatabaseSchema.Completions.Cols.USER + " varchar(30) not null, " +
                ActiveVoyceDatabaseSchema.Completions.Cols.WORD + " varchar(30) not null, " +
                ActiveVoyceDatabaseSchema.Completions.Cols.COMPLETE + " integer not null default 0, " +
                "foreign key (" + ActiveVoyceDatabaseSchema.Completions.Cols.USER +
                ") references " + ActiveVoyceDatabaseSchema.Users.NAME + "(" +
                ActiveVoyceDatabaseSchema.Users.Cols.USER_NAME + "), " +
                "foreign key (" + ActiveVoyceDatabaseSchema.Completions.Cols.WORD +
                ") references " + ActiveVoyceDatabaseSchema.WordCombinations.NAME + "(" +
                ActiveVoyceDatabaseSchema.WordCombinations.Cols.WORD + ")" +
                ")"
        );
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        // DROP existing tables
        db.execSQL("DROP TABLE IF EXISTS " + ActiveVoyceDatabaseSchema.Users.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ActiveVoyceDatabaseSchema.WordCombinations.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ActiveVoyceDatabaseSchema.Completions.NAME);

        // CREATE database from schema
        onCreate(db);
    }


    // Operations on Users table

    public User getUser (String username) {
        User user = new User();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ActiveVoyceDatabaseSchema.Users.Cols.USER_NAME,
            ActiveVoyceDatabaseSchema.Users.Cols.PASSWORD,
            ActiveVoyceDatabaseSchema.Users.Cols.HIGHSCORE
        };
        Cursor cursor =
                db.query(ActiveVoyceDatabaseSchema.Users.NAME,
                        columns,
                        ActiveVoyceDatabaseSchema.Users.Cols.USER_NAME + " = ?",
                        new String[] {String.valueOf(username)},
                        null, null, null, null
                        );
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            user.setUserName(cursor.getString(0));
            user.setPassword(cursor.getString(1));
            user.setHighScore(cursor.getInt(2));
            cursor.close();
        }
        return user;
    }

    public void enterUser(String tableName, String userName, String password, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        String passwordHash = PasswordHash.hashPassword(password);
        contentValues.put(ActiveVoyceDatabaseSchema.Users.Cols.USER_NAME, userName);
        contentValues.put(ActiveVoyceDatabaseSchema.Users.Cols.PASSWORD, passwordHash);
        db.insertWithOnConflict(tableName, null, contentValues,
                SQLiteDatabase.CONFLICT_IGNORE);
        setInitialIncomplete(userName);
    }

    public void enterHighScore(String userName, int newScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ActiveVoyceDatabaseSchema.Users.Cols.HIGHSCORE, newScore);
        String whereClause = ActiveVoyceDatabaseSchema.Users.Cols.USER_NAME + " = \"" + userName + "\"";
        long updateRowId = db.update(ActiveVoyceDatabaseSchema.Users.NAME, contentValues, whereClause, null);
        if (updateRowId < 0) {
            Log.d("ERROR", "High score not updated");
        }
    }

    // Operations on WordCombinations table

    public void enterCSVData(Context context) {
        ContentValues contentValues = new ContentValues();
        CSVHandler csvHandler = new CSVHandler();
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            List<Object> list = csvHandler.readData(context);
            for (Object combination : list) {
                wordList.add(((WordCombinations) combination).getWord());

                contentValues.put(ActiveVoyceDatabaseSchema.WordCombinations.Cols.WORD,
                        ((WordCombinations) combination).getWord());
                contentValues.put(ActiveVoyceDatabaseSchema.WordCombinations.Cols.VERB,
                        ((WordCombinations) combination).getVerb());
                contentValues.put(ActiveVoyceDatabaseSchema.WordCombinations.Cols.PREPOSITION,
                        ((WordCombinations) combination).getPreposition());
                contentValues.put(ActiveVoyceDatabaseSchema.WordCombinations.Cols.SYNONYM1,
                        ((WordCombinations) combination).getSynonym1());
                contentValues.put(ActiveVoyceDatabaseSchema.WordCombinations.Cols.SYNONYM2,
                        ((WordCombinations) combination).getSynonym2());
                long newRowId = db.insertWithOnConflict(ActiveVoyceDatabaseSchema.WordCombinations.NAME,
                        null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                if(newRowId < 0) {
                    Log.d("ERROR", ((WordCombinations) combination).getWord());
                }
            }
        } catch (InvocationTargetException | IOException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    // Operations on Completions table

    // Private helper functions

    private void setInitialIncomplete(String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for(String word : wordList) {
            contentValues.put(ActiveVoyceDatabaseSchema.Completions.Cols.USER, userName);
            contentValues.put(ActiveVoyceDatabaseSchema.Completions.Cols.WORD, word);
            contentValues.put(ActiveVoyceDatabaseSchema.Completions.Cols.COMPLETE, FALSE);
            db.insertWithOnConflict(ActiveVoyceDatabaseSchema.Completions.NAME,
                    null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }
}
