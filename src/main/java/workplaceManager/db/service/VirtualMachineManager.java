package workplaceManager.db.service;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.Computer;
import workplaceManager.db.domain.VirtualMachine;
import workplaceManager.db.domain.components.Ram;

import java.util.List;

@Repository
public class VirtualMachineManager extends EntityManager<VirtualMachine> {
    @Transactional
    public void deleteVirtualMachineListForComputer(Computer computer) {
        List<VirtualMachine> virtualMachineList = getVirtualMachineListByComputer(computer.getId());
        for (VirtualMachine virtualMachine : virtualMachineList) {
            delete(virtualMachine);
        }
    }

    @Transactional
    public List<VirtualMachine> getVirtualMachineListByComputer(Long computerId) {
        Session session = sessionFactory.getCurrentSession();
        List<VirtualMachine> virtualMachineList = session.createQuery("from VirtualMachine as vm " +
                "where vm.computer=" + computerId).list();

        return virtualMachineList;
    }

    @Override
    public void delete(VirtualMachine virtualMachine) {
        virtualMachine.setComputer(null);
        super.save(virtualMachine);
        super.delete(virtualMachine);
    }
}
