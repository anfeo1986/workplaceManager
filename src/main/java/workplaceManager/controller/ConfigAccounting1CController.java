package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.Pages;
import workplaceManager.SecurityCrypt;
import workplaceManager.TypeEvent;
import workplaceManager.TypeObject;
import workplaceManager.db.domain.*;
import workplaceManager.db.service.Accounting1CManager;
import workplaceManager.db.service.EmployeeManager;
import workplaceManager.db.service.JournalManager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
//@RequestMapping("/config/accounting1c")
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

    JournalManager journalManager;

    @Autowired
    public void setJournalManager(JournalManager journalManager) {
        this.journalManager = journalManager;
    }

    @GetMapping(Pages.addUpdateAccounting1C)
    public ModelAndView getForm(@RequestParam(name = "id", required = false) Long id,
                                @RequestParam(name = "redirect", required = false) String redirect,
                                @RequestParam(name = "token") String token) {
        //ModelAndView modelAndView = new ModelAndView("/config/accounting1c");
        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigAccounting1C);
        if (!modelAndView.getViewName().equals(Pages.login)) {
            Accounting1C accounting1C = new Accounting1C();
            if (id != null && id > 0) {
                accounting1C = accounting1CManager.getAccounting1CById(id);
            }
            modelAndView.addObject("accounting1c", accounting1C);
        }

        return getModelAndView(redirect, modelAndView);
    }

    @PostMapping(Pages.addAccounting1CPost)
    public ModelAndView addAccounting1C(@ModelAttribute("accounting1c") Accounting1C accounting1C,
                                        @RequestParam(name = "redirect", required = false) String redirect,
                                        @RequestParam(name = "employee_id", required = false) Long employeeId,
                                        @RequestParam(name = "token") String token) {
        Users user = securityCrypt.getUserByToken(token);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigAccounting1C);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                Employee employee = employeeManager.getEmployeeById(employeeId);
                accounting1C.setEmployee(employee);

                Accounting1C accounting1C1FromDB = accounting1CManager.getAccounting1CByInventoryNumber(accounting1C.getInventoryNumber());
                if (accounting1C1FromDB != null) {
                    modelAndView.addObject("error", String.format("%s (%s) уже существует",
                            accounting1C1FromDB.getInventoryNumber(), accounting1C1FromDB.getTitle()));
                    modelAndView.addObject("accounting1c", accounting1C);
                } else {
                    accounting1CManager.save(accounting1C);

                    journalManager.save(new Journal(TypeEvent.ADD, TypeObject.ACCOUNTING1C, accounting1C, user));

                    modelAndView.addObject("message", String.format("%s успешно добавлен", accounting1C));
                    modelAndView.addObject("accounting1c", new Accounting1C());
                }
            }

            return getModelAndView(redirect, modelAndView);
        } else {
            return new ModelAndView(Pages.login);
        }
    }

    @PostMapping(Pages.updateAccounting1CPost)
    public ModelAndView updateAccounting(@RequestParam(name = "redirect", required = false) String redirect,
                                         @RequestParam(name = "employee_id", required = false) Long employeeId,
                                         @RequestParam(name = "token") String token,
                                         HttpServletRequest request) {
        Users user = securityCrypt.getUserByToken(token);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigAccounting1C);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                Accounting1C accounting1C = null;
                Long id = Long.parseLong(request.getParameter("id"));
                if (id == null || id <= 0) {
                    accounting1C = new Accounting1C(request.getParameter("inventoryNumber"), request.getParameter("title"), null);
                } else {
                    accounting1C = accounting1CManager.getAccounting1CById(id);
                    accounting1C.setInventoryNumber(request.getParameter("inventoryNumber"));
                    accounting1C.setTitle(request.getParameter("title"));
                }

                Employee employee = employeeManager.getEmployeeById(employeeId);
                accounting1C.setEmployee(employee);

                Accounting1C accounting1CFromDb = accounting1CManager.getAccounting1CByInventoryNumber(accounting1C.getInventoryNumber());
                if (accounting1CFromDb != null && accounting1CFromDb.getId() != accounting1C.getId()) {
                    //modelAndView.setViewName("/config/accounting1c");
                    modelAndView.addObject("error", String.format("%s (%s) уже существует",
                            accounting1CFromDb.getInventoryNumber(), accounting1CFromDb.getTitle()));
                    modelAndView.addObject("accounting1c", accounting1C);
                } else {
                    Accounting1C accounting1CFromDbById = accounting1CManager.getAccounting1CById(accounting1C.getId());
                    accounting1CManager.save(accounting1C);

                    journalManager.saveChangeAccounting1C(accounting1CFromDbById, accounting1C, user);

                    modelAndView.setViewName("redirect:/" + redirect);
                }
            }

            return getModelAndView(redirect, modelAndView);
        } else {
            return new ModelAndView(Pages.login);
        }
    }

    private ModelAndView getModelAndView(@RequestParam(name = "redirect", required = false) String redirect, ModelAndView modelAndView) {
        if (redirect == null) {
            redirect = "";
        }
        modelAndView.addObject("redirect", redirect);

        List<Employee> employeeList = employeeManager.getEmployeeList();
        modelAndView.addObject("employeeList", employeeList);

        return modelAndView;
    }

    @GetMapping(Pages.deleteAccounting1CPost)
    public ModelAndView deleteAccounting1C(@RequestParam(name = "id") Long id,
                                           @RequestParam(name = "token") String token) {
        Users user = securityCrypt.getUserByToken(token);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(token, "redirect:/" + Pages.accounting1c);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                Accounting1C accounting1C = accounting1CManager.getAccounting1CById(id);
                accounting1CManager.delete(accounting1C);

                journalManager.save(new Journal(TypeEvent.ACCOUNTING1C_CANCELLATION, TypeObject.ACCOUNTING1C,
                        accounting1C, user));
            }

            return modelAndView;
        } else {
            return new ModelAndView(Pages.login);
        }
    }
}
