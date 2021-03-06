<template>
    <div class="doc-manage">
        <a-row :gutter="24">
            <a-col :span="8">
                <a-table 
                    v-if="level1.length > 0"
                    :dataSource="level1" 
                    :columns="columns"
                    :row-key="record => record.id"
                    :pagination="false"
                    :loading="loading"
                    :defaultExpandAllRows="true"
                >
                    <template #action="{ record }">
                        <a-space size="small">
                            <a-button size="small" type="primary" @click="editItem(record)">
                                编辑
                            </a-button>
                            <a-popconfirm
                                title="确定删除此条记录吗？"
                                ok-text="是"
                                cancel-text="否"
                                @confirm="deleteItem(record.id)"
                            >
                                <a-button size="small" type="danger">
                                    删除
                                </a-button>
                            </a-popconfirm>
                        </a-space>
                    </template>
                </a-table>
            </a-col>
            <a-col :span="16">
                <div class="editModal">
                    <div class="add-btn">
                        <a-space>
                            <a-button type="primary" @click="addItem">新增</a-button>
                            <a-button type="primary" @click="saveItem">{{ saveType }}保存</a-button>
                        </a-space>
                        <a-button type="primary" @click="contentPreview"><EyeOutlined />内容预览</a-button>
                    </div>

                    <a-form ref="docRef" :model="doc" labelAlign="right" :rules="rules"
                        @finishFailed="formFinishFailed"
                        layout="vertical"
                    >
                        <a-form-item name="name">
                            <a-input v-model:value="doc.name" />
                        </a-form-item>

                        <a-form-item name="parent">
                            <a-tree-select
                                v-model:value="doc.parent"
                                :dropdown-style="{ maxHeight: '500px', overflow: 'auto' }"
                                :tree-data="parentTreeData"
                                placeholder="选择父分类"
                                tree-default-expand-all
                                :replaceFields="{children:'children', title:'name', key:'id', value: 'id' }"
                            >
                            </a-tree-select>
                        </a-form-item>

                        <a-form-item name="sort">
                            <a-input type="number" v-model:value="doc.sort" />
                        </a-form-item>

                        <a-form-item>
                            <div id="content"></div>
                        </a-form-item>
                    </a-form>
                </div>
            </a-col>
        </a-row>

        <a-drawer
            title="内容预览"
            placement="right"
            :closable="true"
            v-model:visible="drawerVisible"
            width="900"
        >
            <div class="wangeditor" :innerHTML="previewHtml"></div>
        </a-drawer>
    </div>
</template>

<script lang="ts">
import { createVNode, defineComponent, onMounted, ref, toRaw } from 'vue';
import { useRoute } from 'vue-router'
import { message, Modal } from 'ant-design-vue';
import axios from 'axios';
import { Tool } from "../../../util/Tools";
import ExclamationCircleOutlined from '@ant-design/icons-vue/ExclamationCircleOutlined'

import E from "wangeditor"

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
            // {
            //     title: '父分类',
            //     dataIndex: 'parent'
            // },
            // {
            //     title: '顺序',
            //     dataIndex: 'sort'
            // },
            // {
            //     title: '点赞数',
            //     dataIndex: 'voteCount'
            // },
            // {
            //     title: '阅读数',
            //     dataIndex: 'viewCount'
            // },
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
                    parentTreeData.value.unshift({id: '0', name: '无'});
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
        const addItem = () => {
            docRef.value.resetFields();
            editor.txt.html('');
            saveType.value = '新增';

            parentTreeData.value = Tool.copy2(level1.value);
            parentTreeData.value.unshift({id: '0', name: '无'});
        }
        const editItem = (record: any) => {
            saveType.value = record.name + ' 编辑';
            docRef.value.resetFields();
            editor.txt.html('');

            // 根据 id 查找内容
            getContent(record.id);

            doc.value = Tool.copy(record);
            parentTreeData.value = Tool.copy(level1.value);
            setParentTreeDisabled(parentTreeData.value, record.id);

            parentTreeData.value.unshift({id: '0', name: '无'});
        }
        const getContent = (id: any) => {
            axios.get(`/imoocDoc/content/${id}`).then(res => {
                if(res.data.success){
                    editor.txt.html(res.data.content);
                }
            })
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

        const saveType = ref('新增');
        const saveItem = () => {
            doc.value.ebookId = ebookId;
            doc.value.content = editor.txt.html();

            docRef.value.validate().then(() => {
                axios.post('/imoocDoc/save', doc.value).then(res => {

                    let data = res.data;
                    if(data.success){
                        getDocList(ebookId);

                        Modal.success({
                            // title: 'success',
                            content: 'save success'
                        })
                        docRef.value.resetFields();
                        editor.txt.html('');
                    }else{
                        message.error(data.message);
                    }
                })
            }).catch((error: any) => {
                console.log('error', error);
            });
        };

        const formFinishFailed = (error: any) => {
            console.log(error);
        }

        const doc = ref();
        doc.value = {};
        const docRef = ref();
        const rules = {
            name: [
                { required: true, message: '名称不能为空', trigger: 'change' },
            ],
            parent: [
                { required: true, message: '父分类不能为空', trigger: 'change' },
            ],
            sort: [
                { required: true, type: 'number', transform: (val: any) => Number(val) , message: '排序不能为空', trigger: 'change' },
            ]

        };

        // 内容预览功能
        const drawerVisible = ref(false);
        const previewHtml = ref();
        const contentPreview = () => {
            previewHtml.value = editor.txt.html();
            drawerVisible.value = true;
        }

        let editor:any;
        onMounted(() => {
            // 获取 电子书 的 id
            ebookId = route.query.ebook;
            // 初始化 文档列表
            getDocList(ebookId);
            // 初始化 wangEditor
            editor = new E("#content");
            editor.config.zIndex = 1;
            editor.create();
        })

        return {
            columns,

            // docs,
            level1,
            loading,
            parentTreeData,

            editItem,
            deleteItem,
            addItem,
            saveItem,
            saveType,

            doc,
            docRef,
            rules,
            formFinishFailed,

            contentPreview,
            drawerVisible,
            previewHtml
        }
    }
})
</script>

<style lang="scss" scoped>
.doc-manage{
    .editModal{
        display: flex;
        flex-direction: column;
        justify-content: center;

        .add-btn{
            display: flex;
            margin-bottom: 20px;
            align-items: center;
            justify-content: space-between;
        }
    }
}



</style>