package org.renxiuhu.smalldemo;

import android.app.Activity;
import android.os.Bundle;
import org.renxiuhu.view.CompassView;

public class CampassActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        CompassView compassView=(CompassView)findViewById(R.id.compass);
        compassView.setBearing(0);
    }
}
