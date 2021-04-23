<template>
    <div class="sub-menu">
        <a-sub-menu :key="menuInfo.key" v-bind="$attrs">
            <template #title>
                <span>
                    <span>{{ menuInfo.title }}</span>
                </span>
            </template>
            <template v-for="item in menuInfo.children" :key="item.key">
                <template v-if="!item.children">
                    <a-menu-item :key="item.path">
                        <span>{{ item.title }}</span>
                    </a-menu-item>
                </template>
                <template v-else>
                    <sub-menu :menu-info="item" :key="item.key" />
                </template>
            </template>
        </a-sub-menu>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
export default defineComponent ({
    name: "SubMenu",
    props: {
        menuInfo: {
            type: Object,
            default: () => ({}),
        },
    },
})
</script>

<style lang="scss">

</style>