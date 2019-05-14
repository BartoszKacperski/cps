package com.bkpp;

import com.bkpp.signals.Point;
import com.bkpp.signals.Signal;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class CorrelationDialog {
    @FXML
    public LineChart<Double, Double> chart;

    @Getter
    @Setter
    private Signal correlation;

    public void drawCorrelation() {
        fillChartWithoutPoints(correlation.getPoints());
    }

    private void fillChartWithoutPoints(List<Point> points) {
        fillChartWith(points);
        for (XYChart.Data data : chart.getData().get(chart.getData().size() - 1).getData()) {
            data.getNode().setStyle("-fx-background-color: #00000000;");
        }
    }

    private void fillChartWith(List<Point> points) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();

        series.setName("Korelacja");

        for (Point point : points) {
            series.getData().add(new XYChart.Data<>(point.getX(), point.getY()));
        }

        chart.getData().add(series);
    }
}
