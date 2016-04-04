package com.usv.testexampleapp.testexampleapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

public class SimpleContentProvider extends ContentProvider {

    private final String NOTE_TABLE = "note_list";
    public static final String NOTE_ID = "id";
    public static final String NOTE_TEXT = "note_text";
    static final String DB_NAME = "notes";
    static final int DB_VERSION = 1;
    private final String DB_CREATE = "create table " + NOTE_TABLE + "("
            + NOTE_ID + " integer primary key autoincrement, "
            + NOTE_TEXT + " text" + ");";

    public static final String AUTHORITY = "com.usv.testexampleapp.Notes";
    public static final String NOTE_PATH = "note_list";
    public static final Uri NOTE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + NOTE_PATH);
    private final String NOTE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + NOTE_PATH;
    private final String NOTE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + NOTE_PATH;
    private final int URI_NOTE = 1;
    private final int URI_NOTE_ID = 2;

    private final UriMatcher uriMatcher;
    DBHelper dbHelper;
    SQLiteDatabase db;

    public SimpleContentProvider() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, NOTE_PATH, URI_NOTE);
        uriMatcher.addURI(AUTHORITY, NOTE_PATH + "/#", URI_NOTE_ID);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) == URI_NOTE_ID) {
            String id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                selection = NOTE_ID + " = " + id;
            } else {
                selection = selection + " AND " + NOTE_ID + " = " + id;
            }
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(NOTE_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_NOTE:
                return NOTE_CONTENT_TYPE;
            case URI_NOTE_ID:
                return NOTE_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != URI_NOTE) {
            throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(NOTE_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(NOTE_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case URI_NOTE:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = NOTE_TEXT + " ASC";
                }
                break;
            case URI_NOTE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = NOTE_ID + " = " + id;
                } else {
                    selection = selection + " AND " + NOTE_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(NOTE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), NOTE_CONTENT_URI);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
