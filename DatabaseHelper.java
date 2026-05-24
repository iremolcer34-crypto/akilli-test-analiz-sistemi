package com.beykent.akillitestanaliz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AkilliTestDB";
    private static final int DATABASE_VERSION = 1;

    // --- KİTAPLAR TABLOSU ---
    public static final String TABLE_BOOKS = "books";
    public static final String COL_BOOK_ID = "id";
    public static final String COL_BOOK_NAME = "name";
    public static final String COL_BOOK_SUBJECT = "subject";
    public static final String COL_BOOK_TOTAL_TESTS = "total_tests";

    // --- ÇÖZÜLEN TESTLER TABLOSU ---
    public static final String TABLE_SOLVED_TESTS = "solved_tests";
    public static final String COL_SOLVED_ID = "id";
    public static final String COL_SOLVED_BOOK_ID = "book_id";
    public static final String COL_SOLVED_TEST_NO = "test_no";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Kitaplar tablosunu oluştur
        String createBooksTable = "CREATE TABLE " + TABLE_BOOKS + " (" +
                COL_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_BOOK_NAME + " TEXT, " +
                COL_BOOK_SUBJECT + " TEXT, " +
                COL_BOOK_TOTAL_TESTS + " INTEGER)";

        // Çözülen testler tablosunu oluştur
        String createSolvedTable = "CREATE TABLE " + TABLE_SOLVED_TESTS + " (" +
                COL_SOLVED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SOLVED_BOOK_ID + " INTEGER, " +
                COL_SOLVED_TEST_NO + " INTEGER)";

        db.execSQL(createBooksTable);
        db.execSQL(createSolvedTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOLVED_TESTS);
        onCreate(db);
    }

    // --- TEST DURUMU METOTLARI (TestAdapter için) ---

    public boolean isTestSolved(int bookId, int testNo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SOLVED_TESTS +
                        " WHERE " + COL_SOLVED_BOOK_ID + "=? AND " + COL_SOLVED_TEST_NO + "=?",
                new String[]{String.valueOf(bookId), String.valueOf(testNo)});

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public void updateTestStatus(int bookId, int testNo, boolean isSolved) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (isSolved) {
            ContentValues values = new ContentValues();
            values.put(COL_SOLVED_BOOK_ID, bookId);
            values.put(COL_SOLVED_TEST_NO, testNo);
            db.insert(TABLE_SOLVED_TESTS, null, values);
        } else {
            db.delete(TABLE_SOLVED_TESTS,
                    COL_SOLVED_BOOK_ID + "=? AND " + COL_SOLVED_TEST_NO + "=?",
                    new String[]{String.valueOf(bookId), String.valueOf(testNo)});
        }
    }

    // --- İLERLEME HESAPLAMA (BooksActivity için) ---

    public int getBookProgress(int bookId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Bu kitaptan toplam kaç test çözülmüş?
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_SOLVED_TESTS +
                " WHERE " + COL_SOLVED_BOOK_ID + "=?", new String[]{String.valueOf(bookId)});

        int solvedCount = 0;
        if (cursor.moveToFirst()) {
            solvedCount = cursor.getInt(0);
        }
        cursor.close();

        // Örnek: Kitap başına 20 test olduğunu varsayarsak yüzde hesapla
        // (Eğer kitap eklerken total_tests girdiysen ona göre oranlayabiliriz)
        int totalTests = 20;
        return (solvedCount * 100) / totalTests;
    }
}