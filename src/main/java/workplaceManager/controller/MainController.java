package workplaceManager.controller;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.db.domain.Employee;
import workplaceManager.db.domain.Workplace;
import workplaceManager.db.service.EmployeeManager;
import workplaceManager.db.service.WorkplaceManager;

import java.util.List;

@Controller
public class MainController {
    private enum TypePage {
        workplace,
        computer,
        monitor,
        mfd,
        printer,
        scaner,
        ups,
        net,
        employee
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
    public ModelAndView getEmployee (){
        ModelAndView modelAndView = new ModelAndView("mainPage");

        List<Employee> employeeList = employeeManager.getEmployeeList();

        modelAndView.addObject("employeeList", employeeList);
        modelAndView.addObject("page", TypePage.employee.toString());

        return modelAndView;
    }

    @PostMapping("/add_employee")
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
    }


}
