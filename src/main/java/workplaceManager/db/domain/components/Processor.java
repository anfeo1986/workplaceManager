package workplaceManager.db.domain.components;

import lombok.Data;
import workplaceManager.db.domain.Computer;

import javax.persistence.*;

@Entity
@Data
public class Processor {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String model;

    @Column
    private String numberOfCores;

    @Column
    private String frequency;

    @Column
    private String socket;

    @Column
    private TypeRam typeRam;

    @Column
    private String ramMaxAmount;

    @Column
    private String graphicsCore;

    @OneToOne(mappedBy = "processor")
    private Computer computer;
}
