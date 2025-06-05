package org.artools.orbitcalculator.application;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AstralState {
  @Id
  @UuidGenerator
  private String id;

  private String body;

  @Temporal(TemporalType.TIMESTAMP)
  private Timestamp timestamp;

  private Double xPos;
  private Double yPos;
  private Double zPos;
}
