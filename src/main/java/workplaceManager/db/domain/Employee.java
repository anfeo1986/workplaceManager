package workplaceManager.db.domain;

import lombok.Data;
import workplaceManager.db.service.EntityManager;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class Employee implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String name;

    @Column
    private String post;

    @ManyToOne(optional = true,cascade = CascadeType.ALL)
    @JoinColumn(name = "workplace")
    private Workplace workplace;

}
