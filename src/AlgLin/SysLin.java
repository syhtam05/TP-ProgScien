package AlgLin;

public abstract class SysLin {
    private int ordre;
    private Matrice matriceSystem;
    private Vecteur secondMembre;
    
    public SysLin(Matrice matriceSystem, Vecteur secondMembre) throws IrregularSysLinException {
	if ((matriceSystem.nbColonne() != matriceSystem.nbLigne()) || (matriceSystem.nbLigne() == secondMembre.taille())) {
	    throw new IrregularSysLinException("Matrice non carre ou second membre pas de meme temps que la matrice");
	}
	    
	this.matriceSystem = matriceSystem;
	this.secondMembre = secondMembre;
    }

    public Vecteur getSecondMembre() {
        return secondMembre;
    }

    public void setSecondMembre(Vecteur secondMembre) {
        this.secondMembre = secondMembre;
    }

    public int getOrdre() {
        return ordre;
    }

    public Matrice getMatriceSystem() {
        return matriceSystem;
    }
    
    abstract public Vecteur resolution() throws IrregularSysLinException;
}
