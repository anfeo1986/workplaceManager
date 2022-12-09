package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import workplaceManager.Parameters;
import workplaceManager.SecurityCrypt;
import workplaceManager.TypeEvent;
import workplaceManager.TypeObject;
import workplaceManager.db.domain.Journal;
import workplaceManager.db.domain.Role;
import workplaceManager.db.domain.Users;
import workplaceManager.db.service.JournalManager;
import workplaceManager.db.service.UserManager;

@Controller
public class RegistrationController {
    //@Autowired
    // private UserService userService;

    private UserManager userManager;

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
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

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new Users());

        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") Users userForm, Model model) {
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration";
        }
        Users userFromDB = userManager.getUserByLogin(userForm.getUsername());
        if (userFromDB != null) {
            model.addAttribute(Parameters.error, "Пользователь " + userForm.getUsername() + " уже существует");
            return "registration";
        }
        if(userForm.getPassword().length() <= 6) {
            model.addAttribute(Parameters.error, "Длина пароля должна быть больше 6 знаков");
            return "registration";
        }
        userForm.setRole(Role.USER);
        userForm.setSalt(securityCrypt.generateKey());
        userForm.setPassword(securityCrypt.encode(userForm.getPassword(), userForm.getSalt()));
        userForm.setDeleted(false);
        userManager.add(userForm);

        journalManager.save(new Journal(TypeEvent.USER_REGISTER, TypeObject.USER, userForm));

        return "redirect:/";
    }
}
