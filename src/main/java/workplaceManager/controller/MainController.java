package workplaceManager.controller;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.Pages;
import workplaceManager.Security;
import workplaceManager.db.domain.*;
import workplaceManager.db.service.*;

import java.util.ArrayList;
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

    private Security security;
    @Autowired
    public void setSecurity(Security security) {
        this.security = security;
    }

    private UserManager userManager;

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @GetMapping("/")
    public ModelAndView getMain(@RequestParam(name = "token", required = false) String token) {
        /*if (!"".equals(security.verifyUser(token))) {
            ModelAndView modelAndView = new ModelAndView("login");
            return modelAndView;
        }*/

        //return getWorkplace();
        return security.verifyUser(token, Pages.workplace);
    }

    @GetMapping(Pages.mainPage)
    public ModelAndView getMainPageForm(@RequestParam(name = "token") String token) {
        System.out.println("getMainPageForm");
        return getWorkplace(token);
    }

    @GetMapping(Pages.workplace)
    @Transactional
    public ModelAndView getWorkplace(@RequestParam(name = "token") String token) {

        ModelAndView modelAndView = security.verifyUser(token, Pages.mainPage);
        //modelAndView.setViewName("mainPage");

        List<Workplace> workplaceList = workplaceManager.getWorkplaceList();
        modelAndView.addObject("workplaceList", workplaceList);

        modelAndView.addObject("page", TypePage.workplace.toString());

        return modelAndView;
    }

    @GetMapping("/employee")
    public ModelAndView getEmployee(@RequestParam(name = "token") String token) {
        //ModelAndView modelAndView = new ModelAndView("mainPage");
        ModelAndView modelAndView = security.verifyUser(token, Pages.mainPage);

        List<Employee> employeeList = employeeManager.getEmployeeList();

        modelAndView.addObject("employeeList", employeeList);
        modelAndView.addObject("page", TypePage.employee.toString());

        return modelAndView;
    }

    @GetMapping("/accounting1c")
    public ModelAndView getAccounting1C(@RequestParam(name = "token") String token) {
        ModelAndView modelAndView = security.verifyUser(token, Pages.mainPage);

        List<Accounting1C> accounting1CList = accounting1CManager.getAccounting1cList();

        modelAndView.addObject("accounting1CList", accounting1CList);
        modelAndView.addObject("page", TypePage.accounting1c.toString());

        return modelAndView;
    }

    @GetMapping("/computer")
    public ModelAndView getComputerForm(@RequestParam(name = "token") String token) {
        ModelAndView modelAndView = security.verifyUser(token, Pages.mainPage);

        List<Computer> equipmentList = equipmentManager.getComputerList();

        modelAndView.addObject("equipmentList", equipmentList);
        modelAndView.addObject("page", TypePage.computer.toString());
        modelAndView.addObject("typeEquipment", TypeEquipment.COMPUTER);
        modelAndView.addObject("title", "Компьютеры");

        return modelAndView;
    }

    @GetMapping("/monitor")
    public ModelAndView getMonitors(@RequestParam(name = "token") String token) {
        ModelAndView modelAndView = security.verifyUser(token, Pages.mainPage);

        List<Monitor> equipmentList = equipmentManager.getMonitorList();

        modelAndView.addObject("equipmentList", equipmentList);
        modelAndView.addObject("page", TypePage.monitor.toString());
        modelAndView.addObject("typeEquipment", TypeEquipment.MONITOR);
        modelAndView.addObject("title", "Мониторы");

        return modelAndView;
    }

    @GetMapping("/printer")
    public ModelAndView getPrinters(@RequestParam(name = "token") String token) {
        ModelAndView modelAndView = security.verifyUser(token, Pages.mainPage);

        List<Printer> equipmentList = equipmentManager.getPrinterList();

        modelAndView.addObject("equipmentList", equipmentList);
        modelAndView.addObject("page", TypePage.printer.toString());
        modelAndView.addObject("typeEquipment", TypeEquipment.PRINTER);
        modelAndView.addObject("title", "Принтеры");

        return modelAndView;
    }

    @GetMapping("/scanner")
    public ModelAndView getScanners(@RequestParam(name = "token") String token) {
        ModelAndView modelAndView = security.verifyUser(token, Pages.mainPage);

        List<Scanner> equipmentList = equipmentManager.getScannerList();

        modelAndView.addObject("equipmentList", equipmentList);
        modelAndView.addObject("page", TypePage.scanner.toString());
        modelAndView.addObject("typeEquipment", TypeEquipment.SCANNER);
        modelAndView.addObject("title", "Сканеры");

        return modelAndView;
    }

    @GetMapping("/mfd")
    public ModelAndView getMfd(@RequestParam(name = "token") String token) {
        ModelAndView modelAndView = security.verifyUser(token, Pages.mainPage);

        List<Mfd> equipmentList = equipmentManager.getMfdList();

        modelAndView.addObject("equipmentList", equipmentList);
        modelAndView.addObject("page", TypePage.mfd.toString());
        modelAndView.addObject("typeEquipment", TypeEquipment.MFD);
        modelAndView.addObject("title", "МФУ");

        return modelAndView;
    }

    @GetMapping("/ups")
    public ModelAndView getUps(@RequestParam(name = "token") String token) {
        ModelAndView modelAndView = security.verifyUser(token, Pages.mainPage);

        List<Ups> equipmentList = equipmentManager.getUpsList();

        modelAndView.addObject("equipmentList", equipmentList);
        modelAndView.addObject("page", TypePage.ups.toString());
        modelAndView.addObject("typeEquipment", TypeEquipment.UPS);
        modelAndView.addObject("title", "ИБП");

        return modelAndView;
    }


  /*  @PostMapping("/add_employee")
    public ModelAndView addEmployee(@ModelAttribute("employee") Employee employee,
                                    @ModelAttribute("workplace_id") Long workplaceId) {
        Workplace workplace = workplaceManager.getWorkplaceById(workplaceId);
        //workplace.setEmployee(employee);
        employee.setWorkplace(workplace);
        employeeManager.save(employee);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }


    @GetMapping("/delete_employee")
    public ModelAndView deleteEmployee(@RequestParam(name = "id") Long id) {
        Employee employee = employeeManager.getEmployeeById(id);
        employee.setWorkplace(null);
        employeeManager.save(employee);
        employeeManager.delete(employee);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }*/


}
