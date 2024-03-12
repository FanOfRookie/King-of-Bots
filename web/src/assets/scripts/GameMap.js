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
        this.ctx.canvas.focus()

        const [snake0,snake1]=this.Snakes
        this.ctx.canvas.addEventListener("keydown",e=>{
            if(e.key=== 'w')
                snake0.setDirection(0)
            else if(e.key==='d')
                snake0.setDirection(1)
            else if(e.key==='s')
                snake0.setDirection(2)
            else if(e.key==='a')
                snake0.setDirection(3)
            else if(e.key=== 'ArrowUp')
                snake1.setDirection(0)
            else if(e.key==='ArrowRight')
                snake1.setDirection(1)
            else if(e.key==='ArrowDown')
                snake1.setDirection(2)
            else if(e.key==='ArrowLeft')
                snake1.setDirection(3)
        })
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