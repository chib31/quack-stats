<template>
  <div>
    <b-button-toolbar>
      <h3 class="mr-2"> {{ toTitleCase(statType) + ' Stats' }} </h3>
      <b-dropdown text="Group By" variant="light">
        <b-dropdown-form>
          <b-form-group>
            <b-form-checkbox-group v-model="groupBySelected">
              <b-form-checkbox v-for="gOption in groupableColumns" :key="gOption.key" :value="gOption.key">
                {{ gOption.label }}
              </b-form-checkbox>
            </b-form-checkbox-group>
          </b-form-group>
        </b-dropdown-form>
      </b-dropdown>
      <b-dropdown text="Choose Columns" variant="light">
        <b-dropdown-form>
          <b-form-group>
            <b-form-checkbox-group v-model="activeColumns">
              <b-form-checkbox v-for="col in optionalColumns" :key="col.key" :value="col">
                {{ col.label }}
              </b-form-checkbox>
            </b-form-checkbox-group>
          </b-form-group>
        </b-dropdown-form>
      </b-dropdown>
      <b-button :pressed.sync="showFilters" variant="light">Filter</b-button>
    </b-button-toolbar>
    <hr class="mt-0 mb-2"/>
    <b-container>
      <b-row class="mt-0 mb-2">
        <b-col cols="auto" class="mt-auto text-dark" v-if="sortColumns.length > 0">
          <h5 class="mr-2 my-0" style="display: inline-block">Sorting by:</h5>
          <span v-for="(col, index) in sortColumns" :key="col.key">
          <span style="font-weight: bold">{{ col.label}} </span>
          <span>({{col.sortDirection}})</span>
          <span v-if="index < sortColumns.length - 1">, </span>
        </span>
        </b-col>
      </b-row>
      <b-row>
        <b-col style="overflow-x: auto" class="px-2">
          <stats-table
              :filteredData="filteredData"
              :displayedColumns="activeColumns"
              :tableLoading="tableLoading"
              :sortColumns="sortColumns"
              v-on:clickHeader="clickHeader"
              v-on:clickExistingPriority="clickExistingPriority"
              v-on:clickNextPriority="clickNextPriority"
              v-on:clearSortPriority="clearSortPriority"/>
        </b-col>
        <b-col cols="3" v-if="showFilters" class="pl-1 pr-2">
          <stat-filters :columns="columns" :rawData="rawData"/>
        </b-col>
      </b-row>
    </b-container>
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

  export default {
    components: {
      StatsTable,
      StatFilters,
    },
    props: {
      statType: null,
    },
    data() {
      return {
        reportInfo: Object,
        columns: [],
        rawData: [],
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
        sortColumns: [],
        groupBySelected: [],
        activeColumns: [],
      };
    },
    mixins: [Utils],
    mounted() {
      this.fetchData(this.statType);
    },
    computed: {
      test() {
        let s = sg.supergroup(this.rawData, ['scorecard_name']);
        return s.map(e => this.processSgRow(e));
      },
      optionalColumns() {
        return this.columns.filter(e => e['displayType'] === 'OPTIONAL_SHOW' || e['displayType'] === 'OPTIONAL_HIDE');
      },
      filteredData() {
        let result = this.rawData;
        const textFilterColumns = this.columns.filter(e => Object.prototype.hasOwnProperty.call(e, 'filterValue'));
        for(const column of textFilterColumns) {
          result = result.filter(e =>
              e[column['key']].toLowerCase().includes(column['filterValue'].toLowerCase()));
        }
        const numberFilterColumns = this.columns.filter(e => Object.prototype.hasOwnProperty.call(e, 'filterRange'));
        for(const col of numberFilterColumns) {
          const key = this.getValueColumnKey(col);
          result = result
              .filter(e => e[key] >= col['filterRange']['0'])
              .filter(e => e[key] <= col['filterRange']['1']);
        }
        result.sort((a, b) => this.sortData(a, b));
        return result;
      },
      groupableColumns() {
        return this.columns.filter(e => e['groupable'] === true);
      },
    },
    watch: {
      groupBySelected() {
        this.fetchData(this.statType);
      },
      statType() {
        this.fetchData(this.statType);
      }
    },
    methods: {
      fetchData(statType) {
        const app = this;

        // Construct request url
        if(statType != null) {
          const url = config.BASE_URL + '/' + config.STATS_API_PATH + statType.toUpperCase();

          // Set status to loading and send request
          app.tableLoading = true;
          axios.get(
            url,
            {
              auth: { username: config.API_USER, password: config.API_PASSWORD },
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
            app.columns = responseData['columnList'];
            app.rawData = responseData['dataList'];

            // Populate the initially active columns
            app.setActiveColumns();

            // Populate sort columns array
            app.setSortColumns();

            // Identify min/max values for each column (needed for filters)
            app.getMinMaxValues();

          }).finally(function() {
            app.tableLoading = false;
          });
        }
      },
      setActiveColumns() {
        this.activeColumns = [...this.columns
          .filter(e => e['displayType'] === 'ALWAYS_SHOW' ||  e['displayType'] === 'OPTIONAL_SHOW')];
      },
      // Constructs initial state of sortColumns array
      setSortColumns() {
        this.sortColumns = [];
        const col1 = this.columns.find(e => e['initialSortOrder'] === 1);
        if (col1 != null) {
          this.addNextSortColumn(col1);
          const col2 = this.columns.find(e => e['initialSortOrder'] === 2);
          if (col2 != null) {
            this.addNextSortColumn(col2);
            const col3 = this.columns.find(e => e['initialSortOrder'] === 3);
            if (col3 != null) {
              this.addNextSortColumn(col3);
            }
          }
        }
      },
      getMinMaxValues() {
        for (const col of this.columns.filter(e => e['filterType'] === 'NUM_FILTER')) {
          const key = this.getValueColumnKey(col);
          const min = this.getMinValue(key);
          const max = this.getMaxValue(key);
          Vue.set(col, 'filterConfig', {step: 1, range: {'min': min, 'max': max}});
          Vue.set(col, 'filterRange', [min, max]);
        }
      },
      getValueColumnKey(col) {
        return Object.prototype.hasOwnProperty.call(col, 'valueColumnKey') ? col['valueColumnKey'] : col.key;
      },
      getMinValue(key) {
        const values = this.rawData.map(e => e[key]);
        return Math.min(...values);
      },
      getMaxValue(key) {
        const values = this.rawData.map(e => e[key]);
        return Math.max(...values);
      },
      // Compares 2 rows by priority 1, 2 then 3, and returns 1, 0 or -1 depending on result
      sortData(rowA, rowB) {
        if (this.sortColumns.length > 0) {
          const col1 = this.sortColumns[0];
          const col2 = this.sortColumns[1];
          const col3 = this.sortColumns[2];

          let result = 1;
          if (col1 != null) {
            result = this.compareByCol(col1, rowA, rowB);
          }
          if (result === 0 && col2 != null) {
            result = this.compareByCol(col2, rowA, rowB);
          }
          if (result === 0 && col3 != null) {
            result = this.compareByCol(col3, rowA, rowB);
          }
          if (result === 0) {
            result = 1;
          }
          return result;
        }
      },
      addNextSortColumn(col) {
        let position = this.sortColumns.length;
        while(position >= 3) {
          this.sortColumns.pop();
          position = this.sortColumns.length;
        }
        this.addSortColumnWithPosition(col, position);
      },
      addSortColumnWithPosition(col, position) {
        this.sortColumns.splice(position, 0, col);
        Vue.set(this.sortColumns[position], 'sortDirection', col['defaultSortDir']);
      },
      // Compares 2 rows by a specific column value
      compareByCol(col, rowA, rowB) {
        const key = this.getValueColumnKey(col);
        const dModifier = col['sortDirection'] === 'Asc' ? 1 : -1;
        // Set nulls to -1 (if we ever want nulls first, this will need to be a variable)
        const valA = rowA[key] === null ? -1 : rowA[key];
        const valB = rowB[key] === null ? -1 : rowB[key];
        //TODO - need a new column enum: NULLS_HIGH, NULLS_LOW, NULL_ALWAYS_LAST, NULLS_ALWAYS_FIRST
        if (valA >= 0 && valB < 0) {
          return -1; // don't care about sort directions - nulls always last
        } else if (valA < 0 && valB >= 0) {
          return 1; // don't care about sort directions - nulls always last
        } else
        return valA > valB ? dModifier : (valA === valB ? 0 : -dModifier);
      },
      sortDirectionClick(index) {
        if (this.sortColumns[index]['sortDirection'] === 'Asc') {
          Vue.set(this.sortColumns[index], 'sortDirection', 'Desc');
        } else {
          Vue.set(this.sortColumns[index], 'sortDirection', 'Asc');
        }
      },
      clickHeader(key) {
        if(this.sortColumns.map(e => e.key).includes(key)) {
          this.sortDirectionToggle(key);
        } else {
          this.sortByOnly(key);
        }
      },
      sortDirectionToggle(key) {
        this.sortDirectionClick(this.sortColumns.findIndex(e => e.key === key));
      },
      clearSort(index) {
        this.sortColumns.splice(index, 1);
      },
      sortByOnly(key) {
        this.sortColumns = [];
        this.addNextSortColumn(this.columns.find(e => e.key === key));
      },
      clickExistingPriority(key) {
        const col = this.sortColumns.find(e => e.key === key);
        const currentPos = this.sortColumns.findIndex(e => e.key === key);
        if(currentPos > 0) {
          this.clearSort(currentPos);
          this.addSortColumnWithPosition(col, currentPos - 1);
        }
        else{
          this.clearSort(0);
          this.addNextSortColumn(col);
        }
      },
      clickNextPriority(key) {
        this.addNextSortColumn(this.columns.find(e => e.key === key));
      },
      clearSortPriority(key) {
        this.clearSort(this.sortColumns.findIndex(e => e.key === key));
      },
      processSgRow(row) {
        console.log(row);
        let result = {};
        result[row.dim] = row.valueOf();
        result['innings'] = row.records.length;
        result['runs'] = row.aggregate(d3.sum, 'runs');
        result['deliveries'] = row.aggregate(d3.sum, 'deliveries');
        result['fours'] = row.aggregate(d3.sum, 'fours');
        result['sixes'] = row.aggregate(d3.sum, 'sixes');
        result['wickets'] = row.aggregate(d3.sum, 'wicket');
        result['avg_position'] = row.aggregate(d3.mean, 'position');
        result['average'] = row.aggregate(d3.sum, 'runs') / row.aggregate(d3.sum, 'wicket');
        result['top_score'] = row.aggregate(d3.max, 'runs');
        result['lowest_score'] = row.aggregate(d3.min, 'runs');
        return result;
      },
    },
  };
</script>
