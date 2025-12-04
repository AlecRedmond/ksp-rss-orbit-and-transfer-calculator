package org.artools.orbitcalculator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OrbitNotFoundException extends ResponseStatusException {
  public OrbitNotFoundException(String id) {
    super(HttpStatus.NOT_FOUND, "Could not find an orbit with id: %s".formatted(id));
  }
}
