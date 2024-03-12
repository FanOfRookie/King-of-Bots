import $ from 'jquery'

export default{
    state: {
        id:"",
        username:"",
        photo:"",
        token:"",
        is_login:false,
        loadInfo:true,  //loadInfo是否正在拉取信息，用于判断前端组件是否应该显示
    },
    getters: {
    },
    //同步操作放mutations
    mutations: {
        updateUser(state,user){
            state.id = user.id
            state.username = user.username
            state.photo = user.photo
            state.is_login = user.is_login
        },
        updateToken(state,token){
            state.token = token
        },
        updateLoadInfo(state,loadInfo){
            state.loadInfo = loadInfo
        },
        logout(state){
            state.id = ""
            state.username = ""
            state.photo = ""
            state.token = ""
            state.is_login = false
        }
    },
    //异步操作放actions
    actions: {
        login(context,data){
            $.ajax({
                url:"http://localhost:3000/user/account/token/",
                type:"post",
                data:{
                    username:data.username,
                    password:data.password,
                },
                //接口访问成功的回调函数
                success(resp){
                    //返回状态的登录成功
                    if(resp.error_message === 'success'){
                        context.commit("updateToken",resp.token)
                        localStorage.setItem('jwt_token',resp.token)
                        //执行登录成功的回调
                        data.success(resp)
                    }
                    else
                        data.error(resp)
                },
                error(resp){
                    data.error(resp)
                },
            })
        },
        logout(context){
            localStorage.removeItem('jwt_token')
            context.commit("logout")
        },
        getInfo(context,data){
            $.ajax({
                url:"http://localhost:3000/user/account/info/",
                type:"get",
                headers:{
                    //context指的是user这个类的上下文
                  Authorization:"Bearer " + context.state.token},
                success(resp){
                    if(resp.error_message === "success"){
                        context.commit("updateUser",{
                            //解析resp
                            ...resp,
                            is_login:true,
                        })
                    }
                    data.success()
                },
                error(resp){
                    data.error(resp)
                },
              })
        },
    },
    modules: {
    }
}