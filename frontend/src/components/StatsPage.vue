<template>
  <div>
    <b-button-toolbar>
      <h3 class="mr-2"> {{ toTitleCase(statType) + ' Stats' }} </h3>
      <b-dropdown text="Group By" variant="light">
        <b-dropdown-form>
          <b-checkbox v-for="col of groupableColumns" :key="col.key" v-model="col['grouped']" switch>
            {{col.label}}
          </b-checkbox>
        </b-dropdown-form>
      </b-dropdown>
      <b-dropdown text="Choose Columns" variant="light">
        <b-dropdown-form>
          <b-checkbox v-for="col of viewableColumns" :key="col.key" v-model="col[currentDisplayKey]" switch>
            {{col.label}}
          </b-checkbox>
        </b-dropdown-form>
      </b-dropdown>
      <b-button :pressed.sync="showFilters" variant="light">Filter</b-button>
    </b-button-toolbar>
    <b-card v-if="showFilters">
      <b-container>
        <h5>Selection Filters</h5>
        <b-row cols="2">
          <b-col v-for="col of filterCols" :key="col.key">
            <b-form-group :label="col.label">
              <b-form-tags v-model="col['selectFilters']"
                           add-on-change
                           no-outer-focus
                           class="mb-2">
                <template v-slot="{ tags, inputAttrs, inputHandlers, removeTag }">
                  <ul v-if="tags.length > 0" class="list-inline d-inline-block mb-2">
                    <li v-for="tag in tags" :key="tag" class="list-inline-item">
                      <b-form-tag @remove="removeTag(tag)"
                                  :title="tag">
                        {{ tag }}
                      </b-form-tag>
                    </li>
                  </ul>
                  <b-form-select v-bind="inputAttrs"
                                 v-on="inputHandlers"
                                 :options="getUniqueColValues(col)">
                  </b-form-select>
                </template>
              </b-form-tags>
            </b-form-group>
          </b-col>
        </b-row>
        <hr/>
        <h5>Range Filters</h5>
        <b-row cols="2">
          <b-col v-for="col of rangeCols" :key="col.key">
            <b-form-group :label="col.label">
              <b-row>
                <b-col cols="2">
                  <span class="text-right text-muted">{{ Math.round(col['filterRange'][0]) }}</span>
                </b-col>
                <b-col>
                  <nouislider
                    :key="col['filterRange'][0]"
                    :config="col['filterConfig']"
                    :values="col['filterRange']"
                    class="mt-1"/>
                </b-col>
                <b-col cols="2">
                  <span class="text-left text-secondary">{{ Math.round(col['filterRange'][1]) }}</span>
                </b-col>
              </b-row>
            </b-form-group>
          </b-col>
        </b-row>
      </b-container>
    </b-card>
    <stats-table
      :table-data="cData3Final"
      :table-columns="cCols4Final"
      :table-loading="tableLoading"
      :grouping-active="groupingActive"
      v-on:clickHeader="clickHeader"
      v-on:clickNewPriority="clickNewPriority"
      v-on:clickExistingPriority="clickExistingPriority"
      v-on:clearSortPriority="clearSortPriority"/>
  </div>
</template>

<script>
  import Vue from 'vue'
  import axios from 'axios'
  import StatsTable from "./StatsTable";
  import StatFilters from "./StatFilters";
  import {Utils} from "../utils";
  import config from "../config";
  import * as d3 from 'd3';
  import sg from 'supergroup';
  import Nouislider from 'vue-nouislider/src/components/noUiSlider';

  export default {
    components: {
      StatsTable,
      StatFilters,
      Nouislider,
    },
    props: {
      statType: null,
    },
    data() {
      return {
        reportInfo: Object,
        tableLoading: false,
        showFilters: false,
        currentPage: 1,
        perPage: 20,
        perPageOptions: [
          {value: 20, text: 20},
          {value: 50, text: 50},
          {value: 100, text: 100},
          {value: 99999, text: 'All'}
        ],
        cols1Raw: [],
        data1Raw: [],
        cols3Grouped: [],
      };
    },
    mixins: [Utils],
    mounted() {
      this.fetchData(this.statType);
    },
    computed: {
      cCols2Grouped() {
        // Filter out columns which can't be shown for current grouping status
        return this.cols1Raw.filter(e => e[this.currentViewabilityKey] !== 'ALWAYS_HIDE').slice();
      },
      cData2FilteredGrouped() {
        const filteredCols = this.cols1Raw.filter(e => e['selectFilters'] && e['selectFilters'].length > 0).slice();
        let filteredRows = this.data1Raw.slice();
        for (const fc of filteredCols) {
          const key = fc['key'];
          const allowedValues = fc['selectFilters'];
          // only include rows where the value of the filtered key is in the array of allowed values
          filteredRows = filteredRows.filter(e => allowedValues.includes(e[key])).slice();
        }

        const groupCols = this.cols1Raw.filter(e => e['grouped']).slice();
        if (groupCols.length > 0) {
          const groupedRows = filteredRows.slice();
          let s = sg.supergroup(groupedRows, [...groupCols.map(e => e.key)]).leafNodes();

          const groupTermLabel = this.formatGroupTerm(groupCols.map(e => e['label']).join("/"));
          this.cols1Raw.slice().find(e => e.key === 'group_term')['aggLabel'] = groupTermLabel;

          const aggCols = this.cols1Raw.filter(e => e['aggregateType'] !== null).slice();
          return s.map(e => this.aggregateRow(e, aggCols));
        } else {
          // No grouping - just return filtered rows
          return filteredRows;
        }
      },
      cCols4Final() {
        // Apply column selections
        return this.cols3Grouped.filter(e => e[this.currentDisplayKey]).slice();
      },
      cData3Final() {
        // Apply ranges and sorting
        return this.cData2FilteredGrouped
          .filter(e => this.checkWithinRangeFilters(e, this.rangeCols))
          .sort((a, b) => this.sortData(a, b, this.sortCols));
      },
      groupingActive() {
        // If a single column has GROUP_ON then return true
        return this.cols1Raw.filter(e => e['grouped']).slice().length > 0;
      },
      currentViewabilityKey() {
        return this.groupingActive ? 'aggViewability' : 'viewability';
      },
      currentDisplayKey() {
        return this.groupingActive ? 'aggDisplay' : 'display';
      },
      currentSortPriorities() {
        return this.cols3Grouped
          .filter(e => e['sortPriority'] > 0)
          .sort((a, b) => {return a['sortPriority'] - b['sortPriority']});
      },
      groupableColumns() {
        return this.cols1Raw.filter(e => e['groupable']).slice();
      },
      viewableColumns() {
        return this.cols3Grouped.filter(e => e[this.currentViewabilityKey] === 'OPTIONAL').slice();
      },
      filterCols() {
        return this.cols1Raw.filter(e => e['filterType'] === 'SELECT_FILTER').slice();
      },
      rangeCols() {
        return this.cols3Grouped
          .filter(e => e['filterType'] === 'RANGE_FILTER'
            && e['filterConfig']['range']['min'] !== null
            && e['filterConfig']['range']['max'] !== null
          );
      },
      sortCols() {
        return this.cols3Grouped.filter(e => e['sortPriority'])
          .sort((a, b) => {return a['sortPriority'] - b['sortPriority']});
      }
    },
    watch: {
      statType() {
        // If statType changes we need a full data refresh
        this.fetchData(this.statType);
      },
      cCols2Grouped() {
        // When any change is made to cCols2Grouped, clone it as cols3Grouped
        // This cannot be computed, as we need to modify the cloned array
        this.cols3Grouped = this.cCols2Grouped.slice();
      },
      cData2FilteredGrouped() {
        this.setRangeLimits();
      }
    },
    methods: {
      fetchData(statType) {
        const app = this;

        // Construct request url
        if (statType != null) {
          const url = config.BASE_URL + '/' + config.STATS_API_PATH + statType.toUpperCase();

          // Set status to loading and send request
          app.tableLoading = true;
          axios.get(
            url,
            {
              auth: {username: config.API_USER, password: config.API_PASSWORD},
              timeout: config.REQUEST_TIMEOUT
            }
          ).catch(error => {
            if (error.code === 'ECONNABORTED') {
              return 'timeout';
            } else {
              throw error;
            }
          }).then(response => {
            const responseData = response.data;

            // Extract data from response
            app.reportInfo = responseData['reportInfo'];
            app.cols1Raw = responseData['columnList'];
            app.data1Raw = responseData['dataList'];

          }).finally(function () {
            app.tableLoading = false;
          });
        }
      },
      aggregateRow(row, cols) {
        let result = {};
        result['group_term'] = this.formatGroupTerm(row.namePath());

        const aggCols = cols.filter(e => e['aggregateType'] != null);

        // Process non-complex aggregates first, as complex aggregates depend on these values
        for (const col of aggCols.filter(e => e['aggregateType'] !== 'COMPLEX')) {
          if (col['aggregateType'] === 'SUM') {
            result[col.key] = row.aggregate(d3.sum, col.key);
          } else if (col['aggregateType'] === 'COUNT') {
            result[col.key] = row.records.length;
          } else if (col['aggregateType'] === 'MEAN') {
            result[col.key] = row.aggregate(d3.mean, col.key);
          } else if (col['aggregateType'] === 'MODE') {
            result[col.key] = row.aggregate(this.calculateMode, col.key);
          } else {
            result[col.key] = null;
          }
        }
        // Now process complex aggregates
        for (const col of aggCols.filter(e => e['aggregateType'] === 'COMPLEX')) {
          if (!col['aggCalculation']) {
            throw error('Complex aggregate type does not have associated calculation');
          } else {
            const numeratorKey = col['aggCalculation']['numeratorKey'];
            const numerator = result[numeratorKey];
            const denominatorKey = col['aggCalculation']['denominatorKey'];
            const denominator = result[denominatorKey];
            const multiplier = col['aggCalculation']['multiplier'];
            result[col.key] = (multiplier * numerator) / denominator;
          }
        }
        return result;
      },
      formatGroupTerm(groupTerm) {
        /* Works on the assumption that column order is also the correct order to "prioritise" group terms. If this
           turns out to not be the case, a new "groupTermOrder" StatColumn attribute will be needed. */
        const termsCount = (groupTerm.match(/\//g) || []).length + 1; // count number of "/" + 1
        if (termsCount > 1) {
          return groupTerm.replace("/", " (") // replace first "/" with open bracket
            .replace(/\//g, ", ") // replace subsequent "/" with comma
            .concat(")");
        } else return groupTerm;
      },
      calculateMode(array) {
        if (array.length === 0)
          return null;
        let modeMap = {};
        let maxEl = array[0], maxCount = 1;
        for (let i = 0; i < array.length; i++) {
          let el = array[i];
          if (modeMap[el] == null)
            modeMap[el] = 1;
          else
            modeMap[el]++;
          if (modeMap[el] > maxCount) {
            maxEl = el;
            maxCount = modeMap[el];
          }
        }
        return maxEl;
      },
      checkWithinRangeFilters(row, columns) {
        for (const col of columns) {
          // for each ranged column, return false if the row is outside minimum or maximum
          if (row[col.key] < col['filterRange'][0] || row[col.key] > col['filterRange'][1]) {
            return false;
          }
        }
        // if method has not yet completed, this row has passed all the range filters: return true
        return true;
      },
      setRangeLimits() {
        for (const col of this.cols3Grouped.filter(e => e['filterType'] === 'RANGE_FILTER')) {
          // Get the range of values for this column, then find the range limits
          const dataValues = this.cData2FilteredGrouped.map(e => e[col.key]).filter(e => !isNaN(e)).slice();
          // const dataValues = [3,43,62,3,5,43,34,5];
          const min = dataValues.length > 0 ? Math.min(...dataValues) : null;
          const max = dataValues.length > 0 ? Math.max(...dataValues) : null;
          Vue.set(col, 'filterConfig', {step: 1, range: {'min': min, 'max': max}});
          Vue.set(col, 'filterRange', [min, max]);
        }
      },
      sortData(rowA, rowB, sortCols) {
        let result = 1;
        for (const col of sortCols) {
          result = this.compareByCol(rowA, rowB, col); // returns 1 if rowA should come first (-1 for rowB)
          if (result !== 0) { // if 0 both rows are equal for this column - continue loop
            return result;
          }
        }
        return 1; // if rows are equal across all sort columns just preserve current order
      },
      // Compares 2 rows by a specific column value
      compareByCol(rowA, rowB, col) {
        const dModifier = col['sortType'] === 'ASC' ? 1 : -1;
        const valA = rowA[col.key];
        const valB = rowB[col.key];
        if((valA === null && valB === null) || (valA === valB)) {
          return 0; // If both null, or equal, return 0
        } else if(valB === null || valA > valB) {
          return dModifier; // If B is null, or A > B, return modifier
        } else {
          return -dModifier; // Only remaining option is that B is non-null and > than A
        }
      },
      clickHeader(col) {
        // Clicking a column header makes it the ONLY sort column, and if already sorted, toggles direction of sort
        const colToChange = this.cols3Grouped.find(e => e.key === col.key);
        if (col['sortPriority'] > 0) {
          Vue.set(colToChange, 'sortType', colToChange['sortType'] === 'ASC' ? 'DESC' : 'ASC'); // toggle
        } else {
          this.clearAllSorting();
          Vue.set(colToChange, 'sortPriority', 1);
        }
      },
      clickNewPriority(col) {
        // Clicking a new priority adds it to the end of the existing priority order
        Vue.set(
          this.cols3Grouped.find(e => e.key === col.key),
          'sortPriority',
          this.currentSortPriorities.length + 1
        );
      },
      clickExistingPriority(col) {
        // Clicking an existing priority swaps it with the next priority, or clears it if already priority 1
        const currentPosClicked = col['sortPriority'];
        if (currentPosClicked > 1) {
          Vue.set(
            this.cols3Grouped.find(e => e['sortPriority'] === (currentPosClicked - 1)),
            'sortPriority',
            currentPosClicked
          );
          Vue.set(
            this.cols3Grouped.find(e => e.key === col.key),
            'sortPriority',
            currentPosClicked - 1
          );
        } else if (currentPosClicked === 1) {
          this.clearSortPriority(col);
        }
      },
      clearSortPriority(col) {
        const currentPosClicked = col['sortPriority'];
        // Remove the priority of the column that was clicked
        Vue.set(this.cols3Grouped.find(e => e.key === col.key), 'sortPriority', null);

        for (const c of this.cols3Grouped.filter(e => e['sortPriority'] > currentPosClicked)) {
          // Now loop through each col with a lower sort priority and promote
          const thisCurrentPos = c['sortPriority'];
          Vue.set(c, 'sortPriority', thisCurrentPos > 1 ? thisCurrentPos - 1 : null);
        }
      },
      clearAllSorting() {
        for (const col of this.cols3Grouped) {
          Vue.set(col, 'sortPriority', null);
        }
      },
      getUniqueColValues(col) {
        return [...new Set(this.data1Raw.map(e => e[col.key]))];
      },
    },
  };
</script>
