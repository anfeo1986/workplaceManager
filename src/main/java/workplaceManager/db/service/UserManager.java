package workplaceManager.db.service;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.Users;

import org.hibernate.Query;

import java.util.List;

@Repository
public class UserManager extends EntityManager<Users> {
    @Transactional
    public List<Users> getUserList() {
        Session session = sessionFactory.getCurrentSession();
        List<Users> userList = session.createQuery("from Users as users order by users.username asc").list();

        return userList;
    }

    @Transactional
    public Users getUserByLogin(String login) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Users as users where users.username='" + login + "'");

        return (Users) query.uniqueResult();
    }

    @Transactional
    public Users getUserById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Users as users where users.id=" + id);

        return (Users) query.uniqueResult();
    }

}
