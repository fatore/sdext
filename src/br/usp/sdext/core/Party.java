package br.usp.sdext.core;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import br.usp.sdext.util.HibernateUtil;
import br.usp.sdext.util.Misc;

@Entity
public class Party implements Serializable {

	private static final long serialVersionUID = 7101407303061260986L;

	@Id
	private Long id;
	
	private Integer partyNo;
	private String partyAcronym;
	private String partyName;	
	
	public Party(Integer partyNo, String partyAcronym, String partyName) {
		
		this.partyNo = partyNo;
		this.partyAcronym = partyAcronym;
		this.partyName = partyName;
	}
	
	public Long getID() {return id;}
	public Integer getPartyNo() {return partyNo;}
	public String getPartyAcronym() {return partyAcronym;}
	public String getPartyName() {return partyName;}
	
	public void setID(Long id) {this.id = id;}
	public void setPartyNo(Integer partyNo) {this.partyNo = partyNo;}
	public void setPartyAcronym(String partyAcronym) {this.partyAcronym = partyAcronym;}
	public void setPartyName(String partyName) {this.partyName = partyName;}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((partyNo == null) ? 0 : partyNo.hashCode());
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
		Party other = (Party) obj;
		if (partyNo == null) {
			if (other.partyNo != null)
				return false;
		} else if (!partyNo.equals(other.partyNo))
			return false;
		return true;
	}
	
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
	
	public static Party findByPK(Long id) {
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		Party party = (Party) session.get(Party.class, id);
		
		session.getTransaction().commit();
		
		return party;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Party> findAll() {
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		List<Party> parties = (List<Party>) session.createCriteria(Election.class).list();
		
		session.getTransaction().commit();
		
		return parties;
	}

	public static Party parse(String[] pieces) {

		Integer partyNo  = Misc.parseInt(pieces[16]);
		String partyAcronym = Misc.parseStr(pieces[17]);
		String partyName = Misc.parseStr(pieces[18]);
		
		return new Party(partyNo, partyAcronym, partyName);
	}
}
