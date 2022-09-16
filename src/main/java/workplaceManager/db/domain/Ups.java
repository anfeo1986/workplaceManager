package workplaceManager.db.domain;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@DiscriminatorValue(TypeEquipment.UPS)
public class Ups extends Equipment<Ups>{
    @Override
    public String toString() {
        return super.toString();
    }
}
