<template>
  <div class="app">
    <!-- header -->
    <a-affix :offset-top="top">
      <the-header></the-header>
    </a-affix>

    <router-view/>
    
    <the-footer></the-footer>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import TheFooter from './views/Home/TheFooter.vue'
import TheHeader from './views/Home/TheHeader.vue'

export default defineComponent({
  components: {
    TheHeader,
    TheFooter
  }
})
</script>


<style lang="scss">

</style>
