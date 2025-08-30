package org.artools.orbitcalculator.method.kepler;

import static org.artools.orbitcalculator.application.kepler.KeplerElements.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import org.artools.orbitcalculator.application.kepler.KeplerElements;

@Getter
public class KeplerHolds {
  private final List<KeplerElements> inputElements;
  private boolean solvable;

  public KeplerHolds() {
    this.inputElements = new LinkedList<>();
    this.solvable = false;
  }

  public List<KeplerElements> addHold(KeplerElements element) {
    if (ELLIPTICAL_ELEMENTS.contains(element)) resolveElliptical(element);
    if (ROTATIONAL_ELEMENTS.contains(element)) resolveRotational(element);
    if (EPOCH_ELEMENTS.contains(element)) resolveEpoch(element);
    checkSolvable();
    return inputElements;
  }

  private void removeConcurrent(KeplerElements input, KeplerElements elementA, KeplerElements elementB){
    if(input.equals(elementA)) inputElements.remove(elementB);
    if(input.equals(elementB)) inputElements.remove(elementA);
  }

  private void resolveElliptical(KeplerElements element) {
    inputElements.remove(element);

    removeConcurrent(element,SEMI_MAJOR_AXIS,ORBITAL_PERIOD);

    List<KeplerElements> ellipticals = elementTypes(ELLIPTICAL_ELEMENTS);

    while (ellipticals.size() > 1) {
      inputElements.remove(ellipticals.getFirst());
      ellipticals.removeFirst();
    }

    inputElements.add(element);
  }

  private void resolveRotational(KeplerElements element) {
    inputElements.remove(element);
    inputElements.add(element);
  }

  private void resolveEpoch(KeplerElements element) {
    inputElements.removeAll(EPOCH_ELEMENTS);
    inputElements.add(element);
  }

  private void checkSolvable() {
    boolean twoOfElliptical = elementTypes(ELLIPTICAL_ELEMENTS).size() == 2;
    boolean allOfRotational =
        elementTypes(ROTATIONAL_ELEMENTS).size() == ROTATIONAL_ELEMENTS.size();
    boolean oneOfEpoch = elementTypes(EPOCH_ELEMENTS).size() == 1;

    if (twoOfElliptical && allOfRotational && oneOfEpoch) solvable = true;
  }

  private List<KeplerElements> elementTypes(Set<KeplerElements> keplerElementsSet) {
    return inputElements.stream().filter(keplerElementsSet::contains).toList();
  }
}
