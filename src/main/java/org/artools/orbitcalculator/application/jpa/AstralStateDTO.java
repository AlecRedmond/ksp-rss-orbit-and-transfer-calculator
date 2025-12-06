package org.artools.orbitcalculator.application.jpa;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AstralStateDTO {
  @Id @UuidGenerator private String id;

  @Temporal(TemporalType.TIMESTAMP)
  private Timestamp timestamp;

  @Embedded
  @AttributeOverride(name = "x", column = @Column(name = "pos_x"))
  @AttributeOverride(name = "y", column = @Column(name = "pos_y"))
  @AttributeOverride(name = "z", column = @Column(name = "pos_z"))
  private Vector3DTO position;

  @Embedded
  @AttributeOverride(name = "x", column = @Column(name = "vel_x"))
  @AttributeOverride(name = "y", column = @Column(name = "vel_y"))
  @AttributeOverride(name = "z", column = @Column(name = "vel_z"))
  private Vector3DTO velocity;

  @OneToOne(cascade = CascadeType.ALL)
  private KeplerOrbit orbit;
}
