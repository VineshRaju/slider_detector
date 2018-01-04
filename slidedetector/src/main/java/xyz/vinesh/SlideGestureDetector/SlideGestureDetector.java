package xyz.vinesh.SlideGestureDetector;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by vineshraju on 4/11/16.
 */

public class SlideGestureDetector {
    private static final int RIGHT = 0, LEFT = 1, UP = 2, DOWN = 3, UNKNOWN = -1;
    private final static String TAG = SlideGestureDetector.class.getSimpleName();
    private final int screenHeight, screenWidth;
    private int currentDirection = UNKNOWN;
    private SimpleXY initialPosition = null, previousPosition, currentPosition;
    private SlideGestureListener listener = null;

    public SlideGestureDetector(Context context, SlideGestureListener listener) {
        this.listener = listener;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }

    public void inteceptEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                if (initialPosition == null) {
                    initialPosition = new SimpleXY(event.getX(), event.getY());
                    previousPosition = new SimpleXY(initialPosition);
                }
                break;
            case (MotionEvent.ACTION_UP):
                reset();
                break;
            case (MotionEvent.ACTION_MOVE):
                currentPosition = new SimpleXY(event.getX(), event.getY());
                float difference = 0, differencePercentage = 0;
                switch (getDirection(previousPosition, currentPosition)) {
                    case LEFT:
                        if (currentDirection != LEFT && currentDirection != UNKNOWN) {
                            initialPosition = new SimpleXY(currentPosition);
                            Log.d(TAG, "LEFT");
                        }
                        currentDirection = LEFT;
                        difference = initialPosition.getX() - currentPosition.getX();
                        differencePercentage = findPercentage(difference, screenWidth);
                        if (differencePercentage > 1)
                            listener.onLeft(differencePercentage);
                        break;
                    case RIGHT:
                        if (currentDirection != RIGHT && currentDirection != UNKNOWN) {
                            initialPosition = new SimpleXY(currentPosition);
                            Log.d(TAG, "RIGHT");
                        }
                        currentDirection = RIGHT;
                        difference = initialPosition.getX() - currentPosition.getX();
                        differencePercentage = findPercentage(difference, screenWidth);
                        if (differencePercentage > 1)
                            listener.onRight(differencePercentage);
                        break;
                    case UP:
                        if (currentDirection != UP && currentDirection != UNKNOWN) {
                            initialPosition = new SimpleXY(currentPosition);
                            Log.d(TAG, "UP");
                        }
                        currentDirection = UP;
                        difference = initialPosition.getY() - currentPosition.getY();
                        differencePercentage = findPercentage(difference, screenHeight);
                        if (differencePercentage > 1)
                            listener.onUp(differencePercentage);
                        break;
                    case DOWN:
                        if (currentDirection != DOWN && currentDirection != UNKNOWN) {
                            initialPosition = new SimpleXY(currentPosition);
                            Log.d(TAG, "DOWN");
                        }
                        currentDirection = DOWN;
                        difference = initialPosition.getY() - currentPosition.getY();
                        differencePercentage = findPercentage(difference, screenHeight);
                        if (differencePercentage > 1)
                            listener.onDown(differencePercentage);
                        break;
                }
                previousPosition = new SimpleXY(currentPosition);
                break;
        }
    }

    private int getDirection(SimpleXY previousPosition, SimpleXY currentPosition) {
        if (Math.abs(previousPosition.getX() - currentPosition.getX()) > Math.abs(previousPosition.getY() - currentPosition.getY())) {
            if (previousPosition.getX() < currentPosition.getX()) {
                return RIGHT;
            }

            if (previousPosition.getX() > currentPosition.getX()) {
                return LEFT;

            }

        } else {
            if (previousPosition.getY() > currentPosition.getY()) {
                return UP;

            }
            if (previousPosition.getY() < currentPosition.getY()) {
                return DOWN;

            }
        }
        return UNKNOWN;
    }

    private void reset() {
        initialPosition = null;
    }

    private float findPercentage(float pixelsTravelled, int totalPixels) {
        return (Math.abs(pixelsTravelled) / totalPixels) * 100;
    }

    public class SimpleXY {
        private float X, Y;

        public SimpleXY(SimpleXY pos) {
            this.X = pos.getX();
            this.Y = pos.getY();
        }

        public SimpleXY(float x, float y) {
            X = x;
            Y = y;
        }

        public float getX() {
            return X;
        }

        public float getY() {
            return Y;
        }

    }

}