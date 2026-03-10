package AlgLin;

public class DataOutOfRangeException extends Exception {
    public DataOutOfRangeException(double x, double min, double max) {
        super("La valeur " + x + " est hors de l'intervalle de support [" + min + ", " + max + "].");
    }
}