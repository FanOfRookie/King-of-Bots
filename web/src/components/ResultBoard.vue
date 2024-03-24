<template>
    <div class="result-board" v-if="$store.state.pk.loser !== 'none'">
        <div class="result-board-draw" v-if="$store.state.pk.loser === 'all'">Draw!</div>
        <div class="result-board-lose" v-else-if="isLoser()">You Lose!</div>
        <div class="result-board-win" v-else>You Win!</div>
        <div class="result-board-lose" v-if="$store.state.pk.loser === 'all' || isLoser()">
            <button type="button" class="btn btn-danger btn-lg" @click="restart">再来一局</button>
        </div>
        <div class="result-board-win" v-else>
            <button type="button" class="btn btn-warning btn-lg" @click="restart">乘胜追击！</button>
        </div>
    </div>
</template>

<script>
import {useStore} from 'vuex'
export default{
    setup(){
        const store = useStore()
        const isLoser=()=>{
            if(store.state.pk.loser === 'A' && store.state.pk.a_id == store.state.user.id)
                return true
            if(store.state.pk.loser === 'B' && store.state.pk.b_id == store.state.user.id)
                return true
            return false
        }

        const restart = () =>{
            store.commit("updateStatus","matching")
            store.commit("updateLoser","none")
            store.commit("updateOpponent",{
                    username: "我的对手",
                    photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
                })
            }
        return {
            isLoser,
            restart
        }
    }
}
</script>

<style scoped>
    div.result-board{
        height: 30vh;
        width: 30vw;
        background-color: rgba(50,50,50,0.8);
        position: absolute;
        top: 30vh;
        left: 35vw;
    }
    div.result-board-win{
        text-align: center;
        color: gold;
        font-size: 50px;
        font-weight: 600;
        font-style: italic;
        padding-top: 5vh;
    }
    div.result-board-draw{
        text-align: center;
        color: white;
        font-size: 50px;
        font-weight: 600;
        font-style: italic;
        padding-top: 5vh;
    }
    div.result-board-lose{
        text-align: center;
        color: red;
        font-size: 50px;
        font-weight: 600;
        font-style: italic;
        padding-top: 5vh;
    }
    div.result-board-lose{
        text-align: center;
        padding-top: 5vh;
    }
    div.result-board-win{
        text-align: center;
        padding-top: 5vh;
    }
</style>
