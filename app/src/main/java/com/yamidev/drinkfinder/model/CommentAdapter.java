package com.yamidev.drinkfinder.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yamidev.drinkfinder.R;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentVH> {
    private final List<Comment> comments = new ArrayList<>();

    public void setComments(List<Comment> newComments) {
        comments.clear();
        comments.addAll(newComments);
        notifyDataSetChanged();
    }

    public void addComment(Comment newComment) {
        comments.add(newComment);
        notifyItemInserted(comments.size() - 1);
    }

    @NonNull
    @Override
    public CommentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentVH holder, int position) {
        holder.bind(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentVH extends RecyclerView.ViewHolder {
        TextView tvAuthor;
        TextView tvText;

        public CommentVH(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tv_comment_author);
            tvText = itemView.findViewById(R.id.tv_comment_text);
        }

        void bind(Comment comment) {
            tvAuthor.setText(comment.getUsername());
            tvText.setText(comment.getText());
        }
    }
}