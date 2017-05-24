import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.ChangeListener;

/**This class is a simple JPanel of 4 sliders, with labels and value counters for each.
 * The four sliders are: Horizontal Translation, Horizontal Scale, Vertical Translation,
 * and Vertical Scale. An instance of this class is created for each color, and placed
 * into each of the three tabs in the tabbed pane, allowing each color channel to be adjusted
 * individually.
 * 
 * Moving the slider updates the respective transformation factor in ColorFactory by pulling
 * the value of the slider. Then the graph and spectrum is updated.
 * 
 * @author John
 * @date 26 April 2017
 * 
 */
public class SliderPane extends JPanel
{
	/* Used to color text */
	private final Color GRAY = new Color(0x888888);
	
	/* Used to color text */
	private final Color DARK_GRAY = new Color(0x333333);
	
	/*These four labels identify each slider by name */
	private JLabel horizTransLabel;
	private JLabel horizScaleLabel;
	private JLabel vertiTransLabel;
	private JLabel vertiScaleLabel;
	
	/* These four labels display the location of the knob on the slider in
	 * percent. Updates as the slider is adjusted. */
	public JLabel horizTransPrcnt;
	public JLabel horizScalePrcnt;
	public JLabel vertiTransPrcnt;
	public JLabel vertiScalePrcnt;
	
	/* The sliders themselves */
	public CSlider horizTrans;
	public CSlider horizScale;
	public CSlider vertiTrans;
	public CSlider vertiScale;
	
	
	/* Constructs the panel using the specified color (red, green, or blue), as well
	 * as passing in the change listener that will be attached to each slider so
	 * the program update the spectrum and graph when the slider is adjusted. */
	public SliderPane(Color color, ChangeListener listener)
	{
		setOpaque(false);
		setLayout(null);
		
		horizTransLabel = new JLabel("Horizontal Translation");
		horizTransLabel.setSize(140, 20);
		horizTransLabel.setLocation(18, 14);
		horizTransLabel.setForeground(DARK_GRAY);
		add(horizTransLabel);
		
		horizTransPrcnt = new JLabel();
		horizTransPrcnt.setSize(35, 20);
		horizTransPrcnt.setLocation(204, 14);
		horizTransPrcnt.setForeground(GRAY);
		horizTransPrcnt.setHorizontalAlignment(SwingConstants.RIGHT);
		horizTransPrcnt.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
		add(horizTransPrcnt);
		
		horizTrans = new CSlider(color);
		horizTrans.setSize(248, 20);
		horizTrans.setLocation(5, 39);
		horizTrans.setFocusable(false);
		horizTrans.addChangeListener(listener);
		add(horizTrans);
		
		horizScaleLabel = new JLabel("Horizontal Scale");
		horizScaleLabel.setSize(140, 20);
		horizScaleLabel.setLocation(18, 92);
		horizScaleLabel.setForeground(DARK_GRAY);
		add(horizScaleLabel);
		
		horizScalePrcnt = new JLabel();
		horizScalePrcnt.setSize(35, 20);
		horizScalePrcnt.setLocation(204, 92);
		horizScalePrcnt.setForeground(GRAY);
		horizScalePrcnt.setHorizontalAlignment(SwingConstants.RIGHT);
		horizScalePrcnt.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
		add(horizScalePrcnt);
		
		horizScale = new CSlider(color);
		horizScale.setSize(248, 20);
		horizScale.setLocation(5, 117);
		horizScale.setFocusable(false);
		horizScale.addChangeListener(listener);
		add(horizScale);
		
		vertiTransLabel = new JLabel("Vertical Translation");
		vertiTransLabel.setSize(140, 20);
		vertiTransLabel.setLocation(18, 170);
		vertiTransLabel.setForeground(DARK_GRAY);
		add(vertiTransLabel);
		
		vertiTransPrcnt = new JLabel();
		vertiTransPrcnt.setSize(35, 20);
		vertiTransPrcnt.setLocation(204, 170);
		vertiTransPrcnt.setForeground(GRAY);
		vertiTransPrcnt.setHorizontalAlignment(SwingConstants.RIGHT);
		vertiTransPrcnt.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
		add(vertiTransPrcnt);
		
		vertiTrans = new CSlider(color);
		vertiTrans.setSize(248, 20);
		vertiTrans.setLocation(5, 195);
		vertiTrans.setFocusable(false);
		vertiTrans.addChangeListener(listener);
		add(vertiTrans);
		
		vertiScaleLabel = new JLabel("Vertical Scale");
		vertiScaleLabel.setSize(140, 20);
		vertiScaleLabel.setLocation(18, 248);
		vertiScaleLabel.setForeground(DARK_GRAY);
		add(vertiScaleLabel);
		
		vertiScalePrcnt = new JLabel();
		vertiScalePrcnt.setSize(35, 20);
		vertiScalePrcnt.setLocation(204, 248);
		vertiScalePrcnt.setForeground(GRAY);
		vertiScalePrcnt.setHorizontalAlignment(SwingConstants.RIGHT);
		vertiScalePrcnt.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
		add(vertiScalePrcnt);
		
		vertiScale = new CSlider(color);
		vertiScale.setSize(248, 20);
		vertiScale.setLocation(5, 273);
		vertiScale.setFocusable(false);
		vertiScale.addChangeListener(listener);
		add(vertiScale);
		
		//if its the red panel, set default text and slider value based on redFactors
		if(color.equals(Color.red))
		{
			horizTransPrcnt.setText(Double.valueOf(ColorFactory.redFactors[0] * 100).intValue() + "%");
			horizScalePrcnt.setText(Double.valueOf(ColorFactory.redFactors[1] * 100).intValue() + "%");
			vertiTransPrcnt.setText(Double.valueOf(ColorFactory.redFactors[2] * 100).intValue() + "%");
			vertiScalePrcnt.setText(Double.valueOf(ColorFactory.redFactors[3] * 100).intValue() + "%");
			horizTrans.setValue(Double.valueOf(ColorFactory.redFactors[0] * 100).intValue());
			horizScale.setValue(Double.valueOf(ColorFactory.redFactors[1] * 100).intValue());
			vertiTrans.setValue(Double.valueOf(ColorFactory.redFactors[2] * 100).intValue());
			vertiScale.setValue(Double.valueOf(ColorFactory.redFactors[3] * 100).intValue());
		}
		
		//if its the green panel, set default text and slider value based on greenFactors
		else if(color.equals(Color.green))
		{
			horizTransPrcnt.setText(Double.valueOf(ColorFactory.greenFactors[0] * 100).intValue() + "%");
			horizScalePrcnt.setText(Double.valueOf(ColorFactory.greenFactors[1] * 100).intValue() + "%");
			vertiTransPrcnt.setText(Double.valueOf(ColorFactory.greenFactors[2] * 100).intValue() + "%");
			vertiScalePrcnt.setText(Double.valueOf(ColorFactory.greenFactors[3] * 100).intValue() + "%");
			horizTrans.setValue(Double.valueOf(ColorFactory.greenFactors[0] * 100).intValue());
			horizScale.setValue(Double.valueOf(ColorFactory.greenFactors[1] * 100).intValue());
			vertiTrans.setValue(Double.valueOf(ColorFactory.greenFactors[2] * 100).intValue());
			vertiScale.setValue(Double.valueOf(ColorFactory.greenFactors[3] * 100).intValue());
		}
		
		//if its the blue panel, set default text and slider value based on blueFactors
		else if(color.equals(Color.blue))
		{
			horizTransPrcnt.setText(Double.valueOf(ColorFactory.blueFactors[0] * 100).intValue() + "%");
			horizScalePrcnt.setText(Double.valueOf(ColorFactory.blueFactors[1] * 100).intValue() + "%");
			vertiTransPrcnt.setText(Double.valueOf(ColorFactory.blueFactors[2] * 100).intValue() + "%");
			vertiScalePrcnt.setText(Double.valueOf(ColorFactory.blueFactors[3] * 100).intValue() + "%");
			horizTrans.setValue(Double.valueOf(ColorFactory.blueFactors[0] * 100).intValue());
			horizScale.setValue(Double.valueOf(ColorFactory.blueFactors[1] * 100).intValue());
			vertiTrans.setValue(Double.valueOf(ColorFactory.blueFactors[2] * 100).intValue());
			vertiScale.setValue(Double.valueOf(ColorFactory.blueFactors[3] * 100).intValue());
		}

		// set up fonts
		try
		{
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Font f = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("SFText-Medium.ttf"));
			ge.registerFont(f);
			f = f.deriveFont(Font.PLAIN, 13);
					
			horizTransLabel.setFont(f);
			horizTransPrcnt.setFont(f);
			
			horizScaleLabel.setFont(f);
			horizScalePrcnt.setFont(f);
			
			vertiTransLabel.setFont(f);
			vertiTransPrcnt.setFont(f);
			
			vertiScaleLabel.setFont(f);
			vertiScalePrcnt.setFont(f);
		}
		catch (IOException | FontFormatException e)
		{
			e.printStackTrace();
		}
	}
}
