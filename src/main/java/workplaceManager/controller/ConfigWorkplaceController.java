package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.Pages;
import workplaceManager.SecurityCrypt;
import workplaceManager.db.domain.Workplace;
import workplaceManager.db.service.EmployeeManager;
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
    public void setSecurity(SecurityCrypt securityCrypt) {
        this.securityCrypt = securityCrypt;
    }

    @GetMapping(Pages.addUpdateWorkplace)
    public ModelAndView addWorkplace(@RequestParam(name = "id", required = false) Long workplaceId,
                                     @RequestParam(name = "redirect", required = false) String redirect,
                                     @RequestParam(name = "token") String token) {
        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigWorkplace);
        //ModelAndView modelAndView = new ModelAndView(Pages.formConfigWorkplace);
        System.out.println("redirect="+redirect);
        Workplace workplace = new Workplace();
        if(workplaceId != null && workplaceId > 0) {
            workplace = workplaceManager.getWorkplaceById(workplaceId);
        }
        modelAndView.addObject("workplace", workplace);

        if (redirect == null) {
            redirect = "";
        }
        modelAndView.addObject("redirect", redirect);

        return modelAndView;
    }

    @PostMapping(Pages.addWorkplacePost)
    public ModelAndView addWorkplace(@ModelAttribute("workplace") Workplace workplace,
                                     @ModelAttribute("redirect") String redirect,
                                     @RequestParam(name = "token") String token) {
        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigWorkplace);
        //modelAndView.setViewName("/config/workplace");

        if(!modelAndView.getViewName().equals(Pages.login)) {
            Workplace workplaceFromDb = workplaceManager.getWorkplaceByTitle(workplace.getTitle());
            if (workplaceFromDb != null) {
                modelAndView.addObject("error", String.format("%s уже существует", workplace.getTitle()));
                modelAndView.addObject("workplace", workplace);
            } else {
                workplaceManager.save(workplace);
                modelAndView.addObject("message", String.format("%s успешно добавлен", workplace.getTitle()));
                modelAndView.addObject("workplace", new Workplace());
            }

            if (redirect == null) {
                redirect = "";
            }
            modelAndView.addObject("redirect", redirect);
        }

        return modelAndView;
    }

    @PostMapping(Pages.updateWorkplacePost)
    public ModelAndView updateWorkplace(@ModelAttribute("workplace") Workplace workplace,
                                        @ModelAttribute("redirect") String redirect,
                                        @RequestParam(name = "token") String token) {
        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.formConfigWorkplace);

        if(!modelAndView.getViewName().equals(Pages.login)) {
            Workplace workplaceFromDb = workplaceManager.getWorkplaceByTitle(workplace.getTitle());
            if (workplaceFromDb != null && workplaceFromDb.getId() != workplace.getId()) {
                //modelAndView.setViewName("/config/workplace");
                modelAndView.addObject("error", String.format("%s уже существует", workplace.getTitle()));
                modelAndView.addObject("workplace", workplace);
            } else {
                workplaceManager.save(workplace);
                modelAndView.setViewName("redirect:/" + redirect);
            }

            if (redirect == null) {
                redirect = "";
            }
            modelAndView.addObject("redirect", redirect);
        }

        return modelAndView;
    }

    @GetMapping(Pages.deleteWorkplcaePost)
    public ModelAndView deleteWorkplace(@RequestParam(name = "id") Long id,
                                        @RequestParam(name = "token") String token) {
        ModelAndView modelAndView = securityCrypt.verifyUser(token, "redirect:/" + Pages.workplace);

        if(!modelAndView.getViewName().equals(Pages.login)) {
            Workplace workplace = workplaceManager.getWorkplaceById(id);
            workplaceManager.delete(workplace);
        }

        //modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
}
