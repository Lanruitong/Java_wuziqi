package org.liky.game.frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyChessFrame extends JFrame implements MouseListener {
    public MyChessFrame(){
        this.setTitle("五子棋");//设置标题
        this.setSize(1000,700);//设置窗体初始大小
        this.setResizable(false);//设置窗体是否可以改变大小
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置窗体关闭方式，关闭窗体同时结束程序
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        //System.out.println("gaoduwei:"+height);
        //System.out.println("kuanduwei:"+width);
        this.setLocation((width-200)/2,(height-100)/2);//设置窗体初始位置

        this.addMouseListener(this);//加入鼠标监听

        this.setVisible(true);
    }

    public void paint(Graphics g){//
        /*g.drawString("五子棋游戏",20,40);//绘制字符串*/
        /*g.drawOval(20,40,40,40);//画一个圆（空心），x,y的位置是圆的外界举行的左上角的位置*/
        /*g.fillOval(20,40,40,40);//绘制一个圆（实心）*/
        /*g.drawLine(20,40,80,40);//绘制一条直线*/
        /*g.fillRect(20,40,40,20);//绘制一个实心的矩形*/
        BufferedImage image = null;//读取磁盘中的一个图片存入image变量
        try {
            image = ImageIO.read(new File("D:/timg.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(image,0,0,this);
        g.setColor(Color.BLUE);
        g.fillRect(20,40,40,20);
        g.setFont(new Font("宋体",40,40));
        g.drawString("五子棋游戏",100,100);


    }
    @Override
    public void mouseClicked(MouseEvent e) {
       //监听鼠标点击事件
        //System.out.println("鼠标点击");
        //判断横向是否有5个棋子相连，纵坐标相同
        /*int i = 1;
        while (color == allChess[x + i][y]) {
            count++;
            i++;
        }
        i = 1;
        while (color == allChess[x - i][y]) {
            count++;
            i++;
        }
        if (count >= 5) {
            flag = true;
        }
        //纵向判断
        int i2 = 1;
        int count2 = 1;
        while (color == allChess[x][y + i2]) {
            count2++;
            i2++;
        }
        i2 = 1;
        while (color == allChess[x][y - i2]) {
            count2++;
            i2++;
        }
        if (count2 >= 5) {
            flag = true;
        }
        //斜向判断(右上左下)
        int i3 = 1;
        int count3 = 1;
        while (color == allChess[x + i3][y - i3]) {
            count3++;
            i3++;
        }
        i3 = 1;
        while (color == allChess[x - i3][y + i3]) {
            count3++;
            i3++;
        }
        if (count3 >= 5) {
            flag = true;
        }
        //斜向判断（右下左上）
        int i4 = 1;
        int count4 = 1;
        while (color == allChess[x + i4][y + i4]) {
            count4++;
            i4++;
        }
        i4 = 1;
        while (color == allChess[x - i4][y - i4]) {
            count4++;
            i4++;
        }
        if (count4 >= 5) {
            flag = true;
        }*/

    }

    @Override
    public void mousePressed(MouseEvent e) {
    //监听鼠标按下事件的操作
    //    System.out.println("鼠标按下");
        System.out.println("点击位置： X--> " +e.getX());//得到当前鼠标点击的横向位置坐标（相对于左上角）
        System.out.println("点击位置： Y--> "+e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    //监听鼠标抬起的事件
        System.out.println("鼠标抬起");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    //监听鼠标进入窗体的操作，鼠标一进入窗体久会触发该操作
/*
        System.out.println("鼠标进入");
        JOptionPane.showMessageDialog(this,"鼠标进入");
*/

    }

    @Override
    public void mouseExited(MouseEvent e) {
    //监听鼠标离开事件
/*        System.out.println("鼠标离开");
        JOptionPane.showMessageDialog(this,"鼠标离开");
 */   }
}
