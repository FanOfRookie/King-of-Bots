<template>
    <div class="matchground" style="margin-top: 10ch;">
        <div class="row">
            <div class="col-6">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.user.username }}
                </div>
            </div>
            <div class="col-6">
                <div class="user-photo">
                    <img :src="$store.state.pk.opponent_photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.pk.opponent_username }}
                </div>
            </div>
        </div>
        <div class="col-12" style="text-align: center; padding-top: 10px">
            <div class="matching-status" v-if="$store.state.pk.status === 'success'" style="text-align: center;" >匹配成功!</div>
            <!-- <button type="button" class="btn btn-primary btn-lg" v-if="flag=== true" @click="match_btn_isClicked">开始匹配</button> -->
            <button type="button" class="btn btn-primary btn-lg" v-if="flag=== true" data-bs-toggle="modal" data-bs-target="#selectBot">开始匹配</button>
            <button type="button" class="btn btn-warning btn-lg" v-if="flag === false" @click="match_btn_isClicked">取消匹配</button>
                    <!-- Modal -->
            <div class="modal fade" id="selectBot" tabindex="-1" >
                <div class="modal-dialog modal-dialog-centered modal-xl">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h1 class="modal-title fs-5" id="exampleModalLabel">选择你的出战Bot</h1>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="user-select-bot">
                                <select v-model="selectedBot" class="form-select" aria-label="Default select example">
                                    <option value = '-1' selected>亲自上阵</option>
                                    <option v-for="bot in bots" :key="bot.botId" :value="bot.botId">{{ bot.title }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" @click="cancelEditBot" data-bs-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" @click="match_btn_isClicked">开始匹配</button>
                        </div>
                        </div>
                    </div>
            </div>
        </div>
    </div>
</template>

<script>
import {ref} from 'vue'
import {useStore} from 'vuex'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import $ from'jquery'
export default{
    setup(){
        let flag = ref(true)
        let bots = ref([])
        const store = useStore()
        let selectedBot = ref("-1")

        const match_btn_isClicked = () =>{
            if(flag.value === true){
                flag.value = false
                console.log(selectedBot.value)
                store.state.pk.socket.send(JSON.stringify({
                    event:"start-matching",
                    bot_id: selectedBot.value
                }))
                Modal.getInstance("#selectBot").hide()
            }
            else {
                flag.value = true
                store.state.pk.socket.send(JSON.stringify({
                    event:"cancel-matching",
                }))
            }
        }

        const refreshBots =()=>{
            $.ajax({
            url:"http://127.0.0.1:3000/user/bot/getBotList/",
            type:"get",
            headers:{
                Authorization: "Bearer " + store.state.user.token
            },
            success(resp){
                bots.value = resp
            },
            error(resp){
                console.log(resp)
            }
            })
        }

        refreshBots()

        return {
            match_btn_isClicked,
            flag,
            bots,
            selectedBot
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
    div.user-select-bot > select{
        width: 60%;
        margin: 0 auto;
    }
</style>
