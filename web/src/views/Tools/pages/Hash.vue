<template>
    <div class="hash">
        <div class="hash-text">
            <h1>加密</h1>
            <div class="hash-form">
                <a-textarea @change="beforeHashChange" v-model:value="beforeHash" placeholder="加密前文本" :rows="4" />
                <!-- <RightCircleFilled 
                    :style="{ fontSize: '24px', color: '#ccc', cursor: 'pointer', margin: '0 20px 0 20px' }" 
                    @click="getMD5"
                /> -->
                <div class="hash-type">
                    <div @click="getHash('MD5')">MD5 <CaretRightFilled /></div>
                    <div @click="getHash('SHA1')">SHA1 <CaretRightFilled /></div>
                    <div @click="getHash('SHA256')">SHA256 <CaretRightFilled /></div>
                    <div @click="getHash('SHA512')">SHA512 <CaretRightFilled /></div>
                </div>
                <a-textarea v-model:value="afterHash" readonly placeholder="加密后文本" :rows="4" />
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import axios from 'axios'

export default defineComponent ({
    name: "Hash",
    data(){
        return {
            beforeHash: '',
            afterHash: ''
        }
    },
    methods:{
        getHash(type: string){
            let data = {
                beforeHash: this.beforeHash,
                type
            }
            axios.post('/tools/hash', data).then(res => {
                let data = res.data;
                this.afterHash = data.content;
            })
        },
        beforeHashChange(){
            this.afterHash = '';
        }
    }

    

    
})
</script>

<style lang="scss">
.hash{
    .hash-text{
        display: flex;
        flex-direction: column;
        .hash-form{
            display: flex;
            align-items: center;
            width: 100%;
            .hash-type{
                display: flex;
                flex-direction: column;
                margin: 0 20px;
                div{
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    cursor: pointer;
                    color: #1890FF;
                    // width: 50px;
                }
            }
        }
    }
}
</style>