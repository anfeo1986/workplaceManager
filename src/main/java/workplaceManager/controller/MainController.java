package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        modelAndView.setViewName("mainPage");
        modelAndView.addObject("employeeList", employeeList);
        modelAndView.addObject("workplaceList", workplaceList);

        return modelAndView;
    }

    @PostMapping("/add_employee")
    public ModelAndView addEmployee(@ModelAttribute("employee") Employee employee) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/");
        employeeManager.add(employee);
        return modelAndView;
    }

    @PostMapping("/add_workplace")
    public ModelAndView addWorkplace(@ModelAttribute("workplace") Workplace workplace) {
        workplaceManager.add(workplace);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @GetMapping("/delete_employee")
    public ModelAndView deleteEmployee(@RequestParam(name = "id") Long id) {
        Employee employee = employeeManager.getEmployeeById(id);
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
}
