package workplaceManager.db.domain;

import lombok.Data;
import workplaceManager.db.domain.components.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@DiscriminatorValue(TypeEquipment.COMPUTER)
public class Computer extends Equipment<Computer>{

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "motherboard")
    private MotherBoard motherBoard;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "processor")
    private Processor processor;

    @OneToMany(mappedBy = "computer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ram> ramList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "videoCard")
    private VideoCard videoCard;

    @OneToMany(mappedBy = "computer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HardDrive> hardDriveList = new ArrayList<>();

    @Override
    public String toString() {
        return super.toString();
    }
}
