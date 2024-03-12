<template>
    <ContentView v-if="!$store.state.user.loadInfo">
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="login">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="exampleFormControlInput1" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="exampleFormControlInput1" placeholder="请输入密码">
                    </div>
                    <div class="error_message">
                        {{ error_message }}
                    </div>
                    <button type="submit" class="btn btn-primary">登录</button>
                </form>
            </div>
        </div>
    </ContentView>
</template>
        
<script>
import ContentView from "@/components/ContentView.vue";
import { useStore } from "vuex";
import {ref} from 'vue'
import router from "@/router/index";

export default{
    components:{
        ContentView
    },
    setup(){
        const store = useStore()
        //与上方的组件进行绑定
        let username = ref('')
        let password = ref('')
        let error_message = ref('')

        const jwt_token = localStorage.getItem('jwt_token')
        if(jwt_token){
            //从本地数据库中取出token并放入内存
            store.commit("updateToken",jwt_token)
            //验证token合法性
            store.dispatch("getInfo",{
                //token有效，则回到首页
                success(){
                    router.push({name:"pk"})
                    store.commit("updateLoadInfo",false)
                },
                //token无效，重新登录
                error(){
                    store.commit("updateLoadInfo",false)
                }
            })
        }else{
            store.commit("updateLoadInfo",false)
        }

        const login = ()=>{
            //通过dispatch调用定义在user.js action里面的函数
            store.dispatch("login",{
                //组件.value获取组件内的值
                username:username.value,
                password:password.value,
                //登录成功的回调函数
                success(){
                    store.dispatch("getInfo",{
                        success(){
                           //页面跳转
                            router.push({name:'pk'})
                            console.log(store.state.user)
                        },
                    })
                },
                error(resp){
                    console.log(resp)
                    error_message.value = '用户名或密码错误！'
                },
            })
        }

        return{
            username,
            password,
            error_message,
            login,
        }
    }
}
</script>
    
<style scoped>
button {
    width: 100%;
}
div.error_message{
    color: red;
    
}
</style>