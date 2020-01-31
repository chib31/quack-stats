<template>
  <div class="thinBorder rounded p-1 text-left">
    <h4>Filters</h4>
    <b-form-group
        v-for="column in filterableColumns"
        :key="column.id"
        :id="column.id"
        class="mt-2" border-primary>
      <template slot="label">
        <h6 style="display: inline-block" class="mr-1"> {{ column.label }} </h6>
        <b-button @click="clearFilter(column)" size="sm" variant="light" class="p-0 text-secondary" pill>
          <font-awesome-icon :icon="['far', 'times-circle']"/>
        </b-button>
      </template>
      <b-input-group v-if="column['filterType'] === 'TEXT'">
        <b-form-select
            v-model="column['filterValue']"
            :options="Array.from(new Set(rawData.map(e => e[column.key])))"
            size="sm"
            class="mx-2"/>
      </b-input-group>
      <div v-if="column['filterType'] === 'NUMBER'" class="m-0">
        <b-container>
          <b-row>
            <b-col cols="2">
              <span class="text-right text-muted">{{ Math.round(column['filterRange']['0']) }}</span>
            </b-col>
            <b-col>
              <nouislider
                  :key="column['filterRange'][0]"
                  :config="column['filterConfig']"
                  :values="column['filterRange']"
                  class="mt-1"/>
            </b-col>
            <b-col cols="2">
              <span class="text-left text-secondary">{{ Math.round(column['filterRange']['1']) }}</span>
            </b-col>
          </b-row>
        </b-container>
      </div>
    </b-form-group>
  </div>
</template>

<script>
  import Nouislider from 'vue-nouislider/src/components/noUiSlider';
  import Vue from 'vue'
  
  export default {
    name: "StatFilters",
    components: {
      Nouislider,
    },
    props: {
      filterableColumns: {
        type: Array,
        required: true,
      },
      rawData: {
        type: Array,
        required: true,
      }
    },
    methods: {
      clearFilter(column) {
        if (Object.prototype.hasOwnProperty.call(column, 'filterValue')) {
          column['filterValue'] = '';
        }
        if (Object.prototype.hasOwnProperty.call(column, 'filterRange')) {
          const minReset = column['filterRange'] = column['filterConfig']['range']['min'];
          const maxReset = column['filterRange'] = column['filterConfig']['range']['max'];
          Vue.set(column, 'filterRange', [minReset, maxReset]);
        }
      },
    }
  }
</script>