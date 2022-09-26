package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
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
@RequestMapping("/config/equipment")
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

    @GetMapping("/addUpdateEquipment")
    public ModelAndView getFormAddUpdateEquipment(@RequestParam(name = "id", required = false) Long id,
                                                  @RequestParam(name = "typeEquipment") String typeEquipment,
                                                  @RequestParam(name = "redirect", required = false) String redirect) {
        ModelAndView modelAndView = new ModelAndView("/config/equipment");

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

        return getModelAndView(redirect, modelAndView);
    }

    @PostMapping("/addEquipmentPost")
    public ModelAndView addEquipment(@ModelAttribute("equipment") Equipment equipment,
                                     HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/config/equipment");

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
                Computer computer = addMotherboardToComputerFromReque(request, (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER));
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

        return getModelAndView(request.getParameter("redirect"), modelAndView);
    }

    private Computer addMotherboardToComputerFromReque(HttpServletRequest request, Computer computer) {
        MotherBoard motherBoard = new MotherBoard();

        motherBoard.setManufacturer(request.getParameter("motherboard_manufacturer"));
        motherBoard.setModel(request.getParameter("motherboard_model"));
        motherBoard.setSocket(request.getParameter("motherboard_socket"));
        motherBoard.setTypeRam(TypeRam.valueOf(request.getParameter("selectTypeRam")));
        motherBoard.setRamFrequency(request.getParameter("motherboard_ram_frequency"));
        motherBoard.setRamMaxAmount(request.getParameter("motherboard_ram_max_amount"));

        computer.setMotherBoard(motherBoard);

        return computer;
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

    @PostMapping("/updateEquipmentPost")
    public ModelAndView updateEquipment(@ModelAttribute("equipment") Equipment equipment,
                                        HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        Long workplaceId = request.getParameter("workplace_id") != null ? Long.parseLong(request.getParameter("workplace_id")) : -1;
        String accounting1CRadio = request.getParameter("accounting1CRadio");
        Long selectAccounting1CId = request.getParameter("selectAccounting1CId") != null ? Long.parseLong(request.getParameter("selectAccounting1CId")) : -1;
        String accounting1CInventoryNumber = request.getParameter("accounting1CInventoryNumber");
        String accounting1CTitle = request.getParameter("accounting1CTitle");
        Long employeeId = request.getParameter("employeeId") != null ? Long.parseLong(request.getParameter("employeeId")) : -1;
        String typeEquipment = request.getParameter("typeEquipment");
        String redirect = request.getParameter("redirect");

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
                    Computer computer = addMotherboardToComputerFromReque(request, (Computer) equipment.getChildFromEquipment(TypeEquipment.COMPUTER));
                    equipmentManager.save(computer);
                } else {
                    equipmentManager.save(equipment);
                }
                modelAndView.setViewName("redirect:/" + redirect);
            }
        }

        return getModelAndView(redirect, modelAndView);
    }

    @GetMapping("/deleteEquipment")
    public ModelAndView deleteEquipment(@RequestParam(name = "id") Long id,
                                        @RequestParam(value = "typeEquipment") String typeEquipment,
                                        @RequestParam(name = "redirect") String redirect) {
        Equipment equipment = equipmentManager.getEquipmentById(id);
        equipmentManager.delete(equipment);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/" + redirect);
        modelAndView.addObject("typeEquipment", typeEquipment);
        return modelAndView;
    }
}
