package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.Pages;
import workplaceManager.Parameters;
import workplaceManager.db.domain.Journal;
import workplaceManager.db.service.JournalManager;

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
    public ModelAndView getFormJournal() {
        ModelAndView modelAndView = new ModelAndView(Pages.journal);
        List<Journal> journalList = journalManager.getJournalList();
        modelAndView.addObject(Parameters.journalList, journalList==null ? new ArrayList<>() : journalList);

        return modelAndView;
    }
}
