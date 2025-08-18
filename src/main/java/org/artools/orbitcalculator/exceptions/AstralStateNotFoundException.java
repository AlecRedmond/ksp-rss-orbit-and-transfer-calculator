package org.artools.orbitcalculator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AstralStateNotFoundException extends ResponseStatusException {
  public AstralStateNotFoundException(String id) {
    super(HttpStatus.NOT_FOUND, String.format("Could not find astral body state with id: %s", id));
  }
}
