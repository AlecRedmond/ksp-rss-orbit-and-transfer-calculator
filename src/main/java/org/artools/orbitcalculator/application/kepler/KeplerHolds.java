package org.artools.orbitcalculator.application.kepler;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
public class KeplerHolds {
  @Id @UuidGenerator private String id;

  @ElementCollection
  @CollectionTable(name = "orbit_holds", joinColumns = @JoinColumn(name = "orbit_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "held_element")
  private List<KeplerElement> heldElements;

  private boolean allSolvable;
  private boolean ellipticSolvable;
  private boolean rotationalSolvable;
  private boolean positionSolvable;

  public KeplerHolds() {
    this.heldElements = new ArrayList<>();
  }
}
