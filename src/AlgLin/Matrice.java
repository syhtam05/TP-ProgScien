package AlgLin;

import java.io.*;
import java.util.*;
import java.lang.RuntimeException;

public class Matrice {
	/** Définir ici les attributs de la classe **/
	protected double coefficient[][];

	public final static double EPSILON = 1.0E-06;

	/** Définir ici les constructeur de la classe **/
	Matrice(int nbligne, int nbcolonne) {
		this.coefficient = new double[nbligne][nbcolonne];
	}

	Matrice(double[][] tableau) {
		coefficient = tableau;
	}

	Matrice(String fichier) {
		try {
			Scanner sc = new Scanner(new File(fichier));
			int ligne = sc.nextInt();
			int colonne = sc.nextInt();
			this.coefficient = new double[ligne][colonne];
			for (int i = 0; i < ligne; i++)
				for (int j = 0; j < colonne; j++)
					this.coefficient[i][j] = sc.nextDouble();
			sc.close();

		} catch (FileNotFoundException e) {
			System.out.println("Fichier absent");
		}
	}

	/** Definir ici les autres methodes */

	public void recopie(Matrice arecopier) {
		int ligne, colonne;
		ligne = arecopier.nbLigne();
		colonne = arecopier.nbColonne();
		this.coefficient = new double[ligne][colonne];
		for (int i = 0; i < ligne; i++)
			for (int j = 0; j < colonne; j++)
				this.coefficient[i][j] = arecopier.coefficient[i][j];
	}

	public int nbLigne() {
		return this.coefficient.length;
	}

	public int nbColonne() {
		return this.coefficient[0].length;
	}

	public double getCoef(int ligne, int colonne) {
		return this.coefficient[ligne][colonne];
	}

	public void remplacecoef(int ligne, int colonne, double value) {
		this.coefficient[ligne][colonne] = value;
	}

	public String toString() {
		int ligne = this.nbLigne();
		int colonne = this.nbColonne();
		String matr = "";
		for (int i = 0; i < ligne; i++) {
			for (int j = 0; j < colonne; j++) {
				if (j == 0) {
					matr += this.getCoef(i, j);
				} else {
					matr += " " + this.getCoef(i, j);
				}
			}
			matr += "\n";
		}
		return matr;
	}

	public Matrice produit(double scalaire) {
		int ligne = this.nbLigne();
		int colonne = this.nbColonne();
		for (int i = 0; i < ligne; i++)
			for (int j = 0; j < colonne; j++)
				this.coefficient[i][j] *= scalaire;
		return this;
	}
	
	/** Calcule la norme 1 (max de la somme absolue des colonnes) **/
    public double norme_1() {
        double max = 0;
        for (int j = 0; j < this.nbColonne(); j++) {
            double sommeCol = 0;
            for (int i = 0; i < this.nbLigne(); i++) {
                sommeCol += Math.abs(this.getCoef(i, j));
            }
            if (sommeCol > max) max = sommeCol;
        }
        return max;
    }

    /** Calcule la norme infini (max de la somme absolue des lignes) **/
    public double norme_inf() {
        double max = 0;
        for (int i = 0; i < this.nbLigne(); i++) {
            double sommeLigne = 0;
            for (int j = 0; j < this.nbColonne(); j++) {
                sommeLigne += Math.abs(this.getCoef(i, j));
            }
            if (sommeLigne > max) max = sommeLigne;
        }
        return max;
    }

    public double cond_1() {
        return this.norme_1() * this.inverse().norme_1();
    }

    public double cond_inf() {
        return this.norme_inf() * this.inverse().norme_inf();
    }
    
    public Matrice inverse() {
        int n = this.nbLigne();
        if (n != this.nbColonne()) {
            throw new IllegalOperationException("Erreur : La matrice n'est pas carrée.");
        }

        Matrice inv = new Matrice(n, n);

        try {
            for (int j = 0; j < n; j++) {
                // Création du j-ème vecteur de la base canonique e_j
                Vecteur ej = new Vecteur(n);
                ej.remplacecoef(j, 1.0);

                // On doit passer une copie de la matrice car Helder la modifie (factorisation sur place)
                Matrice copieA = new Matrice(n, n);
                copieA.recopie(this);

                // Résolution du système A * x = ej
                Helder solveur = new Helder(copieA, ej);
                Vecteur xj = solveur.resolution();

                // Le vecteur solution xj devient la j-ème colonne de la matrice inverse
                for (int i = 0; i < n; i++) {
                    inv.remplacecoef(i, j, xj.getCoef(i));
                }
            }
        } catch (IrregularSysLinException e) {
            throw new IllegalOperationException("IllegalOperationException : Matrice non inversible (singulière).");
        }

        return inv;
    }

	static Matrice addition(Matrice a, Matrice b) {
		int ligne = a.nbLigne();
		int colonne = a.nbColonne();
		Matrice mat = new Matrice(ligne, colonne);
		for (int i = 0; i < ligne; i++)
			for (int j = 0; j < colonne; j++)
				mat.coefficient[i][j] = a.coefficient[i][j] + b.coefficient[i][j];
		return mat;
	}

	static Matrice verif_addition(Matrice a, Matrice b) throws IrregularSysLinException {
		if ((a.nbLigne() == b.nbLigne()) && (a.nbColonne() == b.nbColonne())) {
			int ligne = a.nbLigne();
			int colonne = a.nbColonne();
			Matrice mat = new Matrice(ligne, colonne);
			for (int i = 0; i < ligne; i++)
				for (int j = 0; j < colonne; j++)
					mat.coefficient[i][j] = a.coefficient[i][j] + b.coefficient[i][j];
			return mat;
		} else {
			throw new IrregularSysLinException("Les deux matrices n'ont pas les mêmes dimensions !!!");
		}
	}

	static Matrice produit(Matrice a, Matrice b) {
		int ligne, colonne;
		ligne = a.nbLigne();
		colonne = b.nbColonne();
		Matrice mat = new Matrice(ligne, colonne);
		for (int i = 0; i < ligne; i++)
			for (int j = 0; j < colonne; j++) {
				mat.coefficient[i][j] = 0;
				for (int k = 0; k < a.nbColonne(); k++)
					mat.coefficient[i][j] += a.coefficient[i][k] * b.coefficient[k][j];
			}
		return mat;
	}

	static Matrice verif_produit(Matrice a, Matrice b) throws IrregularSysLinException {
		int ligne = 0;
		int colonne = 0;
		if (a.nbColonne() == b.nbLigne()) {
			ligne = a.nbLigne();
			colonne = b.nbColonne();
		} else {
			throw new IrregularSysLinException("Dimensions des matrices à multiplier incorrectes");
		}

		Matrice mat = new Matrice(ligne, colonne);
		for (int i = 0; i < ligne; i++)
			for (int j = 0; j < colonne; j++) {
				mat.coefficient[i][j] = 0;
				for (int k = 0; k < a.nbColonne(); k++)
					mat.coefficient[i][j] += a.coefficient[i][k] * b.coefficient[k][j];
			}
		return mat;
	}
	
	public static Matrice identite(int n) {
	    Matrice id = new Matrice(n, n);
	    for (int i = 0; i < n; i++) {
	        id.remplacecoef(i, i, 1.0);
	    }
	    return id;
	}

	public static void main(String[] args) throws IrregularSysLinException {
		double mat[][] = { { 2, 1 }, { 0, 1 } };
		Matrice a = new Matrice(mat);
		System.out.println("construction d'une matrice par affectation d'un tableau :\n" + a);
		//Matrice b = new Matrice("matrice1.txt");
		//System.out.println("Construction d'une matrice par lecture d'un fichier :\n" + b);
		Matrice c = new Matrice(2, 2);
		c.recopie(a);
		System.out.println("Recopie de la matrice b :\n" + c);
		System.out.println("Nombre de lignes et colonnes de la matrice c : " + c.nbLigne() +
				", " + c.nbColonne());
		System.out.println("Coefficient (2,2) de la matrice c : " + c.getCoef(1, 1));
		System.out.println("Nouvelle valeur de ce coefficient : 8");
		c.remplacecoef(1, 1, 8);
		System.out.println("Vérification de la modification du coefficient");
		System.out.println("Coefficient (2,2) de la matrice c : " + c.getCoef(1, 1));
		System.out.println("Addition de 2 matrices : affichage des 2 matrices " +
				"puis de leur addition");
		System.out.println("matrice 1 :\n" + a + "matrice 2 :\n" + c + "somme :\n" +
				Matrice.addition(a, c));
		System.out.println("Produit de 2 matrices : affichage des 2 matrices " +
				"puis de leur produit");
		System.out.println("matrice 1 :\n" + a + "matrice 2 :\n" + c + "produit :\n" +
				produit(a, c));
		
	    System.out.println("Matrice d'origine A :\n" + a);
	    
	    // --- Nouveaux Tests : Inversion et Conditionnement ---
	    System.out.println("------------------------------------------");
	    System.out.println("TEST DE L'INVERSION ET DU CONDITIONNEMENT");
	    System.out.println("------------------------------------------");

	    try {
	        // 1. Calcul de l'inverse
	        // Note : Ma méthode inverse() gère déjà la copie pour ne pas écraser 'a'
	        Matrice invA = a.inverse();
	        System.out.println("Matrice inverse A^-1 :\n" + invA);

	        // 2. Calcul du conditionnement
	        System.out.println("Conditionnement (Norme 1)   : " + a.cond_1());
	        System.out.println("Conditionnement (Norme Inf) : " + a.cond_inf());

	        // 3. Vérification : Produit A * A^-1
	        // On utilise une copie de 'a' car Helder pourrait être sensible, 
	        // mais ici produit() est statique et sûr.
	        Matrice produit = Matrice.produit(a, invA);
	        System.out.println("\nProduit A * A^-1 :\n" + produit);

	        // 4. Calcul de l'erreur (Comparaison avec l'Identité)
	        Matrice id = Matrice.identite(a.nbLigne());
	        
	        // Calcul de la différence (produit - id)
	        // On réutilise la méthode addition en multipliant id par -1
	        Matrice moinsId = new Matrice(id.nbLigne(), id.nbColonne());
	        moinsId.recopie(id);
	        moinsId.produit(-1.0);
	        
	        Matrice difference = Matrice.addition(produit, moinsId);
	        
	        double erreur1 = difference.norme_1();
	        double erreurInf = difference.norme_inf();

	        System.out.println("Vérification de la précision :");
	        System.out.println("Norme 1 de la différence (A*A^-1 - I)   : " + erreur1);
	        System.out.println("Norme Inf de la différence (A*A^-1 - I) : " + erreurInf);

	        if (erreur1 < EPSILON) {
	            System.out.println("\nRESULTAT : L'inverse est correcte (erreur < EPSILON).");
	        } else {
	            System.out.println("\nRESULTAT : Précision insuffisante.");
	        }

	    } catch (Exception e) {
	        System.out.println("Erreur lors des tests : " + e.getMessage());
	    }
	}
}
