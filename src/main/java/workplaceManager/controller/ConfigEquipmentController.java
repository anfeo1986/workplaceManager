package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.db.domain.*;
import workplaceManager.db.service.EquipmentManager;
import workplaceManager.db.service.WorkplaceManager;

import java.util.List;

@Controller
@RequestMapping("/config/equipment")
public class ConfigEquipmentController {

    EquipmentManager equipmentManager;

    @Autowired
    public void setEquipmentManager(EquipmentManager equipmentManager) {
        this.equipmentManager = equipmentManager;
    }

    WorkplaceManager workplaceManager;

    @Autowired
    public void setWorkplaceManager(WorkplaceManager workplaceManager) {
        this.workplaceManager = workplaceManager;
    }

    @GetMapping("/addUpdateEquipment")
    public ModelAndView getFormAddUpdateEquipment(@RequestParam(name = "id", required = false) Long id,
                                                  @RequestParam(name = "typeEquipment") String typeEquipment,
                                                  @RequestParam(name = "redirect", required = false) String redirect) {
        ModelAndView modelAndView = new ModelAndView("/config/equipment");

        Equipment equipment = new Equipment();
        if (id != null && id > 0) {
            equipment = equipmentManager.getEquipmentById(id);
        }
        modelAndView.addObject("equipment", equipment);
        modelAndView.addObject("typeEquipment", typeEquipment);

        List<Workplace> workplaceList = workplaceManager.getWorkplaceList();
        modelAndView.addObject("workplaceList", workplaceList);

        if (redirect == null) {
            redirect = "";
        }
        modelAndView.addObject("redirect", redirect);

        return modelAndView;
    }

    @PostMapping("/addEquipmentPost")
    public ModelAndView addEquipment(@ModelAttribute("equipment") Equipment equipment,
                                     @RequestParam(value = "typeEquipment") String typeEquipment,
                                     @RequestParam(name = "redirect", required = false) String redirect,
                                     @RequestParam(name = "workplace_id", required = false) Long workplaceId) {
        ModelAndView modelAndView = new ModelAndView("/config/equipment");

        if(equipment != null && workplaceId > 0) {
            Workplace workplace = workplaceManager.getWorkplaceById(workplaceId);
            equipment.setWorkplace(workplace);
        }

        Equipment equipmentFromDb = equipmentManager.getEquipmentByUid(equipment.getUid());

        if (equipmentFromDb != null) {
            modelAndView.addObject("error", String.format("%s уже существует", equipment.getUid()));
            modelAndView.addObject("equipment", equipment);
            modelAndView.addObject("typeEquipment", typeEquipment);
        } else {
            if(TypeEquipment.MONITOR.equals(typeEquipment)) {
                equipmentManager.save((Monitor) equipment.getChildFromEquipment(TypeEquipment.MONITOR));
            }
            if(TypeEquipment.PRINTER.equals(typeEquipment)) {
                equipmentManager.save((Printer) equipment.getChildFromEquipment(TypeEquipment.PRINTER));
            }
            if(TypeEquipment.SCANNER.equals(typeEquipment)) {
                equipmentManager.save((Scanner) equipment.getChildFromEquipment(TypeEquipment.SCANNER));
            }
            if(TypeEquipment.MFD.equals(typeEquipment)) {
                equipmentManager.save((Mfd) equipment.getChildFromEquipment(TypeEquipment.MFD));
            }
            if(TypeEquipment.UPS.equals(typeEquipment)) {
                equipmentManager.save((Ups) equipment.getChildFromEquipment(TypeEquipment.UPS));
            }

            modelAndView.addObject("message", "Успешно");
            modelAndView.addObject("equipment", new Equipment());
            modelAndView.addObject("typeEquipment", typeEquipment);
        }

        if (redirect == null) {
            redirect = "";
        }
        modelAndView.addObject("redirect", redirect);

        return modelAndView;
    }

    @PostMapping("/updateEquipmentPost")
    public ModelAndView updateEquipment(@ModelAttribute("equipment") Equipment equipment,
                                        @RequestParam(value = "workplace_id", required = false) Long workplaceId,
                                        @RequestParam(value = "typeEquipment") String typeEquipment,
                                        @RequestParam(name = "redirect", required = false) String redirect) {
        ModelAndView modelAndView = new ModelAndView();

        Equipment equipmentFromDb = equipmentManager.getEquipmentByUid(equipment.getUid());
        if (equipmentFromDb != null && equipmentFromDb.getId() != equipment.getId()) {
            modelAndView.setViewName("/config/equipment");
            modelAndView.addObject("error", String.format("%s уже существует", equipment.getUid()));
            modelAndView.addObject("equipment", equipment);
            modelAndView.addObject("typeEquipment", typeEquipment);
        } else {
            if(equipment != null && workplaceId > 0) {
                Workplace workplace = workplaceManager.getWorkplaceById(workplaceId);
                equipment.setWorkplace(workplace);
            }
            equipmentManager.save(equipment);
            modelAndView.setViewName("redirect:/" + redirect);
        }

        if (redirect == null) {
            redirect = "";
        }
        modelAndView.addObject("redirect", redirect);

        return modelAndView;
    }

    @GetMapping("/deleteEquipment")
    public ModelAndView deleteEquipment(@RequestParam(name = "id") Long id,
                                        @RequestParam(value = "typeEquipment") String typeEquipment,
                                        @RequestParam(name = "redirect") String redirect) {
        Equipment equipment = equipmentManager.getEquipmentById(id);
        equipmentManager.delete(equipment);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/" + redirect);
        modelAndView.addObject("typeEquipment", typeEquipment);
        return modelAndView;
    }
}
