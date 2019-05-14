package com.bkpp;

import com.bkpp.antena.Antenna;
import com.bkpp.converters.Converter;
import com.bkpp.signals.*;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.SerializationUtils;
import com.bkpp.filters.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    @FXML
    ComboBox<Signal> signalsToDistance;
    @FXML
    Label distance;
    @FXML
    TextField translation;
    @FXML
    TextField speed;
    @FXML
    ComboBox<Filter> filters;
    @FXML
    ComboBox<Window> windows;
    @FXML
    TextField filterFrequency;
    @FXML
    TextField filterM;
    @FXML
    ComboBox<Signal> signalsToFilter;
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
    VBox chartToHide;
    @FXML
    TextField quantizationBytes;
    @FXML
    TextField quantizationFrequency;
    @FXML
    TextField reconstructionNeighbour;

    private HashMap<String, Signal> signalNames;
    private DynamicParameters dynamicParameters;
    private ResourceBundle resourceBundle;
    private ObservableList<Signal> generatedSignals;
    private Converter converter;
    private Service<Signal> generateService;

    //region initializations
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        initializeSignalsName();
        initializeSignalChoiceBox();
        initializeComboBoxBindings();
        initService();
        initWindows();
        initFilters();
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
        signalsToFilter.itemsProperty().bind(listProperty);
        signalsToDistance.itemsProperty().bind(listProperty);
    }

    private void initWindows() {
        windows.getItems().add(new HanningWindow());
        windows.getItems().add(new RectangularWindow());
    }

    private void initFilters() {
        filters.getItems().add(new LowPassFilter());
        filters.getItems().add(new HighPassFilter());
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
        if (generateService.isRunning()) {
            generateService.cancel();
        }
        generateService.reset();
        generateService.start();
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

    public void convulsion(ActionEvent actionEvent) {
        Signal firstSignal = firstSignals.getValue();
        Signal secondSignal = secondSignals.getValue();

        if (firstSignal != null && secondSignal != null) {
            saveResultOfOperation(SignalUtils.convolution(firstSignal, secondSignal));
        } else {
            showErrorDialog("Nalezy wybrac dwa sygnaly aby wykonac operacje");
        }
    }

    public void correlation(ActionEvent actionEvent) {
        Signal firstSignal = firstSignals.getValue();
        Signal secondSignal = secondSignals.getValue();

        if (firstSignal != null && secondSignal != null) {
            saveResultOfOperation(SignalUtils.correlation(firstSignal, secondSignal));
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

    public void sampling(ActionEvent actionEvent) {
        Signal signal = signalsToQuantization.getValue();
        double frequency = getQuantizationFrequency();
        int bytes = getQuantizationBytes();

        converter = new Converter(signal, frequency, bytes);
        List<Point> sampledPoints = converter.samplePoints();

        fillChartWithoutLines(sampledPoints, "Probkowanie " + getConverterInfo());
    }

    public void quantization(ActionEvent actionEvent) {
        try {
            List<Point> quantizedPoints = converter.quantization();

            fillChartWithoutPoints(makeSteppedPoints(quantizedPoints), "Kwantyzacja " + getConverterInfo());
        } catch (RuntimeException r) {
            showErrorDialog("Aby przeprowadzic kwantyzacje naleze przeprowadzic probkowanie");
        }
    }

    public void reconstruction(ActionEvent actionEvent) {
        try {
            int neighbour = getReconstructionNeighbour();
            fillChartWithoutPoints(converter.reconstruction(neighbour), "Rekonstrukcja " + getConverterInfo() + " sasiedzi: " + neighbour);
            showSignalsErrorMetrics(converter.meanSquaredError(),
                    converter.signalNoiseRatio(),
                    converter.peakSignalNoiseRatio(),
                    converter.maximalDifference());
        } catch (RuntimeException r) {
            showErrorDialog("Aby zrekonstruowac sygnal nalezy wpierw przeprowadzic probkowanie");
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
                e.printStackTrace();
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
        chartToHide.getChildren().clear();
    }

    private void fillChartWith(Signal signal) {
        fillChartWithoutPoints(signal.getPoints(), signal.toString());
    }

    private void fillChartWithoutPoints(List<Point> points, String seriesName) {
        fillChartWith(points, seriesName);
        for (XYChart.Data data : chart.getData().get(chart.getData().size() - 1).getData()) {
            data.getNode().setStyle("-fx-background-color: #00000000;");
        }
    }

    private void fillChartWithoutLines(List<Point> points, String seriesName) {
        fillChartWith(points, seriesName);
        chart.getData().get(chart.getData().size() - 1).getNode().setStyle("-fx-stroke: transparent");
    }

    private void fillChartWith(List<Point> points, String seriesName) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();

        series.setName("#" + chart.getData().size() + " " + seriesName);

        for (Point point : points) {
            series.getData().add(new XYChart.Data<>(point.getX(), point.getY()));
        }

        chart.getData().add(series);

        addCheckBox("#" + (chart.getData().size() - 1));
    }

    private void addCheckBox(String title) {
        CheckBox checkBox = new CheckBox();
        checkBox.setPrefSize(100, 40);
        checkBox.setText(title);
        checkBox.setSelected(true);

        final int index = Integer.valueOf(title.replace("#", ""));
        checkBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                changeVisibilityOfChartWith(index, true);
            } else {
                changeVisibilityOfChartWith(index, false);
            }
        });

        chartToHide.getChildren().add(checkBox);
    }

    private void changeVisibilityOfChartWith(int index, boolean visibility) {
        chart.getData().get(index).getNode().setVisible(visibility);
        for (XYChart.Data data : chart.getData().get(index).getData()) {
            data.getNode().setVisible(visibility);
        }
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
    private String getConverterInfo() {
        return "Czestotliwosc: " + converter.getFrequency() +
                " bity: " + converter.getBytes();
    }

    private int getQuantizationBytes() {
        return Integer.valueOf(quantizationBytes.getText());
    }

    private double getQuantizationFrequency() {
        return Integer.valueOf(quantizationFrequency.getText());
    }

    private int getReconstructionNeighbour() {
        return Integer.valueOf(reconstructionNeighbour.getText());
    }

    private void showSignalsErrorMetrics(double meanSquaredErrorValue,
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

    //region async
    private void initService() {
        generateService = new Service<>() {
            @Override
            protected Task<Signal> createTask() {
                return getGeneratingSignalTask();
            }
        };

        generateService.setOnFailed(e -> {
            showErrorDialog("Bledne dane wejsciowe");
        });
        generateService.setOnSucceeded(e -> {
            saveSignal(generateService.getValue());
            fillGuiWith(generateService.getValue());
        });
    }

    private Task<Signal> getGeneratingSignalTask() {
        return new Task<>() {
            @Override
            protected Signal call() throws Exception {
                return getInitializedPickedSignal();
            }
        };
    }
    //endregion

    //region createFilter
    private int getFilterM() {
        return Integer.valueOf(filterM.getText());
    }

    private double getFilterFrequency() {
        return Double.valueOf(filterFrequency.getText());
    }

    public void filtering(ActionEvent actionEvent) {
        Signal chosenSignal = signalsToFilter.getValue();
        Window window = windows.getValue();
        Filter filter = filters.getValue();

        Signal filterSignal = new FilterCreator(getFilterM(), getFilterFrequency(), chosenSignal).createFilter(window, filter);
        Signal result = SignalUtils.convolution(filterSignal, chosenSignal);
        fillChartWithoutPoints(suitResult(chosenSignal, result, filter), "Wynik filtracji");
    }

    private List<Point> suitResult(Signal chosenSignal, Signal result, Filter filter) {
        double interval = (chosenSignal.getPoints().get(chosenSignal.getPoints().size() - 1).getX() - chosenSignal.getPoints().get(0).getX()) / (result.getPoints().size() - 1);
        double currentX = 0.0;

        //double amplitude = Collections.max(result.getPoints(), Comparator.comparingDouble(Point::getY)).getY() * 2.0;
        //amplitude = Collections.max(chosenSignal.getPoints(), Comparator.comparingDouble(Point::getY)).getY()/amplitude;
        System.out.println(result.getPoints());
        List<Point> resultPoints = new ArrayList<>();
        for (int i = 0;i < result.getPoints().size();i++) {
            resultPoints.add(new Point(currentX, result.getPoints().get(i).getY()));
            currentX += interval;
        }

        return resultPoints;
    }


    //endregion

    //region antenna
    Signal correlation;

    private double getSpeed() {
        return Double.valueOf(speed.getText());
    }

    private double getTranslation() {
        return Double.valueOf(translation.getText());
    }

    public void startAntenna(ActionEvent actionEvent) {
        Signal chosenSignal = signalsToDistance.getValue();
        Antenna antenna = new Antenna(chosenSignal, getSpeed(), getTranslation());
        Signal firstSignal = antenna.sendedSignal();
        Signal secondSignal = antenna.receiveSignal();
        correlation = SignalUtils.correlation(firstSignal, secondSignal);

        fillChartWith(firstSignal);
        fillChartWith(secondSignal);
        distance.setText("Dystans: " + countDistance(correlation, getSpeed(), chosenSignal));
    }

    public void drawCorrelation(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/CorrelationDialog.fxml"));
        Parent parent = loader.load();

        CorrelationDialog correlationDialog = loader.getController();
        correlationDialog.setCorrelation(correlation);
        correlationDialog.drawCorrelation();


        Scene scene = new Scene(parent, 1000, 600);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }

    private double countDistance(Signal correlation, double speed, Signal signal) {
        int correlationHalfSize = correlation.getPoints().size() / 2;

        List<Point> secondHalf = correlation.getPoints().stream().skip(correlationHalfSize).collect(Collectors.toList());

        int index = maxYIndex(secondHalf);

        double difference = (signal.getPoints().get(1).getX() - signal.getPoints().get(0).getX());
        double time = (correlationHalfSize - index) * difference;

        return speed * time / 2.0;
    }

    private int maxYIndex(List<Point> points) {
        double maxY = points.get(0).getY();
        int index = 0;

        for (int i = 0;i < points.size();i++) {
            Point currentPoint = points.get(i);
            if (currentPoint.getY() > maxY) {
                maxY = currentPoint.getY();
                index = i;
            }
        }

        return index;
    }


    //endregion
}
