<template>
  <div class="app">
    <!-- header -->
    <a-affix>
      <the-header></the-header>
    </a-affix>

    <router-view/>
    
    <the-footer></the-footer>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import TheFooter from './components/TheFooter.vue'
import TheHeader from './components/TheHeader.vue'

export default defineComponent({
  components: {
    TheHeader,
    TheFooter
  }
})
</script>


<style lang="scss">

</style>
