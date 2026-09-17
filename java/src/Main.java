import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**This program allows you to create a gradient based on sinusoidal interpolation
 * of the three color channels. To create a gradient, you change transform the sine curve
 * by adjusting the horizontal and vertical translation, as well as the horizontal and
 * vertical scale (expansion or compression). By adjusting these 4 sliders for each
 * color channel RGB, you can create interesting gradients that would require
 * more than 2 colors with linear interpolation.
 * 
 * @author John
 * @date 26 April 2017
 */

public class Main extends JFrame implements ChangeListener
{
	/* The three tabs for R, G, and B that allows you to transform the curves
	 * for each of these three color channels
	 */
	public static JTabbedPane tabs;
	
	/* The SliderPane for the red channel */
	public static SliderPane red;
	
	/* The SliderPane for the green channel */
	public static SliderPane green;
	
	/* The SliderPane for the blue channel */
	public static SliderPane blue;
	
	/* The graph that plots the curves of each channel */
	public static Graph graph;
	
	/* Creates the window */
	public static void main(String[] args)
	{
		Main frame = new Main();
		frame.setVisible(true);
	}

	/* Constructor of the class, adds the graph, tabbed pane, and sets fonts */
	public Main()
	{
		Container contentPane = getContentPane();
		contentPane.setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Gradation");
		setSize(650, 428);
		setLocationRelativeTo(null);
		setResizable(false);
		
		graph = new Graph();
		graph.setSize(330, 380);
		graph.setLocation(12, 15);
		add(graph);
		
		red = new SliderPane(Color.red, this);
		green = new SliderPane(Color.green, this);
		blue = new SliderPane(Color.blue, this);
		
		tabs = new JTabbedPane();
		tabs.setSize(280, 359);
		tabs.setLocation(345, 25);
		tabs.setFocusable(false);
		tabs.addTab("   Red  ", red);
		tabs.addTab("Green", green);
		tabs.addTab(" Blue  ", blue);
		add(tabs);
		
		// set up fonts
		try
		{
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Font f = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("SFText-Medium.ttf"));
			ge.registerFont(f);
			f = f.deriveFont(Font.PLAIN, 13);
					
			graph.setFont(f);
			tabs.setFont(f);
		}
		catch (IOException | FontFormatException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	/* When a slider is changed, update the respective value */
	public void stateChanged(ChangeEvent e)
	{
		//the source slider
		JSlider source = (JSlider)e.getSource();
		
		//while it's moving
		if(source.getValueIsAdjusting())
		{
			//red sliders
			if(source.equals(red.horizTrans))
			{
				ColorFactory.transformRed(ColorFactory.HORIZONTAL_TRANSLATION, source.getValue() / 100.0);
				
				red.horizTransPrcnt.setText(source.getValue() + "%");
				graph.rValBox.setText(String.valueOf(Math.abs(ColorFactory.getRed(graph.cursorXLoc, 256) - 255)));
			}
			else if(source.equals(red.horizScale))
			{
				ColorFactory.transformRed(ColorFactory.HORIZONTAL_SCALE, source.getValue() / 100.0);
				
				red.horizScalePrcnt.setText(source.getValue() + "%");
				graph.rValBox.setText(String.valueOf(Math.abs(ColorFactory.getRed(graph.cursorXLoc, 256) - 255)));
			}
			else if(source.equals(red.vertiTrans))
			{
				ColorFactory.transformRed(ColorFactory.VERTICAL_TRANSLATION, source.getValue() / 100.0);
				
				red.vertiTransPrcnt.setText(source.getValue() + "%");
				graph.rValBox.setText(String.valueOf(Math.abs(ColorFactory.getRed(graph.cursorXLoc, 256) - 255)));
			}
			else if(source.equals(red.vertiScale))
			{
				ColorFactory.transformRed(ColorFactory.VERTICAL_SCALE, source.getValue() / 100.0);
				
				red.vertiScalePrcnt.setText(source.getValue() + "%");
				graph.rValBox.setText(String.valueOf(Math.abs(ColorFactory.getRed(graph.cursorXLoc, 256) - 255)));
			}
			
			//green sliders
			else if(source.equals(green.horizTrans))
			{
				ColorFactory.transformGreen(ColorFactory.HORIZONTAL_TRANSLATION, source.getValue() / 100.0);
				
				green.horizTransPrcnt.setText(source.getValue() + "%");
				graph.gValBox.setText(String.valueOf(Math.abs(ColorFactory.getGreen(graph.cursorXLoc, 256) - 255)));
			}
			else if(source.equals(green.horizScale))
			{
				ColorFactory.transformGreen(ColorFactory.HORIZONTAL_SCALE, source.getValue() / 100.0);
				
				green.horizScalePrcnt.setText(source.getValue() + "%");
				graph.gValBox.setText(String.valueOf(Math.abs(ColorFactory.getGreen(graph.cursorXLoc, 256) - 255)));
			}
			else if(source.equals(green.vertiTrans))
			{
				ColorFactory.transformGreen(ColorFactory.VERTICAL_TRANSLATION, source.getValue() / 100.0);
				
				green.vertiTransPrcnt.setText(source.getValue() + "%");
				graph.gValBox.setText(String.valueOf(Math.abs(ColorFactory.getGreen(graph.cursorXLoc, 256) - 255)));
			}
			else if(source.equals(green.vertiScale))
			{
				ColorFactory.transformGreen(ColorFactory.VERTICAL_SCALE, source.getValue() / 100.0);
				
				green.vertiScalePrcnt.setText(source.getValue() + "%");
				graph.gValBox.setText(String.valueOf(Math.abs(ColorFactory.getGreen(graph.cursorXLoc, 256) - 255)));
			}
			
			//blue sliders
			else if(source.equals(blue.horizTrans))
			{
				ColorFactory.transformBlue(ColorFactory.HORIZONTAL_TRANSLATION, source.getValue() / 100.0);
				
				blue.horizTransPrcnt.setText(source.getValue() + "%");
				graph.bValBox.setText(String.valueOf(Math.abs(ColorFactory.getBlue(graph.cursorXLoc, 256) - 255)));
			}
			else if(source.equals(blue.horizScale))
			{
				ColorFactory.transformBlue(ColorFactory.HORIZONTAL_SCALE, source.getValue() / 100.0);
				
				blue.horizScalePrcnt.setText(source.getValue() + "%");
				graph.bValBox.setText(String.valueOf(Math.abs(ColorFactory.getBlue(graph.cursorXLoc, 256) - 255)));
			}
			else if(source.equals(blue.vertiTrans))
			{
				ColorFactory.transformBlue(ColorFactory.VERTICAL_TRANSLATION, source.getValue() / 100.0);
				
				blue.vertiTransPrcnt.setText(source.getValue() + "%");
				graph.bValBox.setText(String.valueOf(Math.abs(ColorFactory.getBlue(graph.cursorXLoc, 256) - 255)));
			}
			else if(source.equals(blue.vertiScale))
			{
				ColorFactory.transformBlue(ColorFactory.VERTICAL_SCALE, source.getValue() / 100.0);
				
				blue.vertiScalePrcnt.setText(source.getValue() + "%");
				graph.bValBox.setText(String.valueOf(Math.abs(ColorFactory.getBlue(graph.cursorXLoc, 256) - 255)));
			}
		}
		
		//update graph
		repaint();
	}
}
