<template>
    <div class="base64">
        <h1>Base64</h1>
        <h3>文本 base64</h3>

        <div class="base64-form">
            <a-textarea @change="valueChange(0)" v-model:value="beforValue" placeholder="Basic usage" :rows="4" />
            <div class="base64-type">
                <label @click="textToBase64()">编码 <CaretRightFilled /></label>
                <label @click="base64ToText()"><CaretLeftFilled /> 解码 </label>
            </div>
            <a-textarea @change="valueChange(1)" v-model:value="afterValue" placeholder="Basic usage" :rows="4" />
        </div>

        <h3 style="margin-top: 20px">图片 base64</h3>
        <div class="base64-form-image">
            <div class="image-upload">
                <a-upload
                    v-model:file-list="fileList"
                    name="file"
                    :multiple="true"
                    action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
                    :headers="headers"
                    @change="handleChange"
                >
                    <a-button>
                    <upload-outlined></upload-outlined>
                    Click to Upload
                    </a-button>
                </a-upload>
                <div class="image-preview">
                    
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
export default defineComponent ({
    name: "Base64",
    data(){
        return {
            beforValue: '',
            afterValue: ''
        }
    },
    methods: {
        textToBase64(){
            console.log(this.beforValue);
            this.afterValue = window.btoa(this.beforValue);
        },
        base64ToText(){
            console.log('');
        },
        valueChange(type: number){
            if(type === 0){
                this.afterValue = ''
            }
            if(type === 1){
                this.beforValue = ''
            }
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
}
</style>