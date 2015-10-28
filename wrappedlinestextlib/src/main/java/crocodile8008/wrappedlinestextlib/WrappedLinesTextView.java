package crocodile8008.wrappedlinestextlib;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IntDef;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * TextView with automatic counting max lines from height
 *
 * Created by Andrey Riik in 2015
 */
public class WrappedLinesTextView extends TextView {

    @IntDef({CALC_TYPE_DIVIDE, CALC_TYPE_GET_FOR_VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CalcType {}

    /** Measuring type based on dividing */
    public static final int CALC_TYPE_DIVIDE = 0;

    /** Measuring type based on {@link android.text.Layout#getLineForVertical(int)} */
    public static final int CALC_TYPE_GET_FOR_VERTICAL = 1;

    /** Tag for logging */
    public static final String TAG = "WrappedLinesTextView";

    /** Last measured line count */
    private int lastLinesInHeight = -1;

    /** Flag for avoid unnecessary requestLayout() call */
    private boolean isRequestLayoutDenied;

    /** Current type of lines measuring algorithm */
    @CalcType private int currentCalcType = CALC_TYPE_GET_FOR_VERTICAL;

    //region Constructors

    public WrappedLinesTextView(Context context) {
        super(context);
        init();
    }

    public WrappedLinesTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WrappedLinesTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WrappedLinesTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    //endregion

    private void init() {
        getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListenerImpl());
    }

    @Override
    public void requestLayout() {
        if (isRequestLayoutDenied) {
            isRequestLayoutDenied = false;
        } else {
            super.requestLayout();
        }
    }

    /**
     * Set type of lines counting
     */
    public void setLinesCalcType(@CalcType int type) {
        if (type != currentCalcType) {
            currentCalcType = type;
            invalidate();
        }
    }

    private void calcLinesCountFromVertical() {
        int linesInHeight = getLayout().getLineForVertical(getHeight());
        setNewLinesCntIfNeeded(linesInHeight);
    }

    private void calcLinesCountFromDivide() {
        int linesInHeight = getHeight() / getOneLineHeight();
        setNewLinesCntIfNeeded(linesInHeight);
    }

    private void setNewLinesCntIfNeeded(int linesInHeight) {
        if (linesInHeight != lastLinesInHeight) {
            lastLinesInHeight = linesInHeight;
            isRequestLayoutDenied = true;
            setMaxLines(linesInHeight);
        }
    }

    private int getOneLineHeight() {
        Layout layout = getLayout();
        int height = layout.getHeight();
        int linesCnt = layout.getLineCount();
        if (linesCnt == 0) {
            return 0;
        }
        return height / linesCnt;
    }

    private class OnPreDrawListenerImpl implements ViewTreeObserver.OnPreDrawListener {
        @Override
        public boolean onPreDraw() {
            switch (currentCalcType) {

                case CALC_TYPE_DIVIDE:
                    calcLinesCountFromDivide();
                    break;

                case CALC_TYPE_GET_FOR_VERTICAL:
                    calcLinesCountFromVertical();
                    break;

                default:
                    break;
            }
            return true;
        }
    }
}
