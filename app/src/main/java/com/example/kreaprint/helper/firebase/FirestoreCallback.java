package com.example.kreaprint.helper.firebase;

public interface FirestoreCallback<T> {
    void onSuccess(T result);
    void onError(Exception e);
}