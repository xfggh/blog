<template>
    <div class="base64">
        <h1>Base64</h1>
        <h3>文本 base64</h3>

        <div class="base64-form">
            <a-textarea @change="valueChange(0)" v-model:value="beforValue" placeholder="文本" :rows="4" />
            <div class="base64-type">
                <label @click="textToBase64()">编码 <CaretRightFilled /></label>
                <label @click="base64ToText('0')"><CaretLeftFilled /> 解码 </label>
            </div>
            <a-textarea @change="valueChange(1)" v-model:value="afterValue" placeholder="文本 base64" :rows="4" />
        </div>

        <h3 style="margin-top: 20px">文件 base64 <a-button type="link" @click="resetClick">重置</a-button></h3>
        <div class="base64-form-image">
            <div class="image-upload">
                <a-upload
                    v-model:file-list="fileList"
                    action="http://localhost:8888/tools/upload"
                    name="file"
                    @change="handleUploadChange"
                    :showUploadList="false"
                    :before-upload="beforeUpload"
                >
                    <a-button>
                        <upload-outlined></upload-outlined>
                        Click to Upload
                    </a-button>
                </a-upload>
                <div class="image-preview">
                    <img :src="imageBase64" alt="">
                    <!-- <p v-if="!isImage">暂不支持预览</p> -->
                    <!-- <a-image
                        :max-width="200"
                        :max-height="200"
                        :src="imageBase64"
                        fallback="data:image/png;base64,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"
                    /> -->
                </div>
            </div>

            <div class="base64-type">
                <label>文件转base64 <CaretRightFilled /></label>
                <label><CaretLeftFilled /> base64转图片 </label>
            </div>

            <a-textarea v-model:value="imageBase64" placeholder="图片 base64" :rows="4" />

        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import axios from 'axios';

interface FileItem {
  uid: string;
  name?: string;
  status?: string;
  response?: string;
  url?: string;
  type?: string;
  size: number;
  originFileObj: any;
}

interface FileInfo {
  file: FileItem;
  fileList: FileItem[];
}

export default defineComponent ({
    name: "Base64",
    setup(){
        const fileList = ref([]);

        return {
            fileList
        }
    },
    data(){
        return {
            beforValue: '',
            afterValue: '',
            imageBase64: ''
        }
    },
    methods: {
        textToBase64(){
            console.log(this.beforValue);
            this.afterValue = window.btoa(this.beforValue);
        },
        base64ToText(type: string){
            let data = {
                afterValue: this.afterValue,
                type
            }
            axios.post('/tools/base64', data).then(res => {
                let data = res.data;
                if(data.success){
                    this.beforValue = data.content;
                }
            })
        },
        resetClick(){
            this.imageBase64 = '';
        },
        valueChange(type: number){
            if(type === 0){
                this.afterValue = '';
            }
            if(type === 1){
                this.beforValue = '';
            }
        },
        
        // 文件上传 处理
        beforeUpload(file: FileItem){
            console.log(file.originFileObj);
            console.log(file.url);
            console.log('beforeUpload');
            // return false;
        },
        handleUploadChange(info: FileInfo){
            if (info.file.status === 'uploading') {
                console.log('uploading');
                return ;
            }
            if (info.file.status === 'done') {
                console.log('done');
                this.getBase64(info.file.originFileObj, (base64Url: string) => {
                    this.imageBase64 = base64Url;
                });
            }
            // if (info.file.status === 'error') {
            //     loading.value = false;
            //     message.error('upload error');
            // }
        },
        getBase64(img: Blob, callback: (base64Url: string) => void) {
            const reader = new FileReader();
            reader.addEventListener('load', () => callback(reader.result as string));
            reader.readAsDataURL(img);
        }

    }

})
</script>

<style lang="scss">
.base64{
    display: flex;
    flex-direction: column;
    .base64-form{
        display: flex;
        align-items: center;
        
    }
    .base64-form-image{
        display: flex;

        .base64-type{
            label{
                width: 100px;
                color: #D9D9D9;
            }
        }

        .image-upload{
            display: flex;
            flex-direction: column;
            // justify-content: center;
            align-items: center;
            .image-preview{
                margin-top: 20px;
                img{
                    max-width: 300px;
                    max-height: 300px;
                }
            }
        }
    }


    .base64-type{
        label{
            display: flex;
            align-items: center;
            justify-content: center;
            width: 50px;
            margin: 0 20px;
            cursor: pointer;
            color: #1890FF;
            padding: 5px 0;
        }
    }
}
</style>