package indwin.c3.shareapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rock on 6/24/16.
 */
public class ResponseModel {
    private String status;

    private List<Error> errors = new ArrayList<>();
    private UserModelServer data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public UserModelServer getData() {
        return data;
    }

    public void setData(UserModelServer data) {
        this.data = data;
    }
}

