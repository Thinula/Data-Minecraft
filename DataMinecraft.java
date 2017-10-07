import java.awt.*;
import java.io.*; // allows file access
import javax.imageio.*;
import java.awt.Image;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener

class DataMinecraft extends JFrame implements KeyListener
{   
    static int row = 18, col = 26, wid = 30, hei = 30, posX, posY;
    static Map map;  // create map, stores maps
    static Map [][] data;
    static Map mine; // create a mine map
    static Village village; // create a village map
    static Map nether;
    static Map desert;
    static Map plain;
    static Fort netherFortress;
    static Map ocean;
    static Map jungle;
    static Map strongHold;
    static Ender theEnd;

    static Timer t, destroy, wither, end, rip; 
    static int counter = 0; // decides the person's animation
    //======================================================== constructor
    public DataMinecraft ()
    {
        // creates all the map objects
        mine = new Map (row, col, wid, hei, "Mine"); // create a mine map
        village = new Village (row, col, wid, hei); // create a village map
        nether = new Map (row, col, wid, hei, "Nether");
        desert = new Map (row, col, wid, hei, "Desert");
        plain = new Map (row, col, wid, hei, "Plains");
        netherFortress = new Fort (row, col, wid, hei);
        ocean = new Map (row, col, wid, hei, "Ocean");
        jungle = new Map (row, col, wid, hei, "Jungle");
        strongHold = new Map (row, col, wid, hei, "Stronghold");
        theEnd = new Ender (row, col, wid, hei);

        // all the maps
        data = new Map[3][4];
        data[0][0] = mine;
        data[1][0] = village;
        data[2][0] = nether;
        data[0][1] = desert;
        data[1][1] = plain;
        data[2][1] = netherFortress;
        data[0][2] = ocean;
        data[1][2] = jungle;
        data[2][2] = strongHold;
        data[0][3] = null;
        data[1][3] = null;
        data[2][3] = theEnd;

        // first level is the mine
        map = data [1][0];

        // sets location
        map.setLoc(col/2,row/2);
        posX = 0;
        posY = 1;

        // 1... Enable key listener for movement
        addKeyListener (this);
        setFocusable (true);
        setFocusTraversalKeysEnabled (false);

        // 2... Create content pane, set layout
        JPanel content =  new JPanel ();        // Create a content pane

        DrawArea board =  new DrawArea (col*wid, row*hei); // Area for map to be displayed

        content.add (board); // map display area

        board.addKeyListener (this);

        Movement randomStuff =  new Movement (); // ActionListener for timer
        t =  new Timer (500, randomStuff); // set up Movement to run every 500 milliseconds
        t.start (); // start the timer

        // initializes the timers
        Attack baus = new Attack ();
        wither = new Timer (200, baus);

        AttackRip bois = new AttackRip ();
        end = new Timer (200, bois);

        Wither bossy =  new Wither (); // ActionListener for timer
        destroy =  new Timer (60000, bossy); // set up Movement to run every minute

        End die = new End();
        rip = new Timer(30000,die);

        addWindowListener(new WindowAdapter() 
            {
                public void windowClosing(WindowEvent e) 
                {
                    t.stop();
                    destroy.stop();
                    wither.stop();
                    end.stop();
                    rip.stop();
                }
            });

        // 4... Set this window's attributes.
        setContentPane (content);
        pack ();
        setTitle ("DataMinecraft");
        setSize (col*wid, (row+1)*hei);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo (null);           // Center window.      
    }

    public void keyTyped (KeyEvent e)
    {
        // nothing
    }

    public void keyReleased (KeyEvent e)
    {
        // nothing
    }

    public void keyPressed (KeyEvent e) // handle cursor keys and enter
    {
        int key = e.getKeyCode ();
        int x = map.getX(); 
        int y = map.getY(); 
        Block[][] power = map.getPower();
        //int temp = map.getHuman().getOrient();

        switch (key)
        {
            case KeyEvent.VK_W: case KeyEvent.VK_UP: // moves up
            if (y < 0 && map.getOrient() == 3 && !map.getHuman().getDead()) // switches between levels if conditions are met
            {
                Image [] hp = map.getHealthBar();
                int health = map.getHealth();
                int defense = map.getDefense();
                int attack = map.getAttack();
                if (posY > 0) // moves between maps
                {
                    posY --;
                    map = data[posY][posX];
                    power = map.getPower();
                    power [power.length - 1][x].setEmpty(true);
                    if (power [power.length - 1][x].getId() != ' ' && power [power.length - 1][x].getId() != 'P')
                        map.setMap (power.length - 1, x, ' ');
                    map.setLoc (x, power.length - 1);
                    if (posY == 2 && posX == 1) // start and stop timers when necessary
                    {
                        destroy.start ();
                        wither.start ();
                    }
                    else
                    {
                        destroy.stop();
                        wither.stop ();
                    }
                }
                // refreshes all the stats
                map.setAttack(attack);
                map.setDefense(defense);
                map.setHealth(health);
                map.setHealthBar(hp);
            }
            counter = (counter+1)%4; // cycles animation
            map.orient (3, counter); // checks orientation
            map.move (0, -1);
            break;

            case KeyEvent.VK_S: case KeyEvent.VK_DOWN: // moves down
            if (y > power.length - 3 && map.getOrient() == 0 && !map.getHuman().getDead()) // switches between levels if conditions are met
            {
                Image [] hp = map.getHealthBar();
                int health = map.getHealth();
                int defense = map.getDefense();
                int attack = map.getAttack();
                if (posY < 2)
                {
                    posY ++;
                    map = data[posY][posX];
                    power = map.getPower();
                    if (posY != 2)
                    {
                        power [0][x].setEmpty(true);
                        if (power [0][x].getId() != ' ' && power [0][x].getId() != 'P')
                            map.setMap (0, x, ' ');
                    }
                    map.setLoc (x,-2);
                    if (posY == 2 && posX == 1)
                    {
                        destroy.start ();
                        wither.start ();
                    }
                    else
                    {
                        destroy.stop();
                        wither.stop ();
                    }
                }
                map.setAttack(attack);
                map.setDefense(defense);
                map.setHealth(health);
                map.setHealthBar(hp);
            }
            counter = (counter+1)%4; // cycles animation
            map.orient (0, counter); // checks orientation
            map.move (0, 1); // moves player
            break;

            case KeyEvent.VK_A: case KeyEvent.VK_LEFT: // moves left
            if (x <= 0 && map.getOrient() == 2 && !map.getHuman().getDead()) // switches between levels if conditions are met
            {
                Image [] hp = map.getHealthBar();
                int health = map.getHealth();
                int defense = map.getDefense();
                int attack = map.getAttack();
                if (posX > 0)
                {
                    posX --;
                    map = data[posY][posX];
                    power = map.getPower();
                    power [y+1][power[0].length - 1].setEmpty(true);
                    if (power [y+1][power[0].length - 1].getId() != ' ' && power [y+1][power[0].length - 1].getId() != 'P')
                        map.setMap (y+1, power[0].length - 1, ' ');
                    map.setLoc (power[0].length, y);
                    if (posY == 2 && posX == 1)
                    {
                        destroy.start ();
                        wither.start ();
                    }
                    else
                    {
                        destroy.stop();
                        wither.stop ();
                    }
                }      
                map.setAttack(attack);
                map.setDefense(defense);
                map.setHealth(health);
                map.setHealthBar(hp);
            }
            counter = (counter+1)%4; // cycles animation
            map.orient (2, counter); // checks orientation
            map.move (-1, 0); // moves player
            break;

            case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: // moves right
            if (x > power[0].length - 2 && map.getOrient() == 1 && !map.getHuman().getDead()) // switches between levels if conditions are met
            {
                Image [] hp = map.getHealthBar();
                int health = map.getHealth();
                int defense = map.getDefense();
                int attack = map.getAttack();
                if (posX < 2 || (posX == 2 && posY == 2))
                {
                    posX ++;
                    map = data[posY][posX];
                    power = map.getPower();

                    if (posY == 2 && posX == 1)
                    {
                        rip.stop();
                        destroy.start ();
                        wither.start ();
                        map.setLoc (0, y);
                    }
                    else if (posY == 2 && posX == 3)
                    {
                        destroy.stop();
                        wither.stop ();
                        rip.start();
                        map.setLoc (0, y);
                    }
                    else
                    {
                        destroy.stop();
                        wither.stop();
                        rip.stop();
                        power [y+1][0].setEmpty(true);
                        if (power [y+1][0].getId() != ' ' && power [y+1][0].getId() != 'P')
                            map.setMap (y+1, 0, ' ');
                        map.setLoc (-1, y);
                    }
                }
                map.setAttack(attack);
                map.setDefense(defense);
                map.setHealth(health);
                map.setHealthBar(hp);
            }
            counter = (counter+1)%4; // cycles animation
            map.orient (1, counter); // checks orientation
            map.move (1, 0); // moves player
            break;

            case KeyEvent.VK_E: 
            map.checkBlock ('D'); // opens or closes a door
            map.checkBlock ('C'); // chests
            break;

            case KeyEvent.VK_SPACE: // attacks enemy
            if (posX == 1 && posY == 2 && !map.getHuman().getDead())
            {
                if (netherFortress.seeEnemy())
                    netherFortress.checkEnemy();
            }
            else if (posY == 3 && posY == 2 && !map.getHuman().getDead())
            {
                if (theEnd.seeEnemy())
                    theEnd.checkEnemy();
            }
            else if (!map.getHuman().getDead())
            {
                map.checkBlock ('S'); // removes a stone block
                for (int i = 0; i < map.getMonstersLength(); i ++)
                {
                    if (map.seeEnemy(i))
                    {
                        map.checkEnemy(i);
                        i = map.getMonstersLength();
                    }
                }
            }
            break;
        }

        repaint ();
    }

    class DrawArea extends JPanel
    {
        public DrawArea (int width, int height)
        {
            this.setPreferredSize (new Dimension (width, height)); // size
        }

        public void paintComponent (Graphics g)
        {
            map.print (g);
        }
    }

    class Movement implements ActionListener // executed according to timer delay
    {
        public void actionPerformed (ActionEvent event)
        {
            if (map.getHealth() < 100)
                map.changePlayerHealth (1);            

            for (int i = 0; i < map.getMonstersLength(); i++)
            {
                if (map.getHuman().getDead()) // if human is adead
                {
                    t.stop();
                    destroy.stop();
                    wither.stop();
                    end.stop();
                    rip.stop();
                    i = map.getMonstersLength();
                }
                else if (!map.getEnemyType(i).equals("Wither") || !map.getEnemyType(i).equals("End")) // if the monster is zombie
                {
                    map.moveEnemy(i); 
                    if (map.getDead(i)) // if monster is dead
                    {
                        map.addAttack(map.getAttack(i));
                        map.changePlayerHealth(map.getHealth(i));
                    }  
                    else if (map.seePerson(i) && map.getHuman().getDefense() <= -1*map.getAttack(i)) // attacks
                    {
                        map.changePlayerHealth (map.getAttack(i)+map.getHuman().getDefense());
                        i = map.getMonstersLength();                        
                    }                                  
                }               
            }
            repaint (); // refresh
        }
    }

    class Attack implements ActionListener // executed according to timer delay
    {
        public void actionPerformed (ActionEvent event)
        {     
            if (map.getHuman().getDead()) // if human is dead
            {
                // stop everything
                t.stop();
                destroy.stop();
                wither.stop();
                end.stop();
                rip.stop();
            }
            else if (netherFortress.getDead()) // if monster is dead
            {
                // stop everything and transport to village
                destroy.stop();
                wither.stop();
                map = village;
                map.setLoc(col/2,row/2);
                posX = 0;
                posY = 1;
            }
            else // attack
            {
                destroy.start();
                wither.start();
                //map.changePlayerHealth (netherFortress.getAttack());
                netherFortress.moveEnemy(); 
                if (netherFortress.seePerson() && map.getHuman().getDefense() <= -1*netherFortress.getAttack())
                    map.changePlayerHealth (netherFortress.getAttack()+map.getHuman().getDefense());     
            }
            repaint (); // refresh
        }
    }

    class AttackRip implements ActionListener // executed according to timer delay
    {
        public void actionPerformed (ActionEvent event)
        {        
            if (map.getHuman().getDead()) // if human is dead, stop everything
            {
                t.stop();
                destroy.stop();
                wither.stop();
                end.stop();
                rip.stop();
            }
            else // otherwise, attack
            {
                end.start();
                rip.start();
                theEnd.moveEnemy(); 
                if (theEnd.seePerson() && map.getHuman().getDefense() <= -1*theEnd.getAttack())
                    map.changePlayerHealth (theEnd.getAttack()+map.getHuman().getDefense());
            }
            repaint (); // refresh
        }
    }

    class Wither implements ActionListener // executed according to timer delay
    {
        public void actionPerformed (ActionEvent event)
        {
            if (map.getHuman().getDead()) // if human is dead, stop everything
            {
                t.stop();
                destroy.stop();
                wither.stop();
                end.stop();
                rip.stop();
            }
            else // addd more attack to nether
                netherFortress.getMonster().addAttack(-15);
        }
    }

    class End implements ActionListener // executed according to timer delay
    {
        public void actionPerformed (ActionEvent event)
        {
            if (map.getHuman().getDead()) // if human is dead, stop everything
            {
                t.stop();
                destroy.stop();
                wither.stop();
                end.stop();
                rip.stop();
            }
            else // add more attack to theEnd
                theEnd.getMonster().addAttack(-35);
        }
    }

    //======================================================== method main
    public static void main (String[] args)
    {
        DataMinecraft window =  new DataMinecraft ();
        window.setVisible (true);
    }
}
