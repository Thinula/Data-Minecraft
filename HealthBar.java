import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.Image;
import javax.swing.*;
import java.awt.event.*;

public class HealthBar
{
    private Image [] healthBar;
    private int health, width, height;
    private Image heart0, heart1, heart2;

    public HealthBar(int life, int w, int h)
    {
        width = w;
        height = h;

        // gets the images
        heart0 = pic (w, h, "Heart 0.png");
        heart1 = pic (w, h, "Heart 1.png");
        heart2 = pic (w, h, "Heart 2.png");

        // initializes health and hp
        health = life;
        int hp = health/10;
        if (health%10 != 0)
            hp ++;

        // makes the health bar and updates it
        healthBar = new Image [hp];
        updateBar();
    }

    public void setHealthBar (Image[] img) // sets health bar to image array
    {
        healthBar = img;
    }

    public Image [] getHealthBar () // gets health bar
    {
        return healthBar;
    }

    public void changeHealth (int h) // changes player's health then updates the health bar
    {
        health = h;
        updateBar();
    }

    public int getLength () // gets the length of the health bar
    {
        return healthBar.length;
    }

    public Image getHpImg (int i) // gets the HP Image at index i
    {
        return healthBar [i];
    }

    public void updateBar() // updates the health bar
    {
        for (int i = 0; i < healthBar.length; i++) // loop through all elemnts
        {
            // draws the image based on how much health you have
            if ((i+1)*10 <= health) 
                healthBar [i] = heart0;
            else if ((i+1)*10-5 <= health)
                healthBar [i] = heart1;
            else
                healthBar [i] = heart2;
        }
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

