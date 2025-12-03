package org.artools.orbitcalculator.application.jpa;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
public class CraftItineraryDTO {
  @Id @UuidGenerator private String id;

  @OneToOne private SpaceCraftDTO spaceCraftDTO;

  @Temporal(TemporalType.TIMESTAMP)
  private Timestamp activationTime;

  @Temporal(TemporalType.TIMESTAMP)
  private Timestamp deactivationTime;

  @OneToOne private KeplerOrbit initialOrbit;
}
