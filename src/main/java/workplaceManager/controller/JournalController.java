package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.*;
import workplaceManager.db.domain.Journal;
import workplaceManager.db.service.JournalManager;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

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
        if (request.getParameter(Components.typeObjectForJournal) != null &&
                !request.getParameter(Components.typeObjectForJournal).isEmpty()) {
            typeObject = TypeObject.valueOf((String) request.getParameter(Components.typeObjectForJournal));
        }
        Long idObject = null;
        if (request.getParameter(Components.objectForJournal) != null) {
            idObject = Long.parseLong(request.getParameter(Components.objectForJournal));
        }
        TypeEvent typeEvent = null;
        if(request.getParameter(Components.eventForJournal) != null &&
        !request.getParameter(Components.eventForJournal).isEmpty()) {
            typeEvent = TypeEvent.valueOf((String) request.getParameter(Components.eventForJournal));
        }

        List<Journal> journalList = journalManager.getJournalList(typeObject, idObject, typeEvent);
        SortedMap<String, Long> objectIdList = journalManager.getObjectIdListForTypeObject(typeObject);

        /*SortedMap<String, Long> objectIdList = new TreeMap<>();
        List<Journal> journalList = new ArrayList<>();
        for (Journal journal : journalListFromDB) {
            if (journal.getObject() == null) {
                continue;
            }
            if (idObject != null && idObject > 0) {
                if (idObject.equals(journal.getIdObject())) {
                    journalList.add(journal);
                }
            } else {
                journalList.add(journal);
            }
            if (!objectIdList.containsKey(journal.getObject().toString())) {
                objectIdList.put(journal.getObject().toString(), journal.getIdObject());
            }
        }*/

        modelAndView.addObject(Parameters.journalFilterTypeObject, request.getParameter(Components.typeObjectForJournal));
        modelAndView.addObject(Parameters.journalList, journalList == null ? new ArrayList<>() : journalList);
        modelAndView.addObject(Parameters.journalObjectIdList, objectIdList == null ? new TreeMap<>() : objectIdList);
        modelAndView.addObject(Parameters.journalFilterObjectId, idObject);
        modelAndView.addObject(Parameters.journalTypeEventList, getEventListForTypeObject(typeObject));
        modelAndView.addObject(Parameters.journalFilterTypeEvent, request.getParameter(Components.eventForJournal));

        return modelAndView;
    }

    private List<TypeEvent> getEventListForTypeObject(TypeObject typeObject) {
        List<TypeEvent> eventList = new ArrayList<>();
        if(typeObject == null) {
            for(TypeEvent typeEvent : TypeEvent.values()) {
                eventList.add(typeEvent);
            }
            return eventList;
        }

        if(TypeObject.COMPUTER.equals(typeObject)) {
            eventList.add(TypeEvent.ADD);
            eventList.add(TypeEvent.UPDATE);
            eventList.add(TypeEvent.READ_CONFIG_COMPUTER);
            eventList.add(TypeEvent.DELETE);
        } else if(TypeObject.USER.equals(typeObject)){
            eventList.add(TypeEvent.USER_REGISTER);
            eventList.add(TypeEvent.USER_LOGIN);
            eventList.add(TypeEvent.USER_DELETE);
        } else if(TypeObject.ACCOUNTING1C.equals(typeObject)) {
            eventList.add(TypeEvent.ADD);
            eventList.add(TypeEvent.UPDATE);
            eventList.add(TypeEvent.ACCOUNTING1C_MOVING);
            eventList.add(TypeEvent.ACCOUNTING1C_CANCELLATION);
        } else {
            eventList.add(TypeEvent.ADD);
            eventList.add(TypeEvent.UPDATE);
            eventList.add(TypeEvent.DELETE);
        }

        return eventList;
    }
}
