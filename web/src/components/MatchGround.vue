<template>
    <div class="matchground" style="margin-top: 10ch;">
        <div class="row">
            <dov class="col-6">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.user.username }}
                </div>
            </dov>
            <dov class="col-6">
                <div class="user-photo">
                    <img :src="$store.state.pk.opponent_photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.pk.opponent_username }}
                </div>
            </dov>
        </div>
        <div class="col-12" style="text-align: center; padding-top: 10px">
            <div class="matching-status" v-if="$store.state.pk.status === 'success'" style="text-align: center;" >匹配成功!</div>
            <button type="button" class="btn btn-primary btn-lg" v-if="flag=== true" @click="match_btn_isClicked">开始匹配</button>
            <button type="button" class="btn btn-warning btn-lg" v-if="flag === false" @click="match_btn_isClicked">取消匹配</button>
        </div>
    </div>
</template>

<script>
import {ref} from 'vue'
import {useStore} from 'vuex'
export default{
    setup(){
        let flag = ref(true)
        const store = useStore()

        const match_btn_isClicked = () =>{
            if(flag.value === true){
                flag.value = false
                store.state.pk.socket.send(JSON.stringify({
                    event:"start-matching",
                }))
            }
            else {
                flag.value = true
                store.state.pk.socket.send(JSON.stringify({
                    event:"cancel-matching",
                    
                }))
            }
        }

        return {
            match_btn_isClicked,
            flag
        }
    }
}
</script>

<style scoped>
    div.matchground{
        width: 60vw;
        height: 70vh;
        margin: 40px auto;
        background-color: rgb(240, 241, 241,0.5);
    }

    div.user-photo{
        margin-top: 10ch;
        text-align: center;
    }
    div.user-photo > img{
        border-radius: 50%;
    }

    div.user-username{
        margin-top: 20px;
        text-align: center;
        font-size: 24px;
        font-weight: 500;
    }

    div.matching-status{
        font-size: 24px;
        font-weight: 500;
        color: rgb(165, 42, 99);
    }
</style>
