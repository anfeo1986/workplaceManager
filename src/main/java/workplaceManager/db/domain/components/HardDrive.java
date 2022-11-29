package workplaceManager.db.domain.components;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import workplaceManager.db.domain.Computer;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class HardDrive {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String model;

    @Column
    private String size;

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "computer")
    private Computer computer;

    @Override
    @Transient
    public String toString() {
        return String.format("%s (%s Gb)", model, size);
    }

    @Transient
    public static boolean isEmpty(HardDrive hardDrive) {
        if (hardDrive == null) {
            return true;
        }
        if (hardDrive.getModel() == null || hardDrive.getModel() == "") {
            return true;
        }

        return false;
    }
}
