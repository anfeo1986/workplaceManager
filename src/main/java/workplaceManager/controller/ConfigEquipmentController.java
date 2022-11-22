package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.Pages;
import workplaceManager.SecurityCrypt;
import workplaceManager.db.domain.*;
import workplaceManager.db.domain.components.MotherBoard;
import workplaceManager.db.domain.components.TypeRam;
import workplaceManager.db.service.Accounting1CManager;
import workplaceManager.db.service.EmployeeManager;
import workplaceManager.db.service.EquipmentManager;
import workplaceManager.db.service.WorkplaceManager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
//@RequestMapping("/config/equipment")
public class ConfigEquipmentController {

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
    public void setSecurity(SecurityCrypt securityCrypt) {
        this.securityCrypt = securityCrypt;
    }

    @GetMapping(Pages.addUpdateEquipment)
    public ModelAndView getFormAddUpdateEquipment(@RequestParam(name = "id", required = false) Long id,
                                                  @RequestParam(name = "typeEquipment") String typeEquipment,
                                                  @RequestParam(name = "token") String token,
                                                  @RequestParam(name = "redirect", required = false) String redirect) {
        //ModelAndView modelAndView = new ModelAndView("/config/equipment");
        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigEquipment);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            Equipment equipment = new Equipment();
            if (id != null && id > 0) {
                equipment = equipmentManager.getEquipmentById(id);
            }

            modelAndView.addObject("equipment", equipment);
            modelAndView.addObject("typeEquipment", typeEquipment);
            if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
                Computer computer = equipmentManager.getComputerById(id);
                modelAndView.addObject("computer", computer == null ? new Computer() : computer);
            }
        }
        return getModelAndView(redirect, modelAndView);
    }

    @PostMapping(Pages.addEquipmentPost)
    public ModelAndView addEquipment(@ModelAttribute("equipment") Equipment equipment,
                                     @RequestParam(name = "token") String token,
                                     HttpServletRequest request) {
        //ModelAndView modelAndView = new ModelAndView("/config/equipment");
        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigEquipment);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            Long workplaceId = request.getParameter("workplace_id") != null ? Long.parseLong(request.getParameter("workplace_id")) : -1;
            String accounting1CRadio = request.getParameter("accounting1CRadio");
            Long selectAccounting1CId = request.getParameter("selectAccounting1CId") != null ? Long.parseLong(request.getParameter("selectAccounting1CId")) : -1;
            String accounting1CInventoryNumber = request.getParameter("accounting1CInventoryNumber");
            String accounting1CTitle = request.getParameter("accounting1CTitle");
            Long employeeId = request.getParameter("employeeId") != null ? Long.parseLong(request.getParameter("employeeId")) : -1;
            String typeEquipment = request.getParameter("typeEquipment");

            if (equipment != null && workplaceId > 0) {
                Workplace workplace = workplaceManager.getWorkplaceById(workplaceId);
                equipment.setWorkplace(workplace);
            }

            String error = setAccounting1CByEquipment(equipment, accounting1CRadio, selectAccounting1CId, accounting1CInventoryNumber,
                    accounting1CTitle, employeeId);

            Equipment equipmentFromDb = equipmentManager.getEquipmentByUid(equipment.getUid());

            if (equipmentFromDb != null || !"".equals(error)) {
                if ("".equals(error)) {
                    error = String.format("%s уже существует", equipment.getUid());
                }
                modelAndView.addObject("error", error);
                modelAndView.addObject("equipment", equipment);
                modelAndView.addObject("typeEquipment", typeEquipment);
            } else {
                if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
                    Computer computer = (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER);
                    computer.setIp(request.getParameter("ip"));
                    addMotherboardToComputer(request, computer);
                    addOperationSystemToComputer(request, computer);
                    equipmentManager.save(computer);
                    modelAndView.addObject("computer", computer);
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

                modelAndView.addObject("message", "Успешно");
                modelAndView.addObject("equipment", new Equipment());
                modelAndView.addObject("typeEquipment", typeEquipment);
            }
        }

        return getModelAndView(request.getParameter("redirect"), modelAndView);
    }

    private void addMotherboardToComputer(HttpServletRequest request, Computer computer) {
        MotherBoard motherBoard = new MotherBoard();
        if (computer != null && computer.getMotherBoard() != null) {
            motherBoard = computer.getMotherBoard();
        }

        motherBoard.setManufacturer(request.getParameter("motherboard_manufacturer"));
        motherBoard.setModel(request.getParameter("motherboard_model"));
        motherBoard.setSocket(request.getParameter("motherboard_socket"));
        motherBoard.setTypeRam(TypeRam.valueOf(request.getParameter("selectTypeRam")));
        motherBoard.setRamFrequency(request.getParameter("motherboard_ram_frequency"));
        motherBoard.setRamMaxAmount(request.getParameter("motherboard_ram_max_amount"));

        computer.setMotherBoard(motherBoard);
    }

    private void addOperationSystemToComputer(HttpServletRequest request, Computer computer) {
        OperationSystem operationSystem = new OperationSystem();
        if (computer != null && computer.getOperationSystem() != null) {
            operationSystem = computer.getOperationSystem();
        }

        operationSystem.setTypeOS(TypeOS.valueOf(request.getParameter("type_operationsystem")));
        operationSystem.setVendor(request.getParameter("vendor_operationsystem"));
        operationSystem.setVersion(request.getParameter("version_operationsystem"));

        computer.setOperationSystem(operationSystem);
    }

    private String setAccounting1CByEquipment(Equipment equipment, String accounting1CRadio, Long selectAccounting1CId,
                                              String accounting1CInventoryNumber, String accounting1CTitle, Long employeeId) {
        if ("noRecord".equals(accounting1CRadio)) {
            equipment.setAccounting1C(null);
        } else if ("useRecord".equals(accounting1CRadio)) {
            Accounting1C accounting1C = accounting1CManager.getAccounting1CById(selectAccounting1CId);
            equipment.setAccounting1C(accounting1C);
        } else if ("addNewRecord".equals(accounting1CRadio)) {
            Accounting1C accounting1CFromDB = accounting1CManager.getAccounting1CByInventoryNumber(accounting1CInventoryNumber);
            if (accounting1CFromDB != null) {
                return String.format("%s уже существует", accounting1CInventoryNumber);
            }
            Accounting1C accounting1C = new Accounting1C(accounting1CInventoryNumber, accounting1CTitle,
                    employeeManager.getEmployeeById(employeeId));
            accounting1CManager.save(accounting1C);
            equipment.setAccounting1C(accounting1C);
        }
        return "";
    }

    private ModelAndView getModelAndView(@RequestParam(name = "redirect", required = false) String redirect, ModelAndView modelAndView) {
        List<Workplace> workplaceList = workplaceManager.getWorkplaceList();
        modelAndView.addObject("workplaceList", workplaceList);

        List<Accounting1C> accounting1CList = accounting1CManager.getAccounting1cList();
        modelAndView.addObject("accounting1CList", accounting1CList);

        List<Employee> employeeList = employeeManager.getEmployeeList();
        modelAndView.addObject("employeeList", employeeList);

        if (redirect == null) {
            redirect = "";
        }
        modelAndView.addObject("redirect", redirect);

        return modelAndView;
    }

    @PostMapping(Pages.updateEquipmentPost)
    public ModelAndView updateEquipment(@ModelAttribute("equipment") Equipment equipment,
                                        @RequestParam(name = "token") String token,
                                        HttpServletRequest request) {
        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigEquipment);


        Long workplaceId = request.getParameter("workplace_id") != null ? Long.parseLong(request.getParameter("workplace_id")) : -1;
        String accounting1CRadio = request.getParameter("accounting1CRadio");
        Long selectAccounting1CId = request.getParameter("selectAccounting1CId") != null ? Long.parseLong(request.getParameter("selectAccounting1CId")) : -1;
        String accounting1CInventoryNumber = request.getParameter("accounting1CInventoryNumber");
        String accounting1CTitle = request.getParameter("accounting1CTitle");
        Long employeeId = request.getParameter("employeeId") != null ? Long.parseLong(request.getParameter("employeeId")) : -1;
        String typeEquipment = request.getParameter("typeEquipment");
        String redirect = request.getParameter("redirect");

        if (!modelAndView.getViewName().equals(Pages.login)) {
            Equipment equipmentFromDb = equipmentManager.getEquipmentByUid(equipment.getUid());
            if (equipmentFromDb != null && equipmentFromDb.getId() != equipment.getId()) {
                modelAndView.setViewName("/config/equipment");
                modelAndView.addObject("error", String.format("%s уже существует", equipment.getUid()));
                modelAndView.addObject("equipment", equipment);
                modelAndView.addObject("typeEquipment", typeEquipment);
            } else {
                if (equipment != null && workplaceId > 0) {
                    Workplace workplace = workplaceManager.getWorkplaceById(workplaceId);
                    equipment.setWorkplace(workplace);
                } else {
                    equipment.setWorkplace(null);
                }

                String error = setAccounting1CByEquipment(equipment, accounting1CRadio, selectAccounting1CId, accounting1CInventoryNumber,
                        accounting1CTitle, employeeId);
                if (!"".equals(error)) {
                    modelAndView.setViewName("/config/equipment");
                    modelAndView.addObject("error", error);
                    modelAndView.addObject("equipment", equipment);
                    modelAndView.addObject("typeEquipment", typeEquipment);
                } else {
                    if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
                        Computer computer = equipmentManager.getComputerById(equipment.getId());
                        computer.setIp(request.getParameter("ip"));
                        addMotherboardToComputer(request, computer);
                        addOperationSystemToComputer(request, computer);
                        equipmentManager.save(computer);
                    } else {
                        equipmentManager.save(equipment);
                    }
                    modelAndView.setViewName("redirect:/" + redirect);
                }
            }
        }
        return getModelAndView(redirect, modelAndView);
    }

    @GetMapping(Pages.deleteEquipmentPost)
    public ModelAndView deleteEquipment(@RequestParam(name = "id") Long id,
                                        @RequestParam(value = "typeEquipment") String typeEquipment,
                                        @RequestParam(name = "redirect") String redirect,
                                        @RequestParam(name = "token") String token) {
        ModelAndView modelAndView =securityCrypt.verifyUser(token, "redirect:/" +redirect);

        if(!modelAndView.getViewName().equals(Pages.login)) {
            Equipment equipment = equipmentManager.getEquipmentById(id);
            equipmentManager.delete(equipment);
        }

        //modelAndView.setViewName("redirect:/" + redirect);
        modelAndView.addObject("typeEquipment", typeEquipment);
        return modelAndView;
    }
}
