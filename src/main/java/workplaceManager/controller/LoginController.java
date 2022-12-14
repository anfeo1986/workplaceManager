package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.*;
import workplaceManager.db.domain.Journal;
import workplaceManager.db.domain.Users;
import workplaceManager.db.service.JournalManager;
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

    private JournalManager journalManager;

    @Autowired
    public void setJournalManager(JournalManager journalManager) {
        this.journalManager = journalManager;
    }

    @GetMapping(Pages.login)
    public ModelAndView getLogin(@ModelAttribute(Parameters.userForm) Users userForm) {
        Users userFromDB = userManager.getUserByLogin(userForm.getUsername());

        Users user = new Users();
        user.setUsername(userForm.getUsername());
        user.setSalt(userFromDB == null ? "" : userFromDB.getSalt());
        user.setPassword(securityCrypt.encode(userForm.getPassword(), user.getSalt()));
        String token = securityCrypt.getToken(user);

        ModelAndView modelAndView =securityCrypt.verifyUser(token, "redirect:/" + Pages.mainPage);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            journalManager.save(new Journal(TypeEvent.USER_LOGIN, TypeObject.USER, userFromDB, userFromDB));
        }

        return modelAndView;
    }
}
