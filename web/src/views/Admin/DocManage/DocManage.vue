<template>
    <div class="doc-manage">
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
            <a-form ref="DocRef" :model="doc" labelAlign="right" :rules="rules"
                @finishFailed="formFinishFailed"
            >
                <a-form-item label="名称" name="name">
                    <a-input v-model:value="doc.name" />
                </a-form-item>

                <a-form-item label="父分类">
                    <a-tree-select
                        v-model:value="doc.parent"
                        style="width: 260px"
                        :dropdown-style="{ maxHeight: '500px', overflow: 'auto' }"
                        :tree-data="parentTreeData"
                        placeholder="选择父分类"
                        tree-default-expand-all
                        :replaceFields="{children:'children', title:'name', key:'id', value: 'id' }"
                    >
                    </a-tree-select>
                </a-form-item>

                <a-form-item label="排序">
                    <a-input v-model:value="doc.sort" />
                </a-form-item>
            </a-form>
        </div>
    </a-modal>
</template>

<script lang="ts">
import { createVNode, defineComponent, onMounted, ref, toRaw } from 'vue';
import { useRoute } from 'vue-router'
import { message, Modal } from 'ant-design-vue';
import axios from 'axios';
import { Tool } from "../../../util/Tools";
import ExclamationCircleOutlined from '@ant-design/icons-vue/ExclamationCircleOutlined'

// interface Doc {
//     id: string,
//     ebookId: string,
//     parent: string,
//     name: string,
//     sort: string,
//     viewCount: string,
//     voteCount: string,
// }

let ebookId: any;
export default defineComponent ({
    name: "DocManage",
    setup(){
        // 打印路由信息
        const route = useRoute();
        console.log(toRaw(route));
        console.log(toRaw(route.params));
        console.log(toRaw(route.query));

        const columns = [
            {
                title: '名称',
                dataIndex: 'name'
            },
            {
                title: '父分类',
                dataIndex: 'parent'
            },
            {
                title: '顺序',
                dataIndex: 'sort'
            },
            {
                title: '点赞数',
                dataIndex: 'voteCount'
            },
            {
                title: '阅读数',
                dataIndex: 'viewCount'
            },
            {
                title: 'Action',
                slots: { customRender: 'action' }
            }
        ];

        // const docs = ref();
        const level1 = ref();
        level1.value = [];
        const loading = ref(false);

        const parentTreeData = ref();
        parentTreeData.value = [];

        const getDocList = (id: any) => {
            console.log(Number(id));
            if(!id || isNaN(Number(id))){
                return ;
            }
            loading.value = true
            axios.get(`/imoocDoc/list/${id}`).then(res => {
                loading.value = false;

                let data = res.data;
                if(data.success){
                    let content = res.data.content
                    // docs.value = content;
                    // console.log(docs.value);

                    level1.value = Tool.array2Tree(content, 0);
                    parentTreeData.value = Tool.array2Tree(content, 0) || [];

                    console.log(toRaw(parentTreeData.value));
                    
                    // 为选择树添加一个"无"
                    parentTreeData.value.unshift({id: 0, name: '无'});
                }else{
                    message.error(data.message);
                }
            });
        }

        /**
         * 使用递归
         * 当前节点 及 其子节点 设为 disabled
         */
        const setParentTreeDisabled = (arr: [], id: string) => {
            for (let i = 0; i < arr.length; i++) {
                const item: any = arr[i];

                if(item.id === id){ // 将 它 及 它的子节点 全部设置为 不可用
                    item.disabled = true;
                    
                    let children = item.children;
                    if(Tool.isNotEmpty(children)){
                        for (let j = 0; j < children.length; j++) {
                            const child = children[j];
                            setParentTreeDisabled(children, child.id);
                        }
                    }
                }else{
                    let children = item.children
                    if(Tool.isNotEmpty(item.children)){
                        setParentTreeDisabled(children, id);
                    }
                }
                
            }
        }

        

        // 弹窗 编辑
        const modalVisible = ref(false);

        const addItem = () => {
            doc.value = { ebookId };
            parentTreeData.value = Tool.copy2(level1.value);
            parentTreeData.value.unshift({id: 0, name: '无'});

            modalVisible.value = true;
        }
        const editItem = (record: any) => {
            doc.value = Tool.copy(record);

            parentTreeData.value = Tool.copy(level1.value);
            setParentTreeDisabled(parentTreeData.value, record.id);

            parentTreeData.value.unshift({id: 0, name: '无'});

            modalVisible.value = true;
        }
        
        let deleteIds: Array<string> = [];
        let deleteNames: Array<string> = [];
        /**
         * 使用递归
         * @param arr 数组
         * @param id 要删除的 顶级 id 
         */
        const getDeleteIds = (arr: [], id: string) => {
            for (let i = 0; i < arr.length; i++) {
                const item: any = arr[i];

                if(item.id === id){ // 将 它 及 它的子节点 全部设置为 不可用
                    deleteIds.push(id);
                    deleteNames.push(item.name);
                    
                    let children = item.children;
                    if(Tool.isNotEmpty(children)){
                        for (let j = 0; j < children.length; j++) {
                            const child = children[j];
                            getDeleteIds(children, child.id);
                        }
                    }
                }else{
                    let children = item.children
                    if(Tool.isNotEmpty(item.children)){
                        getDeleteIds(children, id);
                    }
                }
                
            }
        }

        const deleteItem = (id: any) => {
            deleteIds = [];
            deleteNames = [];
            
            getDeleteIds(parentTreeData.value, id);
            Modal.confirm({
                title: '确认删除吗',
                icon: createVNode(ExclamationCircleOutlined),
                content: `即将删除【${deleteNames.join('，')}】不可恢复`,
                okText: '确定',
                cancelText: '取消',
                onOk() {
                    
                    console.log(deleteIds);

                    axios.delete(`/imoocDoc/delete/${deleteIds}`).then(res => {
                        if(res.data.success){
                            getDocList(ebookId);
                        }
                    })
                }
            });
        }

        const modalConfirmLoading = ref(false);
        const modalHandleOk = () => {
            DocRef.value.validate().then(() => {
                modalConfirmLoading.value = true;
                
                axios.post('/imoocDoc/save', doc.value).then(res => {

                    let data = res.data;
                    if(data.success){
                        modalConfirmLoading.value = false;
                        modalVisible.value = false;

                        getDocList(ebookId);

                        DocRef.value.resetFields();
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

        const doc = ref({});
        const DocRef = ref();
        const rules = {
            name: [
                { required: true, message: '名称不能为空', trigger: 'blur' },
            ],
        };


        onMounted(() => {
            ebookId = route.query.ebook;

            getDocList(ebookId);
        })

        return {
            columns,

            // docs,
            level1,
            loading,
            modalVisible,
            parentTreeData,

            editItem,
            deleteItem,
            addItem,

            modalHandleOk,
            modalConfirmLoading,
            doc,
            DocRef,
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