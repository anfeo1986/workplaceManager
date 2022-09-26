package workplaceManager.db.domain.components;

import lombok.Data;
import workplaceManager.db.domain.Computer;

import javax.persistence.*;

@Entity
@Data
public class MotherBoard {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String manufacturer;

    @Column
    private String model;

    @Column
    private String socket;

    @Column
    private TypeRam typeRam;

    @Column
    private String ramFrequency;

    @Column
    private String ramMaxAmount;

    @OneToOne(mappedBy = "motherBoard")
    private Computer computer;

    @Override
    public String toString() {
        return String.format("%s %s", manufacturer, model);
    }

}
