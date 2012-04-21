package br.usp.sdvt.core;

import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import br.usp.sdvt.parsers.BasicParser;
import br.usp.sdvt.util.HibernateUtil;

@Entity
public class Donor {
	
	@Id
	Long id; 
	
	@Column(unique=true)
	private Long cpf; // or cnpj
	private String name;
	private String uf;
	
	public Donor(String name, Long cpf, String uf) {
		this.name = name;
		this.cpf = cpf;
		this.uf = uf;
	}
	
	public Donor() {}

	// getters
	public Long getID() {return id;}
	public String getName() {return name;}
	public Long getCpf() {return cpf;}
	public String getUf() {return uf;}

	// setters 
	public void setID(Long id) {this.id = id;}
	public void setName(String name) {this.name = name;}
	public void setCpf(Long cpf) {this.cpf = cpf;}
	public void setUf(String uf) {this.uf = uf;}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
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
		Donor other = (Donor) obj;
		if (cpf == null && other.cpf == null) {
			if (name != null && other.name != null) {
				if (!name.equals(other.name)) {
					return false;
				}
			}
		}
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;
		return true;
	}

	public String toString() {
		return name + ", " + uf + ", " + cpf;
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
			System.out.println("Donor already exists in the database.");
			throw e;
		}

		return id;
	}
	
	public static Donor parse(String line, boolean old) {
		// break line where finds ";"
		String pieces[] = line.split("\";\"");

		// remove double quotes
		for (int i = 0; i < pieces.length; i++) {
			pieces[i] = pieces[i].replace("\"", "");
		}
		Long donorCPF = null;
		String donorUF = null;
		String donorName = null;
		if (old) {
			donorCPF = BasicParser.parseLong(pieces[6]);
			donorUF = BasicParser.parseStr(pieces[7]);
			donorName = BasicParser.parseStr(pieces[8]);
		} else {
			donorCPF = BasicParser.parseLong(pieces[10]);
			donorUF = BasicParser.parseStr(pieces[1]);
			donorName = BasicParser.parseStr(pieces[11]);
		}
		
		return new Donor(donorName, donorCPF, donorUF);
	}
	
	public static void main(String[] args) {
		Donor d1 = new Donor();
		d1.setCpf(null);
		d1.setName("tes   te");
		
		Donor d2 = new Donor();
		d2.setCpf(null);
		d2.setName("teste");
		
		HashMap<Donor, Donor> donors = new HashMap<Donor, Donor>();
		
		donors.put(d1, d1);
		donors.put(d2, d2);
		
		System.out.println("OLA V. COSLOVSKY".compareTo("SAULO V. COSLOVSKI"));
		
		System.out.println(donors.size());
	}
}
