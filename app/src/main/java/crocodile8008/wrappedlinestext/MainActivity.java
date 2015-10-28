package crocodile8008.wrappedlinestext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import crocodile8008.wrappedlinestextlib.WrappedLinesTextView;

public class MainActivity extends AppCompatActivity {

    private LayoutSizeChanger changer;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv1);
        changer = new LayoutSizeChanger(textView);

        setSpan();

        changeSizeProgressive();
    }

    private void setSpan() {
        Spannable span = new SpannableString(textView.getText());
        span.setSpan(new RelativeSizeSpan(2.0f), 15, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(span);
    }

    // Method for demonstrating lines count changing with layout height changing
    private void changeSizeProgressive() {
        for (int i = 0; i < 200; i++) {
            textView.postDelayed(changer, i * 30);
        }
    }

    public void onDestroy() {
        textView.removeCallbacks(changer);
        super.onDestroy();
    }

    private class LayoutSizeChanger implements Runnable {
        private View viewToChange;

        LayoutSizeChanger(View view) {
            viewToChange = view;
        }

        @Override
        public void run() {
            ViewGroup.LayoutParams layoutParams = viewToChange.getLayoutParams();
            layoutParams.height *= 1.01f;
            viewToChange.setLayoutParams(layoutParams);
        }
    }
}
