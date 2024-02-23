import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '@/views/pk/PkIndexView' 
import NotFoundView from '@/views/error/NotFoundView' 
import RankListIndexView from '@/views/ranklist/RankListIndexView' 
import RecordIndexView from '@/views/record/RecordIndexView' 
import UserBotsIndexView from '@/views/user/bots/UserBotsIndexView' 


const routes = [
  {
    path:"/",
    name:"pk",
    redirect:"/pk/",
  },
  {
    path:"/pk/",
    name:"pk_index",
    component:PkIndexView,
  },
  {
    path:"/ranklist/",
    name:"ranklist_index",
    component:RankListIndexView,
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
  },
  {
    path:"/user/bots/",
    name:"user_bots_index",
    component:UserBotsIndexView,
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

export default router
