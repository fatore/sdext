package br.usp.sdvt.core;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import br.usp.sdvt.parsers.BasicParser;
import br.usp.sdvt.util.HibernateUtil;

@Entity
public class Income {
	
	@Id
	private Long id;
	
	@ManyToOne
	private Donor donor;
	
	private Float value;
	private String type;
	private Date date;
	
	public Income(Float value, String type, Date date) {
		this.value = value;
		this.type = type;
		this.date = date;
	}
	
	public Income() {}
	
	// getters
	public Long getID() {return id;}
	public Donor getDonor() {return donor;	}
	public Float getValue() {return value;}
	public String getType() {return type;}
	public Date getDate() {return date;}
	
	// setters
	public void setID(Long id) {this.id = id;}
	public void setDonor(Donor donor) {this.donor = donor;}
	public void setValue(Float value) {this.value = value;}
	public void setType(String type) {this.type = type;}
	public void setDate(Date date) {this.date = date;}
	
	
	@Override
	public String toString() {
		return donor.getName() + ", " + value + ", " + type;
	}
	
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
	
	public static Income parse(String line, boolean old) {
		// break line where finds ";"
		String pieces[] = line.split("\";\"");

		// remove double quotes
		for (int i = 0; i < pieces.length; i++) {
			pieces[i] = pieces[i].replace("\"", "");
		}
		
		Date date = null;
		Float value = null;
		String type = null;
		if (old){
			date = BasicParser.parseDate(pieces[5]);
			value = BasicParser.parseFloat(pieces[9]);
			try {
				type = BasicParser.parseStr(pieces[10]);
			} catch (Exception e) {}
		} else {
			date = BasicParser.parseDate(pieces[12]);
			value = BasicParser.parseFloat(pieces[13]);
			type = BasicParser.parseStr(pieces[16]);
		}
		
		return new Income(value, type, date);
	}
}


















