package workplaceManager.db.service;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.Accounting1C;
import workplaceManager.db.domain.Equipment;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

@Repository
public class Accounting1CManager extends EntityManager<Accounting1C> {

    @Transactional
    public List<Accounting1C> getAccounting1cList() {
        Session session = sessionFactory.getCurrentSession();
        List<Accounting1C> accounting1CListFromDb = session.createQuery("from Accounting1C as ac " +
                "where ac.deleted=false order by ac.inventoryNumber").list();

        //List<Accounting1C> accounting1CList = getSortedListAccounting1C(accounting1CListFromDb);
        //accounting1CList.stream().forEach(accounting1C -> initializeAccounting1c(accounting1C));
        accounting1CListFromDb.stream().forEach(accounting1C -> initializeAccounting1c(accounting1C));

        //return accounting1CList;
        return accounting1CListFromDb;
    }

    private List<Accounting1C> getSortedListAccounting1C(List<Accounting1C> accounting1CListNoSort) {
        SortedMap<String, Accounting1C> sortedMap = new TreeMap<String, Accounting1C>();
        List<Accounting1C> accounting1CListOther = new ArrayList<>();
        accounting1CListNoSort.stream().forEach(accounting1C -> {
            if (accounting1C.getInventoryNumber() != null && !accounting1C.getInventoryNumber().isEmpty() && !sortedMap.containsKey(accounting1C.getInventoryNumber())) {
                sortedMap.put(accounting1C.getInventoryNumber(), accounting1C);
            } else {
                accounting1CListOther.add(accounting1C);
            }
        });

        List<Accounting1C> accounting1CList = new ArrayList<>();
        sortedMap.values().forEach(accounting1C -> accounting1CList.add(accounting1C));
        accounting1CListOther.forEach(accounting1C -> accounting1CList.add(accounting1C));

        return accounting1CList;
    }

    @Transactional
    public Accounting1C getAccounting1CById(Long id, boolean isReadAll) {
        Session session = sessionFactory.getCurrentSession();
        String queryStr = "from Accounting1C as ac where ";
        if(!isReadAll) {
            queryStr+="ac.deleted=false and ";
        }
        queryStr+= "ac.id=" + id;
        Query query = session.createQuery(queryStr);
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
        accounting1C.setDeleted(true);
        super.save(accounting1C);
    }

}
