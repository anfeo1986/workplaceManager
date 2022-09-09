package workplaceManager.db.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Workplace implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String title;

    @OneToMany(mappedBy = "workplace", fetch = FetchType.LAZY)
    private List<Employee> employeeList = new ArrayList<>();

    @OneToMany(mappedBy = "workplace", fetch = FetchType.LAZY)
    private List<Equipment> equipmentList = new ArrayList<>();
}
