package AlgLin;

public class ModPoly {
    private double[] a; // Coefficients du polynôme
    private int degre;  // Degré m

    public ModPoly(int degre) {
        this.degre = degre;
        this.a = new double[degre + 1];
    }

    public void identifie(double[] x, double[] y) {
        int m = degre + 1;
        int n = x.length;

        // Matrice du système normal (M) et vecteur second membre (B)
        double[][] M = new double[m][m];
        double[] B = new double[m];

        // Remplissage des moindres carrés
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                double xi_pow_i = Math.pow(x[j], i);
                B[i] += y[j] * xi_pow_i;
                for (int k = 0; k < m; k++) {
                    M[i][k] += xi_pow_i * Math.pow(x[j], k);
                }
            }
        }

        // Résolution par élimination de Gauss
        this.a = pivotDeGauss(M, B);
    }

    private double[] pivotDeGauss(double[][] A, double[] B) {
        int n = B.length;

        for (int p = 0; p < n; p++) {
            // Recherche du pivot max
            int max = p;
            for (int i = p + 1; i < n; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) max = i;
            }

            // Échange des lignes
            double[] temp = A[p]; A[p] = A[max]; A[max] = temp;
            double t = B[p]; B[p] = B[max]; B[max] = t;

            // Pivotage
            for (int i = p + 1; i < n; i++) {
                double alpha = A[i][p] / A[p][p];
                B[i] -= alpha * B[p];
                for (int j = p; j < n; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }

        // Remontée (Back-substitution)
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (B[i] - sum) / A[i][i];
        }
        return x;
    }

    public double evaluate(double xVal) {
        double res = 0;
        for (int i = 0; i <= degre; i++) {
            res += a[i] * Math.pow(xVal, i);
        }
        return res;
    }
}