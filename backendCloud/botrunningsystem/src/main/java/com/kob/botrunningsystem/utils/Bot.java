package com.kob.botrunningsystem.utils;

import java.util.ArrayList;

public class Bot implements com.kob.botrunningsystem.utils.BotInterface{
    static class Cell{
        public int x,y;
        public Cell(int x,int y){
            this.x = x;
            this.y = y;
        }
    }

    int[] dx = {-1,0,1,0}, dy = {0,1,0,-1};

    //检验当前step蛇身长度是否增加
    private boolean checkTailIncreasing(int step){
        if(step <= 10)
            return true;
        return step % 3 == 1;
    }

    public ArrayList<Cell> getCellsString(int sx,int sy, String steps){
        ArrayList<Cell> cells = new ArrayList<>();
        int x = sx, y = sy;
        int step = 0;
        cells.add(new Cell(x,y));
        String[] directions = steps.split(",");
        for (int i = 0; i < directions.length; i++) {
            int d = Integer.parseInt(directions[i]);
            if(d == -1)
                continue;
            x += dx[d];
            y += dy[d];
            cells.add(new Cell(x,y));
            if(!checkTailIncreasing(++step))
                cells.remove(0);
    }
        return cells;
    }

    /*
    getMapString()
                + "#" + me.getSx()+ "#" + me.getSy() + "#" + me.getSteps()
                + "#" + you.getSx() + "#" + you.getSy() + "#" + you.getSteps();
     */

    @Override
    public Integer nextMove(String input) {
        String[] inputs = input.split("#");
        int[][] g = new int[13][14];
        for (int i = 0; i < 13; i++)
            for (int j = 0; j < 14; j++)
                g[i][j] = inputs[0].charAt(14 * i + j) - '0';
        int aSx = Integer.parseInt(inputs[1]), aSy = Integer.parseInt(inputs[2]);
        int bSx = Integer.parseInt(inputs[4]), bSy = Integer.parseInt(inputs[5]);
        ArrayList<Cell> aCells = getCellsString(aSx,aSy,inputs[3]);
        ArrayList<Cell> bCells = getCellsString(bSx,bSy,inputs[6]);
        for (Cell cell: aCells) g[cell.x][cell.y] = 1;
        for (Cell cell: bCells) g[cell.x][cell.y] = 1;
        for (int i = 0; i < 4; i++) {
            int x = aCells.get(aCells.size() - 1).x +dx[i];
            int y = aCells.get(aCells.size() - 1).y +dy[i];
            if(x > 0 && x < 13 && 0 < y && y < 14 && g[x][y] == 0)
                return i;
        }
        return 0;
    }
}
