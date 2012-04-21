package br.usp.sdvt.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import br.usp.sdvt.parsers.BasicParser;
import br.usp.sdvt.util.HibernateUtil;


@Entity
public class Candidate implements Comparable<Candidate>, Serializable {
	
	@Id
	private Long id;
	
	private Long voterID;
	
	@Column(nullable=false) 
	private String name;
	
	private Date birthDate;
	private String uf;
	
	// codes
	private Long sexID;
	private Long citizenshipID;
	private Long birthTownID;
	
	// labels
	private String sex;
	private String citizenship;
	private String birthTown;
	
	public Candidate() {}

	public Candidate(Long voterID, String name, Date birthDate,
			String uf, Long sexID, Long citizenshipID, Long birthTownID,
			String sex, String citizenship, String birthTown) {
		this.voterID = voterID;
		this.name = name;
		this.birthDate = birthDate;
		this.uf = uf;
		this.sexID = sexID;
		this.citizenshipID = citizenshipID;
		this.birthTownID = birthTownID;
		this.sex = sex;
		this.citizenship = citizenship;
		this.birthTown = birthTown;
	}

	// getters
	public Long getID() {return id;}
	public Long getVoterID() {return voterID;}
	public String getName() {return name;}
	public Date getBirthDate() {return birthDate;}
	public String getUF() {return uf;}	
	public Long getSexID() {return sexID;}
	public String getSex() {return sex;}
	public Long getCitizenshipID() {return citizenshipID;}
	public String getCitizenship() {return citizenship;}
	public Long getBirthTownID() {return birthTownID;}
	public String getBirthTown() {return birthTown;}

	// setters
	public void setID(Long id) {this.id = id;}
	public void setVoterID(Long voterID) {this.voterID = voterID;}
	public void setName(String name) {this.name = name;}
	public void setBirthDate(Date birthDate) {this.birthDate = birthDate;}
	public void setUF(String birthState) {this.uf = birthState;}
	public void setSexID(Long sexID) {this.sexID = sexID;}
	public void setSex(String sex) {this.sex = sex;}
	public void setCitizenshipID(Long citizenshipID) {this.citizenshipID = citizenshipID;}
	public void setCitizenship(String citizenship) {this.citizenship = citizenship;}
	public void setBirthTownID(Long birthTownID) {this.birthTownID = birthTownID;}
	public void setBirthTown(String birthTown) {this.birthTown = birthTown;}
	
	// sqls
	public Long save() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		Long id = null;
		try {
			id = (Long) session.save(this);
			session.getTransaction().commit();		
		} catch (ConstraintViolationException e) { 
			session.getTransaction().rollback();
			throw e;
		}
		
		return id;
	}
	
	public void delete() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		try {
			session.delete(this);
			session.getTransaction().commit();		
		} catch (ConstraintViolationException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}
	
	public void update(Candidate newer) {
		
		this.merge(newer);
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		try {
			session.update(this);
			session.getTransaction().commit();		
		} catch (ConstraintViolationException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}
	
	public static Candidate findByID(Long id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		Candidate candidate = (Candidate) session.get(Candidate.class, id);
		
		session.getTransaction().commit();
		
		return candidate;
	}
	
	public static List<Candidate> findAll() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		List<Candidate> candidates = (List<Candidate>) session.createCriteria(Candidate.class).list();
		
		session.getTransaction().commit();
		
		return candidates;
	}
	
	public static Candidate findByBasic(String name, Date birthDate, String birthState) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		Candidate candidate = null;
		
		candidate =  (Candidate) session.createQuery(
				"select c from Candidate c where c.name = :name and c.birthDate = :birthDate " +
				"and c.birthState = :birthState").
				setParameter("name", name).
				setParameter("birthDate", birthDate).
				setParameter("birthState", birthState).
				uniqueResult();
		
		session.getTransaction().commit();
		
		return candidate;
	}

	@Override
	public String toString() {
		return name + ", birthDate=" + birthDate + ", voterId=" + voterID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((voterID == null) ? 0 : voterID.hashCode());
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
		Candidate other = (Candidate) obj;
		if (voterID != null && other.voterID != null) {
			if (!voterID.equals(other.voterID)) {
				return false;
			}
		}
		if (name != null && other.name != null) {
			if (!name.equals(other.name)) {
				return false;
			}
		} 
		if (birthDate != null && other.birthDate != null) {
			if (!birthDate.equals(other.birthDate)) {
				return false;
			}
		} 
		if (uf != null && other.uf != null) { 
			if (!uf.equals(other.uf)) {
				return false;
			}
		}
		return true;
	}

	public int compareTo(Candidate candidate) {
		if (name.compareToIgnoreCase(candidate.name) == 0) {
			if (birthDate.compareTo(candidate.getBirthDate()) == 0) {
				if (uf.compareToIgnoreCase(candidate.uf) == 0) {
					return 0;
				} else {
					return uf.compareToIgnoreCase(candidate.uf);
				}
			} else {
				return birthDate.compareTo(candidate.getBirthDate());
			}
		} else {
			return name.compareToIgnoreCase(candidate.name);
		}
	}
	
	public int validate() {
		int count = 0;
		
		count += (voterID != null && !voterID.equals(-1) && !voterID.equals(0)) ? 100 : 0;
		count += (birthDate != null) ? 5 : 0;
		count += (uf != null) ? 3 : 0;
		count += (sexID != null && !sexID.equals(-1) && !sexID.equals(0)) ? 1 : 0;
		count += (sex != null) ? 1 : 0;
		count += (citizenshipID != null && !citizenshipID.equals(-1) && !citizenshipID.equals(0)) ? 1 : 0;
		count += (citizenship != null) ? 1 : 0;
		count += (birthTownID != null && !birthTownID.equals(-1) && !birthTownID.equals(0)) ? 1 : 0;
		count += (birthTown != null) ? 3 : 0;
		
		count *= (name != null) ? 1 : 0;
		return count;
	}
	
	public void merge(Candidate candidate) {
		this.voterID = (candidate.voterID != null) ? candidate.voterID : voterID;
		this.name = (candidate.name != null) ? candidate.name : name;
		this.birthDate = (candidate.birthDate != null) ? candidate.birthDate : birthDate;
		this.uf = (candidate.uf != null) ? candidate.uf : uf;
		this.sexID = (candidate.sexID != null) ? candidate.sexID : sexID;
		this.sex = (candidate.sex != null) ? candidate.sex : sex;
		this.citizenshipID = (candidate.citizenshipID != null) ? candidate.citizenshipID : citizenshipID;
		this.citizenship = (candidate.citizenship != null) ? candidate.citizenship : citizenship;
		this.birthTownID = (candidate.birthTownID != null) ? candidate.birthTownID : birthTownID;
		this.birthTown = (candidate.birthTown != null) ? candidate.birthTown : birthTown;
	}
	
	public static Candidate parse(String line) {
		
		// break line where finds ";"
		String pieces[] = line.split("\";\"");

		// remove double quotes
		for (int i = 0; i < pieces.length; i++) {
			pieces[i] = pieces[i].replace("\"", "");
		}

		// retrieve general data
		String name = BasicParser.parseStr(pieces[10]);
		Date birthDate = BasicParser.parseDate(pieces[25]);
		String uf = BasicParser.parseStr(pieces[36]);

		// retrieve codes
		Long voterID = BasicParser.parseLong(pieces[26]);
		Long sexID = BasicParser.parseLong(pieces[28]);
		Long citizenshipID = BasicParser.parseLong(pieces[34]);
		Long birthTownID = BasicParser.parseLong(pieces[37]);

		// retrieve labels						
		String sex = BasicParser.parseStr(pieces[29]);
		String citizenship = BasicParser.parseStr(pieces[35]);
		String birthTown = BasicParser.parseStr(pieces[38]);
		
		if (name == null) {
			System.err.println("ERROR: EMPTY OR INVALID CANDIDATE NAME!");
			name = BasicParser.parseStr(pieces[10]);
			return null;
		}
		
		return new Candidate(voterID, name, birthDate, uf, sexID, 
				citizenshipID, birthTownID, sex, citizenship, birthTown);
	}
	
	public static void main(String[] args) {
		
		Candidate candidate = new Candidate();
		candidate.setName("ANTONIO NERES GOUVEIA");
		candidate.setBirthDate(BasicParser.parseDate("22/03/1952"));
		candidate.setUF("SP");
		candidate.setVoterID(1L);
		
		Map<Candidate, Candidate> candidates = new HashMap<Candidate, Candidate>();
		candidates.put(candidate, candidate);
		
		Candidature c = new Candidature();
		c.setCandidate(candidate);
		
		Candidate candidate2 = new Candidate();
		candidate2.setName("ANTONIO NERES GOUVEIA");
		candidate2.setBirthDate(BasicParser.parseDate("22/03/1952"));
		candidate2.setUF("SP");
		candidate2.setVoterID(1L);
		candidate2.setBirthTown("Salvador");
		
		Candidate stored = candidates.get(candidate2);
		candidates.put(candidate2, candidate2);
		
		System.out.println(c.getCandidate().toString());
		
	}
}
