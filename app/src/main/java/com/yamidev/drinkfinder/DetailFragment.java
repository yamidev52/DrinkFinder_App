package com.yamidev.drinkfinder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yamidev.drinkfinder.model.CommentAdapter;
import com.yamidev.drinkfinder.drink.DrinkRepository;
import com.yamidev.drinkfinder.drink.ImagePreviewAdapter;
import com.yamidev.drinkfinder.model.Comment;
import com.yamidev.drinkfinder.utils.AppNotifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DetailFragment extends Fragment {

    private ImageView imgDrinkDetail;
    private TextView tvCategoryDetail, tvInstructionsDetail, tvIngredientsDetail;
    private CollapsingToolbarLayout collapsingToolbar;
    private FloatingActionButton fabShare, fabFavorite;
    private RecyclerView rvComments;
    private CommentAdapter commentAdapter;
    private EditText etNewComment;
    private ImageButton btnSendComment;

    private DrinkRepository repo;
    private Drink currentDrink;
    private boolean isCurrentlyFavorite = false;
    private RecyclerView rvImagePreviews;
    private ImagePreviewAdapter imagePreviewAdapter;
    private ImageButton btnAttachPhoto;
    private Uri tempImageUri;

    private ActivityResultLauncher<String> requestCameraPermissionLauncher;
    private ActivityResultLauncher<Uri> takePictureLauncher;
    private ActivityResultLauncher<String> pickImageLauncher;

    private MaterialButton btnRemindLater;
    private AppNotifier appNotifier;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    sendDrinkNotification();
                } else {
                    Toast.makeText(requireContext(), "Permiso de notificaciones denegado.", Toast.LENGTH_SHORT).show();
                }
            });


    public DetailFragment() {
        super(R.layout.fragment_detail);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.move));
        setupActivityLaunchers();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgDrinkDetail = view.findViewById(R.id.img_drink_detail);
        tvCategoryDetail = view.findViewById(R.id.tv_category_detail);
        tvInstructionsDetail = view.findViewById(R.id.tv_instructions_detail);
        tvIngredientsDetail = view.findViewById(R.id.tv_ingredients_detail);
        collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
        fabShare = view.findViewById(R.id.fab_share);
        fabFavorite = view.findViewById(R.id.fab_favorite);
        rvComments = view.findViewById(R.id.rv_comments);
        etNewComment = view.findViewById(R.id.et_new_comment);
        btnSendComment = view.findViewById(R.id.btn_send_comment);

        rvImagePreviews = view.findViewById(R.id.rv_image_previews);
        btnAttachPhoto = view.findViewById(R.id.btn_attach_photo);

        appNotifier = new AppNotifier(requireContext());
        btnRemindLater = view.findViewById(R.id.btn_remind_later);

        btnRemindLater.setOnClickListener(v -> {
            checkNotificationPermissionAndSend();
        });

        setupImagePreview();

        btnAttachPhoto.setOnClickListener(v -> showImageSourceDialog());

        repo = new DrinkRepository(requireContext());
        commentAdapter = new CommentAdapter();
        rvComments.setAdapter(commentAdapter);

        Toolbar toolbar = view.findViewById(R.id.toolbar_detail);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);

        NavController navController = NavHostFragment.findNavController(this);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(collapsingToolbar, toolbar, navController, appBarConfiguration);

        if (getArguments() != null) {
            String drinkId = getArguments().getString("drinkId");
            if (drinkId != null && !drinkId.isEmpty()) {
                imgDrinkDetail.setTransitionName("image_" + drinkId);
                fetchDrinkDetails(drinkId);
                observeFavoriteStatus(drinkId);
                observeComments(drinkId);
                btnSendComment.setOnClickListener(v -> handleSendComment(drinkId));
            } else {
                showError("No se recibió un ID de bebida.");
            }
        }

        fabShare.setOnClickListener(v -> {
            if (currentDrink != null) {
                shareDrinkWithImage(requireContext(), currentDrink);
            }
        });

        fabFavorite.setOnClickListener(v -> {
            if (currentDrink != null) {
                if (isCurrentlyFavorite) {
                    repo.deleteFavoriteDrink(currentDrink.getId());
                } else {
                    repo.saveFavoriteDrink(currentDrink);
                    appNotifier.show("Favorito agregado", "Guardaste el Mojito en tus favoritos");
                }
            }
        });
    }

    private void observeComments(String drinkId) {
        repo.getCommentsForDrink(drinkId).observe(getViewLifecycleOwner(), comments -> {
            if (isAdded()) {
                commentAdapter.setComments(comments);
            }
        });
    }

    private void handleSendComment(String drinkId) {
        String commentText = etNewComment.getText().toString().trim();
        if (commentText.isEmpty()) {
            Toast.makeText(requireContext(), "El comentario no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1) Obtener la URI de la PRIMERA imagen (si hay)
        String imageUriString = null;
        if (imagePreviewAdapter != null) {
            List<Uri> uris = imagePreviewAdapter.getImageUris();
            if (uris != null && !uris.isEmpty()) {
                // 👇 aquí SOLO usamos get(0) si la lista NO está vacía
                imageUriString = uris.get(0).toString();
            }
        }

        // 2) Crear el Comment usando TU clase de dominio
        long now = System.currentTimeMillis();
        String username = "Tú (Frontend)";

        Comment newComment = new Comment(
                drinkId,        // id de la bebida
                commentText,    // texto del comentario
                username,       // nombre de usuario
                now,            // timestamp
                imageUriString  // uri de la imagen o null
        );

        // 3) Guardar el comentario en el repositorio
        repo.addComment(drinkId, commentText, username, imageUriString);

        // 4) Limpiar UI
        etNewComment.setText("");

        if (imagePreviewAdapter != null) {
            // Vaciar las imágenes del preview sin reventar índices
            while (imagePreviewAdapter.getItemCount() > 0) {
                imagePreviewAdapter.removeImage(0);
            }
            rvImagePreviews.setVisibility(View.GONE);
        }
    }

    private void fetchDrinkDetails(String id) {
        repo.getById(id, new DrinkRepository.Result<Drink>() {
            @Override
            public void onSuccess(Drink drink) {
                if (drink != null && isAdded()) {
                    currentDrink = drink;
                    bindDataToViews(drink);
                } else {
                    showError("No se encontraron detalles para esta bebida.");
                }
            }

            @Override
            public void onError(Throwable t) {
                showError("Error al cargar los detalles: " + t.getMessage());
            }
        });
    }

    private void observeFavoriteStatus(String id) {
        repo.isFavorite(id).observe(getViewLifecycleOwner(), isFavorite -> {
            isCurrentlyFavorite = isFavorite;
            if (isFavorite) {
                fabFavorite.setImageResource(R.drawable.ic_favorite_filled);
            } else {
                fabFavorite.setImageResource(R.drawable.ic_favorite_border);
            }
        });
    }

    private void bindDataToViews(Drink drink) {
        collapsingToolbar.setTitle(drink.getName());
        tvCategoryDetail.setText(drink.getCategory());
        tvInstructionsDetail.setText(drink.getInstructions());

        StringBuilder ingredientsText = new StringBuilder();
        for (String ingredient : drink.getIngredients()) {
            ingredientsText.append("• ").append(ingredient).append("\n");
        }
        tvIngredientsDetail.setText(ingredientsText.toString().trim());

        Glide.with(requireContext())
                .load(drink.getThumbnail())
                .into(imgDrinkDetail);
    }

    private void showError(String message) {
        if (isAdded()) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
        }
        Log.e("DetailFragment", message);
    }

    private void shareDrinkWithImage(Context ctx, Drink drink) {
        String body = buildShareText(drink);
        Glide.with(ctx)
                .asBitmap()
                .load(drink.getThumbnail())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            Uri uri = saveBitmapToCache(ctx, resource, drink.getName());
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/*");
                            share.putExtra(Intent.EXTRA_STREAM, uri);
                            share.putExtra(Intent.EXTRA_TEXT, body);
                            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            ctx.startActivity(Intent.createChooser(share, "Compartir receta…"));
                        } catch (IOException e) {
                            shareDrinkText(ctx, drink, body);
                        }
                    }

                    @Override public void onLoadCleared(@Nullable Drawable placeholder) {}
                    @Override public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        shareDrinkText(ctx, drink, body);
                    }
                });
    }

    private void shareDrinkText(Context ctx, Drink drink, String body) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, drink.getName());
        i.putExtra(Intent.EXTRA_TEXT, body);
        ctx.startActivity(Intent.createChooser(i, "Compartir receta…"));
    }

    private String buildShareText(Drink d) {
        return "🍹 " + d.getName() + "\n" +
                "Categoría: " + d.getCategory() + "\n" +
                "Ingredientes:\n" + String.join("\n", d.getIngredients()) + "\n\n" +
                "Instrucciones:\n" + d.getInstructions() + "\n\n" +
                "#DrinkFinderApp";
    }

    private Uri saveBitmapToCache(Context ctx, Bitmap bmp, String name) throws IOException {
        File imagesFolder = new File(ctx.getCacheDir(), "images");
        if (!imagesFolder.exists()) imagesFolder.mkdirs();
        File file = new File(imagesFolder, name.replaceAll("\\s+", "_") + ".jpg");
        FileOutputStream stream = new FileOutputStream(file);
        bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream);
        stream.flush();
        stream.close();
        return androidx.core.content.FileProvider.getUriForFile(
                ctx, ctx.getPackageName() + ".fileprovider", file
        );
    }

    private void setupActivityLaunchers() {
        // Launcher para pedir permiso de cámara
        requestCameraPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                launchCamera();
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        });

        // Launcher para tomar una foto
        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), isSuccess -> {
            if (isSuccess && tempImageUri != null) {
                addImageToPreview(tempImageUri);
            }
        });

        // Launcher para seleccionar de la galería
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                addImageToPreview(uri);
            }
        });
    }

    private void setupImagePreview() {
        imagePreviewAdapter = new ImagePreviewAdapter();
        rvImagePreviews.setAdapter(imagePreviewAdapter);
        imagePreviewAdapter.setOnImageRemoveListener(position -> {
            imagePreviewAdapter.removeImage(position);
            rvImagePreviews.setVisibility(imagePreviewAdapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);
        });
    }

    private void showImageSourceDialog() {
        ImageSourceBottomSheet bottomSheet = new ImageSourceBottomSheet();
        bottomSheet.setImageSourceListener(new ImageSourceBottomSheet.ImageSourceListener() {
            @Override
            public void onCameraSelected() {
                checkCameraPermissionAndLaunch();
            }

            @Override
            public void onGallerySelected() {
                pickImageLauncher.launch("image/*");
            }
        });
        bottomSheet.show(getParentFragmentManager(), "ImageSourceBottomSheet");
    }

    private void checkCameraPermissionAndLaunch() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void launchCamera() {
        File tempImageFile = new File(requireContext().getCacheDir(), "temp_image.jpg");
        tempImageUri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".fileprovider", tempImageFile);
        takePictureLauncher.launch(tempImageUri);
    }

    private void addImageToPreview(Uri uri) {
        if (imagePreviewAdapter.getItemCount() < 3) {
            imagePreviewAdapter.addImage(uri);
            rvImagePreviews.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(requireContext(), "Puedes adjuntar hasta 3 imágenes", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkNotificationPermissionAndSend() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                sendDrinkNotification();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        } else {
            sendDrinkNotification();
        }
    }

    private void sendDrinkNotification() {
        if (currentDrink != null) {
            appNotifier.show(currentDrink.getId(), currentDrink.getName());
        } else {
            Toast.makeText(requireContext(), "No se puede crear el recordatorio.", Toast.LENGTH_SHORT).show();
        }
    }

}