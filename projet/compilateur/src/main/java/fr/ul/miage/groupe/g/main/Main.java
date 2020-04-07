package fr.ul.miage.groupe.g.main;

import fr.ul.miage.arbre.Noeud;
import fr.ul.miage.groupe.g.generateur.Generateur;
import fr.ul.miage.tds.Tds;

public class Main {
	
	public static void main(String[] args) {
		Etape etape = new Etape();
		Generateur generateur;
		
		// ETAPE 1
		Noeud arbre = etape.genererArbreEtape1();
		Tds tds = etape.genererTdsEtape1();
		generateur = new Generateur(tds);
		//System.out.println(generateur.genererProgramme(arbre));
		
		
		// ETAPE 2
		arbre = etape.genererArbreEtape2();
		tds = etape.genererTdsEtape2();
		generateur = new Generateur(tds);
		//System.out.println(generateur.genererProgramme(arbre));
		
		
		// ETAPE 3
		arbre = etape.genererArbreEtape3();
		tds = etape.genererTdsEtape3();
		generateur = new Generateur(tds);
		//System.out.println(generateur.genererProgramme(arbre));
		
		// ETAPE 4
		arbre = etape.genererArbreEtape4();
		tds = etape.genererTdsEtape4();
		generateur = new Generateur(tds);
		//System.out.println(generateur.genererProgramme(arbre));
		
		// ETAPE 5
		arbre = etape.genererArbreEtape5();
		tds = etape.genererTdsEtape5();
		generateur = new Generateur(tds);
		//System.out.println(generateur.genererProgramme(arbre));
		
		// ETAPE 6
		arbre = etape.genererArbreEtape6();
		tds = etape.genererTdsEtape6();
		generateur = new Generateur(tds);
		//System.out.println(generateur.genererProgramme(arbre));
		
		// ETAPE 7
		arbre = etape.genererArbreEtape7();
		tds = etape.genererTdsEtape7();
		generateur = new Generateur(tds);
		System.out.println(generateur.genererProgramme(arbre));
	}
}
