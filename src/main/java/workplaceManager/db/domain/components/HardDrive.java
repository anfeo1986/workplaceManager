package workplaceManager.db.domain.components;

import lombok.Data;
import workplaceManager.db.domain.Computer;

import javax.persistence.*;

@Entity
@Data
public class HardDrive {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //@Column
    //private String manufacturer;

    @Column
    private String model;

    //@Column
    //private String amount;

    //@Column
    //private TypeHardDrive typeHardDrive;

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "computer")
    private Computer computer;

    @Override
    public String toString() {
        return model;
    }
}
