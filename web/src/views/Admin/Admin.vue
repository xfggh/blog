<template>
    <div class="admin">
        <div class="add-btn">
            <a-button type="primary" @click="addItem">新增</a-button>
        </div>

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

            <template #action="{ record }">
                <a-space size="small">
                    <a-button type="primary" @click="editItem(record)">
                        编辑
                    </a-button>
                    <a-popconfirm
                        title="确定删除此条记录吗？"
                        ok-text="是"
                        cancel-text="否"
                        @confirm="deleteItem(record.id)"
                    >
                        <a-button type="danger">
                            删除
                        </a-button>
                    </a-popconfirm>
                </a-space>
            </template>

        </a-table>
    </div>

    <a-modal v-model:visible="modalVisible" title="编辑" @ok="modalHandleOk"
        cancelText="取消" okText="确定"
        :confirm-loading="modalConfirmLoading"
    >
        <div class="editModal">
            <a-form ref="ebookRef" :model="ebook" labelAlign="right" :rules="rules"
                @finishFailed="formFinishFailed"
            >
                <a-form-item label="封面">
                    <a-input v-model:value="ebook.cover" />
                </a-form-item>

                <a-form-item label="名称" name="name">
                    <a-input v-model:value="ebook.name" />
                </a-form-item>

                <a-form-item label="分类一">
                    <a-input v-model:value="ebook.category1Id" />
                </a-form-item>

                <a-form-item label="分类二">
                    <a-input v-model:value="ebook.category2Id" />
                </a-form-item>

                <!-- <a-form-item label="文档数">
                    <a-input v-model:value="ebook.docCount" />
                </a-form-item>

                <a-form-item label="点赞数">
                    <a-input v-model:value="ebook.voteCount" />
                </a-form-item>

                <a-form-item label="阅读数">
                    <a-input v-model:value="ebook.viewCount" />
                </a-form-item> -->

                <!-- <div class="formBtn">
                    <a-button type="primary" @click="onSubmit">Create</a-button>
                    <a-button style="margin-left: 10px">Cancel</a-button>
                </div> -->
            </a-form>
        </div>
    </a-modal>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import { message } from 'ant-design-vue';
import axios from 'axios';


// interface formData {
//     cover: string,
//     name: string,
//     category1Id: string,
//     category2Id: string,
//     docCount: number,
//     voteCount: number,
//     viewCount: number
// }
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
        
        const ebooks = ref();
        const loading = ref(false);

        const getEBookList = (params: any) => {
            loading.value = true
            axios.get('/ebook/list', { params }).then(res => {
                loading.value = false;
                pagination.value.current = params.pageNum;

                let data = res.data;

                if(data.success){
                    let content = res.data.content
                    ebooks.value = content.list;
                    pagination.value.total = Number(content.total);
                }else{
                    message.error(data.message);
                }
            });
        }

        // 分页
        const pagination = ref({
            current: 0,
            pageSize: 2,
            total: 6
        });
        const paginationChange = (pagination: any) => {
            console.log(pagination);
            getEBookList({
                pageNum: pagination.current,
                pageSize: pagination.pageSize
            });
        }

        // 弹窗 编辑
        const modalVisible = ref(false);

        const addItem = () => {
            modalVisible.value = true;
        }
        const editItem = (record: any) => {
            ebook.value = JSON.parse(JSON.stringify(record));
            modalVisible.value = true;
        }
        const deleteItem = (id: any) => {
            console.log('delete');
            axios.delete(`/ebook/delete/${id}`).then(res => {
                if(res.data.success){
                    getEBookList({pageNum: pagination.value.current, pageSize: pagination.value.pageSize})
                }
            })
        }

        const modalConfirmLoading = ref(false);
        const modalHandleOk = () => {
            ebookRef.value.validate().then(() => {
                modalConfirmLoading.value = true;
                axios.post('/ebook/save', ebook.value).then(res => {
                    let data = res.data;
                    if(data.success){
                        modalConfirmLoading.value = false;
                        modalVisible.value = false;

                        getEBookList({pageNum: pagination.value.current, pageSize: pagination.value.pageSize});

                        ebookRef.value.resetFields();
                    }else{
                        message.error(data.message);
                        modalConfirmLoading.value = false;
                    }
                })
            }).catch((error: any) => {
                console.log('error', error);
            });
        };

        const formFinishFailed = (error: any) => {
            console.log(error);
        }


        // 表单
        // const formData: UnwrapRef<formData> = reactive({
        //     cover: '',
        //     name: '',
        //     category1Id: '',
        //     category2Id: '',
        //     docCount: 0,
        //     viewCount: 0,
        //     voteCount: 0
        // })
        const ebook = ref({});
        const ebookRef = ref();
        const rules = {
            name: [
                { required: true, message: 'Please input Activity name', trigger: 'blur' },
            ],
        };


        onMounted(() => {
            getEBookList({pageNum: 1, pageSize: pagination.value.pageSize});
        })

        return {
            columns,
            pagination,
            ebooks,
            paginationChange,
            loading,
            modalVisible,

            editItem,
            deleteItem,
            addItem,

            modalHandleOk,
            modalConfirmLoading,
            ebook,
            ebookRef,
            rules,
            formFinishFailed
        }
    }
})
</script>

<style lang="scss">
.admin{
    min-height: 600px;
    padding: 20px;

    .add-btn{
        margin-bottom: 20px;
        text-align: right;
    }
}

.editModal{
    display: flex;
    justify-content: center;

    .ant-form-item{
        margin-bottom: 10px;
    }
    .ant-form-item-label{
        width: 56px;
        margin-right: 20px;
    }
    .ant-input{
        width: 260px;
    }
}
</style>