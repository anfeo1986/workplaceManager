package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.Pages;
import workplaceManager.SecurityCrypt;
import workplaceManager.TypeEvent;
import workplaceManager.TypeObject;
import workplaceManager.db.domain.Journal;
import workplaceManager.db.domain.Role;
import workplaceManager.db.domain.Users;
import workplaceManager.db.domain.Workplace;
import workplaceManager.db.service.EmployeeManager;
import workplaceManager.db.service.JournalManager;
import workplaceManager.db.service.WorkplaceManager;

@Controller
//@RequestMapping("/config/workplace")
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

    @GetMapping(Pages.addUpdateWorkplace)
    public ModelAndView addWorkplace(@RequestParam(name = "id", required = false) Long workplaceId,
                                     @RequestParam(name = "redirect", required = false) String redirect,
                                     @RequestParam(name = "token") String token) {

        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigWorkplace);
        //ModelAndView modelAndView = new ModelAndView(Pages.formConfigWorkplace);
        if (!modelAndView.getViewName().equals(Pages.login)) {
            Workplace workplace = new Workplace();
            if (workplaceId != null && workplaceId > 0) {
                workplace = workplaceManager.getWorkplaceById(workplaceId);
            }
            modelAndView.addObject("workplace", workplace);

            if (redirect == null) {
                redirect = "";
            }
            modelAndView.addObject("redirect", redirect);
        }
        return modelAndView;
    }

    @PostMapping(Pages.addWorkplacePost)
    public ModelAndView addWorkplace(@ModelAttribute("workplace") Workplace workplace,
                                     @ModelAttribute("redirect") String redirect,
                                     @RequestParam(name = "token") String token) {
        Users user = securityCrypt.getUserByToken(token);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigWorkplace);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                Workplace workplaceFromDb = workplaceManager.getWorkplaceByTitle(workplace.getTitle());
                if (workplaceFromDb != null) {
                    modelAndView.addObject("error", String.format("%s уже существует", workplace.getTitle()));
                    modelAndView.addObject("workplace", workplace);
                } else {
                    workplaceManager.save(workplace);

                    journalManager.save(new Journal(TypeEvent.ADD, TypeObject.WORKPLACE, workplace, user));

                    modelAndView.addObject("message", String.format("%s успешно добавлен", workplace.getTitle()));
                    modelAndView.addObject("workplace", new Workplace());
                }

                if (redirect == null) {
                    redirect = "";
                }
                modelAndView.addObject("redirect", redirect);
            }

            return modelAndView;
        } else {
            return new ModelAndView(Pages.login);
        }
    }

    @PostMapping(Pages.updateWorkplacePost)
    public ModelAndView updateWorkplace(@ModelAttribute("workplace") Workplace workplace,
                                        @ModelAttribute("redirect") String redirect,
                                        @RequestParam(name = "token") String token) {
        Users user = securityCrypt.getUserByToken(token);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigWorkplace);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                Workplace workplaceFromDb = workplaceManager.getWorkplaceByTitle(workplace.getTitle());
                if (workplaceFromDb != null && workplaceFromDb.getId() != workplace.getId()) {
                    //modelAndView.setViewName("/config/workplace");
                    modelAndView.addObject("error", String.format("%s уже существует", workplace.getTitle()));
                    modelAndView.addObject("workplace", workplace);
                } else {
                    Workplace workplaceFromDbById = workplaceManager.getWorkplaceById(workplace.getId());
                    workplaceManager.save(workplace);

                    journalManager.saveChangeWorkplace(workplaceFromDbById, workplace, user);

                    modelAndView.setViewName("redirect:/" + redirect);
                }

                if (redirect == null) {
                    redirect = "";
                }
                modelAndView.addObject("redirect", redirect);
            }

            return modelAndView;
        } else {
            return new ModelAndView(Pages.login);
        }
    }

    @GetMapping(Pages.deleteWorkplacePost)
    public ModelAndView deleteWorkplace(@RequestParam(name = "id") Long id,
                                        @RequestParam(name = "token") String token) {
        Users user = securityCrypt.getUserByToken(token);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(token, "redirect:/" + Pages.workplace);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                Workplace workplace = workplaceManager.getWorkplaceById(id);
                workplaceManager.delete(workplace);

                journalManager.save(new Journal(TypeEvent.DELETE, TypeObject.WORKPLACE, workplace, user));
            }

            //modelAndView.setViewName("redirect:/");
            return modelAndView;
        } else {
            return new ModelAndView(Pages.login);
        }
    }
}
