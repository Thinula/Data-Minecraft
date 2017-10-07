import java.awt.*;
import javax.imageio.*;
import java.awt.Image;
import javax.swing.*;
import java.awt.event.*;

public class Village extends Map // a Village is a Map
{
    public Village (int rows, int cols, int blockWidth, int blockHeight)
    {
        super (rows, cols, blockWidth, blockHeight);

        // get the Images for the Village
        ground = pic (width, height, "Village\\Cobble.png");
        stone = pic (width, height, "Village\\Stone.png");
        wall = pic (width, height, "Village\\Bedrock.png");
        chest = pic (width, height, "Village\\Chest.png");

        door = pic (width, height, "Village\\Door.png");
        portal = pic (width, height, "Nether Portal.png");
        frame = pic (width, height, "Nether Portal Frame.png");


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

        // create the door, frame and portal
        map [rows - 1][cols / 2 + 1] = 'F';
        map [rows - 1][cols / 2] = 'P'; 
        map [rows - 1][cols / 2 - 1] = 'P';
        map [rows - 1][cols / 2 - 2] = 'F';

        map [0][cols / 2] = 'D'; 
        map [0][cols / 2 - 1] = 'D';

        map [rows / 2][cols - 1] = 'D'; 
        map [rows / 2 - 1][cols - 1] = 'D';

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols ; j++)
                power [i][j] = new Block (map [i][j]);    
        }
    }

    public void print (Graphics g) // prints everything
    {
        super.print(g);
        Image villager;
        g.drawImage (human.getDisplay(), (posx-1)*width, (posy-1)*height, null);
    }

    public void setLoc (int x, int y) // sets location of player
    {
        posx = x;
        posy = y;
    }

    public Block[][] getPower()
    {
        return power;
    }
}
