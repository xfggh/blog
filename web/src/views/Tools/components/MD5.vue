<template>
    <div class="md5">
        <div class="md5-text">
            <label for="">文本加密</label>
            <div>
                <a-textarea @change="beforeMD5Change" v-model:value="beforeMD5" placeholder="加密前文本" :rows="4" />
                <RightCircleFilled 
                    :style="{ fontSize: '24px', color: '#ccc', cursor: 'pointer', margin: '0 20px 0 20px' }" 
                    @click="getMD5"
                />
                <a-textarea v-model:value="afterMD5" readonly placeholder="加密后文本" :rows="4" />
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import axios from 'axios'

export default defineComponent ({
    name: "MD5",
    data(){
        return {
            beforeMD5: '',
            afterMD5: ''
        }
    },
    methods:{
        getMD5(){
            console.log('getMD5');
            let data = {
                beforeMD5: this.beforeMD5
            }
            axios.post('http://127.0.0.1:8888/tools/md5', data).then(res => {
                let data = res.data;
                this.afterMD5 = data.content;
            })
        },
        beforeMD5Change(){
            this.afterMD5 = '';
        }
    }

    

    
})
</script>

<style lang="scss">
.md5{
    .md5-text{
        display: flex;
        flex-direction: column;
        div{
            display: flex;
            align-items: center;
            width: 100%;
        }
    }
}
</style>