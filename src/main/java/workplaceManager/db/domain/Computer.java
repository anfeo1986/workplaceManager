package workplaceManager.db.domain;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@DiscriminatorValue(TypeEquipment.COMPUTER)
public class Computer extends Equipment{
}
