package AlgLin;

class SysTriangSupUnite extends SysTriangSup {
    public SysTriangSupUnite(Matrice M, Vecteur b) throws Exception {
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
}