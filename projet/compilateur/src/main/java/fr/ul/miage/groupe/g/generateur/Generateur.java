package fr.ul.miage.groupe.g.generateur;

import fr.ul.miage.arbre.Noeud;
import fr.ul.miage.groupe.g.main.StringBuilderPlus;
import fr.ul.miage.tds.Tds;

public class Generateur {


	public String genererProgramme(Noeud noeud, Tds tds) {
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
}
