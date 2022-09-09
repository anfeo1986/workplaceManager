package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.db.domain.Workplace;
import workplaceManager.db.service.EmployeeManager;
import workplaceManager.db.service.WorkplaceManager;

@Controller
@RequestMapping("/config/workplace")
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

    @GetMapping("/addUpdateWorkplace")
    public ModelAndView addWorkplace(@RequestParam(name = "id", required = false) Long workplaceId,
                                     @RequestParam(name = "redirect", required = false) String redirect) {
        ModelAndView modelAndView = new ModelAndView("/config/addUpdateWorkplace");

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

    @PostMapping("/addWorkplacePost")
    public ModelAndView addWorkplace(@ModelAttribute("workplace") Workplace workplace,
                                     @ModelAttribute("redirect") String redirect) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/config/addUpdateWorkplace");

        Workplace workplaceFromDb = workplaceManager.getWorkplaceByTitle(workplace.getTitle());
        if(workplaceFromDb != null) {
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


        return modelAndView;
    }

    @PostMapping("/updateWorkplacePost")
    public ModelAndView updateWorkplace(@ModelAttribute("workplace") Workplace workplace,
                                        @ModelAttribute("redirect") String redirect) {
        ModelAndView modelAndView = new ModelAndView();

        Workplace workplaceFromDb = workplaceManager.getWorkplaceByTitle(workplace.getTitle());
        if(workplaceFromDb != null && workplaceFromDb.getId() != workplace.getId()) {
            modelAndView.setViewName("/config/addUpdateWorkplace");
            modelAndView.addObject("error", String.format("%s уже существует", workplace.getTitle()));
            modelAndView.addObject("workplace", workplace);
        } else {
            workplaceManager.save(workplace);
            modelAndView.setViewName("redirect:/"+redirect);
        }

        if (redirect == null) {
            redirect = "";
        }
        modelAndView.addObject("redirect", redirect);

        return modelAndView;
    }

    @GetMapping("/deleteWorkplace")
    public ModelAndView deleteWorkplace(@RequestParam(name = "id") Long id) {
        Workplace workplace = workplaceManager.getWorkplaceById(id);
        workplaceManager.delete(workplace);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
}
