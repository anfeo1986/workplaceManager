package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import workplaceManager.SecurityCrypt;
import workplaceManager.db.domain.Role;
import workplaceManager.db.domain.Users;
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
    public void setSecurity(SecurityCrypt securityCrypt) {
        this.securityCrypt = securityCrypt;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new Users());

        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") Users userForm, Model model) {

        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration";
        }
        userForm.setRole(Role.USER);
        userForm.setSalt(securityCrypt.generateKey());
        userForm.setPassword(securityCrypt.encode(userForm.getPassword(), userForm.getSalt()));
        userManager.add(userForm);

        return "redirect:/";
    }
}
