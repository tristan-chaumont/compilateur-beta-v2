package fr.ul.miage.groupe.g.generateur;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.ul.miage.arbre.*;
import fr.ul.miage.arbre.Noeud.Categories;
import fr.ul.miage.groupe.g.main.StringBuilderPlus;
import fr.ul.miage.tds.Tds;
import fr.ul.miage.tds.Symbole;
import static fr.ul.miage.tds.Symbole.*;

public class Generateur {
	
	private Tds tds;
	
	public Generateur(Tds tds) {
		this.tds = tds;
	}

	/**
	 * Génère la base du programme avec les include, le call à la fonction main, ainsi que la pile.
	 * @param noeud
	 * 		Noeud programme.
	 * @return
	 * 		Le code asm complet du programme.
	 */
	public String genererProgramme(Noeud noeud) {
		// true spécifie qu'on saute une ligne après l'insertion du String
		StringBuilderPlus builder = new StringBuilderPlus();
		builder.appendLine(".include beta.uasm");
		builder.appendLine(".include intio.uasm");
		builder.appendLine(".options tty");
		builder.appendLine(""); // SAUT DE LIGNE
		builder.appendLine("CMOVE(pile, SP)");
		builder.appendLine("BR(debut)");
		builder.append(genererData());
		builder.appendLine(genererCode(noeud));
		builder.appendLine("debut:");
		builder.appendLineTab("CALL(main)");
		builder.appendLineTab("HALT()");
		builder.appendLine(""); // SAUT DE LIGNE
		builder.appendLine("pile:");
		return builder.toString();
	}
	
	/**
	 * Génère les data du programme, i.e. les variables globales.
	 * @return
	 * 		Le code asm de déclaration des variables globales.
	 */
	private String genererData() {
		StringBuilderPlus builder = new StringBuilderPlus();
		
		// vérifie s'il y a au moins une variable globale dans la tds
		int nbGlobales = tds.getTds().entrySet().stream().filter(key -> {
			if (key.getValue().stream().filter(symbole -> symbole.getCat().equals(CAT_GLOBAL)).collect(Collectors.toList()).size() > 0)
				return true;
			return false;
		}).collect(Collectors.toList()).size();
		// COMMENTAIRE
		if(nbGlobales > 0) {
			builder.appendLine("").appendLine("| Déclaration des variables globales");
		}
		
		for (Map.Entry<String, List<Symbole>> variable: tds.getTds().entrySet()) {
			int init = 0;
			List<Symbole> listeVariables = variable.getValue();
			
			for (Symbole symbole: listeVariables) {
				if (symbole.getType().equals(TYPE_ENTIER) && symbole.getScope().equals(SCOPE_GLOBAL) && !symbole.getCat().equals(CAT_FONCTION)) {
					if (symbole.getProp().containsKey(PROP_VALEUR)) {
						init = symbole.getValeur();
					}
					builder.appendLine(String.format("%s: LONG(%d)", variable.getKey(), init));
				}
			}
		}
		return builder.toString();
	}
	
	/**
	 * Génère toutes les fonctions du code.
	 * @param noeud
	 * 		Noeud "Prog" dont on veut générer toutes les fonctions filles.
	 * @return
	 * 		Le code asm de chaque fonction.
	 */
	private String genererCode(Noeud noeud) {
		StringBuilderPlus builder = new StringBuilderPlus();
		for (Noeud n: noeud.getFils()) {
			if (n.getCat().equals(Categories.FONCTION)){
				builder.append(genererFonction((Fonction) n));
			}
		}
		return builder.toString();
	}
	
	/**
	 * Génère toutes les instructions d'une fonction.
	 * @param fonction
	 * 		Noeud "Fonction" dont il faut générer le code asm.
	 * @return
	 * 		Le code asm de la fonction.
	 */
	private String genererFonction(Fonction fonction) {
		StringBuilderPlus builder = new StringBuilderPlus();
		String nomFonction = (String) fonction.getValeur();
		builder.appendLine("");
		
		// commentaire
		builder.appendLine(String.format("| %s", fonction.commentaire())); // COMMENTAIRE
		
		builder.appendLine(String.format("%s:", nomFonction));
		builder.appendLineTab("PUSH(LP)");
		builder.appendLineTab("PUSH(BP)");
		builder.appendLineTab("MOVE(SP, BP)");
		builder.appendLineTab("| Allocation du nombre de variables locales de la fonction."); // COMMENTAIRE
		builder.appendLineTab(String.format("ALLOCATE(%d)", tds.rechercher(nomFonction, SCOPE_GLOBAL).getNbLoc()));
		for (Noeud n: fonction.getFils()) {
			builder.append(genererInstruction(n));
		}
		builder.appendLineTab("MOVE(BP, SP)");
		builder.appendLineTab("POP(BP)");
		builder.appendLineTab("POP(LP)");
		builder.appendLineTab("RTN()");
		return builder.toString();
	}
	
	/**
	 * Génère toutes les instructions d'un bloc.
	 * @param bloc
	 * 		Noeud "Bloc" dont il faut générer les instructions.
	 * @return
	 * 		Le code asm du bloc de code.
	 */
	private String genererBloc(Bloc bloc) {
		StringBuilderPlus builder = new StringBuilderPlus();
		for (Noeud n: bloc.getFils()) {
			builder.append(genererInstruction(n));
		}
		return builder.toString();
	}
	
	/**
	 * Génère une instruction (affectation, ecrire, lire, si, tant que, appel, retour de fonction).
	 * @param noeud
	 * 		Noeud fils de "Fonction" ou "Bloc" dont il faut générer l'instruction.
	 * @return
	 * 		Le code asm de l'instruction cible.
	 */
	private String genererInstruction(Noeud noeud) {
		StringBuilderPlus builder = new StringBuilderPlus();
		switch (noeud.getCat()) {
			case AFF:
				builder.append(genererAffectation((Affectation) noeud));
				break;
			case ECR:
				builder.append(genererEcrire((Ecrire) noeud));
				break;
			case SI:
				builder.append(genererSi((Si) noeud));
				break;
			case TQ:
				builder.append(genererTantQue((TantQue) noeud));
				break;
			case RET:
				builder.append(genererRetourne((Retour) noeud));
				break;
			case APPEL:
				builder.append(genererExpression((Appel) noeud));
			default:
				break;
		}
		return builder.toString();
	}
	
	/**
	 * Génère une expression (addition, soustraction, multiplication, division, constante, identificateur, lecture, appel).
	 * @param noeud
	 * 		Noeud dont il faut générer l'expression.
	 * @return
	 * 		Le code asm de l'expression.
	 */
	private String genererExpression(Noeud noeud) {
		StringBuilderPlus builder = new StringBuilderPlus();
		switch (noeud.getCat()) {
			case CONST:
				//builder.appendLineTab(String.format("| Constante : %s", noeud.commentaire())); // COMMENTAIRE
				builder.appendLineTab(String.format("CMOVE(%d, r0)", ((Const) noeud).getValeur()));
				builder.appendLineTab("PUSH(r0)");
				break;
			case IDF:
				Idf idf = (Idf) noeud;
				Symbole symbole = tds.rechercher((String) idf.getValeur(), (String) (idf.getFonction() != null ? idf.getFonction().getValeur() : SCOPE_GLOBAL));
				switch (symbole.getCat()) {
					case CAT_GLOBAL:
						builder.appendLineTab(String.format("| Idf : %s (variable globale)", idf.commentaire())); // COMMENTAIRE
						builder.appendLineTab(String.format("LD(%s, r0)", idf.getValeur()));
						builder.appendLineTab("PUSH(r0)");
						break;
					case CAT_LOCAL:
						builder.appendLineTab(String.format("| Idf : %s (variable locale n°%d de la fonction '%s')", idf.commentaire(), symbole.getRang(), idf.getFonction().getValeur())); // COMMENTAIRE
						builder.appendLineTab(String.format("GETFRAME(%d, r0)", symbole.getRang() * 4));
						builder.appendLineTab("PUSH(r0)");
						break;
					case CAT_PARAMETRE:
						builder.appendLineTab(String.format("| Idf : %s (paramètre n°%d de la fonction '%s')", idf.commentaire(), symbole.getRang(), idf.getFonction().getValeur())); // COMMENTAIRE
						builder.appendLineTab(String.format("GETFRAME(%d, r0)", (symbole.getRang() + 3) * (-4)));
						builder.appendLineTab("PUSH(r0)");
						break;
				}
				break;
			case PLUS:
				//builder.appendLineTab(String.format("| %s", noeud.commentaire())); // COMMENTAIRE
				builder.append(genererExpression(noeud.getFils().get(0)));
				builder.append(genererExpression(noeud.getFils().get(1)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("ADD(r1, r2, r3)");
				builder.appendLineTab("PUSH(r3)");
				break;
			case MOINS:
				//builder.appendLineTab(String.format("| %s", noeud.commentaire())); // COMMENTAIRE
				builder.append(genererExpression(noeud.getFils().get(0)));
				builder.append(genererExpression(noeud.getFils().get(1)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("SUB(r1, r2, r3)");
				builder.appendLineTab("PUSH(r3)");
				break;
			case MUL:
				//builder.appendLineTab(String.format("| %s", noeud.commentaire())); // COMMENTAIRE
				builder.append(genererExpression(noeud.getFils().get(0)));
				builder.append(genererExpression(noeud.getFils().get(1)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("MUL(r1, r2, r3)");
				builder.appendLineTab("PUSH(r3)");
				break;
			case DIV:
				//builder.appendLineTab(String.format("| %s", noeud.commentaire())); // COMMENTAIRE
				builder.append(genererExpression(noeud.getFils().get(0)));
				builder.append(genererExpression(noeud.getFils().get(1)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("DIV(r1, r2, r3)");
				builder.appendLineTab("PUSH(r3)");
				break;
			case LIRE:
				builder.appendLineTab(String.format("| %s", noeud.commentaire())); // COMMENTAIRE
				builder.appendLineTab("RDINT()");
				builder.appendLineTab("PUSH(r0)");
				break;
			case APPEL:
				builder.append(genererAppel((Appel) noeud));
				break;
			default:
				break;
		}
		return builder.toString();
	}
	
	/**
	 * Génère une expression booléenne (>=, >, <, <=, ==, !=)
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	private String genererExpressionBooleenne(Noeud noeud) {
		StringBuilderPlus builder = new StringBuilderPlus();
		switch (noeud.getCat()) {
			case SUP:
				//builder.appendLineTab(String.format("| %s", noeud.commentaire())); // COMMENTAIRE
				builder.append(genererExpression(noeud.getFils().get(0)));
				builder.append(genererExpression(noeud.getFils().get(1)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("CMPLT(r2, r1, r3)");
				builder.appendLineTab("PUSH(r3)");
				break;
			case SUPE:
				//builder.appendLineTab(String.format("| %s", noeud.commentaire())); // COMMENTAIRE
				builder.append(genererExpression(noeud.getFils().get(0)));
				builder.append(genererExpression(noeud.getFils().get(1)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("CMPLE(r2, r1, r3)");
				builder.appendLineTab("PUSH(r3)");
				break;
			case INF:
				//builder.appendLineTab(String.format("| %s", noeud.commentaire())); // COMMENTAIRE
				builder.append(genererExpression(noeud.getFils().get(0)));
				builder.append(genererExpression(noeud.getFils().get(1)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("CMPLT(r1, r2, r3)");
				builder.appendLineTab("PUSH(r3)");
				break;
			case INFE:
				//builder.appendLineTab(String.format("| %s", noeud.commentaire())); // COMMENTAIRE
				builder.append(genererExpression(noeud.getFils().get(0)));
				builder.append(genererExpression(noeud.getFils().get(0)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("CMPLE(r1, r2, r3)");
				builder.appendLineTab("PUSH(r3)");
				break;
			case EG:
				//builder.appendLineTab(String.format("| %s", noeud.commentaire())); // COMMENTAIRE
				builder.append(genererExpression(noeud.getFils().get(0)));
				builder.append(genererExpression(noeud.getFils().get(0)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("CMPEQ(r1, r2, r3)");
				builder.appendLineTab("PUSH(r3)");
				break;
			case DIF:
				//builder.appendLineTab(String.format("| %s", noeud.commentaire())); // COMMENTAIRE
				builder.append(genererExpression(noeud.getFils().get(0)));
				builder.append(genererExpression(noeud.getFils().get(0)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("CMPEQ(r1, r2, r3)");
				builder.appendLineTab("CMPEQC(r3, 0, r4)");
				builder.appendLineTab("PUSH(r4)");
				break;
			default:
				break;
		}
		return builder.toString();
	}
	
	/**
	 * Génère une affectation.
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	private String genererAffectation(Affectation affectation) {
		StringBuilderPlus builder = new StringBuilderPlus();
		builder.appendLineTab(String.format("| %s", affectation.commentaire())); // COMMENTAIRE
		builder.append(genererExpression(affectation.getFilsDroit()));
		builder.appendLineTab("POP(r0)");
		Idf idf = (Idf) affectation.getFilsGauche();
		Symbole symbole = tds.rechercher((String) idf.getValeur(), (String) (idf.getFonction() != null ? idf.getFonction().getValeur() : SCOPE_GLOBAL));
		switch (symbole.getCat()) {
			case CAT_GLOBAL:
				builder.appendLineTab(String.format("ST(r0, %s)", idf.getValeur()));
				break;
			case CAT_LOCAL:
				builder.appendLineTab(String.format("PUTFRAME(r0, %d)", symbole.getRang() * 4));
				break;
			case CAT_PARAMETRE:
				builder.appendLineTab(String.format("PUTFRAME(r0, %d)", (symbole.getRang() + 3) * (-4)));
				break;
		}
		return builder.toString();
	}
	
	/**
	 * Génère une écriture.
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	private String genererEcrire(Ecrire ecrire) {
		StringBuilderPlus builder = new StringBuilderPlus();
		builder.appendLineTab(String.format("| %s", ecrire.commentaire())); // COMMENTAIRE
		builder.append(genererExpression(ecrire.getLeFils()));
		builder.appendLineTab("POP(r0)");
		builder.appendLineTab("WRINT()");
		return builder.toString();
	}
	
	/**
	 * Génère la condition "SI".
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	private String genererSi(Si si) {
		int numeroSi = si.getValeur();
		StringBuilderPlus builder = new StringBuilderPlus();
		builder.appendLineTab(String.format("| %s", si.commentaire())); // COMMENTAIRE
		builder.append(genererExpressionBooleenne(si.getCondition()));
		builder.appendLineTab("POP(r0)");
		builder.appendLineTab(String.format("BF(r0, sinon%d)", numeroSi));
		builder.append(genererBloc(si.getBlocAlors()));
		builder.appendLineTab(String.format("JMP(fsi%d)", numeroSi));
		builder.appendLine(String.format("sinon%d:", numeroSi));
		builder.append(genererBloc(si.getBlocSinon()));
		builder.appendLine(String.format("fsi%d:", numeroSi));
		return builder.toString();
	}
	
	/**
	 * Génère la condition "TANT QUE".
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	private String genererTantQue(TantQue tantque) {
		int numeroTq = tantque.getValeur();
		StringBuilderPlus builder = new StringBuilderPlus();
		builder.appendLineTab(String.format("| %s", tantque.commentaire())); // COMMENTAIRE
		builder.appendLine(String.format("tantque%d:", numeroTq));
		builder.append(genererExpressionBooleenne(tantque.getCondition()));
		builder.appendLineTab("POP(r0)");
		builder.appendLineTab(String.format("BF(r0, ftantque%d)", numeroTq));
		builder.append(genererBloc(tantque.getBloc()));
		builder.appendLineTab(String.format("BR(tantque%d)", numeroTq));
		builder.appendLine(String.format("ftantque%d:", numeroTq));
		return builder.toString();
	}
	
	/**
	 * Génère un appel à une fonction.
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	private String genererAppel(Appel appel) {
		StringBuilderPlus builder = new StringBuilderPlus();
		Symbole symbole = tds.rechercher((String) ((Fonction) appel.getValeur()).getValeur(), SCOPE_GLOBAL);
		//builder.appendLineTab(String.format("| Appel : %s", appel.commentaire())); // COMMENTAIRE
		if (symbole.getType().equals(TYPE_ENTIER)) {
			builder.appendLineTab("| Allocation de 1 car la fonction a un type de retour.");
			builder.appendLineTab("ALLOCATE(1)");
		}
		for (Noeud parametres: appel.getFils()) {
			builder.append(genererExpression(parametres));
		}
		builder.appendLineTab(String.format("CALL(%s, %d)", ((Fonction) appel.getValeur()).getValeur(), symbole.getNbParam()));
		return builder.toString();
	}
	
	/**
	 * Génère un retour de fonction.
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	private String genererRetourne(Retour retour) {
		StringBuilderPlus builder = new StringBuilderPlus();
		builder.appendLineTab(String.format("| %s", retour.commentaire())); // COMMENTAIRE
		builder.append(genererExpression(retour.getLeFils()));
		builder.appendLineTab("POP(r0)");
		Fonction fonction = (Fonction) retour.getValeur();
		Symbole symbole = tds.rechercher((String) fonction.getValeur(), SCOPE_GLOBAL);
		builder.appendLineTab(String.format("PUTFRAME(r0, %d)", (3 + symbole.getNbParam()) * (-4)));
		return builder.toString();
	}
}
