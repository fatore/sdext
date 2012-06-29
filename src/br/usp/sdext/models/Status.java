package br.usp.sdext.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.Session;

import br.usp.sdext.core.Model;
import br.usp.sdext.util.HibernateUtil;
import br.usp.sdext.util.Misc;

@Entity
public class Status extends Model implements Serializable {

	private static final long serialVersionUID = 2324783032637391486L;

	@Id
	@ManyToOne
	private Candidate candidate;
	
	private Integer year;
	
	private Integer age;
	
	@Column(name="jobID")
	private Long currentJobID;
	@Column(name="job")
	private String currentJob;
	
	@Column(name="schlID")
	private Long schoolingID;
	@Column(name="schl")
	private String schooling;
	
	@Column(name="marID")
	private Long maritalID;
	@Column(name="mar")
	private String marital;
	
	private Long tseID;
	
	// private Estate[] candidateEstate;
	
	public Status() {}
	
	public Status(Integer year, Long currentJobID, String currentJob, Integer age, 
			Long schoolingID, String schooling, Long maritalID, String marital, Long tseID) {
		
		this.year = year;
		this.currentJobID = currentJobID;
		this.currentJob = currentJob;
		this.age = age;
		this.schoolingID = schoolingID;
		this.schooling = schooling;
		this.maritalID = maritalID;
		this.marital = marital;
		this.tseID = tseID;
	}	
	
	// getters
	public Long getCurrentJobID() {return currentJobID;}
	public String getCurrentJob() {return currentJob;}
	public Integer getAge() {return age;}
	public Long getSchoolingID() {return schoolingID;}
	public String getSchooling() {return schooling;	}
	public Long getMaritalID() {return maritalID;}
	public String getMarital() {return marital;}
	public Candidate getCandidate() {return candidate;}
	public Long getTseID() {return tseID;}

	// setters
	public void setCurrentJobID(Long currentJobID) {this.currentJobID = currentJobID;}
	public void setCurrentJob(String currentJob) {this.currentJob = currentJob;}
	public void setAge(Integer age) {this.age = age;}
	public void setSchoolingID(Long schoolingID) {this.schoolingID = schoolingID;}
	public void setSchooling(String schooling) {this.schooling = schooling;}
	public void setMaritalID(Long maritalID) {this.maritalID = maritalID;}
	public void setMarital(String marital) {this.marital = marital;}
	public void setCandidate(Candidate candidate) {this.candidate = candidate;}
	public void setTseID(Long tseID) {this.tseID = tseID;}
	
	@Override
	public int hashCode() {
		
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((candidate == null) ? 0 : candidate.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
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
		Status other = (Status) obj;
		if (candidate == null) {
			if (other.candidate != null)
				return false;
		} else if (!candidate.equals(other.candidate))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

	@Override
	public String toString() {
		
		return year + ", "	+ age + ", " + currentJobID + ", "  
				+ currentJob + ", " + schoolingID + ", " + schooling +
				", " + maritalID + ", " + marital + ", " + tseID ;
	}

	public static Status findByPK(Status id) {
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		Status candidature = (Status) session.get(Status.class, id);
		
		session.getTransaction().commit();
		
		return candidature;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Status> findAll() {
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		List<Status> candidatures = (List<Status>) session.createCriteria(Status.class).list();
		
		session.getTransaction().commit();
		
		return candidatures;
	}

	public static Status parse(String[] pieces, Integer year) {

		Long currentJobID = Misc.parseLong(pieces[23]);
		String currentJob = Misc.parseStr(pieces[24]);
		
		Integer age = Misc.parseInt(pieces[27]);
		
		if (age == null) {
			Date birthDate = Misc.parseDate(pieces[25]);
			age = Misc.getAge(birthDate);
		}
		
		Long schoolingID = Misc.parseLong(pieces[30]);
		String schooling = Misc.parseStr(pieces[31]);
		
		Long maritalID = Misc.parseLong(pieces[32]);
		String marital = Misc.parseStr(pieces[33]);
		
		Long tseID = Misc.parseLong(pieces[11]);
		
		return new Status(year, currentJobID, currentJob, age, schoolingID, schooling, 
				maritalID, marital, tseID);
	}
}








