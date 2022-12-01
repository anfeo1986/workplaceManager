package workplaceManager.db.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OperationSystem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private TypeOS typeOS;

    @Column
    private String vendor;

    @Column
    private String version;

    @Override
    @Transient
    public String toString() {
        return String.format("%s (%s)", vendor, version);
    }

    @Transient
    public static boolean equalsOS(OperationSystem operationSystem1, OperationSystem operationSystem2) {
        if (operationSystem1 == null && operationSystem2 == null) {
            return true;
        }
        if ((operationSystem1 == null && operationSystem2 != null) &&
                (operationSystem1 != null && operationSystem2 == null)) {
            return false;
        }
        if (operationSystem1.getId() == operationSystem2.getId() &&
                operationSystem1.getTypeOS() == operationSystem2.getTypeOS() &&
                StringUtils.equals(operationSystem1.getVendor(), operationSystem2.getVendor()) &&
                StringUtils.equals(operationSystem1.getVersion(), operationSystem2.getVersion())) {
            return true;
        }
        return false;
    }

}
