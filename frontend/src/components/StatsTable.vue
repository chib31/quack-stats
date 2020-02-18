<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <b-container fluid class="thinBorder rounded rounded m-0 py-1 px-2" style="background-color: gray">
    <b-row class="justify-content-center">
      <b-col cols="auto">
        <b-form inline>
          <label> Showing </label>
          <b-form-select v-model="perPage" :options="perPageOptions" size="sm" class="mx-1"/>
          <label> of {{ filteredDataLength }} results </label>
        </b-form>
      </b-col>
      <b-col cols="auto">
        <b-pagination v-model="currentPage"
                      v-if="paginationRequired"
                      :total-rows="filteredDataLength"
                      :per-page="perPage"
                      aria-controls="stats-table"
                      class="small m-0"/>
      </b-col>
    </b-row>
    <b-row class="mt-1">
      <b-col>
        <b-container fluid class="px-1 thinBorder rounded" style="background-color: white">
          <b-table
            id="stats-table"
            :items="tableData"
            :fields="tableColumns"
            :perPage="perPage"
            :currentPage="currentPage"
            :busy="tableLoading"
            thead-class="text-nowrap"
            tbody-class="text-nowrap"
            sticky-header="1000px"
            striped hover small sort-icon-left no-local-sorting no-sorting
            class="my-0 text-left">
            <template v-slot:head()="col">
              <span class="visibleChildOnHover">
                <b-button variant="light"
                          class="py-0 px-1"
                          style="font-weight: bold; background-color: white; margin-left: -0.2rem;"
                          :disabled="!col.field['sortType']"
                          @click="clickHeader(col.field)">
                  {{ col.field[labelKey] }}
                  <span v-if="col.field['sortType'] && col.field['sortPriority']">
                    <b-icon-arrow-up-short :id="col.key + '-sort-asc-icon'"
                                           v-if="col.field['sortType'] === 'ASC'"/>
                    <b-icon-arrow-down-short :id="col.key + '-sort-desc-icon'"
                                             v-if="col.field['sortType'] === 'DESC'"/>
                  </span>
                </b-button>
                <b-button :id="col.key + '-sort-priority'"
                          pill
                          v-if="sortColumnCount > 1 && col.field['sortPriority']"
                          variant="outline-dark"
                          size="sm"
                          class="py-0 px-1"
                          style="font-weight: bold;"
                          @click="clickExistingPriority(col.field)">
                  {{ col.field['sortPriority'] }}
                </b-button>
                <b-button :id="col.key + '-sort-clear-icon'"
                          pill
                          v-if="col.field['sortType']"
                          variant="outline-secondary"
                          size="sm"
                          class="py-0 px-1 ml-1 visibleOnHover"
                          @click="clearSortPriority(col.field)">
                  <b-icon-x-circle/>
                </b-button>
                <b-button :id="col.key + '-sort-priority-new'"
                          pill
                          v-if="sortColumnCount > 0
                            && !col.field['sortPriority']
                            && col.field['sortType']"
                          variant="outline-secondary"
                          size="sm"
                          class="py-0 px-1 ml-1 visibleOnHover"
                          @click="clickNewPriority(col.field)">
                  {{ sortColumnCount + 1 }}
                </b-button>
              </span>
            </template>
            <template v-slot:table-busy>
              <div class="text-center text-danger my-2">
                <b-spinner class="align-middle"></b-spinner>
                <strong>Loading...</strong>
              </div>
            </template>
            <template v-slot:cell(index)="filteredData">
              {{ (filteredData.index + 1) + (perPage * (currentPage - 1)) }}
            </template>
          </b-table>
        </b-container>
      </b-col>
    </b-row>
    <b-row class="justify-content-center mt-1">
      <b-col cols="auto">
        <b-form inline>
          <label> Showing </label>
          <b-form-select v-model="perPage" :options="perPageOptions" size="sm" class="mx-1"/>
          <label> of {{ filteredDataLength }} results </label>
        </b-form>
      </b-col>
      <b-col cols="auto">
        <b-pagination v-model="currentPage"
                      v-if="paginationRequired"
                      :total-rows="filteredDataLength"
                      :per-page="perPage"
                      aria-controls="stats-table"
                      class="small m-0"/>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
  export default {
    name: "StatsTable",
    props: {
      tableData: {type: Array},
      tableColumns: {type: Array},
      tableLoading: {type: Boolean, default: false},
      groupingActive: {type: Boolean},
    },
    data() {
      return {
        currentPage: 1,
        perPage: 20,
        perPageOptions: [
          {value: 20, text: 20},
          {value: 50, text: 50},
          {value: 100, text: 100},
          {value: 99999, text: 'All'}
        ],
      };
    },
    computed: {
      filteredDataLength() {
        return this.tableData.length;
      },
      paginationRequired() {
        return this.filteredDataLength > this.perPage;
      },
      labelKey() {
        return this.groupingActive ? 'aggLabel' : 'label';
      },
      sortColumnCount() {
        return this.tableColumns.filter(e => e['sortPriority']).length;
      }
    },
    methods: {
      clickHeader(col) {
        this.$emit('clickHeader', col);
      },
      clickNewPriority(col) {
        this.$emit('clickNewPriority', col);
      },
      clickExistingPriority(col) {
        this.$emit('clickExistingPriority', col);
      },
      clearSortPriority(col) {
        this.$emit('clearSortPriority', col);
      },

      // Formatters for cell data
      dec2Always(value) {
        return value === null ? '' : parseFloat(`${value}`).toFixed(2);
      },
      dec2NoTrail(value) {
        return value === null ? '' : Math.round((parseFloat(`${value}`) + Number.EPSILON) * 100) / 100;
      },
      percent1Always(value) {
        return value === null ? '' : parseFloat(`${value}`).toFixed(1) + '%';
      },
    }
  }
</script>

<style scoped>

</style>
