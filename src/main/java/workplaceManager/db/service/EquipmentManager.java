package workplaceManager.db.service;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

@Repository
public class EquipmentManager extends EntityManager<Equipment> {
    @Transactional
    public List<Equipment> getEquipmentList() {
        Session session = sessionFactory.getCurrentSession();
        List<Equipment> equipmentList = session.createQuery("from Equipment as equipment " +
                "order by equipment.manufacturer, equipment.model ").list();

        return equipmentList;
    }

    @Transactional
    public List<Computer> getComputerList() {
        List<Equipment> equipmentAllList = getEquipmentList();

        List<Computer> computerList = new ArrayList<>();
        SortedMap<String, Computer> sortedMap = new TreeMap<String, Computer>();
        List<Computer> computerListOther = new ArrayList<>();
        equipmentAllList.stream().forEach(equipment -> {
            if(equipment instanceof Computer) {
                Computer computer = (Computer) equipment;
                if(computer.getIp() != null && computer.getIp() != "" && !sortedMap.containsKey(computer.getIp())) {
                    sortedMap.put(computer.getIp(), computer);
                } else {
                    computerListOther.add(computer);
                }
                //computerList.add((Computer) equipment);
            }
        });

        sortedMap.values().forEach(computer -> computerList.add(computer));
        computerListOther.forEach(computer -> computerList.add(computer));

        computerList.stream().forEach(computer -> initializeComputer(computer));

        return computerList;
    }

    @Transactional
    public List<Monitor> getMonitorList() {
        List<Equipment> equipmentAllList = getEquipmentList();

        List<Monitor> equipmentList = new ArrayList<>();
        equipmentAllList.stream().forEach(equipment -> {
            if(equipment instanceof Monitor) {
                equipmentList.add((Monitor) equipment);
            }
        });

        return equipmentList;
    }

    @Transactional
    public List<Printer> getPrinterList() {
        List<Equipment> equipmentAllList = getEquipmentList();

        List<Printer> equipmentList = new ArrayList<>();
        equipmentAllList.stream().forEach(equipment -> {
            if(equipment instanceof Printer) {
                equipmentList.add((Printer) equipment);
            }
        });

        return equipmentList;
    }

    @Transactional
    public List<Scanner> getScannerList() {
        List<Equipment> equipmentAllList = getEquipmentList();

        List<Scanner> equipmentList = new ArrayList<>();
        equipmentAllList.stream().forEach(equipment -> {
            if(equipment instanceof Scanner) {
                equipmentList.add((Scanner) equipment);
            }
        });

        return equipmentList;
    }

    @Transactional
    public List<Mfd> getMfdList() {
        List<Equipment> equipmentAllList = getEquipmentList();

        List<Mfd> equipmentList = new ArrayList<>();
        equipmentAllList.stream().forEach(equipment -> {
            if(equipment instanceof Mfd) {
                equipmentList.add((Mfd) equipment);
            }
        });

        return equipmentList;
    }

    @Transactional
    public List<Ups> getUpsList() {
        List<Equipment> equipmentAllList = getEquipmentList();

        List<Ups> equipmentList = new ArrayList<>();
        equipmentAllList.stream().forEach(equipment -> {
            if(equipment instanceof Ups) {
                equipmentList.add((Ups) equipment);
            }
        });

        return equipmentList;
    }

    @Transactional
    public Equipment getEquipmentById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Equipment as equipment where equipment.id=" + id);
        Equipment equipment = (Equipment) query.uniqueResult();

        return equipment;
    }

    @Transactional
    public Computer getComputerById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Equipment as equipment where equipment.id=" + id);
        Computer computer = (Computer) query.uniqueResult();
        initializeComputer(computer);

        return computer;
    }

    @Transactional
    public Equipment getEquipmentByUid(String uid) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Equipment as equipment " +
                "where equipment.uid is not null and equipment.uid!='' and equipment.uid='" + uid + "'");
        Equipment equipment = (Equipment) query.uniqueResult();

        return equipment;
    }

    @Transactional
    public Computer getComputerByIp(String ip) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Equipment as equipment " +
                "where equipment.ip is not null and equipment.ip!='' and equipment.ip='" + ip + "'");
        Computer computer = (Computer) query.uniqueResult();

        return computer;
    }

    @Override
    public void delete(Equipment equipment) {
        equipment.setWorkplace(null);
        equipment.setAccounting1C(null);
        super.save(equipment);
        super.delete(equipment);
    }

    protected void initializeComputer(Computer computer) {
        if(computer != null) {
            Hibernate.initialize(computer.getProcessorList());
            Hibernate.initialize(computer.getVideoCardList());
            Hibernate.initialize(computer.getHardDriveList());
            Hibernate.initialize(computer.getRamList());
        }
    }
}
