package AlgLin;

import java.io.*;
import java.util.*;

public class Vecteur extends Matrice {

    public Vecteur(int nbLigne) {
        super(nbLigne, 1);
    }

    public Vecteur(double[] tableau) {
        super(tableau.length, 1);
        for (int i = 0; i < tableau.length; i++) {
            this.coefficient[i][0] = tableau[i];
        }
    }

    public Vecteur(String fichier) {
    	super(0, 0);
    	
        try {
            Scanner sc = new Scanner(new File(fichier));
            int taille = sc.nextInt();
            this.coefficient = new double[taille][1];
            for (int i = 0; i < taille; i++) {
                this.coefficient[i][0] = sc.nextDouble();
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Fichier absent : " + fichier);
        }
    }

    public Vecteur(Matrice m) {
        super(m.nbLigne(), 1);
        for (int i = 0; i < m.nbLigne(); i++) {
            this.coefficient[i][0] = m.getCoef(i, 0);
        }
    }

    public int taille() {
        return this.nbLigne();
    }

    public double getCoef(int i) {
        return super.getCoef(i, 0);
    }

    public void remplacecoef(int i, double value) {
        super.remplacecoef(i, 0, value);
    }

    public static double produitScalaire(Vecteur v1, Vecteur v2) throws Exception {
        if (v1.taille() != v2.taille()) {
            throw new Exception("Les vecteurs doivent avoir la même taille pour le produit scalaire");
        }
        
        double resultat = 0;
        
        for (int i = 0; i < v1.taille(); i++) {
            resultat += v1.getCoef(i) * v2.getCoef(i);
        }
        
        return resultat;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("--- Test de la classe Vecteur ---");

        // Test 1: Constructeur tableau 1D
        double[] tab = {1.0, 2.0, 3.0};
        Vecteur v1 = new Vecteur(tab);
        System.out.println("Vecteur v1 (depuis tableau) :\n" + v1);

        // Test 2: Taille et modification
        System.out.println("Taille de v1 : " + v1.taille());
        v1.remplacecoef(1, 10.0);
        System.out.println("v1 après modification du 2ème élément :\n" + v1);

        // Test 3: Constructeur depuis Matrice
        double[][] matData = {{5}, {6}, {7}};
        Matrice m = new Matrice(matData);
        Vecteur v2 = new Vecteur(m);
        System.out.println("Vecteur v2 (depuis Matrice) :\n" + v2);

        // Test 4: Produit scalaire
        double scalaire = Vecteur.produitScalaire(v1, v2);
        System.out.println("Produit scalaire v1 . v2 = " + scalaire);
        
        // Test 5: Lecture fichier
        Vecteur v3 = new Vecteur("vecteur1.pdf");
        System.out.println("Vecteur v3 (depuis fichier) :\n" + v3);
    }
}