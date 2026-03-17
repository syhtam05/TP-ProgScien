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
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class ModPolyApp extends JFrame {

    public ModPolyApp(ModPoly model, double[] xOrig, double[] yOrig, int degre) {
        super("Modèle Linéaire - Régression Polynomiale degré " + degre);

        XYSeries polySeries = new XYSeries("Modèle p(x)");
        XYSeries pointsSeries = new XYSeries("Points de Support");

        double minX = xOrig[0], maxX = xOrig[0];
        for (int i = 0; i < xOrig.length; i++) {
            pointsSeries.add(xOrig[i], yOrig[i]);
            if (xOrig[i] < minX) minX = xOrig[i];
            if (xOrig[i] > maxX) maxX = xOrig[i];
        }

        // Générer 200 points pour la courbe lissée
        double step = (maxX - minX) / 199.0;
        for (int i = 0; i < 200; i++) {
            double curX = minX + i * step;
            polySeries.add(curX, model.evaluate(curX));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(polySeries);  // Index 0
        dataset.addSeries(pointsSeries); // Index 1

        JFreeChart chart = ChartFactory.createScatterPlot("Ajustement Moindres Carrés", "X", "Y", dataset);
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        // Courbe : Ligne bleue sans points
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        // Points : Croix rouges sans ligne
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesShape(1, ShapeUtilities.createDiagonalCross(4f, 1f));
        renderer.setSeriesPaint(1, Color.RED);

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);

        setContentPane(new ChartPanel(chart));
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.useLocale(Locale.US);

        System.out.print("Nom du fichier : ");
        String file = sc.next();
        System.out.print("Degré du polynôme : ");
        int d = sc.nextInt();

        try (Scanner fs = new Scanner(new File(file))) {
            fs.useLocale(Locale.US);
            ArrayList<Double> xl = new ArrayList<>(), yl = new ArrayList<>();
            while (fs.hasNextDouble()) {
                xl.add(fs.nextDouble());
                yl.add(fs.nextDouble());
            }

            double[] xa = xl.stream().mapToDouble(Double::doubleValue).toArray();
            double[] ya = yl.stream().mapToDouble(Double::doubleValue).toArray();

            ModPoly model = new ModPoly(d);
            model.identifie(xa, ya);

            SwingUtilities.invokeLater(() -> new ModPolyApp(model, xa, ya, d).setVisible(true));
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}