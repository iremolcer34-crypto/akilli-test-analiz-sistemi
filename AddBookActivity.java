package com.beykent.akillitestanaliz;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddBookActivity extends AppCompatActivity {

    private EditText etBookName, etPublisher;
    private Button btnSaveBook;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        dbHelper = new DatabaseHelper(this);

        // Sadece XML'de var olan bileşenleri bağlıyoruz
        etBookName = findViewById(R.id.etBookName);
        etPublisher = findViewById(R.id.etPublisher);
        btnSaveBook = findViewById(R.id.btnSaveBook);

        btnSaveBook.setOnClickListener(v -> saveBook());
    }

    private void saveBook() {
        String name = etBookName.getText().toString().trim();
        String publisher = etPublisher.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Lütfen kitap adını girin!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COL_BOOK_NAME, name);
        values.put(DatabaseHelper.COL_BOOK_SUBJECT, publisher);
        // Test sayısı alanı silindiği için buraya sabit bir değer veriyoruz
        values.put(DatabaseHelper.COL_BOOK_TOTAL_TESTS, 100);

        long id = db.insert(DatabaseHelper.TABLE_BOOKS, null, values);

        if (id != -1) {
            Toast.makeText(this, "Kitap başarıyla eklendi!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Hata oluştu!", Toast.LENGTH_SHORT).show();
        }
    }
}