package workplaceManager.db.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;
import workplaceManager.TypeEvent;
import workplaceManager.TypeObject;
import workplaceManager.TypeParameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Journal {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String event;

    @Column
    private String oldValue;

    @Column
    private String newValue;

    @Column
    private String typeEvent;

    @Column
    private String parameter;

    @Column
    private Date time;

    @Column
    private String typeObject;

    @Column
    private String objectStr;

    @Column
    private Long idObject;

    @Column
    private Long idUser;

    @Column
    private String userStr;

    @Transient
    private Object object;

    @Transient
    private Users user;

    public Journal() {

    }

    public Journal(TypeEvent typeEvent, TypeObject typeObject, Object obj, Users user) {
        this(typeEvent, typeObject, obj);
        if (user != null) {
            this.idUser = user.getId();
            this.userStr = user.toString();
        }
    }

    public Journal(TypeEvent typeEvent, TypeObject typeObject, Object obj) {
        this.typeEvent = typeEvent.name();
        this.event = typeEvent.getTitle();
        this.time = new Date();
        this.typeObject = typeObject.name();
        this.objectStr = obj != null ? obj.toString() : "";

        if (TypeObject.COMPUTER.equals(typeObject)) {
            this.idObject = ((Computer) obj).getId();
            //addEventAndIdObject(typeEvent, typeObject, obj, ((Computer) obj).getId());
        } else if (TypeObject.MONITOR.equals(typeObject)) {
            //addEventAndIdObject(typeEvent, typeObject, obj, ((Monitor) obj).getId());
            this.idObject = ((Monitor) obj).getId();
        } else if (TypeObject.PRINTER.equals(typeObject)) {
            //addEventAndIdObject(typeEvent, typeObject, obj, ((Printer) obj).getId());
            this.idObject = ((Printer) obj).getId();
        } else if (TypeObject.SCANNER.equals(typeObject)) {
            this.idObject = ((Scanner) obj).getId();
            //addEventAndIdObject(typeEvent, typeObject, obj, ((Scanner) obj).getId());
        } else if (TypeObject.MFD.equals(typeObject)) {
            this.idObject = ((Mfd) obj).getId();
            //addEventAndIdObject(typeEvent, typeObject, obj, ((Mfd) obj).getId());
        } else if (TypeObject.UPS.equals(typeObject)) {
            this.idObject = ((Ups) obj).getId();
            //addEventAndIdObject(typeEvent, typeObject, obj, ((Ups) obj).getId());
        } else if (TypeObject.ACCOUNTING1C.equals(typeObject)) {
            this.idObject = ((Accounting1C) obj).getId();
            //addEventAndIdObject(typeEvent, typeObject, obj, ((Accounting1C) obj).getId());
        } else if (TypeObject.USER.equals(typeObject)) {
            this.idObject = ((Users) obj).getId();
        } else if(TypeObject.EMPLOYEE.equals(typeObject)) {
            this.idObject = ((Employee) obj).getId();
        } else if(TypeObject.WORKPLACE.equals(typeObject)) {
            this.idObject = ((Workplace) obj).getId();
        }
    }

    public Journal(TypeEvent typeEvent, TypeObject typeObject, Object obj,
                   String oldValue, String newValue, TypeParameter parameter, Users user) {
        this(typeEvent, typeObject, obj, user);
        this.oldValue = oldValue;
        this.newValue = newValue;
        if(parameter != null) {
            this.parameter = parameter.name();
        }
    }


}
