<template>
    <div class="the-footer">
        powerd by walle
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
export default defineComponent ({
    name: "TheFooter"
})
</script>

<style lang="scss">
.the-footer{
    height: 50px;
    background-color: #f0f0f0;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>