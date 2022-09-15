package workplaceManager.db.domain;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@DiscriminatorValue(TypeEquipment.SCANNER)
public class Scanner extends Equipment<Scanner>{

}
