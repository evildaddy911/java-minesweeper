// The "MineSweeper" class.
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class MineSweeper extends Applet implements MouseListener
{
    boolean[] [] clicked; // has square been clicked?
    int numMines; // now many mines
    Dimension[] mine; // where each mine is located
    int[] [] minesNear; // how many mines are nearby
    int numClicked; // how many squares have been clicked (for checking if won)
    BufferedImage bufferI; // buffer image
    Graphics bufferG; // buffer graphics
    boolean dead = false; // if player has hit a mine
    boolean win = false; // if the player has won
    int X = 20, Y = 20; // how many square horisontal and vertically

    public void init ()
    {
	numClicked = 0;
	numMines = 30;
	mine = new Dimension [numMines];
	for (int i = 0 ; i < numMines ; i++)
	{
	    // make sure each mine's location is unique
	    boolean done;
	    done = false;
	    while (!done)
	    {
		done = true;
		mine [i] = new Dimension ((int) (Math.round (Math.random () * X)), (int) (Math.round (Math.random () * Y)));

		for (int j = 0 ; j < i ; j++)
		{
		    if (mine [i].width == mine [j].width && mine [i].height == mine [j].height)
			done = false;
		}
	    }
	}
	minesNear = new int [X] [Y];
	clicked = new boolean [X] [Y];
	for (int x = 0 ; x < X ; x++)
	{
	    for (int y = 0 ; y < Y ; y++)
	    {
		clicked [x] [y] = false;
		minesNear [x] [y] = 0;
		for (int i = 0 ; i < numMines ; i++)
		{
		    if (mine [i].width == x && mine [i].height == y)
		    {
			minesNear [x] [y] = -1; // -1 means that that square is a mine
			break;
		    }
		    else if (Math.pow (mine [i].width - x, 2) + Math.pow (mine [i].height - y, 2) < 3.9)
		    { // if that mine is less than 2 squares away (this allows for mines in a diagonal direction)
			minesNear [x] [y]++;
		    }
		}
	    }
	}

	this.setSize (X * 30, Y * 30); // sets size
	// set up double buffer
	bufferI = new BufferedImage (this.getWidth (), this.getHeight (), BufferedImage.TYPE_3BYTE_BGR);
	bufferG = bufferI.getGraphics ();
	this.addMouseListener (this); // attaches the MouseListener to the applet
    } // init method


    public void update (Graphics g)  // required for double-buffering
    {
	paint (g);
    }


    public void paint (Graphics g)
    {
	bufferG.setColor (Color.white); // clear screen
	bufferG.fillRect (0, 0, this.getWidth (), this.getHeight ());
	if (dead)
	{
	    bufferG.setColor (Color.red);
	    for (int i = 0 ; i < numMines ; i++)
	    {
		bufferG.fillOval (mine [i].width * 30 + 5, mine [i].height * 30 + 5, 20, 20);
	    }
	    bufferG.setColor (Color.black);
	    bufferG.drawString ("Game Over!", 10, 20);
	}
	else if (win)
	{
	    bufferG.setColor (Color.green);
	    for (int i = 0 ; i < numMines ; i++)
	    {
		bufferG.fillOval (mine [i].width * 30 + 5, mine [i].height * 30 + 5, 20, 20);
	    }
	    bufferG.setColor (Color.black);
	    bufferG.drawString ("You Win!", 10, 20);
	}
	else
	{
	    for (int x = 0 ; x < X ; x++)
	    {
		for (int y = 0 ; y < Y ; y++)
		{
		    if (clicked [x] [y])
		    {
			bufferG.setColor (Color.black);
			bufferG.drawString (Integer.toString (minesNear [x] [y]), x * 30 + 10, y * 30 + 15); // shows how many mines are nearby
		    }
		    else
		    {
			bufferG.setColor (Color.gray);
			bufferG.fillRect (x * 30, y * 30, 30, 30); // draws a blank grey box
		    }
		}
	    }
	}
	bufferG.setColor (Color.black);
	for (int x = 0 ; x < X ; x++)
	{
	    for (int y = 0 ; y < Y ; y++)
	    {
		bufferG.drawRect (x * 30, y * 30, 30, 30); // draws the outlines of each box
	    }
	}
	g.drawImage (bufferI, 0, 0, null); // draws the image to the screen
    } // paint method


    public void click (int x, int y)
    {

	if (minesNear [x] [y] == -1)
	{ // if player has clicked a mine
	    dead = true;
	    return;
	}
	if (clicked [x] [y])
	{ // exits if this square has already been checked
	    return;
	}
	else
	{
	    clicked [x] [y] = true;
	    numClicked++;
	    if (numClicked >= X * Y - numMines)
	    { // checks if player has won yet
		win = true;
		return;
	    }
	}

	if (minesNear [x] [y] == 0)
	{ // uses recursion to click on each nearby square
	    if (x > 0)
	    {
		click (x - 1, y);
	    }
	    if (y > 0)
	    {
		click (x, y - 1);
	    }
	    if (x < X - 1)
	    {
		click (x + 1, y);
	    }
	    if (y < Y - 1)
	    {
		click (x, y + 1);
	    }
	}
    }


    public void mousePressed (MouseEvent e)
    {
	if (e.getX () <= 0 || e.getX () >= this.getWidth () || e.getY () <= 0 || e.getY () >= this.getHeight ())
	{ // ensures that click was inside the window
	    return;
	}

	click ((int) (e.getX () / 30), (int) (e.getY () / 30));
	repaint ();
    }


    public void mouseReleased (MouseEvent e)
    {
    }


    public void mouseClicked (MouseEvent e)
    {
    }


    public void mouseEntered (MouseEvent e)
    {
    }


    public void mouseExited (MouseEvent e)
    {
    }
} // MineSweeper class


