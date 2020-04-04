package fr.ul.miage.groupe.g.main;

import fr.ul.miage.arbre.Noeud;
import fr.ul.miage.groupe.g.generateur.Generateur;
import fr.ul.miage.tds.Tds;

public class Main {
	
	public static void main(String[] args) {
		Etape etape = new Etape();
		Generateur generateur = new Generateur();
		
		// ETAPE 1
		Noeud arbre = etape.genererArbreEtape1();
		Tds tds = etape.genererTdsEtape1();
		//System.out.println(generateur.genererProgramme(arbre, tds));
		
		
		// ETAPE 2
		arbre = etape.genererArbreEtape2();
		tds = etape.genererTdsEtape2();
		//System.out.println(generateur.genererProgramme(arbre, tds));
		
		
		// ETAPE 3
		arbre = etape.genererArbreEtape3();
		tds = etape.genererTdsEtape3();
		System.out.println(generateur.genererProgramme(arbre, tds));
	}
}
