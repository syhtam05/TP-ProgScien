package AlgLin;

public class SysTriangInf extends SysLin {

    public SysTriangInf(Matrice M, Vecteur b) throws Exception {
        super(M, b);
    }

    @Override
    public Vecteur resolution() throws IrregularSysLinException {
        Vecteur x = new Vecteur(ordre);
        
        for (int i = 0; i < ordre; i++) {
            double diag = matriceSystem.getCoef(i, i);
            if (Math.abs(diag) < Matrice.EPSILON) {
                throw new IrregularSysLinException("Pivot nul à la ligne " + i);
            }
            
            double somme = 0;
            for (int j = 0; j < i; j++) {
                somme += matriceSystem.getCoef(i, j) * x.getCoef(j);
            }
            
            double val = (secondMembre.getCoef(i) - somme) / diag;
            x.remplacecoef(i, val);
        }
        return x;
    }

    public static void main(String[] args) throws Exception {
        double[][] mData = {{1, 0}, {4, 1}}; // x = 2; 4x + y = 5
        double[] bData = {2, 5};
        
        Matrice M = new Matrice(mData);
        Vecteur b = new Vecteur(bData);
        SysTriangInf sys = new SysTriangInf(M, b);
        
        Vecteur sol = sys.resolution();
        System.out.println("Solution Inférieure :\n" + sol);
        Vecteur.testSolution(M, sol, b);
    }
}