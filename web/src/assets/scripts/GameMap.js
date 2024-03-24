import { AcGameObject } from "./AcGameObject";
import { Wall } from "./wall";
import { Snake } from "./Snake";

export class GameMap extends AcGameObject{
    constructor(ctx, parent, store){   //ctx 画布
        super()
        
        this.store = store
        this.ctx=ctx
        this.parent=parent
        this.L=0

        this.rows=13
        this.cols=14

        this.innerWallNum=40
        this.Walls=[]
        this.g=null

        this.Snakes=[
            new Snake({id:0,color:'blue',r:this.rows-2,c:1},this),
            new Snake({id:1,color:'red',r:1,c:this.cols-2},this)
        ]
    }

    start(){
        this.g = this.store.state.pk.gameMap
        for(let r=0;r<this.rows;r++)
        {
            for(let c=0;c<this.cols;c++)
                if(this.g[r][c])
                    this.Walls.push(new Wall(r,c,this))
        }
        this.addListeningEvents()
    }

    update_size(){
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols , this.parent.clientHeight / this.rows))
        this.ctx.canvas.width = this.L * this.cols
        this.ctx.canvas.height = this.L * this.rows
    }

    executeNextStep(){
        for(const snake of this.Snakes)
            snake.next_Step()
    }

    update(){
        this.update_size()
        if(this.checkNextStep())
            this.executeNextStep()
        this.render()
    }

    //渲染
    render(){
        const color_even = '#AAD751'
        const color_odds = '#A2D048'

        for(let i=0; i< this.cols;i++)
            for(let j=0; j< this.rows;j++)
                {
                    if((i+j)%2==0)
                        this.ctx.fillStyle=color_even
                    else
                        this.ctx.fillStyle=color_odds
                    this.ctx.fillRect(this.L * i,this.L * j,this.L,this.L)
                }

    }

    checkNextStep(){
        //判断蛇是否准备好了
        for(const  snake of this.Snakes){
            if(snake.status!=='idle') 
                return false
            if(snake.direction===-1)
                return false
        }
        return true
    }

    addListeningEvents(){
        if(this.store.state.record.isRecord){
            let k = 0
            const a_steps = this.store.state.record.a_step
            const b_steps = this.store.state.record.b_step
            const [snake0, snake1] = this.Snakes
            const loser = this.store.state.record.record_loser
            //每一定间隔执行下一步
            const interval_id = setInterval(()=>{
                //处理最后一步
                if( k >= a_steps.length - 1){
                    if(loser === "all"){
                        snake0.status = "die"
                        snake1.status = "die"
                        this.store.commit("updateLoser","all")
                    }else if(loser ==="A"){
                        snake0.status = "die"
                        this.store.commit("updateLoser","A")
                    }
                    else if(loser ==="B"){
                        snake1.status = "die"
                        this.store.commit("updateLoser","B")
                    }
                    this.store.commit("updateIsRecord",false)
                    this.store.commit("updateLoser","none")
                    clearInterval(interval_id)
                }else{
                    console.log(parseInt(b_steps[k]))
                    snake0.setDirection(parseInt(a_steps[k]))
                    snake1.setDirection(parseInt(b_steps[k]))
                }
                k++
            },350)
        }else{
            let d = -1
            this.ctx.canvas.focus()
            this.ctx.canvas.addEventListener("keydown",e=>{
                if(e.key=== 'w')
                    d = 0
                else if(e.key==='d')
                    d = 1
                else if(e.key==='s')
                    d = 2
                else if(e.key==='a')
                    d = 3
                if(d >= 0)
                    this.store.state.pk.socket.send(JSON.stringify({
                        event: "move",
                        direction: d,
                }))
            })
        }
    }

    //形参为头部的下一个位置
    checkValid(cell){
        //判断是否撞墙
        for(const wall of this.Walls)
            if(wall.r === cell.r && wall.c === cell.c)
                return false
        
        //判断是否装上自己的身体或其他蛇
        for(const snake of this.Snakes){
            let k=snake.cells.length
            //判断是否会装上尾部
            if(!snake.check_tail_increasing())
                k--
            for(let i=0;i<k;i++)
                if(snake.cells[i].c === cell.c && snake.cells[i].r === cell.r)
                    return false
        }
        return true 
    }
}