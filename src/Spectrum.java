import java.awt.Color;
import java.awt.Graphics;

import javax.swing.*;

/**
 * This class is a modified JPanel that displays a color gradient (spectrum). The gradient
 * is determined by the width, where each position x along the width determines how the
 * color is computed using the ColorFactory class.
 * 
 * This Spectrum is based on the Spectrum from the Gradation Project
 * 
 * @author John
 * @date 26 April 2017
 */

public class Spectrum extends JPanel
{

	@Override
	/* Paint 1 pixel wide vertical stripes of color over x length */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// only 256 width tested, height arbitrary
		int width = 256;
		int height = 25;

		// iterate over width, each pixel
		for (int x = 0; x < width; x++)
		{
			g.setColor(getColorAt(x, width));
			g.fillRect(x, 0, 1, height);
		}
	}
	
	/* Get cosine value of Red, Green, and Blue, return as color */
	private Color getColorAt(int x, int width)
	{
		//the ColorFactory class returns the y positon 0-255 from top down
		//yet the graph displays 0-255 bottom up
		//so flip the values using abs value
		
        int r = Math.abs(ColorFactory.getRed(x, 256) - 255);
        int g = Math.abs(ColorFactory.getGreen(x, 256) - 255);
        int b = Math.abs(ColorFactory.getBlue(x, 256) - 255);

		return new Color(r, g, b);
	}
}
