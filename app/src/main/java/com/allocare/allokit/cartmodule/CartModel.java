package com.allocare.allokit.cartmodule;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.allocare.allokit.database.LanguageConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "cart")
public class CartModel {

    @PrimaryKey(autoGenerate = true)
    int primaryid;

    String id;
    String productName;
    String image ="";
    String quantity;
    String price;
    String date;
    String totalprice;
    String paytype;
    String orderStatus;
    String orderid;
    String locationLink;
    String address;
    String pincode;
    boolean isSelected;

    /*@TypeConverters(LanguageConverter.class)
    List<Prices> pricelist =new ArrayList<>();*/

    String pricelists;


    /*@TypeConverter
    public static List<Prices> storedStringToMyObjects(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Prices>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String myObjectsToStoredString(List<Prices> myObjects) {
        Gson gson = new Gson();
        return gson.toJson(myObjects);
    }*/


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public int getPrimaryid() {
        return primaryid;
    }

    public void setPrimaryid(int primaryid) {
        this.primaryid = primaryid;
    }

    public String getLocationLink() {
        return locationLink;
    }

    public void setLocationLink(String locationLink) {
        this.locationLink = locationLink;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPricelists() {
        return pricelists;
    }

    public void setPricelists(String pricelists) {
        this.pricelists = pricelists;
    }

    /* public List<Prices> getPricelist() {
        return pricelist;
    }

    public void setPricelist(List<Prices> pricelist) {
        this.pricelist = pricelist;
    }*/

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static class Prices
    {
        int price;
        int above;
        int below;

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getAbove() {
            return above;
        }

        public void setAbove(int above) {
            this.above = above;
        }

        public int getBelow() {
            return below;
        }

        public void setBelow(int below) {
            this.below = below;
        }
    }

}
