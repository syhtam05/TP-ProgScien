package AlgLin;

public class HilbertMatrice extends Matrice {

    /**
     * Constructeur : remplit la matrice selon la formule 1 / (i + j + 1)
     * @param n Ordre de la matrice
     */
    public HilbertMatrice(int n) {
        super(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Utilisation de 1.0 pour forcer la division en double
                double val = 1.0 / (i + j + 1);
                this.remplacecoef(i, j, val);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("TEST DES MATRICES DE HILBERT (Ordre 3 à 15)");
        System.out.println("===========================================");

        for (int n = 3; n <= 15; n++) {
            System.out.println("\n--- Ordre n = " + n + " ---");

            try {
                // 1. Création de la matrice de Hilbert
                HilbertMatrice H = new HilbertMatrice(n);

                // 2. Calcul de l'inverse (via Helder dans Matrice.inverse())
                Matrice H_inv = H.inverse();

                // 3. Calcul du conditionnement
                double c1 = H.cond_1();
                double cinf = H.cond_inf();

                // 4. Vérification : Erreur = ||H * H_inv - I||
                Matrice produit = Matrice.produit(H, H_inv);
                Matrice id = Matrice.identite(n);
                
                // Calcul de la différence
                Matrice diff = new Matrice(n, n);
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        diff.remplacecoef(i, j, produit.getCoef(i, j) - id.getCoef(i, j));
                    }
                }

                double erreur = diff.norme_1();

                // 5. Affichage des résultats
                System.out.printf("Conditionnement (norme 1)   : %.2E%n", c1);
                System.out.printf("Conditionnement (norme inf) : %.2E%n", cinf);
                System.out.printf("Erreur de précision (||H*H^-1 - I||_1) : %.2E%n", erreur);
                
                if (n <= 4) {
                    System.out.println("Inverse de H" + n + " :");
                    System.out.print(H_inv);
                }

            } catch (Exception e) {
                System.out.println("Erreur pour n=" + n + " : " + e.getMessage());
            }
        }
    }
}