public class Block // to change blocks
{
    private boolean empty;
    private char id;

    public Block(char i)
    {
        empty = false; // everything is not empty at the beginning
        id = i; // set id to that (this will never change in our program
    }

    public void change() // changes the value of empty
    {
        empty = !empty;
    }

    public void setEmpty(boolean e) // sets empty to e
    {
        empty = e;
    }

    public boolean getEmpty() // get whether block is empty or not
    {
        return empty;
    }

    public char getId() // get the original char (i.e. Id)
    {
        return id;
    }
}
