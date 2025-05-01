package com.example.foodplanner.model;

import java.util.List;

public class CategoryResponse {
    private List<Category> categories;

    public CategoryResponse() {
    }

    public CategoryResponse(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public static class Category {
        private String idCategory;
        private String strCategory;
        private String strCategoryThumb;
        private String strCategoryDescription;

        public Category() {
        }

        public Category(String idCategory, String strCategory, String strCategoryThumb, String strCategoryDescription) {
            this.idCategory = idCategory;
            this.strCategory = strCategory;
            this.strCategoryThumb = strCategoryThumb;
            this.strCategoryDescription = strCategoryDescription;
        }

        // Getters and Setters
        public String getIdCategory() {
            return idCategory;
        }

        public void setIdCategory(String idCategory) {
            this.idCategory = idCategory;
        }

        public String getStrCategory() {
            return strCategory;
        }

        public void setStrCategory(String strCategory) {
            this.strCategory = strCategory;
        }

        public String getStrCategoryThumb() {
            return strCategoryThumb;
        }

        public void setStrCategoryThumb(String strCategoryThumb) {
            this.strCategoryThumb = strCategoryThumb;
        }

        public String getStrCategoryDescription() {
            return strCategoryDescription;
        }

        public void setStrCategoryDescription(String strCategoryDescription) {
            this.strCategoryDescription = strCategoryDescription;
        }
    }
}
