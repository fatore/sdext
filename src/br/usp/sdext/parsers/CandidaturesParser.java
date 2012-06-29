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

import br.usp.sdext.core.Model;
import br.usp.sdext.models.Candidate;
import br.usp.sdext.models.Candidature;
import br.usp.sdext.models.Coalition;
import br.usp.sdext.models.Election;
import br.usp.sdext.models.Party;
import br.usp.sdext.models.Status;

public class CandidaturesParser extends AbstractParser { 
	
	private int year;
	
	private Map<Model, Model> candidates;
	private ArrayList<Model> duppers;
	private Set<Model> candidatesStatus;
	private Long numCandidates;
	private Map<Model, Model> elections;
	private Map<Model, Model> coalitions;
	private Map<Model, Model> parties;
	private Set<Model> candidatures;
	
	public CandidaturesParser() {
		
		candidates = new HashMap<>();
		duppers = new ArrayList<>();
		candidatesStatus = new HashSet<>();
		numCandidates = 0L;
		elections = new HashMap<>();
		parties = new HashMap<>();
		coalitions = new HashMap<>();
		candidatures = new HashSet<>();
	}
	
	protected void loadFile(File file) throws Exception {
		
		year = Integer.parseInt(file.getParentFile().getName());
		
		if (file.getName().matches("([^\\s]+(\\.(?i)txt))")) {
			
			System.out.println("Parsing file " + file.getName());
			parseFile(file);
		}
	}
	
	private void parseFile(File file) throws Exception {
		
		BufferedReader in = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), "ISO-8859-1"));
		String line = null;
		
		while ((line = in.readLine()) != null) {
			
			// Break line where finds ";"
			String pieces[] = line.split("\";\"");
			
			// remove double quotes
			for (int i = 0; i < pieces.length; i++) {
				pieces[i] = pieces[i].replace("\"", "");
			}
			
			///////////////////////////////////////////
			// PARSING CANDIDATE  /////////////////////
			//////////////////////////////////////////
			
			// Parse data.
			Candidate candidate = Candidate.parse(pieces);
			
			// Ignore invalid candidates.
			if (candidate == null) continue;
			
			// Look for the candidate in map ...
			Candidate mapCandidate = (Candidate) candidates.get(candidate);
			
			// ... if didn't find anything.
			if (mapCandidate == null) {
				
				// Set the ID for the new Candidate ...
				candidate.setID(numCandidates++);
				
				// ... and put it in the map.
				candidates.put(candidate, candidate);
				
			}
			// ... if found something in the map.
			else {
				System.err.println("jah existe! dah uma debugada");
				// Take a look if the objects are similar ...
				if (mapCandidate.similar(candidate)) {
					
					// Verify if the new candidate has more info details.
					if (candidate.validate() > mapCandidate.validate()) {
						
						// Set the new information to the mapped candidate
						mapCandidate.merge(candidate);
					} 
					candidate = mapCandidate;
				} 
				// Objects aren't similar! 
				// VoterID is the same but not other attributes. !?
				else {
					
					System.err.println("Objects aren't similar!" +
							"VoterID is the same but not other attributes. !?");
					// Set both candidates as "duppers".
					mapCandidate.setDupper(true);
					candidate.setDupper(true);
					
					// Set the ID for the new Candidate ...
					candidate.setID(numCandidates++);
					
					// ... and add the new Candidate to a duppers list
					duppers.add(candidate);
				}
			}
			
			///////////////////////////////////////////
			// PARSING CANDIDATE STATUS  /////////////
			//////////////////////////////////////////
			// Parse Status.
			Status status = Status.parse(pieces, year);
			
			// Bind objects.
			status.setCandidate(candidate);
			
			// Add status if does not exists already
			if (!candidatesStatus.contains(status)) {
				candidatesStatus.add(status);
			} else {
				System.err.println("jah existe! dah uma debugada");
			}

			///////////////////////////////////////////
			// PARSING ELECTIONS  ////////////////////
			//////////////////////////////////////////
			
			// Parse data.
			Election election = Election.parse(pieces);
			
			// Look if Election exists in map
			Election mapElection = (Election) elections.get(election);
			
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
			// PARSING PARTIES  ////////////////////
			//////////////////////////////////////////
			
			// Parse data.
			Party party = Party.parse(pieces);
			
			// Look if Election exists in map
			Party mapParty = (Party) parties.get(party);
			
			// If didn't find anything ..
			if (mapParty == null) {
				
				// Set the ID for the new Election ...
				party.setID(new Long(parties.size()));
				
				// ... and put it in the map.
				parties.put(party, party);
				
			} 
			// If found ... 
			else {
				// ...  set as the mapped object.
				party = mapParty;
			}
			///////////////////////////////////////////
			// PARSING COALITIONS  /////////////////
			//////////////////////////////////////////

			// Parse data.
			Coalition coalition = Coalition.parse(pieces);

			// Look if exists in map
			Coalition mapCoalition = (Coalition) coalitions.get(coalition);

			// If didn't find anything ..
			if (mapCoalition == null) {

				// Set the ID for the new Election ...
				coalition.setID(new Long(coalitions.size()));

				// ... and put it in the map.
				coalitions.put(coalition, coalition);

			} 
			// If found ... 
			else {
				// ...  set as the mapped object.
				coalition = mapCoalition;
			}
			///////////////////////////////////////////
			// PARSING CANDIDATURES  /////////////////
			//////////////////////////////////////////
			
			// Parse candidature.
			Candidature candidature = Candidature.parse(pieces);
			
			// Bind objects.
			candidature.setCandidate(candidate);
			candidature.setElection(election);
			candidature.setParty(party);
			candidature.setCoalition(coalition);
			
			// Add candidature if does not exists already
			if (!candidatures.contains(candidature)) {
				candidatures.add(candidature);
			} else {
				System.err.println("jah existe! dah uma debugada");
			}
		}
		
		if (in != null) {
			in.close();
		}
	}
	
	protected void save() {
		
		long start = System.currentTimeMillis();   
		
		System.out.println("\nTotal objects loaded");
		System.out.println("\tCandidates: " + candidates.size());
		System.out.println("\tDuplicate Candidates: " + duppers.size());
		System.out.println("\tElections: " + elections.size());
		System.out.println("\tParties: " + parties.size());
		System.out.println("\tCoalitions: " + coalitions.size());
		System.out.println("\tCandidatures: " + candidatures.size());
		
		System.out.println("\nSaving objects in the database, " +
				"this can take several minutes.");
		
		System.out.println("\tSaving candidates...");
		Model.bulkSave(candidates.values());
		
		System.out.println("\tSaving duppers...");
		Model.bulkSave(duppers);
		
		System.out.println("\tSaving candidates status...");
		Model.bulkSave(candidatesStatus);
		
		System.out.println("\tSaving elections...");
		Model.bulkSave(elections.values());
		
		System.out.println("\tSaving parties...");
		Model.bulkSave(parties.values());
		
		System.out.println("\tSaving coalitions...");
		Model.bulkSave(coalitions.values());
		
		System.out.println("\tSaving candidatures...");
		Model.bulkSave(candidatures);
		
		long elapsedTime = System.currentTimeMillis() - start;
		
		System.out.printf("Finished saving after %d mins and %d secs\n",
			+  (int) (elapsedTime / 60000),(int) (elapsedTime % 60000) / 1000);
	}
}