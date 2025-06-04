package org.artools.orbitcalculator.application;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BodyPosition {
  @Id
  @GeneratedValue(generator = "uuid")
  private String id;

  private String body;

  @Temporal(TemporalType.TIMESTAMP)
  private Timestamp timestamp;

  private Double xPos;
  private Double yPos;
  private Double zPos;
}
