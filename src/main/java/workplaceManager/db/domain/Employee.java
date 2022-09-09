package workplaceManager.db.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import workplaceManager.db.service.EntityManager;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "workplace")
    private Workplace workplace;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Accounting1C> accounting1СList = new ArrayList<>();
}
