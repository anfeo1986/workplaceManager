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
import workplaceManager.sorting.FilterAccounting1C;
import workplaceManager.sorting.SortingAccounting1C;

import javax.servlet.http.HttpServletRequest;
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
        virtualMachine,
        employee,
        accounting1c,
        mainSettings
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

    private VirtualMachineManager virtualMachineManager;

    @Autowired
    public void setVirtualMachineManager(VirtualMachineManager virtualMachineManager) {
        this.virtualMachineManager = virtualMachineManager;
    }

    @GetMapping(Pages.logout)
    public ModelAndView logout(HttpServletRequest request) {
        request.getSession().removeAttribute(Parameters.token);
        request.getSession().removeAttribute(Parameters.role);
        request.getSession().removeAttribute(Parameters.login);

        return new ModelAndView(Pages.login);
    }

    @GetMapping("/")
    public ModelAndView getMain(HttpServletRequest request) {
        //return securityCrypt.verifyUser(request, token, Pages.mainPage);
        return getWorkplace(request);
    }

    @GetMapping(Pages.mainPage)
    public ModelAndView getMainPageForm(HttpServletRequest request) {
        return getWorkplace(request);
    }

    @GetMapping(Pages.workplace)
    @Transactional
    public ModelAndView getWorkplace(HttpServletRequest request) {
        ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.mainPage);

        if(!modelAndView.getViewName().equals(Pages.login)) {
            List<Workplace> workplaceList = new ArrayList<>();

            Long id = request.getParameter(Parameters.id) != null ? Long.parseLong(request.getParameter(Parameters.id)) : null;
            if (id != null && id > 0) {
                Workplace workplace = workplaceManager.getWorkplaceById(id, false);
                if(workplace != null) {
                    workplaceList.add(workplace);
                }
                modelAndView.addObject(Parameters.id, id);
            } else {
                workplaceList = workplaceManager.getWorkplaceList();
            }

            modelAndView.addObject(Parameters.workplaceList, workplaceList);
            modelAndView.addObject(Parameters.page, TypePage.workplace.toString());
        }

        return modelAndView;
    }

    @GetMapping(Pages.virtualMachine)
    public ModelAndView getFormVirtualMachine(HttpServletRequest request) {
        ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.mainPage);
        if (!modelAndView.getViewName().equals(Pages.login)) {
            List<VirtualMachine> virtualMachineList = new ArrayList<>();

            virtualMachineList = virtualMachineManager.getVirtualMachineListAll();

            modelAndView.addObject(Parameters.virtualMachineList, virtualMachineList);
            modelAndView.addObject(Parameters.page, Pages.virtualMachine);
        }
        return modelAndView;
    }

    @GetMapping(Pages.employee)
    public ModelAndView getEmployee(HttpServletRequest request) {
        ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.mainPage);
        if (!modelAndView.getViewName().equals(Pages.login)) {
            List<Employee> employeeList = new ArrayList<>();

            Long id = request.getParameter(Parameters.id) != null ? Long.parseLong(request.getParameter(Parameters.id)) : null;
            if (id != null && id > 0) {
                Employee employee = employeeManager.getEmployeeById(id, false);
                if (employee != null) {
                    employeeList.add(employee);
                }
                modelAndView.addObject(Parameters.id, id);
            } else {
                employeeList = employeeManager.getEmployeeList();
            }
            modelAndView.addObject(Parameters.employeeList, employeeList);
            modelAndView.addObject(Parameters.page, TypePage.employee.toString());
        }
        return modelAndView;
    }

    @GetMapping(Pages.accounting1c)
    public ModelAndView getAccounting1C(HttpServletRequest request) {
        ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.mainPage);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            List<Accounting1C> accounting1CList = new ArrayList<>();

            Long id = request.getParameter(Parameters.id) != null ? Long.parseLong(request.getParameter(Parameters.id)) : null;
            if (id != null && id > 0) {
                Accounting1C accounting1C = accounting1CManager.getAccounting1CById(id, false);
                if (accounting1C != null) {
                    accounting1CList.add(accounting1C);
                }
                modelAndView.addObject(Parameters.id, id);
            } else {
                SortingAccounting1C sortingAccounting1C = SortingAccounting1C.INVENTORY_NUMBER;
                String sortingStr = request.getParameter(Parameters.accountingSorting);
                if (sortingStr != null) {
                    sortingAccounting1C = SortingAccounting1C.valueOf(sortingStr);
                }

                String findText = request.getParameter(Parameters.accounting1CFindText);

                FilterAccounting1C filter = FilterAccounting1C.ALL;
                String filterStr = request.getParameter(Parameters.accounting1CFilter);
                if (filterStr != null) {
                    filter = FilterAccounting1C.valueOf(filterStr);
                }

                accounting1CList = accounting1CManager.getAccounting1cList(sortingAccounting1C, findText, filter);

                modelAndView.addObject(Parameters.accountingSorting, sortingAccounting1C);
            }
            modelAndView.addObject(Parameters.page, TypePage.accounting1c.toString());
            modelAndView.addObject(Parameters.accounting1CList, accounting1CList);
        }
        return modelAndView;
    }

    @GetMapping(Pages.computer)
    public ModelAndView getComputerForm(HttpServletRequest request) {
        ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.mainPage);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            List<Computer> equipmentList = new ArrayList<>();

            Long id = request.getParameter(Parameters.id) != null ? Long.parseLong(request.getParameter(Parameters.id)) : null;
            if (id != null && id > 0) {
                Computer computer = equipmentManager.getComputerById(id, false);
                if (computer != null) {
                    equipmentList.add(computer);
                }
                modelAndView.addObject(Parameters.id, id);
            } else {

                equipmentList = equipmentManager.getComputerList();
            }

            modelAndView.addObject(Parameters.equipmentList, equipmentList);
            modelAndView.addObject(Parameters.page, TypePage.computer.toString());
            modelAndView.addObject(Parameters.typeEquipment, TypeEquipment.COMPUTER);
            modelAndView.addObject(Parameters.title, "Компьютеры");
        }
        return modelAndView;
    }

    @GetMapping(Pages.monitor)
    public ModelAndView getMonitors(HttpServletRequest request) {
        ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.mainPage);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            List<Monitor> equipmentList = new ArrayList<>();

            Long id = request.getParameter(Parameters.id) != null ? Long.parseLong(request.getParameter(Parameters.id)) : null;
            if (id != null && id > 0) {
                Equipment equipment = equipmentManager.getEquipmentById(id, false);
                Monitor monitor = equipment != null ?
                        (Monitor) equipment.getChildFromEquipment(TypeEquipment.MONITOR) : null;
                if (monitor != null) {
                    equipmentList.add(monitor);
                }
                modelAndView.addObject(Parameters.id, id);
            } else {

                equipmentList = equipmentManager.getMonitorList();
            }

            modelAndView.addObject(Parameters.equipmentList, equipmentList);
            modelAndView.addObject(Parameters.page, TypePage.monitor.toString());
            modelAndView.addObject(Parameters.typeEquipment, TypeEquipment.MONITOR);
            modelAndView.addObject(Parameters.title, "Мониторы");
        }
        return modelAndView;
    }

    @GetMapping(Pages.printer)
    public ModelAndView getPrinters(HttpServletRequest request) {
        ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.mainPage);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            List<Printer> equipmentList = new ArrayList<>();

            Long id = request.getParameter(Parameters.id) != null ? Long.parseLong(request.getParameter(Parameters.id)) : null;
            if (id != null && id > 0) {
                Equipment equipment = equipmentManager.getEquipmentById(id, false);
                Printer printer = equipment != null ?
                        (Printer) equipment.getChildFromEquipment(TypeEquipment.PRINTER) : null;
                if (printer != null) {
                    equipmentList.add(printer);
                }
                modelAndView.addObject(Parameters.id, id);
            } else {
                equipmentList = equipmentManager.getPrinterList();
            }
            modelAndView.addObject(Parameters.equipmentList, equipmentList);
            modelAndView.addObject(Parameters.page, TypePage.printer.toString());
            modelAndView.addObject(Parameters.typeEquipment, TypeEquipment.PRINTER);
            modelAndView.addObject(Parameters.title, "Принтеры");
        }
        return modelAndView;
    }

    @GetMapping(Pages.scanner)
    public ModelAndView getScanners(HttpServletRequest request) {
        ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.mainPage);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            List<Scanner> equipmentList = new ArrayList<>();

            Long id = request.getParameter(Parameters.id) != null ? Long.parseLong(request.getParameter(Parameters.id)) : null;
            if (id != null && id > 0) {
                Equipment equipment = equipmentManager.getEquipmentById(id, false);
                Scanner scanner = equipment != null ?
                        (Scanner) equipment.getChildFromEquipment(TypeEquipment.SCANNER) : null;
                if (scanner != null) {
                    equipmentList.add(scanner);
                }
                modelAndView.addObject(Parameters.id, id);
            } else {

                equipmentList = equipmentManager.getScannerList();
            }
            modelAndView.addObject(Parameters.equipmentList, equipmentList);
            modelAndView.addObject(Parameters.page, TypePage.scanner.toString());
            modelAndView.addObject(Parameters.typeEquipment, TypeEquipment.SCANNER);
            modelAndView.addObject(Parameters.title, "Сканеры");
        }
        return modelAndView;
    }

    @GetMapping(Pages.mfd)
    public ModelAndView getMfd(HttpServletRequest request) {
        ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.mainPage);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            List<Mfd> equipmentList = new ArrayList<>();
            Long id = request.getParameter(Parameters.id) != null ? Long.parseLong(request.getParameter(Parameters.id)) : null;
            if (id != null && id > 0) {
                Equipment equipment = equipmentManager.getEquipmentById(id, false);
                Mfd mfd = equipment != null ?
                        (Mfd) equipment.getChildFromEquipment(TypeEquipment.MFD) : null;
                if (mfd != null) {
                    equipmentList.add(mfd);
                }
                modelAndView.addObject(Parameters.id, id);
            } else {

                equipmentList = equipmentManager.getMfdList();
            }
            modelAndView.addObject(Parameters.equipmentList, equipmentList);
            modelAndView.addObject(Parameters.page, TypePage.mfd.toString());
            modelAndView.addObject(Parameters.typeEquipment, TypeEquipment.MFD);
            modelAndView.addObject(Parameters.title, "МФУ");
        }
        return modelAndView;
    }

    @GetMapping(Pages.ups)
    public ModelAndView getUps(HttpServletRequest request) {
        ModelAndView modelAndView = securityCrypt.verifyUser(request, Pages.mainPage);

        if (!modelAndView.getViewName().equals(Pages.login)) {
            List<Ups> equipmentList = new ArrayList<>();
            Long id = request.getParameter(Parameters.id) != null ? Long.parseLong(request.getParameter(Parameters.id)) : null;
            if(id != null && id > 0) {
                Equipment equipment =equipmentManager.getEquipmentById(id, false);
                Ups ups = equipment != null ?
                        (Ups) equipment.getChildFromEquipment(TypeEquipment.UPS) : null;
                if(ups != null) {
                    equipmentList.add(ups);
                }
                modelAndView.addObject(Parameters.id, id);
            } else {

                equipmentList = equipmentManager.getUpsList();
            }
            modelAndView.addObject(Parameters.equipmentList, equipmentList);
            modelAndView.addObject(Parameters.page, TypePage.ups.toString());
            modelAndView.addObject(Parameters.typeEquipment, TypeEquipment.UPS);
            modelAndView.addObject(Parameters.title, "ИБП");
        }
        return modelAndView;
    }
}
