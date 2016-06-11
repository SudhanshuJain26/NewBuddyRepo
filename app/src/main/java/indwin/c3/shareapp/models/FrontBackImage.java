package indwin.c3.shareapp.models;

/**
 * Created by rock on 6/5/16.
 */
public class FrontBackImage {

    private String imgUrl;
    private Boolean isVerified;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Boolean isVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }
}
