package com.bkpp;

import com.bkpp.signals.*;

import com.bkpp.transforms.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TransformController implements Initializable {
    @FXML
    Label executionTime;
    @FXML
    TextField frequency;
    @FXML
    TextField duration;
    @FXML
    ComboBox<SinusoidalSignal> signals;
    @FXML
    ComboBox<Transform> transforms;
    @FXML
    LineChart<Double, Double> signalChart;
    @FXML
    LineChart<Double, Double> imaginaryChart;
    @FXML
    LineChart<Double, Double> realChart;

    private TransformResult currentResult;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initSignals();
        initTransforms();
    }

    public void saveResult() {
        if(currentResult != null){
            try {
                String filePath = fileChooser().showSaveDialog(App.getPrimaryStage()).getAbsolutePath();
                SerializationUtils.serialize(currentResult, new FileOutputStream(new File(filePath)));
            } catch (FileNotFoundException e) {
                showErrorDialog("Nie udało się zapisać pliku");
            }
        }
    }

    public void openResult() {
        try {
            String filePath = fileChooser().showOpenDialog(App.getPrimaryStage()).getAbsolutePath();
            currentResult = SerializationUtils.deserialize(new FileInputStream(new File(filePath)));
            fillResults(currentResult);
        } catch (FileNotFoundException e) {
            showErrorDialog("Nie udało się zapisać pliku");
        }
    }

    public void transform() {
        Transform transform = transforms.getValue();
        SinusoidalSignal signal = getInitializedSignal();

        try {
            currentResult = transform.transform(signal);
            fillResults(currentResult);
        } catch (MathIllegalArgumentException e){
            showErrorDialog("Ilość próbek nie jest potęga liczby 2");
        }

    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Ooops, pojawil sie problem!");
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void clearResults() {
        signalChart.getData().clear();
        realChart.getData().clear();
        imaginaryChart.getData().clear();
    }

    private void initSignals() {
        signals.getItems().add(new FirstSpecialSignal());
        signals.getItems().add(new SecondSpecialSignal());
        signals.getItems().add(new ThirdSpecialSignal());
    }

    private void initTransforms() {
        transforms.getItems().add(new FastFourierTransform());
        transforms.getItems().add(new DiscreteFourierTransform());
        transforms.getItems().add(new DiscreteCosineTransform());
        transforms.getItems().add(new FastCosineTransform());
    }

    private SinusoidalSignal getInitializedSignal(){
        SinusoidalSignal chosenSignal = signals.getValue();

        chosenSignal.setTerm(0.0);
        chosenSignal.setStartTime(0.0);
        chosenSignal.setAmplitude(0.0);

        chosenSignal.setFrequency(getFrequency());
        chosenSignal.setDuration(getDuration());

        chosenSignal.computePoints();

        chosenSignal.setPoints(chosenSignal.getPoints().stream().skip(1).collect(Collectors.toList()));

        return chosenSignal;
    }

    private Double getDuration() {
        return Double.valueOf(duration.getText());
    }

    private Double getFrequency() {
        return Double.valueOf(frequency.getText());
    }

    private void fillResults(TransformResult transformResult){
        clearResults();
        fillCharts(transformResult.getResult(), transformResult.getSignal());

        executionTime.setText("Czas wykonania: = " + transformResult.getExecutionTimeInMillis() + " ns");
    }

    private void fillCharts(List<Complex> complexes, Signal signal){
        List<Point> re = new ArrayList<>();
        List<Point> im = new ArrayList<>();

        double interval = signal.getPoints().get(1).getX() - signal.getPoints().get(0).getX();
        double f0 = (1.0/interval) / signal.getPoints().size();

        for(int i = 0; i < complexes.size(); i++){
            re.add(new Point((double)i * f0, complexes.get(i).getReal()));
            im.add(new Point((double)i * f0, complexes.get(i).getImaginary()));
        }


        fillChartWithoutPoints(re, "real", realChart);
        fillChartWithoutPoints(im, "imaginary", imaginaryChart);
        fillChartWithoutPoints(signal.getPoints(), "signal", signalChart);
    }

    private void fillChartWithoutPoints(List<Point> points, String seriesName, LineChart<Double, Double> chart) {
        fillChartWith(points, seriesName, chart);
        for (XYChart.Data data : chart.getData().get(chart.getData().size() - 1).getData()) {
            data.getNode().setStyle("-fx-background-color: #00000000;");
        }
    }

    private void fillChartWith(List<Point> points, String seriesName, LineChart<Double, Double>  chart) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();

        series.setName(seriesName);

        for (Point point : points) {
            series.getData().add(new XYChart.Data<>(point.getX(), point.getY()));
        }

        chart.getData().add(series);
    }

    private FileChooser fileChooser() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Wybierz plik");
        File defaultDirectory = new File(Paths.get(".").toAbsolutePath().normalize().toString());
        chooser.setInitialDirectory(defaultDirectory);

        return chooser;
    }


}
