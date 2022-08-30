package workplaceManager.db.domain;

import lombok.Data;

import javax.persistence.*;

//@Entity
@Data
public class Monitor extends Accounting1С {
   /* @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    private Accounting1С accounting1С;*/
}
