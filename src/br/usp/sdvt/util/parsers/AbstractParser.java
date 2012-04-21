package br.usp.sdvt.util.parsers;

import java.io.File;

public abstract class AbstractParser {
	
	protected abstract void parseFile(File file) throws Exception;
	
	public void readAndSave(String baseDirStr) throws Exception {
		
		File baseDir = null;
		
		if (baseDirStr != null) {
			baseDir = new File(baseDirStr);
		}
		
		System.out.println("Looking for directory " + baseDirStr);
		
		if ( baseDir != null && baseDir.exists() && baseDir.isDirectory()) {
			System.out.println("Directory successfully found.");					
		} else {
			throw new Exception("\nError: Invalid directory.");
		}
		
		System.out.println("Reading and saving data to the database, this can take several minutes...");
		
		load(baseDir);
		
		System.out.println("Finished! All data has been saved to the database.");
	}
	
	private void load(File file) throws Exception {
		
		if (file.isDirectory()) {
			for (File contents : file.listFiles()) {
				load(contents);
			}
		} else {
			parseFile(file);
		}
	}

}
