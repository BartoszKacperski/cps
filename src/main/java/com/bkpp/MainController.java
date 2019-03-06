package com.bkpp;

import com.bkpp.signals.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    TextField amplitude;
    @FXML
    TextField startTime;
    @FXML
    TextField duration;
    @FXML
    TextField term;
    @FXML
    Button generate;
    @FXML
    LineChart<Double, Double> chart;
    @FXML
    ComboBox<Noise> signalChoiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeChoiceBox();
    }


    public void generateSignal(ActionEvent actionEvent) {
        Noise pickedSignal = getInitializedPickedSignal();
        clearChart();
        fillChartWith(pickedSignal);
    }

    public void onSignalChosen(ActionEvent actionEvent) {
        if(signalChoiceBox.getValue() instanceof Signal){
            term.setVisible(true);
        } else {
            term.setVisible(false);
        }
    }

    private void initializeChoiceBox() {
        signalChoiceBox.getItems().add(new UnvaryingNoise());
        signalChoiceBox.getItems().add(new GaussianNoise());
        signalChoiceBox.getItems().add(new SinusoidalSignal());
    }

    private Noise getInitializedPickedSignal(){
        Noise pickedSignal = signalChoiceBox.getValue();

        pickedSignal.setAmplitude(getAmplitude());
        pickedSignal.setDuration(getDuration());
        pickedSignal.setStartTime(getStartTime());

        if(pickedSignal instanceof Signal){
            ((Signal)pickedSignal).setTerm(getTerm());
        }

        return pickedSignal;
    }

    private Double getAmplitude(){
        return Double.valueOf(amplitude.getText());
    }

    private Double getStartTime(){
        return Double.valueOf(startTime.getText());
    }

    private Double getDuration(){
        return Double.valueOf(duration.getText());
    }

    private Double getTerm(){
        return Double.valueOf(term.getText());
    }

    private void clearChart() {
        chart.getData().clear();
    }

    private void fillChartWith(Noise signal) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();

        series.setName(signal.toString());

        for(Point point : signal.getPoints()){
            series.getData().add(new XYChart.Data<>(point.getX(), point.getY()));
        }

        chart.getData().add(series);
    }

}
