import java.awt.*;
import java.util.ArrayList;
import java.io.*; // allows file access
import javax.imageio.*;
import java.awt.Image;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener

public class Map
{
    protected Block[][] power;
    protected char[][] map;
    protected int width, height;
    protected int posx, posy;
    protected Image ground, wall, door, chest, stone, portal, frame;
    protected Player human;
    protected ArrayList<Enemy> monsters;
    protected Enemy monster; 
    private ArrayList<Integer> chestX, chestY;

    public Map (int rows, int cols, int blockWidth, int blockHeight) // constructor 
    {   
        width = blockWidth;
        height = blockHeight;
        power = new Block [rows][cols];
        map =  new char [rows] [cols]; // define 2-d array size
        human = new Player("Steve", width, height);
        monsters = new ArrayList<Enemy>(0);
    }

    public Map (int rows, int cols, int blockWidth, int blockHeight, String name) // another constructor
    {
        this (rows, cols, blockWidth, blockHeight);

        chestX = new ArrayList<Integer> (0);
        chestY = new ArrayList<Integer> (0);

        int rand = (int) (Math.random()*2+2);

        for (int i = 0; i < rand; i ++) // adds zombies
        {
            monster = new Enemy("Zomby", blockWidth*3, blockHeight*3, map);
            monsters.add(monster);
        }

        // gets Images
        ground = pic (width, height, name + "\\Cobble.png");
        stone = pic (width, height, name + "\\Stone.png");
        wall = pic (width, height, name + "\\Bedrock.png");
        chest = pic (width, height, name + "\\Chest.png");
        // adds Special Images
        if (name.equals ("Nether"))
        {
            door = pic (width, height, name + "\\Door.png");
            portal = pic (width, height, "Nether Portal.png");
            frame = pic (width, height, "Nether Portal Frame.png");
        }
        if (name.equals ("Stronghold"))
        {
            portal = pic (width, height, "End Portal.png");
            frame = pic (width, height, "End Portal Frame.png");
        }

        int randme = (int)(Math.random()*4);  
        monster = monsters.get(0);
        Enemy mons = monsters.get(1);
        int count = 0;
        int maxChest = (int)(Math.random()*10+2);

        // draws the map
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols ; j++)
            {
                if (monster.getY() - 1 <= i && i <= monster.getY() + 1 && monster.getX() - 1 <= j && j <= monster.getX() + 1)
                    map[i][j] = ' ';
                else if ((i >= monster.getY() - 2 && i <= monster.getY()+2) && (j >= monster.getX() - 2 && j <= monster.getX() + 2))
                {    
                    map[i][j] = 'W';
                    if (randme == 0 && i == monster.getY() + 2 && j == monster.getX())
                        map[i][j] = 'D';
                    if (randme == 1 && i == monster.getY() && j == monster.getX() + 2)
                        map[i][j] = 'D';
                    if (randme == 2 && i == monster.getY() - 2 && j == monster.getX())
                        map[i][j] = 'D';                
                    if (randme == 3 && i == monster.getY() && j == monster.getX() - 2)
                        map[i][j] = 'D';
                }
                if (mons.getY() - 1 <= i && i <= mons.getY() + 1 && mons.getX() - 1 <= j && j <= mons.getX() + 1)
                    map[i][j] = ' ';
                else if (i == 0 || i == rows - 1 || j == 0 || j == cols - 1) // border
                    map [i][j] = 'W'; // put up a wall
                else // creates chests up to the maxChest count
                {
                    int chests = (int) (Math.random()*100); // max 2 chests
                    if (chests < 10 && count < maxChest && i > 1 && i < rows -2 && j > 1 && j < cols - 2)
                    {
                        map[i][j] = 'C';
                        chestX.add(j);
                        chestY.add(i);
                        count ++;
                    }
                    else if (name.equals ("Mine"))
                        map [i][j] = 'S'; 
                    else 
                        map [i][j] = ' ';
                }
                power [i][j] = new Block (map [i][j]);    
            }
        }
        
        // makes the appropriate doors
        if (name.equals("Plains") || name.equals("Jungle") || name.equals("Stronghold")) // up passage
        {
            map [0][cols / 2] = ' '; 
            map [0][cols / 2 - 1] = ' ';     
        }
        if (name.equals("Mine") || name.equals("Desert") || name.equals("Ocean") || name.equals("Jungle")) // down passage
        {
            map [rows - 1][cols / 2] = ' '; 
            map [rows - 1][cols / 2 - 1] = ' ';
        }
        if (name.equals("Nether") || name.equals("Desert") || name.equals("Plains")) // right passage 
        {
            map [rows / 2][cols - 1] = ' '; 
            map [rows / 2 - 1][cols - 1] = ' ';
        }
        if (name.equals("Plains") || name.equals("Jungle") || name.equals("Ocean") || name.equals("Nether Fortress")) // left passage
        {
            map [rows/2][0] = ' '; 
            map [rows/2 - 1][0] = ' ';
        }

        if (name.equals ("Nether"))
        {
            map [0][cols / 2 + 1] = 'F';
            map [0][cols / 2] = 'P'; 
            map [0][cols / 2 - 1] = 'P';
            map [0][cols / 2 - 2] = 'F';
        }

        if (name.equals ("Stronghold"))
        {
            map [rows/2 - 2][cols - 1] = 'F';
            map [rows/2 - 1][cols - 1] = 'P'; 
            map [rows/2 ][cols - 1] = 'P';
            map [rows/2 + 1][cols - 1] = 'F';
        }

        if (name.equals("Mine"))
        {
            map [rows - 2][cols / 2] = ' '; 
            map [rows - 2][cols / 2 - 1] = ' ';
        }

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols ; j++)
                power [i][j] = new Block (map [i][j]);    
        }

        posx = cols / 2 - 1;
        posy = rows - 3;
    }

    public void print (Graphics g)    // displays the map on the screen
    {
        Image img = null;
        for (int row = 0 ; row < map.length ; row++) // number of rows
        {
            for (int col = 0 ; col < map [0].length ; col++) // length of first row
            {
                if (map [row] [col] == 'W') // wall
                    img = wall;
                else if (map [row] [col] == 'D') // door
                    img = door;
                else if (map [row] [col] == 'T') // treasure
                    img = chest;
                else if (map [row] [col] == 'S') // stone
                    img = stone;
                else if (map [row] [col] == ' ') // space will erase what was there
                    img = ground;
                else if (map [row] [col] == 'P') // portal
                    img = portal;
                else if (map [row] [col] == 'F') // frame for portal
                    img = frame;
                else if (map[row][col] == 'C') // chest
                    img = chest;
                g.drawImage (img, col*width, row*height, null); // draw image
            }
        }

        posYSort(); // sort monsters

        for (int i = 0; i < monsters.size(); i++) // draw each monster
        {
            monster = monsters.get(i);
            if (monster.getY() <  posy && !monster.getDead() && !monster.getType().equals ("Wither") && !monster.getType().equals ("End"))
                g.drawImage (monster.getDisplay(), (monster.getX()-1)*width, (monster.getY()-1)*height , null); // draw monster
        }

        if (!human.getDead()) // draw human
        {
            g.drawImage (human.getDisplay(), (posx-1)* width, (posy-1)* height, null);
        }

        for (int i = 0; i < monsters.size(); i++) // draw each monster again
        {
            monster = monsters.get(i);                 
            if (monster.getY() >= posy && !monster.getDead() && !monster.getType().equals ("Wither") && !monster.getType().equals ("End"))   
                g.drawImage (monster.getDisplay(), (monster.getX()-1)*width, (monster.getY()-1)*height , null);
        }
        drawBar(g);
    }

    public void posYSort() // sorts the monster by their y positions (for showing them)
    {
        // Selection Sort Algorithm
        int i, j, minIndex;
        Enemy temp;
        int n = monsters.size();
        for (i = 0; i < n - 1; i++) 
        {
            minIndex = i;
            for (j = i + 1; j < n; j++)
            {
                if (monsters.get(j).getY() < monsters.get(minIndex).getY())
                    minIndex = j;
            }
            if (minIndex != i) 
            {
                temp = monsters.get(i);
                monsters.set(i,monsters.get(minIndex));
                monsters.set(minIndex,temp);
            }
        }
    }

    public void move (int dx, int dy) // moves human by a value (moving x by dx and y by dy)
    {
        if (!human.getDead()) // if human isn't dead
        {
            boolean check = true; // check is for whether move is valid or not
            // if position is in bounds
            if (posx + dx >= 0 && posy + dy + 2 > 0 && posx + dx < map[0].length && posy + dy + 1 < map.length)
            {
                for (int row = posy + dy + 1; row < posy + dy + 2; row ++)
                {
                    for (int col = posx + dx; col < posx + dx + 1; col ++)
                    {
                        if (map [row] [col] != ' ' && map [row] [col] != 'P') // makes sure nothing is in the pat
                            check = false; // change chekc accordingly
                    }
                }

                if (check) // if possible to move, move him
                {
                    posx += dx;
                    posy += dy;
                }
            }   
        }
    }

    public String getEnemyType (int i)
    {
        return monsters.get(i).getType();
    }

    public void checkEnemy(int i) // attacks monsters.get(i)
    {
        // making sure orientation and positions are valid for attack
        int check = human.getOrient();
        monster = monsters.get(i);
        int eX = monster.getX();
        int eY = monster.getY();

        // attack if possible
        if (eY == posy && eX == posx)
            monster.addHealth (human.getAttack());
        else if (check == 0 && eY == posy + 1)
            monster.addHealth (human.getAttack());
        else if (check == 1 && eX == posx + 1)
            monster.addHealth (human.getAttack());
        else if (check == 2 && eX == posx - 1)
            monster.addHealth (human.getAttack());
        else if (check == 3 && eY == posy - 1)
            monster.addHealth (human.getAttack());
        if (monster.getHealth() == 0) // remove monster if his health is 0
            monsters.remove(i);
    }

    public void orient (int dir, int type) // orients and animates player
    {
        if (!human.getDead())// as long as human isn't dead, orient him accordingly
        {
            // set the image to the oriented version
            if (type < 2)
                human.setDisplay (dir, type);
            else if (type == 2)
                human.setDisplay (dir, 0);
            else if (type == 3)
                human.setDisplay (dir, type - 1);
            human.setOrient (dir);
        }
    }

    public void changeBlock(int y, int x, char block) // checks for blocks to remove
    {
        if (power[y][x].getId() == block)
        {
            if (block == 'D') // if it's a door
            {
                power[y][x].change();
                if (power[y][x].getEmpty()) // open the door
                    map[y][x] = ' ';
                else // close the door
                    map[y][x] = 'D';
            }
            else if (block =='C') // if it's a chest
            {
                System.out.println("You found treasure!");
                map[y][x] = ' ';

                for (int i = 0; i <chestX.size();i++)
                {
                    if (map [chestY.get(i)][chestX.get(i)] == ' ') // if this is the one you just opened
                    {
                        // remove it
                        chestX.remove(i);
                        chestY.remove(i);
                        // make a new Chest and get the item and amount
                        Chest chest = new Chest();
                        char item = chest.getItem();
                        int amount = chest.getAmount();
                        // reward the player accordingly
                        if (item == 'H')
                            human.changeHealth(amount);
                        else if (item == 'S')
                            human.addAttack (-1*amount);
                        else if (item == 'A')
                            human.addDefense (amount);
                        // show new stats
                        System.out.println ("Attack: " + -1*human.getAttack());
                        System.out.println ("Defense: " + human.getDefense());
                        System.out.println ("Health: " + human.getHealth());
                        System.out.println();
                    }
                }
            }
            else
                map[y][x] = ' ';
        }
    }

    public void checkBlock (char block) // checks for blocks to remove (for doors and chests)
    {
        // considers the orientation and makes sure positons are in the bounds
        if (posx - 1 >= 0 && human.getOrient() == 2)
            changeBlock(posy+1,posx-1,block);
        if (posx + 1 < map[0].length && human.getOrient() == 1)
            changeBlock(posy+1,posx+1,block);
        if (posy >= 0 && human.getOrient() == 3)
            changeBlock(posy,posx,block);
        if (posy + 2 < map.length && human.getOrient() == 0)
            changeBlock(posy+2,posx,block);
    }
    
    // below are a bunch of get and set methods to help run our program...

    public void setHealthBar (Image[] img)
    {
        human.setHealthBar (img);
    }

    public Image [] getHealthBar ()
    {
        return human.getHealthBar ();
    }

    public void drawBar (Graphics g)
    {
        for (int i = 0; i < human.getHpLength(); i++)
            g.drawImage (getHpImg(i), (i+1)*width, (map.length - 1)*height, null);
    }

    public Image getHpImg(int i)
    {
        return human.getHpImg (i);
    }

    public Player getHuman () // retrieves player object
    {
        return human;
    }

    public Enemy getEnemy()
    {
        return monster;
    }

    public int getX()
    {
        return posx;
    }

    public int getY()
    {
        return posy;
    }

    public boolean getDead (int i)
    {
        return monsters.get(i).getDead();
    }

    public void changePlayerHealth (int i)
    {
        human.addHealth (i);
    }

    public int getHealth()
    {
        return human.getHealth();
    }

    public void changeHealth(int h)
    {
        human.changeHealth(h);
    }

    public void setHealth(int h)
    {
        human.setHealth(h);
    }

    public void addAttack(int a)
    {
        human.addAttack(a);
    }
    
    public int getHealth(int i)
    {
        return monsters.get(i).getHealth();
    }

    public void addDefense(int d)
    {
        human.addDefense(d);
    }

    public int getOrient() // retrieves orientation
    {
        return human.getOrient();
    }

    public Block[][] getPower() // retrieves the block 2d array
    {
        return power;
    }

    public char [][] getMap ()
    {
        return map;
    }

    public char getChar(int y, int x)
    {
        return map[y][x];
    }

    public void setMap (int y, int x, char c) // assigns certain values to a certain indice
    {
        map [y][x] = c;
    }

    public int getMonstersLength()
    {
        return monsters.size();
    }

    public boolean seeEnemy(int i)
    {
        int x = monsters.get(i).getX();
        int y = monsters.get(i).getY();
        int orient = human.getOrient();
        if (posx == x && posy == y)
            return true;
        else if (posy == y && ((posx < x && orient == 1) || (posx > x && orient == 2))) 
            return monsters.get(i).taxiDist(posx, posy, x, y) <= 1;
        else if (posx == x && ((posy < y && orient == 0) || (posy > y && orient == 3)))
            return monsters.get(i).taxiDist(posx, posy, x, y) <= 1;

        return false;
    }

    public boolean seePerson(int i)
    {
        return monsters.get(i).seePerson(posx,posy,1);
    }

    public int getDefense()
    {
        return human.getDefense();
    }

    public int getAttack()
    {
        return human.getAttack();
    }

    public void setAttack(int a)
    {
        human.setAttack(a);
    }

    public void setDefense(int d)
    {
        human.setDefense(d);
    }

    public int getAttack(int i)
    {
        return monsters.get(i).getAttack();
    }

    public void setLoc (int x, int y) // sets location of player
    {
        posx = x;
        posy = y;
    }

    public void moveEnemy (int i)
    {
        monsters.get(i).move(posx, posy, 1);
    }

    public Image pic (int width, int height, String file) // reads in images
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
