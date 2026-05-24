package com.beykent.akillitestanaliz;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> bookList;

    public BookAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_card, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.txtBookName.setText(book.getName());
        holder.txtSubject.setText(book.getSubject());
        holder.progressBar.setProgress(book.getProgress());
        holder.txtPercent.setText("%" + book.getProgress());

        // --- KARTI AÇMA KODU BURASI ---
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), BookDetailActivity.class);
            // Hangi kitabın açılacağını ID üzerinden gönderiyoruz
            intent.putExtra("BOOK_ID", book.getId());
            intent.putExtra("BOOK_NAME", book.getName());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView txtBookName, txtSubject, txtPercent;
        ProgressBar progressBar;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBookName = itemView.findViewById(R.id.txtBookName);
            txtSubject = itemView.findViewById(R.id.txtSubject);
            txtPercent = itemView.findViewById(R.id.txtProgressPercent);
            progressBar = itemView.findViewById(R.id.bookProgressBar);
        }
    }
}