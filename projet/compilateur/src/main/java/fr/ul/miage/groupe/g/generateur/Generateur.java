package fr.ul.miage.groupe.g.generateur;

import java.util.List;
import java.util.Map;

import fr.ul.miage.arbre.Noeud;
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
		builder.appendLine(genererData(tds));
		builder.appendLine(genererCode(noeud));
		builder.appendLine("debut:");
		builder.appendLineTab("CALL(main)");
		builder.appendLineTab("HALT");
		builder.appendLine("pile:");
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
				if (symbole.getType().equals(TYPE_ENTIER) && symbole.getScope())
			}
		}
	}
}
