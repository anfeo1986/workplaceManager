package workplaceManager.db.domain.components;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import workplaceManager.db.domain.Computer;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class HardDrive {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String model;

    @Column
    private String size;

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "computer")
    private Computer computer;

    @Override
    @Transient
    public String toString() {
        return String.format("%s (%s Gb)", model, size);
    }

    @Transient
    public static boolean isEmpty(HardDrive hardDrive) {
        if (hardDrive == null) {
            return true;
        }
        if (hardDrive.getModel() == null || hardDrive.getModel() == "") {
            return true;
        }

        return false;
    }

    @Transient
    public static boolean equalsHardDrive(HardDrive hardDrive1, HardDrive hardDrive2) {
        if (hardDrive1 == null && hardDrive2 == null) {
            return true;
        }
        if ((hardDrive1 == null && hardDrive2 != null) ||
                (hardDrive1 != null && hardDrive2 == null)) {
            return false;
        }
        if (hardDrive1.getId() == hardDrive2.getId() &&
                StringUtils.equals(hardDrive1.getModel(), hardDrive2.getModel()) &&
                StringUtils.equals(hardDrive1.getSize(), hardDrive2.getSize())) {
            return true;
        }
        return false;
    }

    @Transient
    public static boolean equalsHardDriveList(List<HardDrive> hardDriveList1, List<HardDrive> hardDriveList2) {
        if ((hardDriveList1 == null && hardDriveList2 == null) ||
                (hardDriveList1.isEmpty() && hardDriveList2.isEmpty())) {
            return true;
        }
        if (hardDriveList1.size() != hardDriveList2.size()) {
            return false;
        }
        if ((hardDriveList1 == null && hardDriveList2 != null) ||
                (hardDriveList1 != null && hardDriveList2 == null)) {
            return false;
        }
        for(HardDrive hardDrive1 : hardDriveList1) {
            boolean isExist = false;
            for(HardDrive hardDrive2 : hardDriveList2) {
                if(equalsHardDrive(hardDrive1, hardDrive2)) {
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
