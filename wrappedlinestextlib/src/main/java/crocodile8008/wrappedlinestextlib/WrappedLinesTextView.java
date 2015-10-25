package crocodile8008.wrappedlinestextlib;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by Andrey Riik in 2015
 */
public class WrappedLinesTextView extends TextView{

    /** Tag for logging */
    public static final String TAG = "WrappedLinesTextView";

    /** Last measured line count */
    private int lastLinesInHeight = -1;

    /** Flag for avoid unnecessary requestLayout() call */
    private boolean isRequestLayoutDenied;

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

    private void calcLinesCount() {
        int oneLineHeight = getOneLineHeight();
        int linesInHeight = getHeight() / oneLineHeight;

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
        return height / linesCnt;
    }

    private class OnPreDrawListenerImpl implements ViewTreeObserver.OnPreDrawListener {
        @Override
        public boolean onPreDraw() {
            calcLinesCount();
            return true;
        }
    }
}
