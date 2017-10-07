import java.awt.*;
import java.util.ArrayList;
import java.io.*;
import javax.imageio.*;
import java.awt.Image;
import javax.swing.*;
import java.awt.event.*;

public class Fort extends Map // Fort is a Map
{
    public Fort (int rows, int cols, int blockWidth, int blockHeight)
    {
        super (rows, cols, blockWidth, blockHeight);
        
        // create the map
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols ; j++)
            {
                if (i == 0 || i == rows - 1 || j == 0 || j == cols - 1)
                    map[i][j] = 'W';
                else 
                    map[i][j] = ' ';
                power [i][j] = new Block (super.getChar(i,j));    
            }
        }
        
        // create the Wither Monster
        monster = new Enemy("Wither", blockWidth*5, blockHeight*5, map);        
        monster.setHealth(500);
        monster.setAttack(-95);
        monsters.add(monster);
             
        // make the images
        ground = pic (width, height, "Nether Fortress\\Cobble.png");
        stone = pic (width, height, "Nether Fortress\\Stone.png");
        wall = pic (width, height, "Nether Fortress\\Bedrock.png");
        chest = pic (width, height, "Nether Fortress\\Chest.png");
    }
    
    public void print (Graphics g) // over-riden print Method
    {
        super.print(g);
        
        if (monster.getY() <= posy && !monster.getDead())
        {
            g.drawImage (monster.getDisplay(),(monster.getX()-2)*width, (monster.getY()-3)*height , null);
            g.drawImage (human.getDisplay(), (posx-1)* width, (posy-1)* height, null);
        }
        else if (!monster.getDead())
        {
            g.drawImage (human.getDisplay(), (posx-1)* width, (posy-1)* height, null);
            g.drawImage (monster.getDisplay(),(monster.getX()-2)*width, (monster.getY()-3)*height , null); 
        }
        else if (!human.getDead())
            g.drawImage (human.getDisplay(), (posx-1)* width, (posy-1)* height, null);
    }
    
    public Block[][] getPower()
    {
        return power;
    }
    
    public boolean getDead()
    {
        return monster.getHealth() <= 0;
    }
    
    public Enemy getMonster()
    {
        return monster;
    }
    
    public void checkEnemy() // over-riden checkEnemy()
    {
        int check = human.getOrient();
        int eX = monster.getX();
        int eY = monster.getY();

        if (eY == posy && eX == posx)
            monster.addHealth (human.getAttack());
        else if (check == 0 && seePerson())
            monster.addHealth (human.getAttack());
        else if (check == 1 && seePerson())
            monster.addHealth (human.getAttack());
        else if (check == 2 && seePerson())
            monster.addHealth (human.getAttack());
        else if (check == 3 && seePerson())
            monster.addHealth (human.getAttack());
    }
    
    public boolean seeEnemy() // over-riden seeEnemy()
    {
        int x = monster.getX();
        int y = monster.getY();
        int orient = human.getOrient();
        if (posx == x && posy == y)
            return true;
        else if (posy == y && ((posx < x && orient == 1) || (posx > x && orient == 2))) 
            return monster.taxiDist(posx, posy, x, y) <= 1;
        else if (posx == x && ((posy < y && orient == 0) || (posy > y && orient == 3)))
            return monster.taxiDist(posx, posy, x, y) <= 1;

        return false;
    }
    
    public void moveEnemy() // over-riden moveEnemy()
    {
        monster.move(posx,posy, 5);
    }
    
    public boolean seePerson() // over-riden seePerson()
    {
        return monster.seePerson(posx, posy, 8);
    }
    
    public int getAttack() // over-riden getAttack()
    {
        return monster.getAttack();
    }  
}
