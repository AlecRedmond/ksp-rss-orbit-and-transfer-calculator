package org.artools.orbitcalculator.application.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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

  public static AstralStateDTOBuilder builder() {
    return new AstralStateDTOBuilder();
  }

  public static class AstralStateDTOBuilder {
    private String id;
    private Timestamp timestamp;
    private Vector3DTO position;
    private Vector3DTO velocity;
    private KeplerOrbit orbit;

    AstralStateDTOBuilder() {
    }

    public AstralStateDTOBuilder id(String id) {
      this.id = id;
      return this;
    }

    public AstralStateDTOBuilder timestamp(Timestamp timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public AstralStateDTOBuilder position(Vector3DTO position) {
      this.position = position;
      return this;
    }

    public AstralStateDTOBuilder velocity(Vector3DTO velocity) {
      this.velocity = velocity;
      return this;
    }

    public AstralStateDTOBuilder orbit(KeplerOrbit orbit) {
      this.orbit = orbit;
      return this;
    }

    public AstralStateDTO build() {
      return new AstralStateDTO(this.id, this.timestamp, this.position, this.velocity, this.orbit);
    }

    public String toString() {
      return "AstralStateDTO.AstralStateDTOBuilder(id=" + this.id + ", timestamp=" + this.timestamp + ", position=" + this.position + ", velocity=" + this.velocity + ", orbit=" + this.orbit + ")";
    }
  }
}
