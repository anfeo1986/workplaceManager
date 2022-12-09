package workplaceManager.db.service;

import org.hibernate.Hibernate;
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
        List<Employee> employeeList = session.createQuery("from Employee as employee " +
                "where employee.deleted=false order by employee.name asc").list();
        employeeList.stream().forEach(employee -> initializeEmployee(employee));

        return employeeList;
    }

    @Transactional
    public Employee getEmployeeById(Long id, boolean isReadAll) {
        Session session = sessionFactory.getCurrentSession();
        String queryStr = "from Employee as employee where ";
        if (!isReadAll) {
            queryStr += "employee.deleted=false and ";
        }
        queryStr += "employee.id=" + id;
        Query query = session.createQuery(queryStr);
        Employee employee = (Employee) query.uniqueResult();
        initializeEmployee(employee);
        return employee;
    }

    @Transactional
    public Employee getEmployeeByName(String name, boolean isReadAll) {
        Session session = sessionFactory.getCurrentSession();
        String queryStr = "from Employee as employee where ";
        if(!isReadAll) {
            queryStr+="employee.deleted=false and ";
        }
        queryStr += "employee.name='" + name + "'";
        Query query = session.createQuery(queryStr);
        Employee employee = (Employee) query.uniqueResult();
        initializeEmployee(employee);
        return employee;
    }

    protected void initializeEmployee(Employee employee) {
        if (employee != null) {
            Hibernate.initialize(employee.getAccounting1СList());
        }
    }

    @Override
    public void delete(Employee employee) {
        employee.setWorkplace(null);
        employee.setDeleted(true);
        super.save(employee);
    }
}
