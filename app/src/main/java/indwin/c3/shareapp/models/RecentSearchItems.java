package indwin.c3.shareapp.models;

import java.io.Serializable;

/**
 * Created by sudhanshu on 3/6/16.
 */
public class RecentSearchItems implements Serializable {
    String seller;
    String price;
    String title;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    String productId;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public RecentSearchItems(String seller, String price, String title,String productId) {
        this.seller = seller;
        this.price = price;
        this.title = title;
        this.productId = productId;
    }

    public RecentSearchItems() {

    }
}
