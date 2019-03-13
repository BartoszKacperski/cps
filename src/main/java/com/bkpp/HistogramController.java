package com.bkpp;

import com.bkpp.histogram.Histogram;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;

public class HistogramController extends BorderPane {

    @FXML
    BarChart<String, Number> barChart;

    @FXML
    TextField textField;

    private List<Point> points;

    public HistogramController(List<Point> points) {
        this.points = points;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Histogram.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    public void onChange() {
        try {
            int numberOfRanges = Integer.valueOf(this.textField.getText());
            this.initializeChart(numberOfRanges);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeChart(int numberOfRanges) {
        barChart.getData().clear();

        BarChart.Series<String, Number> series = new XYChart.Series<>();

        Histogram histogram = new Histogram(this.points, numberOfRanges);
        int[] values = histogram.getValues();

        for (int i = 0; i < values.length; i++) {
            series.getData().add(new XYChart.Data<>(Integer.valueOf(i).toString(), values[i]));
        }

        barChart.getData().add(series);
    }

}
