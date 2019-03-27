package com.bkpp;

import com.bkpp.signals.*;
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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class MainController implements Initializable {
    @FXML
    VBox parameters;
    @FXML
    ComboBox<Signal> signalsToSave;
    @FXML
    Button saveSignal;
    @FXML
    Button loadSignal;
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
    Button multiply;
    @FXML
    Button divide;
    @FXML
    Button subtract;
    @FXML
    Button add;
    @FXML
    ComboBox<Signal> firstSignals;
    @FXML
    ComboBox<Signal> secondSignals;
    @FXML
    Button clear;
    @FXML
    Button generate;
    @FXML
    LineChart<Double, Double> chart;
    @FXML
    ComboBox<String> signalChoiceBox;

    private HashMap<String, Signal> signalNames;
    private DynamicParameters dynamicParameters;
    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        initializeSignalsName();
        initializeChoiceBox();
    }

    public void saveSignal(ActionEvent actionEvent) {
        Signal pickedSignal = signalsToSave.getValue();
        saveSignalToFile(pickedSignal);
    }

    public void loadSignal(ActionEvent actionEvent) {
        loadSignalFromFile();
    }

    public void generateSignal(ActionEvent actionEvent) {
        try {
            Signal pickedSignal = getInitializedPickedSignal();
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

        saveResultOfOperation(SignalUtils.addition(firstSignal, secondSignal), "Dodawanie");
    }

    public void subtractSignals(ActionEvent actionEvent) {
        Signal firstSignal = firstSignals.getValue();
        Signal secondSignal = secondSignals.getValue();

        saveResultOfOperation(SignalUtils.subtraction(firstSignal, secondSignal), "Odejmowanie");
    }

    public void multiplySignals(ActionEvent actionEvent) {
        Signal firstSignal = firstSignals.getValue();
        Signal secondSignal = secondSignals.getValue();

        saveResultOfOperation(SignalUtils.multiplication(firstSignal, secondSignal), "Mnozenie");
    }

    public void divideSignals(ActionEvent actionEvent) {
        Signal firstSignal = firstSignals.getValue();
        Signal secondSignal = secondSignals.getValue();

        saveResultOfOperation(SignalUtils.division(firstSignal, secondSignal), "Dzielenie");
    }


    public void saveResultOfOperation(Signal result, String operationName) {
        fillGuiWith(result);

        saveSignal(result);
    }

    public void onSignalChosen(ActionEvent actionEvent) {
        Signal pickedSignal = signalNames.get(signalChoiceBox.getValue());

        this.dynamicParameters = new DynamicParameters(pickedSignal, resourceBundle);
        parameters.getChildren().clear();
        parameters.getChildren().add(dynamicParameters);
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

    private void initializeChoiceBox() {
        for (String name : signalNames.keySet()) {
            signalChoiceBox.getItems().add(name);
        }
    }

    private Signal getInitializedPickedSignal() throws IllegalAccessException {
        Signal pickedSignal = dynamicParameters.getInitializedSignal();

        pickedSignal.computePoints();

        saveSignal(pickedSignal);

        return pickedSignal;
    }

    private void saveSignal(Signal signal) {
        Signal signalCopy = SerializationUtils.clone(signal);

        firstSignals.getItems().add(signalCopy);
        secondSignals.getItems().add(signalCopy);
        signalsToSave.getItems().add(signalCopy);
    }

    private void clearGeneratedSignals() {
        firstSignals.getItems().clear();
        secondSignals.getItems().clear();
        signalsToSave.getItems().clear();
    }

    private void saveSignalToFile(Signal signal) {
        File file = fileChooser().showSaveDialog(App.getPrimaryStage());
        try (FileOutputStream fileOutputStream = new FileOutputStream(Objects.requireNonNull(file).getAbsolutePath())) {
            SerializationUtils.serialize(signal, fileOutputStream);
        } catch (IOException e) {
            showErrorDialog("Nie udalo sie zapisac sygnalu do pliku");
        }

    }

    private void loadSignalFromFile() {
        File file = fileChooser().showOpenDialog(App.getPrimaryStage());
        try (FileInputStream fileInputStream = new FileInputStream(Objects.requireNonNull(file).getAbsolutePath())) {
            Signal signal = SerializationUtils.deserialize(fileInputStream);
            saveSignal(signal);
            fillGuiWith(signal);
        } catch (IOException e) {
            showErrorDialog("Nie udalo się zaladowac sygnalu");
        }
    }


    private void fillGuiWith(Signal signal) {
        fillChartWith(signal);
        fillValuesWith(signal);
        this.openHistogram(signal.getPoints());
    }


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


    private FileChooser fileChooser() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Wybierz plik");
        File defaultDirectory = new File(Paths.get(".").toAbsolutePath().normalize().toString());
        chooser.setInitialDirectory(defaultDirectory);

        return chooser;
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Ooops, pojawil sie problem!");
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void openHistogram(List<Point> points) {
        Stage stage = new Stage();

        Scene scene = new Scene(new HistogramController(points));

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("CPS Pawel Pomaranski & Bartosz Kacperski");
        stage.show();
    }
}
