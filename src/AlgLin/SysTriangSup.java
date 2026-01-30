package AlgLin;

public class SysTriangSup extends SysLin {

    public SysTriangSup(Matrice M, Vecteur b) throws IrregularSysLinException {
        super(M, b);
    }

    @Override
    public Vecteur resolution() throws IrregularSysLinException {
        Vecteur x = new Vecteur(ordre);

        for (int i = ordre - 1; i >= 0; i--) {
            double diag = matriceSystem.getCoef(i, i);
            if (Math.abs(diag) < Matrice.EPSILON) {
                throw new IrregularSysLinException("Pivot nul à la ligne " + i);
            }

            double somme = 0;
            for (int j = i + 1; j < ordre; j++) {
                somme += matriceSystem.getCoef(i, j) * x.getCoef(j);
            }

            double val = (secondMembre.getCoef(i) - somme) / diag;
            x.remplacecoef(i, val);
        }
        return x;
    }

    public static void main(String[] args) throws IrregularSysLinException {
        double[][] mData = { { 1, 2 }, { 0, 4 } }; // x + 2y = 5; 4y = 4
        double[] bData = { 5, 4 };

        Matrice M = new Matrice(mData);
        Vecteur b = new Vecteur(bData);
        SysTriangSup sys = new SysTriangSup(M, b);
        Vecteur sol = sys.resolution();
        
        System.out.println("getOrdre :\n" + sys.getOrdre() + "\n");
        System.out.println("getMatriceSystem :\n" + sys.getMatriceSystem());
        System.out.println("getSecondMembre :\n" + sys.getSecondMembre());
        sys.setSecondMembre(sol);
        System.out.println("setSecondMembre :\n" + sys.getSecondMembre());
        System.out.println("Solution Supérieure :\n" + sol);
        Vecteur.testSolution(M, sol, b);
    }
}