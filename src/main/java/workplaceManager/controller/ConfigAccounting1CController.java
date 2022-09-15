package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.db.domain.Accounting1C;
import workplaceManager.db.service.Accounting1CManager;

@Controller
@RequestMapping("/config/accounting1c")
public class ConfigAccounting1CController {
    private Accounting1CManager accounting1CManager;

    @Autowired
    public void setAccounting1CManager(Accounting1CManager accounting1CManager) {
        this.accounting1CManager = accounting1CManager;
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



        return modelAndView;
    }

    @PostMapping("/addAccounting1CPost")
    public ModelAndView addAccounting1C(@ModelAttribute("accounting1c") Accounting1C accounting1C,
                                        @RequestParam(name = "redirect", required = false) String redirect) {
        ModelAndView modelAndView = new ModelAndView("/config/accounting1c");

        Accounting1C accounting1C1FromDB = accounting1CManager.getAccounting1CByInventoryNumberAndTitle(
                accounting1C.getInventoryNumber(), accounting1C.getTitle());
        if(accounting1C1FromDB != null) {
            modelAndView.addObject("error", String.format("%s (%s) уже существует",
                    accounting1C.getInventoryNumber(), accounting1C.getTitle()));
            modelAndView.addObject("accounting1c", accounting1C);
        } else {

        }

        return modelAndView;
    }
}
