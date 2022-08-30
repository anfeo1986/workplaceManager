package workplaceManager.db.service;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.Employee;

import java.util.List;

@Repository
public class EmployeeManager extends EntityManager<Employee> {

    @Transactional
    public List<Employee> getEmployeeList() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Employee").list();
    }

    @Transactional
    public Employee getEmployeeById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Employee.class, id);
    }

}
