<template>
    <div class="the-header">
        <a-menu mode="horizontal">
            <a-menu-item>Home</a-menu-item>
            <a-sub-menu title="子菜单">
                <a-menu-item>子菜单项</a-menu-item>
            </a-sub-menu>
        </a-menu>
    </div>
</template>

<script lang="ts">
import { defineComponent, reactive } from 'vue';
export default defineComponent ({
    name: "TheHeader",
})
</script>

<style lang="scss">
.the-header{
    margin-bottom: 16px;
}
</style>