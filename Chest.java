
public class Chest
{
    private char item; // the item
    private int amount; // the amount you increase by
    
    public Chest()
    {
        int rand = (int)(Math.random()*3); // only 3 possible items in chests
        if (rand == 0) // health
        {
            item = 'H';
            amount = (int)(Math.random()*10+5);
        }
        else if (rand == 1) // armor
        {
            item = 'A';
            amount = (int)(Math.random()*10+5);
        }
        else // sword
        {
            item = 'S';
            amount = (int)(Math.random()*10+5);
        }
    }

    public char getItem() // gets the Item
    {
        return item;
    }
    
    public int getAmount() // gets the Amount
    {
        return amount;
    }
}
