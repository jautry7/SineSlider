import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JSlider;

/**A custom implementation of the JSlider, which allows me to paint a color into the track
 * to the left of the knob. This closely resembles the actual slider appearance on Mac, 
 * but not exactly. The color filled in is based on which tabbed pane the slider is in
 * (which color the slider is for).
 * 
 * @author John
 * @date 26 April 2017
 * 
 */
public class CSlider extends JSlider
{
	/* The color for this slider */
	private Color c;
	
	/* Set the color, otherwise, normal construction */
	public CSlider(Color c)
	{
		super();
		this.c = c;
	}
	
	@Override
	/* In addition to painting the slider like normal, paint a colored bar from the left
	 * up to the knob, so it looks like slider is filling up */
	public void paintComponent(Graphics g)
	{
		//special colors because of rounded rectangle ends
		g.setColor(new Color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 0.4f));
		g.fillRect(14, 8, 1, 1);
		g.fillRect(14, 10, 1, 1);
		
		g.setColor(c);
		g.fillRect(14, 9, 1, 1);
		g.fillRect(15, 8, Double.valueOf((getValue() / 100.0) * (getWidth() - 26)).intValue(), 3);
		
		super.paintComponent(g);
	}
}
