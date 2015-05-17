package com.example.root.campusbuddy;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;

public class map3 extends MapView {

    public static float MAX_ZOOM = 20;
    public static float MIN_ZOOM = 5;
    public static float MIN_ZOOM_FOR_FLING = 7;

    public static double MAX_LONGITUDE = 183.61;
    public static double MIN_LONGITUDE = 159.31;
    public static double MAX_LATITUDE = -32.98;
    public static double MIN_LATITUDE = -53.82;

    public static double DEF_LATITUDE = -41.78;
    public static double DEF_LONGITUDE = 173.02;
    public static float DEF_ZOOM = 7;

    private Handler mHandler = new Handler();
    private Context mContext;
    private VisibleRegion mLastCorrectRegion = null;
    private boolean mIsInAnimation = false;

  /*  public map3(Context context)
    {
        super(context);
        init();
    }
    public map3(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }
    public map3(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
*/
    public map3(Context c, AttributeSet a, int o) {
        super(c, a, o);
        init(c);
    }

    public map3(Context c, AttributeSet a) {
        super(c, a);
        init(c);
    }

    map3(Context c) {
        super(c);
        init(c);
    }

    public map3(Context c, GoogleMapOptions o) {
        super(c, o);
        init(c);
    }

    private GestureDetector mGestureDetector = null;
    private GestureDetector.SimpleOnGestureListener mGestudeListener =
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    if (mIsInAnimation) return false;
                    GoogleMap map = getMap();
                    LatLng target = map.getCameraPosition().target;
                    Point screenPoint = map.getProjection().toScreenLocation(target);
                    Point newPoint = new Point(screenPoint.x + (int) distanceX, screenPoint.y + (int) distanceY);
                    LatLng mapNewTarget = map.getProjection().fromScreenLocation(newPoint);
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
                            mapNewTarget, map.getCameraPosition().zoom);
                    tryUpdateCamera(update, 0);
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if (mIsInAnimation) return false;
                    GoogleMap map = getMap();
                    double zoom = map.getCameraPosition().zoom;
                    if (zoom < MIN_ZOOM_FOR_FLING)
                        return false;
                    int velocity = (int) Math.sqrt(velocityX * velocityX + velocityY * velocityY);
                    if (velocity < 500) return false;
                    double k1 = 0.002d; /*exipemental*/
                    double k2 = 0.002d;/*exipemental*/

                    LatLng target = map.getCameraPosition().target;
                    Point screenPoint = map.getProjection().toScreenLocation(target);
                    Point newPoint = new Point(screenPoint.x - (int) (velocityX * k1 * zoom * zoom/*exipemental*/),
                            screenPoint.y - (int) (velocityY * k1 * zoom * zoom/*exipemental*/));
                    LatLng mapNewTarget = map.getProjection().fromScreenLocation(newPoint);
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
                            mapNewTarget, map.getCameraPosition().zoom);
                    tryUpdateCamera(update, (int) (velocity * k2 * zoom * zoom) /*exipemental*/);
                    return true;
                }
            };
    private ScaleGestureDetector mScaleGestureDetector = null;
    private ScaleGestureDetector.SimpleOnScaleGestureListener mScaleGestudeListener =
            new ScaleGestureDetector.SimpleOnScaleGestureListener() {

                @Override
                public boolean onScale(ScaleGestureDetector detector) {
                    if (mIsInAnimation) return false;

                    GoogleMap map = getMap();
                    double zoom = map.getCameraPosition().zoom;

                    double k = 1d / detector.getScaleFactor();
                    int x = (int) detector.getFocusX();
                    int y = (int) detector.getFocusY();
                    LatLng mapFocus = map.getProjection().
                            fromScreenLocation(new Point(x, y));
                    LatLng target = map.getCameraPosition().target;

                    zoom = zoom + Math.log(detector.getScaleFactor()) / Math.log(2d);
                    if (zoom < MIN_ZOOM)
                        if (zoom == MIN_ZOOM) return false;
                        else zoom = MIN_ZOOM;
                    if (zoom > MAX_ZOOM)
                        if (zoom == MAX_ZOOM) return false;
                        else zoom = MAX_ZOOM;

                    double dx = norm(mapFocus.longitude) - norm(target.longitude);
                    double dy = mapFocus.latitude - target.latitude;
                    double dk = 1d - 1d / k;
                    LatLng newTarget = new LatLng(target.latitude - dy * dk,
                            norm(target.longitude) - dx * dk);

                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(newTarget, (float) zoom);
                    tryUpdateCamera(update, 0);
                    return true;
                }
            };


    private void tryUpdateCamera(CameraUpdate update, int animateTime) {
        GoogleMap map = getMap();
        final VisibleRegion reg = map.getProjection().getVisibleRegion();
        if (animateTime <= 0) {
            map.moveCamera(update);
            checkCurrentRegion(reg);
        } else {
            mIsInAnimation = true;
            map.animateCamera(update, animateTime, new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    mIsInAnimation = false;
                    checkCurrentRegion(reg);
                }

                @Override
                public void onCancel() {
                    mIsInAnimation = false;
                    checkCurrentRegion(reg);
                }
            });
        }
    }

    private void checkCurrentRegion(VisibleRegion oldReg) {
        GoogleMap map = getMap();
        VisibleRegion regNew = map.getProjection().getVisibleRegion();
        if (checkBounds(regNew)) {
            mLastCorrectRegion = regNew;
        } else {
            if (mLastCorrectRegion != null)
                oldReg = mLastCorrectRegion;
            mIsInAnimation = true;
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(
                            oldReg.latLngBounds, 0),
                    200, new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            mIsInAnimation = false;
                        }

                        @Override
                        public void onCancel() {
                            mIsInAnimation = false;
                        }
                    });

        }
    }

    /**
     * @param lonVal
     * @return
     */
    private double norm(double lonVal) {
        while (lonVal > 360d) lonVal -= 360d;
        while (lonVal < -360d) lonVal += 360d;
        if (lonVal < 0) lonVal = 360d + lonVal;
        return lonVal;
    }

    private double denorm(double lonVal) {
        if (lonVal > 180d) lonVal = -360d + lonVal;
        return lonVal;
    }

    private boolean checkBounds(VisibleRegion reg) {
        double left = Math.min(
                Math.min(norm(reg.farLeft.longitude), norm(reg.nearLeft.longitude)),
                Math.min(norm(reg.farRight.longitude), norm(reg.nearRight.longitude)));
        double right = Math.max(
                Math.max(norm(reg.farLeft.longitude), norm(reg.nearLeft.longitude)),
                Math.max(norm(reg.farRight.longitude), norm(reg.nearRight.longitude)));
        double top = Math.max(
                Math.max(reg.farLeft.latitude, reg.nearLeft.latitude),
                Math.max(reg.farRight.latitude, reg.nearRight.latitude));
        double bottom = Math.min(
                Math.min(reg.farLeft.latitude, reg.nearLeft.latitude),
                Math.min(reg.farRight.latitude, reg.nearRight.latitude));

        boolean limitBounds = left < MIN_LONGITUDE || right > MAX_LONGITUDE ||
                bottom < MIN_LATITUDE || top > MAX_LATITUDE;
        return !limitBounds;
    }

    private void init(Context c) {
        try {
            MapsInitializer.initialize(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mContext = c;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                GoogleMap map = getMap();
                if (map != null) {
                    getMap().getUiSettings().setZoomControlsEnabled(false);
                    map.getUiSettings().setAllGesturesEnabled(false);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(DEF_LATITUDE, DEF_LONGITUDE), DEF_ZOOM));
                    mLastCorrectRegion = map.getProjection().getVisibleRegion();
                    mGestureDetector = new GestureDetector(mContext, mGestudeListener);
                    mScaleGestureDetector = new ScaleGestureDetector(mContext, mScaleGestudeListener);
                } else mHandler.post(this);
            }
        });
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mGestureDetector != null) mGestureDetector.onTouchEvent(event);
        if (mScaleGestureDetector != null) mScaleGestureDetector.onTouchEvent(event);
        return super.onInterceptTouchEvent(event);
    }
}