package uk.cbradbury.backend.entities;

import uk.cbradbury.backend.enumerations.Column;

public class StatColumn {
  private Column column;
  private Integer displayOrder;
  private Integer aggDisplayOrder;
  private Integer sortOrder;
  private Integer aggSortOrder;
  private String activeSelectFilter;

  public StatColumn(Column column, Integer displayOrder, Integer aggDisplayOrder, Integer sortOrder,
                    Integer aggSortOrder, String activeSelectFilter) {
    this.column = column;
    this.displayOrder = displayOrder;
    this.aggDisplayOrder = aggDisplayOrder;
    this.sortOrder = sortOrder;
    this.aggSortOrder = aggSortOrder;
    this.activeSelectFilter = activeSelectFilter;
  }

  public StatColumn(Column column, Integer displayOrder, Integer aggDisplayOrder) {
    this(column, displayOrder, aggDisplayOrder, null, null, null);
  }

  public Column getColumn() {
    return column;
  }

  public Integer getDisplayOrder() {
    return displayOrder;
  }

  public Integer getAggDisplayOrder() {
    return aggDisplayOrder;
  }

  public Integer getSortOrder() {
    return sortOrder;
  }

  public Integer getAggSortOrder() {
    return aggSortOrder;
  }

  public String getActiveSelectFilter() {
    return activeSelectFilter;
  }
}
