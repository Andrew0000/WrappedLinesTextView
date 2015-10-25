package crocodile8008.wrappedlinestext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private LayoutSizeChanger changer;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv1);
        changer = new LayoutSizeChanger(textView);

        changeSizeProgressive();
    }

    public void onDestroy() {
        textView.removeCallbacks(changer);
        super.onDestroy();
    }

    // Method for demonstrating lines count changing with layout height changing
    private void changeSizeProgressive() {
        for (int i = 0; i < 200; i++) {
            textView.postDelayed(changer, i * 30);
        }
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
