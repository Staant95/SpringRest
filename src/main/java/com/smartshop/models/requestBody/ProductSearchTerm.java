package com.smartshop.models.requestBody;


public class ProductSearchTerm {

    private String searchTerm;

    public ProductSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public ProductSearchTerm() {
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}
