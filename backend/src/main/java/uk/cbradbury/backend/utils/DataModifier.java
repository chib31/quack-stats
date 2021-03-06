package uk.cbradbury.backend.utils;

import com.fasterxml.jackson.databind.node.ObjectNode;

import static uk.cbradbury.backend.enumerations.Column.*;

public class DataModifier {

  public static ObjectNode addWicketDisplayData(ObjectNode on, String key) {
    String oldValue = on.get(key).textValue();
    String newValue = Utils.getWicketTypeFromSql(oldValue).toString();
    return on.put(key, newValue);
  }

  public static ObjectNode addStrikeRateBatData(ObjectNode on, String key) {
    int runs = on.get(RUNS.key).asInt();
    int deliveries = on.get(DELIVERIES.key).asInt();
    double newValue = Utils.calculateBattingStrikeRate(runs, deliveries);
    return on.put(key, newValue);
  }

  public static ObjectNode addStrikeRateBowlData(ObjectNode on, String key) {
    int wickets = on.get(WICKETS_BOWLING.key).asInt();
    int deliveries = on.get(DELIVERIES.key).asInt();
    double newValue = Utils.calculateBowlingStrikeRate(deliveries, wickets);
    return on.put(key, newValue);
  }

  public static ObjectNode addPercentTotalDisplayData(ObjectNode on, String key) {
    int runs = on.get(RUNS.key).asInt();
    int team_runs = on.get(TEAM_TOTAL.key).asInt();
    String value = Utils.getPercentOfTotalDisplay(Utils.calculatePercentOfTotal(runs, team_runs));
    return on.put(key, value);
  }

  public static ObjectNode addPercentTotalValueData(ObjectNode on, String key) {
    int runs = on.get(RUNS.key).asInt();
    int team_runs = on.get(TEAM_TOTAL.key).asInt();
    Double value = Utils.calculatePercentOfTotal(runs, team_runs);
    return on.put(key, value);
  }

  public static ObjectNode addEconomyData(ObjectNode on, String key) {
    int runs = on.get(RUNS.key).asInt();
    int deliveries = on.get(DELIVERIES.key).asInt();
    Double value = Utils.calculateEconomy(runs, deliveries);
    return on.put(key, value);
  }

  public static ObjectNode addAverageBatValueData(ObjectNode on, String key) {
    int runs = on.get(RUNS.key).asInt();
    int wickets = on.get(WICKETS_BATTING.key).asInt();
    double value = Utils.getAverageValue(Utils.calculateAverage(runs, wickets));
    return on.put(key, value);
  }

  public static ObjectNode addAverageBatDisplayData(ObjectNode on, String key) {
    int runs = on.get(RUNS.key).asInt();
    int wickets = on.get(WICKETS_BATTING.key).asInt();
    String value = Utils.getAverageDisplay(Utils.calculateAverage(runs, wickets));
    return on.put(key, value);
  }

  public static ObjectNode addAverageBowlValueData(ObjectNode on, String key) {
    int runs = on.get(RUNS.key).asInt();
    int wickets = on.get(WICKETS_BOWLING.key).asInt();
    Double value = Utils.calculateAverage(runs, wickets);
    return on.put(key, value);
  }

  public static ObjectNode addAverageBowlDisplayData(ObjectNode on, String key) {
    int runs = on.get(RUNS.key).asInt();
    int wickets = on.get(WICKETS_BOWLING.key).asInt();
    String value = Utils.getAverageDisplay(Utils.calculateAverage(runs, wickets));
    return on.put(key, value);
  }
}
