package workplaceManager.db.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "typeEquipment",
        discriminatorType = DiscriminatorType.STRING
)
@DiscriminatorValue(value = TypeEquipment.EQUIPMENT)
@Data
public class Equipment<T> implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "uid")
    private String uid = "";

    @Column(name = "manufacturer")
    private String manufacturer = "";

    @Column(name = "model")
    private String model = "";

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "accounting1С")
    private Accounting1C accounting1C;

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "workplace")
    private Workplace workplace;

    @Override
    public String toString() {
        return String.format("%s (%s %s)", uid, manufacturer, model);
    }

    public T getChildFromEquipment(String typeEquipment) {
        Equipment equipment = new Equipment();

        if(TypeEquipment.COMPUTER.equals(typeEquipment)) {
            equipment = new Computer();
        } else if(TypeEquipment.MONITOR.equals(typeEquipment)) {
            equipment = new Monitor();
        } else if(TypeEquipment.PRINTER.equals(typeEquipment)) {
            equipment = new Printer();
        } else if(TypeEquipment.SCANNER.equals(typeEquipment)) {
            equipment = new Scanner();
        } else if(TypeEquipment.MFD.equals(typeEquipment)) {
            equipment = new Mfd();
        } else if(TypeEquipment.UPS.equals(typeEquipment)) {
            equipment = new Ups();
        }

        equipment.setId(this.getId());
        equipment.setUid(this.getUid());
        equipment.setManufacturer(this.getManufacturer());
        equipment.setModel(this.getModel());
        equipment.setWorkplace(this.getWorkplace());
        equipment.setAccounting1C(this.getAccounting1C());

        return (T) equipment;
    }
}
