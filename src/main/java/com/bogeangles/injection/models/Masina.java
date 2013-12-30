package com.bogeangles.injection.models;

import java.util.ArrayList;

import com.bogeangles.injection.exceptions.NotEnoughTimeException;

public class Masina {
	String nume;
	int schimburiLibere;
	ArrayList<Schimb> schimburi;
	Schimb current;
	Piesa installedMold = null;
	int changeOverTime = 0;
	public int getChangeOverTime() {
		return changeOverTime;
	}

	public void setChangeOverTime(int changeOverTime) {
		this.changeOverTime = changeOverTime;
	}

	public Masina(int nrSchimburi){
		schimburi = new ArrayList<Schimb>();
		for(int i=0; i< nrSchimburi; i++){
			Schimb s = new Schimb();
			s.setMasina(this);
			s.setNrSchimb(i+1);
			schimburi.add(s);
		}
		current =schimburi.get(0);
		schimburiLibere = nrSchimburi;
	}
	
	public void setNextShift(){
		System.out.println("schimbul "+current.nrSchimb +" plin, trecem la urmatorul ");
		schimburiLibere--;
		System.out.println(schimburiLibere);
		if(schimburiLibere<=0) {
			return;
		}
		current = schimburi.get(current.nrSchimb);
	}
	
	public void planItem(Piesa p) {
		
		changeOver(p);
		
		while(p.getNecesar()>0){
			System.out.println("schimburi libere: "+schimburiLibere+"; schimb current: "+current.nrSchimb);
			current.produce(p);
			System.out.println("s-a returnat "+p.getNecesar());
			if(p.getNecesar()>0){
				setNextShift();
			}	
			if(schimburiLibere==0){
				return;
			}
		}
		System.out.println("s-a alocat tot din produsu: "+p);
		return;
	}
	
	private void changeOver(Piesa p){
		//if(installedMold==null){
		if(installedMold!=null&&p!=null&&installedMold.getNume().equals(p.getNume())){
			return;
		}
			System.out.println("instalam matrita: "+p.getNume());
			installedMold = p;
			Action a = new Action();
			a.setNume(Action.SETUP+" - "+getChangeOverTime());
			a.setDurata(getChangeOverTime());
			System.out.println("motarea dureaza: "+a.getDurata());
			try {
				current.addAction(a);
			} catch (NotEnoughTimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setNextShift();
				a = new Action();
				a.setNume(Action.SETUP+" - "+getChangeOverTime());
				a.setDurata(e.getTimeNeeded());
			}
			/*} else if(!installedMold.getNume().equals(p.getNume())){
			//demontam
			Action a = new Action();
			a.setNume(Action.TEAR_DOWN+installedMold.getNume());
			a.setDurata(installedMold.getTimpDemontare());
			try {
				current.addAction(a);
			} catch (NotEnoughTimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setNextShift();
				a = new Action();
				a.setNume(Action.TEAR_DOWN+installedMold.getNume());
				a.setDurata(installedMold.getTimpDemontare());
			}
			installedMold = p;
			a = new Action();
			a.setNume(Action.SETUP+p.getNume());
			a.setDurata(p.getTimpMontare());
			System.out.println("motarea dureaza: "+p.getTimpMontare());
			try {
				current.addAction(a);
			} catch (NotEnoughTimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setNextShift();
				a = new Action();
				a.setNume(Action.SETUP+p.getNume());
				a.setDurata(e.getTimeNeeded());
			}*/
		
	}
	public String getNume() {
		return nume;
	}
	public void setNume(String nume) {
		this.nume = nume;
	}
	public int getSchimburiLibere() {
		return schimburiLibere;
	}
	public void setSchimburiLibere(int schimburiLibere) {
		this.schimburiLibere = schimburiLibere;
		for(int i=0; i< schimburiLibere; i++){
			Schimb s = new Schimb();
			s.setMasina(this);
			s.setNrSchimb(schimburi.size()+1);
			schimburi.add(s);
		}
	}
	public ArrayList<Schimb> getSchimburi(){
		return schimburi;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer("\n");
		sb.append("nume").append(nume);
		sb.append("schimburi libere:").append(schimburiLibere);
		sb.append("schimburi").append(schimburi);
		return sb.toString();
	}
}
