package uk.cbradbury.backend.enumerations;

public enum ResultType {
  //'won','lost','tied','drawn','n/a'
  WON("won", "Won"),
  LOST("lost", "Lost"),
  TIED("tied", "Tied"),
  DRAWN("drawn", "Drawn"),
  NA("n/a", "N/A");

  private String sqlResult;
  private String value;

  ResultType(String sqlResult, String value) {
    this.sqlResult = sqlResult;
    this.value = value;
  }

  public String getSqlResult() {
    return this.sqlResult;
  }

  public String getValue() {
    return this.value;
  }
}
