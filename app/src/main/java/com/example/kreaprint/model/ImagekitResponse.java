package com.example.kreaprint.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class ImagekitResponse {
    @SerializedName("fileId")
    private String fileId;

    @SerializedName("type")
    private String type;

    @SerializedName("name")
    private String name;

    @SerializedName("filePath")
    private String filePath;

    @SerializedName("tags")
    private List<String> tags;

    @SerializedName("AITags")
    private List<AITag> aiTags;

    @SerializedName("versionInfo")
    private VersionInfo versionInfo;

    @SerializedName("isPrivateFile")
    private boolean isPrivateFile;

    @SerializedName("isPublished")
    private boolean isPublished;

    @SerializedName("customCoordinates")
    private String customCoordinates;

    @SerializedName("url")
    private String url;

    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("fileType")
    private String fileType;

    @SerializedName("mime")
    private String mime;

    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;

    @SerializedName("size")
    private int size;

    @SerializedName("hasAlpha")
    private boolean hasAlpha;

    @SerializedName("customMetadata")
    private Map<String, String> customMetadata;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    // Getters
    public String getFileId() {
        return fileId;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<AITag> getAiTags() {
        return aiTags;
    }

    public VersionInfo getVersionInfo() {
        return versionInfo;
    }

    public boolean isPrivateFile() {
        return isPrivateFile;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public String getCustomCoordinates() {
        return customCoordinates;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getFileType() {
        return fileType;
    }

    public String getMime() {
        return mime;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSize() {
        return size;
    }

    public boolean isHasAlpha() {
        return hasAlpha;
    }

    public Map<String, String> getCustomMetadata() {
        return customMetadata;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    // Inner Classes for Nested JSON
    public static class AITag {
        @SerializedName("name")
        private String name;

        @SerializedName("confidence")
        private double confidence;

        @SerializedName("source")
        private String source;

        public String getName() {
            return name;
        }

        public double getConfidence() {
            return confidence;
        }

        public String getSource() {
            return source;
        }
    }

    public static class VersionInfo {
        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
