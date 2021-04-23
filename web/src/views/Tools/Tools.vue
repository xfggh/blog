<template>
    <div class="tools">
        <div class="tools-menu">
            <a-menu mode="inline"  @click="menuClick">
                <a-menu-item>菜单项</a-menu-item>
                <a-sub-menu title="子菜单">
                    <a-menu-item>MD5加密</a-menu-item>
                </a-sub-menu>
            </a-menu>
        </div>

        <div class="tools-content">
            <h1></h1>
            <router-view/>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
export default defineComponent ({
    name: "Tools",
    methods: {
        menuClick(e: any){
            console.log(e);
            if(e.key === 'item_1-menu-item_0'){
                this.$router.push('/tools/md5')
            }
        }
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