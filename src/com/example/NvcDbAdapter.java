/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class NvcDbAdapter {

    private static final String TAG = "NvcHelper";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 3;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE feeling (_id integer primary key autoincrement, name TEXT NOT NULL, description TEXT, needIsMet INTEGER, group_id INTEGER)");
            db.execSQL("CREATE TABLE non_feeling (_id integer primary key autoincrement, name TEXT, description TEXT)");
            db.execSQL("CREATE TABLE non_feeling__feeling (feeling1_id INTEGER, feeling2_id INTEGER, FOREIGN KEY (feeling1_id) REFERENCES feeling(_id), FOREIGN KEY (feeling2_id) REFERENCES feeling(_id), UNIQUE (feeling1_id, feeling2_id))");
            db.execSQL("CREATE TABLE need (_id integer primary key autoincrement, name TEXT, description TEXT, keywords TEXT)");
            db.execSQL("CREATE TABLE strategy (_id integer primary key autoincrement, name TEXT, description TEXT)");
            db.execSQL("CREATE TABLE need__strategy ( need_id INTEGER, strategy_id INTEGER, FOREIGN KEY (need_id) REFERENCES need(_id), FOREIGN KEY (strategy_id) REFERENCES strategy(_id), UNIQUE (need_id, strategy_id))");
            
            db.execSQL("INSERT INTO need (name, description) VALUES (\'Anerkennung\', \'Respekt vor der eigenen Person oder Leistung\')");
            db.execSQL("INSERT INTO need (name, description) VALUES (\'Sicherheit\', \'Sicherheit bezieht sich immer auf ein Bedürftnis, dessen Erfüllung sicher gestellt sein soll\')");
            db.execSQL("INSERT INTO need (name, description) VALUES (\'Liebe\', \'Keine einheitliche Definition gefunden. gez Menschheit\')");
            db.execSQL("INSERT INTO need (name, description) VALUES (\'Gemeinschaft\', \'Zusammensein mit anderen Menschen\')");
            db.execSQL("INSERT INTO need (name, description) VALUES (\'Ruhe\', \'Akustische Stille oder innere Ruhe\')");
            db.execSQL("INSERT INTO feeling (name, needIsMet, description) VALUES (\'freudig\', 1, \'hier erklärung von freudig\')");
            db.execSQL("INSERT INTO feeling (name, needIsMet, description) VALUES (\'gluecklich\', 1, \'hier erklärung von gluecklich\')");
            db.execSQL("INSERT INTO feeling (name, needIsMet, description) VALUES (\'traurig\', 0, \'hier erklärung von traurig\')");
            db.execSQL("INSERT INTO feeling (name, needIsMet, description) VALUES (\'wuetend\', 0, \'hier erklärung von wuetend\')");
            db.execSQL("INSERT INTO feeling (name, needIsMet, description) VALUES (\'erregt\', NULL, \'hier erklärung von erregt\')");
            db.execSQL("INSERT INTO feeling (name, needIsMet, description) VALUES (\'entspannt\', 1, \'hier erklärung von entspannt\')");
            db.execSQL("INSERT INTO feeling (name, needIsMet, description) VALUES (\'genervt\', 1, \'hier erklärung von genervt\')");
            db.execSQL("INSERT INTO feeling (name, needIsMet, description) VALUES (\'ruhelos\', 1, \'hier erklärung von ruhelos\')");
            db.execSQL("INSERT INTO feeling (name, needIsMet, description) VALUES (\'rastlos\', 1, \'hier erklärung von rastlos\')");
            db.execSQL("INSERT INTO strategy (name, description) VALUES (\'Spazieren gehen\', \'Alleine, mit Freunden oder mit Hund 0,5 bis 3 Stunden gehen.\')");
            db.execSQL("INSERT INTO strategy (name, description) VALUES (\'Buch lesen\', \'...\')");
            db.execSQL("INSERT INTO strategy (name, description) VALUES (\'Etwas mit Partner unternehmen\', \'...\')");
            db.execSQL("INSERT INTO strategy (name, description) VALUES (\'Freundeskreis erweitern\', \'...\')");
            db.execSQL("INSERT INTO strategy (name, description) VALUES (\'Rueckmeldung einholen\', \'...\')");
//            db.execSQL("INSERT INTO need__strategy (need_id, strategy_id) VALUES ((select _id from need where name = \'Ruhe\'), (select _id from strategy where label = \'Spazieren gehen\'))");
//            db.execSQL("INSERT INTO need__strategy (need_id, strategy_id) VALUES (4, 1)");
//            db.execSQL("INSERT INTO need__strategy (need_id, strategy_id) VALUES (2, 2)");
//            db.execSQL("INSERT INTO need__strategy (need_id, strategy_id) VALUES (3, 2)");
//            db.execSQL("INSERT INTO need__strategy (need_id, strategy_id) VALUES (3, 3)");
//            db.execSQL("INSERT INTO need__strategy (need_id, strategy_id) VALUES (1, 3)");
//            db.execSQL("INSERT INTO need__strategy (need_id, strategy_id) VALUES (0, 4)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS feeling");
            db.execSQL("DROP TABLE IF EXISTS non_feeling");
            db.execSQL("DROP TABLE IF EXISTS non_feeling__feeling");
            db.execSQL("DROP TABLE IF EXISTS need");
            db.execSQL("DROP TABLE IF EXISTS strategy");
            db.execSQL("DROP TABLE IF EXISTS need__strategy");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public NvcDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public NvcDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
    
    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
//    public Cursor fetchNeeds() {
//
//        return mDb.query("need", new String[] {"_id", "name"}, null, null, null, null, null);
//    }
    
    public Cursor fetch(String table) {

        return mDb.query(table, new String[] {"_id", "name"}, null, null, null, null, null);
    }
    
    public Cursor fetchDetails(String table, long id) {
    	return mDb.query(table, new String[] {"name", "description"}, "_id = "+id, null, null, null, null);
    }
    
//    public Cursor fetchConnected(String table, String conntectTable, long conectedId) {
//
//        return mDb.query(table, new String[] {"_id", "name"}, null, null, null, null, null);
//    }



//    /**
//     * Create a new note using the title and body provided. If the note is
//     * successfully created return the new rowId for that note, otherwise return
//     * a -1 to indicate failure.
//     * 
//     * @param title the title of the note
//     * @param body the body of the note
//     * @return rowId or -1 if failed
//     */
//    public long createNote(String title, String body) {
//        ContentValues initialValues = new ContentValues();
//        initialValues.put(KEY_TITLE, title);
//        initialValues.put(KEY_BODY, body);
//
//        return mDb.insert(DATABASE_TABLE, null, initialValues);
//    }
//
//    /**
//     * Delete the note with the given rowId
//     * 
//     * @param rowId id of note to delete
//     * @return true if deleted, false otherwise
//     */
//    public boolean deleteNote(long rowId) {
//
//        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
//    }
//

//
//    /**
//     * Return a Cursor positioned at the note that matches the given rowId
//     * 
//     * @param rowId id of note to retrieve
//     * @return Cursor positioned to matching note, if found
//     * @throws SQLException if note could not be found/retrieved
//     */
//    public Cursor fetchNote(long rowId) throws SQLException {
//
//        Cursor mCursor =
//
//            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
//                    KEY_TITLE, KEY_BODY}, KEY_ROWID + "=" + rowId, null,
//                    null, null, null, null);
//        if (mCursor != null) {
//            mCursor.moveToFirst();
//        }
//        return mCursor;
//
//    }
//
//    /**
//     * Update the note using the details provided. The note to be updated is
//     * specified using the rowId, and it is altered to use the title and body
//     * values passed in
//     * 
//     * @param rowId id of note to update
//     * @param title value to set note title to
//     * @param body value to set note body to
//     * @return true if the note was successfully updated, false otherwise
//     */
//    public boolean updateNote(long rowId, String title, String body) {
//        ContentValues args = new ContentValues();
//        args.put(KEY_TITLE, title);
//        args.put(KEY_BODY, body);
//
//        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
//    }
}
