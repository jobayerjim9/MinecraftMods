package com.carotalz.minecraftmods.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ModModel implements Serializable {
    @SerializedName("images")
    private ArrayList<Images> images;
    @SerializedName("file")
    private String file;
    @SerializedName("title_def")
    private String title_def;
    @SerializedName("title_ru")
    private String title_ru;
    @SerializedName("desc_def")
    private String desc_def;
    @SerializedName("desc_ru")
    private String desc_ru;
    @SerializedName("favourite")
    private boolean favourite;

    public ModModel() {
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getImageString() {
        String temp="";
        for (Images image:images) {
            temp=temp.concat(image.getImage()+",");
        }
        return temp;
    }


    public ArrayList<Images> getImages() {
        return images;
    }

    public void setImages(ArrayList<Images> images) {
        this.images = images;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getTitle_def() {
        return title_def;
    }

    public void setTitle_def(String title_def) {
        this.title_def = title_def;
    }

    public String getTitle_ru() {
        return title_ru;
    }

    public void setTitle_ru(String title_ru) {
        this.title_ru = title_ru;
    }

    public String getDesc_def() {
        return desc_def;
    }

    public void setDesc_def(String desc_def) {
        this.desc_def = desc_def;
    }

    public String getDesc_ru() {
        return desc_ru;
    }

    public void setDesc_ru(String desc_ru) {
        this.desc_ru = desc_ru;
    }

    @NonNull
    @Override
    public String toString() {
        String temp="";
        for (Images image:images) {
            temp=temp.concat(" [ "+image.getImage()+" ],");
        }
        temp=temp.concat("file: "+file+" title_def: "+title_def+" title_ru: "+title_ru+" dec_def: "+desc_def+" desc_ru: "+desc_ru);
        return temp;
    }
    public class Images implements Serializable {
        @SerializedName("image")
        private String image;

        public Images() {
        }

        public Images(String image) {
            this.image = image;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

}


