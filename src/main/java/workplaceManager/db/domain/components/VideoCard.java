package workplaceManager.db.domain.components;

import lombok.Data;
import workplaceManager.db.domain.Computer;

import javax.persistence.*;

@Entity
@Data
public class VideoCard {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String manufacturer;

    @Column
    private String model;

    @OneToOne(mappedBy = "videoCard", cascade = CascadeType.ALL)
    private Computer computer;
}
