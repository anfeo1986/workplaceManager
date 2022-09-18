package workplaceManager.db.service;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.Accounting1C;

import java.util.List;

@Repository
public class Accounting1CManager extends EntityManager<Accounting1C> {

    @Transactional
    public List<Accounting1C> getAccounting1cList() {
        Session session = sessionFactory.getCurrentSession();
        List<Accounting1C> accounting1CList = session.createQuery("from Accounting1C as ac order by ac.title").list();
        accounting1CList.stream().forEach(accounting1C -> initializeAccounting1c(accounting1C));

        return accounting1CList;
    }

    @Transactional
    public Accounting1C getAccounting1CById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Accounting1C as ac where ac.id=" + id);
        Accounting1C accounting1C = (Accounting1C) query.uniqueResult();
        initializeAccounting1c(accounting1C);

        return accounting1C;
    }

    @Transactional
    public Accounting1C getAccounting1CByInventoryNumberAndTitle(String inventoryNumber, String title) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Accounting1C as ac where" +
                " ac.inventoryNumber='" + inventoryNumber + "' and " +
                "ac.title='" + title + "'");
        Accounting1C accounting1C = (Accounting1C) query.uniqueResult();
        initializeAccounting1c(accounting1C);

        return accounting1C;
    }

    @Transactional
    public Accounting1C getAccounting1CByInventoryNumber(String inventoryNumber) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Accounting1C as ac where" +
                " ac.inventoryNumber='" + inventoryNumber + "'");
        Accounting1C accounting1C = (Accounting1C) query.uniqueResult();
        initializeAccounting1c(accounting1C);

        return accounting1C;
    }

    protected void initializeAccounting1c(Accounting1C accounting1C) {
        if (accounting1C != null) {
            Hibernate.initialize(accounting1C.getEquipmentList());
        }
    }

    @Override
    public void delete(Accounting1C accounting1C) {
        accounting1C.setEmployee(null);
        super.save(accounting1C);
        super.delete(accounting1C);
    }

}
