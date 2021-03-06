package ca.rk.mappalinguarum.ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ca.rk.mappalinguarum.exceptions.IllegalPolygonException;
import ca.rk.mappalinguarum.model.Language;
import ca.rk.mappalinguarum.model.Location;
import ca.rk.mappalinguarum.ui.interfaces.IObservable;
import ca.rk.mappalinguarum.ui.interfaces.IObserver;
import ca.rk.mappalinguarum.util.Colour;
import ca.rk.mappalinguarum.util.RandomColourGenerator;
import ca.rk.mappalinguarum.util.textures.TexturePattern;


/**
 * represents a set of polygonal areas (>= 1) on the map that serves as a visual indicator of a
 * language's geographical distribution
 * 
 * @author RK
 *
 */
public class LanguagePolygon implements IObserver, IObservable {

	private Map map;
	private Location encapsulatedLocation;
	private Colour colour;
	private Colour familyDerivedColour;
	private List<Polygon> polygons;
	private TexturePattern texture;
	private double[] latitudes;
	private double[] longitudes;
	private ArrayList<Integer> xPoints;
	private ArrayList<Integer> yPoints;
	private List<IObserver> observers;
	private boolean isHighlighted;
	private List<Polygon> copiesOnScreen;
	
	/**
	 * constructs a LanguagePolygon; remembers the Map container and takes the Location input
	 * object as the basis for building its polygon
	 * 
	 * @param m the Map object that contains the LanguagePolygon
	 * @param loc the Location object to wrap around
	 */
	public LanguagePolygon(Map m, Location loc) {
		map = m;
		encapsulatedLocation = loc;
		colour = encapsulatedLocation.getColour();
		familyDerivedColour = encapsulatedLocation.getLanguage().getUrFamily().getColour();
		familyDerivedColour = RandomColourGenerator.getInstance().mixColours(familyDerivedColour, colour);
		isHighlighted = false;
		observers = new ArrayList<IObserver>();
		copiesOnScreen = new ArrayList<Polygon>();
		
		update();
		if (polygons == null || polygons.isEmpty()) {
			return;
		}
		texture = new TexturePattern(map, colour, familyDerivedColour);
		addObserver(texture);
	}
	
	/**
	 * construct list of polygons from Location information and map zoom level
	 * return null if the polygon has fewer than three vertices within the viewport
	 * 
	 * @param l the Location object to use
	 * @param zoomLevel the current zoom level in the map viewer
	 * @return one or more Polygons, empty list if there's nothing to draw
	 * @throws IllegalPolygonException if an illegal polygon is detected
	 */
	private List<Polygon> constructPolygons(Location l, int zoomLevel) throws IllegalPolygonException {
		List<Location.LatLongSet> latlongSets = l.getLatLongSets();
		List<Polygon> polys = new ArrayList<Polygon>(latlongSets.size());
		for (Location.LatLongSet latlong : latlongSets) {
			latitudes = latlong.getLatitudes();
			longitudes = latlong.getLongitudes();
			if (xPoints == null) {
				xPoints = new ArrayList<Integer>(longitudes.length);
			}
			if (yPoints == null) {
				yPoints = new ArrayList<Integer>(latitudes.length);
			}
			
			xPoints.clear();
			yPoints.clear();
			
			Point point;
			for (int i = 0; i < latitudes.length; ++i) {
				//force the map viewer to return a non-null Point even if it's outside of the viewport
				point = map.getMapPosition(latitudes[i], longitudes[i], false);
				
				xPoints.add(point.x);
				yPoints.add(point.y);
			}
			
			//unequal number of x and y points means something is wrong
			if ( xPoints.size() != yPoints.size() ) {
				throw new IllegalPolygonException();
			}
			
			//a polygon with fewer than three vertices makes no sense
			if (xPoints.size() < 3) {
				return null;
			}
			
			//convert ArrayLists to primitive arrays, since Polygon only accepts those
			int[] xArray = new int[ xPoints.size() ];
			int[] yArray = new int[ yPoints.size() ];
			Iterator<Integer> xIterator = xPoints.iterator();
			Iterator<Integer> yIterator = yPoints.iterator();
			
			for (int i = 0; i < xArray.length; ++i) {
				xArray[i] = xIterator.next();
				yArray[i] = yIterator.next();
			}
			
			polys.add(new Polygon(xArray, yArray, xArray.length));
		}
		
		return polys;
	}
	
	/**
	 * whether the input point is within the polygons
	 * 
	 * @param p the input point
	 * @return true if any polygon from this.polygons contains p, otherwise false
	 */
	public boolean contains(Point p) {
		//null polygon contains nothing
		if (polygons == null || polygons.isEmpty()) {
			return false;
		}

		for (Polygon poly : polygons) {
			if (poly.contains(p)) {
				return true;
			}
		}
		
		for (Polygon poly : copiesOnScreen) {
			if (poly.contains(p)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void update() {
		try {
			polygons = constructPolygons( encapsulatedLocation, map.getZoom() );
			if (polygons == null) {
				return;
			}
			for (Polygon poly : polygons) {
				poly.invalidate();
			}
		}
		catch(IllegalPolygonException ipe) {
			TextConsole.writeLine("There was an error calculating one of the language areas.");
		}
	}

	/**
	 * generated by Eclipse
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((encapsulatedLocation == null) ? 0 : encapsulatedLocation.hashCode());
		return result;
	}

	/**
	 * generated by Eclipse
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof LanguagePolygon)) {
			return false;
		}
		LanguagePolygon other = (LanguagePolygon) obj;
		if (encapsulatedLocation == null) {
			if (other.encapsulatedLocation != null) {
				return false;
			}
		}
		else if (!encapsulatedLocation.equals(other.encapsulatedLocation)) {
			return false;
		}
		return true;
	}

	@Override
	public void addObserver(IObserver obs) {
		observers.add(obs);
	}

	@Override
	public void removeObserver(IObserver obs) {
		observers.remove(obs);
	}

	@Override
	public void notifyObservers() {
		for (IObserver observer : observers) {
			observer.update();
		}
	}

	/**
	 * gets a Color object depending on the ViewMode and whether the LanguagePolygon is highlighted;
	 * in family mode, return the family-derived colour as a Color; otherwise, return own colour;
	 * if highlighted in either case, return a lightened shade of the underlying colour
	 * 
	 * @return a Color object
	 */
	public Color getColor() {
		if (map.getViewMode() == ViewMode.FAMILIES) {
			if (isHighlighted) {
				return Colour.lightenColour(familyDerivedColour).toColor();
			}
			return familyDerivedColour.toColor();
		}
		if (isHighlighted) {
			Color c = Colour.lightenColour(colour).toColor();
			return c;
		}
		return colour.toColor();
	}
	
	/**
	 * deep-copy a Polygon
	 * 
	 * @param poly original polygon
	 * @return a copy
	 */
	public static Polygon copy(Polygon poly) {
		Polygon copy = new Polygon(poly.xpoints, poly.ypoints, poly.npoints);
		return copy;
	}
	
	//accessors
	public Location getEncapsulatedLocation() { return encapsulatedLocation; }
	public Language getEncapsulatedLanguage() { return encapsulatedLocation.getLanguage(); }
	public List<Polygon> getPolygons() {
		if (polygons == null)
		{
			update();
		}
		return polygons;
	}
	public TexturePattern getTexture() { return texture; }
	public boolean getIsHighlighted() { return isHighlighted; }
	
	public LanguagePolygon setIsHighlighted(boolean b) {
		if (b != isHighlighted) {
			notifyObservers();
		}
		isHighlighted = b;
		return this;
	}
	public List<Polygon> getPolygonCopiesOnScreen() { return copiesOnScreen; }
}
