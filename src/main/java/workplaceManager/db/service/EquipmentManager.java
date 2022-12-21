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
                "where equipment.deleted=false " +
                "order by equipment.manufacturer, equipment.model ").list();

        return equipmentList;
    }

    public String replaceIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return "";
        }
        String ipNew = "";

        int lastIndex = ip.lastIndexOf('.');
        if (lastIndex < 0) {
            return ip;
        }
        ipNew = ip.substring(0, lastIndex + 1);
        if (lastIndex == ip.length()) {
            return ipNew;
        }
        String end = ip.substring(lastIndex + 1);
        if (end.length() == 0) {
            ipNew += "000";
        } else if (end.length() == 1) {
            ipNew += "00" + end;
        } else if (end.length() == 2) {
            ipNew += "0" + end;
        } else {
            ipNew += end;
        }

        return ipNew;
    }

    @Transactional
    public List<Computer> getComputerList() {
        List<Equipment> equipmentAllList = getEquipmentList();

        SortedMap<String, Computer> sortedMap = new TreeMap<String, Computer>();
        List<Computer> computerListOther = new ArrayList<>();
        equipmentAllList.stream().forEach(equipment -> {
            if (equipment instanceof Computer) {
                Computer computer = (Computer) equipment;
                String ip = replaceIp(computer.getIp());
                if (ip != null && !ip.isEmpty() && !sortedMap.containsKey(ip)) {
                    sortedMap.put(ip, computer);
                } else {
                    computerListOther.add(computer);
                }
            }
        });

        List<Computer> computerList = new ArrayList<>();
        sortedMap.values().forEach(computer -> computerList.add(computer));
        computerListOther.forEach(computer -> computerList.add(computer));

        computerList.stream().forEach(computer -> initializeComputer(computer));

        return computerList;
    }

    private List<Equipment> getSortedListEquipment(List<Equipment> equipmentListNoSort) {
        SortedMap<String, Equipment> sortedMap = new TreeMap<String, Equipment>();
        List<Equipment> equipmentListOther = new ArrayList<>();
        equipmentListNoSort.stream().forEach(equipment -> {
            if (equipment.toString() != null && !equipment.toString().isEmpty() && !sortedMap.containsKey(equipment.toString())) {
                sortedMap.put(equipment.toString(), equipment);
            } else {
                equipmentListOther.add(equipment);
            }
        });

        List<Equipment> equipmentList = new ArrayList<>();
        sortedMap.values().forEach(equipment -> equipmentList.add(equipment));
        equipmentListOther.forEach(equipment -> equipmentList.add(equipment));

        return equipmentList;
    }


    @Transactional
    public List<Monitor> getMonitorList() {
        List<Monitor> equipmentList = new ArrayList<>();
        getSortedListEquipment(getEquipmentList()).stream().forEach(equipment -> {
            if (equipment instanceof Monitor) {
                equipmentList.add((Monitor) equipment);
            }
        });
        return equipmentList;
    }

    @Transactional
    public List<Printer> getPrinterList() {
        List<Printer> equipmentList = new ArrayList<>();
        getSortedListEquipment(getEquipmentList()).stream().forEach(equipment -> {
            if (equipment instanceof Printer) {
                equipmentList.add((Printer) equipment);
            }
        });
        return equipmentList;
    }

    @Transactional
    public List<Scanner> getScannerList() {
        List<Scanner> equipmentList = new ArrayList<>();
        getSortedListEquipment(getEquipmentList()).stream().forEach(equipment -> {
            if (equipment instanceof Scanner) {
                equipmentList.add((Scanner) equipment);
            }
        });
        return equipmentList;
    }

    @Transactional
    public List<Mfd> getMfdList() {
        List<Mfd> equipmentList = new ArrayList<>();
        getSortedListEquipment(getEquipmentList()).stream().forEach(equipment -> {
            if (equipment instanceof Mfd) {
                equipmentList.add((Mfd) equipment);
            }
        });
        return equipmentList;
    }

    @Transactional
    public List<Ups> getUpsList() {
        List<Ups> equipmentList = new ArrayList<>();
        getSortedListEquipment(getEquipmentList()).stream().forEach(equipment -> {
            if (equipment instanceof Ups) {
                equipmentList.add((Ups) equipment);
            }
        });
        return equipmentList;
    }

    @Transactional
    public Equipment getEquipmentById(Long id, boolean isReadAll) {
        Session session = sessionFactory.getCurrentSession();
        String queryStr = "from Equipment as equipment where ";
        if (!isReadAll) {
            queryStr += "equipment.deleted=false and ";
        }
        queryStr += "equipment.id=" + id;
        Query query = session.createQuery(queryStr);
        Equipment equipment = (Equipment) query.uniqueResult();

        return equipment;
    }

    @Transactional
    public Computer getComputerById(Long id, boolean isReadAll) {
        Session session = sessionFactory.getCurrentSession();
        String queryStr = "from Equipment as equipment where ";
        if (!isReadAll) {
            queryStr += "equipment.deleted=false and ";
        }
        queryStr += "equipment.id=" + id;
        Query query = session.createQuery(queryStr);
        Computer computer = (Computer) query.uniqueResult();
        initializeComputer(computer);

        return computer;
    }

    @Transactional
    public Equipment getEquipmentByUid(String uid, boolean isReadAll) {
        Session session = sessionFactory.getCurrentSession();
        String queryStr = "from Equipment as equipment where ";
        if (!isReadAll) {
            queryStr += "equipment.deleted=false and ";
        }
        queryStr += "equipment.uid is not null and equipment.uid!='' and equipment.uid='" + uid + "'";
        Query query = session.createQuery(queryStr);
        Equipment equipment = (Equipment) query.uniqueResult();

        return equipment;
    }

    @Transactional
    public Computer getComputerByIp(String ip, boolean isReadAll) {
        Session session = sessionFactory.getCurrentSession();
        String queryStr = "from Equipment as equipment where ";
        if (!isReadAll) {
            queryStr += "equipment.deleted=false and ";
        }
        queryStr += "equipment.ip is not null and equipment.ip!='' and equipment.ip='" + ip + "'";
        Query query = session.createQuery(queryStr);
        Computer computer = (Computer) query.uniqueResult();

        return computer;
    }

    @Override
    public void delete(Equipment equipment) {
        equipment.setWorkplace(null);
        equipment.setAccounting1C(null);
        equipment.setDeleted(true);
        super.save(equipment);
    }

    protected void initializeComputer(Computer computer) {
        if (computer != null) {
            Hibernate.initialize(computer.getProcessorList());
            Hibernate.initialize(computer.getVideoCardList());
            Hibernate.initialize(computer.getHardDriveList());
            Hibernate.initialize(computer.getRamList());
        }
    }
}
