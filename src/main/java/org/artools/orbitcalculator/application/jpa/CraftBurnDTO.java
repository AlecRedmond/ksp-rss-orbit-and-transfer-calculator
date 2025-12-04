package org.artools.orbitcalculator.application.jpa;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
public class CraftBurnDTO {
  @Id @UuidGenerator private String id;

  @ManyToOne private CraftItineraryDTO craftItineraryDTO;

  @OneToOne @Embedded private Vector3DTO craftInertialDeltaV;

  @Temporal(TemporalType.TIMESTAMP)
  private Timestamp burnStart;

  @Temporal(TemporalType.TIMESTAMP)
  private Timestamp burnEnd;

  @OneToOne private AstralStateDTO burnStartState;

  @OneToOne private AstralStateDTO burnEndState;
}
