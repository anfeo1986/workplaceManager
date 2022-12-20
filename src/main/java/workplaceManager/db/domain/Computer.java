package workplaceManager.db.domain;

import lombok.Data;
import workplaceManager.db.domain.components.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@DiscriminatorValue(TypeEquipment.COMPUTER)
public class Computer extends Equipment<Computer> {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "motherboard")
    private MotherBoard motherBoard;

    //@OneToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "processor")
    //private Processor processor;
    @OneToMany(mappedBy = "computer", fetch = FetchType.LAZY)
    private List<Processor> processorList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "operationSystem")
    private OperationSystem operationSystem;

    @Column
    private String ip;

    @Column
    private String netName;

    @OneToMany(mappedBy = "computer", fetch = FetchType.LAZY)
    private List<Ram> ramList = new ArrayList<>();

    //@OneToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "videoCard")
    //private VideoCard videoCard;
    @OneToMany(mappedBy = "computer", fetch = FetchType.LAZY)
    private List<VideoCard> videoCardList = new ArrayList<>();

    @OneToMany(mappedBy = "computer", fetch = FetchType.LAZY)
    private List<HardDrive> hardDriveList = new ArrayList<>();

    @Override
    public String toString() {
        String str = String.format("%s (%s", ip, getUid());
        if ((getManufacturer() != null && !getManufacturer().isEmpty()) ||
                (getModel() != null && !getModel().isEmpty())) {
            str += String.format(", %s %s",getManufacturer(), getModel());
        }
        str += ") ";
        if (getDeleted()) {
            str += " (удалено. id=" + getId() + ")";
        }
        return str;
    }
}
