package workplaceManager.db.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.StateObject;
import workplaceManager.TypeEvent;
import workplaceManager.TypeObject;
import workplaceManager.TypeParameter;
import workplaceManager.db.domain.*;
import workplaceManager.db.domain.components.*;

import java.lang.reflect.Type;
import java.util.*;

@Repository
public class JournalManager extends EntityManager<Journal> {
    private EquipmentManager equipmentManager;

    @Autowired
    public void setEquipmentManager(EquipmentManager equipmentManager) {
        this.equipmentManager = equipmentManager;
    }

    private UserManager userManager;

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    private Accounting1CManager accounting1CManager;

    @Autowired
    public void setAccounting1CManager(Accounting1CManager accounting1CManager) {
        this.accounting1CManager = accounting1CManager;
    }

    private WorkplaceManager workplaceManager;

    @Autowired
    public void setWorkplaceManager(WorkplaceManager workplaceManager) {
        this.workplaceManager = workplaceManager;
    }

    private EmployeeManager employeeManager;

    @Autowired
    public void setEmployeeManager(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    @Transactional
    public void saveChangeWorkplace(Workplace workplaceOld, Workplace workplaceNew, Users user) {
        if (workplaceOld == null && workplaceNew == null) {
            return;
        }

        if (!StringUtils.equals(workplaceOld.getTitle(), workplaceNew.getTitle())) {
            save(new Journal(TypeEvent.UPDATE, TypeObject.WORKPLACE, workplaceOld,
                    workplaceOld.getTitle(), workplaceNew.getTitle(),
                    TypeParameter.WORKPLACE_TITLE, user));
        }
    }

    @Transactional
    public void saveChangeAccounting1C(Accounting1C accounting1COld, Accounting1C accounting1CNew, Users user) {
        if (accounting1COld == null && accounting1CNew == null) {
            return;
        }

        if (!StringUtils.equals(accounting1COld.getTitle(), accounting1CNew.getTitle())) {
            save(new Journal(TypeEvent.UPDATE, TypeObject.ACCOUNTING1C, accounting1COld,
                    accounting1COld.getTitle(), accounting1CNew.getTitle(),
                    TypeParameter.ACCOUNTING1C_TITLE, user));
        }
        if (!StringUtils.equals(accounting1COld.getInventoryNumber(), accounting1CNew.getInventoryNumber())) {
            save(new Journal(TypeEvent.UPDATE, TypeObject.ACCOUNTING1C, accounting1COld,
                    accounting1COld.getInventoryNumber(), accounting1CNew.getInventoryNumber(),
                    TypeParameter.ACCOUNTING1C_INVENTORY_NUMBER, user));
        }
        if (!Employee.equalsEmployee(accounting1COld.getEmployee(), accounting1CNew.getEmployee())) {
            save(new Journal(TypeEvent.ACCOUNTING1C_MOVING, TypeObject.ACCOUNTING1C, accounting1COld,
                    accounting1COld.getEmployee() != null ? accounting1COld.getEmployee().toString() : "",
                    accounting1CNew.getEmployee() != null ? accounting1CNew.getEmployee().toString() : "",
                    TypeParameter.ACCOUNTING1C_EMPLOYEE, user));
        }
    }

    @Transactional
    public void saveChangeEmployee(Employee employeeOld, Employee employeeNew, Users user) {
        if (employeeOld == null && employeeNew == null) {
            return;
        }

        if (!StringUtils.equals(employeeOld.getName(), employeeNew.getName())) {
            save(new Journal(TypeEvent.UPDATE, TypeObject.EMPLOYEE, employeeOld,
                    employeeOld.getName(), employeeNew.getName(),
                    TypeParameter.EMPLOYEE_USERNAME, user));
        }
        if (!StringUtils.equals(employeeOld.getPost(), employeeNew.getPost())) {
            save(new Journal(TypeEvent.UPDATE, TypeObject.EMPLOYEE, employeeOld,
                    employeeOld.getPost(), employeeNew.getPost(),
                    TypeParameter.EMPLOYEE_POST, user));
        }
        if (!Workplace.equalsWorkplace(employeeOld.getWorkplace(), employeeNew.getWorkplace())) {
            save(new Journal(TypeEvent.UPDATE, TypeObject.EMPLOYEE, employeeOld,
                    employeeOld.getWorkplace() != null ? employeeOld.getWorkplace().toString() : "",
                    employeeNew.getWorkplace() != null ? employeeNew.getWorkplace().toString() : "",
                    TypeParameter.WORKPLACE, user));
        }

    }

    @Transactional
    public void saveChangeEquipment(Equipment equipmentOld, Equipment equipmentNew,
                                    String typeEquipment, Users user) {
        if (equipmentNew == null || equipmentOld == null) {
            return;
        }
        if (!StringUtils.equals(equipmentNew.getUid(), equipmentOld.getUid())) {
            saveChange(TypeEvent.UPDATE, equipmentOld, TypeParameter.UID,
                    equipmentOld.getUid(), equipmentNew.getUid(), typeEquipment, user);
        }
        if (!StringUtils.equals(equipmentOld.getManufacturer(), equipmentNew.getManufacturer())) {
            saveChange(TypeEvent.UPDATE, equipmentOld, TypeParameter.MANUFACTURER,
                    equipmentOld.getManufacturer(), equipmentNew.getManufacturer(), typeEquipment, user);
        }
        if (!StringUtils.equals(equipmentNew.getModel(), equipmentOld.getModel())) {
            saveChange(TypeEvent.UPDATE, equipmentOld, TypeParameter.MODEL,
                    equipmentOld.getModel(), equipmentNew.getModel(), typeEquipment, user);
        }
        if (!Workplace.equalsWorkplace(equipmentOld.getWorkplace(), equipmentNew.getWorkplace())) {
            saveChange(TypeEvent.UPDATE, equipmentOld, TypeParameter.WORKPLACE,
                    equipmentOld.getWorkplace() != null ? equipmentOld.getWorkplace().toString() : "",
                    equipmentNew.getWorkplace() != null ? equipmentNew.getWorkplace().toString() : "",
                    typeEquipment, user);
        }
        if(!StringUtils.equals(equipmentNew.getComment(), equipmentOld.getComment())) {
            saveChange(TypeEvent.UPDATE, equipmentOld, TypeParameter.COMMENT,
                    equipmentOld.getComment(), equipmentNew.getCommentHtml(), typeEquipment, user);
        }
        if (!Accounting1C.equalsAccounting1C(equipmentOld.getAccounting1C(), equipmentNew.getAccounting1C())) {
            saveChange(TypeEvent.UPDATE, equipmentOld, TypeParameter.ACCOUNTING1C,
                    equipmentOld.getAccounting1C() != null ? equipmentOld.getAccounting1C().toString() : "",
                    equipmentNew.getAccounting1C() != null ? equipmentNew.getAccounting1C().toString() : "",
                    typeEquipment, user);
        }
        if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
            Computer computerOld = (Computer) equipmentOld;
            Computer computerNew = (Computer) equipmentNew;
            if (!StringUtils.equals(computerOld.getIp(), computerNew.getIp())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.IP,
                        computerOld.getIp(), computerNew.getIp(), typeEquipment, user);
            }
            if (!StringUtils.equals(computerOld.getNetName(), computerNew.getNetName())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.NET_NAME,
                        computerOld.getNetName(), computerNew.getNetName(), typeEquipment, user);
            }
            if (!OperationSystem.equalsOS(computerOld.getOperationSystem(), computerNew.getOperationSystem())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.OS,
                        computerOld.getOperationSystem() != null ? computerOld.getOperationSystem().toString() : "",
                        computerNew.getOperationSystem() != null ? computerNew.getOperationSystem().toString() : "",
                        typeEquipment, user);
            }
            if (!MotherBoard.equalsMotherBoard(computerOld.getMotherBoard(), computerNew.getMotherBoard())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.MOTHERBOARD,
                        computerOld.getMotherBoard() != null ? computerOld.getMotherBoard().toString() : "",
                        computerNew.getMotherBoard() != null ? computerNew.getMotherBoard().toString() : "",
                        typeEquipment, user);
            }
            if (!Processor.equalsProcessorList(computerOld.getProcessorList(), computerNew.getProcessorList())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.PROCESSOR,
                        getStrFromList(computerOld.getProcessorList()),
                        getStrFromList(computerNew.getProcessorList()),
                        typeEquipment, user);
            }
            if (!Ram.equalsRamList(computerOld.getRamList(), computerNew.getRamList())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.RAM,
                        getStrFromList(computerOld.getRamList()),
                        getStrFromList(computerNew.getRamList()),
                        typeEquipment, user);
            }
            if (!HardDrive.equalsHardDriveList(computerOld.getHardDriveList(), computerNew.getHardDriveList())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.HARDDRIVE,
                        getStrFromList(computerOld.getHardDriveList()),
                        getStrFromList(computerNew.getHardDriveList()),
                        typeEquipment, user);
            }
            if (!VideoCard.equalsVideoCardList(computerOld.getVideoCardList(), computerNew.getVideoCardList())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.VIDEOCARD,
                        getStrFromList(computerOld.getVideoCardList()),
                        getStrFromList(computerNew.getVideoCardList()),
                        typeEquipment, user);
            }
        }
    }

    private <T> String getStrFromList(List<T> list) {
        String str = "";
        if (list != null) {
            for (Object obj : list) {
                str += obj.toString() + "; ";
            }
        }
        return str;
    }

    @Transactional
    public void saveChange(TypeEvent typeEvent, Object objectChange, TypeParameter param,
                           String oldValue, String newValue, String typeEquipment, Users user) {
        Journal journal = new Journal(typeEvent, getTypeObjectFromTypeEquipment(typeEquipment),
                objectChange, oldValue, newValue, param, user);

        //String event = String.format("%s. Параметр: %s.", journal.getEvent(), param);
        //journal.setEvent(event);
        save(journal);
    }

    public TypeObject getTypeObjectFromTypeEquipment(String typeEquipment) {
        if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
            return TypeObject.COMPUTER;
        } else if (TypeEquipment.MONITOR.equals(typeEquipment)) {
            return TypeObject.MONITOR;
        } else if (TypeEquipment.PRINTER.equals(typeEquipment)) {
            return TypeObject.PRINTER;
        } else if (TypeEquipment.SCANNER.equals(typeEquipment)) {
            return TypeObject.SCANNER;
        } else if (TypeEquipment.MFD.equals(typeEquipment)) {
            return TypeObject.MFD;
        } else if (TypeEquipment.UPS.equals(typeEquipment)) {
            return TypeObject.UPS;
        }
        return null;
    }

    @Transactional
    public List<Users> getUserIdForFilter(boolean isReadAll) {
        Session session = sessionFactory.getCurrentSession();

        String queryStr = "select distinct idUser from Journal as journal where journal.idUser!=null";
        List<Long> userIdList = new ArrayList<>();
        userIdList = session.createQuery(queryStr).list();

        List<Users> usersList = new ArrayList<>();
        if (userIdList != null) {
            for (Long id : userIdList) {
                Users users = userManager.getUserById(id, isReadAll);
                if (users != null) {
                    usersList.add(users);
                }
            }
        }

        return usersList;
    }

    @Transactional
    public SortedMap<String, Long> getObjectIdListForTypeObject(TypeObject typeObject, StateObject stateObject) {
        Session session = sessionFactory.getCurrentSession();

        String queryStr = "from Journal as journal ";
        if (typeObject != null) {
            queryStr += "where journal.typeObject='" + typeObject.name() + "'";
        }
        List<Journal> journalList = session.createQuery(queryStr).list();
        //List<Journal> journalListNew = setObjectFromJournalList(journalList, null);

        SortedMap<String, Long> objectIdSortedMap = new TreeMap<>();
        SortedSet<Long> objectIdList = new TreeSet<>();
        //for (Journal journal : journalListNew) {
        for (Journal journal : journalList) {
            if (journal.getIdObject() == null || journal.getIdObject() <= 0) {
                continue;
            }
            if(!objectIdList.contains(journal.getIdObject())) {
                Journal journalNew = setObjectByJournal(journal, stateObject);
                if(journalNew == null) {
                    continue;
                }
                objectIdList.add(journalNew.getIdObject());

                if (journal.getObject() != null && !objectIdSortedMap.containsKey(journal.getObject().toString())) {
                    objectIdSortedMap.put(journal.getObject().toString(), journal.getIdObject());
                }
            }
        }

        return objectIdSortedMap;
    }

    private Journal setObjectByJournal(Journal journal, StateObject stateObject) {
        TypeObject typeObjectFromJournal = TypeObject.valueOf(journal.getTypeObject());
        if (TypeObject.COMPUTER.equals(typeObjectFromJournal)) {
            Computer computer = equipmentManager.getComputerById(journal.getIdObject(), true);
            journal.setObject(computer);
            if (stateObject != null) {
                if ((StateObject.DELETED.equals(stateObject) && computer.getDeleted()) ||
                        (StateObject.NO_DELETED.equals(stateObject) && !computer.getDeleted())) {
                    //journalListNew.add(journal);
                    return journal;
                }
            } else {
                //journalListNew.add(journal);
                return journal;
            }
        } else if (TypeObject.MFD.equals(typeObjectFromJournal) ||
                TypeObject.MONITOR.equals(typeObjectFromJournal) ||
                TypeObject.PRINTER.equals(typeObjectFromJournal) ||
                TypeObject.SCANNER.equals(typeObjectFromJournal) ||
                TypeObject.UPS.equals(typeObjectFromJournal)) {
            Equipment equipment = equipmentManager.getEquipmentById(journal.getIdObject(), true);
            journal.setObject(equipment);
            if (stateObject != null) {
                if ((StateObject.DELETED.equals(stateObject) && equipment.getDeleted()) ||
                        (StateObject.NO_DELETED.equals(stateObject) && !equipment.getDeleted())) {
                    //journalListNew.add(journal);
                    return journal;
                }
            } else {
                //journalListNew.add(journal);
                return journal;
            }
        } else if (TypeObject.ACCOUNTING1C.equals(typeObjectFromJournal)) {
            Accounting1C accounting1C = accounting1CManager.getAccounting1CById(journal.getIdObject(), true);
            journal.setObject(accounting1C);
            if (stateObject != null) {
                if ((StateObject.DELETED.equals(stateObject) && accounting1C.getDeleted()) ||
                        (StateObject.NO_DELETED.equals(stateObject) && !accounting1C.getDeleted())) {
                    //journalListNew.add(journal);
                    return journal;
                }
            } else {
                //journalListNew.add(journal);
                return journal;
            }
        } else if (TypeObject.USER.equals(typeObjectFromJournal)) {
            Users user = userManager.getUserById(journal.getIdObject(), true);
            journal.setObject(user);
            if (user != null && stateObject != null) {
                if ((StateObject.DELETED.equals(stateObject) && user.getDeleted()) ||
                        (StateObject.NO_DELETED.equals(stateObject) && !user.getDeleted())) {
                    //journalListNew.add(journal);
                    return journal;
                }
            } else {
                //journalListNew.add(journal);
                return journal;
            }
        } else if (TypeObject.EMPLOYEE.equals(typeObjectFromJournal)) {
            Employee employee = employeeManager.getEmployeeById(journal.getIdObject(), true);
            journal.setObject(employee);
            if (stateObject != null) {
                if ((StateObject.DELETED.equals(stateObject) && employee.getDeleted()) ||
                        (StateObject.NO_DELETED.equals(stateObject) && !employee.getDeleted())) {
                    //journalListNew.add(journal);
                    return journal;
                }
            } else {
                //journalListNew.add(journal);
                return journal;
            }
        } else if (TypeObject.WORKPLACE.equals(typeObjectFromJournal)) {
            Workplace workplace = workplaceManager.getWorkplaceById(journal.getIdObject(), true);
            journal.setObject(workplace);
            if (stateObject != null) {
                if ((StateObject.DELETED.equals(stateObject) && workplace.getDeleted()) ||
                        (StateObject.NO_DELETED.equals(stateObject) && !workplace.getDeleted())) {
                    //journalListNew.add(journal);
                    return journal;
                }
            } else {
                //journalListNew.add(journal);
                return journal;
            }
        }
        return null;
    }

    @Transactional
    public List<Journal> getJournalList(TypeObject typeObject, Long idObject, TypeEvent typeEvent,
                                        TypeParameter typeParameter, Users user, StateObject stateObject,
                                        Date dateStart, Date dateEnd) {
        Session session = sessionFactory.getCurrentSession();

        String queryStr = "from Journal as journal ";
        if (typeObject != null || (idObject != null && idObject > 0) ||
                typeEvent != null || typeParameter != null || user != null ||
                dateStart != null || dateEnd != null) {
            queryStr += "where ";
            if (typeObject != null) {
                queryStr += "journal.typeObject='" + typeObject.name() + "' ";
                if ((idObject != null && idObject > 0) || typeEvent != null || typeParameter != null ||
                        user != null || dateStart != null || dateEnd != null) {
                    queryStr += "and ";
                }
            }
            if (idObject != null && idObject > 0) {
                queryStr += "journal.idObject=" + idObject;
                if (typeEvent != null || typeParameter != null || user != null
                        || dateStart != null || dateEnd != null) {
                    queryStr += "and ";
                }
            }
            if (typeEvent != null) {
                queryStr += "journal.typeEvent='" + typeEvent.name() + "' ";
                if (typeParameter != null || user != null || dateStart != null || dateEnd != null) {
                    queryStr += "and ";
                }
            }
            if (typeParameter != null) {
                queryStr += "journal.parameter='" + typeParameter.name() + "' ";
                if (user != null || dateStart != null || dateEnd != null) {
                    queryStr += "and ";
                }
            }
            if (user != null) {
                queryStr += "journal.idUser=" + user.getId() + " ";
                if (dateStart != null || dateEnd != null) {
                    queryStr += "and ";
                }
            }
            if (dateStart != null) {
                queryStr += "journal.time >= '" + dateStart + "' ";
                if (dateEnd != null) {
                    queryStr += "and ";
                }
            }
            if (dateEnd != null) {
                queryStr += "journal.time <= '" + dateEnd + "' ";
            }
        }

        queryStr += "order by journal.time desc";
        List<Journal> journalList = session.createQuery(queryStr).list();

        List<Journal> journalListNew = setObjectFromJournalList(journalList, stateObject);

        return journalListNew;
    }

    private List<Journal> setObjectFromJournalList(List<Journal> journalList, StateObject stateObject) {
        List<Journal> journalListNew = new ArrayList<>();
        SortedMap<Long, Object> objectList = new TreeMap<>();
        SortedMap<Long, Users> userSortedMap = new TreeMap<>();
        for (Journal journal : journalList) {
            if(journal.getIdUser() != null && !userSortedMap.containsKey(journal.getIdUser())) {
                Users user = userManager.getUserById(journal.getIdUser(), true);
                if(user != null) {
                    userSortedMap.put(user.getId(), user);
                    journal.setUser(user);
                }
            }
            if(!objectList.containsKey(journal.getIdObject())) {
                Journal journalNew = setObjectByJournal(journal, stateObject);
                if(journalNew != null && journalNew.getObject() != null) {
                    objectList.put(journalNew.getIdObject(), journalNew.getObject());
                    journalListNew.add(journalNew);
                }
            } else {
                journal.setObject(objectList.get(journal.getIdObject()));
                journalListNew.add(journal);
            }
        }
        return journalListNew;
    }
}
