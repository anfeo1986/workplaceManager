package workplaceManager.db.service;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.Computer;
import workplaceManager.db.domain.components.Processor;
import workplaceManager.db.domain.components.Ram;

import java.util.List;

@Repository
public class RamManager extends EntityManager<Ram> {
    @Transactional
    public void deleteRamListForComputer(Computer computer) {
        List<Ram> ramList = getRamListByComputer(computer.getId());
        for (Ram ram : ramList) {
            delete(ram);
        }
    }

    @Transactional
    public List<Ram> getRamListByComputer(Long computerId) {
        Session session = sessionFactory.getCurrentSession();
        List<Ram> ramList = session.createQuery("from Ram as r where r.computer=" + computerId).list();

        return ramList;
    }

    @Override
    public void delete(Ram ram) {
        ram.setComputer(null);
        super.save(ram);
        super.delete(ram);
    }
}
