package AlgLin;

public class Thomas extends SysLin {

    public Thomas(Mat3Diag M, Vecteur b) throws IrregularSysLinException {
	super(M, b);
    }

    @Override
    public Vecteur resolution() throws IrregularSysLinException {
	int n = this.ordre;
	Vecteur x = new Vecteur(n);

	// c[0] -> Sur-diagonale | c[1] -> Diagonale principale | c[2] -> Sous-diagonale
	double[][] c = this.matriceSystem.coefficient;

	// Tableaux de travail pour stocker les coefficients modifiés (c' et d')
	double[] cp = new double[n]; // Stocke la nouvelle sur-diagonale après élimination
	double[] dp = new double[n]; // Stocke le nouveau second membre après élimination

	// --- PHASE 1 : DESCENTE ---

	// Initialisation pour la première ligne (i = 0)
	// On vérifie que le premier pivot n'est pas nul
	if (Math.abs(c[1][0]) < Matrice.EPSILON)
	    throw new IrregularSysLinException("Pivot nul au début du balayage");

	// Calcul du premier coefficient de la sur-diagonale modifiée : c'_0 = c_0 / b_0
	cp[0] = c[0][0] / c[1][0];
	// Calcul du premier terme du second membre modifié : d'_0 = d_0 / b_0
	dp[0] = secondMembre.getCoef(0) / c[1][0];

	for (int i = 1; i < n; i++) {
	    // Le dénominateur correspond au pivot courant : b_i - a_i * c'_{i-1}
	    double denom = c[1][i] - c[2][i] * cp[i - 1];

	    if (Math.abs(denom) < Matrice.EPSILON)
		throw new IrregularSysLinException("Système singulier ou division par zéro à l'étape " + i);

	    // Mise à jour de la sur-diagonale (sauf pour la dernière ligne)
	    if (i < n - 1) {
		cp[i] = c[0][i] / denom;
	    }

	    // Mise à jour du second membre : d'_i = (d_i - a_i * d'_{i-1})
	    dp[i] = (secondMembre.getCoef(i) - c[2][i] * dp[i - 1]) / denom;
	}

	// --- PHASE 2 : REMONTÉE ---

	// La dernière inconnue est trouvée directement : x_{n-1} = d'_{n-1}
	x.remplacecoef(n - 1, dp[n - 1]);

	// On remonte le système pour trouver les autres inconnues de n-2 à 0
	// Formule : x_i = d'_i - c'_i * x_{i+1}
	for (int i = n - 2; i >= 0; i--) {
	    x.remplacecoef(i, dp[i] - cp[i] * x.getCoef(i + 1));
	}

	return x;
    }

    public static void main(String[] args) throws IrregularSysLinException {
	double[][] data = { { 1, 1, 1, 0 }, // L0 : Sur-diag
		{ 4, 4, 4, 4 }, // L1 : Diag
		{ 0, 1, 1, 1 } // L2 : Sous-diag
	};

	Mat3Diag M = new Mat3Diag(data);
	System.out.println(M);
	Vecteur b = new Vecteur(new double[] { 5, 6, 6, 5 });

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