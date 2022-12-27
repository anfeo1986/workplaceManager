package workplaceManager.db.service;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.Accounting1C;
import workplaceManager.db.domain.Employee;
import workplaceManager.db.domain.Equipment;
import workplaceManager.db.domain.Workplace;
import workplaceManager.sorting.FilterAccounting1C;
import workplaceManager.sorting.SortingAccounting1C;

import java.util.*;

@Repository
public class Accounting1CManager extends EntityManager<Accounting1C> {

    @Transactional
    public List<Accounting1C> getAccounting1CListByEmployee(Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        List<Accounting1C> accounting1CList = session.createQuery("from Accounting1C as ac " +
                "where ac.employee.id=" + employee.getId()).list();

        accounting1CList.stream().forEach(accounting1C -> initializeAccounting1c(accounting1C));

        return accounting1CList;
    }

    @Transactional
    public List<Accounting1C> getAccounting1cList(SortingAccounting1C sortingAccounting1C,
                                                  String findText,
                                                  FilterAccounting1C filter) {
        if (findText == null) {
            findText = "";
        }
        Session session = sessionFactory.getCurrentSession();
        String queryStr = "select ac from Accounting1C as ac " +
                "left join Equipment as eq on eq.accounting1C.id=ac.id " +
                "left join Employee as em on ac.employee.id=em.id " +
                "where ac.deleted=false and " +
                "(lower(ac.inventoryNumber) like lower('%" + findText + "%') or " +
                "lower(ac.title) like lower('%" + findText + "%') or " +
                "lower(eq.uid) like lower('%" + findText + "%') or " +
                "lower(eq.manufacturer) like lower('%" + findText + "%') or " +
                "lower(eq.model) like lower('%" + findText + "%') or " +
                "lower(em.name) like lower('%" + findText + "%')) ";

        if (SortingAccounting1C.INVENTORY_NUMBER.equals(sortingAccounting1C)) {
            queryStr += "order by ac.inventoryNumber asc";
        } else if (SortingAccounting1C.TITLE.equals(sortingAccounting1C)) {
            queryStr += "order by ac.title asc";
        } else if (SortingAccounting1C.EMPLOYEE.equals(sortingAccounting1C)) {
            queryStr += "order by em.name asc ";
        } else {
            queryStr += "order by ac.inventoryNumber asc";
        }

        List<Accounting1C> accounting1CListFromDb = session.createQuery(queryStr).list();
        Set<Long> idList = new HashSet<>();
        List<Accounting1C> accounting1CList = new ArrayList<>();
        for(Accounting1C accounting1C : accounting1CListFromDb) {
            if(!idList.contains(accounting1C.getId())) {
                if (!FilterAccounting1C.ALL.equals(filter)) {
                    if(FilterAccounting1C.EQUIPMENT_HAVE.equals(filter) &&
                            accounting1C.getEquipmentList() != null && !accounting1C.getEquipmentList().isEmpty()) {
                        accounting1CList.add(accounting1C);
                        idList.add(accounting1C.getId());
                    } else if(FilterAccounting1C.EQUIPMENT_NOT_HAVE.equals(filter) &&
                            (accounting1C.getEquipmentList() == null || accounting1C.getEquipmentList().isEmpty())) {
                        accounting1CList.add(accounting1C);
                        idList.add(accounting1C.getId());
                    }
                } else {
                    accounting1CList.add(accounting1C);
                    idList.add(accounting1C.getId());
                }
            }
        }

        //List<Accounting1C> accounting1CList = getSortedListAccounting1C(accounting1CListFromDb);
        //accounting1CList.stream().forEach(accounting1C -> initializeAccounting1c(accounting1C));
        accounting1CList.stream().forEach(accounting1C -> initializeAccounting1c(accounting1C));

        //return accounting1CList;
        return accounting1CList;
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
        if (!isReadAll) {
            queryStr += "ac.deleted=false and ";
        }
        queryStr += "ac.id=" + id;
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
