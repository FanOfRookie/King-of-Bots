<template>
<div class="container">
    <div class="row">
        <div class="col-3">
            <div class="card" style="margin-top: 20px;">
                <div class="card-body">
                    <img :src="$store.state.user.photo" alt="" style="width: 100%;">
                </div>
            </div>
        </div>
        <div class="col-9">
            <div class="card" style="margin-top: 20px;">
                <div class="card-header">
                    <span style="font-size: 140%;">
                        我的Bots
                    </span>
                    <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#editBot">创建bot</button>
                </div>
                <!-- Modal -->
                <div class="modal fade" id="editBot" tabindex="-1" >
                    <div class="modal-dialog modal-dialog-centered modal-xl">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h1 class="modal-title fs-5" id="exampleModalLabel">创建Bot</h1>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form>
                                <div class="mb-3">
                                    <label for="add-bot-title" class="form-label">Bot名称</label>
                                    <input v-model="addBot.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                                </div>
                                <div class="mb-">
                                    <label class="form-label" for="add-bot-description">Bot简介</label>
                                    <textarea v-model="addBot.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot简介"></textarea>
                                </div>
                                <div class="mb-">
                                    <label class="form-label" for="add-bot-content">Bot Code</label>
                                    <VAceEditor
                                        v-model:value="addBot.content"
                                        @init="editorInit"
                                        lang="c_cpp"
                                        theme="textmate"
                                        style="height: 300px" 
                                        placeholder="请编写Bot's Code"/>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <div class="error_message">{{ addBot.error_message }}</div>
                            <button type="button" class="btn btn-secondary" @click="cancelEditBot" data-bs-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" @click="add_bot">创建</button>
                        </div>
                        </div>
                    </div>
                </div>
                <div class="card-body">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>Bot名称</th>
                                <th>创建时间</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="bot in bots" :key="bot.botId">
                                <td>{{ bot.title }}</td>
                                <td>{{ bot.createTime }}</td>
                                <td>
                                    <button type="button" class="btn btn-warning" style="margin-right: 10px;" data-bs-toggle="modal" :data-bs-target="'#updateBot'+bot.botId">修改</button>
                                    <button type="button" class="btn btn-danger" @click="remove_bot(bot)">删除</button>

                                    <div class="modal fade" :id="'updateBot'+bot.botId" tabindex="-1" >
                                        <div class="modal-dialog modal-dialog-centered modal-xl">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h1 class="modal-title fs-5" id="exampleModalLabel">创建Bot</h1>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                            </div>
                                            <div class="modal-body">
                                                <form>
                                                    <div class="mb-3">
                                                        <label for="add-bot-title" class="form-label">Bot名称</label>
                                                        <input v-model="bot.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                                                    </div>
                                                    <div class="mb-">
                                                        <label class="form-label" for="add-bot-description">Bot简介</label>
                                                        <textarea v-model="bot.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot简介"></textarea>
                                                    </div>
                                                    <div class="mb-">
                                                        <label class="form-label" for="add-bot-content">Bot Code</label>
                                                        <VAceEditor
                                                            v-model:value="bot.content"
                                                            @init="editorInit"
                                                            lang="c_cpp"
                                                            theme="textmate"
                                                            style="height: 300px" 
                                                            placeholder="请编写Bot's Code"
                                                            />
                                                    </div>
                                                </form>
                                            </div>
                                            <div class="modal-footer">
                                                <div class="error_message">{{ bot.error_message }}</div>
                                                <button type="button" class="btn btn-secondary" @click="cancelModifyBot(bot)" data-bs-dismiss="modal">取消</button>
                                                <button type="button" class="btn btn-primary" @click="update_bot(bot)">修改</button>
                                            </div>
                                            </div>
                                        </div>
                                    </div>
                                    
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

</div>
</template>
        
<script>
//ref用于绑定变量
//reactive用于绑定对象
import {ref , reactive} from 'vue'
import $ from 'jquery'
import {useStore} from 'vuex'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';


export default{
    components:{
        VAceEditor
    },
    setup(){
        ace.config.set(
            "basePath", 
            "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")

        const bots = ref([])
        const addBot = reactive({
            title:"",
            description:"",
            content:"",
            error_message:"",
        })
        const store = useStore()
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

        const add_bot = ()=>{
            //清空上一次的报错信息
            addBot.error_message = ""
            $.ajax({
            url:"http://127.0.0.1:3000/user/bot/add/",
            type:"post",
            headers:{
                Authorization: "Bearer " + store.state.user.token
            },
            data:{
                title : addBot.title,
                content : addBot.content,
                description : addBot.description,
            },
            success(resp){
                if(resp.error_message === "success"){
                    addBot.title = ""
                    addBot.description = ""
                    addBot.content = ""
                    Modal.getInstance("#editBot").hide()
                    refreshBots()
                }
                else
                    addBot.error_message = resp.error_message
            },
            error(resp){
                console.log(resp)
            }
            })
        }

        const remove_bot = ( bot )=>{
            console.log(bot)
            //清空上一次的报错信息
            addBot.error_message = ""
            $.ajax({
            url:"http://127.0.0.1:3000/user/bot/remove/",
            type:"post",
            headers:{
                Authorization: "Bearer " + store.state.user.token
            },
            data:{
                bot_id: bot.botId,
            },
            success(resp){
                if(resp.error_message === "success"){
                    refreshBots()
                }
                else
                    addBot.error_message = resp.error_message
            },
            error(resp){
                console.log(resp)
            }
            })
        }

        const update_bot = ( bot )=>{
            //清空上一次的报错信息
            addBot.error_message = ""
            $.ajax({
            url:"http://127.0.0.1:3000/user/bot/update/",
            type:"post",
            headers:{
                Authorization: "Bearer " + store.state.user.token
            },
            data:{
                bot_id : bot.botId,
                title : bot.title,
                content : bot.content,
                description : bot.description,
            },
            success(resp){
                if(resp.error_message === "success"){
                    Modal.getInstance("#updateBot"+bot.botId).hide()
                    refreshBots()
                }
                else
                    bot.error_message = resp.error_message
            },
            error(resp){
                console.log(resp)
            }
            })
        }

        const cancelModifyBot = (bot)=>{
            refreshBots()
            Modal.getInstance("#updateBot"+bot.botId).hide()
        }

        const cancelEditBot = ()=>{
            addBot.title = ""
            addBot.description = ""
            addBot.content = ""
            Modal.getInstance("#editBot").hide()
        }

        refreshBots()
        return{
            bots,
            addBot,
            add_bot,
            cancelEditBot,
            remove_bot,
            update_bot,
            cancelModifyBot,
        }
    }
  
}
</script>

<style scoped>
div.error_message{
    color: red;
}
</style>