import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.*;

/**
 * This class constructs the graph that displays the curve of each color channel. The Spectrum
 * which displays the gradient itself is also contained in the graph, as well as text boxes that 
 * display the x location, the R value, G value, and B value.
 * 
 * This Graph is based on the Graph from the Gradation Project
 * 
 * @author John
 * @date 26 April 2017
 */

public class Graph extends JPanel implements MouseMotionListener
{

	/* The location of the cursor on the graph, used for the mouseover.
	 * Set at 0 initially (left color) */
	public int cursorXLoc = 0;
	
	
	/* The color spectrum displayed below the graph */
	public Spectrum spectrum;

	
	/* The "R" label for the red value text box */
	private final JLabel X_LABEL;

	/* The text box that displays the red value at the current cursor position */
	private JTextField xLocBox;

	
	/* The "R" label for the red value text box */
	private final JLabel R_LABEL;

	/* The text box that displays the red value at the current cursor position */
	public JTextField rValBox;

	/* The "R" label for the red value text box */
	private final JLabel G_LABEL;

	/* The text box that displays the green value at the current cursor position */
	public JTextField gValBox;

	/* The "R" label for the red value text box */
	private final JLabel B_LABEL;

	/* The text box that displays the blue value at the current cursor position */
	public JTextField bValBox;
	
	
	/* Used to color text */
	private final Color GRAY = new Color(0x8888888);
	
	/* Used to color text */
	private final Color DARK_GRAY = new Color(0x333333);

	
	/* Sets the type of graph, sets cosmetics, adds mouse motion listener */
	public Graph()
	{
		super();

		// cosmetic
		setOpaque(false);
		setLayout(null);
		
		//spectrum of colors
		spectrum = new Spectrum();
		spectrum.setSize(256, 25);
		spectrum.setLocation(50, 295);
		spectrum.setVisible(true);
		add(spectrum);
		
		X_LABEL = new JLabel("x");
		X_LABEL.setSize(15, 31);
		X_LABEL.setLocation(31, 331);
		X_LABEL.setForeground(GRAY);
		X_LABEL.setHorizontalAlignment(SwingConstants.RIGHT);
		add(X_LABEL);
		
		xLocBox = new JTextField(String.valueOf(0));
		xLocBox.setSize(48, 31);
		xLocBox.setLocation(47, 331);
		xLocBox.setVisible(true);
		xLocBox.setFocusable(false);
		xLocBox.setForeground(DARK_GRAY);
		xLocBox.setHorizontalAlignment(SwingConstants.CENTER);
		add(xLocBox);

		//display the values
		R_LABEL = new JLabel("R");
		R_LABEL.setSize(15, 31);
		R_LABEL.setLocation(111, 331);
		R_LABEL.setForeground(GRAY);
		R_LABEL.setHorizontalAlignment(SwingConstants.RIGHT);
		add(R_LABEL);
		
		rValBox = new JTextField(String.valueOf(ColorFactory.getRed(0, 256)));
		rValBox.setSize(48, 31);
		rValBox.setLocation(126, 331);
		rValBox.setVisible(true);
		rValBox.setFocusable(false);
		rValBox.setForeground(DARK_GRAY);
		rValBox.setHorizontalAlignment(SwingConstants.CENTER);
		add(rValBox);

		G_LABEL = new JLabel("G");
		G_LABEL.setSize(15, 31);
		G_LABEL.setLocation(179, 331);
		G_LABEL.setForeground(GRAY);
		G_LABEL.setHorizontalAlignment(SwingConstants.RIGHT);
		add(G_LABEL);

		gValBox = new JTextField(String.valueOf(ColorFactory.getGreen(0, 256)));
		gValBox.setSize(48, 31);
		gValBox.setLocation(194, 331);
		gValBox.setVisible(true);
		gValBox.setFocusable(false);
		gValBox.setForeground(DARK_GRAY);
		gValBox.setHorizontalAlignment(SwingConstants.CENTER);
		add(gValBox);

		B_LABEL = new JLabel("B");
		B_LABEL.setSize(15, 31);
		B_LABEL.setLocation(246, 331);
		B_LABEL.setForeground(GRAY);
		B_LABEL.setHorizontalAlignment(SwingConstants.RIGHT);
		add(B_LABEL);

		bValBox = new JTextField(String.valueOf(ColorFactory.getBlue(0, 256)));
		bValBox.setSize(48, 31);
		bValBox.setLocation(261, 331);
		bValBox.setVisible(true);
		bValBox.setFocusable(false);
		bValBox.setForeground(DARK_GRAY);
		bValBox.setHorizontalAlignment(SwingConstants.CENTER);
		add(bValBox);
		
		// set up fonts
		try
		{
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Font f = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("SFText-Medium.ttf"));
			ge.registerFont(f);
			f = f.deriveFont(Font.PLAIN, 13);
					
			X_LABEL.setFont(f);
			xLocBox.setFont(f);
			
			R_LABEL.setFont(f);
			rValBox.setFont(f);

			G_LABEL.setFont(f);
			gValBox.setFont(f);

			B_LABEL.setFont(f);
			bValBox.setFont(f);
		}
		catch (IOException | FontFormatException e)
		{
			e.printStackTrace();
		}

		// seems redundant, but very important (mouseover wont work w/o it)
		this.addMouseMotionListener(this);
	}

	@Override
	/* Paint the component - calls a bunch of methods that have their own explanations */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// cast g to g2 so anti-aliasing can be enabled for text and shapes
		Graphics2D g2 = (Graphics2D) g;
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHints(qualityHints);

		// draw axes and labels
		drawBase(g2);
		
		int width = 256;
		
		// plot colors on graph at each x value on graph
		for (int x = 0; x < width; x++)
		{
			drawPoint(g2, x, width);
		}
		
		// draw the mouseover cursor - called each time component is painted, with new XLoc
		drawCursor(g2, cursorXLoc);
	}
	
	/* Draw the base of the graph including white background, vertical color lines, tick marks along
	 * vertical axes, and axes labels */
	private void drawBase(Graphics2D g2)
	{
		// base white
		g2.setColor(Color.white);
		g2.fillRect(50, 24, 256, 256);

		g2.setColor(new Color(0xdddddd));

		// cross lines
		for (int y = 0; y < 8; y++)
		{
			// left side
			g2.fillRect(50, 24 + (y * 32), 256, 1);
		}

		g2.setColor(DARK_GRAY);

		// tick marks
		for (int y = 0; y < 8; y++)
		{
			// left side
			g2.fillRect(48, 24 + (y * 32), 5, 1);
		}

		// y scale, int
		g2.drawString("255", 17, 33);
		g2.drawString("127", 20, 157);
		g2.drawString("0", 33, 280);


		g2.setColor(GRAY);

		// y scale, int in between
		g2.drawString("63", 25, 221);
		g2.drawString("191", 21, 93);
	}

	/* Draw a point of each color channel at position x */
	private void drawPoint(Graphics2D g2, int x, int width)
	{
		int y;
		
		g2.setColor(Color.RED);
		y = ColorFactory.getRed(x, width);
		g2.fillOval(x + 50, y + 24, 2, 2);
		
		g2.setColor(Color.GREEN);
		y = ColorFactory.getGreen(x, width);
		g2.fillOval(x + 50, y + 24, 2, 2);
		
		g2.setColor(Color.BLUE);
		y = ColorFactory.getBlue(x, width);
		g2.fillOval(x + 50, y + 24, 2, 2);
	}

	/* Draws a vertical line that follows the cursor as it moves along the graph. Includes little
	 * dots that trace each color */
	private void drawCursor(Graphics2D g2, int x)
	{
		// straight line
		g2.setColor(DARK_GRAY);
		g2.fillRect(x + 49, 24, 2, 256);

		int y;
		
		// draw dot borders
		g2.setColor(DARK_GRAY);

		y = ColorFactory.getRed(x, 256);
		g2.fillOval(x + 45, y + 19, 10, 10);

		y = ColorFactory.getGreen(x, 256);
		g2.fillOval(x + 45, y + 19, 10, 10);

		y = ColorFactory.getBlue(x, 256);
		g2.fillOval(x + 45, y + 19, 10, 10);

		// draw dot centers (color)
		g2.setColor(Color.RED);
		y = ColorFactory.getRed(x, 256);
		g2.fillOval(x + 47, y + 21, 6, 6);

		g2.setColor(Color.GREEN);
		y = ColorFactory.getGreen(x, 256);
		g2.fillOval(x + 47, y + 21, 6, 6);

		g2.setColor(Color.BLUE);
		y = ColorFactory.getBlue(x, 256);
		g2.fillOval(x + 47, y + 21, 6, 6);
	}
	
	@Override
	/* Tracks when mouse is dragged across graph, updates cursor position and repaints */
	public void mouseDragged(MouseEvent e)
	{
		cursorAction(e);
	}

	@Override
	/* Tracks when mouse moves across graph, updates cursor position and repaints */
	public void mouseMoved(MouseEvent e)
	{
		cursorAction(e);
	}

	/* All the code that dictates the action of the cursor in one method, so that this
	 * behavior can be put in both mouseMoved and mouseDragged */
	private void cursorAction(MouseEvent e)
	{
		// graph starts at position 50, subtract that out
		cursorXLoc = e.getX() - 50;

		// bounds
		if (cursorXLoc > 255)
			cursorXLoc = 255;
		if (cursorXLoc < 0)
			cursorXLoc = 0;
		
		int width = 256;

		// three values to go in text boxes
		int r = Math.abs(ColorFactory.getRed(cursorXLoc, width) - 255);
		int g = Math.abs(ColorFactory.getGreen(cursorXLoc, width) - 255);
		int b = Math.abs(ColorFactory.getBlue(cursorXLoc, width) - 255);

		rValBox.setText(String.valueOf(r));
		gValBox.setText(String.valueOf(g));
		bValBox.setText(String.valueOf(b));
		
		xLocBox.setText(String.valueOf(cursorXLoc));
		
		repaint();
	}
}
