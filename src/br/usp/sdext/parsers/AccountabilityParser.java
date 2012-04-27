package br.usp.sdext.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import br.usp.sdext.core.Candidature;
import br.usp.sdext.core.Donor;
import br.usp.sdext.core.Income;
import br.usp.sdext.util.FileOperations;


public class AccountabilityParser extends AbstractParser {
	
	private Map<Donor, Donor> donors;
	private Long incomesCounter;
	private boolean old;
	
	public AccountabilityParser() {
		donors = new HashMap<Donor, Donor>();
		incomesCounter = 0L;
	}
	
	protected void loadFile(File file) throws Exception {	
		if (file.getName().matches(".*(?iu)csv")) {	
			old = true;
		}
		if (file.getName().matches(".*(?iu)txt")) {
			old = false;
		}
		parseFile(file);
	}
	
	private void parseFile(File file) throws Exception {
		
		int year = Integer.parseInt(file.getParentFile().getParentFile().getParentFile().getName());
		
		if (old) {
			if (file.getParentFile().getParentFile().getName().matches("(?iu)candidato")) {
				if (file.getParentFile().getName().matches("(?iu)receita")) {
					parseIncome(file, year);
				}  
				if (file.getParentFile().getName().matches("(?iu)despesa")) {
					parseExpense(file, year);
				} 
			}
		} else {
			if (file.getParentFile().getParentFile().getName().matches("(?iu)candidato")) {
				if (file.getName().matches("(?iu)receitas.*")) {
					parseIncome(file, year);
				}  
				if (file.getName().matches("(?iu)despesas.*")) {
					parseExpense(file, year);
				} 
			}
		}
	}
	
	private void parseIncome(File file, int year) throws Exception {
		
		BufferedReader in;
		if (old) {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-1"));
		} else {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		}
		String line = null;
		
		int noLines = FileOperations.getNoLines(file.getAbsolutePath());
		
		if (old) {
			System.out.println("Parsing candidates incomes of year " + year + ".");
		} else {
			System.out.println("Parsing candidates incomes of year " + year + 
					" for " + file.getParentFile().getName() + ".");
		}
		System.out.println("Progress: ");
		
		// ignore first line
		line = in.readLine();
		int count = 0;
		while ((line = in.readLine()) != null) {
			
			// donor
			Donor donor = Donor.parse(line, old);
			Donor mapDonor = donors.get(donor);
			if (mapDonor == null) {
				donor.setID(new Long(donors.size()));
				donors.put(donor, donor);
				donor.save();
			} else {
				donor = mapDonor;
			}
			
			// income
			Income income = Income.parse(line, old);
			income.setID(incomesCounter++);
			income.setDonor(donor);
			income.save();
			
			// binding
			Candidature.addIncome(income, line, year, old);
			
			if (count++ % (noLines / 10)  == 0) {
				System.out.printf("\t%.2f%%\n", (float) ((count / (float) noLines) * 100));
			}
		}
	}
	
	private void parseExpense(File file, int year) throws Exception {}
	
	protected void save() {
		System.out.println("\nTotal objects loaded");
		System.out.println("\tDonors: " + donors.size());
		System.out.println("\tIncomes: " + incomesCounter);
		
		System.out.println("\nSaving objects in the database, this can take several minutes.");
		
		long start, finish;
		
		// donors
		System.out.print("\tSaving donors...");
		start = System.currentTimeMillis();
		for (Donor donor : donors.values()) {
//			donor.save();
		}
		finish = System.currentTimeMillis();
		System.out.printf("Finished!(%d mins %d secs)\n",
				(int) ((finish - start) / 60000),(int) ((finish - start) % 60000) / 1000);
	}

	public static void main(String[] args) throws Exception {
		
		String baseDir = "/work1/wokspace/social-vis/data/eleitorais/prestacao_contas/2010";
		AbstractParser parser = new AccountabilityParser();
		parser.loadAndSave(baseDir);
	}
}
