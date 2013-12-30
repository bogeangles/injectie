package com.bogeangles.injection.planner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.bogeangles.injection.models.Masina;
import com.bogeangles.injection.models.Piesa;
import com.bogeangles.injection.util.IOUtil;

public class MasterPlanner {
	
	public static String FILE_NAME = "plan Imm.xlsx";
	public static String SHEET_NAME = "tabelul initial";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IOUtil.initConstants();
		MasterPlanner mp = new MasterPlanner();
		mp.Planning(FILE_NAME, SHEET_NAME, IOUtil.SCHIMBURI_LIBERE);
	}
	
	
	private void Planning(String fileName, String sheetName, int schimburi){
		ArrayList<Piesa> piese = IOUtil.loadPiese();
		HashMap<String,Masina> listaMasini = IOUtil.loadMasini(fileName, sheetName, schimburi);
		for(int i=0; i<piese.size(); i++){
			Piesa pc = piese.get(i);
			if(pc.getNecesar()==0){
				continue;
			}
			ArrayList<String> numeMasini = pc.getNumeMasini();
			for(int j=0; j<numeMasini.size(); j++){
				Masina mc = listaMasini.get(numeMasini.get(j));
				if(mc.getSchimburiLibere()>0){
					mc.planItem(pc);
				}
				if(pc.getNecesar()==0){
					break;
				}
			}
		}
		
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		boolean needExtraDays = false;
		for (int i=0; i<piese.size(); i++){
			if(piese.get(i).getNecesar()>0){
				System.out.println("avem nevoie de schimburi simbata pt "+piese.get(i).getNume()+" piese de facut in weekend:"+piese.get(i).getNecesar());
				needExtraDays = true;
			}
		}
		if(needExtraDays){
			Iterator<Map.Entry<String, Masina>> it = listaMasini.entrySet().iterator();
			
			while(it.hasNext()){
				Map.Entry<String, Masina> nume = it.next();
				Masina m = nume.getValue();
				System.out.println("adaugam 3 schimburi la masina "+m.getNume());
				m.setSchimburiLibere(4);
				m.setNextShift();
			}
			for(int i=0; i<piese.size(); i++){
				Piesa pc = piese.get(i);
				if(pc.getNecesar()==0){
					continue;
				}
				ArrayList<String> numeMasini = pc.getNumeMasini();
				for(int j=0; j<numeMasini.size(); j++){
					Masina mc = listaMasini.get(numeMasini.get(j));
					if(mc.getSchimburiLibere()>0){
						mc.planItem(pc);
					}
					if(pc.getNecesar()==0){
						break;
					}
				}
			}
		}
		needExtraDays = false;
		for (int i=0; i<piese.size(); i++){
			if(piese.get(i).getNecesar()>0){
				System.out.println("avem nevoie de schimburi duminica pt "+piese.get(i).getNume());
				needExtraDays = true;
			}
		}
		if(needExtraDays){
			Iterator<Map.Entry<String, Masina>> it = listaMasini.entrySet().iterator();
			
			while(it.hasNext()){
				Map.Entry<String, Masina> nume = it.next();
				Masina m = nume.getValue();
				System.out.println("adaugam 3 schimburi la masina "+m.getNume());
				m.setSchimburiLibere(3);
			}
			for(int i=0; i<piese.size(); i++){
				Piesa pc = piese.get(i);
				if(pc.getNecesar()==0){
					continue;
				}
				ArrayList<String> numeMasini = pc.getNumeMasini();
				for(int j=0; j<numeMasini.size(); j++){
					Masina mc = listaMasini.get(numeMasini.get(j));
					if(mc.getSchimburiLibere()>0){
						mc.planItem(pc);
					}
					if(pc.getNecesar()==0){
						break;
					}
				}
			}
		}
		IOUtil.writePlan(FILE_NAME, "gp", listaMasini);
	}

}
