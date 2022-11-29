package workplaceManager.db.service;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.Computer;
import workplaceManager.db.domain.components.Processor;
import workplaceManager.db.domain.components.VideoCard;

import java.util.List;

@Repository
public class VideoCardManager extends EntityManager<VideoCard> {
    @Transactional
    public void deleteVideoCardListForComputer(Computer computer) {
        List<VideoCard> videoCardList = getVideoCardListByComputer(computer.getId());
        for(VideoCard videoCard : videoCardList) {
            delete(videoCard);
        }
    }

    @Transactional
    public List<VideoCard> getVideoCardListByComputer(Long computerId) {
        Session session = sessionFactory.getCurrentSession();
        List<VideoCard> processorList = session.createQuery("from VideoCard as v where v.computer=" + computerId).list();

        return processorList;
    }

    @Override
    public void delete(VideoCard videoCard) {
        videoCard.setComputer(null);
        super.save(videoCard);
        super.delete(videoCard);
    }
}
