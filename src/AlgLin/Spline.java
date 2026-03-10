package AlgLin;

import java.util.Arrays;
import java.util.Comparator;

public class Spline {
    private final double[] x;
    private final double[] y;
    private double[] m; // Dérivées secondes

    public Spline(double[] xInput, double[] yInput) {
        // On s'assure que les points sont triés par abscisses
        int n = xInput.length;
        Double[][] points = new Double[n][2];
        for (int i = 0; i < n; i++) {
            points[i][0] = xInput[i];
            points[i][1] = yInput[i];
        }
        Arrays.sort(points, Comparator.comparingDouble(a -> a[0]));

        this.x = new double[n];
        this.y = new double[n];
        for (int i = 0; i < n; i++) {
            this.x[i] = points[i][0];
            this.y[i] = points[i][1];
        }

        // Calcul automatique des dérivées secondes à l'instanciation
        this.m = calculateSecondDerivatives();
    }

    /**
     * Méthode privée résolvant le système tridiagonal pour les splines naturelles.
     */
    private double[] calculateSecondDerivatives() {
        int n = x.length;
        double[] h = new double[n - 1];
        for (int i = 0; i < n - 1; i++) h[i] = x[i + 1] - x[i];

        double[] a = new double[n];
        double[] b = new double[n];
        double[] c = new double[n];
        double[] d = new double[n];

        // Conditions aux limites pour spline naturelle (m0 = mn = 0)
        b[0] = 1; b[n - 1] = 1;

        for (int i = 1; i < n - 1; i++) {
            a[i] = h[i - 1] / 6.0;
            b[i] = (h[i - 1] + h[i]) / 3.0;
            c[i] = h[i] / 6.0;
            d[i] = (y[i + 1] - y[i]) / h[i] - (y[i] - y[i - 1]) / h[i - 1];
        }

        // Algorithme de Thomas pour résoudre le système tridiagonal
        double[] m_res = new double[n];
        for (int i = 1; i < n; i++) {
            double w = a[i] / b[i - 1];
            b[i] = b[i] - w * c[i - 1];
            d[i] = d[i] - w * d[i - 1];
        }

        m_res[n - 1] = d[n - 1] / b[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            m_res[i] = (d[i] - c[i] * m_res[i + 1]) / b[i];
        }
        return m_res;
    }

    public double evaluate(double val) throws DataOutOfRangeException {
        if (val < x[0] || val > x[x.length - 1]) {
            throw new DataOutOfRangeException(val, x[0], x[x.length - 1]);
        }

        // Recherche du segment i
        int i = 0;
        while (i < x.length - 2 && val > x[i + 1]) i++;

        double h = x[i + 1] - x[i];
        double t1 = m[i] * Math.pow(x[i + 1] - val, 3) / (6 * h);
        double t2 = m[i + 1] * Math.pow(val - x[i], 3) / (6 * h);
        double t3 = (y[i] - m[i] * h * h / 6) * (x[i + 1] - val) / h;
        double t4 = (y[i + 1] - m[i + 1] * h * h / 6) * (val - x[i]) / h;

        return t1 + t2 + t3 + t4;
    }

    public double getXMin() { return x[0]; }
    public double getXMax() { return x[x.length - 1]; }
}