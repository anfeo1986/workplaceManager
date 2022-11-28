package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.*;
import workplaceManager.db.domain.*;
import workplaceManager.db.domain.components.MotherBoard;
import workplaceManager.db.domain.components.Processor;
import workplaceManager.db.domain.components.Ram;
import workplaceManager.db.domain.components.TypeRam;
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

                if(TypeComponentsComputer.processor.equals(typeComponentsComputer)) {
                    Processor processor = new Processor();
                    computer.getProcessorList().add(processor);
                }
                if(TypeComponentsComputer.ram.equals(typeComponentsComputer)) {
                    Ram ram = new Ram();
                    computer.getRamList().add(ram);
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

   /* private ModelAndView deleteProcessor(@ModelAttribute(Parameters.equipment) Equipment equipment,
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

                int countProcessor = Integer.parseInt(request.getParameter(Parameters.countProcessor));
                for (int numberProcessor = 1; numberProcessor <= countProcessor; numberProcessor++) {
                    String buttonDeleteProcessor = Components.buttonDeleteProcessor + numberProcessor;
                    if (request.getParameter(buttonDeleteProcessor) != null) {
                        computer.getProcessorList().remove(numberProcessor - 1);
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
    }*/

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
                        modelAndView.addObject(Parameters.computer, new Computer());
                    }
                    if (TypeEquipment.MONITOR.equals(typeEquipment)) {
                        equipmentManager.save((Monitor) equipment.getChildFromEquipment(TypeEquipment.MONITOR));
                    }
                    if (TypeEquipment.PRINTER.equals(typeEquipment)) {
                        equipmentManager.save((Printer) equipment.getChildFromEquipment(TypeEquipment.PRINTER));
                    }
                    if (TypeEquipment.SCANNER.equals(typeEquipment)) {
                        equipmentManager.save((Scanner) equipment.getChildFromEquipment(TypeEquipment.SCANNER));
                    }
                    if (TypeEquipment.MFD.equals(typeEquipment)) {
                        equipmentManager.save((Mfd) equipment.getChildFromEquipment(TypeEquipment.MFD));
                    }
                    if (TypeEquipment.UPS.equals(typeEquipment)) {
                        equipmentManager.save((Ups) equipment.getChildFromEquipment(TypeEquipment.UPS));
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

    private void setWorkplaceByEquipment(@ModelAttribute(Parameters.equipment) Equipment equipment, HttpServletRequest request) {
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
    }

    private void addRamListToComputer(HttpServletRequest request, Computer computer, boolean isNeedSave) {
        if (isNeedSave) {
            ramManager.deleteRamListForComputer(computer);
        }

        List<Ram> ramList = new ArrayList<>();
        int countRam = Integer.parseInt(request.getParameter(Parameters.countRam));
        System.out.println("countRam="+countRam);
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
                    System.out.println("ramManager.save(ram);"+ram);
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

                    //Equipment equipmentFromDb = equipmentManager.getEquipmentByUid(equipment.getUid());
                    //if (equipmentFromDb != null && equipmentFromDb.getId() != equipment.getId()) {
                    //    modelAndView.addObject(Parameters.error, String.format("%s уже существует", equipment.getUid()));
                    //    modelAndView.addObject(Parameters.equipment, equipment);
                    //    modelAndView.addObject(Parameters.typeEquipment, typeEquipment);
                    //} else {
                    setWorkplaceByEquipment(equipment, request);

                    //String error = setAccounting1CByEquipment(equipment, request, true);
                    // if (!"".equals(error)) {
                    //     modelAndView.addObject(Parameters.error, error);
                    //     modelAndView.addObject(Parameters.equipment, equipment);
                    //    modelAndView.addObject(Parameters.typeEquipment, typeEquipment);
                    //} else {
                    if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
                        Computer computer = equipmentManager.getComputerById(equipment.getId());
                        setParameterComputer(computer, request, equipment, true);
                        equipmentManager.save(computer);
                    } else {
                        equipmentManager.save(equipment);
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
            }

            modelAndView.addObject(Parameters.typeEquipment, typeEquipment);
            return modelAndView;
        } else {
            return new ModelAndView(Pages.login);
        }

    }
}
