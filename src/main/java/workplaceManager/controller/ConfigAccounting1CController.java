package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.db.domain.Accounting1C;
import workplaceManager.db.domain.Employee;
import workplaceManager.db.service.Accounting1CManager;
import workplaceManager.db.service.EmployeeManager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/config/accounting1c")
public class ConfigAccounting1CController {
    private Accounting1CManager accounting1CManager;

    @Autowired
    public void setAccounting1CManager(Accounting1CManager accounting1CManager) {
        this.accounting1CManager = accounting1CManager;
    }

    EmployeeManager employeeManager;

    @Autowired
    public void setEmployeeManager(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    @GetMapping("/addUpdateAccounting1C")
    public ModelAndView getForm(@RequestParam(name = "id", required = false) Long id,
                                @RequestParam(name = "redirect", required = false) String redirect) {
        ModelAndView modelAndView = new ModelAndView("/config/accounting1c");

        Accounting1C accounting1C = new Accounting1C();
        if (id != null && id > 0) {
            accounting1C = accounting1CManager.getAccounting1CById(id);
        }
        modelAndView.addObject("accounting1c", accounting1C);

        return getModelAndView(redirect, modelAndView);
    }

    @PostMapping("/addAccounting1CPost")
    public ModelAndView addAccounting1C(@ModelAttribute("accounting1c") Accounting1C accounting1C,
                                        @RequestParam(name = "redirect", required = false) String redirect,
                                        @RequestParam(name = "employee_id", required = false) Long employeeId) {
        ModelAndView modelAndView = new ModelAndView("/config/accounting1c");

        Employee employee = employeeManager.getEmployeeById(employeeId);
        accounting1C.setEmployee(employee);

        Accounting1C accounting1C1FromDB = accounting1CManager.getAccounting1CByInventoryNumber(accounting1C.getInventoryNumber());
        if(accounting1C1FromDB != null) {
            modelAndView.addObject("error", String.format("%s (%s) уже существует",
                    accounting1C1FromDB.getInventoryNumber(), accounting1C1FromDB.getTitle()));
            modelAndView.addObject("accounting1c", accounting1C);
        } else {
            accounting1CManager.save(accounting1C);

            modelAndView.addObject("message", String.format("%s успешно добавлен", accounting1C));
            modelAndView.addObject("accounting1c", new Accounting1C());
        }

        return getModelAndView(redirect, modelAndView);
    }

    @PostMapping("/updateAccounting1C")
    public ModelAndView updateAccounting(//@ModelAttribute("accounting1c") Accounting1C accounting1C,
                                         @RequestParam(name = "redirect", required = false) String redirect,
                                         @RequestParam(name = "employee_id", required = false) Long employeeId,
                                         HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        Accounting1C accounting1C = null;
        Long id = Long.parseLong(request.getParameter("id"));
        if(id == null || id <= 0) {
            accounting1C = new Accounting1C(request.getParameter("inventoryNumber"), request.getParameter("title"), null);
        } else {
            accounting1C = accounting1CManager.getAccounting1CById(id);
            accounting1C.setInventoryNumber(request.getParameter("inventoryNumber"));
            accounting1C.setTitle(request.getParameter("title"));
        }

        Employee employee = employeeManager.getEmployeeById(employeeId);
        accounting1C.setEmployee(employee);

        Accounting1C accounting1CFromDb = accounting1CManager.getAccounting1CByInventoryNumber(accounting1C.getInventoryNumber());
        if(accounting1CFromDb != null && accounting1CFromDb.getId() != accounting1C.getId()) {
            modelAndView.setViewName("/config/accounting1c");
            modelAndView.addObject("error", String.format("%s (%s) уже существует",
                    accounting1CFromDb.getInventoryNumber(), accounting1CFromDb.getTitle()));
            modelAndView.addObject("accounting1c", accounting1C);
        } else {
            accounting1CManager.save(accounting1C);
            modelAndView.setViewName("redirect:/" + redirect);
        }

        return getModelAndView(redirect, modelAndView);
    }

    private ModelAndView getModelAndView(@RequestParam(name = "redirect", required = false) String redirect, ModelAndView modelAndView) {
        if (redirect == null) {
            redirect = "";
        }
        modelAndView.addObject("redirect", redirect);

        List<Employee> employeeList = employeeManager.getEmployeeList();
        modelAndView.addObject("employeeList", employeeList);

        return modelAndView;
    }

    @GetMapping("/deleteAccounting1C")
    public ModelAndView deleteAccounting1C(@RequestParam(name = "id") Long id) {
        Accounting1C accounting1C = accounting1CManager.getAccounting1CById(id);
        accounting1CManager.delete(accounting1C);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/accounting1c");
        return modelAndView;
    }
}
