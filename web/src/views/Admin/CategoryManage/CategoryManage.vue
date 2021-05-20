<template>
    <div class="category-manage">
        <div class="add-btn">
            <a-button type="primary" @click="addItem">新增</a-button>
        </div>

        <a-table 
            :dataSource="level1" 
            :columns="columns"
            :row-key="record => record.id"
            :pagination="false"
            :loading="loading"
        >

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
            <a-form ref="CategoryRef" :model="Category" labelAlign="right" :rules="rules"
                @finishFailed="formFinishFailed"
            >
                <a-form-item label="名称" name="name">
                    <a-input v-model:value="Category.name" />
                </a-form-item>

                <a-form-item label="父分类">
                    <!-- <a-input v-model:value="Category.parent" /> -->
                    <a-select
                        v-model:value="Category.parent"
                        style="width: 260px"
                    >
                        <a-select-option value="0">无</a-select-option>
                        <a-select-option :disabled="item.id == Category.parent || item.id == Category.id" v-for="item in level1" :value="item.id" :key="item.id">{{ item.name }}</a-select-option>
                    </a-select>
                </a-form-item>

                <a-form-item label="排序">
                    <a-input v-model:value="Category.sort" />
                </a-form-item>
            </a-form>
        </div>
    </a-modal>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import { message } from 'ant-design-vue';
import axios from 'axios';
import { Tool } from "./../../../util/Tools";

export default defineComponent ({
    name: "CategoryManage",
    setup(){
        const columns = [
            {
                title: '名称',
                dataIndex: 'name'
            },
            {
                title: '父分类',
                key: 'parent',
                dataIndex: 'parent'
            },
            {
                title: '顺序',
                dataIndex: 'sort'
            },
            {
                title: 'Action',
                key: 'action',
                slots: { customRender: 'action' }
            }
        ];

        // const categorys = ref();
        const level1 = ref();
        const loading = ref(false);

        
        const handleQuery = () => {
            getCategoryList()
        }

        const getCategoryList = () => {
            loading.value = true
            axios.get('/category/list').then(res => {
                loading.value = false;

                let data = res.data;
                if(data.success){
                    let content = res.data.content

                    level1.value = Tool.array2Tree(content, 0);
                }else{
                    message.error(data.message);
                }
            }, (error: ErrorEvent) => {
                message.error(error.message);
                loading.value = false;
            });
        }


        // 弹窗 编辑
        const modalVisible = ref(false);

        const addItem = () => {
            modalVisible.value = true;
        }
        const editItem = (record: any) => {
            // Category.value = JSON.parse(JSON.stringify(record));
            Category.value = Tool.copy(record);

            modalVisible.value = true;
        }
        const deleteItem = (id: any) => {
            console.log('delete');
            axios.delete(`/category/delete/${id}`).then(res => {
                if(res.data.success){
                    getCategoryList();
                }
            })
        }

        const modalConfirmLoading = ref(false);
        const modalHandleOk = () => {
            CategoryRef.value.validate().then(() => {
                modalConfirmLoading.value = true;
                console.log(Category.value);
                axios.post('/category/save', Category.value).then(res => {

                    let data = res.data;
                    if(data.success){
                        modalConfirmLoading.value = false;
                        modalVisible.value = false;

                        getCategoryList();

                        CategoryRef.value.resetFields();
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

        const Category = ref({});
        const CategoryRef = ref();
        const rules = {
            name: [
                { required: true, message: '名称不能为空', trigger: 'blur' },
            ],
        };


        onMounted(() => {
            getCategoryList();
        })

        return {
            columns,

            handleQuery,

            // categorys,
            level1,
            loading,
            modalVisible,

            editItem,
            deleteItem,
            addItem,

            modalHandleOk,
            modalConfirmLoading,
            Category,
            CategoryRef,
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
        display: flex;
        margin-bottom: 20px;
        align-items: center;
        justify-content: flex-end;
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