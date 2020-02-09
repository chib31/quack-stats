package uk.cbradbury.backend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.cbradbury.backend.entities.StatColumn;
import uk.cbradbury.backend.entities.StatResponse;
import uk.cbradbury.backend.enumerations.Column;
import uk.cbradbury.backend.enumerations.StatType;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import java.util.*;
import java.util.stream.Collectors;

import static uk.cbradbury.backend.enumerations.column_meta.ValueType.DERIVED;
import static uk.cbradbury.backend.enumerations.column_meta.ValueType.RAW;

@Service
public class StatsService {

  private final EntityManager em;
  private final int TEAM_ID = 1;

  @Autowired
  public StatsService(EntityManager em) {
    this.em = em;
  }

  public StatResponse fetchStats(StatType statType) {
    List<StatColumn> statColumns = statType.columns;

    List<Column> columns = statColumns.stream().map(StatColumn::getColumn).collect(Collectors.toList());

    List<Column> selectColumns = columns.stream()
        .filter(e -> e.getValueType() == RAW)
        .collect(Collectors.toList());

    List<Column> derivedColumns = columns.stream()
        .filter(e -> e.getValueType() == DERIVED)
        .collect(Collectors.toList());

    StringBuilder sb = new StringBuilder();

    sb.append("SELECT ");
    List<String> selectFields = new ArrayList<>();
    for (Column column : selectColumns) {
      selectFields.add("t." + column.getKey() + " AS " + column.getKey());
    }
    sb.append(String.join(", ", selectFields));
    sb.append(" FROM ").append(statType.dbTableName).append(" t ");
    sb.append(" WHERE t.team_id = ").append(TEAM_ID);

    @SuppressWarnings("unchecked")
    List<Tuple> tupleList = em.createNativeQuery(sb.toString(), Tuple.class).getResultList();

    List<ObjectNode> resultList = tupleToObjectNodes(tupleList);

    return new StatResponse(
        constructStatInfoMap(statType),
        constructColumnsJson(statColumns),
        resultList);
  }

  private HashMap<String, String> constructStatInfoMap(StatType statType) {
    HashMap<String, String> reportInfo = new HashMap<>();
    reportInfo.put("stat_type", statType.toString());
    return reportInfo;
  }

  private List<LinkedHashMap<String, Object>> constructColumnsJson(List<StatColumn> statColumns) {
    List<LinkedHashMap<String, Object>> columnList = new ArrayList<>();
    for (StatColumn sCol : statColumns) {
      LinkedHashMap<String, Object> map = new LinkedHashMap<>();
      Column col = sCol.getColumn();

      map.put("key", col.getKey());
      map.put("label", col.getLabel());
      map.put("displayType", col.getValueType());
      map.put("visibilityType", col.getVisibilityType());
      map.put("filterType", col.getFilterType());
      map.put("sortType", col.getSortType());
      map.put("groupable", col.getGrouped());
      map.put("aggregateType", col.getAggregateType());
      map.put("aggLabel", col.getAggLabel());
      map.put("aggVisibilityType", col.getAggVisibilityType());

      map.put("displayOrder", sCol.getDisplayOrder());
      map.put("sortOrder", sCol.getSortOrder());
      map.put("addDisplayOrder", sCol.getAggDisplayOrder());
      map.put("aggSortOrder", sCol.getAggSortOrder());
      map.put("activeSelectFilter", sCol.getActiveSelectFilter());

      columnList.add(map);
    }
    return columnList;
  }

  private List<ObjectNode> tupleToObjectNodes(List<Tuple> results) {
    List<ObjectNode> json = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();
    for (Tuple t : results) {
      List<TupleElement<?>> cols = t.getElements();
      ObjectNode on = mapper.createObjectNode();
      for (TupleElement col : cols) {
        String key = col.getAlias();
        Class c = col.getJavaType();
        if(Integer.class.isAssignableFrom(c)) {
          int value = (int) t.get(key);
          on.put(key, value);
        } else {
          on.put(col.getAlias(), t.get(col.getAlias()).toString());
        }
      }
      json.add(on);
    }
    return json;
  }
}
