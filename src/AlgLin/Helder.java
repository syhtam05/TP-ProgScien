package AlgLin;

public class Helder extends SysLin {

    public Helder(Matrice M, Vecteur b) throws Exception {
        super(M, b);
    }

    public void factorLDR() throws IrregularSysLinException {
        int n = this.ordre;
        for (int i = 0; i < n; i++) {
            // Calcul de la ligne i de R et de la colonne i de L
            for (int j = 0; j < i; j++) {
                // Calcul des éléments de L (sous la diagonale)
                double sumL = 0;
                for (int k = 0; k < j; k++) {
                    sumL += matriceSystem.getCoef(i, k) * matriceSystem.getCoef(k, k) * matriceSystem.getCoef(k, j);
                }
                double lij = (matriceSystem.getCoef(i, j) - sumL) / matriceSystem.getCoef(j, j);
                matriceSystem.remplacecoef(i, j, lij);

                // Calcul des éléments de R (au dessus de la diagonale)
                double sumR = 0;
                for (int k = 0; k < j; k++) {
                    sumR += matriceSystem.getCoef(j, k) * matriceSystem.getCoef(k, k) * matriceSystem.getCoef(k, i);
                }
                double rji = (matriceSystem.getCoef(j, i) - sumR) / matriceSystem.getCoef(j, j);
                matriceSystem.remplacecoef(j, i, rji);
            }

            // Calcul de l'élément de D (diagonale)
            double sumD = 0;
            for (int k = 0; k < i; k++) {
                sumD += matriceSystem.getCoef(i, k) * matriceSystem.getCoef(k, k) * matriceSystem.getCoef(k, i);
            }
            double dii = matriceSystem.getCoef(i, i) - sumD;
            if (Math.abs(dii) < Matrice.EPSILON) {
                throw new IrregularSysLinException("Pivot nul rencontré durant la factorisation LDR à l'indice " + i);
            }
            matriceSystem.remplacecoef(i, i, dii);
        }
    }

    public Vecteur resolutionPartielle() throws Exception {
        // 1. Résoudre Lz = b (L est dans la partie inf de A, diag=1)
        SysTriangInfUnite sysL = new SysTriangInfUnite(matriceSystem, secondMembre);
        Vecteur z = sysL.resolution();

        // 2. Résoudre Dy = z (D est sur la diagonale de A)
        SysDiagonal sysD = new SysDiagonal(matriceSystem, z);
        Vecteur y = sysD.resolution();

        // 3. Résoudre Rx = y (R est dans la partie sup de A, diag=1)
        SysTriangSupUnite sysR = new SysTriangSupUnite(matriceSystem, y);
        Vecteur x = sysR.resolution();

        return x;
    }

    @Override
    public Vecteur resolution() throws IrregularSysLinException {
        try {
            factorLDR();
            return resolutionPartielle();
        } catch (IrregularSysLinException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        // 1. Définition des données de l'énoncé
        double[][] dataA = {
                { 1, 1, -2 },
                { 4, -2, 1 },
                { 3, -1, 3 }
        };
        double[] dataB = { 3, 5, 8 };

        // 2. On garde des copies pour la vérification finale (Ax = b)
        Matrice A_originale = new Matrice(dataA);
        Vecteur b_original = new Vecteur(new double[] { 3, 5, 8 });

        // 3. Création du système Helder
        Matrice A_pour_calcul = new Matrice(new double[][] {
                { 1, 1, -2 },
                { 4, -2, 1 },
                { 3, -1, 3 }
        });
        Helder helder = new Helder(A_pour_calcul, new Vecteur(dataB));

        try {
            System.out.println("--- Test avec la matrice utilisateur ---");
            System.out.println("Matrice A :\n" + A_originale);
            System.out.println("Second membre b :\n" + b_original);

            // 4. Résolution (Factorisation + Résolution Partielle)
            Vecteur x = helder.resolution();

            System.out.println("Solution trouvée x :");
            System.out.println(x);

            // 5. Vérification avec la norme de l'erreur
            // Utilise la méthode testSolution ajoutée précédemment dans Vecteur
            Vecteur.testSolution(A_originale, x, b_original);

        } catch (IrregularSysLinException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}