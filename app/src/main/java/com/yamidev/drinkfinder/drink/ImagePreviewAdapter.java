package com.yamidev.drinkfinder.drink;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.yamidev.drinkfinder.R;
import java.util.ArrayList;
import java.util.List;

public class ImagePreviewAdapter extends RecyclerView.Adapter<ImagePreviewAdapter.PreviewVH> {

    public interface OnImageRemoveListener {
        void onImageRemoved(int position);
    }

    private final List<Uri> imageUris = new ArrayList<>();
    private OnImageRemoveListener listener;

    public void setOnImageRemoveListener(OnImageRemoveListener listener) {
        this.listener = listener;
    }

    public void addImage(Uri uri) {
        imageUris.add(uri);
        notifyItemInserted(imageUris.size() - 1);
    }

    public void removeImage(int position) {
        imageUris.remove(position);
        notifyItemRemoved(position);
    }

    public List<Uri> getImageUris() {
        return imageUris;
    }

    @NonNull @Override
    public PreviewVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_preview, parent, false);
        return new PreviewVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewVH holder, int position) {
        holder.bind(imageUris.get(position));
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    class PreviewVH extends RecyclerView.ViewHolder {
        ImageView imgPreview;
        ImageButton btnRemove;

        PreviewVH(@NonNull View itemView) {
            super(itemView);
            imgPreview = itemView.findViewById(R.id.img_preview);
            btnRemove = itemView.findViewById(R.id.btn_remove_image);
            btnRemove.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onImageRemoved(getAdapterPosition());
                }
            });
        }

        void bind(Uri uri) {
            Glide.with(itemView.getContext()).load(uri).into(imgPreview);
        }
    }
}