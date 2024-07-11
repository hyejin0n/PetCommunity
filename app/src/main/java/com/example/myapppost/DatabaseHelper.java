package com.example.myapppost;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "blog.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "posts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";

    // 추가: comments 테이블 관련 상수
    private static final String TABLE_COMMENTS = "comments";
    private static final String COLUMN_COMMENT_ID = "comment_id";
    private static final String COLUMN_POST_ID = "post_id";
    private static final String COLUMN_COMMENT_TEXT = "comment_text";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_CONTENT + " TEXT"
                + ")";
        db.execSQL(CREATE_POSTS_TABLE);

        // 추가: comments 테이블 생성
        String CREATE_COMMENTS_TABLE = "CREATE TABLE " + TABLE_COMMENTS + "("
                + COLUMN_COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_POST_ID + " INTEGER,"
                + COLUMN_COMMENT_TEXT + " TEXT"
                + ")";
        db.execSQL(CREATE_COMMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        onCreate(db);
    }

    public boolean addPost(String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CONTENT, content);
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }

    public Cursor getAllPosts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public boolean deletePost(int postId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("posts", "id = ?", new String[] { String.valueOf(postId) }) > 0;
    }



}
