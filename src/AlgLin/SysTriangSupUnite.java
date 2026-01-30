package AlgLin;

class SysTriangSupUnite extends SysTriangSup {
    public SysTriangSupUnite(Matrice M, Vecteur b) throws IrregularSysLinException {
        super(M, b);
    }

    @Override
    public Vecteur resolution() throws IrregularSysLinException {
        Vecteur x = new Vecteur(ordre);
        for (int i = ordre - 1; i >= 0; i--) {
            double somme = 0;
            for (int j = i + 1; j < ordre; j++) {
                somme += matriceSystem.getCoef(i, j) * x.getCoef(j);
            }
            // On ne divise pas par diag car diag = 1
            x.remplacecoef(i, secondMembre.getCoef(i) - somme);
        }
        return x;
    }
    
    public static void main(String[] args) throws IrregularSysLinException {
        double[][] mData = { { 1, 5 }, { 0, 1 } }; // x + 5y = 2; y = 5
        double[] bData = { 2, 5 };

        Matrice M = new Matrice(mData);
        Vecteur b = new Vecteur(bData);
        SysTriangSupUnite sys = new SysTriangSupUnite(M, b);
        Vecteur sol = sys.resolution();
        
        System.out.println("getOrdre :\n" + sys.getOrdre() + "\n");
        System.out.println("getMatriceSystem :\n" + sys.getMatriceSystem());
        System.out.println("getSecondMembre :\n" + sys.getSecondMembre());
        sys.setSecondMembre(sol);
        System.out.println("setSecondMembre :\n" + sys.getSecondMembre());
        System.out.println("Solution Inférieure :\n" + sol);
        Vecteur.testSolution(M, sol, b);
    }
}