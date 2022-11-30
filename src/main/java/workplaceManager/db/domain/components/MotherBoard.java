package workplaceManager.db.domain.components;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import workplaceManager.db.domain.Computer;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class MotherBoard {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String manufacturer;

    @Column
    private String model;

    @OneToOne(mappedBy = "motherBoard")
    private Computer computer;

    @Override
    public String toString() {
        return String.format("%s %s", manufacturer, model);
    }

    @Transient
    public static boolean equalsMotherBoard(MotherBoard motherBoard1, MotherBoard motherBoard2) {
        if (motherBoard1 == null && motherBoard2 == null) {
            return true;
        }
        if ((motherBoard1 == null && motherBoard2 != null) ||
                (motherBoard1 != null && motherBoard2 == null)) {
            return false;
        }
        if (motherBoard1.getId() == motherBoard2.getId() &&
                StringUtils.equals(motherBoard1.getManufacturer(), motherBoard2.getManufacturer()) &&
                StringUtils.equals(motherBoard1.getModel(), motherBoard2.getModel())) {
            return true;
        }
        return false;
    }

}
