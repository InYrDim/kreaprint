package com.example.kreaprint.model;


public class ImageResponse {
    public int status_code;
    public ImageData image;

    public static class ImageData {
        public String url;
    }
}

