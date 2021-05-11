<template>
    <div class="admin">
        <a-table 
            :dataSource="ebooks" 
            :columns="columns"
            :row-key="(record, index) => index"
            :pagination="pagination"
            @change="paginationChange"
            :loading="loading"
        >
            <template #cover="{ text: cover }">
                <img width="50" height="50" v-if="cover" :src="cover" alt="avatar" />
            </template>

            <template #action>
                <a-space size="small">
                    <a-button type="primary">
                        编辑
                    </a-button>
                    <a-button type="danger">
                        删除
                    </a-button>
                </a-space>
            </template>

        </a-table>
    </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';

export default defineComponent ({
    name: "Admin",
    setup(){
        const columns = [
            {
                title: '封面',
                dataIndex: 'cover',
                slots: { customRender: 'cover' }
            },
            {
                title: '名称',
                dataIndex: 'name'
            },
            {
                title: '分类一',
                key: 'category1Id',
                dataIndex: 'category1Id'
            },
            {
                title: '分类二',
                dataIndex: 'category2Id'
            },
            {
                title: '文档数',
                dataIndex: 'docCount'
            },
            {
                title: '阅读数',
                dataIndex: 'viewCount'
            },
            {
                title: '点赞数',
                dataIndex: 'voteCount'
            },
            {
                title: 'Action',
                key: 'action',
                slots: { customRender: 'action' }
            }
        ];
        const pagination = ref({
            current: 1,
            pageSize: 2,
            total: 6
        });
        const ebooks = ref();
        const loading = ref(false);

        onMounted(() => {
            getEBookList({pageNum: 1, pageSize: pagination.value.pageSize});
        })


        const getEBookList = (params: any) => {
            loading.value = true
            axios.get('/ebook/list', { params }).then(res => {
                loading.value = false;

                let content = res.data.content
                ebooks.value = content.list;

                pagination.value.total = content.total;
                pagination.value.current = params.pageNum;
            });
        }

        const paginationChange = (pagination: any) => {
            console.log(pagination);
            getEBookList({
                pageNum: pagination.current,
                pageSize: pagination.pageSize
            });
        }

        return {
            columns,
            pagination,
            ebooks,
            paginationChange,
            loading
        }
    }
})
</script>

<style lang="scss">
.admin{
    min-height: 600px;
    padding: 20px;
}
</style>