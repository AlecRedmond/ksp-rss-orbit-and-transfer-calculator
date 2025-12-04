package org.artools.orbitcalculator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CraftNotFoundException extends ResponseStatusException {
  public CraftNotFoundException(String id) {
      super(HttpStatus.NOT_FOUND, "Could not find craft with id: %s".formatted(id));
  }
}
