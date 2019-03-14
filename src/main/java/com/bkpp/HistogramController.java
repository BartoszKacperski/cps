package com.bkpp;

import com.bkpp.histogram.Histogram;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public class HistogramController extends BorderPane {

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private TextField textField;


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
        this.barChart.getData().clear();
        this.xAxis.getCategories().clear();

        BarChart.Series<String, Number> series = new XYChart.Series<>();

        Histogram histogram = new Histogram(this.points, numberOfRanges);
        int[] values = histogram.getValues();

        List<String> categories = new LinkedList<>();

        for (int i = 0; i < values.length; i++) {
            String label = this.getLabel(i , histogram);
            series.getData().add(new XYChart.Data<>(label, values[i]));
            categories.add(label);
        }

        this.xAxis.setCategories(FXCollections.observableList(categories));

        this.barChart.getData().add(series);
    }

    private String getLabel(int i, Histogram histogram) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return new StringBuilder()
                .append('(')
                .append(decimalFormat.format(histogram.getMin() + i * histogram.getRange()))
                .append(" ; ")
                .append(decimalFormat.format(histogram.getMin() + (i + 1) * histogram.getRange()))
                .append(')').toString();
    }

}
