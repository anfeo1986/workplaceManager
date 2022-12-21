package workplaceManager.db.domain;

import lombok.Data;
import workplaceManager.ReplaceString;
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
    @Transient
    public String toString() {
        /*String str = String.format("%s (%s", ip, getUid());
        if ((getManufacturer() != null && !getManufacturer().isEmpty()) ||
                (getModel() != null && !getModel().isEmpty())) {
            str += String.format(", %s %s",getManufacturer(), getModel());
        }
        str += ") ";
        if (getDeleted()) {
            str += " (удалено. id=" + getId() + ")";
        }
        return str;*/
        return String.format("%s (%s%s) %s", ip, getUid(), addManufacturerAndModel(), addDeleted());
    }

    @Transient
    public String toStringHtmlSelectIp() {
        return String.format("<b>%s</b> (%s%s) %s", ip, getUid(), addManufacturerAndModel(), addDeleted());
    }

    @Transient
    public String toStringHtmlSelectUid() {
        return String.format("%s (<b>%s</b>%s) %s", ip, getUid(), addManufacturerAndModel(), addDeleted());
    }

    private String addManufacturerAndModel() {
        String str = "";
        if ((getManufacturer() != null && !getManufacturer().isEmpty()) ||
                (getModel() != null && !getModel().isEmpty())) {
            str += String.format(", %s %s", ReplaceString.replace(getManufacturer()), ReplaceString.replace(getModel()));
        }
        return str;
    }
    private String addDeleted() {
        String str = "";
        if (getDeleted()) {
            str += " (удалено. id=" + getId() + ")";
        }
        return str;
    }
}
