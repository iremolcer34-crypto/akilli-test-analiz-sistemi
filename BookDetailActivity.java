package com.beykent.akillitestanaliz;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {

    private RecyclerView rvTests;
    private TextView txtBookDetailTitle;
    private TestAdapter testAdapter;
    private int bookId;
    private String bookName;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests); // Senin oluşturduğun XML adı

        // 1. Intent'ten gelen verileri al (Kütüphaneden hangi kitaba tıklandı?)
        bookId = getIntent().getIntExtra("BOOK_ID", -1);
        bookName = getIntent().getStringExtra("BOOK_NAME");

        // 2. XML Bağlantıları
        rvTests = findViewById(R.id.rvTests);
        txtBookDetailTitle = findViewById(R.id.txtBookDetailTitle);
        dbHelper = new DatabaseHelper(this);

        if (bookName != null) {
            txtBookDetailTitle.setText(bookName);
        }

        // 3. RecyclerView Ayarları
        rvTests.setLayoutManager(new LinearLayoutManager(this));

        loadTests();
    }

    private void loadTests() {
        // Örnek: Kitabın 20 testi olduğunu varsayalım (Normalde bu veri DB'den gelmeli)
        // Şimdilik test amaçlı 15 test oluşturalım
        List<String> testList = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            testList.add("Test " + i);
        }

        // Adapter'ı bağla
        testAdapter = new TestAdapter(testList, bookId, dbHelper);
        rvTests.setAdapter(testAdapter);
    }
}