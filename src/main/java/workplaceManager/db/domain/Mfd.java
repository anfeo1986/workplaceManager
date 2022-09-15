package workplaceManager.db.domain;

import lombok.Data;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@DiscriminatorValue(TypeEquipment.MFD)
public class Mfd extends Equipment<Mfd>{
}
