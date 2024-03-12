<template>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container">
    <a class="navbar-brand" href="/">King Of Bots</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav">
        <li class="nav-item">
          <router-link :class="route_name=='pk_index'? 'nav-link active' :'nav-link' " :to="{name:'pk_index'}">对战</router-link>
        </li>
        <li class="nav-item">
            <router-link :class="route_name=='record_index'? 'nav-link active' :'nav-link' " :to="{name:'record_index'}">对局记录</router-link>
        </li>
        <li class="nav-item">
            <router-link :class="route_name=='ranklist_index'? 'nav-link active' :'nav-link' " :to="{name:'ranklist_index'}">排行榜</router-link>
        </li>
      </ul>
      <ul class="navbar-nav ms-md-auto" v-if="$store.state.user.is_login">
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            {{$store.state.user.username}}
          </a>
          <ul class="dropdown-menu">
            <li>
                <router-link class="dropdown-item" :to="{name:'user_bots_index'}">我的Bots</router-link>
            </li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item" href="#" @click="logout">退出登录</a></li>
          </ul> 
        </li>
      </ul>
      <ul class="navbar-nav ms-md-auto" v-else-if="!$store.state.user.loadInfo">
        <li class="nav-item">
          <router-link class="nav-link dropdown-toggle" :to="{name:'user_account_login'}" >
            点击登录
          </router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link dropdown-toggle" :to="{name:'user_account_register'}" >
            注册账户
          </router-link>
        </li>
      </ul>
    </div>
  </div>
</nav>
</template>

<script>
import { useRoute } from 'vue-router';
import { computed } from 'vue';
import { useStore } from 'vuex';
export default{
  setup(){
    const route=useRoute();
    //Computed可以被实时调用的抽象函数
    let route_name=computed(()=>route.name)

    const store = useStore()
    const logout=()=>{
      store.dispatch("logout")
    }
    return {
      route_name,
      logout
    }
    
  }
}
</script>

<style scoped>
</style>