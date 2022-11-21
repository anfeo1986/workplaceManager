package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.Pages;
import workplaceManager.Security;
import workplaceManager.db.domain.Users;
import workplaceManager.db.domain.Workplace;
import workplaceManager.db.service.WorkplaceManager;

import java.util.List;

@Controller
public class LoginController {

    private Security security;

    @Autowired
    public void setSecurity(Security security) {
        this.security = security;
    }

    private WorkplaceManager workplaceManager;

    @Autowired
    public void setWorkplaceManager(WorkplaceManager workplaceManager) {
        this.workplaceManager = workplaceManager;
    }

    @GetMapping(Pages.login)
    public ModelAndView getLogin(@ModelAttribute("userForm") Users userForm) {
        Users user = new Users();
        user.setUsername(userForm.getUsername());
        user.setPassword(security.encode(userForm.getPassword()));
        String token = security.getToken(user);
        ModelAndView modelAndView = security.verifyUser(token, "redirect:/" + Pages.mainPage);
        //List<Workplace> workplaceList = workplaceManager.getWorkplaceList();
        //modelAndView.addObject("workplaceList", workplaceList);

        //modelAndView.addObject("page", MainController.TypePage.workplace.toString());

        return modelAndView;
    }
}
