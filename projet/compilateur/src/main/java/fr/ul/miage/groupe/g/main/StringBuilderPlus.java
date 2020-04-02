package fr.ul.miage.groupe.g.main;

public class StringBuilderPlus {

    private final StringBuilder stringBuilder;

    public StringBuilderPlus() {
        this.stringBuilder = new StringBuilder();
    }
    
    public <T> StringBuilderPlus(T t, boolean lineSeparator) {
    	if(lineSeparator) {
        	this.stringBuilder = new StringBuilder();
        	this.stringBuilder.append(t).append(System.lineSeparator());
    	} else {
    		this.stringBuilder = new StringBuilder();
    		this.stringBuilder.append(t);
    	}
    }

    public <T> StringBuilderPlus append(T t) {
        stringBuilder.append(t);
        return this;
    }

    public <T> StringBuilderPlus appendLine(T t) {
        stringBuilder.append(t).append(System.lineSeparator());
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