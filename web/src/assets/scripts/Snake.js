import { AcGameObject } from "./AcGameObject";
import { Cell } from "./Cell";

export class Snake extends AcGameObject{
    constructor(info,gamemap){
        super()
        this.id=info.id
        this.color=info.color
        this.gamemap=gamemap

        this.cells=[new Cell(info.r,info.c)]    //存放蛇的身体，Cells[0]为蛇头

        this.speed=3

        this.direction=-1   //-1表示没有指令 0,1,2,3, 上右下左
        this.status='idle'  //'idle'表示静止，‘move'表示移动,'die'表示死亡
        this.nextStep=null

        this.dr=[-1,0,1,0]
        this.dc=[0,1,0,-1]

        this.step=0         //当前步数
        this.eps = 1e-2     //两点坐标允许的误差
        this.snakeWidth=0.8 //舍身宽度

        this.eyeDirection=0 //眼睛朝向，0,1,2,3, 上右下左
        if(this.id === 1)   //右上角的蛇初始朝上
            this.eyeDirection = 2 

        this.eye_dx=[
            [-1,1],         //两只眼睛，两个偏移量
            [1,1],
            [1,-1],
            [-1,-1],
        ]
        this.eye_dy=[
            [-1,-1],
            [-1,1],
            [1,1],
            [1,-1],
        ]
    }

    start(){
 
    }

    setDirection(d){
        this.direction=d
    }

    //检测当前step尾部是否应该增加
    check_tail_increasing(){
        if(this.step<=10)
            return true
        if(this.step % 3 === 1)
            return true
        return false
    }

    next_Step(){
        const d=this.direction
        this.nextCell=new Cell(this.cells[0].r+this.dr[d],this.cells[0].c+this.dc[d])
        this.eyeDirection=d
        this.direction=-1
        this.status='move'
        this.step++

        const  k=this.cells.length
        for (let l = k; l >0 ; l--) {
            this.cells[l]=JSON.parse(JSON.stringify(this.cells[l-1]))
        }

        if(!this.gamemap.checkValid(this.nextCell)){
            this.status='die'
        }
    }

    update_move(){
        const dx = this.nextCell.x - this.cells[0].x
        const dy = this.nextCell.y - this.cells[0].y
        
        const distance=Math.sqrt(dx * dx + dy * dy)

        if(distance < this.eps){    //走到了目的地
            this.status = 'idle'
            this.cells[0]=this.nextCell //将目标点作为新的头部
            this.nextCell=null
            if(!this.check_tail_increasing())   //如果不应该增加，则去掉尾部
                this.cells.pop()
        }else{
            //更新头部
            const moveDistance=this.speed * this.timeDelta / 1000
            this.cells[0].x += moveDistance * dx / distance
            this.cells[0].y += moveDistance * dy / distance

            //更新尾部
            if(!this.check_tail_increasing()){
                const k = this.cells.length
                const tail = this. cells[k-1]
                const target = this.cells[k-2]
                const tail_dx = target.x - tail.x
                const tail_dy = target.y - tail.y
                tail.x += moveDistance * tail_dx / distance
                tail.y += moveDistance * tail_dy / distance
            }
        }
    }

    update(){
        if(this.status === 'move')
            this.update_move()
        this.render()
    }

    render(){
        const L=this.gamemap.L
        const ctx=this.gamemap.ctx

        ctx.fillStyle=this.color

        if(this.status === 'die')
            ctx.fillStyle='white'

        for(const cell of this.cells){
            ctx.beginPath()
            ctx.arc(cell.x*L,cell.y*L, L / 2 * this.snakeWidth, 0,Math.PI*2)
            ctx.fill()
        }

        for(let i=1; i< this.cells.length ; i++){
            const a=this.cells[i-1], b=this.cells[i]
            if(Math.abs(a.x-b.x)<this.eps && Math.abs(a.y-b.y)<this.eps)      //两点重合
                continue
            if(Math.abs(a.x-b.x)<this.eps){    //沿着x轴
                ctx.fillRect((a.x - 0.5*this.snakeWidth) * L, Math.min(a.y,b.y)  * L,L * this.snakeWidth,Math.abs(a.y - b.y) * L)
            }else{                              //沿着y轴
                ctx.fillRect(Math.min(a.x, b.x) * L,  (a.y - 0.5 * this.snakeWidth) * L, Math.abs(a.x - b.x)*L, L * this.snakeWidth)
            }

        }

        //画眼睛
        ctx.fillStyle='black'
        for(let i=0;i<2;i++){
            const eye_x = (this.cells[0].x + this.eye_dx[this.eyeDirection][i] * 0.15) * L;
            const eye_y = (this.cells[0].y + this.eye_dy[this.eyeDirection][i] * 0.15) * L;
            ctx.beginPath()
            ctx.arc(eye_x,eye_y, L*0.05,0,Math.PI * 2)
            ctx.fill()
        }
    }
}