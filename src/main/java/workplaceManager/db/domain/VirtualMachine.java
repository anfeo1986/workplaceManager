package workplaceManager.db.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class VirtualMachine {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String ip;

    @Column
    private TypeOS typeOS;

    @Column
    private String vendor;

    @Column
    private String version;

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "computer")
    private Computer computer;
}
