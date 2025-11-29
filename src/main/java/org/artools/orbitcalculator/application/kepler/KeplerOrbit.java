package org.artools.orbitcalculator.application.kepler;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.jpa.AstralPositionDTO;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
public class KeplerOrbit {

  @Id @UuidGenerator private String id;

  @Temporal(TemporalType.TIMESTAMP)
  private Timestamp timestamp;

  @Enumerated(EnumType.STRING)
  private BodyType centralBodyType;

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

  @OneToOne
  private AstralPositionDTO astralPosition;

  public KeplerOrbit(Timestamp timestamp, BodyType centralBodyType) {
    this.timestamp = timestamp;
    this.centralBodyType = centralBodyType;
    this.elementsMap = new EnumMap<>(KeplerElement.class);
    this.built = false;
    this.holds = new LinkedList<>();
  }

  // Returns Double object to allow passing null values
  public Double getData(KeplerElement element) {
    return elementsMap.get(element);
  }

  public void setData(KeplerElement element, double data) {
    elementsMap.put(element, data);
  }

  public void removeEntry(KeplerElement element) {
    elementsMap.remove(element);
  }

  public boolean containsKey(KeplerElement element) {
    return elementsMap.containsKey(element);
  }
}
