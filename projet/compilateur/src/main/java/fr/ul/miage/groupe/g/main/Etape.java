package fr.ul.miage.groupe.g.main;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	public Noeud genererArbreEtape4() {
		Prog programme = new Prog();
		
		Fonction main = new Fonction("main");
		
		// i = lire()
		Affectation affectationI = new Affectation();
		Idf idfI = new Idf("i", main);
		affectationI.setFilsGauche(new Idf("i"));
		affectationI.setFilsDroit(new Lire());
		
		main.ajouterUnFils(affectationI);
		
		// ecrire(i + j)
		Ecrire ecrire = new Ecrire();
		Plus plus = new Plus();
		plus.ajouterDesFils(new ArrayList<>(Arrays.asList(idfI, new Idf("j"))));
		ecrire.ajouterUnFils(plus);
		
		main.ajouterUnFils(ecrire);
		
		programme.ajouterUnFils(main);
		return programme;
	}
	
	public Tds genererTdsEtape4() {
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
		System.out.println("3. " + s);*/
		
		return tds;
	}
	
	public Noeud genererArbreEtape5() {
		Prog programme = new Prog();
		
		Fonction main = new Fonction("main");
		
		// i = lire()
		Affectation affectationI = new Affectation();
		Idf idfI = new Idf("i", main);
		affectationI.setFilsGauche(idfI);
		affectationI.setFilsDroit(new Lire());
		
		main.ajouterUnFils(affectationI);
		
		// si(i < 10)
		Si si = new Si(1);
		Inferieur condition = new Inferieur();
		condition.setFilsGauche(idfI);
		condition.setFilsDroit(new Const(10));
		si.setCondition(condition);
		Bloc blocAlors = new Bloc();
		Ecrire ecrire1 = new Ecrire();
		ecrire1.ajouterUnFils(new Const(1));
		blocAlors.ajouterUnFils(ecrire1);
		si.setBlocAlors(blocAlors);
		
		//sinon
		Bloc blocSinon = new Bloc();
		Ecrire ecrire2 = new Ecrire();
		ecrire2.ajouterUnFils(new Const(2));
		blocSinon.ajouterUnFils(ecrire2);
		si.setBlocSinon(blocSinon);
		
		main.ajouterUnFils(si);
		
		programme.ajouterUnFils(main);
		return programme;
	}
	
	public Tds genererTdsEtape5() {
		Tds tds = new Tds();
		
		// entier i
		try {
			Symbole s = tds.ajouter("i", Symbole.CAT_GLOBAL, Symbole.SCOPE_GLOBAL);
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
		System.out.println("2. " + s);*/
		
		return tds;
	}
	
	public Noeud genererArbreEtape6() {
		Prog programme = new Prog();
		
		Fonction main = new Fonction("main");
		
		// i = 0
		Affectation affectationI = new Affectation();
		Idf idfI = new Idf("i", main);
		affectationI.setFilsGauche(idfI);
		affectationI.setFilsDroit(new Const(0));
		
		main.ajouterUnFils(affectationI);
		
		// tantque(i < n)
		TantQue tantque = new TantQue(1);
		Inferieur condition = new Inferieur();
		condition.setFilsGauche(idfI);
		condition.setFilsDroit(new Idf("n"));
		tantque.setCondition(condition);
		
		// ecrire(i)
		Bloc blocTq = new Bloc();
		Ecrire ecrire = new Ecrire();
		ecrire.ajouterUnFils(idfI);
		blocTq.ajouterUnFils(ecrire);
		
		// i = i + 1
		Affectation affectationIPlus = new Affectation();
		Plus plus = new Plus();
		plus.setFilsGauche(idfI);
		plus.setFilsDroit(new Const(1));
		affectationIPlus.setFilsGauche(idfI);
		affectationIPlus.setFilsDroit(plus);
		blocTq.ajouterUnFils(affectationIPlus);
		
		tantque.setBlocAlors(blocTq);
		
		main.ajouterUnFils(tantque);
		
		programme.ajouterUnFils(main);
		return programme;
	}
	
	public Tds genererTdsEtape6() {
		Tds tds = new Tds();
		
		// entier i
		try {
			Symbole s = tds.ajouter("i", Symbole.CAT_GLOBAL, Symbole.SCOPE_GLOBAL);
			s.setType(Symbole.TYPE_ENTIER);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}
		
		// entier n = 5
		try {
			Symbole s = tds.ajouter("n", Symbole.CAT_GLOBAL, Symbole.SCOPE_GLOBAL);
			s.setType(Symbole.TYPE_ENTIER);
			s.setValeur(5);
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
		s = tds.rechercher("n", Symbole.SCOPE_GLOBAL);
		System.out.println("3. " + s);*/
		
		return tds;
	}

	public Noeud genererArbreEtape7() {
		Prog programme = new Prog();
		
		// fonction f
		Fonction f = new Fonction("f");
		// x = 1
		Affectation affectationX = new Affectation();
		Idf idfX = new Idf("x", f);
		affectationX.setFilsGauche(idfX);
		affectationX.setFilsDroit(new Const(1));
		// y = 1
		Affectation affectationY = new Affectation();
		Idf idfY = new Idf("y", f);
		affectationY.setFilsGauche(idfY);
		affectationY.setFilsDroit(new Const(1));
		// a = i + x + y
		Affectation affectationA = new Affectation();
		Idf idfA = new Idf("a");
		affectationA.setFilsGauche(idfA);
		Plus plus1 = new Plus();
		plus1.setFilsGauche(new Idf("i", f));
		Plus plus2 = new Plus();
		plus2.setFilsGauche(idfX);
		plus2.setFilsDroit(idfY);
		plus1.setFilsDroit(plus2);
		affectationA.setFilsDroit(plus1);
		f.ajouterDesFils(Arrays.asList(affectationX, affectationY, affectationA));
		
		// main
		Fonction main = new Fonction("main");
		// appel -> f(3)
		Appel appel = new Appel(f);
		appel.ajouterUnFils(new Const(3));
		// ecrire(a)
		Ecrire ecrire = new Ecrire();
		ecrire.ajouterUnFils(idfA);
		main.ajouterDesFils(Arrays.asList(appel, ecrire));
		
		programme.ajouterDesFils(Arrays.asList(f, main));
		
		return programme;
	}
	
	public Tds genererTdsEtape7() {
		Tds tds = new Tds();
		
		// entier a = 10
		try {
			Symbole s = tds.ajouter("a", Symbole.CAT_GLOBAL, Symbole.SCOPE_GLOBAL);
			s.setType(Symbole.TYPE_ENTIER);
			s.setValeur(10);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}
		
		// fonction f
		try {
			Symbole s = tds.ajouter("f", Symbole.CAT_FONCTION, Symbole.SCOPE_GLOBAL);
			s.setType(Symbole.TYPE_VOID);
			s.setNbParam(1);
			s.setNbLoc(2);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}
		
		// parametre i dans fonction f
		try {
			Symbole s = tds.ajouter("i", Symbole.CAT_PARAMETRE, "f");
			s.setType(Symbole.TYPE_ENTIER);
			s.setRang(0);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}
		
		// variable locale x dans fonction f
		try {
			Symbole s = tds.ajouter("x", Symbole.CAT_LOCAL, "f");
			s.setType(Symbole.TYPE_ENTIER);
			s.setRang(0);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}	
		
		// variable locale y dans fonction f
		try {
			Symbole s = tds.ajouter("y", Symbole.CAT_LOCAL, "f");
			s.setType(Symbole.TYPE_ENTIER);
			s.setRang(1);
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
		s = tds.rechercher("f", Symbole.SCOPE_GLOBAL);
		System.out.println("2. " + s);
		s = tds.rechercher("a", Symbole.SCOPE_GLOBAL);
		System.out.println("3. " + s);
		s = tds.rechercher("x", "f");
		System.out.println("4. " + s);
		s = tds.rechercher("y", "f");
		System.out.println("5. " + s);
		s = tds.rechercher("i", "f");
		System.out.println("6. " + s);*/
		
		return tds;
	}

	public Noeud genererArbreEtape8() {
		return null;
	}
	
	public Tds genererTdsEtape8() {
		Tds tds = new Tds();
		
		// entier a = 10
		try {
			Symbole s = tds.ajouter("a", Symbole.CAT_GLOBAL, Symbole.SCOPE_GLOBAL);
			s.setType(Symbole.TYPE_ENTIER);
			s.setValeur(10);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}
		
		// fonction f
		try {
			Symbole s = tds.ajouter("f", Symbole.CAT_FONCTION, Symbole.SCOPE_GLOBAL);
			s.setType(Symbole.TYPE_VOID);
			s.setNbParam(1);
			s.setNbLoc(2);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}
		
		// parametre i dans fonction f
		try {
			Symbole s = tds.ajouter("i", Symbole.CAT_PARAMETRE, "f");
			s.setType(Symbole.TYPE_ENTIER);
			s.setRang(0);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}
		
		// variable locale x dans fonction f
		try {
			Symbole s = tds.ajouter("x", Symbole.CAT_LOCAL, "f");
			s.setType(Symbole.TYPE_ENTIER);
			s.setRang(0);
		} catch(Exception e) {
			LOG.warning(e.getMessage());
		}	
		
		// variable locale y dans fonction f
		try {
			Symbole s = tds.ajouter("y", Symbole.CAT_LOCAL, "f");
			s.setType(Symbole.TYPE_ENTIER);
			s.setRang(1);
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
		s = tds.rechercher("f", Symbole.SCOPE_GLOBAL);
		System.out.println("2. " + s);
		s = tds.rechercher("a", Symbole.SCOPE_GLOBAL);
		System.out.println("3. " + s);
		s = tds.rechercher("x", "f");
		System.out.println("4. " + s);
		s = tds.rechercher("y", "f");
		System.out.println("5. " + s);
		s = tds.rechercher("i", "f");
		System.out.println("6. " + s);*/
		
		return tds;
	}
}
