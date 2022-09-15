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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String inventoryNumber;

    @Column
    private String title;

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "employee")
    private Employee employee;

    @OneToMany(mappedBy = "accounting1С", fetch = FetchType.LAZY)
    private List<Equipment> equipmentList = new ArrayList<>();
}
