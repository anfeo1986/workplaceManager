package workplaceManager.db.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import workplaceManager.db.domain.components.HardDrive;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class VirtualMachine {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String ip = "";

    @Column
    private TypeOS typeOS = TypeOS.windows;

    @Column
    private String vendor = "";

    @Column
    private String version = "";

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "computer")
    private Computer computer;

    @Override
    @Transient
    public String toString() {
        return String.format("%s (Тип: %s, OC: %s (%s))",ip, typeOS, vendor, version);
    }

    @Transient
    public static boolean equalsVirtualMachine(VirtualMachine virtualMachine1, VirtualMachine virtualMachine2) {
        if (virtualMachine1 == null && virtualMachine2 == null) {
            return true;
        }
        if ((virtualMachine1 == null && virtualMachine2 != null) ||
                (virtualMachine1 != null && virtualMachine2 == null)) {
            return false;
        }
        if (virtualMachine1.getId() == virtualMachine2.getId() &&
                StringUtils.equals(virtualMachine1.getIp(), virtualMachine2.getIp()) &&
                StringUtils.equals(virtualMachine1.getVendor(), virtualMachine2.getVendor()) &&
                StringUtils.equals(virtualMachine1.getVersion(), virtualMachine2.getVersion()) &&
                virtualMachine1.getTypeOS().equals(virtualMachine2.getTypeOS())) {
            return true;
        }
        return false;
    }

    @Transient
    public static boolean equalsVirtualMachineList(List<VirtualMachine> virtualMachineList1,
                                                   List<VirtualMachine> virtualMachineList2) {
        if ((virtualMachineList1 == null && virtualMachineList2 == null) ||
                (virtualMachineList1.isEmpty() && virtualMachineList2.isEmpty())) {
            return true;
        }
        if (virtualMachineList1.size() != virtualMachineList2.size()) {
            return false;
        }
        if ((virtualMachineList1 == null && virtualMachineList2 != null) ||
                (virtualMachineList1 != null && virtualMachineList2 == null)) {
            return false;
        }
        for (VirtualMachine virtualMachine1 : virtualMachineList1) {
            boolean isExist = false;
            for (VirtualMachine virtualMachine22 : virtualMachineList2) {
                if (equalsVirtualMachine(virtualMachine1, virtualMachine22)) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                return false;
            }
        }
        return true;
    }
}
