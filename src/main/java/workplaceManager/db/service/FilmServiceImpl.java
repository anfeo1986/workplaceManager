package workplaceManager.db.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.Film;

import java.util.List;

//@Repository
//public class FilmServiceImpl implements FilmService {
public class FilmServiceImpl extends EntityManager<Film> {
    /*private SessionFactory sessionFactory;
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }*/

    /*@Transactional
    public List<Film> allFilms() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Film").list();
    }

    /*@Transactional
    public void add(Film film) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(film);
    }*/

    /*@Transactional
    public void delete(Film film) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(film);
    }*/

    /*@Transactional
    public void edit(Film film) {
        Session session = sessionFactory.getCurrentSession();
        session.update(film);
    }*/

    /*@Transactional
    public Film getById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Film.class, id);
    }*/
}
