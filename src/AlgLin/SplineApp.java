package AlgLin;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class SplineApp extends JFrame {

    public SplineApp(Spline spline, double[] xOrig, double[] yOrig) {
        XYSeries splineSeries = new XYSeries("Spline Cubique");
        XYSeries pointsSeries = new XYSeries("Points de Support");

        // 1. Ajouter les points de support
        for (int i = 0; i < xOrig.length; i++) {
            pointsSeries.add(xOrig[i], yOrig[i]);
        }

        // 2. Évaluer 100 points
        double start = spline.getXMin();
        double end = spline.getXMax();
        double step = (end - start) / 99.0;

        for (int i = 0; i < 100; i++) {
            double curX = start + i * step;
            try {
                splineSeries.add(curX, spline.evaluate(curX));
            } catch (DataOutOfRangeException e) {
                System.err.println(e.getMessage());
            }
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(splineSeries);
        dataset.addSeries(pointsSeries);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Interpolation par Spline Cubique", "X", "Y", dataset);
        
        // Note: Le rendu des lignes pour la spline se configure normalement 
        // via le Renderer du Plot de JFreeChart.

        setContentPane(new ChartPanel(chart));
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Entrez le nom du fichier de données : ");
        String fileName = sc.nextLine();
        
        Scanner fileScanner = new Scanner(new File(fileName));
    	fileScanner.useLocale(java.util.Locale.US); // Force la lecture des points (0.5)

        ArrayList<Double> xList = new ArrayList<>();
        ArrayList<Double> yList = new ArrayList<>();

        try (fileScanner) {
            while (fileScanner.hasNextDouble()) {
                xList.add(fileScanner.nextDouble());
                yList.add(fileScanner.nextDouble());
            }

            if (xList.size() < 15) {
                System.out.println("Erreur : Il faut au moins 15 points.");
                return;
            }

            double[] xa = xList.stream().mapToDouble(Double::doubleValue).toArray();
            double[] ya = yList.stream().mapToDouble(Double::doubleValue).toArray();

            Spline maSpline = new Spline(xa, ya);
            
            SwingUtilities.invokeLater(() -> {
                new SplineApp(maSpline, xa, ya).setVisible(true);
            });

        } catch (Exception e) {
            System.err.println("Erreur de lecture : " + e.getMessage());
        }
    }
}