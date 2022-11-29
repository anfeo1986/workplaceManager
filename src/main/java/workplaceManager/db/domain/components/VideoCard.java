package workplaceManager.db.domain.components;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.Computer;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class VideoCard {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String model;

    @ManyToOne(optional = true,cascade = CascadeType.ALL)
    @JoinColumn(name = "computer")
    private Computer computer;

    @Override
    @Transient
    public String toString() {
        return model;
    }

    @Transient
    public static boolean isEmpty(VideoCard videoCard) {
        if (videoCard == null) {
            return true;
        }
        if (videoCard.getModel() == null || videoCard.getModel() == "") {
            return true;
        }

        return false;
    }
}
