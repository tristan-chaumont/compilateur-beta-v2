package fr.ul.miage.groupe.g.generateur;

import java.util.List;
import java.util.Map;

import fr.ul.miage.arbre.Noeud;
import fr.ul.miage.arbre.Noeud.Categories;
import fr.ul.miage.arbre.NoeudObj;
import fr.ul.miage.arbre.NoeudInt;
import fr.ul.miage.groupe.g.main.StringBuilderPlus;
import fr.ul.miage.tds.Tds;
import fr.ul.miage.tds.Symbole;
import static fr.ul.miage.tds.Symbole.*;

public class Generateur {

	/**
	 * Génère la base du programme avec les include, le call à la fonction main, ainsi que la pile.
	 * @param noeud
	 * 		Arbre abstrait.
	 * @param tds
	 * 		Table des symboles.
	 * @return
	 * 		Le code asm.
	 */
	public String genererProgramme(Noeud noeud, Tds tds) {
		// true spécifie qu'on saute une ligne après l'insertion du String
		StringBuilderPlus builder = new StringBuilderPlus();
		builder.appendLine(".include beta.uasm");
		builder.appendLine(".include intio.uasm");
		builder.appendLine(".options tty");
		builder.appendLine(""); // SAUT DE LIGNE
		builder.appendLine("CMOVE(pile, SP)");
		builder.appendLine("BR(debut)");
		builder.appendLine(""); // SAUT DE LIGNE
		builder.appendLine(genererData(tds));
		builder.appendLine(genererCode(noeud));
		builder.appendLine("debut:");
		builder.appendLineTab("CALL(main)");
		builder.appendLineTab("HALT()");
		builder.appendLine(""); // SAUT DE LIGNE
		builder.appendLine("pile:");
		return builder.toString();
	}
	
	/**
	 * Génère les data du programme, donc les variables locales et globales ainsi que les paramètres des fonctions.
	 * @param tds
	 * 		Tables des symboles.
	 * @return
	 * 		Le code asm.
	 */
	private String genererData(Tds tds) {
		StringBuilderPlus builder = new StringBuilderPlus();
		for (Map.Entry<String, List<Symbole>> variable: tds.getTds().entrySet()) {
			int init = 0;
			List<Symbole> listeVariables = variable.getValue();
			for (Symbole symbole: listeVariables) {
				if (symbole.getType().equals(TYPE_ENTIER) && symbole.getScope().equals(SCOPE_GLOBAL)) {
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
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	private String genererCode(Noeud noeud) {
		StringBuilderPlus builder = new StringBuilderPlus();
		for (Noeud n: noeud.getFils()) {
			if (n.getCat().equals(Categories.FONCTION)){
				builder.appendLine(genererFonction(n));
			}
		}
		return builder.toString();
	}
	
	/**
	 * Génère tous les blocs d'une fonction
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	private String genererFonction(Noeud noeud) {
		NoeudObj noeudObj = (NoeudObj) noeud;
		StringBuilderPlus builder = new StringBuilderPlus();
		builder.appendLine(String.format("%s:", noeudObj.getValeur()));
		builder.appendLine(genererBloc(noeud));
		builder.appendLineTab("HALT()");
		return builder.toString();
	}
	
	/**
	 * Génère toutes les instructions d'un bloc.
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	private String genererBloc(Noeud noeud) {
		StringBuilderPlus builder = new StringBuilderPlus();
		for (Noeud n: noeud.getFils()) {
			builder.appendLine(genererInstruction(n));
		}
		return builder.toString();
	}
	
	/**
	 * Génère une instruction (affectation, ecrire, lire, si, tant que, appel).
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	private String genererInstruction(Noeud noeud) {
		StringBuilderPlus builder = new StringBuilderPlus();
		switch (noeud.getCat()) {
			case AFF:
				builder.appendLine(genererAffectation(noeud));
				break;
			case ECR:
				builder.appendLine(genererEcrire(noeud));
				break;
			case SI:
				builder.appendLine(genererSi(noeud));
				break;
			case TQ:
				builder.appendLine(genererTantQue(noeud));
				break;
			case APPEL:
				builder.appendLine(genererAppel(noeud));
				break;
			default:
				break;
		}
		return builder.toString();
	}
	
	/**
	 * Génère une expression (addition, soustraction, multiplication, division, constante, identificateur, lecture).
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	private String genererExpression(Noeud noeud) {
		StringBuilderPlus builder = new StringBuilderPlus();
		switch (noeud.getCat()) {
			case CONST:
				builder.appendLineTab(String.format("CMOVE(%s, r0)", ((NoeudInt) noeud).getValeur()));
				builder.appendLineTab("PUSH(r0)");
				break;
			case IDF:
				builder.appendLineTab(String.format("LD(%s, r0)", ((NoeudObj) noeud).getValeur()));
				builder.appendLineTab("PUSH(r0)");
				break;
			case PLUS:
				builder.appendLine(genererExpression(noeud.getFils().get(0)));
				builder.appendLine(genererExpression(noeud.getFils().get(1)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("ADD(r1, r2, r3)");
				builder.appendLineTab("PUSH(r3)");
				break;
			case MOINS:
				builder.appendLine(genererExpression(noeud.getFils().get(0)));
				builder.appendLine(genererExpression(noeud.getFils().get(1)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("SUB(r1, r2, r3)");
				builder.appendLineTab("PUSH(r3)");
				break;
			case MUL:
				builder.appendLine(genererExpression(noeud.getFils().get(0)));
				builder.appendLine(genererExpression(noeud.getFils().get(1)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("MUL(r1, r2, r3)");
				builder.appendLineTab("PUSH(r3)");
				break;
			case DIV:
				builder.appendLine(genererExpression(noeud.getFils().get(0)));
				builder.appendLine(genererExpression(noeud.getFils().get(1)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("DIV(r1, r2, r3)");
				builder.appendLineTab("PUSH(r3)");
				break;
			case LIRE:
				builder.appendLineTab("RDINT()");
				builder.appendLineTab("PUSH(r0)");
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
				builder.appendLine(genererExpression(noeud.getFils().get(0)));
				builder.appendLine(genererExpression(noeud.getFils().get(1)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("CMPLT(r2, r1, r3)");
				builder.appendLineTab("PUSH(r3)");
				break;
			case SUPE:
				builder.appendLine(genererExpression(noeud.getFils().get(0)));
				builder.appendLine(genererExpression(noeud.getFils().get(1)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("CMPLE(r2, r1, r3)");
				builder.appendLineTab("PUSH(r3)");
				break;
			case INF:
				builder.appendLine(genererExpression(noeud.getFils().get(0)));
				builder.appendLine(genererExpression(noeud.getFils().get(1)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("CMPLT(r1, r2, r3)");
				builder.appendLineTab("PUSH(r3)");
				break;
			case INFE:
				builder.appendLine(genererExpression(noeud.getFils().get(0)));
				builder.appendLine(genererExpression(noeud.getFils().get(0)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("CMPLE(r1, r2, r3)");
				builder.appendLineTab("PUSH(r3)");
				break;
			case EG:
				builder.appendLine(genererExpression(noeud.getFils().get(0)));
				builder.appendLine(genererExpression(noeud.getFils().get(0)));
				builder.appendLineTab("POP(r2)");
				builder.appendLineTab("POP(r1)");
				builder.appendLineTab("CMPEQ(r1, r2, r3)");
				builder.appendLineTab("PUSH(r3)");
				break;
			case DIF:
				builder.appendLine(genererExpression(noeud.getFils().get(0)));
				builder.appendLine(genererExpression(noeud.getFils().get(0)));
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
	private String genererAffectation(Noeud noeud) {
		StringBuilderPlus builder = new StringBuilderPlus();
		builder.appendLine(genererExpression(noeud.getFils().get(1)));
		builder.appendLineTab("POP(r0)");
		builder.appendLineTab(String.format("ST(r0, %s)", ((NoeudObj) noeud.getFils().get(0)).getValeur()));
		return builder.toString();
	}
	
	/**
	 * Génère une écriture.
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	private String genererEcrire(Noeud noeud) {
		StringBuilderPlus builder = new StringBuilderPlus();
		builder.appendLine(genererExpression(noeud.getFils().get(0)));
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
	private String genererSi(Noeud noeud) {
		String numeroSi = noeud.toString();
		StringBuilderPlus builder = new StringBuilderPlus();
		builder.appendLine(genererExpressionBooleenne(noeud.getFils().get(0)));
		builder.appendLineTab("POP(r0)");
		builder.appendLineTab(String.format("BF(r0, sinon%s)", numeroSi));
		builder.appendLine(genererBloc(noeud.getFils().get(1)));
		builder.appendLineTab(String.format("JMP(fsi%s)", numeroSi));
		builder.appendLine(String.format("sinon%s:", numeroSi));
		builder.appendLine(genererBloc(noeud.getFils().get(2)));
		builder.appendLine(String.format("fsi%s:", numeroSi));
		return builder.toString();
	}
	
	/**
	 * Génère la condition "TANT QUE".
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	private String genererTantQue(Noeud noeud) {
		String numeroTq = noeud.toString();
		StringBuilderPlus builder = new StringBuilderPlus();
		builder.appendLine(String.format("tantque%s:", numeroTq));
		builder.appendLine(genererExpressionBooleenne(noeud.getFils().get(0)));
		builder.appendLineTab("POP(r0)");
		builder.appendLineTab(String.format("BF(r0, ftantque%s)", numeroTq));
		builder.appendLine(genererBloc(noeud.getFils().get(1)));
		builder.appendLineTab(String.format("JMP(tantque%s)", numeroTq));
		builder.appendLineTab(String.format("ftantque%s:", numeroTq));
		return builder.toString();
	}
	
	/**
	 * Génère un appel à une fonction.
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	private String genererAppel(Noeud noeud) {
		StringBuilderPlus builder = new StringBuilderPlus();
		//TODO
		return builder.toString();
	}
}
