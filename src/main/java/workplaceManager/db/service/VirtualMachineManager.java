package workplaceManager.db.service;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.Computer;
import workplaceManager.db.domain.VirtualMachine;
import workplaceManager.db.domain.components.Ram;

import java.util.ArrayList;
import java.util.List;

@Repository
public class VirtualMachineManager extends EntityManager<VirtualMachine> {
    EquipmentManager equipmentManager;

    @Autowired
    public void setEquipmentManager(EquipmentManager equipmentManager) {
        this.equipmentManager = equipmentManager;
    }

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

    @Transactional
    public List<VirtualMachine> getVirtualMachineListAll() {
        List<VirtualMachine> virtualMachineList = new ArrayList<>();
        Session session = sessionFactory.getCurrentSession();
        String queryStr = "select vm from VirtualMachine  as vm left join Equipment  as eq " +
                "on vm.computer.id=eq.id " +
                "where eq.deleted=false";

        virtualMachineList = session.createQuery(queryStr).list();

        virtualMachineList.stream().forEach(virtualMachine -> {
            if(virtualMachine.getComputer() != null) {
                equipmentManager.initializeComputer(virtualMachine.getComputer());
            }
        });

        return virtualMachineList;
    }
}
