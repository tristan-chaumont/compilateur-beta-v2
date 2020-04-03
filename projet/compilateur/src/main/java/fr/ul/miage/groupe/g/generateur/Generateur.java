package fr.ul.miage.groupe.g.generateur;

import java.util.List;
import java.util.Map;

import fr.ul.miage.arbre.Noeud;
import fr.ul.miage.arbre.Noeud.Categories;
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
		StringBuilderPlus builder = new StringBuilderPlus(".include beta.uasm", true);
		builder.appendLine(".include intio.uasm");
		builder.appendLine(".options tty");
		builder.appendLine("");
		builder.appendLine("CMOVE(pile, SP)");
		builder.appendLine("BR(debut)");
		builder.appendLine(genererData(tds));
		builder.appendLine(genererCode(noeud));
		builder.appendLine("debut:");
		builder.appendLineTab("CALL(main)");
		builder.appendLineTab("HALT");
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
	public String genererData(Tds tds) {
		StringBuilderPlus builder = new StringBuilderPlus();
		for (Map.Entry<String, List<Symbole>> variable: tds.getTds().entrySet()) {
			int init = 0;
			List<Symbole> listeVariables = variable.getValue();
			for (Symbole symbole: listeVariables) {
				if (symbole.getType().equals(TYPE_ENTIER) && symbole.getScope().equals(SCOPE_GLOBAL)) {
					if (symbole.getProp().contains(PROP_VALEUR)) {
						init = symbole.getValeur();
					}
					builder.appendLine(String.format("%s: LONG(%d)", variable.getKey(), init));
				}
			}
		}
		return builder.toString();
	}
	
	/**
	 * Génère les blocs de code du programme.
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	public String genererCode(Noeud noeud) {
		StringBuilderPlus builder = new StringBuilderPlus();
		for (Noeud n: noeud.getFils()) {
			if (n.getCat().equals(Categories.FONCTION)){
				builder.appendLine(genererFonction(n));
			}
		}
		return builder.toString();
	}
	
	/**
	 * Génère la fonction du code.
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	public String genererFonction(Noeud noeud) {
		StringBuilderPlus builder = new StringBuilderPlus();
		for (Noeud n: noeud.getFils()) {
			builder.appendLine(genererExpression(n));
		
		}
		return builder.toString();
	}
	
	/**
	 * Génère une expression (addition, soustraction, multiplication, division, constante, identificateur).
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	public String genererExpression(Noeud noeud) {
		StringBuilderPlus builder = new StringBuilderPlus();
		switch (noeud.getCat()) {
			case CONST:
				builder.appendLine(String.format("CMOVE(%s, r0)", noeud.toString()));
				builder.appendLine("PUSH(r0)");
				break;
			case IDF:
				builder.appendLine(String.format("LD(%s, r0)", noeud.toString()));
				builder.appendLine("PUSH(r0)");
				break;
			case PLUS:
				builder.appendLine(genererExpression(noeud.getFils().get(0)));
				builder.appendLine(genererExpression(noeud.getFils().get(1)));
				builder.appendLine("POP(r2)");
				builder.appendLine("POP(r1)");
				builder.appendLine("ADD(r1, r2, r3)");
				builder.appendLine("PUSH(r3)");
				break;
			case MOINS:
				builder.appendLine(genererExpression(noeud.getFils().get(0)));
				builder.appendLine(genererExpression(noeud.getFils().get(1)));
				builder.appendLine("POP(r2)");
				builder.appendLine("POP(r1)");
				builder.appendLine("SUB(r1, r2, r3)");
				builder.appendLine("PUSH(r3)");
				break;
			case MUL:
				builder.appendLine(genererExpression(noeud.getFils().get(0)));
				builder.appendLine(genererExpression(noeud.getFils().get(1)));
				builder.appendLine("POP(r2)");
				builder.appendLine("POP(r1)");
				builder.appendLine("MUL(r1, r2, r3)");
				builder.appendLine("PUSH(r3)");
				break;
			case DIV:
				builder.appendLine(genererExpression(noeud.getFils().get(0)));
				builder.appendLine(genererExpression(noeud.getFils().get(1)));
				builder.appendLine("POP(r2)");
				builder.appendLine("POP(r1)");
				builder.appendLine("DIV(r1, r2, r3)");
				builder.appendLine("PUSH(r3)");
				break;
			default:
				break;
		}
		return builder.toString();
	}
	
	/**
	 * Génère un bloc d'instruction.
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	public String genererBloc(Noeud noeud) {
		StringBuilderPlus builder = new StringBuilderPlus();
		for (Noeud n: noeud.getFils()) {
			builder.appendLine(genererInstruction(n));
		}
		return builder.toString();
	}
	
	/**
	 * Génère une instruction (affectation, ecrire, lire, ).
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	public String genererInstruction(Noeud noeud) {
		StringBuilderPlus builder = new StringBuilderPlus();
		switch (noeud.getCat()) {
			case EG:
				builder.appendLine(genererAffectation(noeud));
				break;
			case ECR:
				builder.appendLine(genererEcrire(noeud));
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
	public String genererAffectation(Noeud noeud) {
		StringBuilderPlus builder = new StringBuilderPlus(genererExpression(noeud.getFils().get(1)), true);
		builder.appendLine("POP(r0)");
		builder.appendLine(String.format("ST(r0, %s)", noeud.getFils().get(0).toString()));
		return builder.toString();
	}
	
	/**
	 * Génère une écriture.
	 * @param noeud
	 * 		Arbre abstrait.
	 * @return
	 * 		Le code asm.
	 */
	public String genererEcrire(Noeud noeud) {
		StringBuilderPlus builder = new StringBuilderPlus(genererExpression(noeud.getFils().get(0)), true);
		builder.appendLine("POP(r0)");
		builder.appendLine("WRINT()");
		return builder.toString();
	}
}
