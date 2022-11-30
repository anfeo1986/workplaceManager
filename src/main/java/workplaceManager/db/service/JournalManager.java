package workplaceManager.db.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.TypeEvent;
import workplaceManager.db.TypeObject;
import workplaceManager.db.domain.*;

@Repository
public class JournalManager extends EntityManager<Journal> {
    @Transactional
    public void saveChangeEquipment(Equipment equipmentOld, Equipment equipmentNew, String typeEquipment) {
        if (equipmentNew == null || equipmentOld == null) {
            return;
        }
        if (!StringUtils.equals(equipmentNew.getUid(), equipmentOld.getUid())) {
            saveChange(TypeEvent.UPDATE_MAIN_INFORMATION, equipmentOld, "UID",
                    equipmentOld.getUid(), equipmentNew.getUid(), typeEquipment);
        }
        if (!StringUtils.equals(equipmentOld.getManufacturer(), equipmentNew.getManufacturer())) {
            saveChange(TypeEvent.UPDATE_MAIN_INFORMATION, equipmentOld, "Производитель",
                    equipmentOld.getManufacturer(), equipmentNew.getManufacturer(), typeEquipment);
        }
        if (!StringUtils.equals(equipmentNew.getModel(), equipmentOld.getModel())) {
            saveChange(TypeEvent.UPDATE_MAIN_INFORMATION, equipmentOld, "Модель",
                    equipmentOld.getModel(), equipmentNew.getModel(), typeEquipment);
        }
        if (!Workplace.equalsWorkplace(equipmentOld.getWorkplace(), equipmentNew.getWorkplace())) {
            saveChange(TypeEvent.UPDATE_BINDING_WORKPLACE, equipmentOld, "Рабочее место",
                    equipmentOld.getWorkplace() != null ? equipmentOld.getWorkplace().toString() : "",
                    equipmentNew.getWorkplace() != null ? equipmentNew.getWorkplace().toString() : "",
                    typeEquipment);
        }
        if (!Accounting1C.equalsAccounting1C(equipmentOld.getAccounting1C(), equipmentNew.getAccounting1C())) {
            saveChange(TypeEvent.UPDATE_BINDING_ACCOUNTING1C, equipmentOld, "Бухгалтерия",
                    equipmentOld.getAccounting1C() != null ? equipmentOld.getAccounting1C().toString() : "",
                    equipmentNew.getAccounting1C() != null ? equipmentNew.getAccounting1C().toString() : "",
                    typeEquipment);
        }
        if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
            Computer computerOld = (Computer) equipmentOld;
            Computer computernew = (Computer) equipmentNew;
            if (computerOld.getIp() != computernew.getIp()) {

            }

        }
    }

    private void saveChange(TypeEvent typeEvent, Object objectChange, String param,
                            String oldValue, String newValue, String typeEquipment) {
        Journal journal = new Journal(typeEvent,
                getTypeObjectFromTypeEquipment(typeEquipment), objectChange);
        String event = String.format("%s. Параметр: %s. Было: %s, стало: %s",
                journal.getEvent(), param, oldValue, newValue);
        journal.setEvent(event);
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
}
