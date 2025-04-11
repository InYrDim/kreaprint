package com.example.kreaprint.helper.firebase;

public class RepositoryFactory {
    public static UserRepository getUserRepository() {
        return new UserRepository();
    }

}