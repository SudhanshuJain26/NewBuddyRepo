package indwin.c3.shareapp.activities;

import android.os.Bundle;

import com.gun0912.tedpicker.ImagePickerActivity;

/**
 * Created by rock on 5/10/16.
 */
public class ImageHelperActivity extends ImagePickerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Choose an Image");
    }
}
