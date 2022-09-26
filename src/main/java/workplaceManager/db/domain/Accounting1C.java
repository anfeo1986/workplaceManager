package workplaceManager.db.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Accounting1C implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

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
}
