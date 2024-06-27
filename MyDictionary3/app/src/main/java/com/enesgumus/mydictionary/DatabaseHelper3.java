package com.enesgumus.mydictionary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper3 extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Wordsss";
    private static final int DATABASE_VERSION = 1;


    public DatabaseHelper3 (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Kelimeler tablosunu oluştur
        db.execSQL("CREATE TABLE IF NOT EXISTS wordsss (id INTEGER PRIMARY KEY, englishname VARCHAR, turkishname VARCHAR, image BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Veritabanı versiyonu yükseltildiğinde gerekirse burada güncelleme yapabilirsiniz
    }

    // Tüm kelimeleri getiren bir metot
    public List<Word> getAllWords() {
        List<Word> wordList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM wordsss", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String englishName = cursor.getString(cursor.getColumnIndex("englishname"));
                @SuppressLint("Range") String turkishName = cursor.getString(cursor.getColumnIndex("turkishname"));
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                // Image ile ilgili işlemleri ekleyebilirsiniz (örneğin byte[]'ı Bitmap'e dönüştürme)
                // byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
                // Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                Word word = new Word(englishName, turkishName,id);
                // word.setImage(image);  // Eğer image kullanıyorsanız
                wordList.add(word);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return wordList;
    }
}

