package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main extends Application {

    private File min_filter(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        int width = image.getWidth();
        int height = image.getHeight();
        int index = 0;
        int[] min_red = new int[9];
        int[] min_green = new int[9];
        int[] min_blue = new int[9];
        int[][] mat = new int[image.getHeight()][image.getWidth()];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        int x = i + k;
                        int y = j + l;
                        if (x > 0 && y > 0 && x < height && y < width) {
//                            System.out.println(x + " " + y);
                            Color color = new Color(image.getRGB(y, x));
                            min_red[index] = color.getRed();
                            index++;
                        }
                    }
                }
                index = 0;
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        int x = i + k;
                        int y = j + l;
                        if (x > 0 && y > 0 && x < height && y < width) {
                            Color color = new Color(image.getRGB(y, x));
                            min_green[index] = color.getGreen();
                            index++;

                        }
                    }
                }
                index = 0;
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        int x = i + k;
                        int y = j + l;
                        if (x > 0 && y > 0 && x < height && y < width) {
                            Color color = new Color(image.getRGB(y, x));
                            min_blue[index] = color.getBlue();
                            index++;
                        }
                    }
                }
                index = 0;
                int min_red_element = getMinElement(min_red);
                int min_greeb_element = getMinElement(min_green);
                int min_blue_element = getMinElement(min_blue);
                Color newColor = new Color(min_red_element, min_greeb_element, min_blue_element);
                mat[i][j] = newColor.getRGB();
                min_red = new int[9];
                min_green = new int[9];
                min_blue = new int[9];
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image.setRGB(j, i, mat[i][j]);
            }
        }
        File output = new File(file.getName() + "-converted.png");
        ImageIO.write(image, "png", output);
        return output;
    }

    private File max_filter(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        int width = image.getWidth();
        int height = image.getHeight();
        int index = 0;
        int[] min_red = new int[9];
        int[] min_green = new int[9];
        int[] min_blue = new int[9];
        int[][] mat = new int[image.getHeight()][image.getWidth()];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        int x = i + k;
                        int y = j + l;
                        if (x > 0 && y > 0 && x < height && y < width) {

//                            System.out.println(x + " " + y);
                            Color color = new Color(image.getRGB(y, x));
                            min_red[index] = color.getRed();
                            index++;
                        }
                    }
                }
                index = 0;
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        int x = i + k;
                        int y = j + l;
                        if (x > 0 && y > 0 && x < height && y < width) {
                            Color color = new Color(image.getRGB(y, x));
                            min_green[index] = color.getGreen();
                            index++;

                        }
                    }
                }
                index = 0;
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        int x = i + k;
                        int y = j + l;
                        if (x > 0 && y > 0 && x < height && y < width) {
                            Color color = new Color(image.getRGB(y, x));
                            min_blue[index] = color.getBlue();
                            index++;
                        }
                    }
                }
                index = 0;
                int min_red_element = getMaxElement(min_red);
                int min_greeb_element = getMaxElement(min_green);
                int min_blue_element = getMaxElement(min_blue);
                Color newColor = new Color(min_red_element, min_greeb_element, min_blue_element);
                mat[i][j] = newColor.getRGB();
                min_red = new int[9];
                min_green = new int[9];
                min_blue = new int[9];
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image.setRGB(j, i, mat[i][j]);
            }
        }
        File output = new File(file.getName() + "-converted.png");
        ImageIO.write(image, "png", output);
        return output;
    }

    private static TextField textField1 = new TextField("150");
    private static TextField textField2 = new TextField("50");

    private static int g_max = Integer.parseInt(textField1.getText());
    private static int g_min = Integer.parseInt(textField2.getText());

    private File pref(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        int width = image.getWidth();
        int height = image.getHeight();
        int red = 0;
        int green = 0;
        int blue = 0;
        g_max = Integer.parseInt(textField1.getText());
        g_min = Integer.parseInt(textField2.getText());
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
//                            System.out.println(x + " " + y);
                Color color = new Color(image.getRGB(j, i));
                red = color.getRed();
                green = color.getGreen();
                blue = color.getBlue();

                if ((red + green + blue) / 3 >= g_max) {
                    red = 255;
                    green = 255;
                    blue = 255;
                } else if ((red + green + blue) / 3 <= g_min) {
                    red = 0;
                    green = 0;
                    blue = 0;
                }

                Color newColor = new Color(red, green, blue);
                image.setRGB(j, i, newColor.getRGB());
            }
        }

        File output = new File(file.getName() + "-converted.png");
        ImageIO.write(image, "png", output);
        return output;
    }

    private int getMinElement(int array[]) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < array.length; i++) {
            if (array[i] != 0) {
                list.add(array[i]);
            }
            if (list.isEmpty()) {
                return 0;
            }
        }
        return Collections.max(list);
    }

    private int getMaxElement(int array[]) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < array.length; i++) {
            if (array[i] != 0) {
                list.add(array[i]);
            }
            if (list.isEmpty()) {
                return 0;
            }
        }
        return Collections.min(list);
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {

        ImageView imageView = new ImageView();
        Button button1 = new Button("ORIGINAL");
        Button button2 = new Button("MIN");
        Button button4 = new Button("MAX");
        Button button5 = new Button("MIN-MAX");
        Button button3 = new Button("PREP");

        final CategoryAxis xAxis_red = new CategoryAxis();
        final NumberAxis yAxis_red = new NumberAxis();
        final BarChart<String, Number> redHistogram
                = new BarChart<>(xAxis_red, yAxis_red);
        redHistogram.setAnimated(true);
        redHistogram.setTitle("1.png");
        redHistogram.setCategoryGap(0);
        redHistogram.setBarGap(0);

        final CategoryAxis xAxis_green = new CategoryAxis();
        final NumberAxis yAxis_green = new NumberAxis();
        final BarChart<String, Number> greenHistogram
                = new BarChart<>(xAxis_green, yAxis_green);
        greenHistogram.setAnimated(true);
        greenHistogram.setTitle("1.png");
        greenHistogram.setCategoryGap(0);
        greenHistogram.setBarGap(0);

        final CategoryAxis xAxis_blue = new CategoryAxis();
        final NumberAxis yAxis_blue = new NumberAxis();
        final BarChart<String, Number> blueHistogram
                = new BarChart<>(xAxis_blue, yAxis_blue);
        blueHistogram.setAnimated(true);
        blueHistogram.setTitle("1.png");
        blueHistogram.setCategoryGap(0);
        blueHistogram.setBarGap(0);

        File file = new File("1.png");
        FileInputStream fileInputStream = new FileInputStream(file);
        Image image = new Image(fileInputStream);
        imageView.setImage(image);
        redHistogram.getData().clear();
        greenHistogram.getData().clear();
        blueHistogram.getData().clear();
        ImageHistogram imageHistogram = new ImageHistogram(image);
        if (imageHistogram.isSuccess()) {
            redHistogram.getData().add(imageHistogram.getSeriesRed());
            greenHistogram.getData().add(imageHistogram.getSeriesGreen());
            blueHistogram.getData().add(imageHistogram.getSeriesBlue());
        }

        button1.setOnAction(value -> {
            try {
                File file2 = new File("1.png");
                FileInputStream fileInputStream2 = new FileInputStream(file2);
                Image image2 = new Image(fileInputStream2);
                imageView.setImage(image2);
                redHistogram.getData().clear();
                greenHistogram.getData().clear();
                blueHistogram.getData().clear();
                ImageHistogram imageHistogram2 = new ImageHistogram(image2);
                if (imageHistogram.isSuccess()) {
                    redHistogram.getData().add(imageHistogram2.getSeriesRed());
                    greenHistogram.getData().add(imageHistogram2.getSeriesGreen());
                    blueHistogram.getData().add(imageHistogram2.getSeriesBlue());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        });

        button2.setOnAction(value -> {
            try {
                File file2 = new File("1.png");
                FileInputStream fileInputStream2 = new FileInputStream(min_filter(file2));
                Image image2 = new Image(fileInputStream2);
                imageView.setImage(image2);
                redHistogram.getData().clear();
                greenHistogram.getData().clear();
                blueHistogram.getData().clear();
                ImageHistogram imageHistogram2 = new ImageHistogram(image2);
                if (imageHistogram2.isSuccess()) {
                    redHistogram.getData().add(imageHistogram2.getSeriesRed());
                    greenHistogram.getData().add(imageHistogram2.getSeriesGreen());
                    blueHistogram.getData().add(imageHistogram2.getSeriesBlue());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        button4.setOnAction(value -> {
            try {
                File file2 = new File("1.png");
                FileInputStream fileInputStream2 = new FileInputStream(max_filter(file2));
                Image image2 = new Image(fileInputStream2);
                imageView.setImage(image2);
                redHistogram.getData().clear();
                greenHistogram.getData().clear();
                blueHistogram.getData().clear();
                ImageHistogram imageHistogram2 = new ImageHistogram(image2);
                if (imageHistogram2.isSuccess()) {
                    redHistogram.getData().add(imageHistogram2.getSeriesRed());
                    greenHistogram.getData().add(imageHistogram2.getSeriesGreen());
                    blueHistogram.getData().add(imageHistogram2.getSeriesBlue());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        button5.setOnAction(value -> {
            try {
                File file2 = new File("1.png");
                FileInputStream fileInputStream2 = new FileInputStream(max_filter(min_filter(file2)));
                Image image2 = new Image(fileInputStream2);
                imageView.setImage(image2);
                redHistogram.getData().clear();
                greenHistogram.getData().clear();
                blueHistogram.getData().clear();
                ImageHistogram imageHistogram2 = new ImageHistogram(image2);
                if (imageHistogram2.isSuccess()) {
                    redHistogram.getData().add(imageHistogram2.getSeriesRed());
                    greenHistogram.getData().add(imageHistogram2.getSeriesGreen());
                    blueHistogram.getData().add(imageHistogram2.getSeriesBlue());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        button3.setOnAction(value -> {
            try {
                File file2 = new File("1.png");
                FileInputStream fileInputStream2 = new FileInputStream(pref(file2));
                Image image2 = new Image(fileInputStream2);
                imageView.setImage(image2);
                redHistogram.getData().clear();
                greenHistogram.getData().clear();
                blueHistogram.getData().clear();
                ImageHistogram imageHistogram2 = new ImageHistogram(image2);
                if (imageHistogram2.isSuccess()) {
                    redHistogram.getData().add(imageHistogram2.getSeriesRed());
                    greenHistogram.getData().add(imageHistogram2.getSeriesGreen());
                    blueHistogram.getData().add(imageHistogram2.getSeriesBlue());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        HBox hBox1 = new HBox();
        hBox1.getChildren().addAll(button1, button2, button3, button4, button5, textField1, textField2);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(redHistogram, greenHistogram, blueHistogram, hBox1);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(imageView, vBox);

        StackPane root = new StackPane();
        root.getChildren().addAll(hBox);

        Scene scene = new Scene(root, 1300, 840);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
