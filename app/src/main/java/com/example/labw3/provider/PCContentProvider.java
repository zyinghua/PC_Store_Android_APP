package com.example.labw3.provider;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PCContentProvider extends ContentProvider {
    PCDatabase db;

    public static final String CONTENT_AUTHORITY = "fit2081.app.yinghua";
    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

//    public UriMatcher createUriMatcher() {
//        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//
//        return uriMatcher;
//    }

    @Override
    public boolean onCreate() {
        this.db = PCDatabase.getDatabase(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleteCount;

        deleteCount = db
                .getOpenHelper()
                .getWritableDatabase()
                .delete(PC.TABLE_NAME, selection, selectionArgs);

        return deleteCount;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowId = db
                .getOpenHelper()
                .getWritableDatabase()
                .insert(PC.TABLE_NAME, 0, values);

        return ContentUris.withAppendedId(CONTENT_URI, rowId);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(PC.TABLE_NAME);
        String query = builder.buildQuery(projection, selection, null, null, sortOrder, null);

        final Cursor cursor = db
                .getOpenHelper()
                .getReadableDatabase()
                .query(query, selectionArgs);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int updateCount;

        updateCount = db
                .getOpenHelper()
                .getWritableDatabase()
                .update(PC.TABLE_NAME, 0, values, selection, selectionArgs);

        return updateCount;
    }
}