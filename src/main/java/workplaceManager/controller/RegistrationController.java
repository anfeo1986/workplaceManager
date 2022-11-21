package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import workplaceManager.Security;
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
        userForm.setRole(Role.ADMIN);
        Security security = new Security();
        userForm.setPassword(security.encode(userForm.getPassword()));
        userManager.add(userForm);

        return "redirect:/";
    }
}
