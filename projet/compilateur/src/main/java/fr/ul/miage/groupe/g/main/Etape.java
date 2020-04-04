package fr.ul.miage.groupe.g.main;

import java.util.logging.Logger;

import fr.ul.miage.arbre.*;
import fr.ul.miage.tds.Main;
import fr.ul.miage.tds.Symbole;
import fr.ul.miage.tds.Tds;

public class Etape {
	
	private static final Logger LOG = Logger.getLogger(Etape.class.getName());

	/**
	 * Génération de l'arbre abstrait de l'exemple 1.
	 * @return
	 * 		L'arbre complet (le noeud programme).
	 */
	public Noeud genererArbreEtape1() {
		Prog programme = new Prog();
		Fonction main = new Fonction("main");
		programme.ajouterUnFils(main);
		return programme;
	}
	
	/**
	 * Génération de la TDS de l'exemple 1.
	 * @return
	 * 		La tds complète.
	 */
	public Tds genererTdsEtape1() {
		Tds tds = new Tds();
		
		// fonction "main"
		try {
			Symbole s = tds.ajouter("main", Symbole.CAT_FONCTION, Symbole.SCOPE_GLOBAL);
			s.setType(Symbole.TYPE_VOID);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}
		
		/*
		 * OPTIONNEL (Pour voir si ça fonctionne)
		 */
		/*System.out.println(tds.toString());
		Symbole s = null;
		s = tds.rechercher("main", Symbole.SCOPE_GLOBAL);
		System.out.println("1. " + s);*/
		
		return tds;
	}
	
	public Noeud genererArbreEtape2() {
		Prog programme = new Prog();
		Fonction main = new Fonction("main");
		programme.ajouterUnFils(main);
		return programme;
	}
	
	public Tds genererTdsEtape2() {
		Tds tds = new Tds();
		
		// entier i = 10
		try {
			Symbole s = tds.ajouter("i", Symbole.CAT_GLOBAL, Symbole.SCOPE_GLOBAL);
			s.setType(Symbole.TYPE_ENTIER);
			s.setValeur(10);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}
		
		// entier j = 20
		try {
			Symbole s = tds.ajouter("j", Symbole.CAT_GLOBAL, Symbole.SCOPE_GLOBAL);
			s.setType(Symbole.TYPE_ENTIER);
			s.setValeur(20);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}
		
		// entier k
		try {
			Symbole s = tds.ajouter("k", Symbole.CAT_GLOBAL, Symbole.SCOPE_GLOBAL);
			s.setType(Symbole.TYPE_ENTIER);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}
		
		// entier l
		try {
			Symbole s = tds.ajouter("l", Symbole.CAT_GLOBAL, Symbole.SCOPE_GLOBAL);
			s.setType(Symbole.TYPE_ENTIER);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}
		
		// fonction "main"
		try {
			Symbole s = tds.ajouter("main", Symbole.CAT_FONCTION, Symbole.SCOPE_GLOBAL);
			s.setType(Symbole.TYPE_VOID);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}
		
		/*
		 * OPTIONNEL (Pour voir si ça fonctionne)
		 */
		/*System.out.println(tds.toString());
		Symbole s = null;
		s = tds.rechercher("main", Symbole.SCOPE_GLOBAL);
		System.out.println("1. " + s);
		s = tds.rechercher("i", Symbole.SCOPE_GLOBAL);
		System.out.println("2. " + s);
		s = tds.rechercher("j", Symbole.SCOPE_GLOBAL);
		System.out.println("3. " + s);
		s = tds.rechercher("k", Symbole.SCOPE_GLOBAL);
		System.out.println("4. " + s);
		s = tds.rechercher("l", Symbole.SCOPE_GLOBAL);
		System.out.println("5. " + s);*/
		
		return tds;
	}
	
	public Noeud genererArbreEtape3() {
		Prog programme = new Prog();
		
		Fonction main = new Fonction("main");
		
		// k = 2
		Affectation affectationK = new Affectation();
		affectationK.setFilsGauche(new Idf("k"));
		affectationK.setFilsDroit(new Const(2));
		
		main.ajouterUnFils(affectationK);
		
		// l = i + j * 3
		Affectation affectationL = new Affectation();
		affectationL.setFilsGauche(new Idf("l"));
		Plus plus = new Plus();
		plus.setFilsGauche(new Idf("i"));
		Multiplication multiplication = new Multiplication();
		multiplication.setFilsGauche(new Const(3));
		multiplication.setFilsDroit(new Idf("j"));
		plus.setFilsDroit(multiplication);
		affectationL.setFilsDroit(plus);
		
		main.ajouterUnFils(affectationL);
		
		programme.ajouterUnFils(main);
		return programme;
	}
	
	public Tds genererTdsEtape3() {
		Tds tds = new Tds();
		
		// entier i = 10
		try {
			Symbole s = tds.ajouter("i", Symbole.CAT_GLOBAL, Symbole.SCOPE_GLOBAL);
			s.setType(Symbole.TYPE_ENTIER);
			s.setValeur(10);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}
		
		// entier j = 20
		try {
			Symbole s = tds.ajouter("j", Symbole.CAT_GLOBAL, Symbole.SCOPE_GLOBAL);
			s.setType(Symbole.TYPE_ENTIER);
			s.setValeur(20);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}
		
		// entier k
		try {
			Symbole s = tds.ajouter("k", Symbole.CAT_GLOBAL, Symbole.SCOPE_GLOBAL);
			s.setType(Symbole.TYPE_ENTIER);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}
		
		// entier l
		try {
			Symbole s = tds.ajouter("l", Symbole.CAT_GLOBAL, Symbole.SCOPE_GLOBAL);
			s.setType(Symbole.TYPE_ENTIER);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}
		
		// fonction "main"
		try {
			Symbole s = tds.ajouter("main", Symbole.CAT_FONCTION, Symbole.SCOPE_GLOBAL);
			s.setType(Symbole.TYPE_VOID);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}
		
		/*
		 * OPTIONNEL (Pour voir si ça fonctionne)
		 */
		/*System.out.println(tds.toString());
		Symbole s = null;
		s = tds.rechercher("main", Symbole.SCOPE_GLOBAL);
		System.out.println("1. " + s);
		s = tds.rechercher("i", Symbole.SCOPE_GLOBAL);
		System.out.println("2. " + s);
		s = tds.rechercher("j", Symbole.SCOPE_GLOBAL);
		System.out.println("3. " + s);
		s = tds.rechercher("k", Symbole.SCOPE_GLOBAL);
		System.out.println("4. " + s);
		s = tds.rechercher("l", Symbole.SCOPE_GLOBAL);
		System.out.println("5. " + s);*/
		
		return tds;
	}
}