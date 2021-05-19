<template>
    <div class="home">
        <div class="home-left">
            <a-menu mode="inline" 
                :openKeys="openKey" 
                @openChange="onOpenChange"
                :selectedKeys="selectedKeys"
            >
                <a-menu-item key="0" @click="welcomeClick">首页</a-menu-item>

                <a-sub-menu @titleClick="categoryClick(category.id, 1)" :title="category.name" :key="category.id" v-for="category in categorys">
                    <a-menu-item @click="categoryClick(item.id, 2)" :key="item.id" v-for="item in category.children">{{ item.name }}</a-menu-item>
                </a-sub-menu>
            </a-menu>
        </div>

        <div class="home-right">
            <h2 style="margin: -10px 0 0 16px" v-if="isWelcome">welcome</h2>
            <a-list v-if="!isWelcome" :grid="{column: 3, gutter: 50}" item-layout="vertical" :pagination="false" :data-source="ebooks">
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
import { defineComponent, onMounted, ref, toRaw } from 'vue'
import axios from 'axios'
import { Tool } from '../../util/Tools';
import { message } from 'ant-design-vue';


export default defineComponent({
    name: 'Home',
    components: {
    },
    setup(){

        // 1. ref 方式绑定
        let ebooks = ref();
        // 2. reactive 方式绑定
        // let ebooks2 = reactive({ books: [] });
        const getEboks = (params: any) => {
            isWelcome.value = false;

            axios.get('/ebook/all', { params }).then(res => {
                ebooks.value = res.data.content;
                // ebooks2.books = res.data.content;
            });
        }

        onMounted(() => {
            getCategorys();
            getEboks({});
        });

        const actions: Record<string, string>[] = [
            { type: 'StarOutlined', text: '156' },
            { type: 'LikeOutlined', text: '156' },
            { type: 'MessageOutlined', text: '2' },
        ];

        // 分类菜单
        const categorys = ref();
        let categoryList: any;
        const getCategorys = () => {
            axios.get("/category/list").then((res) => {
                let data = res.data;
                if (data.success) {
                    categoryList = res.data.content;
                    categorys.value = Tool.array2Tree(Tool.copy(categoryList), 0);
                    console.log(toRaw(categorys.value));
                } else {
                    message.error(data.message);
                }
            });
        }

        // 保持一个导航菜单选中
        const selectedKeys = ref();
        const categoryClick = (key: any, type: number) => {
            if(type === 1){
                selectedKeys.value = [];
            }else{
                selectedKeys.value = [key];
            }
            getEboks({
                categoryId: key,
                type
            })
        }

        // 只展开一个菜单
        const openKey = ref();
        openKey.value = [];
        const onOpenChange = (openKeys: string[]) => {
            let latestOpenKey = openKeys.find((key: any) => { 
                return openKey.value.indexOf(key) === -1
            });

            openKey.value = latestOpenKey ? [latestOpenKey] : [];
        };

        // 显示 欢迎页
        const isWelcome = ref(false);
        const welcomeClick = () => {
            selectedKeys.value = ['0'];
            isWelcome.value = true;
        }

        return{
            ebooks,
            // ebooks2: toRef(ebooks2, 'books'),
            actions,
            categorys,
            categoryClick,
            openKey,
            selectedKeys,
            onOpenChange,
            isWelcome,
            welcomeClick
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
        .ant-list-item{
            min-width: 287px;
        }
    }
}
</style>