package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import static java.lang.Math.sin;


public class Main extends Application {

    private final static int N = 256;
    private double[] points = new double[N];
    private double[] data = new double[N];
    private double[] dataSin = new double[N];
    private double[] lowBlackManCoeefs = new double[4 * N / 5];
    private double[] highBlackManCoeefs = new double[4 * N / 5];
    private double[] sumHighLow = new double[4 * N / 5];
    private Complex[] fftSum = new Complex[4 * N / 5];
    private double[] filterPoints = new double[4 * N / 5];
    private double[] chebyshev = new double[N];
    private Complex[] fft = new Complex[N];

    @Override
    public void start(Stage primaryStage) {
        Controller controller = new Controller();

        for (int i = 0; i < N; i++) {
            points[i] = (double) i / N;
            data[i] = sin(120 * i * 2 * Math.PI / N) + sin(60 * i * 2 * Math.PI / N) + sin(i * 2 * Math.PI / N);
        }
        for (int i = 0; i < N; i++) {
            dataSin[i] = sin(60 * i * 2 * Math.PI / N) + sin(i * 2 * Math.PI / N);
        }
        for (int i = 0; i < 4 * N / 5; i++) {
            filterPoints[i] = (double) i / (4 * N / 5);
        }
        fft = controller.FFT(controller.ConvertDoubleToComplex(data), true);
        lowBlackManCoeefs = controller.lowpassBlackmanCoeffs(4 * N / 5, 50d / N);
        highBlackManCoeefs = controller.lowToHigh(lowBlackManCoeefs.clone());
        for (int i = 0; i < 4 * N / 5; i++) {
            sumHighLow[i] = lowBlackManCoeefs[i] + highBlackManCoeefs[i];
        }
        for (int i = 0; i < N; i++) {
            data[i] = round3(data[i]);
        }
        fftSum = controller.ConvertDoubleToComplex(sumHighLow, N);
        fftSum = controller.FFT(fftSum, true);
        chebyshev = controller.chebyshev(data);

        double P = 0.0;
        double temp = 0.0;
        for (int i = 0; i < N; i++) {
            temp += Math.pow(fftSum[i].getR(), 2);
        }
        P = temp / N;
        double PF = 0.0;
        for (int i = 0; i < N; i++) {
            temp += Math.pow(fft[i].getR(), 2);
        }
        PF = temp / N;
        System.out.println(P + " - Not filtered");
        System.out.println(PF + " - Filtered");

        LineCharts lineCharts = new LineCharts(points, data, "sin120 + sin60 + sin", N);
        lineCharts.lineChart.setCreateSymbols(false);
        LineCharts fftChart = new LineCharts(points, controller.getAbsolute(N, fft), "Исходная амплитудно-частотная", N);
        fftChart.lineChart.setCreateSymbols(false);
        LineCharts chebyshevChart = new LineCharts(points, chebyshev, "Фильтрованная временная(Chebyshev)", N);
        chebyshevChart.lineChart.setCreateSymbols(false);
        LineCharts y = new LineCharts(filterPoints, sumHighLow, "Режекторный - импульсная", 4 * N / 5);
        y.lineChart.setCreateSymbols(false);
        LineCharts b = new LineCharts(points, controller.getAbsolute(N, fftSum), "Режекторный - амплитудна-частотная", N);
        b.lineChart.setCreateSymbols(false);
        LineCharts z = new LineCharts(points, dataSin, "sin60+sin", N);
        z.lineChart.setCreateSymbols(false);
        LineCharts x = new LineCharts(points, controller.filterFFT(controller.getAbsolute(N, fft), controller.getAbsolute(N, fftSum)), "Фильтрованная амплитудно-частотная", N);
        x.lineChart.setCreateSymbols(false);

        GridPane gridPane = new GridPane();
        gridPane.add(z.getLineChart(), 0, 0);
        gridPane.add(y.getLineChart(), 0, 1);
        gridPane.add(x.getLineChart(), 2, 2);
        gridPane.add(lineCharts.getLineChart(), 1, 0);
        gridPane.add(fftChart.getLineChart(), 2, 0);
        gridPane.add(b.getLineChart(), 2, 1);
        gridPane.add(chebyshevChart.getLineChart(), 1, 1);
        primaryStage.setScene(new Scene(gridPane, 1500, 700));
        primaryStage.show();
    }

    public static double round3(double value) {
        int iValue = (int) (value * 1000);
        double dValue = value * 1000;
        if (dValue - iValue >= 0.5) {
            iValue += 1;
        }
        dValue = (double) iValue;
        return dValue / 1000;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
