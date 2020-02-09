package uk.cbradbury.backend.enumerations;

import uk.cbradbury.backend.enumerations.column_meta.*;

import static uk.cbradbury.backend.enumerations.column_meta.AggregateType.*;
import static uk.cbradbury.backend.enumerations.column_meta.Grouped.*;
import static uk.cbradbury.backend.enumerations.column_meta.SortType.*;
import static uk.cbradbury.backend.enumerations.column_meta.ValueType.*;
import static uk.cbradbury.backend.enumerations.column_meta.VisibilityType.*;
import static uk.cbradbury.backend.enumerations.column_meta.FilterType.*;

public enum Column { 

  AVERAGE_BAT("average_bat",null,null,ALWAYS_HIDE,null,DESC,NEVER_GROUP,COMPLEX,"Average",OPTIONAL_SHOW),
  AVERAGE_BOWL("average_bowl","Average",DERIVED,OPTIONAL_SHOW,null,ASC,NEVER_GROUP,COMPLEX,"Average",OPTIONAL_SHOW),
  BOWLER_NUMBER("bowler_number","Bowler Number",RAW,OPTIONAL_HIDE,SELECT_FILTER,ASC,GROUP_OFF,MEAN,"Avg Bowler Number",OPTIONAL_HIDE),
  CATCHES("catches","Catches",RAW,OPTIONAL_SHOW,RANGE_FILTER,DESC,NEVER_GROUP,SUM,"Catches",OPTIONAL_SHOW),
  DATE("date","Date",RAW,OPTIONAL_HIDE,null,DESC,NEVER_GROUP,null,null,ALWAYS_HIDE),
  DELIVERIES("deliveries","Deliveries",RAW,OPTIONAL_SHOW,RANGE_FILTER,DESC,NEVER_GROUP,SUM,"Deliveries",OPTIONAL_SHOW),
  ECONOMY("economy","Economy",DERIVED,OPTIONAL_SHOW,RANGE_FILTER,ASC,NEVER_GROUP,COMPLEX,"Economy",OPTIONAL_SHOW),
  FIELDING_MATCHES("fielding_matches","Matches",RAW,ALWAYS_HIDE,null,ASC,NEVER_GROUP,SUM,"Matches",OPTIONAL_SHOW),
  FIELDING_WICKETS("fielding_wickets","Fielding Wickets",RAW,OPTIONAL_SHOW,RANGE_FILTER,DESC,NEVER_GROUP,SUM,"Fielding Wickets",OPTIONAL_SHOW),
  FIXTURE("fixture","Fixture",DERIVED,OPTIONAL_SHOW,null,null,GROUP_OFF,null,null,ALWAYS_HIDE),
  FOURS("fours","Fours",RAW,OPTIONAL_SHOW,RANGE_FILTER,DESC,NEVER_GROUP,SUM,"Fours",OPTIONAL_SHOW),
  GROUP_TERM("group_term",null,null,ALWAYS_HIDE,null,null,NEVER_GROUP,null,"GROUP_TERM",ALWAYS_SHOW),
  HAT_TRICKS("hat_tricks","Hat Tricks",RAW,OPTIONAL_HIDE,RANGE_FILTER,DESC,NEVER_GROUP,SUM,"Hat Tricks",OPTIONAL_HIDE),
  ID("id",null,RAW,ALWAYS_HIDE,null,null,NEVER_GROUP,null,null,ALWAYS_HIDE),
  INDEX("index","#",null,ALWAYS_SHOW,null,null,NEVER_GROUP,null,"#",ALWAYS_SHOW),
  INNINGS("innings",null,RAW,ALWAYS_HIDE,null,DESC,NEVER_GROUP,SUM,"Innings",OPTIONAL_SHOW),
  INNINGS_LENGTH("innings_length","Innings Length",RAW,OPTIONAL_SHOW,RANGE_FILTER,DESC,NEVER_GROUP,null,null,ALWAYS_HIDE),
  MAIDENS("maidens","Maidens",RAW,OPTIONAL_HIDE,RANGE_FILTER,DESC,NEVER_GROUP,SUM,"Maidens",OPTIONAL_HIDE),
  MATCH_FORMAT("match_format","Match Format",RAW,OPTIONAL_HIDE,SELECT_FILTER,null,NEVER_GROUP,null,null,ALWAYS_HIDE),
  NOT_OUTS("not_outs",null,RAW,ALWAYS_HIDE,null,null,NEVER_GROUP,SUM,"Not Outs",OPTIONAL_HIDE),
  NO_BALLS("no_balls","No Balls",RAW,OPTIONAL_SHOW,RANGE_FILTER,DESC,NEVER_GROUP,SUM,"No Balls",OPTIONAL_HIDE),
  OPPOSITION("opposition","Opposition",RAW,OPTIONAL_HIDE,SELECT_FILTER,null,GROUP_OFF,null,null,ALWAYS_HIDE),
  OVERS("overs","Overs",DERIVED,OPTIONAL_HIDE,null,null,NEVER_GROUP,null,null,ALWAYS_HIDE),
  OVER_LENGTH("over_length","Over Length",RAW,OPTIONAL_HIDE,null,null,NEVER_GROUP,null,null,ALWAYS_HIDE),
  PERCENT_TOTAL("percent_total","% of Total",DERIVED,OPTIONAL_HIDE,RANGE_FILTER,DESC,NEVER_GROUP,COMPLEX,"Avg % of Total",OPTIONAL_HIDE),
  PLAYER_NAME("player_name","Name",RAW,ALWAYS_SHOW,SELECT_FILTER,ASC,GROUP_OFF,null,null,ALWAYS_HIDE),
  POSITION("position","Position",RAW,OPTIONAL_HIDE,SELECT_FILTER,ASC,GROUP_OFF,MEAN,"Avg Position",OPTIONAL_HIDE),
  RESULT("result","Result",RAW,OPTIONAL_HIDE,SELECT_FILTER,null,GROUP_OFF,null,null,ALWAYS_HIDE),
  RUNS("runs","Runs",RAW,OPTIONAL_SHOW,RANGE_FILTER,DESC,NEVER_GROUP,SUM,"Runs",OPTIONAL_SHOW),
  RUN_OUTS("run_outs","Run Outs",RAW,OPTIONAL_SHOW,RANGE_FILTER,DESC,NEVER_GROUP,SUM,"Run Outs",OPTIONAL_SHOW),
  SEASON("season","Season",RAW,OPTIONAL_HIDE,SELECT_FILTER,DESC,GROUP_OFF,null,null,ALWAYS_HIDE),
  SIXES("sixes","Sixes",RAW,OPTIONAL_SHOW,RANGE_FILTER,DESC,NEVER_GROUP,SUM,"Sixes",OPTIONAL_SHOW),
  STRIKE_RATE_BAT("strike_rate_bat","Strike Rate",DERIVED,OPTIONAL_SHOW,RANGE_FILTER,DESC,NEVER_GROUP,COMPLEX,"Strike Rate",OPTIONAL_SHOW),
  STRIKE_RATE_BOWL("strike_rate_bowl","Strike Rate",DERIVED,OPTIONAL_SHOW,RANGE_FILTER,ASC,NEVER_GROUP,COMPLEX,"Strike Rate",OPTIONAL_SHOW),
  STUMPINGS("stumpings","Stumpings",RAW,OPTIONAL_SHOW,RANGE_FILTER,DESC,NEVER_GROUP,SUM,"Stumpings",OPTIONAL_SHOW),
  TEAM_TOTAL("team_total","Team Total",RAW,OPTIONAL_HIDE,null,null,NEVER_GROUP,null,null,ALWAYS_HIDE),
  WICKETS_BATTING("wickets_batting",null,RAW,ALWAYS_HIDE,null,null,NEVER_GROUP,SUM,"Wickets",OPTIONAL_SHOW),
  WICKETS_BOWLING("wickets_bowling","Wickets",RAW,OPTIONAL_SHOW,RANGE_FILTER,DESC,NEVER_GROUP,SUM,"Wickets",OPTIONAL_SHOW),
  WICKET_TYPE("wicket_type","Wicket Type",RAW,OPTIONAL_SHOW,SELECT_FILTER,null,GROUP_OFF,MODE,"MCD",OPTIONAL_HIDE),
  WIDES("wides","Wides",RAW,OPTIONAL_SHOW,RANGE_FILTER,DESC,NEVER_GROUP,SUM,"Wides",OPTIONAL_HIDE);

  private final String key;
  private final String label;
  private final ValueType valueType;
  private final VisibilityType visibilityType;
  private final FilterType filterType;
  private final SortType sortType;
  private final Grouped grouped;
  private final AggregateType aggregateType;
  private final String aggLabel;
  private final VisibilityType aggVisibilityType;

  Column(String key,String label,ValueType valueType,VisibilityType visibilityType,FilterType filterType,SortType sortType,Grouped grouped,AggregateType aggregateType,String aggLabel,VisibilityType aggVisibilityType) {
    this.key = key;
    this.label = label;
    this.valueType = valueType;
    this.visibilityType = visibilityType;
    this.filterType = filterType;
    this.sortType = sortType;
    this.grouped = grouped;
    this.aggregateType = aggregateType;
    this.aggLabel = aggLabel;
    this.aggVisibilityType = aggVisibilityType;
  }

  public String getKey() {
  return this.key;
  }

  public String getLabel() {
  return this.label;
  }

  public ValueType getValueType() {
  return this.valueType;
  }

  public VisibilityType getVisibilityType() {
  return this.visibilityType;
  }

  public FilterType getFilterType() {
  return this.filterType;
  }

  public SortType getSortType() {
  return this.sortType;
  }

  public Grouped getGrouped() {
  return this.grouped;
  }

  public AggregateType getAggregateType() {
  return this.aggregateType;
  }

  public String getAggLabel() {
  return this.aggLabel;
  }

  public VisibilityType getAggVisibilityType() {
  return this.aggVisibilityType;
  }

}