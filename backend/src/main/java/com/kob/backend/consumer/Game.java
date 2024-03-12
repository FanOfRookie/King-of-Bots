package com.kob.backend.consumer;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    final private int rows;
    final private int cols;
    final private int innerWallNum;
    int[][] g;
    final static int[] dx = {-1,0,1,0}, dy = {0,1,0,-1};

    private final Player playerA, playerB;

    public Game(int rows,int cols, int innerWallNum, int idA, int idB){
        this.rows = rows;
        this.cols = cols;
        this.innerWallNum = innerWallNum;
        this.g = new int[rows][cols];
        playerA = new Player(idA,rows-2,1,new ArrayList<>());
        playerB = new Player(idB,1,cols-2,new ArrayList<>());
    }

    public int[][] getG(){
        return g;
    }

    public Player getPlayerA(){
        return playerA;
    }

    public Player getPlayerB(){
        return playerB;
    }

    //判断地图是否可用
    private boolean checkGraphConnectivity(int sx,int sy,int tx,int ty){
        if(sx == tx && sy == ty)
            return true;
        //将源点设为已遍历
        g[sx][sy]=1;

        for (int i = 0; i < 4; i++) {
            int x = sx + dx[i], y = sy + dy[i];
            //x y未超限，且g[X][Y]未被访问
            if(x >= 0 && x < rows && y >= 0 && y < cols && g[x][y] == 0)
                if(checkGraphConnectivity(x,y,tx,ty)){
                    //由于g是全局变量，判断结束之后要恢复
                    g[sx][sy] = 0;
                    return true;
                }
        }

        //由于g是全局变量，判断结束之后要恢复
        g[sx][sy]=0;
        return false;
    }

    private boolean randomInitMap(){
        for (int i = 0; i < rows; i++)
            g[i][0] = g[i][cols - 1] = 1;
        for (int i = 0; i < cols; i++)
            g[0][i] = g[rows - 1][i] = 1;
        Random random = new Random();
        for (int i = 0; i < this.innerWallNum / 2; i++) {
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(rows);   // 0 ~ rows - 1
                int c = random.nextInt(cols);
                //重复点
                if(g[r][c] == 1 || g[rows - 1 - r][cols - 1 - c] == 1)
                    continue;
                //起始点
                if(r == rows - 2 && c == 1 || r == 1 && c == cols - 2)
                    continue;
                g[r][c] = g[rows - 1 - r][cols - 1 - c] = 1;
                break;
            }
        }
        return checkGraphConnectivity(rows-2,1,1,cols-2);
    }

    //随机创建一个地图
    public void createMap(){
        for (int i = 0; i < 1000; i++) {
            if(randomInitMap())
                break;
        }
    }
}
