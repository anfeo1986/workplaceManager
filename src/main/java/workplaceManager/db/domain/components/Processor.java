package workplaceManager.db.domain.components;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import workplaceManager.db.domain.Computer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Processor implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String model;

    @Column
    private String numberOfCores;

    @Column
    private String frequency;

    @Column
    private String socket;

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "computer")
    private Computer computer;

    @Override
    @Transient
    public String toString() {
        return String.format("%s (Кол-во ядер: %s, %s, %s)", model, numberOfCores, frequency, socket);
    }

    @Transient
    public static boolean isEmpty(Processor processor) {
        if (processor == null) {
            return true;
        }
        if ((processor.getModel() == null || processor.getModel() == "") &&
                (processor.getNumberOfCores() == null || processor.getNumberOfCores() == "") &&
                (processor.getFrequency() == null || processor.getFrequency() == "") &&
                (processor.getSocket() == null || processor.getSocket() == "")) {
            return true;
        }
        return false;
    }

    @Transient
    public static boolean equalsProcessor(Processor processor1, Processor processor2) {
        if (processor1 == null && processor2 == null) {
            return true;
        }
        if ((processor1 == null && processor2 != null) ||
                (processor1 != null && processor2 == null)) {
            return false;
        }
        if (processor1.getId() == processor2.getId() &&
                StringUtils.equals(processor1.getModel(), processor2.getModel()) &&
                StringUtils.equals(processor1.getNumberOfCores(), processor2.getNumberOfCores()) &&
                StringUtils.equals(processor1.getFrequency(), processor2.getFrequency()) &&
                StringUtils.equals(processor1.getSocket(), processor2.getSocket())) {
            return true;
        }
        return false;
    }

    @Transient
    public static boolean equalsProcessorList(List<Processor> processorList1, List<Processor> processorList2) {
        if ((processorList1 == null && processorList2 == null) ||
                (processorList1.isEmpty() && processorList2.isEmpty()) ||
                (processorList1.size() != processorList2.size())) {
            return true;
        }
        if ((processorList1 == null && processorList2 != null) ||
                (processorList1 != null && processorList2 == null)) {
            return false;
        }

        for(Processor processor1 : processorList1) {
            boolean isExist = false;
            for (Processor processor2:processorList2) {
                if(equalsProcessor(processor1, processor2)) {
                    isExist = true;
                    break;
                }
            }
            if(!isExist) {
                return false;
            }
        }

        return true;
    }
}
