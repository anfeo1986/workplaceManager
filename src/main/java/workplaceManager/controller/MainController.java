package workplaceManager.controller;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.db.domain.Employee;
import workplaceManager.db.domain.Workplace;
import workplaceManager.db.service.EmployeeManager;
import workplaceManager.db.service.WorkplaceManager;

import java.util.List;

@Controller
public class MainController {
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
        List<Employee> employeeList = employeeManager.getEmployeeList();
        List<Workplace> workplaceList = workplaceManager.getWorkplaceList();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("test");
        modelAndView.addObject("employeeList", employeeList);
        modelAndView.addObject("workplaceList", workplaceList);

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

    @PostMapping("/add_workplace")
    public ModelAndView addWorkplace(@ModelAttribute("workplace") Workplace workplace) {
        workplaceManager.save(workplace);

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

    @GetMapping("/delete_workplace")
    public ModelAndView deleteWorkplace(@RequestParam(name="id") Long id) {
        Workplace workplace = workplaceManager.getWorkplaceById(id);
        workplaceManager.delete(workplace);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @GetMapping("/mainPage")
    public ModelAndView test() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("content2");
        return modelAndView;
    }
}
