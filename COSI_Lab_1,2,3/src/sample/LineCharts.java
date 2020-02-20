package sample;

import javafx.scene.chart.*;

public class LineCharts {

    final NumberAxis xAxis_brightness = new NumberAxis();
    final NumberAxis yAxis_brightness = new NumberAxis();
    final LineChart<Number, Number> lineChart = new LineChart<>(xAxis_brightness, yAxis_brightness);
    XYChart.Series signal = new XYChart.Series();

    LineCharts(double[] x, double[] y, String name, int length) {
        lineChart.setTitle(name);
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(true);
        for (int i = 0; i < length; i++) {
            signal.getData().add(new XYChart.Data(x[i], y[i]));
        }
        lineChart.getData().add(signal);
    }

    public LineChart<Number, Number> getLineChart() {
        return lineChart;
    }

}
