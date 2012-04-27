package br.usp.sdext.ghosts;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.usp.sdext.core.Income;

@Entity
@Table(name="ghost_candidate")
public class CandidateGhost {
	
	@Id
	@GeneratedValue
	Long id;
	String name;
	Integer ballotNo;
	@OneToOne
	Income income;

	public CandidateGhost(String name, Integer ballotNo, Income income) {
		this.name = name;
		this.ballotNo = ballotNo;
		this.income = income;
	}
	
	public CandidateGhost() {}
}
