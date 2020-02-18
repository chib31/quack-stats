package uk.cbradbury.backend.enumerations;

import uk.cbradbury.backend.enumerations.column_meta.*;

import uk.cbradbury.backend.entities.Calculation;

import static uk.cbradbury.backend.enumerations.column_meta.AggregateType.*;
import static uk.cbradbury.backend.enumerations.column_meta.SortType.*;
import static uk.cbradbury.backend.enumerations.column_meta.Viewability.*;
import static uk.cbradbury.backend.enumerations.column_meta.FilterType.*;

public enum Column { 

  BOWLER_NUMBER("bowler_number","Bowler Number",true,OPTIONAL,false,SELECT_FILTER,ASC,"dec2NoTrail",true,false,"Avg Bowler Number",MEAN,null,OPTIONAL,false),
  CATCHES("catches","Catches",true,OPTIONAL,true,RANGE_FILTER,DESC,null,false,false,"Catches",SUM,null,OPTIONAL,true),
  DATE("date","Date",true,OPTIONAL,false,null,DESC,null,false,false,null,null,null,ALWAYS_HIDE,false),
  DELIVERIES("deliveries","Deliveries",true,OPTIONAL,true,RANGE_FILTER,DESC,null,false,false,"Deliveries",SUM,null,OPTIONAL,true),
  FIELDING_MATCHES("fielding_matches","Matches",true,ALWAYS_HIDE,false,null,ASC,null,false,false,"Matches",SUM,null,OPTIONAL,true),
  FIELDING_WICKETS("fielding_wickets","Wickets",true,OPTIONAL,true,RANGE_FILTER,DESC,null,false,false,"Wickets",SUM,null,OPTIONAL,true),
  FIXTURE("fixture","Fixture",true,OPTIONAL,true,null,null,null,true,false,null,null,null,ALWAYS_HIDE,false),
  FOURS("fours","Fours",true,OPTIONAL,true,RANGE_FILTER,DESC,null,false,false,"Fours",SUM,null,OPTIONAL,true),
  GROUP_TERM("group_term",null,false,ALWAYS_HIDE,false,null,null,null,false,false,"GROUP_TERM",null,null,ALWAYS_SHOW,true),
  HAT_TRICKS("hat_tricks","Hat Tricks",true,OPTIONAL,false,RANGE_FILTER,DESC,null,false,false,"Hat Tricks",SUM,null,OPTIONAL,false),
  ID("id",null,true,ALWAYS_HIDE,false,null,null,null,false,false,null,null,null,ALWAYS_HIDE,false),
  INDEX("index","#",false,ALWAYS_SHOW,true,null,null,null,false,false,"#",null,null,ALWAYS_SHOW,true),
  INNINGS("innings",null,true,ALWAYS_HIDE,false,null,DESC,null,false,false,"Innings",SUM,null,OPTIONAL,true),
  INNINGS_LENGTH("innings_length","Innings Length",true,OPTIONAL,true,RANGE_FILTER,DESC,null,false,false,null,null,null,ALWAYS_HIDE,false),
  MAIDENS("maidens","Maidens",true,OPTIONAL,false,RANGE_FILTER,DESC,null,false,false,"Maidens",SUM,null,OPTIONAL,false),
  MATCH_FORMAT("match_format","Match Format",true,OPTIONAL,false,SELECT_FILTER,null,null,false,false,null,null,null,ALWAYS_HIDE,false),
  NOT_OUTS("not_outs",null,true,ALWAYS_HIDE,false,null,null,null,false,false,"Not Outs",SUM,null,OPTIONAL,false),
  NO_BALLS("no_balls","No Balls",true,OPTIONAL,true,RANGE_FILTER,DESC,null,false,false,"No Balls",SUM,null,OPTIONAL,false),
  OPPOSITION("opposition","Opposition",true,OPTIONAL,false,SELECT_FILTER,null,null,true,false,null,null,null,ALWAYS_HIDE,false),
  OVERS("overs","Overs",true,OPTIONAL,false,null,null,null,false,false,null,null,null,ALWAYS_HIDE,false),
  OVER_LENGTH("over_length","Over Length",true,OPTIONAL,false,null,null,null,false,false,null,null,null,ALWAYS_HIDE,false),
  PLAYER_NAME("player_name","Player",true,ALWAYS_SHOW,true,SELECT_FILTER,ASC,null,true,false,null,null,null,ALWAYS_HIDE,false),
  POSITION("position","Position",true,OPTIONAL,false,SELECT_FILTER,ASC,"dec2NoTrail",true,false,"Avg Position",MEAN,null,OPTIONAL,false),
  RESULT("result","Result",true,OPTIONAL,false,SELECT_FILTER,null,null,true,false,null,null,null,ALWAYS_HIDE,false),
  RUNS("runs","Runs",true,OPTIONAL,true,RANGE_FILTER,DESC,null,false,false,"Runs",SUM,null,OPTIONAL,true),
  RUN_OUTS("run_outs","Run Outs",true,OPTIONAL,true,RANGE_FILTER,DESC,null,false,false,"Run Outs",SUM,null,OPTIONAL,true),
  SEASON("season","Season",true,OPTIONAL,false,SELECT_FILTER,DESC,null,true,false,null,null,null,ALWAYS_HIDE,false),
  SIXES("sixes","Sixes",true,OPTIONAL,true,RANGE_FILTER,DESC,null,false,false,"Sixes",SUM,null,OPTIONAL,true),
  STUMPINGS("stumpings","Stumpings",true,OPTIONAL,true,RANGE_FILTER,DESC,null,false,false,"Stumpings",SUM,null,OPTIONAL,true),
  TEAM_TOTAL("team_total","Team Total",true,OPTIONAL,false,null,null,null,false,false,null,SUM,null,ALWAYS_HIDE,false),
  WICKETS_BATTING("wickets_batting",null,true,ALWAYS_HIDE,false,null,null,null,false,false,"Wickets",SUM,null,OPTIONAL,true),
  WICKETS_BOWLING("wickets_bowling","Wickets",true,OPTIONAL,true,RANGE_FILTER,DESC,null,false,false,"Wickets",SUM,null,OPTIONAL,true),
  WICKET_TYPE("wicket_type","Wicket Type",true,OPTIONAL,true,SELECT_FILTER,null,null,true,false,null,null,null,ALWAYS_HIDE,false),
  WIDES("wides","Wides",true,OPTIONAL,true,RANGE_FILTER,DESC,null,false,false,"Wides",SUM,null,OPTIONAL,false),
  // Complex columns (must go here to avoid forward references),
  AVERAGE_BAT("average_bat",null,false,ALWAYS_HIDE,false,null,DESC,"dec2Always",false,false,"Average",COMPLEX,new Calculation(RUNS, WICKETS_BATTING, 1),OPTIONAL,true),
  AVERAGE_BOWL("average_bowl","Average",true,OPTIONAL,true,null,ASC,"dec2Always",false,false,"Average",COMPLEX,new Calculation(RUNS, WICKETS_BOWLING, 1),OPTIONAL,true),
  ECONOMY("economy","Economy",true,OPTIONAL,true,RANGE_FILTER,ASC,"dec2Always",false,false,"Economy",COMPLEX,new Calculation(RUNS, DELIVERIES, 6),OPTIONAL,true),
  PERCENT_TOTAL("percent_total","% of Total",true,OPTIONAL,false,RANGE_FILTER,DESC,"percent1Always",false,false,"Avg % of Total",COMPLEX,new Calculation(RUNS, TEAM_TOTAL, 1),OPTIONAL,false),
  STRIKE_RATE_BAT("strike_rate_bat","Strike Rate",true,OPTIONAL,true,RANGE_FILTER,DESC,"dec2Always",false,false,"Strike Rate",COMPLEX,new Calculation(RUNS, DELIVERIES, 100),OPTIONAL,true),
  STRIKE_RATE_BOWL("strike_rate_bowl","Strike Rate",true,OPTIONAL,true,RANGE_FILTER,ASC,"dec2Always",false,false,"Strike Rate",COMPLEX,new Calculation(DELIVERIES, WICKETS_BOWLING, 1),OPTIONAL,true);

  private final String key;
  private final String label;
  private final Boolean rawValue;
  private final Viewability viewability;
  private final boolean display;
  private final FilterType filterType;
  private final SortType sortType;
  private final String formatter;
  private final boolean groupable;
  private final boolean grouped;
  private final String aggLabel;
  private final AggregateType aggregateType;
  private final Calculation aggCalculation;
  private final Viewability aggViewability;
  private final boolean aggDisplay;

  Column(String key,String label,Boolean rawValue,Viewability viewability,boolean display,FilterType filterType,SortType sortType,String formatter,boolean groupable,boolean grouped,String aggLabel,AggregateType aggregateType,Calculation aggCalculation,Viewability aggViewability,boolean aggDisplay) {
    this.key = key;
    this.label = label;
    this.rawValue = rawValue;
    this.viewability = viewability;
    this.display = display;
    this.filterType = filterType;
    this.sortType = sortType;
    this.formatter = formatter;
    this.groupable = groupable;
    this.grouped = grouped;
    this.aggLabel = aggLabel;
    this.aggregateType = aggregateType;
    this.aggCalculation = aggCalculation;
    this.aggViewability = aggViewability;
    this.aggDisplay = aggDisplay;
  }

  public String getKey() {
  return this.key;
  }

  public String getLabel() {
  return this.label;
  }

  public Boolean getRawValue() {
  return this.rawValue;
  }

  public Viewability getViewability() {
  return this.viewability;
  }

  public boolean getDisplay() {
  return this.display;
  }

  public FilterType getFilterType() {
  return this.filterType;
  }

  public SortType getSortType() {
  return this.sortType;
  }

  public String getFormatter() {
  return this.formatter;
  }

  public boolean getGroupable() {
  return this.groupable;
  }

  public boolean getGrouped() {
  return this.grouped;
  }

  public String getAggLabel() {
  return this.aggLabel;
  }

  public AggregateType getAggregateType() {
  return this.aggregateType;
  }

  public Calculation getAggCalculation() {
  return this.aggCalculation;
  }

  public Viewability getAggViewability() {
  return this.aggViewability;
  }

  public boolean getAggDisplay() {
  return this.aggDisplay;
  }

}