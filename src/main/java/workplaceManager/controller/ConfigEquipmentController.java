package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.*;
import workplaceManager.db.TypeEvent;
import workplaceManager.db.TypeObject;
import workplaceManager.db.domain.*;
import workplaceManager.db.domain.components.*;
import workplaceManager.db.service.*;

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

    @GetMapping(Pages.addUpdateEquipment)
    public ModelAndView getFormAddUpdateEquipment(@RequestParam(name = Parameters.id, required = false) Long id,
                                                  @RequestParam(name = Parameters.typeEquipment) String typeEquipment,
                                                  @RequestParam(name = Parameters.token) String token,
                                                  @RequestParam(name = Parameters.redirect, required = false) String redirect) {
        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigEquipment);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            Equipment equipment = new Equipment();
            if (id != null && id > 0) {
                equipment = equipmentManager.getEquipmentById(id);
            }

            modelAndView.addObject(Parameters.equipment, equipment);
            modelAndView.addObject(Parameters.typeEquipment, typeEquipment);
            if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
                Computer computer = equipmentManager.getComputerById(id);
                modelAndView.addObject(Parameters.computer, computer == null ? new Computer() : computer);
            }
        }
        return getModelAndView(redirect, modelAndView);
    }

    private ModelAndView readConfigComputer(@ModelAttribute(Parameters.equipment) Equipment equipment,
                                            @RequestParam(name = Parameters.token) String token,
                                            HttpServletRequest request) {
        Users user = securityCrypt.getUserByToken(token);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigEquipment);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                setWorkplaceByEquipment(equipment, request);

                setAccounting1CByEquipment(equipment, request, false);

                Computer computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);

                setParameterComputer(computer, request, equipment, false);

                String message = "";
                if (TypeOS.windows.toString().equals(request.getParameter(Parameters.typeOS))) {
                    try {
                        WMI.getAllConfigFormComputerWindows(computer);
                    } catch (Exception exception) {
                        message = exception.getMessage();
                    }
                }

                //equipmentManager.save(computer);
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
                                              @RequestParam(name = Parameters.token) String token,
                                              HttpServletRequest request,
                                              TypeComponentsComputer typeComponentsComputer) {
        Users user = securityCrypt.getUserByToken(token);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigEquipment);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                setWorkplaceByEquipment(equipment, request);

                setAccounting1CByEquipment(equipment, request, false);

                Computer computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);

                setParameterComputer(computer, request, equipment, false);

                if (TypeComponentsComputer.processor.equals(typeComponentsComputer)) {
                    computer.getProcessorList().add(new Processor());
                }
                if (TypeComponentsComputer.ram.equals(typeComponentsComputer)) {
                    computer.getRamList().add(new Ram());
                }
                if (TypeComponentsComputer.videoCard.equals(typeComponentsComputer)) {
                    computer.getVideoCardList().add(new VideoCard());
                }
                if (TypeComponentsComputer.hardDrive.equals(typeComponentsComputer)) {
                    computer.getHardDriveList().add(new HardDrive());
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
                                                  @RequestParam(name = Parameters.token) String token,
                                                  HttpServletRequest request,
                                                  TypeComponentsComputer typeComponentsComputer) {
        Users user = securityCrypt.getUserByToken(token);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigEquipment);

            if (!modelAndView.getViewName().equals(Pages.login)) {

                setWorkplaceByEquipment(equipment, request);

                setAccounting1CByEquipment(equipment, request, false);

                Computer computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);

                setParameterComputer(computer, request, equipment, false);

                if (TypeComponentsComputer.processor.equals(typeComponentsComputer)) {
                    int countProcessor = Integer.parseInt(request.getParameter(Parameters.countProcessor));
                    for (int numberProcessor = 1; numberProcessor <= countProcessor; numberProcessor++) {
                        String buttonDeleteProcessor = Components.buttonDeleteProcessor + numberProcessor;
                        if (request.getParameter(buttonDeleteProcessor) != null) {
                            computer.getProcessorList().remove(numberProcessor - 1);
                        }
                    }
                }
                if (TypeComponentsComputer.ram.equals(typeComponentsComputer)) {
                    int countRam = Integer.parseInt(request.getParameter(Parameters.countRam));
                    for (int numberRam = 1; numberRam <= countRam; numberRam++) {
                        String buttonDeleteRam = Components.buttonDeleteRam + numberRam;
                        if (request.getParameter(buttonDeleteRam) != null) {
                            computer.getRamList().remove(numberRam - 1);
                        }
                    }
                }
                if (TypeComponentsComputer.videoCard.equals(typeComponentsComputer)) {
                    int countVideoCard = Integer.parseInt(request.getParameter(Parameters.countVideoCard));
                    for (int i = 1; i <= countVideoCard; i++) {
                        String buttonDeleteVideoCard = Components.buttonDeleteVideoCard + i;
                        if (request.getParameter(buttonDeleteVideoCard) != null) {
                            computer.getVideoCardList().remove(i - 1);
                        }
                    }
                }
                if (TypeComponentsComputer.hardDrive.equals(typeComponentsComputer)) {
                    int countHardDrive = Integer.parseInt(request.getParameter(Parameters.countHardDrive));
                    for (int i = 1; i <= countHardDrive; i++) {
                        String buttonDeleteHardDrive = Components.buttonDeleteHardDrive + i;
                        if (request.getParameter(buttonDeleteHardDrive) != null) {
                            computer.getHardDriveList().remove(i - 1);
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

    private ModelAndView searchPage(@ModelAttribute(Parameters.equipment) Equipment equipment,
                                    @RequestParam(name = Parameters.token) String token,
                                    HttpServletRequest request) {
        if (request.getParameter(Components.buttonReadConfigComputer) != null) {
            return readConfigComputer(equipment, token, request);
        }
        if (request.getParameter(Components.buttonAddProcessor) != null) {
            return addComponentComputer(equipment, token, request, TypeComponentsComputer.processor);
        }
        int countProcessor = Integer.parseInt(request.getParameter(Parameters.countProcessor));
        for (int i = 1; i <= countProcessor; i++) {
            String buttonDeleteProcessor = Components.buttonDeleteProcessor + i;
            if (request.getParameter(buttonDeleteProcessor) != null) {
                return deleteComponentsComputer(equipment, token, request, TypeComponentsComputer.processor);
            }
        }
        if (request.getParameter(Components.buttonAddRam) != null) {
            return addComponentComputer(equipment, token, request, TypeComponentsComputer.ram);
        }
        int countRam = Integer.parseInt(request.getParameter(Parameters.countRam));
        for (int i = 1; i <= countRam; i++) {
            String buttonDeleteRam = Components.buttonDeleteRam + i;
            if (request.getParameter(buttonDeleteRam) != null) {
                return deleteComponentsComputer(equipment, token, request, TypeComponentsComputer.ram);
            }
        }
        if (request.getParameter(Components.buttonAddVideoCard) != null) {
            return addComponentComputer(equipment, token, request, TypeComponentsComputer.videoCard);
        }
        int countVideCard = Integer.parseInt(request.getParameter(Parameters.countVideoCard));
        for (int i = 1; i <= countVideCard; i++) {
            String buttonDeleteVideoCard = Components.buttonDeleteVideoCard + i;
            if (request.getParameter(buttonDeleteVideoCard) != null) {
                return deleteComponentsComputer(equipment, token, request, TypeComponentsComputer.videoCard);
            }
        }
        if (request.getParameter(Components.buttonAddHardDrive) != null) {
            return addComponentComputer(equipment, token, request, TypeComponentsComputer.hardDrive);
        }
        int countHardDrive = Integer.parseInt(request.getParameter(Parameters.countHardDrive));
        for (int i = 1; i <= countHardDrive; i++) {
            String buttonDeleteHardDrive = Components.buttonDeleteHardDrive + i;
            if (request.getParameter(buttonDeleteHardDrive) != null) {
                return deleteComponentsComputer(equipment, token, request, TypeComponentsComputer.hardDrive);
            }
        }
        return null;
    }

    private String dataVerification(HttpServletRequest request, Equipment equipment) {
        String typeEquipment = request.getParameter(Parameters.typeEquipment);
        String error = "";
        if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
            Computer computerFromDb = equipmentManager.getComputerByIp(request.getParameter(Parameters.ip));
            if (computerFromDb != null && computerFromDb.getId() != equipment.getId()) {
                error = request.getParameter(Parameters.ip) + " уже существует ";
            }
        }

        Equipment equipmentFromDb = equipmentManager.getEquipmentByUid(equipment.getUid());
        if (equipmentFromDb != null && equipmentFromDb.getId() != equipment.getId()) {
            error += String.format("%s уже существует ", equipment.getUid());
        }
        return error;
    }

    @PostMapping(Pages.addEquipmentPost)
    public ModelAndView addEquipment(@ModelAttribute(Parameters.equipment) Equipment equipment,
                                     @RequestParam(name = Parameters.token) String token,
                                     HttpServletRequest request) {
        ModelAndView modelAndViewSearch = searchPage(equipment, token, request);
        if (modelAndViewSearch != null) {
            return modelAndViewSearch;
        }
        Users user = securityCrypt.getUserByToken(token);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigEquipment);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                setWorkplaceByEquipment(equipment, request);
                String typeEquipment = request.getParameter(Parameters.typeEquipment);

                String error = dataVerification(request, equipment);
                if (!"".equals(error)) {
                    error += setAccounting1CByEquipment(equipment, request, false);
                } else {
                    error += setAccounting1CByEquipment(equipment, request, true);
                }

                if (!"".equals(error)) {
                    modelAndView.addObject(Parameters.error, error);
                    modelAndView.addObject(Parameters.equipment, equipment);
                    modelAndView.addObject(Parameters.typeEquipment, typeEquipment);
                    if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
                        Computer computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);
                        setParameterComputer(computer, request, equipment, false);
                        modelAndView.addObject(Parameters.computer, computer);
                    }
                } else {
                    if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
                        Computer computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);

                        setParameterComputer(computer, request, equipment, true);
                        equipmentManager.save(computer);
                        journalManager.save(new Journal(TypeEvent.ADD, TypeObject.COMPUTER, computer));
                        modelAndView.addObject(Parameters.computer, new Computer());
                    }
                    if (TypeEquipment.MONITOR.equals(typeEquipment)) {
                        Monitor monitor = (Monitor) equipment.getChildFromEquipment(TypeEquipment.MONITOR);
                        equipmentManager.save(monitor);
                        journalManager.save(new Journal(TypeEvent.ADD, TypeObject.MONITOR, monitor));
                    }
                    if (TypeEquipment.PRINTER.equals(typeEquipment)) {
                        Printer printer = (Printer) equipment.getChildFromEquipment(TypeEquipment.PRINTER);
                        equipmentManager.save(printer);
                        journalManager.save(new Journal(TypeEvent.ADD, TypeObject.PRINTER, printer));
                    }
                    if (TypeEquipment.SCANNER.equals(typeEquipment)) {
                        Scanner scanner = (Scanner) equipment.getChildFromEquipment(TypeEquipment.SCANNER);
                        equipmentManager.save(scanner);
                        journalManager.save(new Journal(TypeEvent.ADD, TypeObject.SCANNER, scanner));
                    }
                    if (TypeEquipment.MFD.equals(typeEquipment)) {
                        Mfd mfd = (Mfd) equipment.getChildFromEquipment(TypeEquipment.MFD);
                        equipmentManager.save(mfd);
                        journalManager.save(new Journal(TypeEvent.ADD, TypeObject.MFD, mfd));
                    }
                    if (TypeEquipment.UPS.equals(typeEquipment)) {
                        Ups ups = (Ups) equipment.getChildFromEquipment(TypeEquipment.UPS);
                        equipmentManager.save(ups);
                        journalManager.save(new Journal(TypeEvent.ADD, TypeObject.UPS, ups));
                    }

                    if (error == "") {
                        modelAndView.addObject(Parameters.message, "Успешно");
                        modelAndView.addObject(Parameters.equipment, new Equipment());
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

    private void setWorkplaceByEquipment(@ModelAttribute(Parameters.equipment) Equipment equipment,
                                         HttpServletRequest request) {
        Long workplaceId = request.getParameter(Parameters.workplaceId) != null ? Long.parseLong(request.getParameter(Parameters.workplaceId)) : -1;

        if (equipment != null && workplaceId > 0) {
            Workplace workplace = workplaceManager.getWorkplaceById(workplaceId);
            equipment.setWorkplace(workplace);
        } else {
            equipment.setWorkplace(null);
        }
    }

    private void setParameterComputer(Computer computer, HttpServletRequest request, Equipment equipment, boolean isNeedSave) {
        computer.setIp(request.getParameter(Parameters.ip));
        computer.setUid(equipment.getUid());
        computer.setManufacturer(equipment.getManufacturer());
        computer.setModel(equipment.getModel());
        computer.setWorkplace(equipment.getWorkplace());
        computer.setAccounting1C(equipment.getAccounting1C());
        computer.setNetName(request.getParameter(Parameters.netName));

        addMotherboardToComputer(request, computer);
        addOperationSystemToComputer(request, computer);
        addProcessorListToComputer(request, computer, isNeedSave);
        addRamListToComputer(request, computer, isNeedSave);
        addVideoCardListToComputer(request, computer, isNeedSave);
        addHardDriveListToComputer(request, computer, isNeedSave);
    }

    private void addHardDriveListToComputer(HttpServletRequest request, Computer computer, boolean isNeedSave) {
        if (isNeedSave) {
            hardDriveManager.deleteHardDriveListForComputer(computer);
        }

        List<HardDrive> hardDriveList = new ArrayList<>();
        int countHardDrive = Integer.parseInt(request.getParameter(Parameters.countHardDrive));
        for (int i = 1; i < countHardDrive; i++) {
            HardDrive hardDrive = new HardDrive();
            String hardDriveModelName = Components.hardDriveModelInputText + i;
            String hardDriveSizeName = Components.hardDriveSizeInputText + i;

            boolean isAllNull = true;

            if (request.getParameter(hardDriveModelName) != null) {
                hardDrive.setModel(request.getParameter(hardDriveModelName));
                isAllNull = false;
            }
            if (request.getParameter(hardDriveSizeName) != null) {
                hardDrive.setSize(request.getParameter(hardDriveSizeName));
                isAllNull = false;
            }

            if (isAllNull) {
                continue;
            }

            hardDrive.setComputer(computer);

            hardDriveList.add(hardDrive);

            if (isNeedSave) {
                if (!HardDrive.isEmpty(hardDrive)) {
                    hardDriveManager.save(hardDrive);
                }
            }
        }
        computer.setHardDriveList(hardDriveList);
    }

    private void addVideoCardListToComputer(HttpServletRequest request, Computer computer, boolean isNeedSave) {
        if (isNeedSave) {
            videoCardManager.deleteVideoCardListForComputer(computer);
        }

        List<VideoCard> videoCardList = new ArrayList<>();
        int countVideoCard = Integer.parseInt(request.getParameter(Parameters.countVideoCard));
        for (int i = 1; i < countVideoCard; i++) {
            VideoCard videoCard = new VideoCard();
            String videoCardModelName = Components.videoCardModelInputText + i;

            if (request.getParameter(videoCardModelName) != null) {
                videoCard.setModel(request.getParameter(videoCardModelName));
            } else {
                continue;
            }

            videoCard.setComputer(computer);

            videoCardList.add(videoCard);

            if (isNeedSave) {
                if (!VideoCard.isEmpty(videoCard)) {
                    videoCardManager.save(videoCard);
                }
            }
        }
        computer.setVideoCardList(videoCardList);
    }

    private void addRamListToComputer(HttpServletRequest request, Computer computer, boolean isNeedSave) {
        if (isNeedSave) {
            ramManager.deleteRamListForComputer(computer);
        }

        List<Ram> ramList = new ArrayList<>();
        int countRam = Integer.parseInt(request.getParameter(Parameters.countRam));
        System.out.println("countRam=" + countRam);
        for (int i = 1; i < countRam; i++) {
            Ram ram = new Ram();
            String ramModelName = Components.ramModelInputText + i;
            String ramTypeRamName = Components.ramTypeRamSelect + i;
            String ramAmountName = Components.ramAmountInputText + i;
            String ramFrequencyName = Components.ramFrequencyInputText + i;
            String ramDeviceLocatorName = Components.ramDeviceLocatorInputText + i;
            boolean isAllNull = true;

            if (request.getParameter(ramModelName) != null) {
                System.out.println("if (request.getParameter(ramModelName) != null) {");
                ram.setModel(request.getParameter(ramModelName));
                isAllNull = false;
            }
            if (request.getParameter(ramTypeRamName) != null) {
                System.out.println("if (request.getParameter(ramTypeRamName) != null) {");
                ram.setTypeRam(TypeRam.valueOf(request.getParameter(ramTypeRamName)));
                isAllNull = false;
            }
            if (request.getParameter(ramAmountName) != null) {
                System.out.println("if (request.getParameter(ramAmountName) != null) {");
                ram.setAmount(request.getParameter(ramAmountName));
                isAllNull = false;
            }
            if (request.getParameter(ramFrequencyName) != null) {
                System.out.println("if (request.getParameter(ramFrequencyName) != null) {");
                ram.setFrequency(request.getParameter(ramFrequencyName));
                isAllNull = false;
            }
            if (request.getParameter(ramDeviceLocatorName) != null) {
                System.out.println("if (request.getParameter(ramDeviceLocatorName) != null) {");
                ram.setDeviceLocator(request.getParameter(ramDeviceLocatorName));
                isAllNull = false;
            }

            if (isAllNull) {
                continue;
            }
            ram.setComputer(computer);

            ramList.add(ram);

            if (isNeedSave) {
                System.out.println("if (isNeedSave) {");
                if (!Ram.isEmpty(ram)) {
                    ramManager.save(ram);
                    System.out.println("ramManager.save(ram);" + ram);
                }
            }
        }
        computer.setRamList(ramList);
    }

    private void addMotherboardToComputer(HttpServletRequest request, Computer computer) {
        MotherBoard motherBoard = new MotherBoard();
        if (computer != null && computer.getMotherBoard() != null) {
            motherBoard = computer.getMotherBoard();
        }

        motherBoard.setManufacturer(request.getParameter(Components.motherboardManufacturer));
        motherBoard.setModel(request.getParameter(Components.motherboardModel));

        computer.setMotherBoard(motherBoard);
    }

    private void addOperationSystemToComputer(HttpServletRequest request, Computer computer) {
        OperationSystem operationSystem = new OperationSystem();
        if (computer != null && computer.getOperationSystem() != null) {
            operationSystem = computer.getOperationSystem();
        }

        operationSystem.setTypeOS(TypeOS.valueOf(request.getParameter(Parameters.OsType)));
        operationSystem.setVendor(request.getParameter(Parameters.OsVendor));
        operationSystem.setVersion(request.getParameter(Parameters.OsVersion));

        computer.setOperationSystem(operationSystem);
    }

    private void addProcessorListToComputer(HttpServletRequest request, Computer computer, boolean isNeedSave) {
        if (isNeedSave) {
            processorManager.deleteProcessorListForComputer(computer);
        }

        List<Processor> processorList = new ArrayList<>();
        int countProcessor = Integer.parseInt(request.getParameter(Parameters.countProcessor));
        for (int i = 1; i < countProcessor; i++) {
            Processor processor = new Processor();
            String modelName = Components.processorModelInputText + i;
            String numberCoreName = Components.processorNumberOfCoresInputText + i;
            String frequencyName = Components.processorFrequencyInputText + i;
            String socketName = Components.processorSocketInputText + i;
            boolean isAllNull = true;

            if (request.getParameter(modelName) != null) {
                processor.setModel(request.getParameter(modelName));
                isAllNull = false;
            }
            if (request.getParameter(numberCoreName) != null) {
                processor.setNumberOfCores(request.getParameter(numberCoreName));
                isAllNull = false;
            }
            if (request.getParameter(frequencyName) != null) {
                processor.setFrequency(request.getParameter(frequencyName));
                isAllNull = false;
            }
            if (request.getParameter(socketName) != null) {
                processor.setSocket(request.getParameter(socketName));
                isAllNull = false;
            }

            if (isAllNull) {
                break;
            }
            processor.setComputer(computer);

            processorList.add(processor);

            if (isNeedSave) {
                if (!Processor.isEmpty(processor)) {
                    processorManager.save(processor);
                }
            }
        }

        computer.setProcessorList(processorList);
    }

    private String setAccounting1CByEquipment(Equipment equipment, HttpServletRequest request, boolean isNeedSave) {
        String accounting1CRadio = request.getParameter(Components.accounting1CRadio);
        Long selectAccounting1CId = request.getParameter(Components.accounting1CIdSelect) != null ? Long.parseLong(request.getParameter(Components.accounting1CIdSelect)) : -1;
        String accounting1CInventoryNumber = request.getParameter(Components.accounting1CInventoryNumberInputText);
        String accounting1CTitle = request.getParameter(Components.accounting1CTitleInputText);
        Long employeeId = request.getParameter(Components.accounting1CEmployeeIdInputText) != null ? Long.parseLong(request.getParameter(Components.accounting1CEmployeeIdInputText)) : -1;

        if (Parameters.accounting1CUseRecord.equals(accounting1CRadio)) {
            if (selectAccounting1CId < 0) {
                equipment.setAccounting1C(null);
            } else {
                Accounting1C accounting1C = accounting1CManager.getAccounting1CById(selectAccounting1CId);
                equipment.setAccounting1C(accounting1C);
            }
        } else if (Parameters.accounting1CAddNewRecord.equals(accounting1CRadio)) {
            Accounting1C accounting1CFromDB = accounting1CManager.getAccounting1CByInventoryNumber(accounting1CInventoryNumber);
            if (accounting1CFromDB != null) {
                return String.format("%s уже существует", accounting1CInventoryNumber);
            }
            Accounting1C accounting1C = new Accounting1C(accounting1CInventoryNumber, accounting1CTitle,
                    employeeManager.getEmployeeById(employeeId));
            if (isNeedSave) {
                accounting1CManager.save(accounting1C);
                journalManager.save(new Journal(TypeEvent.ADD, TypeObject.ACCOUNTING1C, accounting1C));
            }
            equipment.setAccounting1C(accounting1C);
        }
        return "";
    }

    private ModelAndView getModelAndView(@RequestParam(name = Parameters.redirect, required = false) String redirect, ModelAndView modelAndView) {
        List<Workplace> workplaceList = workplaceManager.getWorkplaceList();
        modelAndView.addObject(Parameters.workplaceList, workplaceList);

        List<Accounting1C> accounting1CList = accounting1CManager.getAccounting1cList();
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
    public ModelAndView updateEquipment(@ModelAttribute(Parameters.equipment) Equipment equipment,
                                        @RequestParam(name = Parameters.token) String token,
                                        HttpServletRequest request) {
        ModelAndView modelAndViewSearch = searchPage(equipment, token, request);
        if (modelAndViewSearch != null) {
            return modelAndViewSearch;
        }
        Users user = securityCrypt.getUserByToken(token);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigEquipment);

            String typeEquipment = request.getParameter(Parameters.typeEquipment);
            String redirect = request.getParameter(Parameters.redirect);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                List<Journal> journalList = new ArrayList<>();
                String error = dataVerification(request, equipment);
                if (!"".equals(error)) {
                    error += setAccounting1CByEquipment(equipment, request, false);
                } else {
                    error += setAccounting1CByEquipment(equipment, request, true);
                }

                if (!"".equals(error)) {
                    modelAndView.addObject(Parameters.error, error);
                    modelAndView.addObject(Parameters.equipment, equipment);
                    modelAndView.addObject(Parameters.typeEquipment, typeEquipment);
                    if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
                        Computer computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);
                        setParameterComputer(computer, request, equipment, false);
                        modelAndView.addObject(Parameters.computer, computer);
                    }
                } else {
                    setWorkplaceByEquipment(equipment, request);

                    if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
                        Computer computerOld = equipmentManager.getComputerById(equipment.getId());
                        Computer computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);
                        setParameterComputer(computer, request, equipment, true);

                        equipmentManager.save(computer);
                        journalManager.saveChangeEquipment(computerOld, computer, typeEquipment);
                    } else {
                        Equipment equipmentOld = equipmentManager.getEquipmentById(equipment.getId());
                        equipmentManager.save(equipment);
                        journalManager.saveChangeEquipment(equipmentOld, equipment, typeEquipment);
                    }
                    modelAndView.setViewName("redirect:/" + redirect);
                    //}
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
                                        @RequestParam(name = Parameters.token) String token) {
        Users user = securityCrypt.getUserByToken(token);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(token, "redirect:/" + redirect);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                Equipment equipment = equipmentManager.getEquipmentById(id);
                equipmentManager.delete(equipment);
                journalManager.save(new Journal(TypeEvent.DELETE,
                        journalManager.getTypeObjectFromTypeEquipment(typeEquipment), equipment));
            }

            modelAndView.addObject(Parameters.typeEquipment, typeEquipment);
            return modelAndView;
        } else {
            return new ModelAndView(Pages.login);
        }

    }


}
