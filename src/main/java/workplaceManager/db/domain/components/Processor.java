package workplaceManager.db.domain.components;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import workplaceManager.db.domain.Computer;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class Processor implements Serializable {
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

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "computer")
    private Computer computer;

    @Override
    @Transient
    public String toString() {
        return String.format("%s (Кол-во ядер: %s, %s, %s)", model, numberOfCores, frequency, socket);
    }

    @Transient
    public static boolean isEmpty(Processor processor) {
        if (processor == null) {
            return true;
        }
        if ((processor.getModel() == null || processor.getModel() == "") &&
                (processor.getNumberOfCores() == null || processor.getNumberOfCores() == "") &&
                (processor.getFrequency() == null || processor.getFrequency() == "") &&
                (processor.getSocket() == null || processor.getSocket() == "")) {
            return true;
        }
        return false;
    }
}
