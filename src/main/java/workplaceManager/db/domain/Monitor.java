package workplaceManager.db.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@DiscriminatorValue(TypeEquipment.MONITOR)
public class Monitor extends Equipment<Monitor> {

    @Override
    public String toString() {
        return super.toString();
    }
}
