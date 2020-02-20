package sample;

public class Complex {
    private double r;
    private double img;

    public Complex() {
        this.r = 0;
        this.img = 0;
    }

    public Complex(double r) {
        this.r = r;
        this.img = 0;
    }

    public Complex(double r, double img) {
        this.r = r;
        this.img = img;
    }

    public String toString() {
        if (this.img == 0) return this.r + "";
        if (this.r == 0) return this.img + "i";
        if (this.img < 0) return this.r + " - " + (-this.img) + "i";
        return r + " + " + img + "i";
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getImg() {
        return img;
    }

    public void setImg(double img) {
        this.img = img;
    }

    public Complex pairing() {
        return new Complex(this.r, -1 * this.img);
    }

    public Complex sumComplex(Complex add) {
        return new Complex(this.r + add.r, this.img + add.img);
    }

    public Complex mulComplex(Complex mul) {
        double real = this.r * mul.r - this.img * mul.img;
        double imaginary = this.r * mul.img + this.img * mul.r;
        return new Complex(real, imaginary);
    }

    public Complex mulComplex(double number) {
        return new Complex(this.r * number, this.img * number);
    }

    public Complex divNumber(int N) {
        return new Complex(this.r / N, this.img / N);
    }

    public Complex subComplex(Complex sub) {
        return new Complex(this.r - sub.r, this.img - sub.img);
    }

}
