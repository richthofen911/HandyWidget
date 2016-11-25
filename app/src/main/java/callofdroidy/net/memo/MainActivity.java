package callofdroidy.net.memo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.vv_intro)
    VideoView vvIntro;

    MediaController mediaController;
    GestureDetector gestureDetector;
    @BindView(R.id.iv_cover_layer)
    ImageView ivCoverLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (vvIntro.isPlaying()) vvIntro.pause();
                ivCoverLayer.setVisibility(View.VISIBLE);
                return true;
            }
        });

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.test);
        vvIntro.setVideoURI(uri);
        vvIntro.seekTo(100);
        mediaController = new MediaController(this);
        mediaController.setMediaPlayer(vvIntro);

        ivCoverLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivCoverLayer.setVisibility(View.GONE);
                vvIntro.start();
                vvIntro.requestFocus();
            }
        });

        vvIntro.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });

    }
}
