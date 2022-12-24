package workplaceManager.controller;

import org.apache.commons.lang3.StringUtils;
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

import javax.servlet.http.HttpServletRequest;

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
    public ModelAndView getLogin(@ModelAttribute(Parameters.userForm) Users userForm,
                                 HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(Pages.login);

        try {
            Users userFromDB = userManager.getUserByLogin(userForm.getUsername());
            if (userFromDB == null) {
                modelAndView.addObject(Parameters.error, "Пользователь не найден");
                return modelAndView;
            }
            String passwordCrypt = securityCrypt.encode(userForm.getPassword(), userFromDB.getSalt());
            if (!StringUtils.equals(passwordCrypt, userFromDB.getPassword())) {
                modelAndView.addObject(Parameters.error, "Неверный пароль");
                return modelAndView;
            }

            securityCrypt.setNewSaltForUser(userFromDB, request);
            String token = securityCrypt.getToken(userFromDB);
            request.getSession().setAttribute(Parameters.token, token);
            request.getSession().setAttribute(Parameters.login, userFromDB.getUsername());
            request.getSession().setAttribute(Parameters.role, userFromDB.getRole());

            modelAndView.setViewName("redirect:/" + Pages.mainPage);
            journalManager.save(new Journal(TypeEvent.USER_LOGIN, TypeObject.USER, userFromDB, userFromDB));
        } catch (Exception exception) {
            modelAndView.addObject(Parameters.error, "Ошибка авторизации");
        }
        //Users user = new Users();
        //user.setUsername(userForm.getUsername());
        //user.setSalt(userFromDB == null ? "" : userFromDB.getSalt());
        //user.setPassword(securityCrypt.encode(userForm.getPassword(), user.getSalt()));
        //String token = securityCrypt.getToken(user);

        //ModelAndView modelAndView =securityCrypt.verifyUser(request, token, "redirect:/" + Pages.mainPage);

        //if (!modelAndView.getViewName().equals(Pages.login)) {
        //    request.getSession().setAttribute(Parameters.tokenSession, token);
        //    journalManager.save(new Journal(TypeEvent.USER_LOGIN, TypeObject.USER, userFromDB, userFromDB));
        //} else {
        //    modelAndView.addObject(Parameters.error, "Ошибка авторизации");
        //}

        return modelAndView;
    }
}
