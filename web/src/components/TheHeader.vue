<template>
    <div class="the-header">
        <a-menu mode="horizontal" @click="menuClick"
            v-model:selectedKeys="selectedKeys"
            theme="dark"
        >
            <a-menu-item key="/">Home</a-menu-item>
            <a-sub-menu key="sub01" title="子菜单">
                <a-menu-item>子菜单项</a-menu-item>
            </a-sub-menu>
            <a-menu-item key="/tools">tools</a-menu-item>
            <a-menu-item key="/admin">电子书管理</a-menu-item>
            <a-menu-item key="/admin/category">分类管理</a-menu-item>
            <a-menu-item key="/admin/user">用户管理</a-menu-item>
        </a-menu>
    </div>
</template>

<script lang="ts">
import { defineComponent, reactive, toRefs } from 'vue';
export default defineComponent ({
    name: "TheHeader",
    setup(){
        const state = reactive({
            selectedKeys: [''],
            openKeys: [''],
        });

        // let router = (getCurrentInstance() as any).ctx.$router.currentRoute.value;

        return {
            ...toRefs(state),
        }
    },
    methods: {
        menuClick(e: any){
            this.$router.push(e.key);
        }
    }
})
</script>

<style lang="scss">
// .the-header{
//     // margin-bottom: 16px;
    
//     // .ant-menu{
//     //     // background: #fafafa;
//     // }
// }
</style>