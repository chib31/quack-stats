package uk.cbradbury.backend.entities;

import uk.cbradbury.backend.enumerations.Column;

public class Calculation {
  private String numeratorKey;
  private String denominatorKey;
  private int multiplier;

  public Calculation(Column numerator, Column denominator, int multiplier) {
    this.numeratorKey = numerator.getKey();
    this.denominatorKey = denominator.getKey();
    this.multiplier = multiplier;
  }

  public String getNumeratorKey() {
    return numeratorKey;
  }

  public String getDenominatorKey() {
    return denominatorKey;
  }

  public int getMultiplier() {
    return multiplier;
  }
}
