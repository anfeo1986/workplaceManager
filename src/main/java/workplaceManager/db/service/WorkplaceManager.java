package workplaceManager.db.service;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.Employee;
import workplaceManager.db.domain.Equipment;
import workplaceManager.db.domain.Workplace;

import java.util.ArrayList;
import java.util.List;

@Repository
@Service
public class WorkplaceManager extends EntityManager<Workplace> {

    @Transactional
    public List<Workplace> getWorkplaceList() {
        Session session = sessionFactory.getCurrentSession();

        List<Workplace> workplaceList = session.createQuery("from Workplace as workplace " +
                " where workplace.deleted=false order by workplace.title asc ").list();
        if(workplaceList == null) {
            workplaceList = new ArrayList<>();
        }
        workplaceList.stream().forEach(workplace -> initializeWorkplace(workplace));

        //return session.createQuery("from Workplace as workplace order by workplace.title asc ").list();
        return workplaceList;
    }

    @Transactional
    public Workplace getWorkplaceById(Long id, boolean isReadAll) {
        Session session = sessionFactory.getCurrentSession();
        String queryStr = "from Workplace as workplace where ";
        if(!isReadAll) {
            queryStr+="workplace.deleted=false and ";
        }
        queryStr+="workplace.id=" + id;

        Query query = session.createQuery(queryStr);
        Workplace workplace = (Workplace) query.uniqueResult();
        initializeWorkplace(workplace);
        return workplace;
    }

    @Transactional
    public Workplace getWorkplaceByIdNoDeleted1(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Workplace as workplace " +
                "where workplace.deleted=false and workplace.id=" + id);
        Workplace workplace = (Workplace) query.uniqueResult();
        initializeWorkplace(workplace);
        return workplace;
    }

    @Transactional
    public Workplace getWorkplaceByIdAll(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Workplace as workplace where workplace.id=" + id);
        Workplace workplace = (Workplace) query.uniqueResult();
        initializeWorkplace(workplace);
        return workplace;
    }

    @Transactional
    public Workplace getWorkplaceByTitle(String title) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Workplace as workplace" +
                " where workplace.deleted=false and workplace.title='" + title + "'");
        Workplace workplace = (Workplace) query.uniqueResult();
        initializeWorkplace(workplace);
        return workplace;
    }

    protected void initializeWorkplace(Workplace workplace) {
        if(workplace != null) {
            Hibernate.initialize(workplace.getEmployeeList());
            Hibernate.initialize(workplace.getEquipmentList());
        }
    }
    @Override
    public void delete(Workplace workplace) {

        workplace.setDeleted(true);
        super.save(workplace);
    }

}
