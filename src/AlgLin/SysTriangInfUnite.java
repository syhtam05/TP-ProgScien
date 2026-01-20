package AlgLin;

// Classe pour L (diagonale unité)
class SysTriangInfUnite extends SysTriangInf {
    public SysTriangInfUnite(Matrice M, Vecteur b) throws Exception { super(M, b); }
    
    @Override
    public Vecteur resolution() throws IrregularSysLinException {
        Vecteur x = new Vecteur(ordre);
        for (int i = 0; i < ordre; i++) {
            double somme = 0;
            for (int j = 0; j < i; j++) {
                somme += matriceSystem.getCoef(i, j) * x.getCoef(j);
            }
            // On ne divise pas par diag car diag = 1
            x.remplacecoef(i, secondMembre.getCoef(i) - somme);
        }
        return x;
    }
}