package com.bkpp;

import com.bkpp.signals.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    Label parameterLabel;
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
    ComboBox<String> signalChoiceBox;

    private HashMap<String, Signal> signalNames;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeSignalsName();
        initializeChoiceBox();
    }

    public void saveSignal(ActionEvent actionEvent) {
        Signal pickedSignal = signalNames.get(signalChoiceBox.getValue());
        saveSignalToFile(pickedSignal);
    }

    public void loadSignal(ActionEvent actionEvent) {
        loadSignalFromFile();
    }

    public void generateSignal(ActionEvent actionEvent) {
        try {
            Signal pickedSignal = getInitializedPickedSignal();
            fillGuiWith(pickedSignal);
        } catch (NumberFormatException e) {
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

        fillChartWith(SignalUtils.addition(firstSignal, secondSignal), "Dodawanie");
    }

    public void subtractSignals(ActionEvent actionEvent) {
        Signal firstSignal = firstSignals.getValue();
        Signal secondSignal = secondSignals.getValue();

        fillChartWith(SignalUtils.subtraction(firstSignal, secondSignal), "Odejmowanie");
    }

    public void multiplySignals(ActionEvent actionEvent) {
        Signal firstSignal = firstSignals.getValue();
        Signal secondSignal = secondSignals.getValue();

        fillChartWith(SignalUtils.multiplication(firstSignal, secondSignal), "Mnozenie");
    }

    public void divideSignals(ActionEvent actionEvent) {
        Signal firstSignal = firstSignals.getValue();
        Signal secondSignal = secondSignals.getValue();

        fillChartWith(SignalUtils.division(firstSignal, secondSignal), "Dzielenie");
    }

    public void onSignalChosen(ActionEvent actionEvent) {
        Signal pickedSignal = signalNames.get(signalChoiceBox.getValue());

        if (pickedSignal instanceof UnitaryJump) {
            parameter.setPromptText("Czas skoku");
            parameterLabel.setText("Czas skoku");
        } else if (pickedSignal instanceof UnitaryImpulse) {
            parameter.setPromptText("Numer probki");
            parameterLabel.setText("Numer probki");
        } else {
            parameter.setPromptText("Prawdopodobienstwo");
            parameterLabel.setText("Prawdopodobienstwo");
        }

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

    private Signal getInitializedPickedSignal() {
        Signal pickedSignal = signalNames.get(signalChoiceBox.getValue());

        if (frequency.getText() != null && !frequency.getText().isEmpty()) {
            pickedSignal.setFrequency(getFrequency());
        }

        pickedSignal.setAmplitude(getAmplitude());
        pickedSignal.setDuration(getDuration());
        pickedSignal.setStartTime(getStartTime());

        if (pickedSignal instanceof TermSignal) {
            ((TermSignal) pickedSignal).setTerm(getTerm());
        }

        if (pickedSignal instanceof FillFactorTermSignal) {
            ((FillFactorTermSignal) pickedSignal).setFillFactor(getFillFactor());
        }

        if (pickedSignal instanceof DiscreteSignal) {
            ((DiscreteSignal) pickedSignal).setParameter(getParameter());
        }

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
        try (FileOutputStream fileOutputStream = new FileOutputStream(chooseFile().getAbsolutePath());) {
            SerializationUtils.serialize(signal, fileOutputStream);
        } catch (IOException e) {
            showErrorDialog("Nie udalo sie zapisac sygnalu do pliku");
        }
    }

    private void loadSignalFromFile() {
        try (FileInputStream fileInputStream = new FileInputStream(chooseFile().getAbsolutePath())) {
            Signal signal = SerializationUtils.deserialize(fileInputStream);
            fillGuiWith(signal);
        } catch (IOException e) {
            showErrorDialog("Nie udalo się zaladowac sygnalu");
        }
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

    private void fillGuiWith(Signal signal) {
        fillChartWith(signal);
        fillValuesWith(signal);
        this.openHistogram(signal.getPoints());
    }

    private void fillValuesWith(Signal signal) {
        avgValue.setText(String.format("Wartosc srednia \n %g", SignalUtils.averageValue(signal)));
        absoluteAvgValue.setText(String.format("Bez. wart. srednia \n %g", SignalUtils.absoluteAverageValue(signal)));
        power.setText(String.format("Moc \n %g", SignalUtils.power(signal)));
        variance.setText(String.format("Wariacja \n %g", SignalUtils.variance(signal)));
        effectiveValue.setText(String.format("Wartosc skuteczna \n %g", SignalUtils.effectiveValue(signal)));
    }

    private void clearChart() {
        chart.getData().clear();
    }

    private void fillChartWith(Signal signal) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();

        series.setName(signal.toString() + " #" + chart.getData().size());

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

    private File chooseFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Wybierz plik");
        File defaultDirectory = new File(Paths.get(".").toAbsolutePath().normalize().toString());
        chooser.setInitialDirectory(defaultDirectory);
        return chooser.showOpenDialog(App.getPrimaryStage());
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
