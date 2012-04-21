package br.usp.sdvt.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.service.spi.Stoppable;

import br.usp.sdvt.core.Candidate;
import br.usp.sdvt.core.Candidature;
import br.usp.sdvt.core.Election;
import br.usp.sdvt.util.FileOperations;

public class InitialParser extends AbstractParser { 
	
	private Map<Candidate, Candidate> candidates;
	private Map<Election, Election> elections;
	private Set<Candidature> candidatures;
	
	public InitialParser() {
		candidates = new HashMap<Candidate, Candidate>();
		elections = new HashMap<Election, Election>();
		candidatures = new HashSet<Candidature>();
	}
	
	protected void loadFile(File file) throws Exception {		
		if (file.getName().matches(".*(?iu)txt")) {
			System.out.println("Parsing file " + file.getName());
			parseFile(file);
		}
	}
	
	private void parseFile(File file) throws Exception {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-1"));
		String line = null;
		
		while ((line = in.readLine()) != null) {
			
			// candidate
			Candidate candidate = Candidate.parse(line);
			// ignore invalid candidate
			if (candidate == null) continue;
			Candidate mapCandidate = candidates.get(candidate);
			if (mapCandidate == null) {
				candidate.setID(new Long(candidates.size()));
				candidates.put(candidate, candidate);
			} else {
				if (candidate.validate() > mapCandidate.validate()) {
					// merge valid fields
					candidates.get(candidate).merge(candidate);
				} 
				candidate = mapCandidate;
			}

			// election
			Election election= Election.parse(line);
			Election mapElection = elections.get(election);
			if (mapElection == null) {
				election.setID(new Long(elections.size()));
				elections.put(election, election);
			} else {
				election = mapElection;
			}
			
			// bind candidate and election
			Candidature candidature = Candidature.parse(line);
			candidature.setCandidate(candidate);
			candidature.setElection(election);
			if (!candidatures.contains(candidature)) {
				candidatures.add(candidature);
			} 
		}
	}
	
	
	protected void save() {
		System.out.println("\nTotal objects loaded");
		System.out.println("\tCandidates: " + candidates.size());
		System.out.println("\tElections: " + elections.size());
		System.out.println("\tCandidatures: " + candidatures.size());
		
		System.out.println("\nSaving objects in the database, this can take several minutes.");
		
		long start, finish, count;
		
		// candidates
		System.out.println("\tSaving candidates...");
		start = System.currentTimeMillis();
		count = 0;
		System.out.println("\tProgress:");
		for (Candidate candidate : candidates.values()) {
			candidate.save();
			if (count++ % (candidates.size() / 10) == 0) {
				System.out.printf("\t\t%.2f%%\n", (float) ((count / (float) candidates.size()) * 100));
			}
		}
		finish = System.currentTimeMillis();
		System.out.printf("\t\tFinished!(%d mins %d secs)\n",
				(int) ((finish - start) / 60000),(int) ((finish - start) % 60000) / 1000);
		
		// elections
		System.out.println("\tSaving elections...");
		start = System.currentTimeMillis();
		count = 0;
		System.out.println("\tProgress:");
		for (Election election : elections.values()) {
			election.save();
			if (count++ % (elections.size() / 10) == 0) {
				System.out.printf("\t\t%.2f%%\n", (float) ((count / (float) elections.size()) * 100));
			}
		}
		finish = System.currentTimeMillis();
		System.out.printf("\t\tFinished!(%d mins %d secs)\n",
				(int) ((finish - start) / 60000),(int) ((finish - start) % 60000) / 1000);
		
		// candidatures
		System.out.println("\tSaving candidatures...");
		start = System.currentTimeMillis();
		count = 0;
		System.out.println("\tProgress:");
		for (Candidature candidature : candidatures) {
			candidature.save();
			if (count++ % (candidatures.size() / 10) == 0) {
				System.out.printf("\t\t%.2f%%\n", (float) ((count / (float) candidatures.size()) * 100));
			}
		}
		finish = System.currentTimeMillis();
		System.out.printf("\t\tFinished!(%d mins %d secs)\n",
				(int) ((finish - start) / 60000),(int) ((finish - start) % 60000) / 1000);
	}
	
	public static void main(String[] args) throws Exception {
		
		AbstractParser parser;
		
		parser = new InitialParser();
		String baseDir = "/work1/wokspace/social-vis/data/eleitorais/candidatos";
		parser.loadAndSave(baseDir);
		
		parser = new AccountabilityParser();
		baseDir = "/work1/wokspace/social-vis/data/eleitorais/prestacao_contas";
		parser.loadAndSave(baseDir);
	}
}