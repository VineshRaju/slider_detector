package xyz.vinesh.SlideGestureDetector;

/**
 * Created by vineshraju on 4/11/16.
 */

public interface SlideGestureListener {
    void onUp(float amount);

    void onDown(float amount);

    void onLeft(float amount);

    void onRight(float amount);
}
