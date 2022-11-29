package workplaceManager.db.service;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.Computer;
import workplaceManager.db.domain.components.HardDrive;
import workplaceManager.db.domain.components.Processor;

import java.util.List;

@Repository
public class HardDriveManager extends EntityManager<HardDrive> {
    @Transactional
    public void deleteHardDriveListForComputer(Computer computer) {
        List<HardDrive> hardDriveList = getHardDriveListByComputer(computer.getId());
        for(HardDrive hardDrive : hardDriveList) {
            delete(hardDrive);
        }
    }

    @Transactional
    public List<HardDrive> getHardDriveListByComputer(Long computerId) {
        Session session = sessionFactory.getCurrentSession();
        List<HardDrive> hardDriveList = session.createQuery("from HardDrive as hd where hd.computer=" + computerId).list();

        return hardDriveList;
    }

    @Override
    public void delete(HardDrive hardDrive) {
        hardDrive.setComputer(null);
        super.save(hardDrive);
        super.delete(hardDrive);
    }
}
