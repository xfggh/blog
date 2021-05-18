<template>
  <div class="ebook-manage">
    <div class="add-btn">
      <a-form layout="inline" :model="queryParam">
        <a-form-item>
          <a-input
            style="width: 260px"
            v-model:value="queryParam.name"
            placeholder="名称"
          >
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleQuery"> 查询 </a-button>
        </a-form-item>
      </a-form>

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

      <template #category="{ record }">
        {{ getCategoryName(record.category1Id) }} / {{
          getCategoryName(record.category2Id)
        }}
      </template>

      <template #action="{ record }">
        <a-space size="small">
          <a-button type="primary" @click="editItem(record)"> 编辑 </a-button>
          <a-popconfirm
            title="确定删除此条记录吗？"
            ok-text="是"
            cancel-text="否"
            @confirm="deleteItem(record.id)"
          >
            <a-button type="danger"> 删除 </a-button>
          </a-popconfirm>
        </a-space>
      </template>
    </a-table>
  </div>

  <a-modal
    v-model:visible="modalVisible"
    title="编辑"
    @ok="modalHandleOk"
    cancelText="取消"
    okText="确定"
    :confirm-loading="modalConfirmLoading"
  >
    <div class="editModal">
      <a-form
        ref="ebookRef"
        :model="ebook"
        labelAlign="right"
        :rules="rules"
        @finishFailed="formFinishFailed"
      >
        <a-form-item label="封面">
          <a-input v-model:value="ebook.cover" />
        </a-form-item>

        <a-form-item label="名称" name="name">
          <a-input v-model:value="ebook.name" />
        </a-form-item>

        <a-form-item label="分类">
          <a-cascader
            v-model:value="categoryVal"
            :options="categorys"
            placeholder="选择分类"
            style="width: 260px"
            :fieldNames="{ label: 'name', value: 'id', children: 'children' }"
          />
        </a-form-item>

        <a-form-item label="描述">
          <a-input v-model:value="ebook.description" />
        </a-form-item>

        <!-- <a-form-item label="分类一">
                    <a-input v-model:value="ebook.category1Id" />
                </a-form-item>

                <a-form-item label="分类二">
                    <a-input v-model:value="ebook.category2Id" />
                </a-form-item> -->

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
import { defineComponent, onMounted, ref, toRaw } from "vue";
import { message } from "ant-design-vue";
import axios from "axios";
import { Tool } from "./../../../util/Tools";
import { ColSize } from "ant-design-vue/lib/grid/Col";

interface Ebook {
  category1Id: string;
  category2Id: string;
  cover: string;
  description: string;
  docCount: number;
  id: string;
  name: string;
  viewCount: number;
  voteCount: number;
}

const columns = [
  {
    title: "封面",
    dataIndex: "cover",
    slots: { customRender: "cover" },
  },
  {
    title: "名称",
    dataIndex: "name",
  },
  {
    title: "分类",
    key: "category1Id",
    dataIndex: "category1Id",
    slots: { customRender: "category" },
  },
//   {
//     title: "分类二",
//     dataIndex: "category2Id",
//   },
  {
    title: "文档数",
    dataIndex: "docCount",
  },
  {
    title: "阅读数",
    dataIndex: "viewCount",
  },
  {
    title: "点赞数",
    dataIndex: "voteCount",
  },
  {
    title: "Action",
    key: "action",
    slots: { customRender: "action" },
  },
];

export default defineComponent({
  name: "EbookManage",
  setup() {
    const queryParam = ref();
    queryParam.value = {};

    const ebooks = ref();
    const loading = ref(false);

    const handleQuery = () => {
      console.log(queryParam.value.name);
      getEBookList({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      });
    };

    const getEBookList = (params: any) => {
      loading.value = true;
      axios
        .get("/ebook/list", {
          params: {
            pageNum: params.pageNum,
            pageSize: params.pageSize,
            name: queryParam.value.name,
          },
        })
        .then((res) => {
          loading.value = false;
          pagination.value.current = params.pageNum;

          let data = res.data;

          if (data.success) {
            let content = res.data.content;
            ebooks.value = content.list;
            pagination.value.total = Number(content.total);
          } else {
            message.error(data.message);
          }
        });
    };

    // 获取分类
    const categorys = ref();
    let categoryList: [];
    const categoryVal = ref();
    const getCategorys = () => {
      axios.get("/category/list").then((res) => {
        let data = res.data;
        if (data.success) {
          categoryList = res.data.content;
          categorys.value = Tool.array2Tree(Tool.copy(categoryList), 0);

          getEBookList({ pageNum: 1, pageSize: pagination.value.pageSize });
        } else {
          message.error(data.message);
        }
      });
    };
    const getCategoryName = (id: string) => {
      let result = "";
      let res = categoryList.find((item: any) => {
        return item.id == id;
      });
      if (res) {
        result = res["name"];
      }
      return result;
    };

    // 分页
    const pagination = ref({
      current: 0,
      pageSize: 2,
      total: 6,
    });
    const paginationChange = (pagination: any) => {
      console.log(pagination);
      getEBookList({
        pageNum: pagination.current,
        pageSize: pagination.pageSize,
      });
    };

    // 弹窗 编辑
    const modalVisible = ref(false);

    const addItem = () => {
      ebook.value = {} as Ebook;
      categoryVal.value = [];
      modalVisible.value = true;
    };
    const editItem = (record: Ebook) => {
      // ebook.value = JSON.parse(JSON.stringify(record));
      ebook.value = Tool.copy(record);
      console.log(record);
      categoryVal.value = ["" + record.category1Id, record.category2Id + ""];

      console.log(toRaw(categoryVal.value));

      modalVisible.value = true;
    };
    const deleteItem = (id: any) => {
      console.log("delete");
      axios.delete(`/ebook/delete/${id}`).then((res) => {
        pagination.value.total;
        if (res.data.success) {
          if (
            (pagination.value.total - 1) / pagination.value.pageSize >
            pagination.value.current - 1
          ) {
            getEBookList({
              pageNum: pagination.value.current,
              pageSize: pagination.value.pageSize,
            });
          } else {
            getEBookList({
              pageNum: pagination.value.current - 1,
              pageSize: pagination.value.pageSize,
            });
          }
        }
      });
    };

    const modalConfirmLoading = ref(false);
    const modalHandleOk = () => {
      ebookRef.value
        .validate()
        .then(() => {
          modalConfirmLoading.value = true;

          if (ebook.value) {
            ebook.value.category1Id = categoryVal.value[0];
            ebook.value.category2Id = categoryVal.value[1];
          }

          axios.post("/ebook/save", ebook.value).then((res) => {
            let data = res.data;
            if (data.success) {
              modalConfirmLoading.value = false;
              modalVisible.value = false;

              getEBookList({
                pageNum: pagination.value.current,
                pageSize: pagination.value.pageSize,
              });

            } else {
              message.error(data.message);
              modalConfirmLoading.value = false;
            }
          });
        })
        .catch((error: any) => {
          console.log("error", error);
        });
    };
    // const modalHandleCancel = () => {
    //   console.log(123);
    // }

    const formFinishFailed = (error: any) => {
      console.log(error);
    };

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
    const ebook = ref<Ebook>();
    ebook.value = {} as Ebook;
    const ebookRef = ref();
    const rules = {
      name: [
        {
          required: true,
          message: "Please input Activity name",
          trigger: "blur",
        },
      ],
    };

    onMounted(() => {
      getCategorys();
    });

    return {
      columns,
      pagination,

      queryParam,
      handleQuery,

      ebooks,
      paginationChange,
      loading,
      modalVisible,

      editItem,
      deleteItem,
      addItem,

      modalHandleOk,
      // modalHandleCancel,
      modalConfirmLoading,
      ebook,
      ebookRef,
      rules,
      formFinishFailed,

      categorys,
      categoryVal,
      getCategoryName,
    };
  },
});
</script>

<style lang="scss" scoped>
.admin {
  min-height: 600px;
  padding: 20px;

  .add-btn {
    display: flex;
    margin-bottom: 20px;
    align-items: center;
    justify-content: space-between;
  }
}

.editModal {
  display: flex;
  justify-content: center;

  .ant-form-item {
    margin-bottom: 10px;
  }
  .ant-form-item-label {
    width: 56px;
    margin-right: 20px;
  }
  .ant-input {
    width: 260px;
  }
}
</style>