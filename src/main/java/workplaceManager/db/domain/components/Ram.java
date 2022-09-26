package workplaceManager.db.domain.components;

import lombok.Data;
import workplaceManager.db.domain.Computer;

import javax.persistence.*;

@Entity
@Data
public class Ram {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String model;

    @Column
    private TypeRam typeRam;

    @Column
    private String amount;

    @Column
    private String frequency;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "computer")
    private Computer computer;
}
