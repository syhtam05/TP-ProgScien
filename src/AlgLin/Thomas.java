package AlgLin;

public class Thomas extends SysLin {

    public Thomas(Mat3Diag M, Vecteur b) throws IrregularSysLinException {
	super(M, b);
    }

    @Override
    public Vecteur resolution() throws IrregularSysLinException {
	int n = this.ordre;
	Vecteur x = new Vecteur(n);
	double[][] c = this.matriceSystem.coefficient;

	double[] cp = new double[n];
	double[] dp = new double[n];

	// Phase d'élimination (Forward)
	// b[0] est en c[1][0]
	if (Math.abs(c[1][0]) < Matrice.EPSILON)
	    throw new IrregularSysLinException("Pivot nul");

	cp[0] = c[0][0] / c[1][0]; // sur-diag / diag
	dp[0] = secondMembre.getCoef(0) / c[1][0];

	for (int i = 1; i < n; i++) {
	    // denom = diag[i] - sous-diag[i] * cp[i-1]
	    double denom = c[1][i] - c[2][i] * cp[i - 1];
	    if (Math.abs(denom) < Matrice.EPSILON)
		throw new IrregularSysLinException("Système singulier");

	    if (i < n - 1) {
		cp[i] = c[0][i] / denom;
	    }
	    dp[i] = (secondMembre.getCoef(i) - c[2][i] * dp[i - 1]) / denom;
	}

	// Phase de substitution (Backward)
	x.remplacecoef(n - 1, dp[n - 1]);
	for (int i = n - 2; i >= 0; i--) {
	    x.remplacecoef(i, dp[i] - cp[i] * x.getCoef(i + 1));
	}

	return x;
    }

    public static void main(String[] args) throws IrregularSysLinException {
	    double[][] data = {
	        {1, 1, 1, 0}, // L0 : Sur-diag
	        {4, 4, 4, 4}, // L1 : Diag
	        {0, 1, 1, 1}  // L2 : Sous-diag
	    };

	    Mat3Diag M = new Mat3Diag(data);
	    Vecteur b = new Vecteur(new double[]{5, 6, 6, 5});

	    System.out.println("--- Test Thomas ---");
	    Thomas solver = new Thomas(M, b);
	    Vecteur x = solver.resolution();

	    System.out.println("Solution x :\n" + x);
	    
	    Vecteur Ax = Mat3Diag.produit(M, x);
	    
	    double erreur = 0;
	    for (int i = 0; i < x.taille(); i++) {
	        erreur += Math.abs(Ax.getCoef(i) - b.getCoef(i));
	    }

	    System.out.println("Vérification : Norme de l'erreur ||Ax - b|| = " + erreur);
	    
	    if (erreur < Matrice.EPSILON) {
	        System.out.println("Résultat VALIDÉ.");
	    } else {
	        System.out.println("Résultat INCORRECT.");
	    }
	}
}