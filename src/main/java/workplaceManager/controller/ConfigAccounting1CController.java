package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.*;
import workplaceManager.db.domain.*;
import workplaceManager.db.service.Accounting1CManager;
import workplaceManager.db.service.EmployeeManager;
import workplaceManager.db.service.EquipmentManager;
import workplaceManager.db.service.JournalManager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ConfigAccounting1CController {
    private Accounting1CManager accounting1CManager;

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

    private JournalManager journalManager;

    @Autowired
    public void setJournalManager(JournalManager journalManager) {
        this.journalManager = journalManager;
    }

    private EquipmentManager equipmentManager;

    @Autowired
    public void setEquipmentManager(EquipmentManager equipmentManager) {
        this.equipmentManager = equipmentManager;
    }

    @GetMapping(Pages.addUpdateAccounting1C)
    public ModelAndView getForm(@RequestParam(name = Parameters.id, required = false) Long id,
                                @RequestParam(name = Parameters.redirect, required = false) String redirect,
                                HttpServletRequest request) {
        ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.formConfigAccounting1C);
        if (!modelAndView.getViewName().equals(Pages.login)) {
            Accounting1C accounting1C = new Accounting1C();
            if (id != null && id > 0) {
                accounting1C = accounting1CManager.getAccounting1CById(id, false);
            }
            modelAndView.addObject(Parameters.accounting1C, accounting1C);
        }

        return getModelAndView(redirect, modelAndView);
    }

    @PostMapping(Pages.addAccounting1CPost)
    public ModelAndView addAccounting1C(@ModelAttribute(Parameters.accounting1C) Accounting1C accounting1C,
                                        @RequestParam(name = Parameters.redirect, required = false) String redirect,
                                        @RequestParam(name = Parameters.employeeId, required = false) Long employeeId,
                                        HttpServletRequest request) {
        Users user = securityCrypt.getUserBySession(request);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.formConfigAccounting1C);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                Employee employee = employeeManager.getEmployeeById(employeeId, false);
                accounting1C.setEmployee(employee);

                accounting1C.setDeleted(false);
                accounting1CManager.save(accounting1C);

                journalManager.save(new Journal(TypeEvent.ADD, TypeObject.ACCOUNTING1C, accounting1C, user));

                modelAndView.addObject(Parameters.message, String.format("%s успешно добавлен", accounting1C));
                modelAndView.addObject(Parameters.accounting1C, new Accounting1C());
                modelAndView.addObject(Parameters.closeWindow, true);
            }

            return getModelAndView(redirect, modelAndView);
        } else {
            return new ModelAndView(Pages.login);
        }
    }

    @PostMapping(Pages.updateAccounting1CPost)
    public ModelAndView updateAccounting(@RequestParam(name = Parameters.redirect, required = false) String redirect,
                                         @RequestParam(name = Parameters.employeeId, required = false) Long employeeId,
                                         HttpServletRequest request) {
        Users user = securityCrypt.getUserBySession(request);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.formConfigAccounting1C);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                Accounting1C accounting1C = null;
                Long id = Long.parseLong(request.getParameter(Parameters.id));
                if (id == null || id <= 0) {
                    accounting1C = new Accounting1C(request.getParameter(Parameters.inventoryNumber), request.getParameter(Parameters.title), null);
                } else {
                    accounting1C = accounting1CManager.getAccounting1CById(id, false);
                    accounting1C.setInventoryNumber(request.getParameter(Parameters.inventoryNumber));
                    accounting1C.setTitle(request.getParameter(Parameters.title));
                }

                Employee employee = employeeManager.getEmployeeById(employeeId, false);
                accounting1C.setEmployee(employee);

                Accounting1C accounting1CFromDb = accounting1CManager.getAccounting1CByInventoryNumber(accounting1C.getInventoryNumber());
                if (accounting1CFromDb != null && accounting1CFromDb.getId() != accounting1C.getId()) {
                    //modelAndView.setViewName("/config/accounting1c");
                    modelAndView.addObject(Parameters.error, String.format("%s (%s) уже существует",
                            accounting1CFromDb.getInventoryNumber(), accounting1CFromDb.getTitle()));
                    modelAndView.addObject(Parameters.accounting1C, accounting1C);
                } else {
                    Accounting1C accounting1CFromDbById = accounting1CManager.getAccounting1CById(accounting1C.getId(), false);
                    accounting1C.setDeleted(false);
                    accounting1CManager.save(accounting1C);

                    journalManager.saveChangeAccounting1C(accounting1CFromDbById, accounting1C, user);

                    //modelAndView.setViewName("redirect:/" + redirect);
                    modelAndView.addObject(Parameters.closeWindow, true);
                }
            }

            return getModelAndView(redirect, modelAndView);
        } else {
            return new ModelAndView(Pages.login);
        }
    }

    private ModelAndView getModelAndView(@RequestParam(name = Parameters.redirect, required = false) String redirect, ModelAndView modelAndView) {
        if (redirect == null) {
            redirect = "";
        }
        modelAndView.addObject(Parameters.redirect, redirect);

        List<Employee> employeeList = employeeManager.getEmployeeList();
        modelAndView.addObject(Parameters.employeeList, employeeList);

        return modelAndView;
    }

    @GetMapping(Pages.deleteAccounting1CPost)
    public ModelAndView deleteAccounting1C(@RequestParam(name = Parameters.id) Long id,
                                           HttpServletRequest request) {
        Users user = securityCrypt.getUserBySession(request);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            //ModelAndView modelAndView = securityCrypt.verifyUser(request, "redirect:/" + Pages.accounting1c);
            ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.formConfigAccounting1C);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                Accounting1C accounting1C = accounting1CManager.getAccounting1CById(id, false);
                accounting1CManager.delete(accounting1C);

                List<Equipment> equipmentList = equipmentManager.getEquipmentListByAccounting1C(accounting1C);
                for(Equipment equipment : equipmentList) {
                    equipment.setAccounting1C(null);
                    equipmentManager.save(equipment);
                }

                journalManager.save(new Journal(TypeEvent.ACCOUNTING1C_CANCELLATION, TypeObject.ACCOUNTING1C,
                        accounting1C, user));

                modelAndView.addObject(Parameters.closeWindow, true);
            }

            return modelAndView;
        } else {
            return new ModelAndView(Pages.login);
        }
    }
}
