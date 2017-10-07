import java.util.ArrayList;
import java.io.*;
import javax.imageio.*;
import java.awt.Image;

public class Player
{
    private int health, orient, attack, defense;
    private Image[][] move;
    private Image display;
    private HealthBar hp;

    public Player(String name, int w, int h)
    {
        // initializes data fields
        health = 100; 
        hp = new HealthBar (health, w, h);
        attack = -1;
        defense = 0;
        move = new Image[4][3]; // stores in space so heap space is not wrecked
        orient = 0;

        // gets all the movement images (for animation purposes)
        move[0][0] = pic (w*3, h*3, name + "\\Front.png");
        move[0][1] = pic (w*3, h*3, name + "\\Front Move 1.png");
        move[0][2] = pic (w*3, h*3, name + "\\Front Move 2.png");
        move[1][0] = pic (w*3, h*3, name + "\\Right.png");
        move[1][1] = pic (w*3, h*3, name + "\\Right Move 1.png");
        move[1][2] = pic (w*3, h*3, name + "\\Right Move 2.png");
        move[2][0] = pic (w*3, h*3, name + "\\Left.png");
        move[2][1] = pic (w*3, h*3, name + "\\Left Move 1.png");
        move[2][2] = pic (w*3, h*3, name + "\\Left Move 2.png");
        move[3][0] = pic (w*3, h*3, name + "\\Back.png");
        move[3][1] = pic (w*3, h*3, name + "\\Back Move 1.png");
        move[3][2] = pic (w*3, h*3, name + "\\Back Move 2.png");

        display = move [0][0];
    }
    
    // here are a whole bunch of get and set Methods that help our program...    
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
    
    public int getDefense()
    {
        return defense;
    }
    
    public boolean getDead()
    {
        return health <= 0;
    }
    
    public void setHealthBar (Image[] img)
    {
        hp.setHealthBar (img);
    }

    public Image [] getHealthBar ()
    {
        return hp.getHealthBar ();
    }

    public int getOrient ()
    {
        return orient;
    }

    public void setOrient (int o)
    {
        orient = o;
    }
    
    public void setDefense(int d)
    {
        defense = d;
    }
    
    public void addDefense(int d)
    {
        defense += d;
    }

    public void setDisplay (int dir, int or)
    {
        display = move [dir] [or];
    }

    public Image getDisplay ()
    {
        return display;
    }

    public int getHealth() // do we need this method...?
    {
        return health;
    }

    public void changeHealth(int h) // do we need this method...?
    {
        health += h;
    }
    
    public void setHealth(int h)
    {
        health = h;
    }

    public Image[][] getMove()
    {
        return move;
    }

    public void addHealth(int h)
    {
        health += h;
        hp.changeHealth (health);
    }

    public int getHpLength ()
    {
        return hp.getLength();
    }

    public Image getHpImg (int i)
    {
        return hp.getHpImg(i);
    }

    public Image pic (int width, int height, String file)
    {
        Image img = null;
        try
        {
            img = ImageIO.read (new File (file));
        }
        catch (IOException e)
        {
        }

        return img.getScaledInstance (width, height, Image.SCALE_SMOOTH);
    }

    public Image reSize (int width, int height, Image img) // resizes images
    {
        return img.getScaledInstance (width, height, Image.SCALE_SMOOTH);
    }
}
