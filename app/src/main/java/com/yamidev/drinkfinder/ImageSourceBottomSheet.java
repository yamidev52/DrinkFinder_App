package com.yamidev.drinkfinder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ImageSourceBottomSheet extends BottomSheetDialogFragment {

    public interface ImageSourceListener {
        void onCameraSelected();
        void onGallerySelected();
    }

    private ImageSourceListener mListener;

    public void setImageSourceListener(ImageSourceListener listener) {
        mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_image_source, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView optionCamera = view.findViewById(R.id.option_camera);
        TextView optionGallery = view.findViewById(R.id.option_gallery);

        optionCamera.setOnClickListener(v -> {
            if (mListener != null) mListener.onCameraSelected();
            dismiss();
        });

        optionGallery.setOnClickListener(v -> {
            if (mListener != null) mListener.onGallerySelected();
            dismiss();
        });
    }
}