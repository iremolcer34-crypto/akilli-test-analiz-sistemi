package com.beykent.akillitestanaliz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    private List<String> testList;
    private int bookId;
    private DatabaseHelper dbHelper;

    public TestAdapter(List<String> testList, int bookId, DatabaseHelper dbHelper) {
        this.testList = testList;
        this.bookId = bookId;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_row, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        String testName = testList.get(position);
        int testNumber = position + 1; // Test 1, Test 2...

        holder.txtTestNo.setText(testName);

        // --- VERİTABANINDAN KONTROL ET (Tik görünsün mü?) ---
        boolean isSolved = dbHelper.isTestSolved(bookId, testNumber);
        holder.imgStatusTik.setVisibility(isSolved ? View.VISIBLE : View.GONE);

        // --- TIKLAMA OLAYI ---
        holder.itemView.setOnClickListener(v -> {
            // Durumu tersine çevir (Çözüldüyse çözülmedi yap, tam tersi)
            boolean currentStatus = dbHelper.isTestSolved(bookId, testNumber);
            dbHelper.updateTestStatus(bookId, testNumber, !currentStatus);

            // Görünümü tazele
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public static class TestViewHolder extends RecyclerView.ViewHolder {
        TextView txtTestNo;
        ImageView imgStatusTik;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTestNo = itemView.findViewById(R.id.txtTestNo);
            imgStatusTik = itemView.findViewById(R.id.imgStatusTik);
        }
    }
}
