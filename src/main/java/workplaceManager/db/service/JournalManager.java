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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class JournalManager extends EntityManager<Journal> {
    EquipmentManager equipmentManager;

    @Autowired
    public void setEquipmentManager(EquipmentManager equipmentManager) {
        this.equipmentManager = equipmentManager;
    }

    @Transactional
    public void saveChangeEquipment(Equipment equipmentOld, Equipment equipmentNew, String typeEquipment) {
        if (equipmentNew == null || equipmentOld == null) {
            return;
        }
        if (!StringUtils.equals(equipmentNew.getUid(), equipmentOld.getUid())) {
            saveChange(TypeEvent.UPDATE, equipmentOld, TypeParameter.UID.toString(),
                    equipmentOld.getUid(), equipmentNew.getUid(), typeEquipment);
        }
        if (!StringUtils.equals(equipmentOld.getManufacturer(), equipmentNew.getManufacturer())) {
            saveChange(TypeEvent.UPDATE, equipmentOld, TypeParameter.MANUFACTURER.toString(),
                    equipmentOld.getManufacturer(), equipmentNew.getManufacturer(), typeEquipment);
        }
        if (!StringUtils.equals(equipmentNew.getModel(), equipmentOld.getModel())) {
            saveChange(TypeEvent.UPDATE, equipmentOld, TypeParameter.MODEL.toString(),
                    equipmentOld.getModel(), equipmentNew.getModel(), typeEquipment);
        }
        if (!Workplace.equalsWorkplace(equipmentOld.getWorkplace(), equipmentNew.getWorkplace())) {
            saveChange(TypeEvent.UPDATE, equipmentOld, TypeParameter.WORKPLACE.toString(),
                    equipmentOld.getWorkplace() != null ? equipmentOld.getWorkplace().toString() : "",
                    equipmentNew.getWorkplace() != null ? equipmentNew.getWorkplace().toString() : "",
                    typeEquipment);
        }
        if (!Accounting1C.equalsAccounting1C(equipmentOld.getAccounting1C(), equipmentNew.getAccounting1C())) {
            saveChange(TypeEvent.UPDATE, equipmentOld, TypeParameter.ACCOUNTING1C.toString(),
                    equipmentOld.getAccounting1C() != null ? equipmentOld.getAccounting1C().toString() : "",
                    equipmentNew.getAccounting1C() != null ? equipmentNew.getAccounting1C().toString() : "",
                    typeEquipment);
        }
        if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
            Computer computerOld = (Computer) equipmentOld;
            Computer computerNew = (Computer) equipmentNew;
            if (!StringUtils.equals(computerOld.getIp(), computerNew.getIp())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.IP.toString(),
                        computerOld.getIp(), computerNew.getIp(), typeEquipment);
            }
            if (!StringUtils.equals(computerOld.getNetName(), computerNew.getNetName())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.NET_NAME.toString(),
                        computerOld.getNetName(), computerNew.getNetName(), typeEquipment);
            }
            if (!OperationSystem.equalsOS(computerOld.getOperationSystem(), computerNew.getOperationSystem())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.OS.toString(),
                        computerOld.getOperationSystem() != null ? computerOld.getOperationSystem().toString() : "",
                        computerNew.getOperationSystem() != null ? computerNew.getOperationSystem().toString() : "",
                        typeEquipment);
            }
            if (!MotherBoard.equalsMotherBoard(computerOld.getMotherBoard(), computerNew.getMotherBoard())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.MOTHERBOARD.toString(),
                        computerOld.getMotherBoard() != null ? computerOld.getMotherBoard().toString() : "",
                        computerNew.getMotherBoard() != null ? computerNew.getMotherBoard().toString() : "",
                        typeEquipment);
            }
            if (!Processor.equalsProcessorList(computerOld.getProcessorList(), computerNew.getProcessorList())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.PROCESSOR.toString(),
                        getStrFromList(computerOld.getProcessorList()),
                        getStrFromList(computerNew.getProcessorList()),
                        typeEquipment);
            }
            if (!Ram.equalsRamList(computerOld.getRamList(), computerNew.getRamList())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.RAM.toString(),
                        getStrFromList(computerOld.getRamList()),
                        getStrFromList(computerNew.getRamList()),
                        typeEquipment);
            }
            if (!HardDrive.equalsHardDriveList(computerOld.getHardDriveList(), computerNew.getHardDriveList())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.HARDDRIVE.toString(),
                        getStrFromList(computerOld.getHardDriveList()),
                        getStrFromList(computerNew.getHardDriveList()),
                        typeEquipment);
            }
            if (!VideoCard.equalsVideoCardList(computerOld.getVideoCardList(), computerNew.getVideoCardList())) {
                saveChange(TypeEvent.UPDATE, computerOld, TypeParameter.VIDEOCARD.toString(),
                        getStrFromList(computerOld.getVideoCardList()),
                        getStrFromList(computerNew.getVideoCardList()),
                        typeEquipment);
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
                           String oldValue, String newValue, String typeEquipment) {
        Journal journal = new Journal(typeEvent, getTypeObjectFromTypeEquipment(typeEquipment),
                objectChange, oldValue, newValue, param);

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
    public List<Journal> getJournalList() {
        Session session = sessionFactory.getCurrentSession();
        List<Journal> journalList = session.createQuery("from Journal as journal " +
                "order by journal.time desc ").list();

        for (Journal journal : journalList) {
            TypeObject typeObject = TypeObject.valueOf(journal.getTypeObject());
            if (TypeObject.COMPUTER.equals(typeObject)) {
                journal.setObject(equipmentManager.getComputerById(journal.getIdObject()));
            } else if (TypeObject.MFD.equals(typeObject) ||
                    TypeObject.MONITOR.equals(typeObject) ||
                    TypeObject.PRINTER.equals(typeObject) ||
                    TypeObject.SCANNER.equals(typeObject) ||
                    TypeObject.UPS.equals(typeObject)) {
                journal.setObject(equipmentManager.getEquipmentById(journal.getIdObject()));
            } else if(TypeObject.ACCOUNTING1C.equals(typeObject)) {

            } else if(TypeObject.USER.equals(typeObject)) {

            }
        }

        return journalList;
    }
}
