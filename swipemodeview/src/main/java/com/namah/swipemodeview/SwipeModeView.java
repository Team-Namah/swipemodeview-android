package com.namah.swipemodeview;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.card.MaterialCardView;

public class SwipeModeView extends FrameLayout {

    public static final int MODE_START = 0;
    public static final int MODE_END = 1;

    public enum Mode { START, END }

    private int currentMode = MODE_START;

    private FrameLayout track;
    private MaterialCardView thumb;
    private ImageView icon;
    private TextView text;

    private int startIcon;
    private int endIcon;
    private String startText;
    private String endText;
    
    @ColorInt private int leftModeColor;
    @ColorInt private int rightModeColor;
    @ColorInt private int centerTextColor;
    @ColorInt private int thumbColor;
    private boolean hapticsEnabled;
    private int orientation;
    private boolean swipeEnabled;
    private int animationDuration;
    private int thumbSize;

    private float downX;
    private float downY;
    private OnModeChangeListener listener;

    public SwipeModeView(@NonNull Context context) {
        this(context, null);
    }

    public SwipeModeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.view_swipe_mode, this);

        track = findViewById(R.id.swipeTrack);
        thumb = findViewById(R.id.swipeThumb);
        icon = findViewById(R.id.swipeIcon);
        text = findViewById(R.id.swipeText);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeModeView);
            leftModeColor = a.getColor(R.styleable.SwipeModeView_leftModeColor, Color.parseColor("#BFFFFFFF"));
            rightModeColor = a.getColor(R.styleable.SwipeModeView_rightModeColor, Color.parseColor("#BFFFFFFF"));
            centerTextColor = a.getColor(R.styleable.SwipeModeView_centerTextColor, Color.BLACK);
            thumbColor = a.getColor(R.styleable.SwipeModeView_thumbColor, Color.BLACK);
            hapticsEnabled = a.getBoolean(R.styleable.SwipeModeView_hapticsEnabled, true);
            currentMode = a.getInt(R.styleable.SwipeModeView_mode, MODE_START);
            orientation = a.getInt(R.styleable.SwipeModeView_orientation, 0); // 0 = horizontal
            startIcon = a.getResourceId(R.styleable.SwipeModeView_startIcon, 0);
            endIcon = a.getResourceId(R.styleable.SwipeModeView_endIcon, 0);
            startText = a.getString(R.styleable.SwipeModeView_startText);
            endText = a.getString(R.styleable.SwipeModeView_endText);
            swipeEnabled = a.getBoolean(R.styleable.SwipeModeView_swipeEnabled, true);
            animationDuration = a.getInt(R.styleable.SwipeModeView_animationDuration, 250);
            thumbSize = a.getDimensionPixelSize(R.styleable.SwipeModeView_thumbSize, -1);
            a.recycle();
        }

        if (startText == null) startText = "START MODE";
        if (endText == null) endText = "END MODE";

        applyStaticAttributes();
        post(this::updateUIInitial);

        track.setOnTouchListener((v, e) -> {
            if (!swipeEnabled) return false;
            
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = e.getX();
                    downY = e.getY();
                    return true;

                case MotionEvent.ACTION_UP:
                    if (orientation == 0) { // Horizontal
                        float deltaX = e.getX() - downX;
                        if (Math.abs(deltaX) > 50) {
                            setMode(deltaX > 0 ? MODE_END : MODE_START);
                        }
                    } else { // Vertical
                        float deltaY = e.getY() - downY;
                        if (Math.abs(deltaY) > 50) {
                            setMode(deltaY > 0 ? MODE_END : MODE_START);
                        }
                    }
                    return true;
            }
            return false;
        });
    }

    private void applyStaticAttributes() {
        text.setTextColor(centerTextColor);
        thumb.setCardBackgroundColor(ColorStateList.valueOf(thumbColor));
        if (thumbSize != -1) {
            ViewGroup.LayoutParams lp = thumb.getLayoutParams();
            lp.width = thumbSize;
            lp.height = thumbSize;
            thumb.setLayoutParams(lp);
            thumb.setRadius(thumbSize / 2f);
        }
    }

    private void updateUIInitial() {
        if (track == null || thumb == null) return;

        if (orientation == 0) { // Horizontal
            float endX = track.getWidth() - thumb.getWidth() - track.getPaddingEnd();
            float startX = track.getPaddingStart();
            thumb.setX(currentMode == MODE_END ? endX : startX);
        } else { // Vertical
            float endY = track.getHeight() - thumb.getHeight() - track.getPaddingBottom();
            float startY = track.getPaddingTop();
            thumb.setY(currentMode == MODE_END ? endY : startY);
        }

        text.setText(currentMode == MODE_START ? startText : endText);
        icon.setImageResource(currentMode == MODE_START ? startIcon : endIcon);
        track.getBackground().setTint(currentMode == MODE_START ? leftModeColor : rightModeColor);
    }

    private void updateUI() {
        if (track == null || thumb == null) return;

        if (orientation == 0) { // Horizontal
            float endX = track.getWidth() - thumb.getWidth() - track.getPaddingEnd();
            float startX = track.getPaddingStart();
            thumb.animate()
                    .x(currentMode == MODE_END ? endX : startX)
                    .setDuration(animationDuration)
                    .start();
        } else { // Vertical
            float endY = track.getHeight() - thumb.getHeight() - track.getPaddingBottom();
            float startY = track.getPaddingTop();
            thumb.animate()
                    .y(currentMode == MODE_END ? endY : startY)
                    .setDuration(animationDuration)
                    .start();
        }

        text.setText(currentMode == MODE_START ? startText : endText);
        icon.setImageResource(currentMode == MODE_START ? startIcon : endIcon);

        // Animate background color change
        int colorFrom = (currentMode == MODE_END) ? leftModeColor : rightModeColor;
        int colorTo = (currentMode == MODE_END) ? rightModeColor : leftModeColor;
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(animationDuration);
        colorAnimation.addUpdateListener(animator -> track.getBackground().setTint((int) animator.getAnimatedValue()));
        colorAnimation.start();
    }

    public void setMode(int mode) {
        if (mode == currentMode) return;

        currentMode = mode;
        if (hapticsEnabled) {
            performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        }
        updateUI();

        if (listener != null) {
            listener.onModeChanged(
                    currentMode == MODE_START ? Mode.START : Mode.END
            );
        }
    }

    public int getMode() {
        return currentMode;
    }

    public void setModeIcons(@DrawableRes int start, @DrawableRes int end) {
        this.startIcon = start;
        this.endIcon = end;
        updateUI();
    }

    public void setTexts(String start, String end) {
        this.startText = start;
        this.endText = end;
        updateUI();
    }

    public void setOnModeChangeListener(OnModeChangeListener l) {
        this.listener = l;
    }

    public interface OnModeChangeListener {
        void onModeChanged(@NonNull Mode mode);
    }
}
