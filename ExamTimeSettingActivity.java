package com.beykent.akillitestanaliz;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExamTimeSettingActivity extends AppCompatActivity {

    private TextView txtTitle;
    private TextView lbl1, lbl2, lbl3, lbl4;
    private EditText edt1, edt2, edt3, edt4;

    private String exam;

    // lesson keys mapped to 4 fields
    private String k1, k2, k3, k4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_time_settings);

        exam = getIntent().getStringExtra("exam");
        if (TextUtils.isEmpty(exam)) exam = TimePrefs.EXAM_TYT;

        txtTitle = findViewById(R.id.txtExamTitle);

        lbl1 = findViewById(R.id.lbl1);
        lbl2 = findViewById(R.id.lbl2);
        lbl3 = findViewById(R.id.lbl3);
        lbl4 = findViewById(R.id.lbl4);

        edt1 = findViewById(R.id.edt1);
        edt2 = findViewById(R.id.edt2);
        edt3 = findViewById(R.id.edt3);
        edt4 = findViewById(R.id.edt4);

        txtTitle.setText("Ders Süreleri (" + exam + ")");

        setupForExam(exam);
        loadValues();

        findViewById(R.id.btnSaveTimes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveValues();
                Toast.makeText(ExamTimeSettingActivity.this, "Kaydedildi", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setupForExam(String exam) {
        if (TimePrefs.EXAM_AYT.equals(exam)) {
            k1 = TimePrefs.L_EDEB;
            k2 = TimePrefs.L_MAT2;
            k3 = TimePrefs.L_AYT_SOS;
            k4 = TimePrefs.L_AYT_FEN;

            lbl1.setText("Edebiyat (dk)");
            lbl2.setText("Matematik-2 (dk)");
            lbl3.setText("Sosyal (dk)");
            lbl4.setText("Fen (dk)");
        } else {
            // TYT + LGS
            k1 = TimePrefs.L_TURKCE;
            k2 = TimePrefs.L_MAT;
            k3 = TimePrefs.L_SOS;
            k4 = TimePrefs.L_FEN;

            lbl1.setText("Türkçe (dk)");
            lbl2.setText("Matematik (dk)");
            lbl3.setText("Sosyal (dk)");
            lbl4.setText("Fen (dk)");
        }
    }

    private void loadValues() {
        int d1, d2, d3, d4;

        if (TimePrefs.EXAM_TYT.equals(exam)) {
            d1 = TimePrefs.getMinutes(this, exam, k1, 55);
            d2 = TimePrefs.getMinutes(this, exam, k2, 75);
            d3 = TimePrefs.getMinutes(this, exam, k3, 25);
            d4 = TimePrefs.getMinutes(this, exam, k4, 35);
        } else if (TimePrefs.EXAM_AYT.equals(exam)) {
            d1 = TimePrefs.getMinutes(this, exam, k1, 60);
            d2 = TimePrefs.getMinutes(this, exam, k2, 80);
            d3 = TimePrefs.getMinutes(this, exam, k3, 40);
            d4 = TimePrefs.getMinutes(this, exam, k4, 40);
        } else {
            d1 = TimePrefs.getMinutes(this, exam, k1, 50);
            d2 = TimePrefs.getMinutes(this, exam, k2, 60);
            d3 = TimePrefs.getMinutes(this, exam, k3, 30);
            d4 = TimePrefs.getMinutes(this, exam, k4, 40);
        }

        edt1.setText(String.valueOf(d1));
        edt2.setText(String.valueOf(d2));
        edt3.setText(String.valueOf(d3));
        edt4.setText(String.valueOf(d4));
    }

    private void saveValues() {
        int v1 = parseIntSafe(edt1.getText().toString());
        int v2 = parseIntSafe(edt2.getText().toString());
        int v3 = parseIntSafe(edt3.getText().toString());
        int v4 = parseIntSafe(edt4.getText().toString());

        TimePrefs.putMinutes(this, exam, k1, v1);
        TimePrefs.putMinutes(this, exam, k2, v2);
        TimePrefs.putMinutes(this, exam, k3, v3);
        TimePrefs.putMinutes(this, exam, k4, v4);
    }

    private int parseIntSafe(String s) {
        try {
            if (TextUtils.isEmpty(s)) return 0;
            int v = Integer.parseInt(s.trim());
            if (v < 0) return 0;
            if (v > 300) return 300;
            return v;
        } catch (Exception e) {
            return 0;
        }
    }
}

