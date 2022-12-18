package workplaceManager.db.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import workplaceManager.db.service.EntityManager;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Employee implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String name;

    @Column
    private String post;

    @Column
    private Boolean deleted = false;

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "workplace")
    private Workplace workplace;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Accounting1C> accounting1СList = new ArrayList<>();

    @Override
    @Transient
    public String toString() {
        String str = String.format("%s (%s)", name, post);
        if (deleted) {
            str += " (удалено. id=" + id + ")";
        }
        return str;
    }

    @Transient
    public static boolean equalsEmployee(Employee employee1, Employee employee2) {
        if (employee1 == null && employee2 == null) {
            return true;
        }
        if ((employee1 == null && employee2 != null) ||
                (employee1 != null && employee2 == null)) {
            return false;
        }
        if (employee1.getId() == employee2.getId() &&
                StringUtils.equals(employee1.getName(), employee2.getName()) &&
                StringUtils.equals(employee1.getPost(), employee2.getPost())) {
            return true;
        }
        return false;
    }
}
