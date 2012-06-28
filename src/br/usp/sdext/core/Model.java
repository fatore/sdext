package br.usp.sdext.core;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import br.usp.sdext.util.HibernateUtil;

public abstract class Model  {

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
	
//	public static Model findByPK(Long id) {
//		
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		
//		Model party = (Model) session.get(Model.class, id);
//		
//		session.getTransaction().commit();
//		
//		return party;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public static List<Model> findAll() {
//		
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		
//		List<Model> parties = (List<Model>) session.createCriteria(Election.class).list();
//		
//		session.getTransaction().commit();
//		
//		return parties;
//	}
}
