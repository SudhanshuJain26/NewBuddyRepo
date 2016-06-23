package indwin.c3.shareapp;

/**
 * Created by sudhanshu on 23/6/16.
 */

        
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;
        

public class VideoPagerAdapter extends PagerAdapter {

    Context context;
    static int[] arrayvid;
    private VideoView videoView;
    LayoutInflater inflater;

    public VideoPagerAdapter(Context context, int[] arrayvid) {
        this.context = context;
        this.arrayvid = arrayvid;
    }

            @Override
    public int getCount() {
            return arrayvid.length;
        }

            @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container,
                        false);

                videoView = (VideoView) itemView.findViewById(R.id.video_view);

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                                mp.setLooping(true);
                            }
                    });
            MediaController mediaController = new MediaController(context, false);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            ((ViewPager) container).addView(itemView);
            return null;
        }

            @Override
    public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }
    public void play(int position) {
        videoView.setVideoURI(Uri
                        .parse("android.resource://indwin.c3.shareapp/"+arrayvid[position]));
        videoView.requestFocus();
        }
}
