package workplaceManager.db.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class OperationSystem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private TypeOS typeOS;

    @Column
    private String vendor;

    @Column
    private String version;

    //@Column
    //private String netName;

    @Override
    public String toString() {
        return String.format("%s (%s)", vendor, version);
    }
}
