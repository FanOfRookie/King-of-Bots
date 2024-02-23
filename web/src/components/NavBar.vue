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
          <router-link class="nav-link" :to="{name:'pk_index'}">对战</router-link>
        </li>
        <li class="nav-item">
            <router-link class="nav-link" :to="{name:'record_index'}">对局记录</router-link>
        </li>
        <li class="nav-item">
            <router-link class="nav-link" :to="{name:'ranklist_index'}">排行榜</router-link>
        </li>
      </ul>
      <ul class="navbar-nav ms-md-auto">
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            用户名
          </a>
          <ul class="dropdown-menu">
            <li>
                <router-link class="dropdown-item" :to="{name:'user_bots_index'}">我的Bots</router-link>
            </li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item" href="#">退出登录</a></li>
          </ul> 
        </li>
      </ul>

    </div>
  </div>
</nav>
</template>

<script>
</script>

<style scoped>
</style>