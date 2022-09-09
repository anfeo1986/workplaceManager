package workplaceManager.db.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "typeEquipment",
        discriminatorType = DiscriminatorType.STRING
)
@DiscriminatorValue(value = "equipment")
@Data
public class Equipment implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "uid")
    public String uid = "";

    @Column(name = "manufacturer")
    public String manufacturer = "";

    @Column(name = "model")
    public String model = "";

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "accounting1С")
    private Accounting1C accounting1С;

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "workplace")
    private Workplace workplace;
}
