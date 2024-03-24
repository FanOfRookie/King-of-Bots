<template>
    <ContentView>
        <table class="table table-hover">
            <thead>
                <tr>
                    <th>蓝方</th>
                    <th>红方</th>
                    <th>赛果</th>
                    <th>对局时间</th>
                    <th>对局回放</th>
                </tr> 
            </thead>
            <tbody>
                <tr v-for="record in records" :key="record.record.gameId">
                    <td>
                        <img :src="record.a_photo" alt="" class="record-user-photo">
                        &nbsp;
                        <span class="record-user-username">{{ record.a_username }}</span>
                    </td>
                    <td>
                        <img :src="record.b_photo" alt="" class="record-user-photo">
                        &nbsp;
                        <span class="record-user-username">{{ record.b_username }}</span>
                    </td>
                    <td>
                        <span class="record-result-lose" v-if="record.loserId == $store.state.user.id">失败</span>
                        <span class="record-result-draw" v-else-if="record.loserId == '平局'">平局</span>
                        <span class="record-result-win" v-else> 胜利 </span>
                    </td>
                    <td>{{ record.record.createTime }}</td>
                    <td>
                        <button type="button" class="btn btn-secondary" @click="displayRecord(record.record)">查看回放</button>
                    </td>
                </tr>
            </tbody>
        </table>
        <nav aria-label="...">
            <ul class="pagination" style="float: right;">
                <li :class="'page-item ' + lastPageStatus">
                    <a class="page-link" href="#" @click="pull_page(1)">&laquo;</a>
                </li>
                <li :class="'page-item ' + lastPageStatus " >
                    <a class="page-link"   href="#" @click="pull_page(currentPage - 1)">上一页</a>
                </li>
                
                <li :class="'page-item ' + page.is_active " v-for="page in pages" :key="page.number">
                    <a class='page-link ' href="#" @click="pull_page(page.index)">
                        {{ page.index }}
                    </a>
                </li>

                <li :class="'page-item ' + nextPageStatus">
                    <a class="page-link" href="#" @click="pull_page(currentPage + 1)">下一页</a>
                </li>
                <li :class="'page-item ' + nextPageStatus">
                    <a class="page-link" href="#" @click="pull_page(-1)">&raquo;</a>
                </li>
            </ul>
        </nav>
    </ContentView>
</template>
        
<script>
import ContentView from "@/components/ContentView.vue";
import {useStore} from "vuex"
import $ from "jquery"
import {ref} from 'vue'
import router from "@/router";

export default{
    components:{
        ContentView
    },
    setup(){
        const store = useStore()
        let currentPage = ref(1)
        const userId =  store.state.user.id
        let records = ref([])
        let maxPage = 0

        let pages = ref([])
        let lastPageStatus = ref('')
        let nextPageStatus = ref('')

        const updatePagesBar = () => {
            let new_pages = []
            for (let i = currentPage.value - 2; i <= currentPage.value + 2; i++) 
               if(i >= 1 && i <= maxPage)
                new_pages.push({
                    index: i,
                    is_active: i === currentPage.value ? "active" : ""
            })
            pages.value = new_pages
            if(currentPage.value == 1)
                lastPageStatus.value = 'disabled'
            else
                lastPageStatus.value = ''
            if(currentPage.value == maxPage)
                nextPageStatus.value = 'disabled'
            else
                nextPageStatus.value = ''
        }

        const pull_page = (page)=>{
            if(page == -1)
                page = maxPage
            currentPage.value = page
            $.ajax({
            url:"http://127.0.0.1:3000/record/getList/",
            type:"get",
            data:{
                userId,
                page,
            },
            headers:{
                Authorization: "Bearer " + store.state.user.token
            },
            success(resp){
                console.log(resp)
                records.value = resp.records
                maxPage = resp.maxPage
                updatePagesBar()
            },
            error(resp){
                console.log(resp)
            }
            })
        }

        pull_page(currentPage.value)

        const stringToArr = (mapString) => {
            let g =[]
            for(let i = 0, k = 0;i < 13; i++){
                let line = []
                for (let j = 0; j < 14; j++,k++)
                    if(mapString[k] === '0')
                        line.push(0)
                    else
                        line.push(1)
                g.push(line)
            }
            return g;
        }
        
        const displayRecord = (record)=>{
            store.commit("updateIsRecord",true)
            store.commit("updateGameInfo",{
                gameMap: stringToArr(record.map),
                a_id: record.aid,
                a_sx : record.asx,
                a_sy : record.asy,
                b_id : record.bid,
                b_sx : record.bsx,
                b_sy : record.bsy,
            })
            store.commit("updateSteps",{
                a_step : record.asteps,
                b_step : record.bsteps
            })
            store.commit("updateRecordLoser",record.loser)
            router.push({
                name:"display_record",
                params:{
                    recordId: record.gameId
                }
            })
        }

        return{
            records,
            displayRecord,
            pages,
            pull_page,
            currentPage,
            lastPageStatus,
            nextPageStatus,
        } 
    }
}
</script>
    
<style scoped>
img.record-user-photo{
    width: 5vh;
    border-radius: 50%;
}
span.record-result-lose{
    color: red;
    font-weight: 700;
    font-size: large;
}
span.record-result-draw{
    color: gray;
    font-weight: 700;
    font-size: large;
}
span.record-result-win{
    /* color: rgb(0, 119, 255); */
    color: gold;
    font-weight: 700;
    font-size: large;
}
</style>