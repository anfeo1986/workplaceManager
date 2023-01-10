package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.Pages;
import workplaceManager.Parameters;
import workplaceManager.SecurityCrypt;
import workplaceManager.db.domain.MainSettings;
import workplaceManager.db.domain.Role;
import workplaceManager.db.domain.Users;
import workplaceManager.db.service.MainSettingsManager;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

@Controller
public class MainSettingsController {
    MainSettingsManager mainSettingsManager;

    @Autowired
    public void setMainSettingsManager(MainSettingsManager mainSettingsManager) {
        this.mainSettingsManager = mainSettingsManager;
    }

    private SecurityCrypt securityCrypt;

    @Autowired
    public void setSecurity(SecurityCrypt securityCrypt) {
        this.securityCrypt = securityCrypt;
    }

    @GetMapping(Pages.mainSettings)
    public ModelAndView getFormMainSettings(HttpServletRequest request) {
        ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.mainSettings);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            MainSettings mainSettings = mainSettingsManager.getMainSettings();

            modelAndView.addObject(Parameters.mainSettings, mainSettings);
            modelAndView.addObject(Parameters.page, MainController.TypePage.mainSettings.toString());
        }
        return modelAndView;
    }

    @PostMapping(Pages.mainSettingsSavePost)
    public ModelAndView saveMainSettings(HttpServletRequest request) {
        Users user = securityCrypt.getUserBySession(request);
        if (user != null && Role.ADMIN.equals(user.getRole())) {
            ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.mainSettings);

            if (!modelAndView.getViewName().equals(Pages.login)) {
                MainSettings mainSettings =  mainSettingsManager.getMainSettings();
                if(mainSettings == null) {
                    mainSettings = new MainSettings();
                }

                mainSettings.setPrivateKey(request.getParameter(Parameters.mainSettingsPrivateKey) != null ?
                        request.getParameter(Parameters.mainSettingsPrivateKey) : "");
                mainSettings.setPublicKey(request.getParameter(Parameters.mainSettingsPublicKey) != null ?
                        request.getParameter(Parameters.mainSettingsPublicKey) : "");

                mainSettingsManager.save(mainSettings);

                modelAndView.addObject(Parameters.closeWindow, true);
            }
            return modelAndView;
        } else {
            return new ModelAndView(Pages.login);
        }
    }
}
