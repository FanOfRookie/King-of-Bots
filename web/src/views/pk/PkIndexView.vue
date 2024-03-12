<template>
    <playground v-if="$store.state.pk.status === 'playing'">
    </playground>
    <MatchGround v-if="$store.state.pk.status === 'matching' || $store.state.pk.status === 'success' ">

    </MatchGround>
</template>
    
<script>
import playground from "@/components/PlayGround.vue";
import MatchGround from "@/components/MatchGround.vue"
//当组件被挂载，执行onMounted，反之则onUnmounted
import { onMounted,onUnmounted } from "vue"; 
import { useStore } from "vuex";
export default{
    components:{
        playground,
        MatchGround,
    },
    setup(){
        const store = useStore()

        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`
        let socket = null
        onMounted(()=>{
            //默认头像
            store.commit("updateOpponent",{
                    username: "我的对手",
                    photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
                })
            socket = new WebSocket(socketUrl)

            socket.onopen = () =>{
                console.log("connected!")
                store.commit("updateSocket",socket)
            }

            socket.onmessage = (msg) =>{
                const data = JSON.parse(msg.data)
                if(data.event === 'matching-success'){
                    store.commit("updateOpponent",{
                    username: data.opponent_username,
                    photo: data.opponent_photo
                })

                store.commit("updateGameInfo",data.gameInfo)

                store.commit("updateStatus","success")
                setTimeout(() => {
                    store.commit("updateStatus","playing")
                }, 2000);
                }
            }

            socket.onclose = () =>{
                console.log("disconnected!")
            }
        })

        onUnmounted(()=>{
            socket.close()
            store.commit("updateStatus","matching")
        })
    }
}
</script>

<style scoped>

</style>