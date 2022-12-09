package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.Components;
import workplaceManager.Pages;
import workplaceManager.Parameters;
import workplaceManager.TypeObject;
import workplaceManager.db.domain.Journal;
import workplaceManager.db.service.JournalManager;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class JournalController {
    JournalManager journalManager;

    @Autowired
    public void setJournalManager(JournalManager journalManager) {
        this.journalManager = journalManager;
    }

    @GetMapping(Pages.journal)
    public ModelAndView getFormJournal(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(Pages.journal);
        TypeObject typeObject = null;
        if(request.getParameter(Components.typeObjectForJournal) != null &&
                !request.getParameter(Components.typeObjectForJournal).isEmpty()) {
            typeObject = TypeObject.valueOf((String) request.getParameter(Components.typeObjectForJournal));
        }

        List<Journal> journalList = journalManager.getJournalList(typeObject);

        modelAndView.addObject(Parameters.journalFilterTypeObject, request.getParameter(Components.typeObjectForJournal));
        modelAndView.addObject(Parameters.journalList, journalList==null ? new ArrayList<>() : journalList);

        return modelAndView;
    }
}
