package com.bkpp;

import com.bkpp.converters.Converter;
import com.bkpp.signals.*;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class MainController implements Initializable {
    @FXML
    VBox parameters;
    @FXML
    ComboBox<String> signalChoiceBox;
    @FXML
    ComboBox<Signal> signalsToSave;
    @FXML
    ComboBox<Signal> firstSignals;
    @FXML
    ComboBox<Signal> secondSignals;
    @FXML
    ComboBox<Signal> signalsToQuantization;
    @FXML
    Label avgValue;
    @FXML
    Label absoluteAvgValue;
    @FXML
    Label power;
    @FXML
    Label variance;
    @FXML
    Label effectiveValue;
    @FXML
    Label meanSquaredError;
    @FXML
    Label signalNoiseRatio;
    @FXML
    Label peakSignalNoiseRation;
    @FXML
    Label maximalDifference;
    @FXML
    LineChart<Double, Double> chart;
    @FXML
    TextField quantizationBytes;
    @FXML
    TextField quantizationSampling;

    private HashMap<String, Signal> signalNames;
    private DynamicParameters dynamicParameters;
    private ResourceBundle resourceBundle;
    private ObservableList<Signal> generatedSignals;
    private Converter converter;

    //region initializations
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        initializeSignalsName();
        initializeSignalChoiceBox();
        initializeComboBoxBindings();
    }

    private void initializeSignalsName() {
        signalNames = new HashMap<>();

        signalNames.put("Szum o rozkladzie jednostajnym", new UnvaryingNoise());
        signalNames.put("Szum gaussowski", new GaussianNoise());
        signalNames.put("Sygnal sinusoidalny", new SinusoidalSignal());
        signalNames.put("Sygnal sinusoidalny wyprostowany jednopolowkowo", new HalfStrightSinusoidalSignal());
        signalNames.put("Sygnal sinusoidalny wyprostowany dwupolowkowo", new StraightSinusoidalSignal());
        signalNames.put("Sygnal prostokatny", new RectangleSignal());
        signalNames.put("Sygnal prostokatny symetryczny", new SymetricRectangleSignal());
        signalNames.put("Sygnal trojkatny", new TriangleSignal());
        signalNames.put("Skok jednostkowy", new UnitaryJump());
        signalNames.put("Impuls jednostkowy", new UnitaryImpulse());
        signalNames.put("Szum impulsowy", new ImpulseNoise());
    }

    private void initializeSignalChoiceBox() {
        for (String name : signalNames.keySet()) {
            signalChoiceBox.getItems().add(name);
        }
    }

    private void initializeComboBoxBindings() {
        generatedSignals = FXCollections.observableArrayList();
        ListProperty<Signal> listProperty = new SimpleListProperty<>();
        listProperty.set(generatedSignals);

        signalsToSave.itemsProperty().bind(listProperty);
        firstSignals.itemsProperty().bind(listProperty);
        secondSignals.itemsProperty().bind(listProperty);
        signalsToQuantization.itemsProperty().bind(listProperty);
    }
    //endregion

    //region onActions
    public void saveSignal(ActionEvent actionEvent) {
        Signal pickedSignal = signalsToSave.getValue();

        if (pickedSignal != null) {
            saveSignalToFile(pickedSignal);
        } else {
            showErrorDialog("Wybierz sygnal do zapisania");
        }

    }

    public void loadSignal(ActionEvent actionEvent) {
        loadSignalFromFile();
    }

    public void generateSignal(ActionEvent actionEvent) {
        try {
            Signal pickedSignal = getInitializedPickedSignal();
            saveSignal(pickedSignal);
            fillGuiWith(pickedSignal);
        } catch (NumberFormatException | IllegalAccessException e) {
            showErrorDialog("Bledne dane wejsciowe");
        }
    }

    public void clearChart(ActionEvent actionEvent) {
        clearChart();
        clearGeneratedSignals();
    }

    public void addSignals(ActionEvent actionEvent) {
        Signal firstSignal = firstSignals.getValue();
        Signal secondSignal = secondSignals.getValue();

        if (firstSignal != null && secondSignal != null) {
            saveResultOfOperation(SignalUtils.addition(firstSignal, secondSignal));
        } else {
            showErrorDialog("Nalezy wybrac dwa sygnaly aby wykonac operacje");
        }
    }

    public void subtractSignals(ActionEvent actionEvent) {
        Signal firstSignal = firstSignals.getValue();
        Signal secondSignal = secondSignals.getValue();

        if (firstSignal != null && secondSignal != null) {
            saveResultOfOperation(SignalUtils.subtraction(firstSignal, secondSignal));
        } else {
            showErrorDialog("Nalezy wybrac dwa sygnaly aby wykonac operacje");
        }
    }

    public void multiplySignals(ActionEvent actionEvent) {
        Signal firstSignal = firstSignals.getValue();
        Signal secondSignal = secondSignals.getValue();

        if (firstSignal != null && secondSignal != null) {
            saveResultOfOperation(SignalUtils.multiplication(firstSignal, secondSignal));
        } else {
            showErrorDialog("Nalezy wybrac dwa sygnaly aby wykonac operacje");
        }
    }

    public void divideSignals(ActionEvent actionEvent) {
        Signal firstSignal = firstSignals.getValue();
        Signal secondSignal = secondSignals.getValue();

        if (firstSignal != null && secondSignal != null) {
            saveResultOfOperation(SignalUtils.division(firstSignal, secondSignal));
        } else {
            showErrorDialog("Nalezy wybrac dwa sygnaly aby wykonac operacje");
        }
    }

    public void onSignalChosen(ActionEvent actionEvent) {
        Signal pickedSignal = signalNames.get(signalChoiceBox.getValue());

        this.dynamicParameters = new DynamicParameters(pickedSignal, resourceBundle);
        parameters.getChildren().clear();
        parameters.getChildren().add(dynamicParameters);
    }

    public void quantization(ActionEvent actionEvent) {
        Signal signal = signalsToQuantization.getValue();
        int bytes = getQuantizationBytes();
        int sampling = getQuantizationSampling();

        converter = new Converter(signal, bytes);

        fillChartWith(makeSteppedPoints(converter.quantization(sampling)), "kwantyzacja");
    }

    public void reconstruction(ActionEvent actionEvent) {
        try {
            fillChartWith(converter.reconstruction(), "rekonstrukcja");
            showMeanSquaredError(converter.meanSquaredError(),
                    converter.signalNoiseRatio(),
                    converter.peakSignalNoiseRatio(),
                    converter.maximalDifference());
        } catch (RuntimeException r) {
            showErrorDialog("Aby zrekonstruowac sygnal nalezy wpierw przeprowadzic kwantyzacje");
        }
    }
    //endregion

    //region signalGUI
    private Signal getInitializedPickedSignal() throws IllegalAccessException {
        Signal pickedSignal = dynamicParameters.getInitializedSignal();
        pickedSignal.computePoints();

        return pickedSignal;
    }

    private void fillGuiWith(Signal signal) {
        fillChartWith(signal);
        fillValuesWith(signal);
        this.openHistogram(signal.getPoints());
    }
    //endregion

    //region generatedSignals
    private void saveSignal(Signal signal) {
        Signal signalCopy = SerializationUtils.clone(signal);

        generatedSignals.add(signalCopy);
    }

    private void saveResultOfOperation(Signal result) {
        saveSignal(result);
        fillGuiWith(result);
    }

    private void clearGeneratedSignals() {
        generatedSignals.clear();
    }
    //endregion

    //region signalsFileOperations
    private void saveSignalToFile(Signal signal) {
        File file = fileChooser().showSaveDialog(App.getPrimaryStage());
        if (file != null) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath())) {
                SerializationUtils.serialize(signal, fileOutputStream);
            } catch (Exception e) {
                showErrorDialog("Nie udalo sie zapisac sygnalu do pliku");
            }
        }
    }

    private void loadSignalFromFile() {
        File file = fileChooser().showOpenDialog(App.getPrimaryStage());
        if (file != null) {
            try (FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath())) {
                Signal signal = SerializationUtils.deserialize(fileInputStream);
                saveSignal(signal);
                fillGuiWith(signal);
            } catch (Exception e) {
                showErrorDialog("Nie udalo się zaladowac sygnalu");
            }
        }
    }

    private FileChooser fileChooser() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Wybierz plik");
        File defaultDirectory = new File(Paths.get(".").toAbsolutePath().normalize().toString());
        chooser.setInitialDirectory(defaultDirectory);

        return chooser;
    }
    //endregion

    //region signalValues
    private void fillValuesWith(Signal signal) {
        showValues(SignalUtils.averageValue(signal),
                SignalUtils.absoluteAverageValue(signal),
                SignalUtils.power(signal),
                SignalUtils.variance(signal),
                SignalUtils.effectiveValue(signal));
    }

    private void showValues(double avg, double v2, double power, double variance, double v3) {
        this.avgValue.setText(String.format("Wartosc srednia \n %g", avg));
        this.absoluteAvgValue.setText(String.format("Bez. wart. srednia \n %g", v2));
        this.power.setText(String.format("Moc \n %g", power));
        this.variance.setText(String.format("Wariacja \n %g", variance));
        this.effectiveValue.setText(String.format("Wartosc skuteczna \n %g", v3));
    }
    //endregion

    //region chart
    private void clearChart() {
        chart.getData().clear();
    }

    private void fillChartWith(Signal signal) {
        fillChartWith(signal.getPoints(), signal.toString());
    }

    private void fillChartWith(List<Point> points, String seriesName) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();

        series.setName(seriesName);

        for (Point point : points) {
            series.getData().add(new XYChart.Data<>(point.getX(), point.getY()));
        }

        chart.getData().add(series);
    }
    //endregion

    //region alerts
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Ooops, pojawil sie problem!");
        alert.setContentText(message);

        alert.showAndWait();
    }
    //endregion

    //region histogram
    private void openHistogram(List<Point> points) {
        Stage stage = new Stage();

        Scene scene = new Scene(new HistogramController(points));

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("CPS Pawel Pomaranski & Bartosz Kacperski");
        stage.show();
    }

    //endregion

    //region quantization
    private int getQuantizationBytes() {
        return Integer.valueOf(quantizationBytes.getText());
    }

    private int getQuantizationSampling() {
        return Integer.valueOf(quantizationSampling.getText());
    }

    private void showMeanSquaredError(double meanSquaredErrorValue,
                                      double signalNoiseRatioValue,
                                      double peakSignalNoiseRationValue,
                                      double maximalDifferenceValue) {
        meanSquaredError.setText("MSE: " + meanSquaredErrorValue);
        signalNoiseRatio.setText("SNR: " + signalNoiseRatioValue);
        peakSignalNoiseRation.setText("PSNR: " + peakSignalNoiseRationValue);
        maximalDifference.setText("MD: " + maximalDifferenceValue);
    }

    private List<Point> makeSteppedPoints(List<Point> points) {
        List<Point> steppedPoints = new ArrayList<>();

        for (int i = 0;i < points.size() - 1;i++) {
            Point currentPoint = points.get(i);
            Point nextPoint = points.get(i + 1);

            steppedPoints.add(currentPoint);

            double halfX = Math.abs(nextPoint.getX() + currentPoint.getX()) / 2.0;

            steppedPoints.add(new Point(halfX, currentPoint.getY()));
            steppedPoints.add(new Point(halfX, nextPoint.getY()));

            steppedPoints.add(nextPoint);
        }

        return steppedPoints;
    }

    //endregion
}
