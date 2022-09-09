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
    public ModelAndView addUpdateEmployee(@RequestParam(name = "id", required = false) Long employeeId,
                                          @RequestParam(name = "redirect", required = false) String redirect) {
        ModelAndView modelAndView = new ModelAndView("/config/addUpdateEmployee");

        Employee employee = new Employee();
        if (employeeId != null && employeeId > 0) {
            employee = employeeManager.getEmployeeById(employeeId);
        }
        modelAndView.addObject("employee", employee);

        List<Workplace> workplaceList = workplaceManager.getWorkplaceList();
        modelAndView.addObject("workplaceList", workplaceList);

        if (redirect == null) {
            redirect = "";
        }
        modelAndView.addObject("redirect", redirect);

        return modelAndView;
    }

    @PostMapping("/addEmployeePost")
    public ModelAndView addEmployee(@ModelAttribute("employee") Employee employee,
                                    @ModelAttribute("workplace_id") Long workplaceId,
                                    @RequestParam(name = "redirect", required = false) String redirect) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/config/addUpdateEmployee");

        Workplace workplace = workplaceManager.getWorkplaceById(workplaceId);

        Employee employeeFromDb = employeeManager.getEmployeeByName(employee.getName());
        if (employeeFromDb != null) {
            employee.setWorkplace(workplace);

            modelAndView.addObject("error", String.format("%s уже существует", employee.getName()));
            modelAndView.addObject("employee", employee);
        } else {
            employee.setWorkplace(workplace);
            employeeManager.save(employee);

            modelAndView.addObject("message", String.format("%s успешно добавлен", employee.getName()));
            modelAndView.addObject("employee", new Employee());
        }

        List<Workplace> workplaceList = workplaceManager.getWorkplaceList();
        modelAndView.addObject("workplaceList", workplaceList);

        if (redirect == null) {
            redirect = "";
        }
        modelAndView.addObject("redirect", redirect);


        return modelAndView;
    }

    @PostMapping("/updateEmployeePost")
    public ModelAndView updateEmployee(@ModelAttribute("employee") Employee employee,
                                       @ModelAttribute("workplace_id") Long workplaceId,
                                       @ModelAttribute("redirect") String redirect) {
        ModelAndView modelAndView = new ModelAndView();

        Workplace workplace = workplaceManager.getWorkplaceById(workplaceId);

        Employee employeeFromDB = employeeManager.getEmployeeByName(employee.getName());
        if (employeeFromDB != null && employeeFromDB.getId() != employee.getId()) {
            employee.setWorkplace(workplace);
            modelAndView.setViewName("/config/addUpdateEmployee");
            modelAndView.addObject("error", String.format("%s уже существует", employee.getName()));
            modelAndView.addObject("employee", employee);
        } else {
            employee.setWorkplace(workplace);
            employeeManager.save(employee);
            modelAndView.setViewName("redirect:/" + redirect);
        }

        List<Workplace> workplaceList = workplaceManager.getWorkplaceList();
        modelAndView.addObject("workplaceList", workplaceList);

        if (redirect == null) {
            redirect = "";
        }
        modelAndView.addObject("redirect", redirect);


        return modelAndView;
    }

    @GetMapping("/deleteEmployee")
    public ModelAndView deleteEmployee(@RequestParam(name = "id") Long id) {
        Employee employee = employeeManager.getEmployeeById(id);
        employee.setWorkplace(null);
        employeeManager.save(employee);
        employeeManager.delete(employee);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/employee");
        return modelAndView;
    }
}
