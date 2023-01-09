package workplaceManager.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.*;
import workplaceManager.TypeEvent;
import workplaceManager.TypeObject;
import workplaceManager.db.domain.*;
import workplaceManager.db.domain.components.*;
import workplaceManager.db.service.*;
import workplaceManager.sorting.FilterAccounting1C;
import workplaceManager.sorting.SortingAccounting1C;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ConfigEquipmentController {

    private enum TypeComponentsComputer {
        processor,
        ram,
        hardDrive,
        videoCard
    }

    EquipmentManager equipmentManager;

    @Autowired
    public void setEquipmentManager(EquipmentManager equipmentManager) {
        this.equipmentManager = equipmentManager;
    }

    WorkplaceManager workplaceManager;

    @Autowired
    public void setWorkplaceManager(WorkplaceManager workplaceManager) {
        this.workplaceManager = workplaceManager;
    }

    Accounting1CManager accounting1CManager;

    @Autowired
    public void setAccounting1CManager(Accounting1CManager accounting1CManager) {
        this.accounting1CManager = accounting1CManager;
    }

    EmployeeManager employeeManager;

    @Autowired
    public void setEmployeeManager(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    private SecurityCrypt securityCrypt;

    @Autowired
    public void setSecurityCrypt(SecurityCrypt securityCrypt) {
        this.securityCrypt = securityCrypt;
    }

    private ProcessorManager processorManager;

    @Autowired
    public void setProcessorManager(ProcessorManager processorManager) {
        this.processorManager = processorManager;
    }

    private RamManager ramManager;

    @Autowired
    public void setRamManager(RamManager ramManager) {
        this.ramManager = ramManager;
    }

    private VideoCardManager videoCardManager;

    @Autowired
    public void setVideoCardManager(VideoCardManager videoCardManager) {
        this.videoCardManager = videoCardManager;
    }

    private HardDriveManager hardDriveManager;

    @Autowired
    public void setHardDriveManager(HardDriveManager hardDriveManager) {
        this.hardDriveManager = hardDriveManager;
    }

    private JournalManager journalManager;

    @Autowired
    public void setJournalManager(JournalManager journalManager) {
        this.journalManager = journalManager;
    }

    private VirtualMachineManager virtualMachineManager;

    @Autowired
    public void setVirtualMachineManager(VirtualMachineManager virtualMachineManager) {
        this.virtualMachineManager = virtualMachineManager;
    }

    @GetMapping(Pages.addUpdateEquipment)
    public ModelAndView getFormAddUpdateEquipment(@RequestParam(name = Parameters.id, required = false) Long id,
                                                  @RequestParam(name = Parameters.typeEquipment) String typeEquipment,
                                                  @RequestParam(name = Parameters.redirect, required = false) String redirect,
                                                  HttpServletRequest request) {
        ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.formConfigEquipment);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            Equipment equipment = new Equipment();
            if (id != null && id > 0) {
                equipment = equipmentManager.getEquipmentById(id, false);
            }

            modelAndView.addObject(Parameters.equipment, equipment);
            modelAndView.addObject(Parameters.typeEquipment, typeEquipment);
            if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
                Computer computer = equipmentManager.getComputerById(id, false);
                modelAndView.addObject(Parameters.computer, computer == null ? new Computer() : computer);
            }

            if (request.getParameter(Parameters.workplaceId) != null) {
                Long workplaceId = Long.parseLong(request.getParameter(Parameters.workplaceId));
                modelAndView.addObject(Parameters.workplaceId, workplaceId);
            }
        }
        return getModelAndView(redirect, modelAndView);
    }

    private ModelAndView readConfigComputer(@ModelAttribute(Parameters.equipment) Equipment equipment,
                                            HttpServletRequest request, boolean readOsVM) {
        Users user = securityCrypt.getUserBySession(request);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.formConfigEquipment);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                setWorkplaceByEquipment(equipment, request);

                setAccounting1CByEquipment(equipment, request, false, user);

                //Computer computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);
                Computer computer = equipmentManager.getComputerById(equipment.getId(), false);
                if (computer == null) {
                    computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);
                }
                Computer computerOld = equipmentManager.getComputerById(computer.getId(), false);
                if (computerOld == null) {
                    computerOld = new Computer();
                }

                setParameterComputer(computer, request, equipment);
                computer.setDeleted(false);

                if (!saveComputer(modelAndView, request, computerOld, computer, equipment, user)) {
                    return modelAndView;
                }

                computerOld = equipmentManager.getComputerById(computer.getId(), false);
                if (computerOld == null) {
                    computerOld = new Computer();
                }

                String message = "";
                if (readOsVM) {
                    int countVirtualMachine = Integer.parseInt(request.getParameter(Parameters.countVirtualMachine));
                    for (int numberVirtualMachine = 1; numberVirtualMachine <= countVirtualMachine; numberVirtualMachine++) {
                        String buttonReadOsVM = Parameters.virtualMachineButtonReadOs + numberVirtualMachine;
                        if (request.getParameter(buttonReadOsVM) != null) {
                            VirtualMachine virtualMachine = computer.getVirtualMachineList().get(numberVirtualMachine - 1);

                            if(virtualMachine == null) {
                                continue;
                            }

                            if(TypeOS.windows.equals(virtualMachine.getTypeOS())) {
                                try {
                                    OperationSystem os = WMI.getOperationSystem(virtualMachine.getIp());

                                    virtualMachine.setTypeOS(TypeOS.windows);
                                    virtualMachine.setVendor(os.getVendor());
                                    virtualMachine.setVersion(os.getVersion());

                                    virtualMachineManager.save(virtualMachine);
                                } catch (Exception exception) {
                                    message = exception.getMessage();
                                }
                            } else if(TypeOS.linux.equals(virtualMachine.getTypeOS())) {

                            }
                        }
                    }
                } else {
                    Computer computerWithConfig = new Computer();
                    computerWithConfig.setIp(computer.getIp());
                    if (TypeOS.windows.toString().equals(request.getParameter(Parameters.typeOS))) {
                        try {
                            WMI.getAllConfigFormComputerWindows(computerWithConfig);
                        } catch (Exception exception) {
                            message = exception.getMessage();
                        }
                        journalManager.save(new Journal(TypeEvent.READ_CONFIG_COMPUTER, TypeObject.COMPUTER, computer, user));

                        saveComponentComputer(computer, computerWithConfig);
                    }
                }

                if (!saveComputer(modelAndView, request, computerOld, computer, equipment, user)) {
                    return modelAndView;
                }

                modelAndView.addObject(Parameters.computer, computer);
                modelAndView.addObject(Parameters.message, message);
                modelAndView.addObject(Parameters.equipment, equipment);
                modelAndView.addObject(Parameters.typeEquipment, request.getParameter(Parameters.typeEquipment));
            }

            return getModelAndView(request.getParameter(Parameters.redirect), modelAndView);
        } else {
            return new ModelAndView(Pages.login);
        }
    }

    private ModelAndView addComponentComputer(@ModelAttribute(Parameters.equipment) Equipment equipment,
                                              //@RequestParam(name = Parameters.token) String token,
                                              HttpServletRequest request,
                                              TypeComponentsComputer typeComponentsComputer) {
        Users user = securityCrypt.getUserBySession(request);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.formConfigEquipment);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                setWorkplaceByEquipment(equipment, request);

                setAccounting1CByEquipment(equipment, request, false, user);

                Computer computer = equipmentManager.getComputerById(equipment.getId(), false);
                if (computer == null) {
                    computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);
                }
                Computer computerOld = equipmentManager.getComputerById(computer.getId(), false);
                if (computerOld == null) {
                    computerOld = new Computer();
                }

                setParameterComputer(computer, request, equipment);

                if (!saveComputer(modelAndView, request, computerOld, computer, equipment, user)) {
                    return modelAndView;
                }
                computerOld = equipmentManager.getComputerById(computer.getId(), false);
                if (computerOld == null) {
                    computerOld = new Computer();
                }

                if (TypeComponentsComputer.processor.equals(typeComponentsComputer)) {
                    //Computer computerOld = equipmentManager.getComputerById(equipment.getId(), false);

                    Processor processor = new Processor();
                    processor.setComputer(computer);
                    processorManager.save(processor);
                    computer.getProcessorList().add(processor);

                    if (!saveComputer(modelAndView, request, computerOld, computer, equipment, user)) {
                        return modelAndView;
                    }

                    //journalManager.saveChangeEquipment(computerOld, computer, TypeEquipment.COMPUTER, user);
                }
                if (TypeComponentsComputer.ram.equals(typeComponentsComputer)) {
                    //Computer computerOld = equipmentManager.getComputerById(equipment.getId(), false);

                    Ram ram = new Ram();
                    ram.setComputer(computer);
                    ramManager.save(ram);
                    computer.getRamList().add(ram);

                    if (!saveComputer(modelAndView, request, computerOld, computer, equipment, user)) {
                        return modelAndView;
                    }

                    //journalManager.saveChangeEquipment(computerOld, computer, TypeEquipment.COMPUTER, user);
                }
                if (TypeComponentsComputer.videoCard.equals(typeComponentsComputer)) {
                    VideoCard videoCard = new VideoCard();
                    videoCard.setComputer(computer);
                    videoCardManager.save(videoCard);
                    computer.getVideoCardList().add(videoCard);

                    if (!saveComputer(modelAndView, request, computerOld, computer, equipment, user)) {
                        return modelAndView;
                    }
                }
                if (TypeComponentsComputer.hardDrive.equals(typeComponentsComputer)) {
                    HardDrive hardDrive = new HardDrive();
                    hardDrive.setComputer(computer);
                    hardDriveManager.save(hardDrive);
                    computer.getHardDriveList().add(hardDrive);

                    if (!saveComputer(modelAndView, request, computerOld, computer, equipment, user)) {
                        return modelAndView;
                    }
                }

                //equipmentManager.save(computer);
                modelAndView.addObject(Parameters.computer, computer);
                modelAndView.addObject(Parameters.equipment, equipment);
                modelAndView.addObject(Parameters.typeEquipment, request.getParameter(Parameters.typeEquipment));
            }
            return getModelAndView(request.getParameter(Parameters.redirect), modelAndView);
        } else {
            return new ModelAndView(Pages.login);
        }
    }

    private ModelAndView deleteComponentsComputer(@ModelAttribute(Parameters.equipment) Equipment equipment,
                                                  HttpServletRequest request,
                                                  TypeComponentsComputer typeComponentsComputer) {
        Users user = securityCrypt.getUserBySession(request);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.formConfigEquipment);

            if (!modelAndView.getViewName().equals(Pages.login)) {

                setWorkplaceByEquipment(equipment, request);

                setAccounting1CByEquipment(equipment, request, false, user);

                //Computer computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);
                Computer computer = equipmentManager.getComputerById(equipment.getId(), false);
                if (computer == null) {
                    computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);
                }
                Computer computerOld = equipmentManager.getComputerById(computer.getId(), false);
                if (computerOld == null) {
                    computerOld = new Computer();
                }

                setParameterComputer(computer, request, equipment);

                if (!saveComputer(modelAndView, request, computerOld, computer, equipment, user)) {
                    return modelAndView;
                }
                computerOld = equipmentManager.getComputerById(computer.getId(), false);
                if (computerOld == null) {
                    computerOld = new Computer();
                }

                if (TypeComponentsComputer.processor.equals(typeComponentsComputer)) {
                    int countProcessor = Integer.parseInt(request.getParameter(Parameters.countProcessor));
                    for (int numberProcessor = 1; numberProcessor <= countProcessor; numberProcessor++) {
                        String buttonDeleteProcessor = Components.buttonDeleteProcessor + numberProcessor;
                        if (request.getParameter(buttonDeleteProcessor) != null) {
                            Processor processor = computer.getProcessorList().get(numberProcessor - 1);
                            processorManager.delete(processor);
                            computer.getProcessorList().remove(numberProcessor - 1);

                            if (!saveComputer(modelAndView, request, computerOld, computer, equipment, user)) {
                                return modelAndView;
                            }
                        }
                    }
                }
                if (TypeComponentsComputer.ram.equals(typeComponentsComputer)) {
                    int countRam = Integer.parseInt(request.getParameter(Parameters.countRam));
                    for (int numberRam = 1; numberRam <= countRam; numberRam++) {
                        String buttonDeleteRam = Components.buttonDeleteRam + numberRam;
                        if (request.getParameter(buttonDeleteRam) != null) {
                            Ram ram = computer.getRamList().get(numberRam - 1);
                            ramManager.delete(ram);
                            computer.getRamList().remove(numberRam - 1);

                            if (!saveComputer(modelAndView, request, computerOld, computer, equipment, user)) {
                                return modelAndView;
                            }
                        }
                    }
                }
                if (TypeComponentsComputer.videoCard.equals(typeComponentsComputer)) {
                    int countVideoCard = Integer.parseInt(request.getParameter(Parameters.countVideoCard));
                    for (int i = 1; i <= countVideoCard; i++) {
                        String buttonDeleteVideoCard = Components.buttonDeleteVideoCard + i;
                        if (request.getParameter(buttonDeleteVideoCard) != null) {
                            VideoCard videoCard = computer.getVideoCardList().get(i - 1);
                            videoCardManager.delete(videoCard);
                            computer.getVideoCardList().remove(i - 1);

                            if (!saveComputer(modelAndView, request, computerOld, computer, equipment, user)) {
                                return modelAndView;
                            }
                        }
                    }
                }
                if (TypeComponentsComputer.hardDrive.equals(typeComponentsComputer)) {
                    int countHardDrive = Integer.parseInt(request.getParameter(Parameters.countHardDrive));
                    for (int i = 1; i <= countHardDrive; i++) {
                        String buttonDeleteHardDrive = Components.buttonDeleteHardDrive + i;
                        if (request.getParameter(buttonDeleteHardDrive) != null) {
                            HardDrive hardDrive = computer.getHardDriveList().get(i - 1);
                            hardDriveManager.delete(hardDrive);
                            computer.getHardDriveList().remove(i - 1);

                            if (!saveComputer(modelAndView, request, computerOld, computer, equipment, user)) {
                                return modelAndView;
                            }
                        }
                    }
                }

                //equipmentManager.save(computer);
                modelAndView.addObject(Parameters.computer, computer);
                modelAndView.addObject(Parameters.equipment, equipment);
                modelAndView.addObject(Parameters.typeEquipment, request.getParameter(Parameters.typeEquipment));
            }
            return getModelAndView(request.getParameter(Parameters.redirect), modelAndView);
        } else {
            return new ModelAndView(Pages.login);
        }
    }

    private ModelAndView deleteVirtualMachineFromComputer(@ModelAttribute(Parameters.equipment) Equipment equipment,
                                                          HttpServletRequest request) {
        Users user = securityCrypt.getUserBySession(request);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.formConfigEquipment);

            if (!modelAndView.getViewName().equals(Pages.login)) {

                setWorkplaceByEquipment(equipment, request);

                setAccounting1CByEquipment(equipment, request, false, user);

                //Computer computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);
                Computer computer = equipmentManager.getComputerById(equipment.getId(), false);
                if (computer == null) {
                    computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);
                }
                Computer computerOld = equipmentManager.getComputerById(computer.getId(), false);
                if (computerOld == null) {
                    computerOld = new Computer();
                }

                setParameterComputer(computer, request, equipment);

                if (!saveComputer(modelAndView, request, computerOld, computer, equipment, user)) {
                    return modelAndView;
                }
                computerOld = equipmentManager.getComputerById(computer.getId(), false);
                if (computerOld == null) {
                    computerOld = new Computer();
                }

                int countVirtualMachine = Integer.parseInt(request.getParameter(Parameters.countVirtualMachine));
                for (int numberVirtualMachine = 1; numberVirtualMachine <= countVirtualMachine; numberVirtualMachine++) {
                    String buttonDeleteVM = Parameters.virtualMachineButtonDelete + numberVirtualMachine;
                    if (request.getParameter(buttonDeleteVM) != null) {
                        //Computer computerOld = equipmentManager.getComputerById(equipment.getId(), false);
                        VirtualMachine virtualMachine = computer.getVirtualMachineList().get(numberVirtualMachine - 1);
                        virtualMachineManager.delete(virtualMachine);

                        computer.getVirtualMachineList().remove(numberVirtualMachine - 1);

                        if (!saveComputer(modelAndView, request, computerOld, computer, equipment, user)) {
                            return modelAndView;
                        }

                        //journalManager.saveChangeEquipment(computerOld, computer, TypeEquipment.COMPUTER, user);
                    }
                }

                //equipmentManager.save(computer);
                modelAndView.addObject(Parameters.computer, computer);
                modelAndView.addObject(Parameters.equipment, equipment);
                modelAndView.addObject(Parameters.typeEquipment, request.getParameter(Parameters.typeEquipment));
            }
            return getModelAndView(request.getParameter(Parameters.redirect), modelAndView);
        } else {
            return new ModelAndView(Pages.login);
        }
    }

    private boolean saveComputer(ModelAndView modelAndView, HttpServletRequest request, Computer computerOld,
                                 Computer computer, Equipment equipment, Users user) {
        if (computer.getId() <= 0) {
            String error = dataVerification(request, equipment);
            if (error != null && !StringUtils.equals(error, "")) {
                modelAndView.addObject(Parameters.computer, computer);
                modelAndView.addObject(Parameters.error, error);
                modelAndView.addObject(Parameters.equipment, equipment);
                modelAndView.addObject(Parameters.typeEquipment, request.getParameter(Parameters.typeEquipment));
                //return getModelAndView(request.getParameter(Parameters.redirect), modelAndView);
                return false;
            }
            equipmentManager.save(computer);
            equipment.setId(computer.getId());
            journalManager.save(new Journal(TypeEvent.ADD, TypeObject.COMPUTER, computer, user));
        } else {
            equipmentManager.save(computer);
            computer = equipmentManager.getComputerById(computer.getId(), false);
            journalManager.saveChangeEquipment(computerOld, computer, TypeEquipment.COMPUTER, user);
        }
        return true;
    }

    private void saveProcessorComputer(Computer computer, Computer computerWithConfig) {
        if (!CollectionUtils.isEmpty(computerWithConfig.getProcessorList())) {
            //добавляем новые
            for (Processor processorConfig : computerWithConfig.getProcessorList()) {
                boolean isExist = false;
                for (Processor processor : computer.getProcessorList()) {
                    if (Processor.equalsProcessor(processorConfig, processor)) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    processorConfig.setComputer(computer);
                    processorManager.save(processorConfig);
                    computer.getProcessorList().add(processorConfig);
                }
            }
            //удаляем те, которые не вошли в новый массив
            for (Processor processor : computer.getProcessorList()) {
                boolean isExist = false;
                for (Processor processorConfig : computerWithConfig.getProcessorList()) {
                    if (Processor.equalsProcessor(processor, processorConfig)) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    processorManager.delete(processor);
                }
            }
        } else {
            computer.setProcessorList(new ArrayList<>());
        }
    }

    private void saveRamComputer(Computer computer, Computer computerWithConfig) {
        if (!CollectionUtils.isEmpty(computerWithConfig.getRamList())) {
            //добавляем новые
            for (Ram ramConfig : computerWithConfig.getRamList()) {
                boolean isExist = false;
                for (Ram ram : computer.getRamList()) {
                    if (Ram.equalsRam(ramConfig, ram)) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    ramConfig.setComputer(computer);
                    ramManager.save(ramConfig);
                    computer.getRamList().add(ramConfig);
                }
            }
            //удаляем те, которые не вошли в новый массив
            for (Ram ram : computer.getRamList()) {
                boolean isExist = false;
                for (Ram ramConfig : computerWithConfig.getRamList()) {
                    if (Ram.equalsRam(ram, ramConfig)) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    ramManager.delete(ram);
                }
            }
        } else {
            computer.setRamList(new ArrayList<>());
        }
    }

    private void saveVideoCardComputer(Computer computer, Computer computerWithConfig) {
        if (!CollectionUtils.isEmpty(computerWithConfig.getVideoCardList())) {
            //добавляем новые
            for (VideoCard videoCardConfig : computerWithConfig.getVideoCardList()) {
                boolean isExist = false;
                for (VideoCard videoCard : computer.getVideoCardList()) {
                    if (VideoCard.equalsVideoCard(videoCardConfig, videoCard)) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    videoCardConfig.setComputer(computer);
                    videoCardManager.save(videoCardConfig);
                    computer.getVideoCardList().add(videoCardConfig);
                }
            }
            //удаляем те, которые не вошли в новый массив
            for (VideoCard videoCard : computer.getVideoCardList()) {
                boolean isExist = false;
                for (VideoCard videoCardConfig : computerWithConfig.getVideoCardList()) {
                    if (VideoCard.equalsVideoCard(videoCard, videoCardConfig)) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    videoCardManager.delete(videoCard);
                }
            }
        } else {
            computer.setVideoCardList(new ArrayList<>());
        }
    }

    private void saveHardDriveComputer(Computer computer, Computer computerWithConfig) {
        if (!CollectionUtils.isEmpty(computerWithConfig.getHardDriveList())) {
            //добавляем новые
            for (HardDrive hardDriveConfig : computerWithConfig.getHardDriveList()) {
                boolean isExist = false;
                for (HardDrive hardDrive : computer.getHardDriveList()) {
                    if (HardDrive.equalsHardDrive(hardDriveConfig, hardDrive)) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    hardDriveConfig.setComputer(computer);
                    hardDriveManager.save(hardDriveConfig);
                    computer.getHardDriveList().add(hardDriveConfig);
                }
            }
            //удаляем те, которые не вошли в новый массив
            for (HardDrive hardDrive : computer.getHardDriveList()) {
                boolean isExist = false;
                for (HardDrive hardDriveConfig : computerWithConfig.getHardDriveList()) {
                    if (HardDrive.equalsHardDrive(hardDrive, hardDriveConfig)) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    hardDriveManager.delete(hardDrive);
                }
            }
        } else {
            computer.setHardDriveList(new ArrayList<>());
        }
    }

    private void saveComponentComputer(Computer computer, Computer computerWithConfig) {
        computer.setNetName(computerWithConfig.getNetName());

        if (computer.getOperationSystem() == null || computer.getOperationSystem().getId() <= 0) {
            computer.setOperationSystem(new OperationSystem());
        }
        computer.getOperationSystem().setTypeOS(computerWithConfig.getOperationSystem().getTypeOS());
        computer.getOperationSystem().setVendor(computerWithConfig.getOperationSystem().getVendor());
        computer.getOperationSystem().setVersion(computerWithConfig.getOperationSystem().getVersion());
        computer.getOperationSystem().setOSArchitecture(computerWithConfig.getOperationSystem().getOSArchitecture());

        if (computer.getMotherBoard() == null || computer.getMotherBoard().getId() <= 0) {
            computer.setMotherBoard(new MotherBoard());
            computer.getMotherBoard().setComputer(computer);
        }
        computer.getMotherBoard().setModel(computerWithConfig.getMotherBoard().getModel());
        computer.getMotherBoard().setManufacturer(computerWithConfig.getMotherBoard().getManufacturer());

        saveProcessorComputer(computer, computerWithConfig);
        saveRamComputer(computer, computerWithConfig);
        saveVideoCardComputer(computer, computerWithConfig);
        saveHardDriveComputer(computer, computerWithConfig);
    }

    private ModelAndView addVirtualMachine(Equipment equipment, HttpServletRequest request) {
        Users user = securityCrypt.getUserBySession(request);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.formConfigEquipment);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                setWorkplaceByEquipment(equipment, request);

                setAccounting1CByEquipment(equipment, request, false, user);

                Computer computer = equipmentManager.getComputerById(equipment.getId(), false);
                if (computer == null) {
                    computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);
                }
                Computer computerOld = equipmentManager.getComputerById(computer.getId(), false);
                if (computerOld == null) {
                    computerOld = new Computer();
                }

                setParameterComputer(computer, request, equipment);

                if (!saveComputer(modelAndView, request, computerOld, computer, equipment, user)) {
                    return modelAndView;
                }
                computerOld = equipmentManager.getComputerById(computer.getId(), false);
                if (computerOld == null) {
                    computerOld = new Computer();
                }

                //Computer computerOld = equipmentManager.getComputerById(equipment.getId(), false);

                VirtualMachine virtualMachine = new VirtualMachine();
                virtualMachine.setComputer(computer);
                virtualMachineManager.save(virtualMachine);

                computer.getVirtualMachineList().add(virtualMachine);

                if (!saveComputer(modelAndView, request, computerOld, computer, equipment, user)) {
                    return modelAndView;
                }
                //journalManager.saveChangeEquipment(computerOld, computer, TypeEquipment.COMPUTER, user);

                //equipmentManager.save(computer);
                modelAndView.addObject(Parameters.computer, computer);
                modelAndView.addObject(Parameters.equipment, equipment);
                modelAndView.addObject(Parameters.typeEquipment, request.getParameter(Parameters.typeEquipment));
            }
            return getModelAndView(request.getParameter(Parameters.redirect), modelAndView);
        } else {
            return new ModelAndView(Pages.login);
        }
    }

    private ModelAndView searchPage(Equipment equipment,
                                    HttpServletRequest request) {
        if (request.getParameter(Components.buttonReadConfigComputer) != null) {
            return readConfigComputer(equipment, request, false);
        }
        if (request.getParameter(Components.buttonAddVirtualMachine) != null) {
            return addVirtualMachine(equipment, request);
        }
        int countVirtualMachine = Integer.parseInt(request.getParameter(Parameters.countVirtualMachine));
        for (int i = 1; i <= countVirtualMachine; i++) {
            String virtualMachineButtonDelete = Parameters.virtualMachineButtonDelete + i;
            if (request.getParameter(virtualMachineButtonDelete) != null) {
                return deleteVirtualMachineFromComputer(equipment, request);
            }
            String vmButtonReadOs = Parameters.virtualMachineButtonReadOs + i;
            if (request.getParameter(vmButtonReadOs) != null) {
                return readConfigComputer(equipment, request, true);
            }
        }

        if (request.getParameter(Components.buttonAddProcessor) != null) {
            return addComponentComputer(equipment, request, TypeComponentsComputer.processor);
        }
        int countProcessor = Integer.parseInt(request.getParameter(Parameters.countProcessor));
        for (int i = 1; i <= countProcessor; i++) {
            String buttonDeleteProcessor = Components.buttonDeleteProcessor + i;
            if (request.getParameter(buttonDeleteProcessor) != null) {
                return deleteComponentsComputer(equipment, request, TypeComponentsComputer.processor);
            }
        }
        if (request.getParameter(Components.buttonAddRam) != null) {
            return addComponentComputer(equipment, request, TypeComponentsComputer.ram);
        }
        int countRam = Integer.parseInt(request.getParameter(Parameters.countRam));
        for (int i = 1; i <= countRam; i++) {
            String buttonDeleteRam = Components.buttonDeleteRam + i;
            if (request.getParameter(buttonDeleteRam) != null) {
                return deleteComponentsComputer(equipment, request, TypeComponentsComputer.ram);
            }
        }
        if (request.getParameter(Components.buttonAddVideoCard) != null) {
            return addComponentComputer(equipment, request, TypeComponentsComputer.videoCard);
        }
        int countVideCard = Integer.parseInt(request.getParameter(Parameters.countVideoCard));
        for (int i = 1; i <= countVideCard; i++) {
            String buttonDeleteVideoCard = Components.buttonDeleteVideoCard + i;
            if (request.getParameter(buttonDeleteVideoCard) != null) {
                return deleteComponentsComputer(equipment, request, TypeComponentsComputer.videoCard);
            }
        }
        if (request.getParameter(Components.buttonAddHardDrive) != null) {
            return addComponentComputer(equipment, request, TypeComponentsComputer.hardDrive);
        }
        int countHardDrive = Integer.parseInt(request.getParameter(Parameters.countHardDrive));
        for (int i = 1; i <= countHardDrive; i++) {
            String buttonDeleteHardDrive = Components.buttonDeleteHardDrive + i;
            if (request.getParameter(buttonDeleteHardDrive) != null) {
                return deleteComponentsComputer(equipment, request, TypeComponentsComputer.hardDrive);
            }
        }
        return null;
    }

    private String dataVerification(HttpServletRequest request, Equipment equipment) {
        String typeEquipment = request.getParameter(Parameters.typeEquipment);
        String error = "";
        if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
            Computer computerFromDb = equipmentManager.getComputerByIp(request.getParameter(Parameters.ip), false);
            if (computerFromDb != null && computerFromDb.getId() != equipment.getId()) {
                error = request.getParameter(Parameters.ip) + " уже существует ";
            }
        }

        Equipment equipmentFromDb = equipmentManager.getEquipmentByUid(equipment.getUid(), false);
        if (equipmentFromDb != null && equipmentFromDb.getId() != equipment.getId()) {
            error += String.format("%s уже существует ", equipment.getUid());
        }
        return error;
    }

    private Equipment getEquipment(@ModelAttribute(Parameters.equipment) Equipment equipmentRequest) {
        Equipment equipment = new Equipment();
        if (equipmentRequest.getId() > 0) {
            equipment = equipmentManager.getEquipmentById(equipmentRequest.getId(), false);
        }

        equipment.setUid(equipmentRequest.getUid());
        equipment.setManufacturer(equipmentRequest.getManufacturer());
        equipment.setModel(equipmentRequest.getModel());
        equipment.setComment(equipmentRequest.getComment());

        return equipment;
    }

    @PostMapping(Pages.addEquipmentPost)
    public ModelAndView addEquipment(@ModelAttribute(Parameters.equipment) Equipment equipmentRequest,
                                     //@RequestParam(name = Parameters.token) String token,
                                     HttpServletRequest request) {
        //equipment.setDeleted(false);
        Equipment equipment = getEquipment(equipmentRequest);

        ModelAndView modelAndViewSearch = searchPage(equipment, request);
        if (modelAndViewSearch != null) {
            return modelAndViewSearch;
        }
        Users user = securityCrypt.getUserBySession(request);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.formConfigEquipment);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                setWorkplaceByEquipment(equipment, request);
                String typeEquipment = request.getParameter(Parameters.typeEquipment);

                String error = dataVerification(request, equipment);
                if (!"".equals(error)) {
                    error += setAccounting1CByEquipment(equipment, request, false, user);
                } else {
                    error += setAccounting1CByEquipment(equipment, request, true, user);
                }
                String message = "";
                if (!"".equals(error)) {
                    modelAndView.addObject(Parameters.error, error);
                    modelAndView.addObject(Parameters.equipment, equipment);
                    modelAndView.addObject(Parameters.typeEquipment, typeEquipment);
                    if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
                        Computer computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);
                        setParameterComputer(computer, request, equipment);
                        modelAndView.addObject(Parameters.computer, computer);
                    }
                } else {
                    if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
                        Computer computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);

                        setParameterComputer(computer, request, equipment);
                        computer.setDeleted(false);
                        if (computer.getId() <= 0) {
                            equipmentManager.save(computer);
                            journalManager.save(new Journal(TypeEvent.ADD, TypeObject.COMPUTER, computer, user));
                            message = String.format("%s успешно добавлен", computer);
                        }
                        modelAndView.addObject(Parameters.computer, new Computer());
                    }
                    if (TypeEquipment.MONITOR.equals(typeEquipment)) {
                        Monitor monitor = (Monitor) equipment.getChildFromEquipment(TypeEquipment.MONITOR);
                        monitor.setDeleted(false);
                        equipmentManager.save(monitor);
                        journalManager.save(new Journal(TypeEvent.ADD, TypeObject.MONITOR, monitor, user));
                        message = String.format("%s успешно добавлен", monitor);
                    }
                    if (TypeEquipment.PRINTER.equals(typeEquipment)) {
                        Printer printer = (Printer) equipment.getChildFromEquipment(TypeEquipment.PRINTER);
                        printer.setDeleted(false);
                        equipmentManager.save(printer);
                        journalManager.save(new Journal(TypeEvent.ADD, TypeObject.PRINTER, printer, user));
                        message = String.format("%s успешно добавлен", printer);
                    }
                    if (TypeEquipment.SCANNER.equals(typeEquipment)) {
                        Scanner scanner = (Scanner) equipment.getChildFromEquipment(TypeEquipment.SCANNER);
                        scanner.setDeleted(false);
                        equipmentManager.save(scanner);
                        journalManager.save(new Journal(TypeEvent.ADD, TypeObject.SCANNER, scanner, user));
                        message = String.format("%s успешно добавлен", scanner);
                    }
                    if (TypeEquipment.MFD.equals(typeEquipment)) {
                        Mfd mfd = (Mfd) equipment.getChildFromEquipment(TypeEquipment.MFD);
                        mfd.setDeleted(false);
                        equipmentManager.save(mfd);
                        journalManager.save(new Journal(TypeEvent.ADD, TypeObject.MFD, mfd, user));
                        message = String.format("%s успешно добавлен", mfd);
                    }
                    if (TypeEquipment.UPS.equals(typeEquipment)) {
                        Ups ups = (Ups) equipment.getChildFromEquipment(TypeEquipment.UPS);
                        ups.setDeleted(false);
                        equipmentManager.save(ups);
                        journalManager.save(new Journal(TypeEvent.ADD, TypeObject.UPS, ups, user));
                        message = String.format("%s успешно добавлен", ups);
                    }

                    if (StringUtils.equals(error, "")) {
                        modelAndView.addObject(Parameters.message, message);
                        modelAndView.addObject(Parameters.equipment, new Equipment());
                        modelAndView.addObject(Parameters.closeWindow, true);
                    } else {
                        modelAndView.addObject(Parameters.equipment, equipment);
                    }

                    modelAndView.addObject(Parameters.typeEquipment, typeEquipment);
                }
            }

            return getModelAndView(request.getParameter(Parameters.redirect), modelAndView);
        } else {
            return new ModelAndView(Pages.login);
        }
    }

    private void setWorkplaceByEquipment(Equipment equipment, HttpServletRequest request) {
        Long workplaceId = request.getParameter(Parameters.workplaceId) != null ?
                Long.parseLong(request.getParameter(Parameters.workplaceId)) : -1;

        if (workplaceId > 0) {
            if (equipment.getWorkplace() == null || equipment.getWorkplace().getId() != workplaceId) {
                Workplace workplace = workplaceManager.getWorkplaceById(workplaceId, false);
                equipment.setWorkplace(workplace);
            }
        } else {
            equipment.setWorkplace(null);
        }
    }

    //private void setParameterComputer(Computer computer, HttpServletRequest request, Equipment equipment, boolean isNeedSave) {
    private void setParameterComputer(Computer computer, HttpServletRequest request, Equipment equipment) {
        computer.setIp(request.getParameter(Parameters.ip));
        computer.setUid(equipment.getUid());
        computer.setManufacturer(equipment.getManufacturer());
        computer.setModel(equipment.getModel());
        computer.setWorkplace(equipment.getWorkplace());
        computer.setAccounting1C(equipment.getAccounting1C());
        computer.setNetName(request.getParameter(Parameters.netName));
        computer.setComment(request.getParameter(Parameters.comment));

        addMotherboardToComputer(request, computer);
        addOperationSystemToComputer(request, computer);
        addProcessorListToComputer(request, computer);
        addRamListToComputer(request, computer);
        addVideoCardListToComputer(request, computer);
        addHardDriveListToComputer(request, computer);
        addVirtualMachineToComputer(request, computer);
    }

    private void addVirtualMachineToComputer(HttpServletRequest request, Computer computer) {
        int countVirtualMachine = Integer.parseInt(request.getParameter(Parameters.countVirtualMachine));
        for (int i = 1; i < countVirtualMachine; i++) {
            String ipVirtualMachineName = Parameters.ipVirtualMachine + i;
            String osTypeVirtualMachineName = Parameters.OsTypeVirtualMachine + i;
            String osVendorVirtualMachineName = Parameters.OsVendorVirtualMachine + i;
            String osVersionVirtualMachineName = Parameters.OsVersionVirtualMachine + i;
            String virtualMachineIdName = Parameters.idVirtualMachine + i;
            String virtualMachineDescription = Parameters.description + i;

            Long virtualMachineId = Long.parseLong(request.getParameter(virtualMachineIdName));
            VirtualMachine virtualMachine = null;
            if (virtualMachineId > 0) {
                for (VirtualMachine virtualMachine1 : computer.getVirtualMachineList()) {
                    if (virtualMachine1.getId() == virtualMachineId) {
                        virtualMachine = virtualMachine1;
                        break;
                    }
                }
            }
            if (virtualMachine == null) {
                continue;
            }

            virtualMachine.setIp(request.getParameter(ipVirtualMachineName) != null ?
                    request.getParameter(ipVirtualMachineName) : "");
            virtualMachine.setTypeOS(request.getParameter(osTypeVirtualMachineName) != null ?
                    TypeOS.valueOf(request.getParameter(osTypeVirtualMachineName)) : null);
            virtualMachine.setVendor(request.getParameter(osVendorVirtualMachineName) != null ?
                    request.getParameter(osVendorVirtualMachineName) : "");
            virtualMachine.setVersion(request.getParameter(osVersionVirtualMachineName) != null ?
                    request.getParameter(osVersionVirtualMachineName) : "");
            virtualMachine.setDescription(request.getParameter(virtualMachineDescription) != null ?
                    request.getParameter(virtualMachineDescription) : "");

            virtualMachineManager.save(virtualMachine);
        }
    }

    private void addHardDriveListToComputer(HttpServletRequest request, Computer computer) {
        int countHardDrive = Integer.parseInt(request.getParameter(Parameters.countHardDrive));
        for (int i = 1; i < countHardDrive; i++) {
            HardDrive hardDrive = null;
            String hardDriveModelName = Components.hardDriveModelInputText + i;
            String hardDriveSizeName = Components.hardDriveSizeInputText + i;
            String hardDriveIdName = Components.hardDriveIdHiddenText + i;

            Long hardDriveId = Long.parseLong(request.getParameter(hardDriveIdName));
            if (hardDriveId > 0) {
                for (HardDrive hardDrive1 : computer.getHardDriveList()) {
                    if (hardDrive1.getId() == hardDriveId) {
                        hardDrive = hardDrive1;
                        break;
                    }
                }
            }
            if (hardDrive == null) {
                continue;
            }

            hardDrive.setModel(request.getParameter(hardDriveModelName) != null ?
                    request.getParameter(hardDriveModelName) : "");

            hardDrive.setSize(request.getParameter(hardDriveSizeName) != null ?
                    request.getParameter(hardDriveSizeName) : "");

            hardDriveManager.save(hardDrive);
        }
    }

    private void addVideoCardListToComputer(HttpServletRequest request, Computer computer) {
        int countVideoCard = Integer.parseInt(request.getParameter(Parameters.countVideoCard));
        for (int i = 1; i < countVideoCard; i++) {
            VideoCard videoCard = null;
            String videoCardModelName = Components.videoCardModelInputText + i;
            String videoCardIdName = Components.videoCardIdHiddenText + i;

            Long videoCardId = Long.parseLong(request.getParameter(videoCardIdName));
            if (videoCardId > 0) {
                for (VideoCard videoCard1 : computer.getVideoCardList()) {
                    if (videoCard1.getId() == videoCardId) {
                        videoCard = videoCard1;
                        break;
                    }
                }
            }

            if (videoCard == null) {
                continue;
            }

            videoCard.setModel(request.getParameter(videoCardModelName) != null ?
                    request.getParameter(videoCardModelName) : "");

            videoCardManager.save(videoCard);
        }
    }

    private void addRamListToComputer(HttpServletRequest request, Computer computer) {
        int countRam = Integer.parseInt(request.getParameter(Parameters.countRam));
        for (int i = 1; i < countRam; i++) {
            Ram ram = new Ram();
            String ramModelName = Components.ramModelInputText + i;
            String ramTypeRamName = Components.ramTypeRamSelect + i;
            String ramAmountName = Components.ramAmountInputText + i;
            String ramFrequencyName = Components.ramFrequencyInputText + i;
            String ramDeviceLocatorName = Components.ramDeviceLocatorInputText + i;
            String ramIdName = Components.ramIdHiddenText + i;

            Long ramId = Long.parseLong(request.getParameter(ramIdName));
            if (ramId > 0) {
                for (Ram ram1 : computer.getRamList()) {
                    if (ram1.getId() == ramId) {
                        ram = ram1;
                        break;
                    }
                }
            }
            if (ram == null) {
                continue;
            }

            ram.setModel(request.getParameter(ramModelName) != null ?
                    request.getParameter(ramModelName) : "");

            ram.setTypeRam(request.getParameter(ramTypeRamName) != null ?
                    TypeRam.valueOf(request.getParameter(ramTypeRamName)) : null);

            ram.setAmount(request.getParameter(ramAmountName) != null ?
                    request.getParameter(ramAmountName) : "");

            ram.setFrequency(request.getParameter(ramFrequencyName) != null ?
                    request.getParameter(ramFrequencyName) : "");

            ram.setDeviceLocator(request.getParameter(ramDeviceLocatorName) != null ?
                    request.getParameter(ramDeviceLocatorName) : "");

            ramManager.save(ram);
        }
    }

    private void addMotherboardToComputer(HttpServletRequest request, Computer computer) {
        MotherBoard motherBoard = new MotherBoard();

        if (computer != null && computer.getMotherBoard() != null) {
            motherBoard = computer.getMotherBoard();
        } else {
            computer.setMotherBoard(motherBoard);
        }

        motherBoard.setManufacturer(request.getParameter(Components.motherboardManufacturer));
        motherBoard.setModel(request.getParameter(Components.motherboardModel));
    }

    private void addOperationSystemToComputer(HttpServletRequest request, Computer computer) {
        OperationSystem operationSystem = new OperationSystem();
        if (computer != null && computer.getOperationSystem() != null) {
            operationSystem = computer.getOperationSystem();
        } else {
            computer.setOperationSystem(operationSystem);
        }

        operationSystem.setTypeOS(TypeOS.valueOf(request.getParameter(Parameters.OsType)));
        operationSystem.setVendor(request.getParameter(Parameters.OsVendor));
        operationSystem.setVersion(request.getParameter(Parameters.OsVersion));
        operationSystem.setOSArchitecture(request.getParameter(Parameters.OSArchitecture));
    }

    //private void addProcessorListToComputer(HttpServletRequest request, Computer computer, boolean isNeedSave) {
    private void addProcessorListToComputer(HttpServletRequest request, Computer computer) {
        int countProcessor = Integer.parseInt(request.getParameter(Parameters.countProcessor));
        for (int i = 1; i < countProcessor; i++) {
            Processor processor = null;
            String modelName = Components.processorModelInputText + i;
            String numberCoreName = Components.processorNumberOfCoresInputText + i;
            String frequencyName = Components.processorFrequencyInputText + i;
            String socketName = Components.processorSocketInputText + i;
            String processorIdName = Components.processorIdHiddenText + i;

            Long processorId = Long.parseLong(request.getParameter(processorIdName));
            if (processorId > 0) {
                for (Processor processor1 : computer.getProcessorList()) {
                    if (processor1.getId() == processorId) {
                        processor = processor1;
                        break;
                    }
                }
            }

            if (processor == null) {
                continue;
            }

            processor.setModel(request.getParameter(modelName) != null ?
                    request.getParameter(modelName) : "");

            processor.setNumberOfCores(request.getParameter(numberCoreName) != null ?
                    request.getParameter(numberCoreName) : "");

            processor.setFrequency(request.getParameter(frequencyName) != null ?
                    request.getParameter(frequencyName) : "");

            processor.setSocket(request.getParameter(socketName) != null ?
                    request.getParameter(socketName) : "");

            //if (isNeedSave) {
            processorManager.save(processor);
            //}
        }
    }

    private String setAccounting1CByEquipment(Equipment equipment, HttpServletRequest request,
                                              boolean isNeedSave, Users user) {
        String accounting1CRadio = request.getParameter(Components.accounting1CRadio);
        Long selectAccounting1CId = request.getParameter(Components.accounting1CIdSelect) != null ? Long.parseLong(request.getParameter(Components.accounting1CIdSelect)) : -1;
        String accounting1CInventoryNumber = request.getParameter(Components.accounting1CInventoryNumberInputText);
        String accounting1CTitle = request.getParameter(Components.accounting1CTitleInputText);
        Long employeeId = request.getParameter(Components.accounting1CEmployeeIdInputText) != null ? Long.parseLong(request.getParameter(Components.accounting1CEmployeeIdInputText)) : -1;

        if (Parameters.accounting1CUseRecord.equals(accounting1CRadio)) {
            if (selectAccounting1CId < 0) {
                equipment.setAccounting1C(null);
            } else if (equipment.getAccounting1C() == null || equipment.getAccounting1C().getId() != selectAccounting1CId) {
                Accounting1C accounting1C = accounting1CManager.getAccounting1CById(selectAccounting1CId, false);
                equipment.setAccounting1C(accounting1C);
            }
        } else if (Parameters.accounting1CAddNewRecord.equals(accounting1CRadio)) {
            //Accounting1C accounting1CFromDB = accounting1CManager.getAccounting1CByInventoryNumber(accounting1CInventoryNumber);
            //if (accounting1CFromDB != null) {
            //    return String.format("%s уже существует", accounting1CInventoryNumber);
            //}
            Accounting1C accounting1C = new Accounting1C(accounting1CInventoryNumber, accounting1CTitle,
                    employeeManager.getEmployeeById(employeeId, false));
            if (isNeedSave) {
                accounting1CManager.save(accounting1C);
                journalManager.save(new Journal(TypeEvent.ADD, TypeObject.ACCOUNTING1C, accounting1C, user));
            }
            equipment.setAccounting1C(accounting1C);
        }
        return "";
    }

    private ModelAndView getModelAndView(@RequestParam(name = Parameters.redirect, required = false) String redirect, ModelAndView modelAndView) {
        List<Workplace> workplaceList = workplaceManager.getWorkplaceList();
        modelAndView.addObject(Parameters.workplaceList, workplaceList);

        List<Accounting1C> accounting1CList = accounting1CManager.getAccounting1cList(SortingAccounting1C.INVENTORY_NUMBER, "", FilterAccounting1C.ALL);
        modelAndView.addObject(Parameters.accounting1CList, accounting1CList);

        List<Employee> employeeList = employeeManager.getEmployeeList();
        modelAndView.addObject(Parameters.employeeList, employeeList);

        if (redirect == null) {
            redirect = "";
        }
        modelAndView.addObject(Parameters.redirect, redirect);

        return modelAndView;
    }

    @PostMapping(Pages.updateEquipmentPost)
    public ModelAndView updateEquipment(@ModelAttribute(Parameters.equipment) Equipment equipmentRequest,
                                        HttpServletRequest request) {
        //equipment.setDeleted(false);
        Equipment equipment = getEquipment(equipmentRequest);

        ModelAndView modelAndViewSearch = searchPage(equipment, request);
        if (modelAndViewSearch != null) {
            return modelAndViewSearch;
        }
        Users user = securityCrypt.getUserBySession(request);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.formConfigEquipment);

            String typeEquipment = request.getParameter(Parameters.typeEquipment);
            String redirect = request.getParameter(Parameters.redirect);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                List<Journal> journalList = new ArrayList<>();
                String error = dataVerification(request, equipment);
                if (!"".equals(error)) {
                    error += setAccounting1CByEquipment(equipment, request, false, user);
                } else {
                    error += setAccounting1CByEquipment(equipment, request, true, user);
                }

                if (!"".equals(error)) {
                    modelAndView.addObject(Parameters.error, error);
                    modelAndView.addObject(Parameters.equipment, equipment);
                    modelAndView.addObject(Parameters.typeEquipment, typeEquipment);
                    if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
                        Computer computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);
                        setParameterComputer(computer, request, equipment);
                        modelAndView.addObject(Parameters.computer, computer);
                    }
                } else {
                    setWorkplaceByEquipment(equipment, request);

                    if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
                        Computer computerOld = equipmentManager.getComputerById(equipment.getId(), false);

                        Computer computer = equipmentManager.getComputerById(equipment.getId(), false);
                        setParameterComputer(computer, request, equipment);
                        computer.setDeleted(false);
                        equipmentManager.save(computer);
                        computer = equipmentManager.getComputerById(equipment.getId(), false);

                        journalManager.saveChangeEquipment(computerOld, computer, typeEquipment, user);
                    } else {
                        Equipment equipmentOld = equipmentManager.getEquipmentById(equipment.getId(), false);
                        equipment.setDeleted(false);
                        equipmentManager.save(equipment);
                        journalManager.saveChangeEquipment(equipmentOld, equipment, typeEquipment, user);
                    }
                    //modelAndView.setViewName("redirect:/" + redirect);
                    //}

                    modelAndView.addObject(Parameters.closeWindow, true);
                }
            }
            return getModelAndView(redirect, modelAndView);
        } else {
            return new ModelAndView(Pages.login);
        }
    }

    @GetMapping(Pages.deleteEquipmentPost)
    public ModelAndView deleteEquipment(@RequestParam(name = Parameters.id) Long id,
                                        @RequestParam(value = Parameters.typeEquipment) String typeEquipment,
                                        @RequestParam(name = Parameters.redirect) String redirect,
                                        HttpServletRequest request) {
        Users user = securityCrypt.getUserBySession(request);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            //ModelAndView modelAndView = securityCrypt.verifyUser(request, "redirect:/" + redirect);
            ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.formConfigEquipment);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                Equipment equipment = equipmentManager.getEquipmentById(id, false);
                equipmentManager.delete(equipment);
                journalManager.save(new Journal(TypeEvent.DELETE,
                        journalManager.getTypeObjectFromTypeEquipment(typeEquipment), equipment, user));

                modelAndView.addObject(Parameters.closeWindow, true);
            }

            modelAndView.addObject(Parameters.typeEquipment, typeEquipment);
            return modelAndView;
        } else {
            return new ModelAndView(Pages.login);
        }

    }


}
