package com.beykent.akillitestanaliz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    // Değişkenleri en üstte tanımlıyoruz
    private TextView chipTYT, chipAYT, chipLGS;
    private TextView txtSummaryMain, txtSummarySub;
    private String selectedExam = TimePrefs.EXAM_TYT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // 1. Görünümleri Bağla (Bind views)
        chipTYT = findViewById(R.id.chipTYT);
        chipAYT = findViewById(R.id.chipAYT);
        chipLGS = findViewById(R.id.chipLGS);
        txtSummaryMain = findViewById(R.id.txtSummaryMain);
        txtSummarySub = findViewById(R.id.txtSummarySub);

        // 2. Modül Kartlarını Bağla
        View cardSummary = findViewById(R.id.cardSummary);
        View cardBooks = findViewById(R.id.cardBooks);
        View cardTests = findViewById(R.id.cardTests);
        View cardWrongBox = findViewById(R.id.cardWrongBox);
        View cardReports = findViewById(R.id.cardReports);

        // 3. Tıklama Olayları
        cardSummary.setOnClickListener(v -> {
            try {
                // ExamTimeSettingActivity isminin dosya isminle BİREBİR aynı olduğundan emin ol
                Intent intent = new Intent(DashboardActivity.this, ExamTimeSettingActivity.class);
                intent.putExtra("exam", selectedExam);
                startActivity(intent);
            } catch (Exception e) {
                // Eğer hala kapanıyorsa hatayı ekranda görmek için:
                Toast.makeText(this, "Hata: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        cardBooks.setOnClickListener(v -> startActivity(new Intent(this, BooksActivity.class)));
        cardTests.setOnClickListener(v -> startActivity(new Intent(this, TestsActivity.class)));
        cardWrongBox.setOnClickListener(v -> startActivity(new Intent(this, WrongBoxActivity.class)));
        cardReports.setOnClickListener(v -> startActivity(new Intent(this, ReportsActivity.class)));

        // Sınav Seçimleri
        chipTYT.setOnClickListener(v -> selectExam(TimePrefs.EXAM_TYT));
        chipAYT.setOnClickListener(v -> selectExam(TimePrefs.EXAM_AYT));
        chipLGS.setOnClickListener(v -> selectExam(TimePrefs.EXAM_LGS));

        highlightSelected();
        refreshSummary();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshSummary();
        highlightSelected();
    }

    private void selectExam(String exam) {
        selectedExam = exam;
        highlightSelected();
        refreshSummary();
    }

    private void highlightSelected() {
        chipTYT.setAlpha(TimePrefs.EXAM_TYT.equals(selectedExam) ? 1f : 0.45f);
        chipAYT.setAlpha(TimePrefs.EXAM_AYT.equals(selectedExam) ? 1f : 0.45f);
        chipLGS.setAlpha(TimePrefs.EXAM_LGS.equals(selectedExam) ? 1f : 0.45f);
    }

    private void refreshSummary() {
        if (selectedExam == null) selectedExam = TimePrefs.EXAM_TYT;
        int total = TimePrefs.getTotalMinutes(this, selectedExam);
        Map<String, Integer> lessons = TimePrefs.getLessonMinutes(this, selectedExam);

        StringBuilder details = new StringBuilder();
        for (Map.Entry<String, Integer> e : lessons.entrySet()) {
            details.append(TimePrefs.label(e.getKey()))
                    .append(": ")
                    .append(e.getValue())
                    .append(" dk, ");
        }

        if (details.length() >= 2) {
            details.setLength(details.length() - 2);
        }

        txtSummaryMain.setText(selectedExam + " toplam hedef: " + total + " dk");
        txtSummarySub.setText(details.toString());
    }
}

