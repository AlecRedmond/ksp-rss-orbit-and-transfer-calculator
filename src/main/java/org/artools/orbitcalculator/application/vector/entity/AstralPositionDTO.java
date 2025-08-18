package org.artools.orbitcalculator.application.vector.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.OrbitInfo;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AstralPositionDTO {
  @Id @UuidGenerator private String id;

  private String body;
  private Double radius;

  @Temporal(TemporalType.TIMESTAMP)
  private Timestamp timestamp;

  @Embedded private Vector3 position;

  @Embedded private Vector3 velocity;

  @OneToOne private OrbitInfo orbit;
}
