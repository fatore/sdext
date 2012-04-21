package br.usp.sdvt.core;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Session;

import br.usp.sdvt.util.HibernateUtil;


@Entity
@Table(name="invalid_candidate")
public class InvalidCandidate {
	
	@Id
	private String name;
	
	public InvalidCandidate() {}
	
	public InvalidCandidate(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return ("Invalid Candidate\n");
	}
	
	public void save() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();

		session.beginTransaction();
		session.save(this);
		session.getTransaction().commit();
	}
	
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
}
