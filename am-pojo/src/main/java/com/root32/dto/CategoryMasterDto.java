package com.root32.dto;

import javax.persistence.Column;

public class CategoryMasterDto {

    @Column(unique = true)
    private String name;

    private String description;

    private Base64Image image;

    public void setImage(Base64Image image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Base64Image getImage() {
        return image;
    }

    public void setImages(Base64Image image) {
        this.image = image;
    }

}
