package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.db.domain.Workplace;
import workplaceManager.db.service.WorkplaceManager;

@Controller
@RequestMapping("/config")
public class ConfigController {
    private WorkplaceManager workplaceManager;

    @Autowired
    public void setWorkplaceManager(WorkplaceManager workplaceManager) {
        this.workplaceManager = workplaceManager;
    }

    @GetMapping("/add_workplace")
    public ModelAndView addWorkplace() {
        ModelAndView modelAndView = new ModelAndView("/config/addWorkplace");



        return modelAndView;
    }

    @PostMapping("/add_workplace")
    public ModelAndView addWorkplace(@ModelAttribute("workplace") Workplace workplace) {


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/config/addWorkplace");
        modelAndView.addObject("error", "Ошибка!!!");

        workplaceManager.save(workplace);

        return modelAndView;
    }

    @PostMapping("/edit_workplace")
    public ModelAndView editWorkplace(@ModelAttribute("workplace") Workplace workplace) {
        workplaceManager.save(workplace);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
}
