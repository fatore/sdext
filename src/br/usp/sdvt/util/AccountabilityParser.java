package br.usp.sdvt.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class AccountabilityParser extends AbstractParser {
	
	protected void parseFile(File file) throws Exception {		
		if (file.getName().matches(".*(?iu)txt")) {
			parseTXT(file);
		}
		if (file.getName().matches(".*(?iu)csv")) {				
			parseCSV(file);
		}
	}
	
	private void parseTXT(File file) {
		
	}
	
	private void parseCSV(File file) throws Exception {
		
		int year = Integer.parseInt(file.getParentFile().getParentFile().getParentFile().getName());
		
		if (file.getParentFile().getParentFile().getName().matches("(?iu)candidato")) {
			if (file.getParentFile().getName().matches("(?iu)receita")) {
				parseCandidateIncome(file, year);
			}  
			if (file.getParentFile().getName().matches("(?iu)despesa")) {
				parseCandidateExpense(file, year);
			} 
		}
	}
	
	private void parseCandidateIncome(File file, int year) {
		
		BufferedReader in = null;
		String line = null;
		
		try {
			in = new BufferedReader(new FileReader(file));
			
			// skip first line
			in.readLine();
			
			while ((line = in.readLine()) != null) {
				String pieces[] = line.split(";");
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void parseCandidateExpense(File file, int year) {
		
	}

	public static void main(String[] args) {
		
		String baseDir = "/work1/wokspace/social-vis/data/eleitorais/prestacao_contas";
		AbstractParser parser = new AccountabilityParser();
		try {
			parser.readAndSave(baseDir);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace(); // TODO remove later
		}
	}
}
