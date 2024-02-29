const AC_GAME_OBJECT=[]

export class AcGameObject{
    constructor(){
        AC_GAME_OBJECT.push(this)
        this.has_called_start=false //是否被调用
        this.timeDelta=0            //时间间隔，速度
    }

    //初始化执行一次
    start(){
    }

    //每一帧执行
    update(){

    }

    //回收前调用
    on_destroy(){

    }

    destroy(){
        this.on_destroy()

        for(let i in AC_GAME_OBJECT){
            const obj=AC_GAME_OBJECT[i]
            if(obj === this){
                AC_GAME_OBJECT.splice(i)
                break
            }
        }
    }
}

let last_timestamp
const step= timestamp =>{

    for(let obj of AC_GAME_OBJECT){
        if(!obj.has_called_start){
            obj.has_called_start=true
            obj.start()
        }
        else{
            obj.timeDelta=timestamp-last_timestamp
            obj.update()
        }
    }

    last_timestamp=timestamp
    requestAnimationFrame(step)
}

//在下一帧执行step函数
requestAnimationFrame(step)