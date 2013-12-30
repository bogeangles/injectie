package com.bogeangles.injection.models;

import java.util.ArrayList;

import com.bogeangles.injection.exceptions.*;

public class Schimb {
	Masina masina;
	int nrSchimb;
	ArrayList<Action> actions;
	int remainingTime = 8*60;//hours x minutes
	public Masina getMasina() {
		return masina;
	}
	public void setMasina(Masina masina) {
		this.masina = masina;
	}
	public int getNrSchimb() {
		return nrSchimb;
	}
	public void setNrSchimb(int nrSchimb) {
		this.nrSchimb = nrSchimb;
	}
	public ArrayList<Action> getActions() {
		return actions;
	}
	public void addAction(Action action) throws NotEnoughTimeException {
		if(actions==null){
			actions = new ArrayList<Action>();
		}
		actions.add(action);
		decreaseTime(action.getDurata());
	}
	private void decreaseTime(int time) throws NotEnoughTimeException{
		
		System.out.println("time: "+time+" remt: "+remainingTime);
		
		remainingTime-=time;
		if(remainingTime<0){
			time = -1*remainingTime;
			remainingTime = 0;
			throw new NotEnoughTimeException(time);
		}
	}
	public int getReminingTime() {
		return remainingTime;
	}
	public void setReminingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}
	public int produce(Piesa p) {
		//System.out.println(p);
		float timpNecesarPePiesa = (float)480/(float)p.getPiesePeSchimb();
		System.out.println("timpNecesarPePiesa: "+(float)timpNecesarPePiesa);
		Action a = new Action();
		a.setNume(p.getNume());
		a.setRef(String.valueOf(p.getReferinta()));
		
		int nrPieseRealizabil = (int)Math.ceil((float)remainingTime/(float)timpNecesarPePiesa);
		System.out.println("se pot face "+nrPieseRealizabil);
		if(nrPieseRealizabil==0){
			return 0;//nu mai e loc in schimbul asta
		}
		if(nrPieseRealizabil>p.getNecesar()){//ramine loc si pt alt produs
			a.setDurata((int) Math.ceil(p.getNecesar()*timpNecesarPePiesa));
			a.setCellContent(String.valueOf(p.getNecesar()));
			p.setNecesar(0);
		} else {
			a.setDurata(remainingTime);
			p.setNecesar(p.getNecesar()-nrPieseRealizabil);
			a.setCellContent(String.valueOf(nrPieseRealizabil));
		}
		try {
			addAction(a);
		} catch (NotEnoughTimeException e) {
			System.out.println("nu se pot face toate, depasim cu"+(e.getTimeNeeded()));
		}		
		return remainingTime;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer("\n");
		sb.append(" nr schimb: ").append(nrSchimb);
		sb.append(" remainingTime: ").append(remainingTime);
		sb.append(" actiuni: ").append(actions);
		return sb.toString();
	}
}

