package br.usp.sdext.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.usp.sdext.core.Candidate;
import br.usp.sdext.core.Candidature;
import br.usp.sdext.core.Election;

public class InitialParser extends AbstractParser { 
	
	// Objects Maps
	private Map<Candidate, Candidate> candidates;
	private ArrayList<Candidate> duppers;
	private Long numCandidates;
	
	private Map<Election, Election> elections;
	
	private Set<Candidature> candidatures;
	
	public InitialParser() {
		
		candidates = new HashMap<Candidate, Candidate>();
		duppers = new ArrayList<Candidate>();
		numCandidates = 0L;
		
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
			
			///////////////////////////////////////////
			// PARSING CANDIDATE  /////////////////////
			//////////////////////////////////////////
			
			// Parse data.
			Candidate candidate = Candidate.parse(line);
			
			// Ignore invalid candidates.
			if (candidate == null) continue;
			
			// Look for the candidate in map ...
			Candidate mapCandidate = candidates.get(candidate);
			
			// ... if didn't find anything.
			if (mapCandidate == null) {
				
				// Set a ID for the new Candidate ...
				candidate.setID(numCandidates++);
				
				// ... and put it in the map.
				candidates.put(candidate, candidate);
				
			}
			// ... if found something in the map.
			else {
				// Take a look if the objects are similar ...
				if (mapCandidate.similar(candidate)) {
					
					// Verify if the new candidate has more info details.
					if (candidate.validate() > mapCandidate.validate()) {
						
						// Set the new information to the mapped candidate
						candidates.get(mapCandidate).merge(candidate);
					} 
					// Else the candidate must accept the mapped values
					else {
						
						candidate = mapCandidate;
					}
					
				} 
				// Objects aren't similar! 
				// VoterID is the same but not other attributes. !?
				else {
					
					System.err.println("Objects aren't similar!" +
							"VoterID is the same but not other attributes. !?");
					// Set both candidates as "duppers".
					mapCandidate.setDupper(true);
					candidate.setDupper(true);
					
					// Set a ID for the new Candidate ...
					candidate.setID(numCandidates++);
					
					// ... and add the new Candidate to a duppers list
					duppers.add(candidate);
				}
			}

			///////////////////////////////////////////
			// PARSING ELECTIONS  ////////////////////
			//////////////////////////////////////////
			
			// Parse data.
			Election election = Election.parse(line);
			
			// Look if Election exists in map
			Election mapElection = elections.get(election);
			
			// If didn't find anything ..
			if (mapElection == null) {
				
				// Set the ID for the new Election ...
				election.setID(new Long(elections.size()));
				
				// ... and put it in the map.
				elections.put(election, election);
				
			} 
			// If found ... 
			else {
				// ...  set as the mapped object.
				election = mapElection;
			}
			
			///////////////////////////////////////////
			// PARSING CANDIDATURES  /////////////////
			//////////////////////////////////////////
			
			// Parse candidature.
			Candidature candidature = Candidature.parse(line);
			
			// Bind objects.
			candidature.setCandidate(candidate);
			candidature.setElection(election);
			
			// Add candidature if does not exists already
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
		
		///////////////////////////////////////////
		// SAVING CANDIDATES  ////////////////////
		//////////////////////////////////////////
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
		
		/////////////////////////////////////////
		// SAVING DUPPERS  /////////////////////
		////////////////////////////////////////	
		System.out.println("\tSaving duppers...");
		start = System.currentTimeMillis();
		count = 0;
		System.out.println("\tProgress:");
		for (Candidate candidate : duppers) {
			candidate.save();
			if (count++ % (candidates.size() / 10) == 0) {
				System.out.printf("\t\t%.2f%%\n", (float) ((count / (float) candidates.size()) * 100));
			}
		}
		finish = System.currentTimeMillis();
		System.out.printf("\t\tFinished!(%d mins %d secs)\n",
				(int) ((finish - start) / 60000),(int) ((finish - start) % 60000) / 1000);
		
		///////////////////////////////////////////
		// SAVING ELECTIONS  /////////////////////
		//////////////////////////////////////////
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
		
		
		///////////////////////////////////////////
		// SAVING CANDIDATURES  ///////////////////
		//////////////////////////////////////////
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
}