package workplaceManager.db.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Workplace implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String title;

    @OneToMany(mappedBy = "workplace", fetch = FetchType.LAZY)
    private List<Employee> employeeList = new ArrayList<>();

    @OneToMany(mappedBy = "workplace", fetch = FetchType.LAZY)
    private List<Equipment> equipmentList = new ArrayList<>();

    @Override
    @Transient
    public String toString() {
        return title;
    }

    @Transient
    public static boolean equalsWorkplace(Workplace workplace1, Workplace workplace2) {
        if (workplace1 == null && workplace2 == null) {
            return true;
        }
        if ((workplace1 == null && workplace2 != null) ||
                (workplace1 != null && workplace2 == null)) {
            return false;
        }

        if (workplace1.getId() == workplace2.getId() &&
                StringUtils.equals(workplace1.getTitle(), workplace2.getTitle())) {
            return true;
        }
        return false;
    }
}
