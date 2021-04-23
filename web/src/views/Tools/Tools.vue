<template>
    <div class="tools">
        <div class="tools-menu">
            <!-- <a-menu mode="inline"  @click="menuClick">
                <a-menu-item>菜单项</a-menu-item>
                <a-sub-menu title="子菜单">
                    <a-menu-item>加密</a-menu-item>
                    <a-menu-item>base64</a-menu-item>
                </a-sub-menu>
                <a-menu-item>常用Apache包</a-menu-item>
            </a-menu> -->
            <a-menu mode="inline"  @click="menuClick"
                v-model:selectedKeys="selectedKeys"
                v-model:openKeys="openKeys"
                @openChange="onOpenChange"
            >
                <template v-for="item in menuList">
                    <a-menu-item v-if="!item.children" :key="item.path">
                        <span>{{ item.title }}</span>
                    </a-menu-item>
                    <sub-menu v-else :key="item.path" :menu-info="item" />
                </template>
            </a-menu>
        </div>

        <div class="tools-content">
            <h1></h1>
            <router-view/>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, onMounted, reactive, ref, toRefs } from 'vue';
import SubMenu from './../../components/SubMenu.vue'

export default defineComponent ({
    name: "Tools",
    components: {
        SubMenu
    },
    setup(){
        const menuList = [
            {
                key: 1,
                title: '首页',
                path: '/',
            },
            {
                key: 2,
                title: '编码/加密',
                path: '/tools',
                children: [
                    { key: 2.1, title: '编码', path: '/tools/base64' },
                    { key: 2.2, title: '加密', path: '/tools/hash' }
                ]
            },
            {
                key: 3,
                title: 'apache',
                path: '/apache',
            }
        ];
        const state = reactive({
            selectedKeys: ['1'],
            openKeys: ['sub1'],
        });

        const onOpenChange = (e: any) => {
            console.log(e);
            state.openKeys = e;
        }

        let router = (getCurrentInstance() as any).ctx.$router.currentRoute.value;
        let matchedRouter = router.matched;
        state.openKeys.push(matchedRouter[0].path);
        state.selectedKeys.push(router.path);

        return {
            ...toRefs(state),
            menuList,
            onOpenChange
        }
    },
    methods: {
        menuClick(e: any){
            console.log(e);
            this.$router.push(e.key);
        },

        
    }
})
</script>

<style lang="scss">
.tools{
    min-height: 600px;
    width: 100%;
    display: flex;
    .tools-menu{
        width: 250px;
    }
    .tools-content{
        width: 100%;
        padding: 0 20px 0 20px;
    }
}
</style>