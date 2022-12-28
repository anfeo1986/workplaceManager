package workplaceManager.db.domain;

import lombok.Getter;
import lombok.Setter;
import workplaceManager.db.domain.components.HardDrive;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class VirtualMachine {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String ip;

    @Column
    private TypeOS typeOS;

    @Column
    private String vendor;

    @Column
    private String version;

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "computer")
    private Computer computer;

    @Transient
    public static boolean isEmpty(VirtualMachine virtualMachine) {
        if (virtualMachine == null) {
            return true;
        }
        if ((virtualMachine.getIp() == null || virtualMachine.getIp() == "") &&
                (virtualMachine.getVendor() == null || virtualMachine.getVendor() == "") &&
                (virtualMachine.getVersion() == null || virtualMachine.getVersion() == "")) {
            return true;
        }

        return false;
    }
}
