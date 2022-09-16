package workplaceManager.db.domain;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@DiscriminatorValue(TypeEquipment.PRINTER)
public class Printer extends Equipment<Printer> {
    @Override
    public String toString() {
        return super.toString();
    }
}
