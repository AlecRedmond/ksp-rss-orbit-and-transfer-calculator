package org.artools.orbitcalculator.method.kepler;

import static org.artools.orbitcalculator.application.kepler.KeplerElement.*;
import static org.artools.orbitcalculator.method.kepler.KeplerUtils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import org.artools.orbitcalculator.application.kepler.KeplerElement;
import org.artools.orbitcalculator.application.kepler.KeplerHolds;

@Getter
public class KeplerHoldsLogic {

  private KeplerHoldsLogic() {}

  public static void addHold(KeplerHolds holds, KeplerElement element) {
    List<KeplerElement> inputElements = new ArrayList<>(holds.getHeldElements());
    if (ELLIPTICAL_ELEMENTS.contains(element)) {
      resolveElliptical(inputElements, element);
    }
    if (ROTATIONAL_ELEMENTS.contains(element)) {
      resolveRotational(inputElements, element);
    }
    if (POSITION_ELEMENTS.contains(element)) {
      resolvePosition(inputElements, element);
    }
    holds.setHeldElements(inputElements);
    setSolvableFlags(holds);
  }

  private static void resolveElliptical(List<KeplerElement> inputElements, KeplerElement element) {

    inputElements.remove(element);

    if (element.equals(SEMI_MAJOR_AXIS)) inputElements.remove(ORBITAL_PERIOD);
    if (element.equals(ORBITAL_PERIOD)) inputElements.remove(SEMI_MAJOR_AXIS);

    List<KeplerElement> ellipticals = getHeldElementsOfType(ELLIPTICAL_ELEMENTS, inputElements);

    while (ellipticals.size() > 1) {
      inputElements.remove(ellipticals.getFirst());
      ellipticals.removeFirst();
    }

    inputElements.add(element);
  }

  private static void resolveRotational(List<KeplerElement> inputElements, KeplerElement element) {
    inputElements.remove(element);
    inputElements.add(element);
  }

  private static void resolvePosition(List<KeplerElement> inputElements, KeplerElement element) {
    inputElements.removeAll(POSITION_ELEMENTS);
    inputElements.add(element);
  }

  private static void setSolvableFlags(KeplerHolds holds) {
    List<KeplerElement> inputElements = holds.getHeldElements();

    boolean ellipticalSolvable = hasTwoOfElliptical(inputElements);
    holds.setEllipticSolvable(ellipticalSolvable);

    boolean rotationalSolvable = hasAllOfRotational(inputElements);
    holds.setRotationalSolvable(rotationalSolvable);

    boolean positionSolvable = positionSolvable(inputElements, ellipticalSolvable);
    holds.setPositionSolvable(positionSolvable);

    holds.setAllSolvable(ellipticalSolvable && rotationalSolvable && positionSolvable);
  }

  private static List<KeplerElement> getHeldElementsOfType(
      Set<KeplerElement> keplerElementSet, List<KeplerElement> inputElements) {
    return inputElements.stream().filter(keplerElementSet::contains).toList();
  }

  private static boolean hasTwoOfElliptical(List<KeplerElement> inputElements) {
    return getHeldElementsOfType(ELLIPTICAL_ELEMENTS, inputElements).size() == 2;
  }

  private static boolean hasAllOfRotational(List<KeplerElement> inputElements) {
    return getHeldElementsOfType(ROTATIONAL_ELEMENTS, inputElements).size()
        == ROTATIONAL_ELEMENTS.size();
  }

  private static boolean positionSolvable(
      List<KeplerElement> inputElements, boolean ellipticalSolvable) {
    List<KeplerElement> positionalElements =
        getHeldElementsOfType(POSITION_ELEMENTS, inputElements);
    boolean onePositional = positionalElements.size() == 1;
    return onePositional && ellipticalSolvable;
  }

  public static void removeHold(KeplerHolds holds, KeplerElement element) {
    List<KeplerElement> inputElements = new ArrayList<>(holds.getHeldElements());
    inputElements.remove(element);
    holds.setHeldElements(inputElements);
    setSolvableFlags(holds);
  }
}
