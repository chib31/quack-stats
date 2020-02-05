<template>
  <div id="app" style="max-width: 70rem" class="ml-auto mr-auto">
    <navbar/>
    <b-card>
      <router-view/>
    </b-card>
  </div>
</template>

<script>
  import Navbar from './components/Navbar';

  export default {
    name: 'app',
    components: {
      Navbar
    },
  }
</script>

<style lang="scss">
  @import './assets/custom-vars.scss';

  @import "../node_modules/bootstrap/scss/bootstrap";
  @import '../node_modules/bootstrap-vue/src/index.scss';
</style>
