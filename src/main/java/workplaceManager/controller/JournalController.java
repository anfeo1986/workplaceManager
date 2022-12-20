package workplaceManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.*;
import workplaceManager.db.domain.Journal;
import workplaceManager.db.domain.Users;
import workplaceManager.db.service.JournalManager;
import workplaceManager.db.service.UserManager;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class JournalController {
    JournalManager journalManager;

    @Autowired
    public void setJournalManager(JournalManager journalManager) {
        this.journalManager = journalManager;
    }

    UserManager userManager;

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @GetMapping(Pages.journal)
    public ModelAndView getFormJournal(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(Pages.journal);

        SimpleDateFormat formatWithoutTime = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date dateStart = null;
        try {
            if(request.getParameter(Components.dateStartForFilterJournal) != null) {
                dateStart = format.parse(request.getParameter(Components.dateStartForFilterJournal));
            } else {
                dateStart = format.parse(formatWithoutTime.format(new Date()) + "T00:00");
            }
        } catch (Exception e) {
        }
        Date dateEnd = null;
        try {
            if(request.getParameter(Components.dateEndForFilterJournal) != null) {
                dateEnd = format.parse(request.getParameter(Components.dateEndForFilterJournal));
            } else {
                dateEnd = format.parse(formatWithoutTime.format(new Date()) + "T23:59");
            }
        } catch (Exception e) {
        }

        TypeObject typeObject = null;
        if (request.getParameter(Components.typeObjectForFilterJournal) != null &&
                !request.getParameter(Components.typeObjectForFilterJournal).isEmpty()) {
            typeObject = TypeObject.valueOf((String) request.getParameter(Components.typeObjectForFilterJournal));
        }
        Long idObject = null;
        if (request.getParameter(Components.objectForFilterJournal) != null) {
            idObject = Long.parseLong(request.getParameter(Components.objectForFilterJournal));
        }
        TypeEvent typeEvent = null;
        if (request.getParameter(Components.eventForFilterJournal) != null &&
                !request.getParameter(Components.eventForFilterJournal).isEmpty()) {
            typeEvent = TypeEvent.valueOf((String) request.getParameter(Components.eventForFilterJournal));
        }
        TypeParameter typeParameter = null;
        if (request.getParameter(Components.parameterForFilterJournal) != null &&
                !request.getParameter(Components.parameterForFilterJournal).isEmpty()) {
            typeParameter = TypeParameter.valueOf((String) request.getParameter(Components.parameterForFilterJournal));
        }
        Users userForFilter = null;
        if (request.getParameter(Components.userForFilterJournal) != null) {
            Long userId = Long.parseLong(request.getParameter(Components.userForFilterJournal));
            userForFilter = userManager.getUserById(userId, true);
        }
        StateObject stateObject = null;
        if (request.getParameter(Components.stateObjectForFilterJournal) != null &&
                !request.getParameter(Components.stateObjectForFilterJournal).isEmpty()) {
            stateObject = StateObject.valueOf((String) request.getParameter(Components.stateObjectForFilterJournal));
        }

        List<Journal> journalList = journalManager.getJournalList(typeObject, idObject, typeEvent,
                typeParameter, userForFilter, stateObject, dateStart, dateEnd);
        SortedMap<String, Long> objectIdList = journalManager.getObjectIdListForTypeObject(typeObject, stateObject);
        List<Users> usersList = journalManager.getUserIdForFilter(true);

        modelAndView.addObject(Parameters.journalList, journalList == null ? new ArrayList<>() : journalList);

        modelAndView.addObject(Parameters.journalFilterTypeObject, request.getParameter(Components.typeObjectForFilterJournal));

        modelAndView.addObject(Parameters.journalFilterObjectId, idObject);
        modelAndView.addObject(Parameters.journalObjectIdListForFilter, objectIdList == null ? new TreeMap<>() : objectIdList);

        modelAndView.addObject(Parameters.journalFilterTypeEvent, request.getParameter(Components.eventForFilterJournal));
        modelAndView.addObject(Parameters.journalTypeEventListForFilter, getEventListForTypeObject(typeObject));

        modelAndView.addObject(Parameters.journalFilterTypeParameter, request.getParameter(Components.parameterForFilterJournal));
        modelAndView.addObject(Parameters.journalParametersListForFilter, getParameterListForTypeObject(typeObject));

        modelAndView.addObject(Parameters.journalFilterUser, userForFilter);
        modelAndView.addObject(Parameters.journalUsersListForFilter, usersList);

        modelAndView.addObject(Parameters.journalFilterStateObject, request.getParameter(Components.stateObjectForFilterJournal));
        modelAndView.addObject(Parameters.journalStateObjectListForFilter, getStateObjectListForTypeObject(typeObject));

        modelAndView.addObject(Parameters.journalDateStartForFilter, dateStart);
        modelAndView.addObject(Parameters.journalDateEndForFilter, dateEnd);

        return modelAndView;
    }

    private List<StateObject> getStateObjectListForTypeObject(TypeObject typeObject) {
        List<StateObject> stateObjectList = new ArrayList<>();
        for (StateObject stateObject : StateObject.values()) {
            stateObjectList.add(stateObject);
        }
        return stateObjectList;
    }

    private List<TypeParameter> getParameterListForTypeObject(TypeObject typeObject) {
        List<TypeParameter> parameterList = new ArrayList<>();
        if (typeObject == null) {
            for (TypeParameter typeParameter : TypeParameter.values()) {
                parameterList.add(typeParameter);
            }
            return parameterList;
        }

        if (TypeObject.COMPUTER.equals(typeObject)) {
            parameterList.add(TypeParameter.UID);
            parameterList.add(TypeParameter.MANUFACTURER);
            parameterList.add(TypeParameter.MODEL);
            parameterList.add(TypeParameter.WORKPLACE);
            parameterList.add(TypeParameter.ACCOUNTING1C);
            parameterList.add(TypeParameter.IP);
            parameterList.add(TypeParameter.NET_NAME);
            parameterList.add(TypeParameter.OS);
            parameterList.add(TypeParameter.MOTHERBOARD);
            parameterList.add(TypeParameter.PROCESSOR);
            parameterList.add(TypeParameter.RAM);
            parameterList.add(TypeParameter.HARDDRIVE);
            parameterList.add(TypeParameter.VIDEOCARD);
        } else if (TypeObject.MONITOR.equals(typeObject) ||
                TypeObject.PRINTER.equals(typeObject) ||
                TypeObject.SCANNER.equals(typeObject) ||
                TypeObject.MFD.equals(typeObject) ||
                TypeObject.UPS.equals(typeObject)) {
            parameterList.add(TypeParameter.UID);
            parameterList.add(TypeParameter.MANUFACTURER);
            parameterList.add(TypeParameter.MODEL);
            parameterList.add(TypeParameter.WORKPLACE);
            parameterList.add(TypeParameter.ACCOUNTING1C);
        } else if (TypeObject.USER.equals(typeObject)) {

        } else if (TypeObject.ACCOUNTING1C.equals(typeObject)) {
            parameterList.add(TypeParameter.ACCOUNTING1C_INVENTORY_NUMBER);
            parameterList.add(TypeParameter.ACCOUNTING1C_TITLE);
        } else if (TypeObject.WORKPLACE.equals(typeObject)) {
            parameterList.add(TypeParameter.WORKPLACE_TITLE);
        }

        return parameterList;
    }

    private List<TypeEvent> getEventListForTypeObject(TypeObject typeObject) {
        List<TypeEvent> eventList = new ArrayList<>();
        if (typeObject == null) {
            for (TypeEvent typeEvent : TypeEvent.values()) {
                eventList.add(typeEvent);
            }
            return eventList;
        }

        if (TypeObject.COMPUTER.equals(typeObject)) {
            eventList.add(TypeEvent.ADD);
            eventList.add(TypeEvent.UPDATE);
            eventList.add(TypeEvent.READ_CONFIG_COMPUTER);
            eventList.add(TypeEvent.DELETE);
        } else if (TypeObject.USER.equals(typeObject)) {
            eventList.add(TypeEvent.USER_REGISTER);
            eventList.add(TypeEvent.USER_LOGIN);
            eventList.add(TypeEvent.USER_DELETE);
        } else if (TypeObject.ACCOUNTING1C.equals(typeObject)) {
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
