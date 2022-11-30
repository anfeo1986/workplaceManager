package workplaceManager.db.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.TypeEvent;
import workplaceManager.db.TypeObject;
import workplaceManager.db.domain.*;
import workplaceManager.db.domain.components.MotherBoard;
import workplaceManager.db.domain.components.Processor;

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
            Computer computerNew = (Computer) equipmentNew;
            if (!StringUtils.equals(computerOld.getIp(), computerNew.getIp())) {
                saveChange(TypeEvent.UPDATE_MAIN_INFORMATION, computerOld, "IP",
                        computerOld.getIp(), computerNew.getIp(), typeEquipment);
            }
            if (!StringUtils.equals(computerOld.getNetName(), computerNew.getNetName())) {
                saveChange(TypeEvent.UPDATE_MAIN_INFORMATION, computerOld, "Сетевое имя",
                        computerOld.getNetName(), computerNew.getNetName(), typeEquipment);
            }
            if(!MotherBoard.equalsMotherBoard(computerOld.getMotherBoard(), computerNew.getMotherBoard())) {
                saveChange(TypeEvent.UPDATE_CONFIG_COMPUTER, computerOld, "Материнская плата",
                        computerOld.getMotherBoard() != null ? computerOld.getMotherBoard().toString() : "",
                        computerNew.getMotherBoard() != null ? computerNew.getMotherBoard().toString() : "",
                        typeEquipment);
            }
            if(!Processor.equalsProcessorList(computerOld.getProcessorList(), computerNew.getProcessorList())) {
                String oldProc = "";
                if(computerOld.getProcessorList() != null) {
                    for(Processor processor : computerOld.getProcessorList()) {
                        oldProc += processor.toString();
                    }
                }
                String newProc = "";
                if(computerNew.getProcessorList() != null) {
                    for(Processor processor : computerNew.getProcessorList()) {
                        newProc += processor.toString();
                    }
                }
                saveChange(TypeEvent.UPDATE_CONFIG_COMPUTER, computerOld, "Процессоры",
                        oldProc, newProc, typeEquipment);
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
