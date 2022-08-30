package workplaceManager.db.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Workplace {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String title;
}
