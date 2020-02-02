package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import de.fhpotsdam.unfolding.providers.Microsoft;
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Microsoft.AerialProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);


	    for (PointFeature eq : earthquakes) {
	    	Marker currEq = createMarker(eq);
	    	markers.add(currEq);
		}

	    // Add the markers to the map so that they are displayed
	    map.addMarkers(markers);
	}


	private SimplePointMarker createMarker(PointFeature feature)
	{
		int blue = color(0, 0, 255);
		int yellow = color(255, 255, 0);
		int red = color(255, 0,0);
		float small = (float) 5;
		float medium = (float) 10;
		float large = (float) 15;

		// To print all of the features in a PointFeature (so you can see what they are)
		// uncomment the line below.  Note this will only print if you call createMarker
		// from setup
		//System.out.println(feature.getProperties());

		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());

		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());


		if (mag < THRESHOLD_LIGHT) {
			marker.setColor(blue);
			marker.setRadius(small);
		}
		else if (mag >= THRESHOLD_LIGHT && mag < THRESHOLD_MODERATE) {
			marker.setColor(yellow);
			marker.setRadius(medium);
		}
		else {
			marker.setColor(red);
			marker.setRadius(large);
		}
	    return marker;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{
		fill(255,255,255);
		rect(25, 50, 150, 250);
		fill(0, 0, 0);
		textSize(15);
		textAlign(CENTER, BOTTOM);
		text("Earthquake Key", 100, 75);
		textSize(12);
		textAlign(LEFT, CENTER);
		text("5.0+ Magnitude", 70, 120);
		text("4.0+ Magnitude", 70, 175);
		text("Below 4.0", 70, 230);
		fill(225, 0, 0);
		ellipse(50, 120, 15, 15);
		fill(225, 225, 0);
		ellipse(50, 175, 10, 10);
		fill(0, 0, 225);
		ellipse(50, 230, 5, 5);
	}
}
