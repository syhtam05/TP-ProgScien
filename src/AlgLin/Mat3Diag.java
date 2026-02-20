package AlgLin;

public class Mat3Diag extends Matrice {

    public Mat3Diag(int dim1, int dim2) {
	super(dim1, dim2);
	if (dim1 != 3)
	    throw new RuntimeException("Le stockage doit avoir 3 lignes.");
    }

    public Mat3Diag(double[][] tableau) {
	super(tableau);
	if (tableau.length != 3)
	    throw new RuntimeException("Le tableau doit avoir 3 lignes.");
    }

    public Mat3Diag(int dim) {
	super(3, dim);
    }

    @Override
    public int nbLigne() {
	return this.coefficient[0].length;
    }

    @Override
    public int nbColonne() {
	return this.coefficient[0].length;
    }

    @Override
    public double getCoef(int i, int j) {
	if (i == j)
	    return coefficient[1][i]; // Diagonale (Ligne 1)
	if (i == j - 1)
	    return coefficient[0][i]; // Sur-diagonale (Ligne 0)
	if (i == j + 1)
	    return coefficient[2][i]; // Sous-diagonale (Ligne 2)
	return 0.0;
    }

    public static Vecteur produit(Mat3Diag M, Vecteur V) {
	int n = M.nbLigne();
	if (n != V.taille()) {
	    throw new RuntimeException("Dimensions incompatibles pour le produit Mat-Vect.");
	}

	Vecteur resultat = new Vecteur(n);
	double[][] c = M.coefficient; //[0:sur, 1:diag, 2:sous]

	for (int i = 0; i < n; i++) {
	    double somme = 0;

	    // 1. Terme de la sous-diagonale (Ligne 2)
	    if (i > 0) {
		somme += c[2][i] * V.getCoef(i - 1);
	    }

	    // 2. Terme de la diagonale principale (Ligne 1)
	    somme += c[1][i] * V.getCoef(i);

	    // 3. Terme de la sur-diagonale (Ligne 0)
	    if (i < n - 1) {
		somme += c[0][i] * V.getCoef(i + 1);
	    }

	    resultat.remplacecoef(i, somme);
	}

	return resultat;
    }
    
    public static void main(String[] args) {
	    double[][] data = {
	        {1, 1, 1, 0}, // Sur-diagonale
	        {2, 2, 2, 2}, // Diagonale principale
	        {0, 1, 1, 1}  // Sous-diagonale
	    };
	    
	    Mat3Diag M = new Mat3Diag(data);
	    System.out.println(M);
	    
	    // Vecteur V = [1, 1, 1, 1]
	    Vecteur V = new Vecteur(new double[]{1, 1, 1, 1});

	    // Calcul du produit
	    Vecteur res = Mat3Diag.produit(M, V);

	    System.out.println("Produit M * V :");
	    System.out.println(res);
	    
	    /* Attendu :
	       Ligne 0 : (2*1) + (1*1)          = 3
	       Ligne 1 : (1*1) + (2*1) + (1*1)  = 4
	       Ligne 2 : (1*1) + (2*1) + (1*1)  = 4
	       Ligne 3 : (1*1) + (2*1)          = 3
	    */
	}
}