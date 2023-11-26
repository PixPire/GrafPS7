import java.awt.*;
import java.io.Console;
import java.io.Serializable;
import java.util.ArrayList;

public class PolygonFigure  implements Serializable {


    ArrayList<Point> pointsList=new ArrayList<>();

    public int pointsAmount;
    public PolygonFigure(){

    }



    public void setPoints(ArrayList<Point> pointsList){
        this.pointsList=pointsList;
    }

    public void addPoint(Point point){
        pointsList.add(new Point(point.x, point.y));

    }

    public void moveByVector(int vectorX, int vectorY){
        for (Point point:pointsList
             ) {   point.x=point.x+vectorX;
            point.y=point.y+vectorY;

        }
    }

    public void setPointsAmount(int pointsNumber) {
        pointsAmount=pointsNumber;
    }
}
