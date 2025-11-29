package org.artools.orbitcalculator.method.kepler;

import static org.artools.orbitcalculator.application.kepler.KeplerElement.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import org.artools.orbitcalculator.application.kepler.KeplerElement;

@Getter
public class KeplerHolds {
  private final List<KeplerElement> inputElements;
  private boolean solvable;

  public KeplerHolds() {
    this.inputElements = new LinkedList<>();
    this.solvable = false;
  }

  public KeplerHolds(List<KeplerElement> inputElements) {
    this.inputElements = new LinkedList<>();
    inputElements.forEach(this::addHold);
  }

  public List<KeplerElement> addHold(KeplerElement element) {
    if (ELLIPTICAL_ELEMENTS.contains(element)) resolveElliptical(element);
    if (ROTATIONAL_ELEMENTS.contains(element)) resolveRotational(element);
    if (EPOCH_ELEMENTS.contains(element)) resolveEpoch(element);
    checkSolvable();
    return inputElements;
  }

  private void resolveElliptical(KeplerElement element) {
    inputElements.remove(element);

    if (element.equals(SEMI_MAJOR_AXIS)) inputElements.remove(ORBITAL_PERIOD);
    if (element.equals(ORBITAL_PERIOD)) inputElements.remove(SEMI_MAJOR_AXIS);

    List<KeplerElement> ellipticals = getHeldElementsOfType(ELLIPTICAL_ELEMENTS);

    while (ellipticals.size() > 1) {
      inputElements.remove(ellipticals.getFirst());
      ellipticals.removeFirst();
    }

    inputElements.add(element);
  }

  private void resolveRotational(KeplerElement element) {
    inputElements.remove(element);
    inputElements.add(element);
  }

  private void resolveEpoch(KeplerElement element) {
    inputElements.removeAll(EPOCH_ELEMENTS);
    inputElements.add(element);
  }

  private void checkSolvable() {
    boolean twoOfElliptical = getHeldElementsOfType(ELLIPTICAL_ELEMENTS).size() == 2;
    boolean allOfRotational =
        getHeldElementsOfType(ROTATIONAL_ELEMENTS).size() == ROTATIONAL_ELEMENTS.size();
    boolean oneOfEpoch = getHeldElementsOfType(EPOCH_ELEMENTS).size() == 1;

    if (twoOfElliptical && allOfRotational && oneOfEpoch) solvable = true;
  }

  private List<KeplerElement> getHeldElementsOfType(Set<KeplerElement> keplerElementSet) {
    return inputElements.stream().filter(keplerElementSet::contains).toList();
  }
}
