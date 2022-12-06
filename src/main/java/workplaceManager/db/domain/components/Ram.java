package workplaceManager.db.domain.components;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import workplaceManager.db.domain.Computer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
public class Ram implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String model;

    @Column
    private TypeRam typeRam;

    @Column
    private String amount;

    @Column
    private String frequency;

    @Column
    private String deviceLocator;

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "computer")
    private Computer computer;

    @Override
    @Transient
    public String toString() {
        return String.format("%s (%s, %s Gb, %s, %s)", model, typeRam, amount, frequency, deviceLocator);
    }

    @Transient
    public static boolean isEmpty(Ram ram) {
        if (ram == null) {
            return true;
        }
        if ((ram.getModel() == null || ram.getModel() == "") &&
                (ram.getTypeRam() == null) &&
                (ram.getAmount() == null || ram.getAmount() == "") &&
                (ram.getFrequency() == null || ram.getFrequency() == "") &&
                (ram.getDeviceLocator() == null || ram.getDeviceLocator() == "")) {
            return true;
        }

        return false;
    }

    @Transient
    public static boolean equalsRam(Ram ram1, Ram ram2) {
        if (ram1 == null && ram2 == null) {
            return true;
        }

        if ((ram1 == null && ram2 != null) ||
                (ram1 != null && ram2 == null)) {
            return false;
        }
        if (ram1.getId() == ram2.getId() &&
                StringUtils.equals(ram1.getModel(), ram2.getModel()) &&
                ram1.getTypeRam() == ram2.getTypeRam() &&
                StringUtils.equals(ram1.getAmount(), ram2.getAmount()) &&
                StringUtils.equals(ram1.getFrequency(), ram2.getFrequency()) &&
                StringUtils.equals(ram1.getDeviceLocator(), ram2.getDeviceLocator())) {
            return true;
        }
        return false;
    }

    @Transient
    public static boolean equalsRamList(List<Ram> ramList1, List<Ram> ramList2) {
        if ((ramList1 == null && ramList2 == null) ||
                (ramList1.isEmpty() && ramList2.isEmpty())) {
            return true;
        }
        if (ramList1.size() != ramList2.size()) {
            return false;
        }
        if ((ramList1 == null && ramList2 != null) ||
                (ramList1 != null && ramList2 == null)) {
            return false;
        }
        for(Ram ram1 : ramList1) {
            boolean isExist = false;
            for(Ram ram2 : ramList2) {
                if(equalsRam(ram1, ram2)) {
                    isExist = true;
                    break;
                }
            }
            if(!isExist) {
                return false;
            }
        }
        return true;
    }
}
