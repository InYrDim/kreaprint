package com.example.kreaprint.model;

public class ProductSection {
    public static final int HEADER = 0;
    public static final int PRODUCT_ITEM = 1;
    public static final int SPINNER_SECTION = 2;

    private int type;
    private String headerTitle;
    private Product product;

    public ProductSection(int type, String headerTitle) {
        this.type = type;
        this.headerTitle = headerTitle;
    }

    public ProductSection(int type, Product product) {
        this.type = type;
        this.product = product;
    }

    public int getType() { return type; }
    public String getHeaderTitle() { return headerTitle; }
    public Product getProduct() { return product; }
}
