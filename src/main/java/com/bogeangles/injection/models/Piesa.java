package com.bogeangles.injection.models;

import java.util.ArrayList;

public class Piesa {
	String nume;
	int referinta;
	int necesar;
	int restant;
	int piesePeSchimb;
	ArrayList<String> masini;
	int timpMontare;
	int timpDemontare;
	public String getNume() {
		return nume;
	}
	public int getReferinta() {
		return referinta;
	}
	public void setReferinta(int referinta) {
		this.referinta = referinta;
	}
	public void setNume(String nume) {
		this.nume = nume;
	}
	public int getNecesar() {
		return necesar;
	}
	public void setNecesar(int necesar) {
		this.necesar = necesar;
	}
	public int getRestant() {
		return restant;
	}
	public void setRestant(int restant) {
		this.restant = restant;
	}
	public int getPiesePeSchimb() {
		return piesePeSchimb;
	}
	public void setPiesePeSchimb(int piesePeSchimb) {
		this.piesePeSchimb = piesePeSchimb;
	}
	public ArrayList<String> getNumeMasini() {
		return masini;
	}
	public void setMasini(ArrayList<String> masini) {
		this.masini = masini;
	}
	public int getTimpMontare() {
		return timpMontare;
	}
	public void setTimpMontare(int timpMontare) {
		this.timpMontare = timpMontare;
	}
	public int getTimpDemontare() {
		return timpDemontare;
	}
	public void setTimpDemontare(int timpDemontare) {
		this.timpDemontare = timpDemontare;
	}
	public int getNumerulDeMasini(){
		return masini==null?0:masini.size();
	}
	public String toString(){
		StringBuffer sb= new StringBuffer(  );
		sb.append("nume: ").append(nume);
		sb.append("| ref: ").append(referinta);
		sb.append("| pps: ").append(piesePeSchimb);
		sb.append("| necesar: ").append(necesar);
		sb.append("| masini: ").append(masini);
		sb.append("\n");
		return sb.toString();
	}
}
