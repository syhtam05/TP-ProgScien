package AlgLin;

public abstract class SysLin {
    protected int ordre;
    protected Matrice matriceSystem;
    protected Vecteur secondMembre;

    public SysLin(Matrice M, Vecteur b) throws Exception {
        if (M.nbLigne() != M.nbColonne()) {
            throw new Exception("La matrice doit être carrée.");
        }
        if (M.nbLigne() != b.taille()) {
            throw new Exception("La matrice et le vecteur doivent avoir la même dimension.");
        }
        this.ordre = M.nbLigne();
        this.matriceSystem = M;
        this.secondMembre = b;
    }

    public int getOrdre() { return ordre; }
    public Matrice getMatriceSystem() { return matriceSystem; }
    public Vecteur getSecondMembre() { return secondMembre; }
    public void setSecondMembre(Vecteur b) { this.secondMembre = b; }

    public abstract Vecteur resolution() throws IrregularSysLinException;
}