package com.yamidev.drinkfinder.model;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.yamidev.drinkfinder.local.AppDatabase; // ajusta el package a tu proyecto

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommentsRepository {

    private static volatile CommentsRepository INSTANCE;

    private final CommentDAO commentDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private CommentsRepository(CommentDAO commentDao) {
        this.commentDao = commentDao;
    }

    public static CommentsRepository getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (CommentsRepository.class) {
                if (INSTANCE == null) {
                    AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
                    INSTANCE = new CommentsRepository(db.commentDao());
                }
            }
        }
        return INSTANCE;
    }

    public LiveData<List<CommentEntity>> getCommentsForDrink(String drinkId) {
        return commentDao.getCommentsForDrink(drinkId);
    }

    public void insert(final CommentEntity comment) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                commentDao.insert(comment);
            }
        });
    }

    public void update(final CommentEntity comment) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                commentDao.update(comment);
            }
        });
    }

    public void delete(final CommentEntity comment) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                commentDao.delete(comment);
            }
        });
    }

    public void deleteAllForDrink(final String drinkId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                commentDao.deleteAllForDrink(drinkId);
            }
        });
    }
}