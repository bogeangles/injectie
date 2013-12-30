package com.bogeangles.injection.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hamcrest.MatcherAssert;

import com.bogeangles.injection.models.Action;
import com.bogeangles.injection.models.Masina;
import com.bogeangles.injection.models.Piesa;
import com.bogeangles.injection.models.Schimb;
import com.bogeangles.injection.planner.MasterPlanner;

public class IOUtil {
	public static int ACTION_NAME = 1;
	public static int ACTION_REF = 2;
	public static int ACTION_NEEDED = 3;
	public static int SCHIMB = 4;
	public static int HEADER=1;
	public static int NUME=2;
	public static int REF=3;
	public static int PIESE_PE_SCHIMB=11;
	public static int NECESAR=12;
	public static int M1=13;
	public static int SCHIMBURI_LIBERE = 15;
	
	private static Properties changeOver;
	
	public static void writeSheet(){
		
	}
	
	public static Sheet loadSheet(){
		try {
			Workbook wb = WorkbookFactory.create(new FileInputStream(MasterPlanner.FILE_NAME));
			return wb.getSheet(MasterPlanner.SHEET_NAME);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			try {
				return WorkbookFactory.create(IOUtil.class.getClassLoader().getResourceAsStream(MasterPlanner.FILE_NAME)).getSheet(MasterPlanner.SHEET_NAME);
			} catch (InvalidFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Piesa> loadPiese(){
		initConstants();
		Sheet sheet = loadSheet();
		
		ArrayList<Piesa> ress = new ArrayList<Piesa>();
		int lines = sheet.getLastRowNum();
		Row header = sheet.getRow(HEADER);
		for (int i=2; i<lines; i++){
			Row row = sheet.getRow(i);
			if(row !=null && row.getCell(NUME)!=null && row.getCell(NUME).getCellType()==Cell.CELL_TYPE_STRING){
				Piesa p = new Piesa();
				p.setNume(row.getCell(NUME).getStringCellValue());
				if(row.getCell(REF).getCellType()==Cell.CELL_TYPE_NUMERIC){
					p.setReferinta((int)row.getCell(REF).getNumericCellValue());
				}
				p.setPiesePeSchimb((int)row.getCell(PIESE_PE_SCHIMB).getNumericCellValue());
				p.setNecesar((int)row.getCell(NECESAR).getNumericCellValue());				
				p.setMasini(setMasini(row, header));
				ress.add(p);
				System.out.println(p);
			}
		}
		return ress;
	}
	
	public static void initConstants() {
		Properties p = new Properties();
		changeOver = new Properties();
		try {
			
			changeOver.load(IOUtil.class.getClassLoader().getResourceAsStream("changeOver.properties"));

			p.load(IOUtil.class.getClassLoader().getResourceAsStream("coloane.properties"));
			MasterPlanner.FILE_NAME = p.getProperty("file.name");
			System.out.println(MasterPlanner.FILE_NAME);
			MasterPlanner.SHEET_NAME = p.getProperty("sheet.name");
			System.out.println(MasterPlanner.SHEET_NAME);
			
			System.out.println(p.getProperty("product.name")+"product.name");
			
			NUME=Integer.parseInt(p.getProperty("product.name"));
			System.out.println(p.getProperty("referinta")+"referinta");
			REF=Integer.parseInt(p.getProperty("referinta"));
			System.out.println(p.getProperty("piese.pe.schimb")+"piese.pe.schimb");
			PIESE_PE_SCHIMB=Integer.parseInt(p.getProperty("piese.pe.schimb"));
			System.out.println(p.getProperty("necesar.de.piese")+"necesar.de.piese");
			NECESAR=Integer.parseInt(p.getProperty("necesar.de.piese"));
			System.out.println(p.getProperty("prima.masina")+"prima.masina");
			M1=Integer.parseInt(p.getProperty("prima.masina"));
			SCHIMBURI_LIBERE  = Integer.parseInt(p.getProperty("schimburi.libere"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static HashMap<String, Masina> loadMasini(String fileName, String sheetName, int schimburi){
		Sheet sheet = loadSheet();
		HashMap<String, Masina> ress = new HashMap<String, Masina>();
		Row header = sheet.getRow(HEADER);
		int endCell = header.getLastCellNum();
		for (int i=M1; i<endCell; i++){
			Masina m = new Masina(schimburi);
			System.out.println("i: "+i+" type: "+header.getCell(i).getCellType());
			System.out.println(header.getCell(i).getStringCellValue());
			m.setNume(header.getCell(i).getStringCellValue());
			m.setChangeOverTime(Integer.parseInt(changeOver.getProperty(m.getNume()).trim()));
			ress.put(m.getNume(), m);
		}
		
		return ress;
	}
	
	private static ArrayList<String> setMasini(Row row, Row header){
		int endCell = row.getLastCellNum();
		ArrayList<String> masini = new ArrayList<String>();
		for (int i=M1; i<endCell; i++){
			if(row.getCell(i)!=null && row.getCell(i).getCellType()==Cell.CELL_TYPE_FORMULA){
				masini.add(header.getCell(i).getStringCellValue());
			}
		}
		return masini;
	}
	
	/**
	 * @param fileName
	 * @param sheetName
	 * @param masini
	 */
	public static void writePlan(String fileName, String sheetName, HashMap<String, Masina> masini){
		Workbook wb = null;
		int rowCnt = 0;
		try {
			wb = WorkbookFactory.create(new FileInputStream(fileName));
		}catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			try {
				wb = WorkbookFactory.create(IOUtil.class.getClassLoader().getResourceAsStream(fileName));
			} catch (InvalidFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			Sheet sheet = wb.createSheet(sheetName);
			Row topRow = sheet.createRow(rowCnt++);

			sheet.addMergedRegion(new CellRangeAddress(topRow.getRowNum(), topRow.getRowNum(), 4, 6));
			if(topRow.getCell(4)==null){
				Cell c = topRow.createCell(4);
				c.setCellType(Cell.CELL_TYPE_STRING);
				c.setCellValue("luni");	
			} else {
				topRow.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
				topRow.getCell(4).setCellValue("luni");					
			}
			sheet.addMergedRegion(new CellRangeAddress(topRow.getRowNum(), topRow.getRowNum(), 7, 9));
			if(topRow.getCell(7)==null){
				Cell c = topRow.createCell(7);
				c.setCellType(Cell.CELL_TYPE_STRING);
				c.setCellValue("marti");	
			} else {
				topRow.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
				topRow.getCell(7).setCellValue("marti");					
			}
			sheet.addMergedRegion(new CellRangeAddress(topRow.getRowNum(), topRow.getRowNum(), 10, 12));
			if(topRow.getCell(10)==null){
				Cell c = topRow.createCell(10);
				c.setCellType(Cell.CELL_TYPE_STRING);
				c.setCellValue("miercuri");	
			} else {
				topRow.getCell(10).setCellType(Cell.CELL_TYPE_STRING);
				topRow.getCell(10).setCellValue("miercuri");					
			}
			sheet.addMergedRegion(new CellRangeAddress(topRow.getRowNum(), topRow.getRowNum(), 13, 15));
			if(topRow.getCell(13)==null){
				Cell c = topRow.createCell(13);
				c.setCellType(Cell.CELL_TYPE_STRING);
				c.setCellValue("joi");	
			} else {
				topRow.getCell(13).setCellType(Cell.CELL_TYPE_STRING);
				topRow.getCell(13).setCellValue("joi");					
			}
			sheet.addMergedRegion(new CellRangeAddress(topRow.getRowNum(), topRow.getRowNum(), 16, 18));
			if(topRow.getCell(16)==null){
				Cell c = topRow.createCell(16);
				c.setCellType(Cell.CELL_TYPE_STRING);
				c.setCellValue("vineri");	
			} else {
				topRow.getCell(16).setCellType(Cell.CELL_TYPE_STRING);
				topRow.getCell(16).setCellValue("vineri");					
			}
			sheet.addMergedRegion(new CellRangeAddress(topRow.getRowNum(), topRow.getRowNum(), 19, 21));
			if(topRow.getCell(19)==null){
				Cell c = topRow.createCell(19);
				c.setCellType(Cell.CELL_TYPE_STRING);
				c.setCellValue("sambata");	
			} else {
				topRow.getCell(19).setCellType(Cell.CELL_TYPE_STRING);
				topRow.getCell(19).setCellValue("sambata");					
			}
			sheet.addMergedRegion(new CellRangeAddress(topRow.getRowNum(), topRow.getRowNum(), 22, 24));
			if(topRow.getCell(22)==null){
				Cell c = topRow.createCell(22);
				c.setCellType(Cell.CELL_TYPE_STRING);
				c.setCellValue("duminica");	
			} else {
				topRow.getCell(22).setCellType(Cell.CELL_TYPE_STRING);
				topRow.getCell(22).setCellValue("duminica");					
			}
			
			Row secondRow = sheet.createRow(rowCnt++);
			for(int i=4; i<21; i++){
				Cell c = secondRow.createCell(i);
				c.setCellType(Cell.CELL_TYPE_NUMERIC);
				c.setCellValue(i-3);
			}
			
			
			for (int i=0; i<masini.size(); i++){
				Masina m = (Masina) masini.values().toArray()[i];
				Row newRow = null;
				int masinaRowMerge = rowCnt;
				int cellX=SCHIMB;
				ArrayList<Schimb> schimburi = m.getSchimburi();
				System.out.println("starting machine: "+m.getNume());
				for (int j=0; j<schimburi.size(); j++){
					Schimb s = schimburi.get(j);
					ArrayList<Action> actions = s.getActions();
					if(actions == null){
						continue;
					}
					//if(j==0){//we have to create a new row for each action
					for (int k=0; k<actions.size(); k++){
						Action a = actions.get(k);
						Cell c = null;
//						System.out.println(newRow+" "+);
						if(newRow==null || newRow.getCell(ACTION_NAME)==null || !newRow.getCell(ACTION_NAME).getStringCellValue().equals(a.getNume())){
							System.out.println("new row "+rowCnt+ " - " +a.getNume());
							newRow = sheet.createRow(rowCnt++);
//							System.out.println("newRow: "+newRow.getRowNum());
							c = newRow.createCell(ACTION_NAME);
							c.setCellType(Cell.CELL_TYPE_STRING);
							c.setCellValue(a.getNume());
							
							c = newRow.createCell(ACTION_REF);
							c.setCellType(Cell.CELL_TYPE_NUMERIC);
							c.setCellValue(a.getReference());
							
							c = newRow.createCell(ACTION_NEEDED);
							c.setCellType(Cell.CELL_TYPE_NUMERIC);
							c.setCellValue(a.getNeeded());
						}
						c = newRow.getCell(cellX);
						if( c == null ){
							c = newRow.createCell(cellX);
						} 
						try{
							int cellValue = Integer.parseInt(a.getCellContent());
							c.setCellType(Cell.CELL_TYPE_NUMERIC);
							c.setCellValue(cellValue);
						} catch (NumberFormatException e){
							c.setCellType(Cell.CELL_TYPE_STRING);
							c.setCellValue(a.getCellContent());
						}
						
					}
					cellX++;
				}
				//merging masina cells
				System.out.println(masinaRowMerge+" "+ rowCnt);
				if(masinaRowMerge==rowCnt){
					continue;
				}
				sheet.addMergedRegion(new CellRangeAddress(masinaRowMerge, rowCnt-1, 0, 0));
				
				//System.out.println("<<<<"+sheet.getRow(masinaRowMerge).getRowNum() +masinaRowMerge);
//				if(sheet.getRow(masinaRowMerge)==null){
//					continue;
//				}
				Cell cellMasina = sheet.getRow(masinaRowMerge).getCell(0);
				if(cellMasina == null){
					cellMasina = sheet.getRow(masinaRowMerge).createCell(0);
				}
				cellMasina.setCellType(Cell.CELL_TYPE_STRING);
				cellMasina.setCellValue(m.getNume());
				
			}
			
			try {
				FileOutputStream fos = new FileOutputStream("test.xlsx");
				wb.write(fos);
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
}
