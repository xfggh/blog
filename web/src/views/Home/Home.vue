<template>
    <div class="home">
        <div class="home-left">
            <a-menu mode="inline">
                <a-menu-item key="1">菜单项</a-menu-item>

                <a-sub-menu key="sub1">
                    <template #title>
                        <span>
                            <!-- <LogoutOutlined /> -->
                            <span>Navigation One</span>
                        </span>
                    </template>
                    <a-menu-item key="5">Option 5</a-menu-item>
                    <a-menu-item key="6">Option 6</a-menu-item>
                    <a-menu-item key="7">Option 7</a-menu-item>
                    <a-menu-item key="8">Option 8</a-menu-item>
                </a-sub-menu>
            </a-menu>
        </div>

        <div class="home-right">
            {{ ebooks }}
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive, ref, toRef } from 'vue'
import axios from 'axios'


export default defineComponent({
    name: 'Home',
    components: {
    },
    setup(){

        // 1. ref 方式绑定
        let ebooks = ref();
        // 2. reactive 方式绑定
        // let ebooks2 = reactive({ books: [] });

        onMounted(() => {
            axios.get('http://127.0.0.1:8888/ebook/list').then(res => {
                ebooks.value = res.data.content;
                // ebooks2.books = res.data.content;
            });
        });

        return{
            ebooks,
            // ebooks2: toRef(ebooks2, 'books')
        }
    }
})
</script>


<style lang="scss">
.home{
    // margin-top: 16px;
    min-height: 600px;
    display: flex;
    
    .home-left{
        width: 20%;
        border-bottom: 1px solid #F0F0F0;
    }
}
</style>