<template>
    <playground v-if="$store.state.pk.status === 'playing'">
    </playground>
    <MatchGround v-if="$store.state.pk.status === 'matching' || $store.state.pk.status === 'success' ">
    </MatchGround>
    <ResultBoard/>
</template>
    
<script>
import playground from "@/components/PlayGround.vue";
import MatchGround from "@/components/MatchGround.vue"
//当组件被挂载，执行onMounted，反之则onUnmounted
import { onMounted,onUnmounted } from "vue"; 
import { useStore } from "vuex";
import ResultBoard from "@/components/ResultBoard.vue"
export default{
    components:{
        playground,
        MatchGround,
        ResultBoard
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
                console.log(data.gameInfo.gameMap)

                store.commit("updateStatus","success")
                setTimeout(() => {
                    store.commit("updateStatus","playing")
                }, 2000);
                }else if(data.event === "move"){
                    const game = store.state.pk.gameObject
                    const [snake0,snake1] = game.Snakes
                    snake0.setDirection(data.a_direction)
                    snake1.setDirection(data.b_direction)
                }else if(data.event === "result"){
                    console.log(data)
                    const game = store.state.pk.gameObject
                    const [snake0,snake1] = game.Snakes
                    if(data.loser === "all"){
                        snake0.status = "die"
                        snake1.status = "die"
                        store.commit("updateLoser","all")
                    }else if(data.loser ==="A"){
                        snake0.status = "die"
                        store.commit("updateLoser","A")
                    }
                    else if(data.loser ==="B"){
                        snake1.status = "die"
                        store.commit("updateLoser","B")
                    }
                }
            }

            socket.onclose = () =>{
                console.log("disconnected!")
            }
        })

        onUnmounted(()=>{
            store.commit("updateLoser","none")
            socket.close()
            store.commit("updateStatus","matching")
        })
    }
}
</script>

<style scoped>

</style>