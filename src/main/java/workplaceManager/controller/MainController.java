package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.Pages;
import workplaceManager.Parameters;
import workplaceManager.SecurityCrypt;
import workplaceManager.db.domain.*;
import workplaceManager.db.service.*;

import java.util.List;

@Controller
public class MainController {
    public enum TypePage {
        workplace,
        computer,
        monitor,
        mfd,
        printer,
        scanner,
        ups,
        net,
        employee,
        accounting1c
    }

    private EmployeeManager employeeManager;

    @Autowired
    public void setEmployeeManager(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    private WorkplaceManager workplaceManager;

    @Autowired
    public void setWorkplaceManager(WorkplaceManager workplaceManager) {
        this.workplaceManager = workplaceManager;
    }

    private Accounting1CManager accounting1CManager;

    @Autowired
    public void setAccounting1CManager(Accounting1CManager accounting1CManager) {
        this.accounting1CManager = accounting1CManager;
    }

    private EquipmentManager equipmentManager;

    @Autowired
    public void setEquipmentManager(EquipmentManager equipmentManager) {
        this.equipmentManager = equipmentManager;
    }

    private SecurityCrypt securityCrypt;

    @Autowired
    public void setSecurity(SecurityCrypt securityCrypt) {
        this.securityCrypt = securityCrypt;
    }

    private UserManager userManager;

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @GetMapping("/")
    public ModelAndView getMain(@RequestParam(name = Parameters.token, required = false) String token) {
        return securityCrypt.verifyUser(token, Pages.workplace);
    }

    @GetMapping(Pages.mainPage)
    public ModelAndView getMainPageForm(@RequestParam(name = Parameters.token) String token) {
        return getWorkplace(token);
    }

    @GetMapping(Pages.workplace)
    @Transactional
    public ModelAndView getWorkplace(@RequestParam(name = Parameters.token) String token) {
        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.mainPage);

        List<Workplace> workplaceList = workplaceManager.getWorkplaceList();
        modelAndView.addObject(Parameters.workplaceList, workplaceList);

        modelAndView.addObject(Parameters.page, TypePage.workplace.toString());

        return modelAndView;
    }

    @GetMapping(Pages.employee)
    public ModelAndView getEmployee(@RequestParam(name = Parameters.token) String token) {
        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.mainPage);
        if (!modelAndView.getViewName().equals(Pages.login)) {
            List<Employee> employeeList = employeeManager.getEmployeeList();

            modelAndView.addObject(Parameters.employeeList, employeeList);
            modelAndView.addObject(Parameters.page, TypePage.employee.toString());
        }
        return modelAndView;
    }

    @GetMapping(Pages.accounting1c)
    public ModelAndView getAccounting1C(@RequestParam(name = Parameters.token) String token) {
        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.mainPage);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            List<Accounting1C> accounting1CList = accounting1CManager.getAccounting1cList();

            modelAndView.addObject(Parameters.accounting1CList, accounting1CList);
            modelAndView.addObject(Parameters.page, TypePage.accounting1c.toString());
        }
        return modelAndView;
    }

    @GetMapping(Pages.computer)
    public ModelAndView getComputerForm(@RequestParam(name = Parameters.token) String token) {
        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.mainPage);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            List<Computer> equipmentList = equipmentManager.getComputerList();

            modelAndView.addObject(Parameters.equipmentList, equipmentList);
            modelAndView.addObject(Parameters.page, TypePage.computer.toString());
            modelAndView.addObject(Parameters.typeEquipment, TypeEquipment.COMPUTER);
            modelAndView.addObject(Parameters.title, "Компьютеры");
        }
        return modelAndView;
    }

    @GetMapping(Pages.monitor)
    public ModelAndView getMonitors(@RequestParam(name = Parameters.token) String token) {
        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.mainPage);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            List<Monitor> equipmentList = equipmentManager.getMonitorList();

            modelAndView.addObject(Parameters.equipmentList, equipmentList);
            modelAndView.addObject(Parameters.page, TypePage.monitor.toString());
            modelAndView.addObject(Parameters.typeEquipment, TypeEquipment.MONITOR);
            modelAndView.addObject(Parameters.title, "Мониторы");
        }
        return modelAndView;
    }

    @GetMapping(Pages.printer)
    public ModelAndView getPrinters(@RequestParam(name = Parameters.token) String token) {
        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.mainPage);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            List<Printer> equipmentList = equipmentManager.getPrinterList();

            modelAndView.addObject(Parameters.equipmentList, equipmentList);
            modelAndView.addObject(Parameters.page, TypePage.printer.toString());
            modelAndView.addObject(Parameters.typeEquipment, TypeEquipment.PRINTER);
            modelAndView.addObject(Parameters.title, "Принтеры");
        }
        return modelAndView;
    }

    @GetMapping(Pages.scanner)
    public ModelAndView getScanners(@RequestParam(name = Parameters.token) String token) {
        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.mainPage);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            List<Scanner> equipmentList = equipmentManager.getScannerList();

            modelAndView.addObject(Parameters.equipmentList, equipmentList);
            modelAndView.addObject(Parameters.page, TypePage.scanner.toString());
            modelAndView.addObject(Parameters.typeEquipment, TypeEquipment.SCANNER);
            modelAndView.addObject(Parameters.title, "Сканеры");
        }
        return modelAndView;
    }

    @GetMapping(Pages.mfd)
    public ModelAndView getMfd(@RequestParam(name = Parameters.token) String token) {
        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.mainPage);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            List<Mfd> equipmentList = equipmentManager.getMfdList();

            modelAndView.addObject(Parameters.equipmentList, equipmentList);
            modelAndView.addObject(Parameters.page, TypePage.mfd.toString());
            modelAndView.addObject(Parameters.typeEquipment, TypeEquipment.MFD);
            modelAndView.addObject(Parameters.title, "МФУ");
        }
        return modelAndView;
    }

    @GetMapping(Pages.ups)
    public ModelAndView getUps(@RequestParam(name = Parameters.token) String token) {
        ModelAndView modelAndView = securityCrypt.verifyUser(token, Pages.mainPage);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            List<Ups> equipmentList = equipmentManager.getUpsList();

            modelAndView.addObject(Parameters.equipmentList, equipmentList);
            modelAndView.addObject(Parameters.page, TypePage.ups.toString());
            modelAndView.addObject(Parameters.typeEquipment, TypeEquipment.UPS);
            modelAndView.addObject(Parameters.title, "ИБП");
        }
        return modelAndView;
    }
}
