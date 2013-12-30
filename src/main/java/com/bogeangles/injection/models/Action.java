package com.bogeangles.injection.models;


public class Action {
	static String SETUP="CHANGE OVER" ;
	static String TEAR_DOWN="demontare-" ;
	String nume;
	String ref="";
	String cellContent;
	int needed=0;
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public int getNeeded() {
		return needed;
	}
	public void setNeeded(int needed) {
		this.needed = needed;
	}
	public String getCellContent() {
		return cellContent;
	}
	public void setCellContent(String cellContent) {
		this.cellContent = cellContent;
	}
	int durata;
	Schimb parinte;
	boolean completed = false;
	public String getNume() {
		return nume;
	}
	public void setNume(String nume) {
		this.nume = nume;
	}
	public int getDurata() {
		return durata;
	}
	public void setDurata(int durata) {
		this.durata = durata;
	}
	public Schimb getParinte() {
		return parinte;
	}
	public void setParinte(Schimb parinte) {
		this.parinte = parinte;
	}
	public String toString(){
		StringBuffer sb = new StringBuffer("\n");
		sb.append(" actiune: ").append(nume);
		sb.append(" durata: ").append(durata);
		sb.append(" xsl cell content: ").append(cellContent);
		return sb.toString();
	}
	public String getReference() {
		return ref;
	}
	public void setReference(String ref){
		this.ref = ref;
	}
}
