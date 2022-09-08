package workplaceManager.db.service;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import workplaceManager.db.domain.Employee;
import workplaceManager.db.domain.Workplace;

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
        Query query = session.createQuery("from Employee as employee where employee.id=" + id);
        Employee employee = (Employee) query.uniqueResult();
        return employee;
    }

    @Transactional
    public Employee getEmployeeByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Employee as employee where employee.name='" + name + "'");
        Employee employee = (Employee) query.uniqueResult();
        return employee;
    }
}
