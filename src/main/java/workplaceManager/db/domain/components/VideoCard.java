package workplaceManager.db.domain.components;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.Computer;

import javax.persistence.*;
import java.util.List;

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

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
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

    @Transient
    public static boolean equalsVideoCard(VideoCard videoCard1, VideoCard videoCard2) {
        if (videoCard1 == null && videoCard2 == null) {
            return true;
        }
        if ((videoCard1 != null && videoCard2 == null) ||
                (videoCard1 == null && videoCard2 != null)) {
            return false;
        }
        if (videoCard1.getId() == videoCard2.getId() &&
                StringUtils.equals(videoCard1.getModel(), videoCard2.getModel())) {
            return true;
        }
        return false;
    }

    @Transient
    public static boolean equalsVideoCardList(List<VideoCard> videoCardList1, List<VideoCard> videoCardList2) {
        if ((videoCardList1 == null && videoCardList2 == null) ||
                (videoCardList1.isEmpty() && videoCardList2.isEmpty())) {
            return true;
        }
        if (videoCardList1.size() != videoCardList2.size()) {
            return false;
        }
        if ((videoCardList1 == null && videoCardList2 != null) ||
                (videoCardList1 != null && videoCardList2 == null)) {
            return false;
        }
        for(VideoCard videoCard1 : videoCardList1) {
            boolean isExist = false;
            for(VideoCard videoCard2 : videoCardList2) {
                if(equalsVideoCard(videoCard1, videoCard2)) {
                    isExist = true;
                    break;
                }
            }
            if(!isExist) {
                return false;
            }
        }
        return true;
    }
}
