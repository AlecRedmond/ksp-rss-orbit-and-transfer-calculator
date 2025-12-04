package org.artools.orbitcalculator.application.kepler;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.jpa.AstralStateDTO;
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

  private boolean allElementsBuilt;

  @OneToOne private AstralStateDTO astralPosition;

  public KeplerOrbit(Timestamp timestamp, BodyType centralBodyType) {
    this.timestamp = timestamp;
    this.centralBodyType = centralBodyType;
    this.elementsMap = new EnumMap<>(KeplerElement.class);
    this.allElementsBuilt = false;
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
