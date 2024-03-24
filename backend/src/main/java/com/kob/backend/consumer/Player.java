package com.kob.backend.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private int id;
    private int sx;
    private int sy;
    private List<Integer> steps = new ArrayList<>();
    public List<Cell> cells = new ArrayList<>();
    private int step=0;

    public Player(int id, int sx, int sy) {
        this.id = id;
        this.sx = sx;
        this.sy = sy;
        addStep(-1);
    }

    public void addStep(int d){
        steps.add(d);
        int[] dx = {-1,0,1,0}, dy = {0,1,0,-1};
        if(d > -1){
            sx += dx[d];
            sy += dy[d];
        }
        cells.add(new Cell(sx,sy));
        if(!checkTailIncreasing(++step))
            cells.remove(0);
    }

    //检验当前step蛇身长度是否增加
    private boolean checkTailIncreasing(int step){
        if(step <= 10)
            return true;
        return step % 3 == 1;
    }

    public String getCellsString(){
        StringBuilder builder = new StringBuilder();
        for (Integer d : steps)
            builder.append(d);
        return builder.toString();
    }
}
