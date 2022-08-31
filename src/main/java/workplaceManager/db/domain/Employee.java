package workplaceManager.db.domain;

import lombok.Data;
import workplaceManager.db.service.EntityManager;

import javax.persistence.*;

@Entity
@Data
public class Employee {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String name;

    @Column
    private String post;

    @OneToOne(optional = true,cascade = CascadeType.ALL)
    @JoinColumn(name = "workplace_id")
    private Workplace workplace;

}
