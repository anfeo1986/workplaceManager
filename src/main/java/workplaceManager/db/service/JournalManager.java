package workplaceManager.db.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.TypeEvent;
import workplaceManager.TypeObject;
import workplaceManager.TypeParameter;
import workplaceManager.db.domain.*;
import workplaceManager.db.domain.components.*;

import java.util.List;

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
                    TypeParameter.WORKPLACE_TITLE.toString(), user));
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
                    TypeParameter.ACCOUNTING1C_TITLE.toString(), user));
        }
        if (!StringUtils.equals(accounting1COld.getInventoryNumber(), accounting1CNew.getInventoryNumber())) {
            save(new Journal(TypeEvent.UPDATE, TypeObject.ACCOUNTING1C, accounting1COld,
                    accounting1COld.getInventoryNumber(), accounting1CNew.getInventoryNumber(),
                    TypeParameter.ACCOUNTING1C_INVENTORY_NUMBER.toString(), user));
        }
        if (!Employee.equalsEmployee(accounting1COld.getEmployee(), accounting1CNew.getEmployee())) {
            save(new Journal(TypeEvent.ACCOUNTING1C_MOVING, TypeObject.ACCOUNTING1C, accounting1COld,
                    accounting1COld.getEmployee() != null ? accounting1COld.getEmployee().toString() : "",
                    accounting1CNew.getEmployee() != null ? accounting1CNew.getEmployee().toString() : "",
                    TypeParameter.ACCOUNTING1C_EMPLOYEE.toString(), user));
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
                    TypeParameter.EMPLOYEE_USERNAME.toString(), user));
        }
        if (!StringUtils.equals(employeeOld.getPost(), employeeNew.getPost())) {
            save(new Journal(TypeEvent.UPDATE, TypeObject.EMPLOYEE, employeeOld,
                    employeeOld.getPost(), employeeNew.getPost(),
                    TypeParameter.EMPLOYEE_POST.toString(), user));
        }
        if (!Workplace.equalsWorkplace(employeeOld.getWorkplace(), employeeNew.getWorkplace())) {
            save(new Journal(TypeEvent.UPDATE, TypeObject.EMPLOYEE, employeeOld,
                    employeeOld.getWorkplace() != null ? employeeOld.getWorkplace().toString() : "",
                    employeeNew.getWorkplace() != null ? employeeNew.getWorkplace().toString() : "",
                    TypeParameter.WORKPLACE.toString(), user));
        }

    }

    @Transactional
    public void saveChangeEquipment(Equipment equipmentOld, Equipment equipmentNew,
                                    String typeEquipment, Users user) {
        if (equipmentNew == null || equipmentOld == null) {
            return;
        }
        if (!StringUtils.equals(equipmentNew.getUid(), equipmentOld.getUid())) {
            saveChange(TypeEvent.UPDATE, equipmentOld, TypeParameter.UID.toString(),
                    equipmentOld.getUid(), equipmentNew.getUid(), typeEquipment, user);
        }
        if (!StringUtils.equals(equipmentOld.getManufacturer(), equipmentNew.getManufacturer())) {
            saveChange(TypeEvent.UPDATE, equipmentOld, TypeParameter.MANUFACTURER.toString(),
                    equipmentOld.getManufacturer(), equipmentNew.getManufacturer(), typeEquipment, user);
        }
        if (!StringUtils.equals(equipmentNew.getModel(), equipmentOld.getModel())) {
            saveChange(TypeEvent.UPDATE, equipmentOld, TypeParameter.MODEL.toString(),
                    equipmentOld.getModel(), equipmentNew.getModel(), typeEquipment, user);
        }
        if (!Workplace.equalsWorkplace(equipmentOld.getWorkplace(), equipmentNew.getWorkplace())) {
            saveChange(TypeEvent.UPDATE, equipmentOld, TypeParameter.WORKPLACE.toString(),
                    equipmentOld.getWorkplace() != null ? equipmentOld.getWorkplace().toString() : "",
                    equipmentNew.getWorkplace() != null ? equipmentNew.getWorkplace().toString() : "",
                    typeEquipment, user);
        }
        if (!Accounting1C.equalsAccounting1C(equipmentOld.getAccounting1C(), equipmentNew.getAccounting1C())) {
            saveChange(TypeEvent.UPDATE, equipmentOld, TypeParameter.ACCOUNTING1C.toString(),
                    equipmentOld.getAccounting1C() != null ? equipmentOld.getAccounting1C().toString() : "",
                    equipmentNew.getAccounting1C() != null ? equipmentNew.getAccounting1C().toString() : "",
                    typeEquipment, user);
        }
        if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
            Computer computerOld = (Computer) equipmentOld;
            Computer computerNew = (Computer) equipmentNew;
            if (!StringUtils.equals(computerOld.getIp(), computerNew.getIp())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.IP.toString(),
                        computerOld.getIp(), computerNew.getIp(), typeEquipment, user);
            }
            if (!StringUtils.equals(computerOld.getNetName(), computerNew.getNetName())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.NET_NAME.toString(),
                        computerOld.getNetName(), computerNew.getNetName(), typeEquipment, user);
            }
            if (!OperationSystem.equalsOS(computerOld.getOperationSystem(), computerNew.getOperationSystem())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.OS.toString(),
                        computerOld.getOperationSystem() != null ? computerOld.getOperationSystem().toString() : "",
                        computerNew.getOperationSystem() != null ? computerNew.getOperationSystem().toString() : "",
                        typeEquipment, user);
            }
            if (!MotherBoard.equalsMotherBoard(computerOld.getMotherBoard(), computerNew.getMotherBoard())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.MOTHERBOARD.toString(),
                        computerOld.getMotherBoard() != null ? computerOld.getMotherBoard().toString() : "",
                        computerNew.getMotherBoard() != null ? computerNew.getMotherBoard().toString() : "",
                        typeEquipment, user);
            }
            if (!Processor.equalsProcessorList(computerOld.getProcessorList(), computerNew.getProcessorList())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.PROCESSOR.toString(),
                        getStrFromList(computerOld.getProcessorList()),
                        getStrFromList(computerNew.getProcessorList()),
                        typeEquipment, user);
            }
            if (!Ram.equalsRamList(computerOld.getRamList(), computerNew.getRamList())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.RAM.toString(),
                        getStrFromList(computerOld.getRamList()),
                        getStrFromList(computerNew.getRamList()),
                        typeEquipment, user);
            }
            if (!HardDrive.equalsHardDriveList(computerOld.getHardDriveList(), computerNew.getHardDriveList())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.HARDDRIVE.toString(),
                        getStrFromList(computerOld.getHardDriveList()),
                        getStrFromList(computerNew.getHardDriveList()),
                        typeEquipment, user);
            }
            if (!VideoCard.equalsVideoCardList(computerOld.getVideoCardList(), computerNew.getVideoCardList())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.VIDEOCARD.toString(),
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
    public void saveChange(TypeEvent typeEvent, Object objectChange, String param,
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
    public List<Journal> getJournalList(TypeObject typeObject) {
        Session session = sessionFactory.getCurrentSession();

        String queryStr = "from Journal as journal ";
        if (typeObject != null) {
            queryStr += "where journal.typeObject='" + typeObject.name() + "' ";
        }
        queryStr += "order by journal.time desc";
        List<Journal> journalList = session.createQuery(queryStr).list();

        for (Journal journal : journalList) {
            journal.setUser(userManager.getUserById(journal.getIdUser(), true));
            TypeObject typeObjectFromJournal = TypeObject.valueOf(journal.getTypeObject());
            if (TypeObject.COMPUTER.equals(typeObjectFromJournal)) {
                journal.setObject(equipmentManager.getComputerById(journal.getIdObject(), true));
            } else if (TypeObject.MFD.equals(typeObjectFromJournal) ||
                    TypeObject.MONITOR.equals(typeObjectFromJournal) ||
                    TypeObject.PRINTER.equals(typeObjectFromJournal) ||
                    TypeObject.SCANNER.equals(typeObjectFromJournal) ||
                    TypeObject.UPS.equals(typeObjectFromJournal)) {
                journal.setObject(equipmentManager.getEquipmentById(journal.getIdObject(), true));
            } else if (TypeObject.ACCOUNTING1C.equals(typeObjectFromJournal)) {
                journal.setObject(accounting1CManager.getAccounting1CById(journal.getIdObject(), true));
            } else if (TypeObject.USER.equals(typeObjectFromJournal)) {
                journal.setObject(userManager.getUserById(journal.getIdObject(), true));
            } else if (TypeObject.EMPLOYEE.equals(typeObjectFromJournal)) {
                journal.setObject(employeeManager.getEmployeeById(journal.getIdObject(), true));
            } else if (TypeObject.WORKPLACE.equals(typeObjectFromJournal)) {
                journal.setObject(workplaceManager.getWorkplaceById(journal.getIdObject(), true));
            }
        }

        return journalList;
    }
}
