package org.artools.orbitcalculator.application.jpa;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.kepler.KeplerElement;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
public class KeplerOrbitDTO {
  @Id @UuidGenerator private String id;
  private Instant epoch;

  @Enumerated(EnumType.STRING)
  private BodyType centralBody;

  @ElementCollection
  @CollectionTable(name = "orbit_elements", joinColumns = @JoinColumn(name = "orbit_id"))
  @MapKeyEnumerated(EnumType.STRING)
  @Column(name = "value")
  private Map<KeplerElement, Double> elementsMap;

  private boolean built;

  @ElementCollection
  @CollectionTable(name = "orbit_holds", joinColumns = @JoinColumn(name = "orbit_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "held_element")
  private List<KeplerElement> holds;
}
