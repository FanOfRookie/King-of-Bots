<template>
    <ContentView>
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="register">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="exampleFormControlInput1" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="exampleFormControlInput1" placeholder="请输入密码">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">确认密码</label>
                        <input v-model="confirmPassword" type="password" class="form-control" id="exampleFormControlInput1" placeholder="请再次输入密码">
                    </div>
                    <div class="error_message">
                        {{ error_message }}
                    </div>
                    <button type="submit" class="btn btn-primary" >注册</button>
                </form>
            </div>
        </div>
    </ContentView>
</template>
        
<script>
import ContentView from "@/components/ContentView.vue";
import { ref } from "vue";
import router from "@/router/index";
// import { useStore } from "vuex";
import $ from 'jquery'

export default{
    components:{
        ContentView
    },
    setup(){
        // const store=useStore()
        let username = ref('')
        let password = ref('')
        let confirmPassword = ref('')
        let error_message = ref('')

        const register = ()=>{
            console.log(confirmPassword.value)
            $.ajax({
            url:"http://127.0.0.1:3000/user/account/register/",
            type:"post",
            data:{
                username:username.value,
                password:password.value,
                confirmPassword:confirmPassword.value,
            },
            success(resp){
                if(resp.error_message === 'success')
                    router.push({name:"user_account_login"})
                else
                    error_message.value = resp.error_message
            },
            error(resp){
                error_message.value = resp.error_message
            }
            })
        }
        
        return{
            username,
            password,
            confirmPassword,
            error_message,
            register
        }
    }
}
</script>
    
<style scoped>
button.btn{
    width: 100%;
}
div.error_message{
    color: red;
    
}
</style>