package indwin.c3.shareapp.models;

import java.util.List;
import java.util.Map;

/**
 * Created by rock on 6/4/16.
 */
public class Image {

    private List<String> validImgUrls;
    private List<String> invalidImgUrls;
    private List<String> imgUrls;
    private Map<String, String> newImgUrls;
    private boolean isUpdateNewImgUrls;

    private FrontBackImage back;
    private boolean isUpdateFront;
    private String frontStatus;

    private FrontBackImage front;
    private boolean isUpdateBack;
    private String backStatus;

    public List<String> getValidImgUrls() {
        return validImgUrls;
    }

    public void setValidImgUrls(List<String> validImgUrls) {
        this.validImgUrls = validImgUrls;
    }

    public List<String> getInvalidImgUrls() {
        return invalidImgUrls;
    }

    public void setInvalidImgUrls(List<String> invalidImgUrls) {
        this.invalidImgUrls = invalidImgUrls;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public Map<String, String> getNewImgUrls() {
        return newImgUrls;
    }

    public void setNewImgUrls(Map<String, String> newImgUrls) {
        this.newImgUrls = newImgUrls;
    }

    public boolean isUpdateNewImgUrls() {
        return isUpdateNewImgUrls;
    }

    public void setUpdateNewImgUrls(boolean updateNewImgUrls) {
        isUpdateNewImgUrls = updateNewImgUrls;
    }

    public FrontBackImage getBack() {
        return back;
    }

    public void setBack(FrontBackImage back) {
        this.back = back;
    }

    public boolean isUpdateFront() {
        return isUpdateFront;
    }

    public void setUpdateFront(boolean updateFront) {
        isUpdateFront = updateFront;
    }

    public String getFrontStatus() {
        return frontStatus;
    }

    public void setFrontStatus(String frontStatus) {
        this.frontStatus = frontStatus;
    }

    public FrontBackImage getFront() {
        return front;
    }

    public void setFront(FrontBackImage front) {
        this.front = front;
    }

    public boolean isUpdateBack() {
        return isUpdateBack;
    }

    public void setUpdateBack(boolean updateBack) {
        isUpdateBack = updateBack;
    }

    public String getBackStatus() {
        return backStatus;
    }

    public void setBackStatus(String backStatus) {
        this.backStatus = backStatus;
    }
}
