package com.ultramega.shop.pojo;

public class ImageSlider {
    private String ImageName;
    private String ImagePath;

    public ImageSlider(String imageName, String imagePath) {
        ImageName = imageName;
        ImagePath = imagePath;
    }

    public String getImageName() {
        return ImageName;
    }

    public String getImagePath() {
        return ImagePath;
    }
}
