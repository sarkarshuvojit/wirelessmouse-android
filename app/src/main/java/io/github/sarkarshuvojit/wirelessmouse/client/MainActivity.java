package io.github.sarkarshuvojit.wirelessmouse.client;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import io.github.sarkarshuvojit.wirelessmouse.client.utils.DistanceUtility;

public class MainActivity extends Activity {
    private String TAG = "MainActivity";
    private VelocityTracker _velocityTracker;
    private DistanceUtility _distanceUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _distanceUtility = new DistanceUtility(getWindowManager().getDefaultDisplay());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);

        switch (action) {
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
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                showMovementPercentages(_velocityTracker.getXVelocity(pointerId), _velocityTracker.getYVelocity(pointerId));
                _velocityTracker.recycle();
                _velocityTracker = null;
                break;
        }

        return true;
    }

    public void showMovementPercentages(float velocityX, float velocityY) {
        float[] movementPercent = _distanceUtility.getMovementPercentages(velocityX, velocityY);
        Log.d(TAG, "showMovementPercentages: ");
        Log.d(TAG, "x% => " + movementPercent[0]);
        Log.d(TAG, "y% => " + movementPercent[1]);
        updateLocation(movementPercent[0], movementPercent[1]);
    }

    public void updateLocation(float x, float y) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.29.214:8000/move?x_percent="+x+"&y_percent="+y;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d(TAG, "onResponse: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: That didn't work!", error);
                    }
                }
        );

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}