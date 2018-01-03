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
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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
    private static final HashMap<String, Pair<String, String>> WORD_MAP = new HashMap<>();
    private static final HashMap<String, String[]> SYNONYM_MAP = new HashMap<>();
    private static final Multimap<String, String> VERB_PREPOSITION_MAP = HashMultimap.create();
    private static final Multimap<String, String> PREPOSITION_VERB_MAP = HashMultimap.create();


    public DatabaseHelper (Context context) {
        super (context, DATABASE_NAME, null, VERSION);
    }
    public static Vector<String> VERBS = new Vector<>();
    public static Vector<String> PREPOSITIONS = new Vector<>();

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
        String whereClause = ActiveVoyceDatabaseSchema.Users.Cols.USER_NAME + " = ?";
        String[] whereArgs = {userName};
        db.updateWithOnConflict(ActiveVoyceDatabaseSchema.Users.NAME, contentValues,
                whereClause, whereArgs, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void enterCSVData(Context context) {
        ContentValues contentValues = new ContentValues();
        CSVHandler csvHandler = new CSVHandler();
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            List<Object> list = csvHandler.readData(context);
            for (Object combination : list) {
                WordCombinations data = ((WordCombinations) combination);
                wordList.add(data.getWord());
                WORD_MAP.put(data.getWord(),
                        new Pair<>(data.getVerb(), data.getPreposition()));
                SYNONYM_MAP.put(data.getWord(), new String[] {data.getSynonym1(), data.getSynonym2()});
                VERB_PREPOSITION_MAP.put(data.getVerb(), data.getPreposition());
                PREPOSITION_VERB_MAP.put(data.getPreposition(), data.getVerb());

                contentValues.put(ActiveVoyceDatabaseSchema.WordCombinations.Cols.WORD,
                        data.getWord());
                contentValues.put(ActiveVoyceDatabaseSchema.WordCombinations.Cols.VERB,
                        data.getVerb());
                contentValues.put(ActiveVoyceDatabaseSchema.WordCombinations.Cols.PREPOSITION,
                        data.getPreposition());
                contentValues.put(ActiveVoyceDatabaseSchema.WordCombinations.Cols.SYNONYM1,
                        data.getSynonym1());
                contentValues.put(ActiveVoyceDatabaseSchema.WordCombinations.Cols.SYNONYM2,
                        data.getSynonym2());
                db.insertWithOnConflict(ActiveVoyceDatabaseSchema.WordCombinations.NAME,
                        null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            }
        } catch (InvocationTargetException | IOException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

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

    public void markCompleted(String userName, String word) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ActiveVoyceDatabaseSchema.Completions.Cols.COMPLETE, TRUE);
        String whereClause = ActiveVoyceDatabaseSchema.Completions.Cols.USER + " = ?" +
                " AND " + ActiveVoyceDatabaseSchema.Completions.Cols.WORD + " = ?";
        String[] whereArgs = {userName, word};
        db.updateWithOnConflict(ActiveVoyceDatabaseSchema.Completions.NAME, contentValues,
                whereClause, whereArgs, SQLiteDatabase.CONFLICT_IGNORE);
        VERBS.remove(WORD_MAP.get(word).first);
        PREPOSITIONS.remove(WORD_MAP.get(word).second);
    }

    public void resetGame(String userName){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ActiveVoyceDatabaseSchema.Completions.Cols.USER + " = ?";
        String[] whereArgs = {userName};
        db.delete(ActiveVoyceDatabaseSchema.Completions.NAME, whereClause, whereArgs);
        setInitialIncomplete(userName);
    }

    public boolean checkCompletion(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ActiveVoyceDatabaseSchema.Completions.Cols.USER,
                ActiveVoyceDatabaseSchema.Completions.Cols.WORD,
                ActiveVoyceDatabaseSchema.Completions.Cols.COMPLETE
        };
        Cursor cursor =
                db.query(ActiveVoyceDatabaseSchema.Completions.NAME,
                        columns,
                        ActiveVoyceDatabaseSchema.Completions.Cols.USER + " = ?" +
                                " AND " +
                        ActiveVoyceDatabaseSchema.Completions.Cols.COMPLETE + " = ?",
                        new String[] {userName, String.valueOf(FALSE)}, null,
                        null, null, null);
        boolean wordsLeft = cursor.getCount() > 0;
        cursor.close();
        return wordsLeft;
    }

    public void getIncompleteVerbs(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ActiveVoyceDatabaseSchema.Completions.Cols.WORD};
        Cursor cursor =
                db.query(ActiveVoyceDatabaseSchema.Completions.NAME,
                        columns,
                        ActiveVoyceDatabaseSchema.Completions.Cols.USER + " = ?" +
                                " AND " +
                                ActiveVoyceDatabaseSchema.Completions.Cols.COMPLETE + " = ?",
                        new String[] {userName, String.valueOf(FALSE)}, null,
                        null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if(!VERBS.contains(WORD_MAP.get(cursor.getString(0)).first)) {
                    VERBS.add(WORD_MAP.get(cursor.getString(0)).first);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
    }

    public void getIncompletePrepositions(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ActiveVoyceDatabaseSchema.Completions.Cols.WORD};
        Cursor cursor =
                db.query(ActiveVoyceDatabaseSchema.Completions.NAME,
                        columns,
                        ActiveVoyceDatabaseSchema.Completions.Cols.USER + " = ?" +
                                " AND " +
                                ActiveVoyceDatabaseSchema.Completions.Cols.COMPLETE + " = ?",
                        new String[] {userName, String.valueOf(FALSE)}, null,
                        null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if(!PREPOSITIONS.contains(WORD_MAP.get(cursor.getString(0)).second)) {
                    PREPOSITIONS.add(WORD_MAP.get(cursor.getString(0)).second);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
    }

    public Vector<String> getVerbsForPreposition(String preposition) {
        return new Vector<>(PREPOSITION_VERB_MAP.get(preposition));
    }

    public Vector<String> getPrepositionsForVerb(String verb) {
        return new Vector<>(VERB_PREPOSITION_MAP.get(verb));
    }
}
