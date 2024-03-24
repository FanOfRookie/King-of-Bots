import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '@/views/pk/PkIndexView' 
import NotFoundView from '@/views/error/NotFoundView' 
import RankListIndexView from '@/views/ranklist/RankListIndexView' 
import RecordIndexView from '@/views/record/RecordIndexView' 
import UserBotsIndexView from '@/views/user/bots/UserBotsIndexView' 
import UserAccountLoginView from "@/views/user/account/UserAccountLoginView"
import UserAccountRegisterView from "@/views/user/account/UserAccountRegisterView"
import DisplayRecordView from '@/views/record/DisplayRecordView.vue'
import store from '@/store/index'

const routes = [
  {
    path:"/",
    name:"pk",
    redirect:"/pk/",
    meta:{
      requestAuth:true,
    }
  },
  {
    path:"/pk/",
    name:"pk_index",
    component:PkIndexView,
    meta:{
      requestAuth:true,
    }
  },
  {
    path:"/ranklist/",
    name:"ranklist_index",
    component:RankListIndexView,
    meta:{
      requestAuth:true,
    }
  },
  {
    path:"/404/",
    name:"404NotFound",
    component:NotFoundView,
  },
  {
    path:"/record/",
    name:"record_index",
    component:RecordIndexView,
    meta:{
      requestAuth:true,
    }
  },
  {
    path:"/record/:recordId/",
    name:"display_record",
    component:DisplayRecordView,
    meta:{
      requestAuth:true,
    }
  },
  {
    path:"/user/bots/",
    name:"user_bots_index",
    component:UserBotsIndexView,
    meta:{
      requestAuth:true,
    }
  },
    {
    path:"/user/account/login",
    name:"user_account_login",
    component:UserAccountLoginView,
    meta:{
      requestAuth:false,
    }
  },
  {
    path:"/user/account/register",
    name:"user_account_register",
    component:UserAccountRegisterView,
    meta:{
      requestAuth:false,
    }
  },
  {
    path:"/:catchAll(.*)",
    redirect:"/404/",
  },

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

//每次通过router进入页面之前会调用该函数
//to表示目的页面
//from表示源页面
//next表示页面执行的下一步操作
router.beforeEach((to,from,next)=>{
  if(to.meta.requestAuth && !store.state.user.is_login )
    next({name:"user_account_login"})
  else
    next()
})

export default router
