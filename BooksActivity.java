package com.beykent.akillitestanaliz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends AppCompatActivity {

    private RecyclerView rvBooks;
    private View layoutEmptyState;
    private BookAdapter adapter;
    private List<Book> bookList;
    private DatabaseHelper dbHelper;
    private FloatingActionButton fabAddBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        rvBooks = findViewById(R.id.rvBooks);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        fabAddBook = findViewById(R.id.fabAddBook);

        dbHelper = new DatabaseHelper(this);
        bookList = new ArrayList<>();
        rvBooks.setLayoutManager(new LinearLayoutManager(this));

        fabAddBook.setOnClickListener(v -> {
            startActivity(new Intent(this, AddBookActivity.class));
        });

        loadBooks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Başka sayfadan (AddBookActivity gibi) dönünce listeyi tazele
        loadBooks();
    }

    private void loadBooks() {
        bookList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_BOOKS, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BOOK_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BOOK_NAME));
                String subject = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BOOK_SUBJECT));
                int progress = dbHelper.getBookProgress(id);

                bookList.add(new Book(id, name, subject, progress));
            } while (cursor.moveToNext());
            cursor.close();
        }

        updateUI();
    }

    private void updateUI() {
        if (bookList.isEmpty()) {
            rvBooks.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
        } else {
            rvBooks.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);

            adapter = new BookAdapter(bookList);
            rvBooks.setAdapter(adapter);
        }
    }
}