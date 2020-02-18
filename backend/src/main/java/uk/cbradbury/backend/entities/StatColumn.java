package uk.cbradbury.backend.entities;

import uk.cbradbury.backend.enumerations.Column;

import java.util.Set;

public class StatColumn {
  private Column column;
  private Integer displayOrder;
  private Integer aggDisplayOrder;
  private Integer sortOrder;
  private Set<String> selectFilters;

  public StatColumn(Column column, Integer displayOrder, Integer aggDisplayOrder, Integer sortOrder,
                    Set<String> selectFilters) {
    this.column = column;
    this.displayOrder = displayOrder;
    this.aggDisplayOrder = aggDisplayOrder;
    this.sortOrder = sortOrder;
    this.selectFilters = selectFilters;
  }

  public StatColumn(Column column, Integer displayOrder, Integer aggDisplayOrder) {
    this(column, displayOrder, aggDisplayOrder, null, null);
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

  public Set<String> getSelectFilters() {
    return selectFilters;
  }
}
