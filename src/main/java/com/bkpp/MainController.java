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
import java.util.ResourceBundle;

public class MainController implements Initializable {
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
    ComboBox<Noise> signalChoiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeChoiceBox();
    }


    public void generateSignal(ActionEvent actionEvent) {
        Noise pickedSignal = getInitializedPickedSignal();
        fillChartWith(pickedSignal);
    }

    public void clearChart(ActionEvent actionEvent) {
        clearChart();
    }

    public void onSignalChosen(ActionEvent actionEvent) {
        if (signalChoiceBox.getValue() instanceof FillFactorSignal) {
            setTextsVisibility(true, true, false);
        } else if (signalChoiceBox.getValue() instanceof Signal) {
            setTextsVisibility(false, true, false);
        } else if (signalChoiceBox.getValue() instanceof ExtendedNoise) {
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
        signalChoiceBox.getItems().add(new SinusoidalSignal());
        signalChoiceBox.getItems().add(new HalfStrightSinusoidalSignal());
        signalChoiceBox.getItems().add(new StrightSinusoidalSignal());
        signalChoiceBox.getItems().add(new RectangleSignal());
        signalChoiceBox.getItems().add(new SymetricRectangleSignal());
        signalChoiceBox.getItems().add(new TriangleSignal());
        signalChoiceBox.getItems().add(new UnitaryJump());
        signalChoiceBox.getItems().add(new UnitaryImpulse());
        signalChoiceBox.getItems().add(new ImpulseNoise());
    }

    private Noise getInitializedPickedSignal() {
        Noise pickedSignal = signalChoiceBox.getValue();

        if (frequency.getText() != null && !frequency.getText().isEmpty()) {
            pickedSignal.setSampling(getFrequency());
        }

        pickedSignal.setAmplitude(getAmplitude());
        pickedSignal.setDuration(getDuration());
        pickedSignal.setStartTime(getStartTime());

        if (pickedSignal instanceof Signal) {
            ((Signal) pickedSignal).setTerm(getTerm());
        }

        if (pickedSignal instanceof FillFactorSignal) {
            ((FillFactorSignal) pickedSignal).setFillFactor(getFillFactor());
        }

        if (pickedSignal instanceof ExtendedNoise) {
            ((ExtendedNoise) pickedSignal).setParameter(getParameter());
        }

        return pickedSignal;
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

    private void fillChartWith(Noise signal) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();

        series.setName(signal.toString());

        for (Point point : signal.getPoints()) {
            series.getData().add(new XYChart.Data<>(point.getX(), point.getY()));
        }

        chart.getData().add(series);
    }

}
