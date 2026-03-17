package AlgLin;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class SplineApp extends JFrame {

    public SplineApp(Spline spline, double[] xOrig, double[] yOrig) {
        super("Interpolation par Spline Cubique");

        // 1. Création des séries de données
        XYSeries splineSeries = new XYSeries("Spline Cubique");
        XYSeries pointsSeries = new XYSeries("Points de Support");

        // Ajouter les points de support originaux
        for (int i = 0; i < xOrig.length; i++) {
            pointsSeries.add(xOrig[i], yOrig[i]);
        }

        // Évaluer la spline sur 200 points pour une courbe bien lisse
        double start = spline.getXMin();
        double end = spline.getXMax();
        int nbPoints = 200; 
        double step = (end - start) / (nbPoints - 1);

        for (int i = 0; i < nbPoints; i++) {
            double curX = start + i * step;
            try {
                splineSeries.add(curX, spline.evaluate(curX));
            } catch (Exception e) {
                // On ignore les erreurs mineures d'arrondi aux bornes
            }
        }

        // 2. Assemblage du dataset
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(splineSeries); // Index 0
        dataset.addSeries(pointsSeries); // Index 1

        // 3. Création du graphique de base
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Interpolation par Spline Cubique", 
                "Axe X", 
                "Axe Y", 
                dataset
        );

        // 4. Personnalisation du rendu (Ligne vs Croix)
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        // --- Configuration Série 0 (Spline) : Ligne bleue sans points ---
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        // --- Configuration Série 1 (Points) : Croix rouges sans ligne ---
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesPaint(1, Color.RED);
        // On définit la forme comme une croix diagonale (X)
        Shape croix = ShapeUtilities.createDiagonalCross(4f, 1f);
        renderer.setSeriesShape(1, croix);

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // 5. Affichage
        setContentPane(new ChartPanel(chart));
        setSize(900, 700);
        setLocationRelativeTo(null); // Centre la fenêtre
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Entrez le nom du fichier de données (ex: data.txt) : ");
        String fileName = sc.nextLine();

        try {
            File file = new File(fileName);
            Scanner fileScanner = new Scanner(file);
            fileScanner.useLocale(Locale.US); // Pour lire les nombres avec des points (ex: 1.5)

            ArrayList<Double> xList = new ArrayList<>();
            ArrayList<Double> yList = new ArrayList<>();

            while (fileScanner.hasNextDouble()) {
                xList.add(fileScanner.nextDouble());
                if (fileScanner.hasNextDouble()) {
                    yList.add(fileScanner.nextDouble());
                }
            }
            fileScanner.close();

            if (xList.size() < 3) { // 15 points était votre condition, 3 est le min mathématique
                System.out.println("Erreur : Il faut plus de points pour une spline.");
                return;
            }

            double[] xa = xList.stream().mapToDouble(Double::doubleValue).toArray();
            double[] ya = yList.stream().mapToDouble(Double::doubleValue).toArray();

            // Initialisation de votre classe Spline
            Spline maSpline = new Spline(xa, ya);

            SwingUtilities.invokeLater(() -> {
                new SplineApp(maSpline, xa, ya).setVisible(true);
            });

        } catch (FileNotFoundException e) {
            System.err.println("Fichier introuvable : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}