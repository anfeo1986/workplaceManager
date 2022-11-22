package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.Pages;
import workplaceManager.SecurityCrypt;
import workplaceManager.db.domain.Users;
import workplaceManager.db.service.UserManager;
import workplaceManager.db.service.WorkplaceManager;

@Controller
public class LoginController {

    private SecurityCrypt securityCrypt;

    @Autowired
    public void setSecurity(SecurityCrypt securityCrypt) {
        this.securityCrypt = securityCrypt;
    }

    private WorkplaceManager workplaceManager;

    @Autowired
    public void setWorkplaceManager(WorkplaceManager workplaceManager) {
        this.workplaceManager = workplaceManager;
    }

    private UserManager userManager;

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @GetMapping(Pages.login)
    public ModelAndView getLogin(@ModelAttribute("userForm") Users userForm) {
        Users userFromDB = userManager.getUserByLogin(userForm.getUsername());

        Users user = new Users();
        user.setUsername(userForm.getUsername());
        user.setSalt(userFromDB == null ? "" : userFromDB.getSalt());
        user.setPassword(securityCrypt.encode(userForm.getPassword(), user.getSalt()));
        String token = securityCrypt.getToken(user);

        return securityCrypt.verifyUser(token, "redirect:/" + Pages.mainPage);
    }
}
