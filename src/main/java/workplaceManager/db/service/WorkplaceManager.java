package workplaceManager.db.service;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.Employee;
import workplaceManager.db.domain.Workplace;

import java.util.List;

@Repository
public class WorkplaceManager extends EntityManager<Workplace> {

    @Transactional
    public List<Workplace> getWorkplaceList() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Workplace").list();
    }

    @Transactional
    public Workplace getWorkplaceById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        //return session.get(Workplace.class, id);
        Query query = session.createQuery("from Workplace as workplace where workplace.id=" + id);
        Workplace workplace = (Workplace) query.uniqueResult();
        return workplace;
    }
}
