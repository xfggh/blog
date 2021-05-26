<template>
  <div class="doc">
		<a-row v-if="level1.length > 0">
			<a-col :span="6" class="doc-nav">
				<a-tree
					v-model:selectedKeys="selectedKeys"
					v-if="level1.length > 0"
					:tree-data="level1"
					:replaceFields="{ children: 'children', title: 'name', key: 'id' }"
					:defaultExpandAll="true"
					@select="selectItem(selectedKeys)"
				/>

			</a-col>

			<a-col :span="18" class="doc-content">
				<div class="wangeditor" :innerHTML="contentHtml"></div>
			</a-col>

		</a-row>

    <a-empty v-if="level1.length <= 0" />
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import axios from "axios";
import { Tool } from "../util/Tools";
import { message } from "ant-design-vue";
import { useRoute } from "vue-router";

let ebookId: any;
export default defineComponent({
  name: "Doc",
  setup() {
    const route = useRoute();
    const level1 = ref();
    level1.value = [];

    const getDocList = (ebookId: any) => {
      console.log(Number(ebookId));
      if (!ebookId || isNaN(Number(ebookId))) {
        return;
      }

      axios.get(`/imoocDoc/list/${ebookId}`).then((res) => {
        let data = res.data;
        if (data.success) {
          let content = res.data.content;

          level1.value = Tool.array2Tree(content, 0);
          
          if(level1.value.length > 0){
            let id = level1.value[0].id
            selectedKeys.value = [id];
            getContent(id);
          }
          
        } else {
          message.error(data.message);
        }
      });
    };

		const selectedKeys = ref();
    selectedKeys.value = [];
		let contentHtml = ref();
    const selectItem = (selectedKeys: any) => {
			console.log(selectedKeys);
			let id = selectedKeys[0]
      if (!id || isNaN(Number(id))) {
        return;
      }

      getContent(id);
    };
		const getContent = (id: any) => {
			axios.get(`/imoocDoc/content/${id}`).then((res) => {
				if (res.data.success) {
					contentHtml.value = res.data.content;
				}
			});
		};

    onMounted(() => {
      ebookId = route.query.ebook;
      getDocList(ebookId);
    });

    return {
      level1,

      selectItem,
			selectedKeys,
			contentHtml
    };
  },
});
</script>

<style lang="scss">
.doc{
	padding: 20px 20px;
	.doc-nav{
		background-color: #fafafa;
	}
	.doc-content{
		padding-left: 16px;
	}

	
}
</style>