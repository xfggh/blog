<template>
  <div class="user-manage">
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
      :dataSource="users"
      :columns="columns"
      :row-key="(record, index) => index"
      :pagination="pagination"
      @change="paginationChange"
      :loading="loading"
    >
      <!-- <template #cover="{ text: cover }">
        <img width="50" height="50" v-if="cover" :src="cover" alt="avatar" />
      </template> -->

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
    @cancel="modalHandleCancel"
    cancelText="取消"
    okText="确定"
    :confirm-loading="modalConfirmLoading"
  >
    <div class="editModal">
      <a-form
        ref="userRef"
        :model="user"
        labelAlign="right"
        :rules="rules"
        @finishFailed="formFinishFailed"
      >
        <a-form-item label="用户名" name="loginName">
          <a-input v-model:value="user.loginName" />
        </a-form-item>

        <a-form-item label="昵称" name="username">
          <a-input v-model:value="user.username" />
        </a-form-item>

        <a-form-item label="密码" name="password">
          <a-input v-model:value="user.password" />
        </a-form-item>
      </a-form>
    </div>
  </a-modal>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { message } from "ant-design-vue";
import axios from "axios";
import { Tool } from "./../../../util/Tools";
import { RuleObject } from "ant-design-vue/lib/form/interface";

interface User {
	id: string,
	loginName: string,
	username: string,
	password: string,
}

const columns = [
  {
    title: "用户名",
    dataIndex: "loginName",
    slots: { customRender: "cover" },
  },
  {
    title: "昵称",
    dataIndex: "username",
  },
  {
    title: "密码",
    dataIndex: "password",
  },
  {
    title: "Action",
    key: "action",
    slots: { customRender: "action" },
  }
];

export default defineComponent({
  name: "UserManage",
  setup() {
    const queryParam = ref();
    queryParam.value = {};

    const users = ref();
    const loading = ref(false);

    const handleQuery = () => {
      console.log(queryParam.value.name);
      getUserList({
        pageNum: 1,
        pageSize: pagination.value.pageSize
      });
    };

    const getUserList = (params: any) => {
      loading.value = true;
      axios
        .get("/user/list", {
          params: {
            pageNum: params.pageNum,
            pageSize: params.pageSize,
            loginName: queryParam.value.name,
          },
        })
        .then((res) => {
          loading.value = false;
          pagination.value.current = params.pageNum;

          let data = res.data;

          if (data.success) {
            let content = res.data.content;
            users.value = content.list;
            pagination.value.total = Number(content.total);
          } else {
            message.error(data.message);
          }
        });
    };

    // 分页
    const pagination = ref({
      current: 0,
      pageSize: 2,
      total: 0,
    });
    const paginationChange = (pagination: any) => {
      console.log(pagination);
      getUserList({
        pageNum: pagination.current,
        pageSize: pagination.pageSize,
      });
    };

    // 弹窗 编辑
    const modalVisible = ref(false);

    const addItem = () => {
      user.value = {} as User;
      modalVisible.value = true;
    };
    const editItem = (record: User) => {
      user.value = Tool.copy(record);
      modalVisible.value = true;
    };
    const deleteItem = (id: any) => {
      console.log("delete");
      axios.delete(`/user/delete/${id}`).then((res) => {
        pagination.value.total;
        if (res.data.success) {
          if (
            (pagination.value.total - 1) / pagination.value.pageSize >
            pagination.value.current - 1
          ) {
            getUserList({
              pageNum: pagination.value.current,
              pageSize: pagination.value.pageSize,
            });
          } else {
            getUserList({
              pageNum: pagination.value.current - 1,
              pageSize: pagination.value.pageSize,
            });
          }
        }
      });
    };

    const modalConfirmLoading = ref(false);
    const modalHandleOk = () => {
      userRef.value
        .validate()
        .then(() => {
          modalConfirmLoading.value = true;

          console.log(user.value);
          axios.post("/user/save", user.value).then((res) => {
            let data = res.data;
            if (data.success) {
              modalConfirmLoading.value = false;
              modalVisible.value = false;

              getUserList({
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
    const modalHandleCancel= () => {
      userRef.value.resetFields();
    }


    const formFinishFailed = (error: any) => {
      console.log(error);
    };

    const user = ref<User>();
    user.value = {} as User;
    const userRef = ref();
    
    const validator = (rule: RuleObject, value: string) => {
      let pattern = rule.pattern;

      if(!value){
        return Promise.reject('密码不能为空');
      }else if(!(pattern && pattern.test(value))){
        return Promise.reject('密码至少包含 数字和英文，长度6-20');
      }else{
        return Promise.resolve();
      }
      
    }
    const rules = {
      loginName: [{ required: true, message: "用户名不能为空", trigger: "change" }],
      username: [{ required: true, message: "用户名不能为空", trigger: "change" }],
      // password: [{ required: true, message: "密码 不能为空 至少包含 数字和英文，长度6-20", trigger: "change",
      //              pattern: '^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$'
      //           }]
      password: [{ validator: validator, pattern: /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$/, required: true, trigger: "change" }]
    };

    onMounted(() => {
      getUserList({ pageNum: 1, pageSize: 2 })
    })

    return {
      columns,
      pagination,

      queryParam,
      handleQuery,

      users,
      paginationChange,
      loading,
      modalVisible,

      editItem,
      deleteItem,
      addItem,

      modalHandleOk,
      modalHandleCancel,
      modalConfirmLoading,
      user,
      userRef,
      rules,
      formFinishFailed,
    };
  },
});
</script>

<style lang="scss" scoped>
.admin {
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