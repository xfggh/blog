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
            <a-list :grid="{column: 3, gutter: 50}" item-layout="vertical" :pagination="false" :data-source="ebooks">
                <!-- <template #footer>
                    <div>
                        <b>ant design vue</b>
                        footer part
                    </div>
                </template> -->
                <template #renderItem="{ item }">
                    <a-list-item key="item.name">
                        <template #actions>
                            <span v-for="{ type, text } in actions" :key="type">
                                <component v-bind:is="type" style="margin-right: 8px" />
                                {{ text }}
                            </span>
                        </template>
                        <!-- <template #extra>
                        <img
                            width="272"
                            alt="logo"
                            src="https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png"
                        />
                        </template> -->
                        <a-list-item-meta :description="item.description">
                            <template #title>
                                <a :href="item.href">{{ item.name }}</a>
                            </template>
                            <template #avatar><a-avatar :src="item.cover" /></template>
                        </a-list-item-meta>
                        {{ item.content }}
                    </a-list-item>
                </template>
            </a-list>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue'
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
            axios.get('/ebook/list').then(res => {
                ebooks.value = res.data.content;
                // ebooks2.books = res.data.content;
            });
        });

        const actions: Record<string, string>[] = [
            { type: 'StarOutlined', text: '156' },
            { type: 'LikeOutlined', text: '156' },
            { type: 'MessageOutlined', text: '2' },
        ];

        return{
            ebooks,
            // ebooks2: toRef(ebooks2, 'books'),
            actions
        }
    }
})
</script>


<style lang="scss">
.home{
    // margin-top: 16px;
    min-height: 600px;
    display: flex;
    // padding-top: 16px;
    
    .home-left{
        // width: 250px;
        // margin-right: 16px;
        // border-bottom: 1px solid #F0F0F0;
        border-right: 1px solid #F0F0F0;

        .ant-menu{
            width: 250px;
            border-right: none;
        }

        // box-shadow: 2px 4px 6px 0 rgba(0,0,0,0.04);
        // border-radius: 2px;
    }
    .home-right{
        margin-top: 16px;
        .ant-row{
            margin-right: 0 !important;
            margin-left: 0 !important;
        }
        .ant-avatar{
            border-radius: 2%;
        }
    }
}
</style>