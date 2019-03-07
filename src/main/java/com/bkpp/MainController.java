package com.bkpp;

import com.bkpp.signals.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    Button add;
    @FXML
    ComboBox<Signal> firstSignals;
    @FXML
    ComboBox<Signal> secondSignals;
    @FXML
    Button clear;
    @FXML
    TextField frequency;
    @FXML
    TextField parameter;
    @FXML
    TextField fillFactor;
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
    ComboBox<Signal> signalChoiceBox;

    private List<Signal> generatedSignals = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeChoiceBox();
    }


    public void generateSignal(ActionEvent actionEvent) {
        Signal pickedSignal = getInitializedPickedSignal();
        fillChartWith(pickedSignal);
    }

    public void clearChart(ActionEvent actionEvent) {
        clearChart();
    }

    public void addSignals(ActionEvent actionEvent) {
        Signal firstSignal = firstSignals.getValue();
        Signal secondSignal = secondSignals.getValue();

        fillChartWith(SignalOperationUtils.addition(firstSignal, secondSignal), "Dodawanie");
    }

    public void onSignalChosen(ActionEvent actionEvent) {
        if (signalChoiceBox.getValue() instanceof FillFactorContinuousSignal) {
            setTextsVisibility(true, true, false);
        } else if (signalChoiceBox.getValue() instanceof ContinuousSignal) {
            setTextsVisibility(false, true, false);
        } else if (signalChoiceBox.getValue() instanceof DiscreteSignal) {
            setTextsVisibility(false, false, true);

            if (signalChoiceBox.getValue() instanceof UnitaryJump) {
                parameter.setPromptText("Czas skoku");
            } else if (signalChoiceBox.getValue() instanceof UnitaryImpulse) {
                parameter.setPromptText("Numer probki");
            } else {
                parameter.setPromptText("Prawdopodobienstwo");
            }
        } else {
            setTextsVisibility(false, false, false);
        }
    }

    private void setTextsVisibility(boolean fillFactorVisibility, boolean termVisibility, boolean parameterVisibility) {
        fillFactor.setVisible(fillFactorVisibility);
        term.setVisible(termVisibility);
        parameter.setVisible(parameterVisibility);
    }

    private void initializeChoiceBox() {
        signalChoiceBox.getItems().add(new UnvaryingNoise());
        signalChoiceBox.getItems().add(new GaussianNoise());
        signalChoiceBox.getItems().add(new SinusoidalContinuousSignal());
        signalChoiceBox.getItems().add(new HalfStrightSinusoidalContinuousSignal());
        signalChoiceBox.getItems().add(new StraightSinusoidalContinuousSignal());
        signalChoiceBox.getItems().add(new RectangleContinuousSignal());
        signalChoiceBox.getItems().add(new SymetricRectangleContinuousSignal());
        signalChoiceBox.getItems().add(new TriangleContinuousSignal());
        signalChoiceBox.getItems().add(new UnitaryJump());
        signalChoiceBox.getItems().add(new UnitaryImpulse());
        signalChoiceBox.getItems().add(new ImpulseNoise());
    }

    private Signal getInitializedPickedSignal() {
        Signal pickedSignal = signalChoiceBox.getValue();

        if (frequency.getText() != null && !frequency.getText().isEmpty()) {
            pickedSignal.setFrequency(getFrequency());
        }

        pickedSignal.setAmplitude(getAmplitude());
        pickedSignal.setDuration(getDuration());
        pickedSignal.setStartTime(getStartTime());

        if (pickedSignal instanceof ContinuousSignal) {
            ((ContinuousSignal) pickedSignal).setTerm(getTerm());
        }

        if (pickedSignal instanceof FillFactorContinuousSignal) {
            ((FillFactorContinuousSignal) pickedSignal).setFillFactor(getFillFactor());
        }

        if (pickedSignal instanceof DiscreteSignal) {
            ((DiscreteSignal) pickedSignal).setParameter(getParameter());
        }

        saveSignal(pickedSignal);

        return pickedSignal;
    }

    private void saveSignal(Signal signal){
        firstSignals.getItems().add(signal);
        secondSignals.getItems().add(signal);
        generatedSignals.add(signal);
    }

    private Double getAmplitude() {
        return Double.valueOf(amplitude.getText());
    }

    private Double getStartTime() {
        return Double.valueOf(startTime.getText());
    }

    private Double getDuration() {
        return Double.valueOf(duration.getText());
    }

    private Double getTerm() {
        return Double.valueOf(term.getText());
    }

    private Double getFillFactor() {
        return Double.valueOf(fillFactor.getText());
    }

    private Double getParameter() {
        return Double.valueOf(parameter.getText());
    }

    private Double getFrequency() {
        return Double.valueOf(frequency.getText());
    }

    private void clearChart() {
        chart.getData().clear();
    }

    private void fillChartWith(Signal signal) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();

        series.setName(signal.toString());

        for (Point point : signal.getPoints()) {
            series.getData().add(new XYChart.Data<>(point.getX(), point.getY()));
        }

        chart.getData().add(series);
    }

    private void fillChartWith(List<Point> points, String name) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();

        series.setName(name);

        for (Point point : points) {
            series.getData().add(new XYChart.Data<>(point.getX(), point.getY()));
        }

        chart.getData().add(series);
    }


}
