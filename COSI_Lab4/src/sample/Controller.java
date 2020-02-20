package sample;

public class Controller {

    private final static Complex complex = new Complex();
    private static WalshFunctions walsh_functions = new WalshFunctions();
    double[] aSeq = new double[]{1.434449e-02, 8.606697e-02, 2.151674e-01, 2.868899e-01, 2.151674e-01, 8.606697e-02, 1.434449e-02};
    double[] bSeq = new double[]{1.076052, -1.662847, 1.191063, -7.403087e-01, 2.752158e-01, -5.722251e-02};

    public Complex[] DFT(Complex[] complexes, boolean dir, int N) {
        Complex[] result = new Complex[N];
        for (int i = 0; i < N; i++)
            result[i] = new Complex(0);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Complex w_n = new Complex(Math.cos((2 * Math.PI * j * i) / N),
                        (-1) * ((dir) ? 1 : -1) * Math.sin((2 * Math.PI * j * i) / N));
                result[i] = result[i].sumComplex(w_n.mulComplex(complexes[j]));
            }
            if (!dir) {
                result[i] = result[i].divNumber(N);
            }
        }
        return result;
    }

    public Complex[] FFT(Complex[] signal, boolean dir) {
        int N = signal.length;
        int base = 2;
        if (N == 1) {
            return signal;
        }
        Complex[] a1 = new Complex[N / 2];
        Complex[] a2 = new Complex[N / 2];
        for (int i = 0; i < N; i++) {
            if (i % 2 == 0) {
                a1[i / 2] = signal[i];
            } else {
                a2[i / 2] = signal[i];
            }
        }
        Complex[] b_even = FFT(a1, dir);
        Complex[] b_odd = FFT(a2, dir);
        Complex wN = new Complex(Math.cos((2 * Math.PI) / N),
                (-1) * ((dir) ? 1 : -1) * Math.sin((2 * Math.PI) / N));
        Complex w = new Complex(1);
        Complex[] y = new Complex[N];
        for (int i = 0; i < N / 2; i++) {
            if (dir) {
                y[i] = b_even[i].sumComplex(w.mulComplex(b_odd[i]));
                y[i + N / 2] = b_even[i].subComplex(w.mulComplex(b_odd[i]));
            }
            if (!dir) {
                y[i] = b_even[i].sumComplex(w.mulComplex(b_odd[i])).divNumber(base);
                y[i + N / 2] = b_even[i].subComplex(w.mulComplex(b_odd[i])).divNumber(base);
            }
            w = w.mulComplex(wN);
        }
        return y;
    }

    public double[] corr(double[] argCos, double[] argSin) {
        int N = argCos.length;
        double[] result = new double[N];
        for (int i = 0; i < N; i++) {
            result[i] = 0;
        }
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                int z = i + j;
                if (z >= N)
                    z -= N;
                result[i] += argCos[j] * argSin[z];
            }
        return result;
    }

    public double[] conv(double[] argCos, double[] argSin) {
        int N = argCos.length;
        double[] result = new double[N];
        for (int i = 0; i < N; i++)
            result[i] = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                int z = i - j;
                if (z < 0)
                    z += N;
                result[i] += argCos[j] * argSin[z];
            }
        return result;
    }

    public Complex[] corrFFT(Complex[] cos, Complex[] sin) {
        int N = cos.length;
        Complex[] signal_x_conjugated = new Complex[N];
        for (int i = 0; i < N; i++) {
            signal_x_conjugated[i] = cos[i];
        }
        Complex[] result_correlation_fft = new Complex[N];
        for (int i = 0; i < N; i++) {
            result_correlation_fft[i] = signal_x_conjugated[i].mulComplex(sin[i]);
        }
        return FFT(result_correlation_fft, false);
    }

    public Complex[] convFFT(Complex[] cos, Complex[] sin) {
        int N = cos.length;
        Complex[] result_convolution_fft = new Complex[N];
        for (int i = 0; i < N; i++) {
            result_convolution_fft[i] = cos[i].mulComplex(sin[i]);
        }
        return FFT(result_convolution_fft, false);
    }

    public static Complex[] FastWalshTransformation(Complex[] signal, int direction) {
        int N = signal.length;
        if (N == 1) {
            return new Complex[]{signal[0]};
        }

        Complex[] left = new Complex[N / 2];
        Complex[] right = new Complex[N / 2];

        for (int i = 0; i < N / 2; i++) {
            left[i] = signal[i].sumComplex(signal[i + N / 2]);
            right[i] = signal[i].subComplex(signal[i + N / 2]);
        }

        Complex[] buffer_left = FastWalshTransformation(left, direction);
        Complex[] buffer_right = FastWalshTransformation(right, direction);

        Complex[] result = new Complex[N];

        if (direction == 1) {
            for (int i = 0; i < N / 2; i++) {
                result[i] = buffer_left[i].divNumber(2);
                result[i + result.length / 2] = buffer_right[i].divNumber(2);
            }
        } else {
            for (int i = 0; i < N / 2; i++) {
                result[i] = buffer_left[i];
                result[i + result.length / 2] = buffer_right[i];
            }
        }

        return result;
    }

    public static Complex[] DiscreteWalshTransformationHadamard_Rademacher(Complex[] signal, int direction, int mode) {
        int N = signal.length;
        int[][] matrix_of_functions;
        if (mode == 0) {
            matrix_of_functions = walsh_functions.GetHadamardMarix();
        } else {
            matrix_of_functions = walsh_functions.GetWalshFunctionsByRademacher();
        }

        Complex[] result = new Complex[N];
        for (int i = 0; i < N; i++) {
            result[i] = new Complex(0);
        }

        for (int m = 0; m < N; m++) {
            for (int n = 0; n < N; n++) {
                result[m] = result[m].sumComplex(signal[n].mulComplex(matrix_of_functions[m][n]));
            }
            if (direction == 1) {
                result[m] = result[m].divNumber(N);
            }
        }

        return result;
    }


    public double[] getAbsolute(int N, Complex[] dft) {
        for (int i = 0; i < N; i++) {
            dft[i].setR(Math.round(dft[i].getR() * 100.0) / 100.0);
            dft[i].setImg(Math.round(dft[i].getImg() * 100.0) / 100.0);
        }
        double[] result = new double[N];
        for (int i = 0; i < N; i++) {
            result[i] = Math.sqrt(dft[i].getR() * dft[i].getR() + dft[i].getImg() * dft[i].getImg());
        }
        return result;
    }

    public double[] getFase(int N, Complex[] dft) {
        double[] result = new double[N];
        for (int i = 0; i < N; i++) {
            result[i] = Math.atan2(dft[i].getImg(), dft[i].getR());
        }
        return result;
    }

    public Complex[] ConvertDoubleToComplex(double[] values) {
        Complex[] result = new Complex[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = new Complex(values[i]);
        }
        return result;
    }

    public Complex[] ConvertDoubleToComplex(double[] values, int N) {
        Complex[] result = new Complex[N];
        for (int i = 0; i < N; i++) {
            if (i >= values.length) {
                result[i] = new Complex();
            } else {
                result[i] = new Complex(values[i]);
            }
        }
        return result;
    }

    public double[] ConvertComplexToDouble(Complex[] values) {
        double[] result = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i].getR();
        }
        return result;
    }

    public double[] lowpassBlackmanCoeffs(int m, double passFc) {
        double[] result = new double[m];
        for (int i = 0; i < m; i++) {
            if (i != m / 2) {
                result[i] = Math.sin(2 * Math.PI * passFc * (i - m / 2)) / (i - m / 2) * (0.42 - 0.5 * Math.cos(2 * Math.PI * i / m) + 0.08 * Math.cos(4 * Math.PI * i / m));
            } else
                result[i] = 2 * Math.PI * passFc;
        }
        var sum = 0.0;
        for (double r : result)
            sum += r;
        for (int i = 0; i < m; i++) {
            result[i] = result[i] / sum;
        }
        return result;
    }

    public double[] lowToHigh(double[] coeffs) {
        for (int i = 0; i < coeffs.length; i++) {
            if (i % 2 == 0) {
                coeffs[i] *= -1;
            }
        }
        return coeffs;
    }

    public double[] chebyshev(double[] xs) {
        double result[] = new double[xs.length];
        for (int i = 0; i < xs.length; i++) {
            switch (i) {
                case 0: {
                    result[i] = xs[0] * aSeq[0];
                    break;
                }
                case 1: {
                    result[i] = xs[1] * aSeq[0] + xs[0] * aSeq[1] + result[0] * bSeq[0];
                    break;
                }
                case 2: {
                    result[i] = xs[2] * aSeq[0] + xs[1] * aSeq[1] + xs[0] * aSeq[2] +
                            result[1] * bSeq[0] + result[0] * bSeq[1];
                    break;
                }
                case 3: {
                    result[i] = xs[3] * aSeq[0] + xs[2] * aSeq[1] + xs[1] * aSeq[2] + xs[0] * aSeq[3] +
                            result[2] * bSeq[0] + result[1] * bSeq[1] + result[0] * bSeq[2];
                    break;
                }
                case 4: {
                    result[i] = xs[4] * aSeq[0] + xs[3] * aSeq[1] + xs[2] * aSeq[2] + xs[1] * aSeq[3] +
                            xs[0] * aSeq[4] + result[3] * bSeq[0] + result[2] * bSeq[1] +
                            result[1] * bSeq[2] + result[0] * bSeq[3];
                    break;
                }
                default: {
                    result[i] = xs[i] * aSeq[0] + xs[i - 1] * aSeq[1] + xs[i - 2] * aSeq[2] +
                            xs[i - 3] * aSeq[3] + xs[i - 4] * aSeq[4] + xs[i - 5] * aSeq[5] +
                            result[i - 1] * bSeq[0] + result[i - 2] * bSeq[1] +
                            result[i - 3] * bSeq[2] + result[i - 4] * bSeq[3] + result[i - 5] * bSeq[4];
                    break;
                }
            }
        }
        return result;
    }

    public double[] filterFFT(double[] data, double[] filter) {
        double[] doubles = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            if (data[i] > filter[i]) {
                doubles[i] = filter[i];
            } else {
                doubles[i] = data[i];
            }
        }
        return doubles;
    }
}
