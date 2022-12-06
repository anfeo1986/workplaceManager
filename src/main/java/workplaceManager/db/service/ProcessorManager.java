package workplaceManager.db.service;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.Components;
import workplaceManager.Parameters;
import workplaceManager.db.domain.Computer;
import workplaceManager.db.domain.Employee;
import workplaceManager.db.domain.components.HardDrive;
import workplaceManager.db.domain.components.Processor;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProcessorManager extends EntityManager<Processor> {
    @Transactional
    public void deleteProcessorListForComputer(Computer computer) {
        List<Processor> processorList = getProcessorListByComputer(computer.getId());
        for(Processor processor : processorList) {
            delete(processor);
        }
    }

    @Transactional
    public List<Processor> getProcessorListByComputer(Long computerId) {
        Session session = sessionFactory.getCurrentSession();
        List<Processor> processorList = session.createQuery("from Processor as p where p.computer=" + computerId).list();

        return processorList;
    }

    @Override
    public void delete(Processor processor) {
        processor.setComputer(null);
        super.save(processor);
        super.delete(processor);
    }
}
