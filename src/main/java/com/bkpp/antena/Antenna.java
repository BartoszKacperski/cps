package com.bkpp.antena;

import com.bkpp.signals.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor

public class Antenna {
    private Signal signal;
    private double speed;
    private double translation;

    public Signal sendedSignal(){
        return signal;
    }

    public Signal receiveSignal(){
        List<Point> pointList = new ArrayList<>();
        int toSkip = (int) (translation/(signal.getPoints().get(1).getX() - signal.getPoints().get(0).getX()));
        List<Point> skipped = signal.getPoints().stream().skip(toSkip).collect(Collectors.toList());
        List<Point> limited = signal.getPoints().stream().limit(toSkip).collect(Collectors.toList());

        int i;
        for(i = 0; i < skipped.size(); i++){
            double x = signal.getPoints().get(i).getX();
            double y = skipped.get(i).getY();

            pointList.add(new Point(x, y));
        }

        for(int j = 0; j < limited.size(); j++){
            double x = signal.getPoints().get(i + j).getX();
            double y = limited.get(j).getY();

            pointList.add(new Point(x, y));
        }


        return new ResultSignal(pointList, ResultSignal.Operation.translation);
    }
}
