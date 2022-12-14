package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.*;
import workplaceManager.db.domain.*;
import workplaceManager.db.service.EmployeeManager;
import workplaceManager.db.service.JournalManager;
import workplaceManager.db.service.WorkplaceManager;

import java.util.List;

@Controller
//@RequestMapping("/config/employee")
public class ConfigEmployeeController {
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
    public void setSecurity(SecurityCrypt securityCrypt) {
        this.securityCrypt = securityCrypt;
    }

    private JournalManager journalManager;

    @Autowired
    public void setJournalManager(JournalManager journalManager) {
        this.journalManager = journalManager;
    }

    @GetMapping(Pages.addUpdateEmployee)
    public ModelAndView addUpdateEmployee(@RequestParam(name = Parameters.id, required = false) Long employeeId,
                                          @RequestParam(name = Parameters.redirect, required = false) String redirect,
                                          @RequestParam(name = Parameters.token) String token) {
        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigEmployee);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            Employee employee = new Employee();
            if (employeeId != null && employeeId > 0) {
                employee = employeeManager.getEmployeeById(employeeId, false);
            }
            modelAndView.addObject(Parameters.employee, employee);

            List<Workplace> workplaceList = workplaceManager.getWorkplaceList();
            modelAndView.addObject(Parameters.workplaceList, workplaceList);

            if (redirect == null) {
                redirect = "";
            }
            modelAndView.addObject(Parameters.redirect, redirect);
        }
        return modelAndView;
    }

    @PostMapping(Pages.addEmployeePost)
    public ModelAndView addEmployee(@ModelAttribute(Parameters.employee) Employee employee,
                                    @ModelAttribute(Parameters.workplaceId) Long workplaceId,
                                    @RequestParam(name = Parameters.redirect, required = false) String redirect,
                                    @RequestParam(name = Parameters.token) String token) {
        Users user = securityCrypt.getUserByToken(token);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigEmployee);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                Workplace workplace = workplaceManager.getWorkplaceById(workplaceId, false);

                Employee employeeFromDb = employeeManager.getEmployeeByName(employee.getName(), false);
                if (employeeFromDb != null) {
                    employee.setWorkplace(workplace);

                    modelAndView.addObject(Parameters.error, String.format("%s уже существует", employee.getName()));
                    modelAndView.addObject(Parameters.employee, employee);
                } else {
                    employee.setWorkplace(workplace);
                    employee.setDeleted(false);
                    employeeManager.save(employee);

                    journalManager.save(new Journal(TypeEvent.ADD, TypeObject.EMPLOYEE, employee, user));

                    modelAndView.addObject(Parameters.message, String.format("%s успешно добавлен", employee.getName()));
                    modelAndView.addObject(Parameters.employee, new Employee());
                }

                List<Workplace> workplaceList = workplaceManager.getWorkplaceList();
                modelAndView.addObject(Parameters.workplaceList, workplaceList);

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

    @PostMapping(Pages.updateEmployeePost)
    public ModelAndView updateEmployee(@ModelAttribute(Parameters.employee) Employee employee,
                                       @ModelAttribute(Parameters.workplaceId) Long workplaceId,
                                       @ModelAttribute(Parameters.redirect) String redirect,
                                       @RequestParam(name = Parameters.token) String token) {
        Users user = securityCrypt.getUserByToken(token);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigEmployee);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                Workplace workplace = workplaceManager.getWorkplaceById(workplaceId, false);

                Employee employeeFromDB = employeeManager.getEmployeeByName(employee.getName(), false);
                if (employeeFromDB != null && employeeFromDB.getId() != employee.getId()) {
                    employee.setWorkplace(workplace);
                    //modelAndView.setViewName("/config/employee");
                    modelAndView.addObject(Parameters.error, String.format("%s уже существует", employee.getName()));
                    modelAndView.addObject(Parameters.employee, employee);
                } else {
                    Employee employeeFromDbByID = employeeManager.getEmployeeById(employee.getId(), false);
                    employee.setWorkplace(workplace);
                    employee.setDeleted(false);
                    employeeManager.save(employee);

                    journalManager.saveChangeEmployee(employeeFromDbByID, employee, user);

                    modelAndView.setViewName("redirect:/" + redirect);
                }

                List<Workplace> workplaceList = workplaceManager.getWorkplaceList();
                modelAndView.addObject(Parameters.workplaceList, workplaceList);

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

    @GetMapping(Pages.deleteEmployeePost)
    public ModelAndView deleteEmployee(@RequestParam(name = Parameters.id) Long id,
                                       @RequestParam(name = Parameters.token) String token) {
        Users user = securityCrypt.getUserByToken(token);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(token, "redirect:/"+Pages.employee);
            if (!modelAndView.getViewName().equals(Pages.login)) {
                Employee employee = employeeManager.getEmployeeById(id, false);
                employeeManager.delete(employee);

                journalManager.save(new Journal(TypeEvent.DELETE, TypeObject.EMPLOYEE, employee, user));
            }
            //modelAndView.setViewName("redirect:/employee");
            return modelAndView;
        } else {
            return new ModelAndView(Pages.login);
        }
    }


}
