package workplaceManager.db.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Workplace {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String title;

    @OneToOne(optional = false, mappedBy = "workplace")
    private Employee employee;
}
