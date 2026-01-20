package com.namah.swipemodeview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.card.MaterialCardView;

public class SwipeModeView extends FrameLayout {

    public static final int MODE_START = 0;
    public static final int MODE_END = 1;

    public enum Mode { LEFT, RIGHT }

    private int currentMode = MODE_START;

    private FrameLayout track;
    private MaterialCardView thumb;
    private ImageView icon;
    private TextView text;

    private int startIcon;
    private int endIcon;

    private float downX;
    private OnModeChangeListener listener;

    public SwipeModeView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SwipeModeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_swipe_mode, this);

        track = findViewById(R.id.swipeTrack);
        thumb = findViewById(R.id.swipeThumb);
        icon = findViewById(R.id.swipeIcon);
        text = findViewById(R.id.swipeText);

        post(this::updateUI);

        track.setOnTouchListener((v, e) -> {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = e.getX();
                    return true;

                case MotionEvent.ACTION_UP:
                    float delta = e.getX() - downX;
                    if (Math.abs(delta) > 80) {
                        setMode(delta > 0 ? MODE_END : MODE_START);
                    }
                    return true;
            }
            return false;
        });
    }

    private void updateUI() {
        float endX = track.getWidth() - thumb.getWidth() - track.getPaddingEnd();
        float startX = track.getPaddingStart();

        thumb.animate()
                .x(currentMode == MODE_END ? endX : startX)
                .setDuration(200)
                .start();

        text.setText(currentMode == MODE_START ? "BATTERY MODE" : "ENGINE MODE");
        icon.setImageResource(currentMode == MODE_START ? startIcon : endIcon);
    }

    public void setMode(int mode) {
        if (mode == currentMode) return;

        currentMode = mode;
        updateUI();

        if (listener != null) {
            listener.onModeChanged(
                    currentMode == MODE_START ? Mode.LEFT : Mode.RIGHT
            );
        }
    }

    public void setModeIcons(@DrawableRes int start, @DrawableRes int end) {
        startIcon = start;
        endIcon = end;
        updateUI();
    }

    public void setOnModeChangeListener(OnModeChangeListener l) {
        this.listener = l;
    }

    public interface OnModeChangeListener {
        void onModeChanged(@NonNull Mode mode);
    }
}
