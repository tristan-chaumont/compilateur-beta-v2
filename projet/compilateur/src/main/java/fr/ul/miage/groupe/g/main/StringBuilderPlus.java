package fr.ul.miage.groupe.g.main;

public class StringBuilderPlus {

    private final StringBuilder stringBuilder;

    public StringBuilderPlus() {
        this.stringBuilder = new StringBuilder();
    }
    
    /**
     * Constructeur qui prend une valeur T ainsi qu'un booléen valant true s'il faut sauter une ligne (appendLine) après la valeur ou non.
     * Ce constructeur évite de devoir instancier le constructeur vide puis de faire un append / appendLine pour ajouter une première valeur.
     * @param t
     * 		Valeur à ajouter dans le builder.
     * @param lineSeparator
     * 		Booléen valant true s'il faut sauter une ligne ou non.
     */
    /*public <T> StringBuilderPlus(T t, boolean lineSeparator) {
    	if(lineSeparator) {
        	this.stringBuilder = new StringBuilder();
        	this.stringBuilder.append(t).append(System.lineSeparator());
    	} else {
    		this.stringBuilder = new StringBuilder();
    		this.stringBuilder.append(t);
    	}
    }*/

    public <T> StringBuilderPlus append(T t) {
        stringBuilder.append(t);
        return this;
    }
    
    public <T> StringBuilderPlus appendTab(T t) {
        stringBuilder.append("\t").append(t);
        return this;
    }

    public <T> StringBuilderPlus appendLine(T t) {
        stringBuilder.append(t).append(System.lineSeparator());
        return this;
    }
    
    public <T> StringBuilderPlus appendLineTab(T t) {
        stringBuilder.append("\t").append(t).append(System.lineSeparator());
        return this;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }
}