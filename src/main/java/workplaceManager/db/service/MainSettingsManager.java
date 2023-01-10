package workplaceManager.db.service;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.Employee;
import workplaceManager.db.domain.MainSettings;

@Repository
public class MainSettingsManager extends EntityManager<MainSettings> {
    @Transactional
    public MainSettings getMainSettings() {
        Session session = sessionFactory.getCurrentSession();
        String queryStr = "from MainSettings as ms";
        Query query = session.createQuery(queryStr);
        MainSettings mainSettings = (MainSettings) query.uniqueResult();
        return mainSettings;
    }
}
