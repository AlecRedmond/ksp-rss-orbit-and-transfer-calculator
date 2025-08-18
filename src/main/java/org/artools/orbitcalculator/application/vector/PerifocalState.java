package org.artools.orbitcalculator.application.vector;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class PerifocalState extends OrbitalState {

  public PerifocalState() {
    super();
  }
}
