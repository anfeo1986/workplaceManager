package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.*;
import workplaceManager.db.domain.*;
import workplaceManager.db.service.EmployeeManager;
import workplaceManager.db.service.EquipmentManager;
import workplaceManager.db.service.JournalManager;
import workplaceManager.db.service.WorkplaceManager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ConfigWorkplaceController {
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

    @GetMapping(Pages.addUpdateWorkplace)
    public ModelAndView addWorkplace(@RequestParam(name = Parameters.id, required = false) Long workplaceId,
                                     @RequestParam(name = Parameters.redirect, required = false) String redirect,
                                     HttpServletRequest request) {
        ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.formConfigWorkplace);
        if (!modelAndView.getViewName().equals(Pages.login)) {
            Workplace workplace = new Workplace();
            if (workplaceId != null && workplaceId > 0) {
                workplace = workplaceManager.getWorkplaceById(workplaceId, false);
            }
            modelAndView.addObject(Parameters.workplace, workplace);
            if (redirect == null) {
                redirect = "";
            }

            modelAndView.addObject(Parameters.redirect, redirect);
        }
        return modelAndView;
    }

    @PostMapping(Pages.addWorkplacePost)
    public ModelAndView addWorkplace(@ModelAttribute(Parameters.workplace) Workplace workplace,
                                     @ModelAttribute(Parameters.redirect) String redirect,
                                     HttpServletRequest request) {
        Users user = securityCrypt.getUserBySession(request);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.formConfigWorkplace);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                Workplace workplaceFromDb = workplaceManager.getWorkplaceByTitle(workplace.getTitle());
                if (workplaceFromDb != null) {
                    modelAndView.addObject(Parameters.error, String.format("%s ?????? ????????????????????", workplace.getTitle()));
                    modelAndView.addObject(Parameters.workplace, workplace);
                } else {
                    workplace.setDeleted(false);
                    workplaceManager.save(workplace);

                    journalManager.save(new Journal(TypeEvent.ADD, TypeObject.WORKPLACE, workplace, user));

                    modelAndView.addObject(Parameters.message, String.format("%s ?????????????? ????????????????", workplace.getTitle()));
                    modelAndView.addObject(Parameters.workplace, new Workplace());
                }

                if (redirect == null) {
                    redirect = "";
                }
                modelAndView.addObject(Parameters.redirect, redirect);
                modelAndView.addObject(Parameters.closeWindow, true);
            }

            return modelAndView;
        } else {
            return new ModelAndView(Pages.login);
        }
    }

    @PostMapping(Pages.updateWorkplacePost)
    public ModelAndView updateWorkplace(@ModelAttribute(Parameters.workplace) Workplace workplace,
                                        @ModelAttribute(Parameters.redirect) String redirect,
                                        //@RequestParam(name = Parameters.token) String token,
                                        HttpServletRequest request) {
        Users user = securityCrypt.getUserBySession(request);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.formConfigWorkplace);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                Workplace workplaceFromDb = workplaceManager.getWorkplaceByTitle(workplace.getTitle());
                if (workplaceFromDb != null && workplaceFromDb.getId() != workplace.getId()) {
                    //modelAndView.setViewName("/config/workplace");
                    modelAndView.addObject(Parameters.error, String.format("%s ?????? ????????????????????", workplace.getTitle()));
                    modelAndView.addObject(Parameters.workplace, workplace);
                } else {
                    Workplace workplaceFromDbById = workplaceManager.getWorkplaceById(workplace.getId(), false);
                    workplace.setDeleted(false);
                    workplaceManager.save(workplace);

                    journalManager.saveChangeWorkplace(workplaceFromDbById, workplace, user);

                    modelAndView.addObject(Parameters.message, workplace + "?????????????? ????????????????????????????");
                    modelAndView.addObject(Parameters.closeWindow, true);
                    //modelAndView.setViewName("redirect:/" + redirect);
                }

                if (redirect == null) {
                    redirect = "";
                }
                modelAndView.addObject(Parameters.redirect, redirect);
            }

            return modelAndView;
        } else {
            return new ModelAndView(Pages.login);
        }
    }

    @GetMapping(Pages.deleteWorkplacePost)
    public ModelAndView deleteWorkplace(@RequestParam(name = Parameters.id) Long id,
                                        //@RequestParam(name = Parameters.token) String token,
                                        HttpServletRequest request) {
        Users user = securityCrypt.getUserBySession(request);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            //ModelAndView modelAndView = securityCrypt.verifyUser(request, "redirect:/" + Pages.workplace);
            ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.formConfigWorkplace);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                Workplace workplace = workplaceManager.getWorkplaceById(id, false);

                List<Employee> employeeList = employeeManager.getEmployeeListByWorkplace(workplace);
                for(Employee employee : employeeList) {
                    employee.setWorkplace(null);
                    employeeManager.save(employee);
                }
                List<Equipment> equipmentList = equipmentManager.getEquipmentListByWorkplace(workplace);
                for(Equipment equipment : equipmentList) {
                    equipment.setWorkplace(null);
                    equipmentManager.save(equipment);
                }

                workplaceManager.delete(workplace);

                journalManager.save(new Journal(TypeEvent.DELETE, TypeObject.WORKPLACE, workplace, user));

                modelAndView.addObject(Parameters.closeWindow, true);
            }

            //modelAndView.setViewName("redirect:/");
            return modelAndView;
        } else {
            return new ModelAndView(Pages.login);
        }
    }
}
