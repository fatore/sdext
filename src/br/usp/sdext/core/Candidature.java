package br.usp.sdext.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import br.usp.sdext.ghosts.GhostCandidate;
import br.usp.sdext.parsers.BasicParser;
import br.usp.sdext.util.HibernateUtil;

@Entity
public class Candidature implements Serializable {

	@Id
	@ManyToOne
	private Candidate candidate;
	
	@Id
	@ManyToOne
	private Election election;
	
	private String ballotName;
	private Integer ballotNo;
	private Long currentJobID;
	private String currentJob;
	private Integer age;
	private Long schoolingID;
	private String schooling;
	private Long situationID;
	private String situation;
	private Long maritalID;
	private String marital;
	private Float maxExpenses;
	private Integer partyNo;
	private String partyAcronym;
	private String partyName;
	private Long tseID;
	
	@OneToMany
	private List<Income> incomes = new ArrayList<Income>(); 
	
	// private Estate[] candidateEstate;
	// private Expenses[] campaignIcome
	
	public Candidature(String ballotName, Integer ballotNo, Long currentJobID,
			String currentJob, Integer age, Long schoolingID, String schooling,
			Long situationID, String situation, Long maritalID, String marital,
			Float maxExpenses, Integer partyNo, String partyAcronym, String partyName, Long tseID) {
		this.ballotName = ballotName;
		this.ballotNo = ballotNo;
		this.currentJobID = currentJobID;
		this.currentJob = currentJob;
		this.age = age;
		this.schoolingID = schoolingID;
		this.schooling = schooling;
		this.situationID = situationID;
		this.situation = situation;
		this.maritalID = maritalID;
		this.marital = marital;
		this.maxExpenses = maxExpenses;
		this.partyNo = partyNo;
		this.partyAcronym = partyAcronym;
		this.partyName = partyName;
		this.tseID = tseID;
	}	
	
	public Candidature() {}
	
	// getters
	public Election getElection() {return election;}
	public String getBallotName() {return ballotName;}
	public Integer getBallotNo() {return ballotNo;}
	public Long getCurrentJobID() {return currentJobID;}
	public String getCurrentJob() {return currentJob;}
	public Integer getAge() {return age;}
	public Long getSchoolingID() {return schoolingID;}
	public String getSchooling() {return schooling;	}
	public Long getSituationID() {return situationID;}
	public String getSituation() {return situation;}
	public Long getMaritalID() {return maritalID;}
	public String getMarital() {return marital;}
	public Float getMaxExpenses() {return maxExpenses;}
	public Integer getPartyNo() {return partyNo;}
	public String getPartyAcronym() {return partyAcronym;}
	public String getPartyName() {return partyName;}
	public Candidate getCandidate() {return candidate;}
	public Long getTseID() {return tseID;}
	public List<Income> getIncomes() {return incomes;}

	// setters
	public void setElection(Election election) {this.election = election;}
	public void setBallotName(String ballotName) {this.ballotName = ballotName;}
	public void setBallotNo(Integer ballotNo) {this.ballotNo = ballotNo;}
	public void setCurrentJobID(Long currentJobID) {this.currentJobID = currentJobID;}
	public void setCurrentJob(String currentJob) {this.currentJob = currentJob;}
	public void setAge(Integer age) {this.age = age;}
	public void setSchoolingID(Long schoolingID) {this.schoolingID = schoolingID;}
	public void setSchooling(String schooling) {this.schooling = schooling;}
	public void setSituationID(Long situationID) {this.situationID = situationID;}
	public void setSituation(String situation) {this.situation = situation;}
	public void setMaritalID(Long maritalID) {this.maritalID = maritalID;}
	public void setMarital(String marital) {this.marital = marital;}
	public void setMaxExpenses(Float maxExpenses) {this.maxExpenses = maxExpenses;}
	public void setPartyNo(Integer partyNo) {this.partyNo = partyNo;}
	public void setPartyAcronym(String partyAcronym) {this.partyAcronym = partyAcronym;}
	public void setPartyName(String partyName) {this.partyName = partyName;}
	public void setCandidate(Candidate candidate) {this.candidate = candidate;}
	public void setTseID(Long tseID) {this.tseID = tseID;}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((candidate == null) ? 0 : candidate.getID().hashCode());
		result = prime * result	+ ((election == null) ? 0 : election.getID().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Candidature other = (Candidature) obj;
		if (candidate == null) {
			if (other.candidate != null)
				return false;
		} else if (!candidate.getID().equals(other.candidate.getID()))
			return false;
		if (election == null) {
			if (other.election != null)
				return false;
		} else if (!election.getID().equals(other.election.getID()))
			return false;
		return true;
	}

	public void save() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		try {
			session.save(this);
			session.getTransaction().commit();		
		} catch (ConstraintViolationException e) { 
			session.getTransaction().rollback();
			throw e;
		}
	}
	
	public static Candidature findByPK(Candidature id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		Candidature candidature = (Candidature) session.get(Candidature.class, id);
		
		session.getTransaction().commit();
		
		return candidature;
	}
	
	public static List<Candidature> findAll() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		List<Candidature> candidatures = (List<Candidature>) session.createCriteria(Candidature.class).list();
		
		session.getTransaction().commit();
		
		return candidatures;
	}
	
	public static Candidature findByBasic(String candidateName, Integer ballotNo, Integer year, String post) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		Candidature candidature;
		
		String sql = "select CR from Candidature as CR where CR.candidate.name = :name " +
				"and CR.ballotNo = :ballotNo and CR.election.year = :year " +
				"and CR.election.round = 1 and CR.election.post = :post";
		
		candidature =  (Candidature) session.createQuery(sql).
				setParameter("name", candidateName).
				setParameter("ballotNo", ballotNo).
				setParameter("year", year).
				setParameter("post", post).
				uniqueResult();
		
		session.getTransaction().commit();
		
		return candidature;
	}

	public static Candidature parse(String line) {
		
		// break line where finds ";"
		String pieces[] = line.split("\";\"");

		Integer ballotNo = BasicParser.parseInt(pieces[12]);
		String ballotName = BasicParser.parseStr(pieces[13]);
		Long situationID = BasicParser.parseLong(pieces[14]);
		String situation = BasicParser.parseStr(pieces[15]);
		Integer partyNo  = BasicParser.parseInt(pieces[16]);
		String partyAcronym = BasicParser.parseStr(pieces[17]);
		String partyName = BasicParser.parseStr(pieces[18]);
		Long currentJobID = BasicParser.parseLong(pieces[23]);
		String currentJob = BasicParser.parseStr(pieces[24]);
		Integer age = BasicParser.parseInt(pieces[27]);
		Long schoolingID = BasicParser.parseLong(pieces[30]);
		String schooling = BasicParser.parseStr(pieces[31]);
		Long maritalID = BasicParser.parseLong(pieces[32]);
		String marital = BasicParser.parseStr(pieces[33]);
		Float maxExpenses = BasicParser.parseFloat(pieces[39]);
		Long tseID = BasicParser.parseLong(pieces[11]);
		
		return new Candidature(ballotName, ballotNo, currentJobID, currentJob, 
				age, schoolingID, schooling, situationID, situation, maritalID, 
				marital, maxExpenses, partyNo, partyAcronym, partyName, tseID);
	}
	
	public static void addIncome(Income income, String line, Integer year, boolean old) {
		// break line where finds ";"
		String pieces[] = line.split("\";\"");

		// remove double quotes
		for (int i = 0; i < pieces.length; i++) {
			pieces[i] = pieces[i].replace("\"", "");
		}
		String candidateUF = null;
		String post = null;
		String candidateName = null;
		Integer ballotNo = null;
		if (old) {
			candidateUF = BasicParser.parseStr(pieces[0]);
			post = BasicParser.parseStr(pieces[2]);
			candidateName = BasicParser.parseStr(pieces[3]);
			ballotNo = BasicParser.parseInt(pieces[4]);
		} else {
			post = BasicParser.parseStr(pieces[4]);
			candidateName = BasicParser.parseStr(pieces[5]);
			ballotNo = BasicParser.parseInt(pieces[3]);
		}
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		List<Candidature> candidatures;
		
		String sql = "select CR from Candidature as CR where CR.candidate.name = :name " +
				"and CR.ballotNo = :ballotNo and CR.election.year = :year " +
				"and CR.election.round = 1 and CR.election.post = :post";
		
		candidatures =  (List<Candidature>) session.createQuery(sql).
				setParameter("name", candidateName).
				setParameter("ballotNo", ballotNo).
				setParameter("year", year).
				setParameter("post", post).
				list();
		
		
		if (candidatures.size() == 1) {
			candidatures.get(0).getIncomes().add(income);
		} else {
			session.save(new GhostCandidate(candidateName, ballotNo, income));
		}
		if (candidatures.size() > 1){
			System.err.println("maior que 1");
		}
		
		session.getTransaction().commit();
		
	}
	
	public static void main(String[] args) {
		
		Candidate candidate = new Candidate();
		candidate.setName("ANTONIO NERES GOUVEIA");
		candidate.setBirthDate(BasicParser.parseDate("22/03/1952"));
		candidate.setUF("SP");
		candidate.setVoterID(new Long(1));
		
		candidate.save();
		
		Candidate candidate2 = new Candidate();
		candidate2.setName("ANTONIO NERES GOUVEIA");
		candidate2.setBirthDate(BasicParser.parseDate("22/03/1952"));
		candidate2.setUF("SP");
		candidate2.setVoterID(new Long(1));
		
		candidate2.save();
		
		System.out.println();
	}
}












