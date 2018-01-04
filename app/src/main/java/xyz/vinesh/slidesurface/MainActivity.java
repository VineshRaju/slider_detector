package xyz.vinesh.slidesurface;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import xyz.vinesh.SlideGestureDetector.SlideGestureDetector;
import xyz.vinesh.SlideGestureDetector.SlideGestureListener;

public class MainActivity extends AppCompatActivity {
    private final static int MAX_VOLUME = 15, MIN_VOLUME = 0;
    private final String TAG = MainActivity.class.getSimpleName();
    private SlideGestureDetector detector;
    private AudioManager audioManager;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.inteceptEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        detector = new SlideGestureDetector(this, new SlideGestureListener() {
            @Override
            public void onUp(float amount) {
                Log.d(TAG, "Up by " + amount + "%");
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                int volumeToRaise = (int) Math.ceil(MAX_VOLUME * amount / 100);
                if (currentVolume != MAX_VOLUME) {
                    int volume = (currentVolume + volumeToRaise) > 15 ? 15 : currentVolume + volumeToRaise;
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
                }
            }

            @Override
            public void onDown(float amount) {
                Log.d(TAG, "Down by " + amount + "%");
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                int volumeToLower = (int) Math.ceil(MAX_VOLUME * amount / 100);
                if (currentVolume != MIN_VOLUME) {
                    int volume = (currentVolume - volumeToLower) > 15 ? 15 : currentVolume - volumeToLower;
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
                }
            }

            @Override
            public void onLeft(float amount) {
                Log.d(TAG, "Left by " + amount + "%");
            }

            @Override
            public void onRight(float amount) {
                Log.d(TAG, "Right by " + amount + "%");
            }
        });
    }
}
