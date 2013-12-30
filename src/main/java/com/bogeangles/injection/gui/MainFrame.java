package com.bogeangles.injection.gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import net.miginfocom.swing.MigLayout;

import com.bogeangles.injection.planner.MasterPlanner;
import com.bogeangles.injection.util.IOUtil;

public class MainFrame {
	
	JFrame mainFrame = null;
	JMenuBar menu;
	File sourceFile = null;
	
	private void setSourceFile(File f){
		this.sourceFile = f;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MainFrame mf = new MainFrame();
		mf.init();
		
	}

	private void init() {
		mainFrame = new JFrame("Planificare injectie");
		
		menu = new JMenuBar();
		
		//File menu definition
		JMenu file = new JMenu("Fisier");
		JMenuItem mi1 = new JMenuItem("Incarcati fisierul");
		final JFileChooser fc = new JFileChooser();
		mi1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				fc.showOpenDialog(mainFrame);
				setSourceFile(fc.getSelectedFile());
				System.out.println("done...");
			}	
		});
		
		//Settings menu definition
		IOUtil.initConstants();
		JMenu tools = new JMenu("Instrumente");
		JMenuItem settings = new JMenuItem("Settari");
		settings.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				displaySettings();
			}
		});
		tools.add(settings);
		
		file.add(mi1);
		menu.add(tools);
		menu.add(file);
		
		mainFrame.setJMenuBar(menu);
		
		mainFrame.setSize(new Dimension(1000, 800));
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}

	protected void displaySettings() {
		System.out.println("display settings");
		JLabel filenameL = new JLabel("Nume fisier");
		JLabel sheetNameL = new JLabel("Nume sheet");
		JLabel productNameL = new JLabel("Nume produs");
		JLabel refL = new JLabel("Referinta");
		JLabel partsPerCycleL = new JLabel("Piese pe schimb");
		JLabel neededL = new JLabel("Necesar");
		JLabel firstMachineL = new JLabel("Prima masina");
		JLabel unusedShifts = new JLabel("Schimburi libere");
		
		JDialog dialog = new JDialog(mainFrame);
		dialog.setSize(new Dimension(800,600));
		dialog.setAlwaysOnTop(true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		//MigLayout first arg as string: metadata about rows, 
		//secong string: metadata about columns(margin [value] margin ]value], margin)
		dialog.setLayout(new MigLayout(
				"",
				"20[]20[]20",
				""
				));
		
		dialog.add(filenameL);
		dialog.add(new JLabel(MasterPlanner.FILE_NAME), "wrap");
		dialog.add(sheetNameL);
		dialog.add(new JLabel(MasterPlanner.SHEET_NAME), "wrap");
		dialog.add(productNameL);
		dialog.add(new JLabel(String.valueOf(IOUtil.NUME)), "wrap");
		dialog.add(refL);
		dialog.add(new JLabel(String.valueOf(IOUtil.REF)), "wrap");
		dialog.add(partsPerCycleL);
		dialog.add(new JLabel(String.valueOf(IOUtil.PIESE_PE_SCHIMB)), "wrap");
		dialog.add(neededL);
		dialog.add(new JLabel(String.valueOf(IOUtil.NECESAR)), "wrap");
		dialog.add(firstMachineL);
		dialog.add(new JLabel(String.valueOf(IOUtil.M1)), "wrap");
		dialog.add(unusedShifts);
		dialog.add(new JLabel(String.valueOf(IOUtil.SCHIMBURI_LIBERE)), "wrap");
		
		
		dialog.setVisible(true);
		
		
	}

}
