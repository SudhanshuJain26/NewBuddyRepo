package indwin.c3.shareapp.models;

/**
 * Created by rock on 6/5/16.
 */
public class FrontBackImage {

    private String imgUrl;
    private boolean isVerified;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
