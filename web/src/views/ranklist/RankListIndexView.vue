<template>
    <ContentView>
        <table class="table table-hover">
            <thead>
                <tr>
                    <th>玩家</th>
                    <th>天梯积分</th>
                </tr> 
            </thead>
            <tbody>
                <tr v-for="user in users" :key="user.id">
                    <td>
                        <img :src="user.photo" alt="" class="record-user-photo">
                        &nbsp;
                        <span class="record-user-username">{{ user.username }}</span>
                    </td>
                    <td>
                        {{ user.rating }}
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

export default{
    components:{
        ContentView
    },
    setup(){
        const store = useStore()
        let currentPage = ref(1)
        let users = ref([])
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
            url:"http://127.0.0.1:3000/ranklist/getlist/",
            type:"get",
            data:{
                page,
                pageSize:10,
                userId: store.state.user.id
            },
            headers:{
                Authorization: "Bearer " + store.state.user.token
            },
            success(resp){
                console.log(maxPage)
                users.value = resp.users
                maxPage = resp.maxPage
                updatePagesBar()
            },
            error(resp){
                console.log(resp)
            }
            })
        }

        pull_page(currentPage.value)

        return{
            users,
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