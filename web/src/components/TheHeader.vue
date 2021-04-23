<template>
    <div class="the-header">
        <a-menu mode="horizontal" @click="menuClick">
            <a-menu-item>Home</a-menu-item>
            <a-sub-menu title="子菜单">
                <a-menu-item>子菜单项</a-menu-item>
            </a-sub-menu>
            <a-menu-item>tools</a-menu-item>
        </a-menu>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
export default defineComponent ({
    name: "TheHeader",
    setup(){
        // const menuClick = (e: Event) => {
        //     console.log('click', e);
        //     console.log(this);
        //     // this.$router.push('/tools');
        // };



        // return {
        //     menuClick
        // }
    },
    methods: {
        menuClick(e: any){
            if(e.key === 'item_0'){
                this.$router.push('/');
            }
            if(e.key === 'item_2'){
                this.$router.push('/tools');
            }
            
        }
    }
})
</script>

<style lang="scss">
.the-header{
    margin-bottom: 16px;
    
    .ant-menu{
        background: #fafafa;
    }
}
</style>