
/**This class is a utlity class that allows colors to be computed in a gradient
 * based on position x. This class returns (separately) the red, green, and blue 
 * values based on position x. However, the values returned are the y position
 * in the graph, which is situated to top down. To get the actual color values, 
 * you must flip the axis using absolute value.
 * 
 * ColorFactory computes colors based on a transformed cosine curve, returning the 
 * color at position x in the cosine function. For each color channel, a separate
 * curve exists, with its own transformations (vert and horiz translation, vert
 * and horiz scale). Each of the four transformations are stored in an array.
 * When the slider is adjusted, the factors of transformation are adjusted,
 * adjusting the color at that position.
 * 
 * @author John
 * @date 26 April 2017
 * 
 */
public class ColorFactory
{
	/* These four ints are used in method calls to specify the transformation
	 * type. They also correspond to the index in the array of factors. */
	public static final int HORIZONTAL_TRANSLATION  = 0;
	public static final int HORIZONTAL_SCALE        = 1;
	public static final int VERTICAL_TRANSLATION    = 2;
	public static final int VERTICAL_SCALE          = 3;
	
	/* The four transformations of the red curve, indexes corresponding to above */
	public static Double[] redFactors   = {1.0, 0.5, 0.5, 1.0};
	
	/* The four transformations of the green curve, indexes corresponding to above */
	public static Double[] greenFactors = {0.83, 0.5, 0.5, 1.0};
	
	/* The four transformations of the blue curve, indexes corresponding to above */
	public static Double[] blueFactors  = {0.67, 0.5, 0.5, 1.0};


	/* Transform the red curve by setting the specified factor in the
	 * array to the passed in value */
	public static void transformRed(int transformType, double factor)
	{
		redFactors[transformType] = factor;
	}
	
	/* Transform the green curve by setting the specified factor in the
	 * array to the passed in value */
	public static void transformGreen(int transformType, double factor)
	{
		greenFactors[transformType] = factor;
	}
	
	/* Transform the blue curve by setting the specified factor in the
	 * array to the passed in value */
	public static void transformBlue(int transformType, double factor)
	{
		blueFactors[transformType] = factor;
	}
	
	/* Return the red value at position x in graph of certain width.
	 * NOTE: This returns the y position in the graph, not the actual
	 * color value. Since the graph is top down, you must flip the value to 
	 * get the actual color value (flip using abs value) */
	public static int getRed(int x, int width)
	{
		//since Math.cos is in radians, cancel that
        double radianFactor = 2 * Math.PI;
        
        //if hScale is 1 / 0 that's undefined and bad
        if(redFactors[1] == 0.0)
        {
        	redFactors[1] = 0.01;
        }
         
        double hTrans = (255 * 2) * redFactors[0];				//default 0.5
        double hScale = 1 / ((255 * 4) * redFactors[1]);		//default 0.5
        double vTrans = (255 * redFactors[2]);					//default 0.5
        double vScale = 127.5 * redFactors[3];					//default 1.0
        
        //the magic behind the whole program
        Double r = vScale * Math.cos( hScale * radianFactor * (x + hTrans) ) + vTrans;

        //bounds
        if(r > 255.0)
        	r = 255.0;
        if(r < 0.0)
        	r = 0.0;
        
		return r.intValue();
	}
	
	/* Return the green value at position x in graph of certain width.
	 * NOTE: This returns the y position in the graph, not the actual
	 * color value. Since the graph is top down, you must flip the value to 
	 * get the actual color value (flip using abs value) */
	public static int getGreen(int x, int width)
	{
		//since Math.cos is in radians, cancel that
        double radianFactor = 2 * Math.PI;
        
        //if hScale is 1 / 0 that's undefined and bad
        if(greenFactors[1] == 0.0)
        {
        	greenFactors[1] = 0.01;
        }
         
        double hTrans = (255 * 2) * greenFactors[0];				//default 0.5
        double hScale = 1 / ((255 * 4) * greenFactors[1]);			//default 0.5
        double vTrans = (255 * greenFactors[2]);					//default 0.5
        double vScale = 127.5 * greenFactors[3];					//default 1.0
         
        //the magic behind the whole program
        Double g = vScale * Math.cos( hScale * radianFactor * (x + hTrans) ) + vTrans;

        //bounds
        if(g > 255.0)
        	g = 255.0;
        if(g < 0.0)
        	g = 0.0;
        
		return g.intValue();
	}
	
	/* Return the blue value at position x in graph of certain width.
	 * NOTE: This returns the y position in the graph, not the actual
	 * color value. Since the graph is top down, you must flip the value to 
	 * get the actual color value (flip using abs value) */
	public static int getBlue(int x, int width)
	{
		//since Math.cos is in radians, cancel that
        double radianFactor = 2 * Math.PI;
        
        //if hScale is 1 / 0 that's undefined and bad
        if(blueFactors[1] == 0.0)
        {
        	blueFactors[1] = 0.01;
        }
         
        double hTrans = (255 * 2) * blueFactors[0];				//default 0.5
        double hScale = 1 / ((255 * 4) * blueFactors[1]);			//default 0.5
        double vTrans = (255 * blueFactors[2]);					//default 0.5
        double vScale = 127.5 * blueFactors[3];					//default 1.0
         
        //the magic behind the whole program
        Double b = vScale * Math.cos( hScale * radianFactor * (x + hTrans) ) + vTrans;

        //bounds
        if(b > 255.0)
        	b = 255.0;
        if(b < 0.0)
        	b = 0.0;
        
		return b.intValue();
	}
}
