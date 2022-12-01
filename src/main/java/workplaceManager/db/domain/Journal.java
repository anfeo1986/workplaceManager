package workplaceManager.db.domain;

import lombok.Getter;
import lombok.Setter;
import workplaceManager.TypeEvent;
import workplaceManager.TypeObject;

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
    private Date time;

    @Column
    private String typeObject;

    @Column
    private Long idObject;

    public Journal() {

    }

    public Journal(TypeEvent typeEvent, TypeObject typeObject, Object obj) {
        this.typeEvent = typeEvent.name();
        this.time = new Date();
        this.typeObject = typeObject.name();
        if (TypeObject.COMPUTER.equals(typeObject)) {
            addEventAndIdObject(typeEvent, typeObject, obj, ((Computer) obj).getId());
        } else if (TypeObject.MONITOR.equals(typeObject)) {
            addEventAndIdObject(typeEvent, typeObject, obj, ((Monitor) obj).getId());
        } else if (TypeObject.PRINTER.equals(typeObject)) {
            addEventAndIdObject(typeEvent, typeObject, obj, ((Printer) obj).getId());
        } else if (TypeObject.SCANNER.equals(typeObject)) {
            addEventAndIdObject(typeEvent, typeObject, obj, ((Scanner) obj).getId());
        } else if (TypeObject.MFD.equals(typeObject)) {
            addEventAndIdObject(typeEvent, typeObject, obj, ((Mfd) obj).getId());
        } else if (TypeObject.UPS.equals(typeObject)) {
            addEventAndIdObject(typeEvent, typeObject, obj, ((Ups) obj).getId());
        } else if (TypeObject.ACCOUNTING1C.equals(typeObject)) {
            addEventAndIdObject(typeEvent, typeObject, obj, ((Accounting1C) obj).getId());
        }
    }

    public Journal(TypeEvent typeEvent, TypeObject typeObject, Object obj, String oldValue, String newValue) {
        this(typeEvent, typeObject, obj);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    private void addEventAndIdObject(TypeEvent typeEvent, TypeObject typeObject, Object object, Long idObject) {
        this.event = String.format("%s. Тип объекта: %s. Объект: %s",
                typeEvent, typeObject, object);
        this.idObject = idObject;
    }

}
