package br.usp.sdext.app;

import br.usp.sdext.parsers.AbstractParser;
import br.usp.sdext.parsers.InitialParser;

public class App {

	public void readData() throws Exception {
		
		AbstractParser parser;
		
		parser = new InitialParser();
		String baseDir = "../data/eleitorais/candidatos/2010";
		parser.loadAndSave(baseDir);
		
//		parser = new AccountabilityParser();
//		baseDir = "/home/fatore/workspace/sdext/data/eleitorais/prestacao_contas";
//		parser.loadAndSave(baseDir);
	}
	
	public static void main(String[] args) throws Exception {
		
		App app = new App();
		app.readData();
	}
}
