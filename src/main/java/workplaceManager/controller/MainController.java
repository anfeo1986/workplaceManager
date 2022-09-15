package workplaceManager.controller;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.db.domain.*;
import workplaceManager.db.service.Accounting1CManager;
import workplaceManager.db.service.EmployeeManager;
import workplaceManager.db.service.EquipmentManager;
import workplaceManager.db.service.WorkplaceManager;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    private enum TypePage {
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

    @GetMapping("/")
    public ModelAndView getMainPage() {
        return getWorkplace();
    }

    @GetMapping("/workplace")
    @Transactional
    public ModelAndView getWorkplace() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("mainPage");

        List<Workplace> workplaceList = workplaceManager.getWorkplaceList();
        modelAndView.addObject("workplaceList", workplaceList);

        modelAndView.addObject("page", TypePage.workplace.toString());

        return modelAndView;
    }

    @GetMapping("/employee")
    public ModelAndView getEmployee() {
        ModelAndView modelAndView = new ModelAndView("mainPage");

        List<Employee> employeeList = employeeManager.getEmployeeList();

        modelAndView.addObject("employeeList", employeeList);
        modelAndView.addObject("page", TypePage.employee.toString());

        return modelAndView;
    }

    @GetMapping("/accounting1c")
    public ModelAndView getAccounting1C() {
        ModelAndView modelAndView = new ModelAndView("mainPage");

        List<Accounting1C> accounting1CList = accounting1CManager.getAccounting1cList();

        modelAndView.addObject("accounting1CList", accounting1CList);
        modelAndView.addObject("page", TypePage.accounting1c.toString());

        return modelAndView;
    }

    @GetMapping("/monitor")
    public ModelAndView getMonitors() {
        ModelAndView modelAndView = new ModelAndView("mainPage");

        List<Monitor> equipmentList = equipmentManager.getMonitorList();

        modelAndView.addObject("equipmentList", equipmentList);
        modelAndView.addObject("page", TypePage.monitor.toString());
        modelAndView.addObject("typeEquipment", TypeEquipment.MONITOR);
        modelAndView.addObject("title", "Мониторы");

        return modelAndView;
    }

    @GetMapping("/printer")
    public ModelAndView getPrinters() {
        ModelAndView modelAndView = new ModelAndView("mainPage");

        List<Printer> equipmentList = equipmentManager.getPrinterList();

        modelAndView.addObject("equipmentList", equipmentList);
        modelAndView.addObject("page", TypePage.printer.toString());
        modelAndView.addObject("typeEquipment", TypeEquipment.PRINTER);
        modelAndView.addObject("title", "Принтеры");

        return modelAndView;
    }

    @GetMapping("/scanner")
    public ModelAndView getScanners() {
        ModelAndView modelAndView = new ModelAndView("mainPage");

        List<Scanner> equipmentList = equipmentManager.getScannerList();

        modelAndView.addObject("equipmentList", equipmentList);
        modelAndView.addObject("page", TypePage.scanner.toString());
        modelAndView.addObject("typeEquipment", TypeEquipment.SCANNER);
        modelAndView.addObject("title", "Сканеры");

        return modelAndView;
    }

    @GetMapping("/mfd")
    public ModelAndView getMfd() {
        ModelAndView modelAndView = new ModelAndView("mainPage");

        List<Mfd> equipmentList = equipmentManager.getMfdList();

        modelAndView.addObject("equipmentList", equipmentList);
        modelAndView.addObject("page", TypePage.mfd.toString());
        modelAndView.addObject("typeEquipment", TypeEquipment.MFD);
        modelAndView.addObject("title", "МФУ");

        return modelAndView;
    }

    @GetMapping("/ups")
    public ModelAndView getUps() {
        ModelAndView modelAndView = new ModelAndView("mainPage");

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
