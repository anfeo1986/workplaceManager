package workplaceManager.controller;

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
@RequestMapping("/config/employee")
public class ConfigEmployeeController {
    private WorkplaceManager workplaceManager;

    @Autowired
    public void setWorkplaceManager(WorkplaceManager workplaceManager) {
        this.workplaceManager = workplaceManager;
    }

    private EmployeeManager employeeManager;

    @Autowired
    public void setEmployeeManager(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    @GetMapping("/addUpdateEmployee")
    public ModelAndView addUpdateEmployee(@RequestParam(name = "id", required = false) Long employeeId) {
        ModelAndView modelAndView = new ModelAndView("/config/addUpdateEmployee");

        Employee employee = new Employee();
        if(employeeId != null && employeeId > 0) {
            employee = employeeManager.getEmployeeById(employeeId);
        }
        modelAndView.addObject("employee", employee);

        List<Workplace> workplaceList = workplaceManager.getWorkplaceList();
        modelAndView.addObject("workplaceList", workplaceList);

        return modelAndView;
    }

    @PostMapping("/addEmployeePost")
    public ModelAndView addEmployee(@ModelAttribute("employee") Employee employee,
                                    @ModelAttribute("workplace_id") Long workplaceId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/config/addUpdateEmployee");

        Employee employeeFromDb = employeeManager.getEmployeeByName(employee.getName());
        if(employeeFromDb != null) {
            modelAndView.addObject("error", String.format("%s уже существует", employee.getName()));
            modelAndView.addObject("employee", employee);
        } else {
            Workplace workplace = workplaceManager.getWorkplaceById(workplaceId);
            employee.setWorkplace(workplace);
            employeeManager.save(employee);

            modelAndView.addObject("message", String.format("%s успешно добавлен", employee.getName()));
            modelAndView.addObject("employee", new Employee());
        }

        List<Workplace> workplaceList = workplaceManager.getWorkplaceList();
        modelAndView.addObject("workplaceList", workplaceList);

        return modelAndView;
    }

    @PostMapping("/updateEmployeePost")
    public ModelAndView updateEmployee() {
        return null;
    }

    @GetMapping("/deleteEmployee")
    public ModelAndView deleteEmployee() {
        return null;
    }
}
