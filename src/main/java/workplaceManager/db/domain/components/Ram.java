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
public class Ram implements Serializable {
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

    @Column
    private String deviceLocator;

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "computer")
    private Computer computer;

    @Override
    @Transient
    public String toString() {
        return String.format("%s (%s, %s Gb, %s, %s)", model, typeRam, amount, frequency, deviceLocator);
    }

    @Transient
    public static boolean isEmpty(Ram ram) {
        if (ram == null) {
            return true;
        }
        if ((ram.getModel() == null || ram.getModel() == "") &&
                (ram.getTypeRam() == null) &&
                (ram.getAmount() == null || ram.getAmount() == "") &&
                (ram.getFrequency() == null || ram.getFrequency() == "") &&
                (ram.getDeviceLocator() == null || ram.getDeviceLocator() == "")) {
            return true;
        }

        return false;
    }
}
