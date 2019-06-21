package b1g4.com.yourseat;

import net.daum.mf.map.api.MapPoint;

import java.util.Map;

public class CurrentLocationXY {
    private static CurrentLocationXY currentLocationXYInstance = new CurrentLocationXY();

    private String x;
    private String y;
    private MapPoint mapPoint;

    private CurrentLocationXY(){}

    public static CurrentLocationXY getInstance() {
        if(currentLocationXYInstance == null) {
            currentLocationXYInstance = new CurrentLocationXY();
        }
        return currentLocationXYInstance;
    }

    public void setX(String x) {
        this.x = x;
    }

    public void setY(String y) {
        this.y = y;
    }

    public void setMapPoint(MapPoint mapPoint) {
        this.mapPoint = mapPoint;
    }

    public String getX() {
        return this.x;
    }

    public String getY() {
        return this.y;
    }

    public MapPoint getMapPoint() {
        return this.mapPoint;
    }
}
