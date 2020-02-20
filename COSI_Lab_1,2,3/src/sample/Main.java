package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main extends Application {

    private int N = 8;
    private double[] arg = new double[N];
    private double[] original = new double[N];
    private double[] sin = new double[N];
    private double[] cos = new double[N];
    private Complex[] dft = new Complex[N];
    private double[] idft = new double[N];
    private Complex[] fft = new Complex[N];
    private double[] ifft = new double[N];

    private double[] correlation = new double[N];
    private double[] correlationFFT = new double[N];
    private Complex[] corrFFTSIN = new Complex[N];
    private Complex[] corrFFTCOS = new Complex[N];
    private double[] convolution = new double[N];
    private Complex[] convFFTSIN = new Complex[N];
    private Complex[] convFFTCOS = new Complex[N];
    private double[] convolutionFFT = new double[N];

    private Complex[] fwt_values = new Complex[N];
    private double[] ifwt_values = new double[N];

    private Complex[] dwt_values_rademacher = new Complex[N];
    private double[] idwt_values_rademacher = new double[N];


    @Override
    public void start(Stage primaryStage) throws IOException {

        Controller controller = new Controller();

        for (int i = 0; i < N; i++) {
            arg[i] = (2 * i * Math.PI) / N;
            original[i] = Math.cos(arg[i]) + Math.sin(arg[i]);
        }
        for (int i = 0; i < N; i++) {
            arg[i] = (2 * i * Math.PI) / N;
            cos[i] = Math.cos(1 * arg[i]);
            sin[i] = Math.sin(1 * arg[i]);
        }

        Scanner in = new Scanner(System.in);
        System.out.print("Input a number: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        var lab = Integer.parseInt(reader.readLine());

        dft = controller.DFT(controller.ConvertDoubleToComplex(original), true, N);
        idft = controller.ConvertComplexToDouble(controller.DFT(dft, false, N));
        fft = controller.FFT(controller.ConvertDoubleToComplex(original), true);
        ifft = controller.ConvertComplexToDouble(controller.FFT(dft, false));

        correlation = controller.corr(cos, sin);
        corrFFTSIN = controller.FFT(controller.ConvertDoubleToComplex(sin), true);
        corrFFTCOS = controller.FFT(controller.ConvertDoubleToComplex(cos), true);
        correlationFFT = controller.ConvertComplexToDouble(controller.corrFFT(corrFFTCOS, corrFFTSIN));
        convolution = controller.conv(cos, sin);
        convFFTSIN = controller.FFT(controller.ConvertDoubleToComplex(sin), true);
        convFFTCOS = controller.FFT(controller.ConvertDoubleToComplex(cos), true);
        convolutionFFT = controller.ConvertComplexToDouble(controller.convFFT(convFFTCOS, convFFTSIN));

        fwt_values = Controller.FastWalshTransformation(controller.ConvertDoubleToComplex(original), 1);
        ifwt_values = controller.ConvertComplexToDouble(Controller.FastWalshTransformation(fwt_values, -1));
        dwt_values_rademacher = Controller.DiscreteWalshTransformationHadamard_Rademacher(controller.ConvertDoubleToComplex(original), 1, 1);
        idwt_values_rademacher = controller.ConvertComplexToDouble(Controller.DiscreteWalshTransformationHadamard_Rademacher(dwt_values_rademacher, -1, 1));

        LineCharts lineCharts = new LineCharts(arg, original, "ORIGINAL", N);
        LineCharts dftChart = new LineCharts(arg, controller.getAbsolute(N, dft), "DFT", N);
        LineCharts dftPhase = new LineCharts(arg, controller.getFase(N, dft), "pahase DFT", N);
        LineCharts idftChart = new LineCharts(arg, controller.getAbsolute(N, fft), "IDFT", N);
        LineCharts fftChart = new LineCharts(arg, controller.getAbsolute(N, fft), "FFT", N);
        LineCharts fftPhase = new LineCharts(arg, controller.getFase(N, fft), "pahase DFT", N);
        LineCharts ifftChart = new LineCharts(arg, controller.getAbsolute(N, fft), "IDFT", N);

        LineCharts cosChart = new LineCharts(arg, cos, "cos(x)", N);
        LineCharts sinChart = new LineCharts(arg, sin, "sin(x)", N);

        LineCharts corrChart = new LineCharts(arg, correlation, "Corr", N);
        LineCharts corrfftChart = new LineCharts(arg, correlationFFT, "Corr FFT", N);
        LineCharts convChart = new LineCharts(arg, convolution, "Conv", N);
        LineCharts convfftChart = new LineCharts(arg, convolutionFFT, "Conv FFT", N);

        LineCharts absolute_fwt_signal = new LineCharts(arg, controller.getAbsolute(N, fwt_values), "FWT absolute values", N);
        LineCharts ifwt_signal = new LineCharts(arg, ifwt_values, "IFWT signal", N);
        LineCharts absolute_dwt_signal_rademacher = new LineCharts(arg, controller.getAbsolute(N, dwt_values_rademacher), "DWT Rademah absolute values", N);
        LineCharts idwt_signal_rademacher = new LineCharts(arg, idwt_values_rademacher, "IDWT Rademah signal", N);

        if (lab == 1) {
            GridPane gridPane = new GridPane();
            gridPane.add(lineCharts.getLineChart(), 0, 0);
            gridPane.add(dftChart.getLineChart(), 0, 1);
            gridPane.add(dftPhase.getLineChart(), 1, 1);
            gridPane.add(idftChart.getLineChart(), 2, 1);
            gridPane.add(fftChart.getLineChart(), 0, 2);
            gridPane.add(fftPhase.getLineChart(), 1, 2);
            gridPane.add(ifftChart.getLineChart(), 2, 2);
            Scene scene = new Scene(gridPane, 850, 700);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        if (lab == 2) {
            GridPane gridPane = new GridPane();
            gridPane.add(sinChart.getLineChart(), 0, 0);
            gridPane.add(cosChart.getLineChart(), 1, 0);
            gridPane.add(corrChart.getLineChart(), 0, 1);
            gridPane.add(corrfftChart.getLineChart(), 0, 2);
            gridPane.add(convChart.getLineChart(), 1, 1);
            gridPane.add(convfftChart.getLineChart(), 1, 2);
            Scene scene = new Scene(gridPane, 850, 700);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        if (lab == 3) {
            GridPane gridPane = new GridPane();
            gridPane.add(lineCharts.getLineChart(), 0, 0);
            gridPane.add(absolute_fwt_signal.getLineChart(), 0, 1);
            gridPane.add(absolute_dwt_signal_rademacher.getLineChart(), 1, 1);
            gridPane.add(ifwt_signal.getLineChart(), 0, 2);
            gridPane.add(idwt_signal_rademacher.getLineChart(), 1, 2);
            Scene scene = new Scene(gridPane, 850, 700);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
