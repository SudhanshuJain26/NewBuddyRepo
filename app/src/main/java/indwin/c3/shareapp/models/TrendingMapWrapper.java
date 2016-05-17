package indwin.c3.shareapp.models;

import java.util.HashMap;

/**
 * Created by shubhang on 24/04/16.
 */
public class TrendingMapWrapper {
    HashMap<String, HashMap<String, String>> image;
    HashMap<String, HashMap<String, String>> mrp1;
    HashMap<String, HashMap<String, String>> fkid1;
    HashMap<String, HashMap<String, String>> title;
    HashMap<String, HashMap<String, String>> sellers;
    HashMap<String, HashMap<String, String>> selling;
    HashMap<String, HashMap<String, String>> category;
    HashMap<String, HashMap<String, String>> subCategory;
    HashMap<String, HashMap<String, String>> brand;

    public HashMap<String, HashMap<String, String>> getImage() {
        return image;
    }

    public void setImage(HashMap<String, HashMap<String, String>> image) {
        this.image = image;
    }

    public HashMap<String, HashMap<String, String>> getMrp1() {
        return mrp1;
    }

    public void setMrp1(HashMap<String, HashMap<String, String>> mrp1) {
        this.mrp1 = mrp1;
    }

    public HashMap<String, HashMap<String, String>> getFkid1() {
        return fkid1;
    }

    public void setFkid1(HashMap<String, HashMap<String, String>> fkid1) {
        this.fkid1 = fkid1;
    }

    public HashMap<String, HashMap<String, String>> getTitle() {
        return title;
    }

    public void setTitle(HashMap<String, HashMap<String, String>> title) {
        this.title = title;
    }

    public HashMap<String, HashMap<String, String>> getSellers() {
        return sellers;
    }

    public void setSellers(HashMap<String, HashMap<String, String>> sellers) {
        this.sellers = sellers;
    }

    public HashMap<String, HashMap<String, String>> getSelling() {
        return selling;
    }

    public void setSelling(HashMap<String, HashMap<String, String>> selling) {
        this.selling = selling;
    }

    public HashMap<String, HashMap<String, String>> getCategory() {
        return category;
    }

    public void setCategory(HashMap<String, HashMap<String, String>> category) {
        this.category = category;
    }

    public HashMap<String, HashMap<String, String>> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(HashMap<String, HashMap<String, String>> subCategory) {
        this.subCategory = subCategory;
    }

    public HashMap<String, HashMap<String, String>> getBrand() {
        return brand;
    }

    public void setBrand(HashMap<String, HashMap<String, String>> brand) {
        this.brand = brand;
    }
}
