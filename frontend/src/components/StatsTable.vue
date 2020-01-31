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
              :items="filteredData"
              :fields="displayedColumns"
              :perPage="perPage"
              :currentPage="currentPage"
              :busy="tableLoading"
              thead-class="text-nowrap"
              tbody-class="text-nowrap"
              sticky-header="1000px"
              striped hover small sort-icon-left no-local-sorting no-sorting
              class="my-0 text-left">
            <template v-slot:head()="displayedColumns">
            <span class="visibleChildOnHover">
              <b-button pill
                        variant="light"
                        class="py-0 px-1"
                        style="font-weight: bold; background-color: white; margin-left: -0.2rem;"
                        :disabled="displayedColumns.field['sortColumn'] !== true"
                        @click="clickHeader(displayedColumns.field.key)">
                {{ displayedColumns.field.label }}
                <font-awesome-icon
                    v-if="sortColumns.some(e => e.key === displayedColumns.field.key)"
                    :icon="['fas',
                      sortColumns.find(e => e.key === displayedColumns.field.key)['sortDirection'] === 'Asc' ?
                      'caret-up' : 'caret-down']"/>
              </b-button>
              <b-button pill
                        v-if="sortColumns.length > 1 &&
                          sortColumns.some(e => e.key === displayedColumns.field.key)"
                        variant="outline-dark"
                        size="sm"
                        class="py-0 px-1"
                        style="font-weight: bold;"
                        @click="clickExistingPriority(displayedColumns.field.key)">
                {{ sortColumns.findIndex(e => e.key === displayedColumns.field.key) + 1 }}
              </b-button>
              <b-button pill
                        v-if="sortColumns.some(e => e.key === displayedColumns.field.key)"
                        variant="outline-secondary"
                        size="sm"
                        class="py-0 px-1 ml-1 visibleOnHover"
                        @click="clearSortPriority(displayedColumns.field.key)">
                <font-awesome-icon :icon="['fas', 'times']"/>
              </b-button>
              <b-button pill
                        v-if="sortColumns.length > 0 &&
                          sortColumns.length < 3 &&
                          displayedColumns.field['sortColumn'] === true &&
                          !sortColumns.some(e => e.key === displayedColumns.field.key)"
                        variant="outline-secondary"
                        size="sm"
                        class="py-0 px-1 ml-1 visibleOnHover"
                        @click="clickNextPriority(displayedColumns.field.key)">
                {{ sortColumns.length + 1 }}
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
      filteredData: {type: Array},
      displayedColumns: {type: Array},
      tableLoading: {type: Boolean, default: false},
      sortColumns: {type: Array},
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
        return this.filteredData.length;
      },
      paginationRequired() {
        return this.filteredDataLength > this.perPage;
      },
    },
    methods: {
      clickHeader(key) {
        this.$emit('clickHeader', key);
      },
      clickNextPriority(key) {
        this.$emit('clickNextPriority', key);
      },
      clickExistingPriority(key) {
        this.$emit('clickExistingPriority', key);
      },
      clearSortPriority(key) {
        this.$emit('clearSortPriority', key);
      }
    }
  }
</script>

<style scoped>

</style>