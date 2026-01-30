package AlgLin;

public class SysDiagonal extends SysLin {

    public SysDiagonal(Matrice M, Vecteur b) throws IrregularSysLinException {
        super(M, b);
    }

    @Override
    public Vecteur resolution() throws IrregularSysLinException {
        Vecteur x = new Vecteur(ordre);
        for (int i = 0; i < ordre; i++) {
            double diag = matriceSystem.getCoef(i, i);
            if (Math.abs(diag) < 1.0E-12) {
                throw new IrregularSysLinException("Système irrégulier : zéro sur la diagonale à l'indice " + i);
            }
            x.remplacecoef(i, secondMembre.getCoef(i) / diag);
        }
        return x;
    }

    public static void main(String[] args) throws IrregularSysLinException {
        double[][] mData = { { 2, 0 }, { 0, 4 } };
        double[] bData = { 10, 20 };
        
        Matrice M = new Matrice(mData);
        Vecteur b = new Vecteur(bData);
        SysDiagonal sys = new SysDiagonal(M, b);
        Vecteur sol = sys.resolution();
        
        System.out.println("getOrdre :\n" + sys.getOrdre() + "\n");
        System.out.println("getMatriceSystem :\n" + sys.getMatriceSystem());
        System.out.println("getSecondMembre :\n" + sys.getSecondMembre());
        sys.setSecondMembre(sol);
        System.out.println("setSecondMembre :\n" + sys.getSecondMembre());
        System.out.println("Solution du système diagonal :\n" + sol);
        Vecteur.testSolution(M, sol, b);
    }
}
