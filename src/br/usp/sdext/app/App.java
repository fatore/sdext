package br.usp.sdext.app;

import br.usp.sdext.parsers.AbstractParser;
import br.usp.sdext.parsers.AccountabilityParser;
import br.usp.sdext.parsers.CandidaturesParser;

public class App {

	public void readData() throws Exception {
		
		AbstractParser parser;
		String baseDir;
		
		parser = new CandidaturesParser();
		baseDir = "data/candidatos/candidaturas/2008";
		parser.parseAndSave(baseDir);
		
		parser = new AccountabilityParser();
		baseDir = "data/prestacao_contas";
//		parser.parseAndSave(baseDir);
	}
	
	public static void main(String[] args) throws Exception {
		
		App app = new App();
		app.readData();
	}
}
