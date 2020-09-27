package io.github.sarkarshuvojit.wirelessmouse.client.utils;

import android.graphics.Point;
import android.util.Size;
import android.view.Display;

public class DistanceUtility {

    int screenWidth, screenHeight;

    public DistanceUtility(Display defaultDisplay){
        Point size = new Point();
        defaultDisplay.getSize(size);
        this.screenHeight = size.y;
        this.screenWidth = size.x;
    }

    public float[] getMovementPercentages(float x, float y) {
        float xPercent = (x/screenWidth) * 100;
        float yPercent = (y/screenHeight) * 100;
        return new float[] { xPercent, yPercent };
    }

}
