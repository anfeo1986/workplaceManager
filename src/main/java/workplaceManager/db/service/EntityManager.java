package workplaceManager.db.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

public abstract class EntityManager<T> {
    protected SessionFactory sessionFactory;
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void add(T entity) {
        Session session = sessionFactory.getCurrentSession();
        //session.persist(entity);
        session.saveOrUpdate(entity);
    }

    @Transactional
    public void save(T entity) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(entity);
    }

    @Transactional
    public void delete(T entity) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(entity);
    }

    @Transactional
    public void update(T entity) {
        Session session = sessionFactory.getCurrentSession();
        session.update(entity);
    }
}
