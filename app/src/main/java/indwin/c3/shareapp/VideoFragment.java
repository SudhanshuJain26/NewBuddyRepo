package indwin.c3.shareapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

/**
 * Created by sudhanshu on 12/7/16.
 */
public class VideoFragment extends Fragment {
    private int position;
    private Context context;
    private VideoView mVideoView;
    int videoResource;
    private boolean _hasLoadedOnce = false;
    private boolean loadUrls;
    private ViewPager viewPager;
    View placeholder;

    public VideoFragment() {
    }

    @SuppressLint("ValidFragment")
    public VideoFragment(int position, ViewPager viewPager) {
        this.position = position;
        this.viewPager = viewPager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager_item, null);
        mVideoView = (VideoView) view.findViewById(R.id.videoView);
        placeholder = (View) view.findViewById(R.id.placeholder);
        videoResource = 0;

        if (position == 0) {
//
            videoResource = R.raw.first_screen;
        } else if (position == 1) {


            videoResource = R.raw.second_screen;
        } else if (position == 2) {


            videoResource = R.raw.third_screen;
        }

        Uri videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + videoResource);


        mVideoView.setVideoURI(videoUri);
        mVideoView.start();
        if (position != 0) {
//            mVideoView.seekTo(500);
            mVideoView.pause();
        }
        return view;
    }

    public void stopVideo() {
//        mVideoView.seekTo(0);
        mVideoView.stopPlayback();
    }

    public void startVideo(int position) {
        videoResource = 0;
        if (position == 0) {
            videoResource = R.raw.first_screen;
            placeholder.setBackgroundResource(R.drawable.screen2);
        } else if (position == 1) {
            videoResource = R.raw.second_screen;
            placeholder.setBackgroundResource(R.drawable.screen2);
        } else if (position == 2) {
            videoResource = R.raw.third_screen;
            placeholder.setBackgroundResource(R.drawable.screen3);
        }
        Uri videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + videoResource);


        mVideoView.setVideoURI(videoUri);
        if (viewPager.getCurrentItem() == position) {
//            mVideoView.seekTo(0);
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {


                    mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mp, int what, int extra) {
                            Log.d("TAG", "onInfo, what = " + what);
                            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                                // video started; hide the placeholder.
                                placeholder.setVisibility(View.GONE);
                                return true;
                            }
                            return false;
                        }


                    });

                    mVideoView.start();

                }
            });
        }
    }
}
