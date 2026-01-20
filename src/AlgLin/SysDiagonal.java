package AlgLin;

public class SysDiagonal extends SysLin {

    public SysDiagonal(Matrice M, Vecteur b) throws Exception {
        super(M, b);
    }

    @Override
    public Vecteur resolution() throws IrregularSysLinException {
        Vecteur x = new Vecteur(ordre);
        for (int i = 0; i < ordre; i++) {
            double diag = matriceSystem.getCoef(i, i);
            if (Math.abs(diag) < 1.0E-12) { // Proche de zéro
                throw new IrregularSysLinException("Système irrégulier : zéro sur la diagonale à l'indice " + i);
            }
            x.remplacecoef(i, secondMembre.getCoef(i) / diag);
        }
        return x;
    }

    public static void main(String[] args) throws Exception {
        double[][] data = { { 2, 0 }, { 0, 4 } };
        double[] bData = { 10, 20 };
        SysDiagonal sys = new SysDiagonal(new Matrice(data), new Vecteur(bData));
        Vecteur sol = sys.resolution();
        System.out.println("Solution du système diagonal :\n" + sol);
    }
}
