package com.namah.swipemodeview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;

public class SwipeModeView extends FrameLayout {

    public enum Mode {
        LEFT,
        RIGHT
    }

    public interface OnModeChangeListener {
        void onModeChanged(Mode mode);
    }

    private Mode currentMode = Mode.LEFT;
    private OnModeChangeListener listener;

    private FrameLayout track;
    private MaterialCardView thumb;
    private TextView centerText;
    private ImageView icon;

    private float dX;
    private float marginPx;

    private int leftColor;
    private int rightColor;
    private int textColor;
    private boolean hapticsEnabled = true;

    public SwipeModeView(Context context) {
        super(context);
        init(null);
    }

    public SwipeModeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        inflate(getContext(), R.layout.view_swipe_mode, this);

        track = findViewById(R.id.swipeTrack);
        thumb = findViewById(R.id.swipeThumb);
        centerText = findViewById(R.id.swipeText);
        icon = findViewById(R.id.swipeIcon);

        marginPx = dp(5);

        // Default values
        leftColor = Color.BLACK;
        rightColor = Color.parseColor("#F97316");
        textColor = Color.BLACK;
        hapticsEnabled = true;

        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(
                    attrs,
                    R.styleable.SwipeModeView
            );

            leftColor = ta.getColor(
                    R.styleable.SwipeModeView_leftModeColor,
                    leftColor
            );

            rightColor = ta.getColor(
                    R.styleable.SwipeModeView_rightModeColor,
                    rightColor
            );

            textColor = ta.getColor(
                    R.styleable.SwipeModeView_centerTextColor,
                    textColor
            );

            hapticsEnabled = ta.getBoolean(
                    R.styleable.SwipeModeView_hapticsEnabled,
                    true
            );

            ta.recycle();
        }

        centerText.setTextColor(textColor);

        setupTouch();

        if (isInEditMode()) {
            post(() -> applyMode(false));
        }
    }

    private void setupTouch() {
        thumb.setOnTouchListener((v, event) -> {

            float maxX = track.getWidth() - thumb.getWidth() - marginPx;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX = v.getX() - event.getRawX();
                    return true;

                case MotionEvent.ACTION_MOVE:
                    float newX = event.getRawX() + dX;
                    newX = Math.max(marginPx, Math.min(newX, maxX));
                    v.setX(newX);
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (v.getX() > maxX / 2f) {
                        setMode(Mode.RIGHT);
                    } else {
                        setMode(Mode.LEFT);
                    }
                    v.performClick();
                    break;
            }
            return true;
        });
    }

    private void applyMode(boolean animate) {
        if (track.getWidth() == 0) return;

        float targetX = currentMode == Mode.RIGHT
                ? track.getWidth() - thumb.getWidth() - marginPx
                : marginPx;

        if (animate) {
            ValueAnimator animator = ValueAnimator.ofFloat(thumb.getX(), targetX);
            animator.setDuration(220);
            animator.addUpdateListener(a ->
                    thumb.setX((Float) a.getAnimatedValue()));
            animator.start();
        } else {
            thumb.setX(targetX);
        }

        if (currentMode == Mode.RIGHT) {
            thumb.setCardBackgroundColor(rightColor);
            centerText.setText("RIGHT MODE");
        } else {
            thumb.setCardBackgroundColor(leftColor);
            centerText.setText("LEFT MODE");
        }

        if (hapticsEnabled && animate) {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        }

        if (listener != null) {
            listener.onModeChanged(currentMode);
        }
    }

    public void setMode(Mode mode) {
        if (currentMode == mode) return;
        currentMode = mode;
        applyMode(true);
    }

    public Mode getMode() {
        return currentMode;
    }

    public void setOnModeChangeListener(OnModeChangeListener listener) {
        this.listener = listener;
    }

    private float dp(int value) {
        return value * getResources().getDisplayMetrics().density;
    }

    public void setHapticsEnabled(boolean enabled) {
        hapticsEnabled = enabled;
    }
}
