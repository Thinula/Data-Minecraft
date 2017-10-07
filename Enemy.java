import java.awt.*;
import java.io.*; 
import javax.imageio.*;
import java.awt.Image;
import javax.swing.*;
import java.awt.event.*; 

public class Enemy
{
    private String type;
    private int health, attack, orient, posx, posy;
    private Image front; 
    private Image left; 
    private Image right; 
    private Image back; 
    private Image display;
    private char[][] map;

    public Enemy(String t, int w, int h, char[][] eh)
    {
        map = eh;
        type = t;
        posx = (int) (Math.random()*(map[0].length-10))+5;
        posy = (int) (Math.random()*(map.length-10))+5;
        if (type.equals ("Wither")) // sets position for wither
        {
            posx = map[0].length/2;
            posy = map.length/2;
        }
        if (t.equals("Zomby")) // sets the attack and health for zombies
        {
            attack = (int) (Math.random()*8-30);
            health = (int) (Math.random()*10+20);
            orient = 0;
        }
        // pictures
        front = reSize(w,h,pic (type + " Front.png"));
        left = reSize(w,h,pic (type + " Left.png"));
        right = reSize(w,h,pic (type + " Right.png"));
        back = reSize(w,h,pic (type + " Back.png"));
        display = front;
    }
    
    // below are a bunch of get and set methods to help run our program...

    public String getType ()
    {
        return type;
    }

    public boolean getDead()
    {
        return health <= 0;
    }

    public int getX()
    {
        return posx;
    }

    public int getY()
    {
        return posy;
    }

    public void addAttack(int a)
    {
        attack += a;
    }

    public void setAttack(int a)
    {
        attack = a;
    }

    public int getAttack()
    {
        return attack;
    }

    public void setPos(int x, int y)
    {
        posx = x;
        posy = y;
    }

    public Image getDisplay ()
    {
        return display;
    }

    public void addHealth (int h)
    {
        health += h;
    }

    public int getHealth ()
    {
        return health;
    }

    public void setHealth (int h)
    {
        health = h;
    }

    public void mapUpdate (char[][] eh)
    {
        map = eh;
    }

    private int absDiff(int a1, int a2) // returns absolute difference between a1 and a2
    {
        return a1 >= a2 ? a1 - a2: a2 - a1;
    }

    public int taxiDist(int x1, int y1, int x2, int y2) // taxi-cab Distance (sum of differences in x and y)
    {
        return absDiff(x1,x2)+absDiff(y1,y2);
    }

    public void move(int x, int y, int inc)
    {
        int minDist = map.length + map[0].length + 20; // 20 for buffer room
        int minNum = -1; // to determine the orientation
        boolean samePos = posx == x && posy == y; // making sure they don't move if they're on the human already
        int move = 0; // amount of spaces to move

        for (int i = 1; i <= inc; i++) // move to stop with minimum distance away
        {
            // check to the right
            if (checkMove (0, i) && taxiDist(posx, posy+i, x, y) < minDist && !samePos)
            {
                minDist = taxiDist(posx, posy+1, x, y); 
                minNum = 0;
                move = i;
            }
            // check up
            if (checkMove (i, 0) && taxiDist(posx+i, posy, x, y) < minDist && !samePos)
            {
                minDist = taxiDist(posx+1, posy, x, y);
                minNum = 1;
                move = i;
            }
            // check down
            if (checkMove (-i, 0) && taxiDist(posx-i, posy, x, y) < minDist && !samePos)
            {
                minDist = taxiDist(posx-1, posy, x, y);
                minNum = 2;
                move = i;
            }
            // check to the left
            if (checkMove (0, -i) && taxiDist(posx, posy-i, x, y) < minDist && !samePos)
            {
                minDist = taxiDist(posx, posy-1, x, y);
                minNum = 3;
                move = i;
            }
            // update all values if necessary
        }
        // change the orientation too
        orient = minNum; 
        dirChange(orient);

        // move accordingly
        if (minNum == 0)
            posy += move;
        else if (minNum == 1)
            posx += move;
        else if (minNum == 2)
            posx -= move;
        else if (minNum == 3)
            posy -= move; 
    }

    public boolean seePerson(int x, int y, int maxDist) // whether monster can see person or not
    {
        if (posx == x && posy == y) // if same stop, yes
            return true;
        else if (posy == y && ((posx < x && orient == 1) || (posx > x && orient == 2))) 
            return taxiDist(posx, posy, x, y) <= maxDist; // return if distance is at most maxDist
        else if (posx == x && ((posy < y && orient == 0) || (posy > y && orient == 3)))
            return taxiDist(posx, posy, x, y) <= maxDist; // return if distance is at most maxDist

        return false; // can't see person
    }

    public boolean checkMove (int dx, int dy) // checks if a move is value (moving x by dx and y by dy)
    {
        boolean check = true; // check is for whether move is valid or not
        // if position is in bounds
        if (posx + dx >= 0 && posy + dy + 2 > 0 && posx + dx < map[0].length && posy + dy + 1< map.length)
        { 
            for (int i = posy + dy + 1; i < posy + dy + 2; i ++)
            {
                for (int j = posx + dx; j < posx + dx + 1; j ++)
                {
                    if (map [i] [j] != ' ') // makes sure nothing is in the path
                        check = false; // change chekc accordingly
                }
            }
        }
        return check; // returns check
    }

    public void dirChange (int dir) // changes direction
    {
        if (dir == 3) // up
            display = back;
        else if (dir == 0) // down
            display = front;
        else if (dir == 2) // left
            display = left;
        else if (dir == 1) // right
            display = right;
    }

    public Image pic (String file)
    {
        Image img = null;
        try
        {
            img = ImageIO.read (new File (file));
        }
        catch (IOException e)
        {
        }
        return img;
    }

    public Image reSize (int width, int height, Image img) // resizes images
    {
        return img.getScaledInstance (width, height, Image.SCALE_SMOOTH);
    }
}
