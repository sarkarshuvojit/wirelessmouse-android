package io.github.sarkarshuvojit.wirelessmouse.client;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

public class MainActivity extends Activity {
    private String TAG = "MainActivity";
    private VelocityTracker _velocityTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);

        switch(action) {
            case MotionEvent.ACTION_DOWN:
                if (_velocityTracker == null) {
                    _velocityTracker = VelocityTracker.obtain();
                } else {
                    _velocityTracker.clear();
                }
                _velocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                _velocityTracker.addMovement(event);
                _velocityTracker.computeCurrentVelocity(1000);
                Log.d(TAG, "onTouchEvent: " + _velocityTracker.getXVelocity(pointerId) + "\n" + _velocityTracker.getYVelocity(pointerId));
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                _velocityTracker.recycle();
                _velocityTracker = null;
                break;
        }

        return true;
    }

}