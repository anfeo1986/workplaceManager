package workplaceManager.db.service;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.Users;

import org.hibernate.Query;
import workplaceManager.db.domain.Workplace;

import java.util.List;

@Repository
public class UserManager extends EntityManager<Users> {
    @Transactional
    public List<Users> getUserList() {
        Session session = sessionFactory.getCurrentSession();
        List<Users> userList = session.createQuery("from Users as users " +
                "where users.deleted=false order by users.username asc").list();

        return userList;
    }


    @Transactional
    public Users getUserByLogin(String login) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Users as users " +
                "where users.deleted=false and users.username='" + login + "'");

        return (Users) query.uniqueResult();
    }

    @Transactional
    public Users getUserById(Long id, boolean isReadAll) {
        Session session = sessionFactory.getCurrentSession();
        String queryStr = "from Users as users where ";
        if (!isReadAll) {
            queryStr += "users.deleted=false and ";
        }
        queryStr += "users.id=" + id;
        Query query = session.createQuery(queryStr);

        return (Users) query.uniqueResult();
    }

    @Override
    public void delete(Users user) {
        user.setDeleted(true);
        super.save(user);
    }
}
