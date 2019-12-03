package org.liky.game.frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FiveChessFrame extends JFrame implements MouseListener, Runnable {

    int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    //保存棋子坐标
    int x = 0;
    int y = 0;
    //保存之前下过的全部棋子的坐标
    //0表示没有棋子，1表示黑子，2表示白子
    int[][] allChess = new int[19][19];
    //标识下一步应该是黑棋还是白棋
    boolean isBlack = true;
    //标识当前游戏是否可以继续
    boolean canPlay = true;
    //保存显示的提示信息
    String message = "黑方先行";
    //保存最多拥有多少时间(秒)
    int maxTime = 0;
    //做倒计时的线程类
    Thread t = new Thread(this);
    //保存黑方与白方的剩余时间
    int blackTime = 0;
    int whiteTime = 0;
    //保存双方剩余时间的显示信息
    String blackMessage = "无限制";
    String whiteMessage = "无限制";
    BufferedImage bgImage = null;

    public FiveChessFrame() {
        this.setTitle("五子棋");
        this.setSize(500, 500);
        this.setLocation((width - 500) / 2, (height - 500) / 2);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.addMouseListener(this);
        t.start();
        t.suspend();
        this.repaint();

        try {
            bgImage = ImageIO.read(new File("background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        //双缓冲技术，防止屏幕闪烁
        BufferedImage bi = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics g2 = bi.createGraphics();
        g2.setColor(Color.BLACK);
        //绘制背景
        g2.drawImage(bgImage, 3, 20, this);
        g2.setFont(new Font("黑体", Font.BOLD, 26));
        g2.drawString("游戏信息：" + message, 130, 60);
        g2.setFont(new Font("宋体", 0, 14));
        g2.drawString("黑方时间：" + blackMessage, 40, 490);
        g2.drawString("白方时间：" + whiteMessage, 260, 490);
        //绘制棋盘
        for (int i = 0; i < 19; i++) {
            g2.drawLine(13, 73 + 20 * i, 375, 73 + 20 * i);
            g2.drawLine(13 + 20 * i, 73, 13 + 20 * i, 450);
        }
        //绘制棋盘上的九个点
        g2.fillOval(70, 130, 5, 5);
        g2.fillOval(310, 130, 5, 5);
        g2.fillOval(310, 370, 5, 5);
        g2.fillOval(70, 370, 5, 5);
        g2.fillOval(310, 250, 5, 5);
        g2.fillOval(190, 130, 5, 5);
        g2.fillOval(70, 250, 5, 5);
        g2.fillOval(190, 370, 5, 5);
        g2.fillOval(190, 250, 5, 5);
        //绘制棋子
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (allChess[i][j] == 1) {
                    //绘制黑子
                    int tempX = i * 20 + 13;
                    int tempY = j * 20 + 73;
                    g2.fillOval(tempX - 7, tempY - 7, 14, 14);
                }
                if (allChess[i][j] == 2) {
                    //绘制白子
                    int tempX = i * 20 + 13;
                    int tempY = j * 20 + 73;
                    g2.setColor(Color.white);
                    g2.fillOval(tempX - 7, tempY - 7, 14, 14);
                    g2.setColor(Color.BLACK);
                    g2.drawOval(tempX - 7, tempY - 7, 14, 14);
                }
            }
        }
        g.drawImage(bi, 0, 0, this);
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    //下棋
    public void mousePressed(MouseEvent e) {
        /*System.out.println("X:"+e.getX());
        System.out.println("Y:"+e.getY());*/
        if (canPlay == true) {
            x = e.getX();
            y = e.getY();
            if (x >= 13 && x <= 373 && y >= 73 && y <= 430) {
                x = (x - 13) / 20;
                y = (y - 73) / 20;
                if (allChess[x][y] == 0) {

                    if (isBlack == true) {
                        allChess[x][y] = 1;
                        isBlack = false;
                        message = "轮到白方";
                    } else {
                        allChess[x][y] = 2;
                        isBlack = true;
                        message = "轮到黑方";
                    }
                    //判断当前落子是否形成5子连珠
                    boolean winFlag = this.checkWin();
                    if (winFlag == true) {
                        JOptionPane.showMessageDialog(this, "游戏结束！"
                                + (allChess[x][y] == 1 ? "黑方" : "白方") + "获胜");
                        canPlay = false;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "当前位置已经落子,请重新落子");
                }
                this.repaint();//表示重新执行一次paint方法
            }
        }
        //System.out.println(e.getX() + "--" + e.getY());
        //点击开始游戏按钮
        if (e.getX() >= 408 && e.getX() <= 478 && e.getY() >= 74 && e.getY() <= 105) {
            int result = JOptionPane.showConfirmDialog(this, "是否重新开始游戏？");
            if (result == 0) {//现在重新开始游戏
                //1.清空棋盘，allchess数组归零
                //2.将游戏信息的显示重置
                //3.将下一步下棋的标记重置
                allChess = new int[19][19];
                message = "黑方先行";
                isBlack = true;
                canPlay = true;
                blackTime = maxTime;
                whiteTime = maxTime;
                if (maxTime > 0) {
                    blackMessage = maxTime / 3600 + ":"
                            + (maxTime / 60 - maxTime / 3600 * 60) + ":"
                            + (maxTime - maxTime / 60 * 60);
                    whiteMessage = maxTime / 3600 + ":"
                            + (maxTime / 60 - maxTime / 3600 * 60) + ":"
                            + (maxTime - maxTime / 60 * 60);
                    t.resume();
                } else {
                    blackMessage = "无限制";
                    whiteMessage = "无限制";
                }
                this.repaint();
            }
        }
        //点击游戏设置按钮
        if (e.getX() >= 408 && e.getX() <= 478 && e.getY() >= 130 && e.getY() <= 160) {
            String input = JOptionPane.showInputDialog("请输入游戏的最大时间:(分钟),如果输入0表示没有时间限制");
            try {
                maxTime = Integer.parseInt(input) * 60;
                if (maxTime < 0) {
                    JOptionPane.showMessageDialog(this, "请输入正数！");
                }
                if (maxTime == 0) {
                    int result = JOptionPane.showConfirmDialog(this, "设置完成，是否重新开始游戏？");
                    if (result == 0) {
                        allChess = new int[19][19];
                        message = "黑方先行";
                        isBlack = true;
                        blackTime = maxTime;
                        whiteTime = maxTime;
                        blackMessage = "无限制";
                        whiteMessage = "无限制";
                        this.repaint();
                    }
                }
                if (maxTime > 0) {
                    int result = JOptionPane.showConfirmDialog(this, "设置完成，是否重新开始游戏？");
                    if (result == 0) {
                        allChess = new int[19][19];
                        message = "黑方先行";
                        isBlack = true;
                        blackTime = maxTime;
                        whiteTime = maxTime;
                        blackMessage = maxTime / 3600 + ":"
                                + (maxTime / 60 - maxTime / 3600 * 60) + ":"
                                + (maxTime - maxTime / 60 * 60);
                        whiteMessage = maxTime / 3600 + ":"
                                + (maxTime / 60 - maxTime / 3600 * 60) + ":"
                                + (maxTime - maxTime / 60 * 60);
                        t.resume();
                        this.repaint();

                    }
                }
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(this, "请正确输入信息！");
            }
        }
        //点击游戏说明按钮
        if (e.getX() >= 408 && e.getX() <= 478 && e.getY() >= 180 && e.getY() <= 210) {
            JOptionPane.showMessageDialog(this, "这是一个五子棋程序，黑白双方轮流下棋，先连上五子的一方获胜！");
        }
        if (e.getX() >= 408 && e.getX() <= 478 && e.getY() >= 286 && e.getY() <= 317) {
            int result = JOptionPane.showConfirmDialog(this, "是否确认认输？");
            if (result == 0) {
                if (isBlack) {
                    JOptionPane.showMessageDialog(this, "黑方已经认输，游戏结束！");
                } else {
                    JOptionPane.showMessageDialog(this, "白方已经认输，游戏结束！");
                }
                allChess = new int[19][19];
                message = "黑方先行";
                isBlack = true;
                this.repaint();
                //canPlay = false;
            }
        }
        if (e.getX() >= 408 && e.getX() <= 478 && e.getY() >= 338 && e.getY() <= 368) {
            JOptionPane.showMessageDialog(this, "本游戏由SWUFE制作。。。");
        }
        if (e.getX() >= 408 && e.getX() <= 478 && e.getY() >= 391 && e.getY() <= 421) {
            JOptionPane.showMessageDialog(this, "游戏结束!");
            System.exit(0);
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    private boolean checkWin() {
        boolean flag = false;
        //保存共有多少相同颜色棋子相连
        int count = 1;
        int color = allChess[x][y];
        //判断横向
        count = this.checkCount(1, 0, color);
        if (count >= 5) {
            flag = true;
        } else {
            //判断纵向
            count = this.checkCount(0, 1, color);
            if (count >= 5) {
                flag = true;
            } else {
                //判断右上左下
                count = this.checkCount(1, -1, color);
                if (count >= 5) {
                    flag = true;
                } else {
                    //判断右下左上
                    count = this.checkCount(1, 1, color);
                    if (count >= 5) {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    //判断棋子链接的数量
    private int checkCount(int xChange, int yChange, int color) {
        int count = 1;
        int tempX = xChange;
        int tempY = yChange;
        while (x + xChange >= 0 && x + xChange <= 18 && y + yChange >= 0 && y + yChange <= 18 &&
                color == allChess[x + xChange][y + yChange]) {
            count++;
            if (xChange != 0)
                xChange++;
            if (yChange != 0) {
                if (yChange > 0)
                    yChange++;
                else {
                    yChange--;
                }
            }
        }
        xChange = tempX;
        yChange = tempY;
        while (x - xChange >= 0 && x - xChange <= 18 && y - yChange >= 0 && y - yChange <= 18 && color == allChess[x - xChange][y - yChange]) {
            count++;
            if (xChange != 0)
                xChange++;
            if (yChange != 0) {
                if (yChange > 0)
                    yChange++;
                else {
                    yChange--;
                }
            }
        }

        return count;

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void run() {
        //判断是否有时间限制
        if (maxTime > 0) {
            while (true) {
                if (isBlack) {
                    blackTime--;
                    if (blackTime == 0) {
                        JOptionPane.showMessageDialog(this, "黑方超时！游戏结束！");
                    }
                } else {
                    whiteTime--;
                    if (whiteTime == 0) {
                        JOptionPane.showMessageDialog(this, "白方超时！游戏结束！");
                    }
                }
                blackMessage = blackTime / 3600 + ":"
                        + (blackTime / 60 - blackTime / 3600 * 60) + ":"
                        + (blackTime - blackTime / 60 * 60);
                whiteMessage = whiteTime / 3600 + ":"
                        + (whiteTime / 60 - whiteTime / 3600 * 60) + ":"
                        + (whiteTime - whiteTime / 60 * 60);
                this.repaint();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
