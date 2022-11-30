package workplaceManager.db.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Accounting1C implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String inventoryNumber;

    @Column
    private String title;

    @ManyToOne(optional = true, cascade = CascadeType.MERGE)
    @JoinColumn(name = "employee")
    private Employee employee;

    @OneToMany(mappedBy = "accounting1C", fetch = FetchType.LAZY)
    private List<Equipment> equipmentList = new ArrayList<>();

    public Accounting1C() {

    }

    public Accounting1C(String inventoryNumber, String title, Employee employee) {
        this.inventoryNumber = inventoryNumber;
        this.title = title;
        this.employee = employee;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s)", title, employee == null ? "" : employee.getName(), inventoryNumber);
    }

    @Transient
    public static boolean equalsAccounting1C(Accounting1C accounting1C1, Accounting1C accounting1C2) {
        if (accounting1C1 == null && accounting1C2 == null) {
            return true;
        }
        if ((accounting1C1 == null && accounting1C2 != null) ||
                (accounting1C1 != null && accounting1C2 == null)) {
            return false;
        }
        if (accounting1C1.getId() == accounting1C2.getId() &&
                StringUtils.equals(accounting1C1.getTitle(), accounting1C2.getTitle()) &&
                StringUtils.equals(accounting1C1.getInventoryNumber(), accounting1C2.getInventoryNumber())) {
            return true;
        }
        return false;
    }
}
