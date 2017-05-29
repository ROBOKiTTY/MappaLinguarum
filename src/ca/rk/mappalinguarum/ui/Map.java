package ca.rk.mappalinguarum.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.xml.parsers.ParserConfigurationException;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.OsmFileCacheTileLoader;
import org.openstreetmap.gui.jmapviewer.Tile;

import ca.rk.mappalinguarum.exceptions.InvalidXMLException;
import ca.rk.mappalinguarum.exceptions.MapInitializationFailureException;
import ca.rk.mappalinguarum.model.Feature;
import ca.rk.mappalinguarum.model.Language;
import ca.rk.mappalinguarum.model.LanguageFamily;
import ca.rk.mappalinguarum.model.Location;
import ca.rk.mappalinguarum.model.MapData;
import ca.rk.mappalinguarum.ui.interfaces.IObservable;
import ca.rk.mappalinguarum.ui.interfaces.IObserver;
import ca.rk.mappalinguarum.util.textures.TexturePattern;


/**
 * the map layer of the application using OpenStreetMap API and JMapViewer
 * Map is an observed subject of every LanguagePolygon
 * 
 * @author RK
 *
 */

public class Map extends JMapViewer implements IObservable {
	/**
	 * uid for serialization
	 */
	private static final long serialVersionUID = 1;
	
	protected final ViewMode DEFAULT_VIEW_MODE = ViewMode.MOSAIC;
	protected final SelectionMode DEFAULT_SELECTION_MODE = SelectionMode.ONE_OF;
	protected final static double DEFAULT_LATITUDE = 54.0;
	protected final static double DEFAULT_LONGITUDE = -130.0;
	protected final static double MIN_LATITUDE = -85.05112878;
	protected final static double MAX_LATITUDE = -MIN_LATITUDE;
	protected final static double MIN_LONGITUDE = -180;
	protected final static double MAX_LONGITUDE = 180;
	protected final static int DEFAULT_ZOOM = 4;

	private ViewMode viewMode;
	private SelectionMode selectionMode;
	private List<IObserver> observers;
	private List<LanguagePolygon> langPolygons;

	private List<LanguagePolygon> selectedPolygons;
	private List<LanguageFamily> criteriaFamilies;
	private List<Feature> criteriaFeatures;
	private List<Location> criteriaLocations;
	
	private LanguagePolygon mouseoveredLP;
	private MapData data;
	private ControlPanel controlPanel;
	
	private boolean isParseFailed = false;
	/**
	 * if true, use simple colours; if false, use textures
	 */
	private boolean simpleRender = true;
	
	/**
	 * constructs a Map and initializes settings; loads MapData;
	 * handles ParserConfigurationException / InvalidXMLException / IOException from the parser
	 * 
	 * @param cp the ControlPanel object to associate with this
	 */
	public Map(ControlPanel cp) {
		viewMode = DEFAULT_VIEW_MODE;
		selectionMode = DEFAULT_SELECTION_MODE;
		controlPanel = cp;
		controlPanel.setMap(this);
		observers = new ArrayList<IObserver>();
		new MapTroller(this);
		ToolTipManager.sharedInstance().registerComponent(this);
		setBorder(BorderFactory.createEtchedBorder());
		try {
			setTileLoader( new OsmFileCacheTileLoader(this) );
		}
		catch (SecurityException e) {
			TextConsole.writeLine("Failure to access system property for security reasons. Please check "
					+ "error logs.");
			e.printStackTrace();
			throw new MapInitializationFailureException(e);
		}
		catch (IOException e) {
			TextConsole.writeLine("Encountered an I/O error accessing filesystem.");
			e.printStackTrace();
			throw new MapInitializationFailureException(e);
		}
		
		try {
			data = new MapData().getParsedData();
			List<Location> locs = data.getLocations();
			
			for (Location l : locs) {
				addObserver(new LanguagePolygon(this, l));
			}
			
			langPolygons = new ArrayList<LanguagePolygon>();
			for (IObserver obs : observers) {
				langPolygons.add( (LanguagePolygon) obs);
			}

			selectedPolygons = new ArrayList<LanguagePolygon>( langPolygons.size() );
			criteriaFamilies = new ArrayList<LanguageFamily>();
			criteriaFeatures = new ArrayList<Feature>();
			criteriaLocations = new ArrayList<Location>();
		}
		catch (ParserConfigurationException pce) {
			isParseFailed = true;
			
			TextConsole.writeLine("A configuration error is detected in the XML parser.");
			pce.printStackTrace();
		}
		catch (InvalidXMLException ixe) {
			isParseFailed = true;
			
			TextConsole.writeLine("An error occurred while parsing the XML data.");
			ixe.printStackTrace();
		}
		catch (IOException ie) {
			isParseFailed = true;
			
			TextConsole.writeLine("An error occurred while reading the XML data.");
			ie.printStackTrace();
		}
		finally {
			setDisplayPositionByLatLon(DEFAULT_LATITUDE, DEFAULT_LONGITUDE, DEFAULT_ZOOM);
			repaint();
		}
	}

	/**
	 * set the centre of the map to the input values, then repaint the map;
	 * does not attempt to validate input
	 * 
	 * @param latitude
	 * @param longitude
	 */
	public void relocate(double latitude, double longitude) {
		setDisplayPositionByLatLon(latitude, longitude, getZoom());
		repaint();
	}
	
	/**
	 * add input to selection criteria and update the list of selected polygons
	 * 
	 * @param fams a LanguageFamily as selection criterion
	 */
	public void addSelected(LanguageFamily fam) {
		criteriaFamilies.add(fam);
		updateSelectedPolygons();
	}
	
	/**
	 * add input to selection criteria and update the list of selected polygons
	 * 
	 * @param features a Feature as selection criterion
	 */
	public void addSelected(Feature feature) {
		criteriaFeatures.add(feature);
		updateSelectedPolygons();
	}
	
	/**
	 * add input to selection criteria and update the list of selected polygons
	 * 
	 * @param locs a Location as selection criterion
	 */
	public void addSelected(Location loc) {
		criteriaLocations.add(loc);
		updateSelectedPolygons();
	}
	
	/**
	 * remove input item from selection criteria and update list of selected polygons
	 * 
	 * @param fam a Family to remove
	 */
	public void removeSelected(LanguageFamily fam) {
		criteriaFamilies.remove(fam);
		updateSelectedPolygons();
	}

	/**
	 * remove input item from selection criteria and update list of selected polygons
	 * 
	 * @param feature a Feature to remove
	 */
	public void removeSelected(Feature feature) {
		criteriaFeatures.remove(feature);
		updateSelectedPolygons();
	}

	/**
	 * remove input item from selection criteria and update list of selected polygons
	 * 
	 * @param loc a Location to remove
	 */
	public void removeSelected(Location loc) {
		criteriaLocations.remove(loc);
		updateSelectedPolygons();
	}
	
	/**
	 * checks whether a LanguagePolygon meets the criteria to count
	 * as selected based on current SelectionMode;
	 * 
	 * if SelectionMode is ONE_OF:
	 * a LanguagePolygon only needs to match at least one item from the three
	 * selection criteria lists to count as selected
	 * 
	 * if SelectionMode is ALL_OF:
	 * All family/feature criteria must be met, but this requirement is waived
	 * if the LanguagePolygon has a match in the list of Locations/Languages
	 * 
	 * @param lp the LanguagePolygon object to check
	 * @return true if lp meets the criteria, false otherwise
	 */
	private boolean meetsSelectedCriteria(LanguagePolygon lp) {

		List<LanguageFamily> lfs = lp.getEncapsulatedLanguage().getFamilies();
		List<Feature> features = lp.getEncapsulatedLanguage().getFeatures();
		Location locOfLP = lp.getEncapsulatedLocation();

		if (selectionMode == SelectionMode.ALL_OF) {
			boolean isLocSelected = criteriaLocations.contains(locOfLP);
			
			if (criteriaFamilies.isEmpty() && criteriaFeatures.isEmpty() ) {
				return isLocSelected;
			}
			else if ( criteriaFamilies.isEmpty() ) {
				return isLocSelected || features.containsAll(criteriaFeatures);
			}
			else if ( criteriaFeatures.isEmpty() ) {
				return isLocSelected || lfs.containsAll(criteriaFamilies);
			}
			
			return isLocSelected ||
					(lfs.containsAll(criteriaFamilies) &&
					features.containsAll(criteriaFeatures) );
		}

		if (selectionMode == SelectionMode.ONE_OF) {
			if ( criteriaLocations.contains(locOfLP) ) {
				return true;
			}
			if ( !criteriaFamilies.isEmpty() ) {
				for (LanguageFamily lf : lfs) {
					if ( criteriaFamilies.contains(lf) ) {
						return true;
					}
				}
			}
			if ( !criteriaFeatures.isEmpty() ) {
				for (Feature f : features) {
					if ( criteriaFeatures.contains(f) ) {
						return true;
					}
				}
			}
			return false;
		}
		//undefined case
		TextConsole.writeLine("An undefined selection mode has been encountered. Please talk to the developer.");
		throw new RuntimeException("Undefined SelectionMode.");
	}
	
	/**
	 * check the current criteria for polygon selection and update the list,
	 * then repaint
	 */
	private void updateSelectedPolygons() {
		selectedPolygons.clear();
		for (LanguagePolygon lp : langPolygons) {
			if ( meetsSelectedCriteria(lp) ) {
				selectedPolygons.add(lp);
			}
		}
		repaint();
	}
	
	/**
	 * paint LanguagePolygons using Graphics2D
	 * 
	 * @param g the Graphics object for this
	 * @param lpCollection the collection of LanguagePolygons to paint
	 */
	private void paintLanguagePolygons(Graphics g, Collection<LanguagePolygon> lpCollection) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		notifyObservers();
		LanguagePolygon renderLast = null;
		for (LanguagePolygon lp : lpCollection) {
			if (lp == mouseoveredLP) {
				//if the lp is highlighted, skip for now and render after the loop so it's on top
				lp.setIsHighlighted(true);
				renderLast = lp;
				continue;
			}
			else {
				lp.setIsHighlighted(false);
			}
			paintLanguagePolygon(lp, g2d);
		}
		
		if (renderLast != null) {
			paintLanguagePolygon(renderLast, g2d);
		}
	}
	
	/**
	 * paint a single LanguagePolygon collection, which cannot be null
	 */
	private void paintLanguagePolygon(LanguagePolygon lp, Graphics2D g2d) {
		g2d.setColor(lp.getColor());
		List<Polygon> polys = lp.getPolygons();
		//invalidate copies and recalculate
		lp.getPolygonCopiesOnScreen().clear();;
		
		if (polys == null) {
			return;
		}
		int mapWidth = tileController.getTileSource().getTileSize() << zoom;
		for (Polygon poly : polys) {
			if (poly != null) {
				if (simpleRender) {
					g2d.fill(poly);
				}
				else {
					Rectangle2D polyRect = poly.getBounds2D();
					Rectangle2D rect = new Rectangle2D.Float( (int) polyRect.getMinX(), (int) polyRect.getMinY(),
							TexturePattern.WIDTH, TexturePattern.HEIGHT);
					g2d.setPaint(new TexturePaint(lp.getTexture().getImage(), rect));
					g2d.fill(poly);
				}
				//try to draw copies where they should be on seamless map
				Polygon copy = LanguagePolygon.copy(poly);
				copy.translate(-mapWidth, 0);
				int xAmountFromOriginal = -mapWidth;
				//draw copies to the west where they should be visible
				while (copy.getBounds().x + copy.getBounds().width > 0) {
					g2d.fill(copy);
					lp.getPolygonCopiesOnScreen().add(copy);
					copy = LanguagePolygon.copy(copy);
					copy.translate(-mapWidth, 0);
					xAmountFromOriginal -= mapWidth;
				}
				//set copy polygon back to original x + one mapWidth east
				copy.translate(-xAmountFromOriginal + mapWidth, 0);
				//draw copies to the east where they should be visible
				while (copy.getBounds().x < getWidth()) {
					g2d.fill(copy);
					lp.getPolygonCopiesOnScreen().add(copy);
					copy = LanguagePolygon.copy(copy);
					copy.translate(mapWidth, 0);
				}
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		drawMapSeamlessly(g);
		
		//if no data, then skip painting
		if (isParseFailed) {
			return;
		}
		
		if (criteriaLocations.isEmpty() &&
			criteriaFamilies.isEmpty() &&
			criteriaFeatures.isEmpty() ) {
			paintLanguagePolygons(g, langPolygons);
		}
		else {
			paintLanguagePolygons(g, selectedPolygons);
		}
	}

	/**
	 * repeat the map as necessary so that it wraps around seamlessly
	 * 
	 * @param g graphics device
	 */
	private void drawMapSeamlessly(Graphics g) {
		//draw extra tiles to make map seamless
		//determine a bounding box of where the map is in relation to the component viewport
		//the map rectangle starts at (topLeftX, topLeftY) and has a width/height of mapWidth
		//the viewport rectangle starts at 0, 0 and has a width/height of getWidth(), getHeight()
		final int tileWidth = tileController.getTileSource().getTileSize();
		final int topLeftX = getWidth() / 2 - center.x;
		final int topLeftY = getHeight() / 2 - center.y;
		final int mapWidth = tileWidth << zoom;
		
		//west edge plus x offset is within or to the east of the viewport
		//loop and draw tiles until the viewport is covered
		int x, y, bottomY;
		for (x = topLeftX, y = topLeftY, bottomY = topLeftY + mapWidth; x > 0; x -= tileWidth) {
			while (true) {
				//y component not in viewport, skip
				if (y > getHeight() || bottomY < 0) {
					break;
				}
				//y gone over map width, finish
				if (y >= topLeftY + mapWidth) {
					break;
				}
				//reduce y until range includes only visible tiles
				final boolean isYTileOutOfView = y + tileWidth < 0;
				final boolean isBottomYOutOfView = bottomY > getHeight();
				if (isYTileOutOfView || isBottomYOutOfView) {
					if (isYTileOutOfView) {
						y += tileWidth;
					}
					if (isBottomYOutOfView) {
						bottomY -= tileWidth;
					}
					continue;
				}
				//draw west
				Coordinate topLeft = getAdjustedPosition(x - tileWidth + 1, y + 1);
				Tile tile = tileController.getTile(longitudeToTileX(topLeft.getLon(), zoom),
							latitudeToTileY(topLeft.getLat(), zoom), zoom);
				if (tile != null) {
					tile.paint(g, x - tileWidth + 1, y);
				}
				y += tileWidth;
			}
			//reset y; bottomY stays the same
			y = topLeftY;
		}
		
		final int topRightX = topLeftX + mapWidth;
		//east edge is within viewport or to its west
		for (x = topRightX, y = topLeftY, bottomY = topLeftY + mapWidth; x < getWidth(); x += tileWidth) {
			while (true) {
				//y component not in viewport, skip
				if (y > getHeight() || bottomY < 0) {
					break;
				}
				//y gone over map width, finish
				if (y >= topLeftY + mapWidth) {
					break;
				}
				//reduce y until range includes only visible tiles
				final boolean isYTileOutOfView = y + tileWidth < 0;
				final boolean isBottomYOutOfView = bottomY > getHeight();
				if (isYTileOutOfView || isBottomYOutOfView) {
					if (isYTileOutOfView) {
						y += tileWidth;
					}
					if (isBottomYOutOfView) {
						bottomY -= tileWidth;
					}
					continue;
				}
				//draw east
				Coordinate topRight = getAdjustedPosition(x + 1, y + 1);
				Tile tile = tileController.getTile(longitudeToTileX(topRight.getLon(), zoom),
							latitudeToTileY(topRight.getLat(), zoom), zoom);
				if (tile != null) {
					tile.paint(g, x, y);
				}
				y += tileWidth;
			}
			//reset y; bottomY stays the same
			y = topLeftY;
		}
	}
	
	/**
	 * move the tooltip dynamically
	 */
	@Override
	public Point getToolTipLocation(MouseEvent event) {
		if (getToolTipText(event) == null) {
			return null;
		}
		
		Point point = event.getPoint();
		if (point.y < 40) {
			point.y += 20;
		}
		else {
			point.y -= 20;
		}
		
		return point;
	}
	
	/**
	 * @param lng longitude
	 * @param zoom zoom level
	 * @return tilex for slippy map tile
	 */
	private int longitudeToTileX(double lng, int zoom) {
		return (int) Math.floor((lng + 180.0) / 360.0 * (1 << zoom));
	}
	
	/**
	 * @param lat latitude
	 * @param zoom zoom level
	 * @return tiley for slippy map tile
	 */
	private int latitudeToTileY(double lat, int zoom) {
		int val = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat))
						+ 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 << zoom));
		if (val < 0) {
			val = 0;
		}
		else if (val >= (1 << zoom)) {
			val = (1 << zoom) - 1;
		}
		
		return val;
	}
	
	/**
	 * translate a point to a latlong coordinate, wrapped between max and min values
	 * @param point a point on screen
	 * @return latlong coordinate
	 */
	public Coordinate getAdjustedPosition(Point point) {
		return getAdjustedPosition(point.x, point.y);
	}
	
	/**
	 * translate a point to a latlong coordinate, wrapped between max and min values
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return latlong coordinate
	 */
	public Coordinate getAdjustedPosition(int x, int y) {
		Coordinate coord = getPosition(x, y);
		
		double lat = coord.getLat();
		while (lat < MIN_LATITUDE) {
			lat += MAX_LATITUDE * 2;
		}
		while (lat > MAX_LATITUDE) {
			lat -= MAX_LATITUDE * 2;
		}
		
		double lon = coord.getLon();
		while (lon < MIN_LONGITUDE) {
			lon += MAX_LONGITUDE * 2;
		}
		while (lon > MAX_LONGITUDE) {
			lon -= MAX_LONGITUDE * 2;
		}
		
		coord.setLat(lat);
		coord.setLon(lon);
		
		return coord;
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
		for (IObserver obs : observers) {
			obs.update();
		}
	}
	
	//accessors
	public MapData getData() { return data; }
	public ViewMode getViewMode() { return viewMode; }
	public Map setViewMode(ViewMode vm) { viewMode = vm; return this; }
	/**
	 * @return if true, use simple colours; if false, use textures
	 */
	public boolean getSimpleRender() { return simpleRender; }
	/**
	 * sets simpleRender and calls for the component to repaint itself
	 * 
	 * @param b if true, use simple colours; if false, use textures
	 */
	public Map setSimpleRender(boolean b) {
		if (simpleRender != b) {
			simpleRender = b;
			repaint();
		}
		return this;
	}
	/**
	 * this setter also calls an update if selection mode is changed
	 */
	public Map setSelectionMode(SelectionMode sm) {
		if (selectionMode != sm) {
			selectionMode = sm;
			updateSelectedPolygons();
		}
		return this;
	}
	
	/**
	 * inner helper class that serves as Map object's mouse listener
	 * 
	 * @author RK
	 * @see DefaultMapController
	 */
	private class MapTroller extends DefaultMapController {
		
		/**
		 * constructor simply calls parent constructor,
		 * also enables left-button for movement
		 * 
		 * @param map the JMapViewer object to listen for
		 * @see DefaultMapController
		 */
		public MapTroller(JMapViewer map) {
			super(map);
			setMovementMouseButton(MouseEvent.BUTTON1);
		}
		
		/**
		 * releasing mouse prints latitude/longitude at mouse pointer to text console
		 */
		@Override
		public void mouseReleased(MouseEvent e)
		{
			super.mouseReleased(e);
			if (e.getClickCount() == 1) {
				Point mousePosition = getMousePosition();
				if (mousePosition == null) {
					//mouse pointer has most likely exited the component; just return
					return;
				}
				else {
					Coordinate coord = getAdjustedPosition(mousePosition);
					TextConsole.writeLine("{Latitude/Longitude: " + coord.getLat() + ", " + coord.getLon() +
										"} at mouse pointer.");
				}
			}
		}

		/**
		 * single-clicking, without dragging, on a language shows information about it;
		 * left-clicking shows info in the left box, right-clicking in the right.
		 * 
		 * zoom in when double-clicking is detected, regardless of which key
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				map.zoomIn(e.getPoint());
			}
			else if (e.getClickCount() == 1) {
				if (mouseoveredLP != null) {
					if (isParseFailed) {
						return;
					}
					
					Language l = mouseoveredLP.getEncapsulatedLanguage();
	
					if ( SwingUtilities.isLeftMouseButton(e) ) {
						controlPanel.setInfoBoxLeftText( l.getHTML() );
					}
					else if ( SwingUtilities.isRightMouseButton(e) ) {
						controlPanel.setInfoBoxRightText( l.getHTML() );
					}
				}
			}
		}
		
		/**
		 * detects which polygon the mouse is currently over and update the state accordingly
		 * also quietly recentres component if it goes too far left or right
		 */
		@Override
		public void mouseMoved(MouseEvent e) {
			super.mouseMoved(e);
			final int mapWidth = tileController.getTileSource().getTileSize() << zoom;
			while (center.x < 0) {
				moveMap(mapWidth, 0);
			}
			while (center.x > getWidth()) {
				moveMap(-mapWidth, 0);
			}
			
			if (isParseFailed) {
				return;
			}
			
			Point p = e.getPoint();
			if (mouseoveredLP != null && mouseoveredLP.contains(p) ) {
				return;
			}

			for (LanguagePolygon lp : langPolygons) {
				if (lp.contains(p) ) {
					//only update state if moused-over language polygon is actually visible in the current view mode
					if ( ( criteriaLocations.isEmpty() && criteriaFamilies.isEmpty() && criteriaFeatures.isEmpty() )
						|| selectedPolygons.contains(lp) ) {
						mouseoveredLP = lp;
						setToolTipText( lp.getEncapsulatedLanguage().getCommonName() );
						repaint();
						return;
					}
					else {
						continue;
					}
				}
			}
			mouseoveredLP = null;
			setToolTipText(null);
			repaint();
		}
		
		/**
		 * mouse entering the map viewer is treated as equivalent to mouse moving
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			super.mouseEntered(e);
			mouseMoved(e);
		}
		
		/**
		 * empties mouse-over state upon mouse exiting the map viewer
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			super.mouseExited(e);
			mouseoveredLP = null;
			setToolTipText(null);
			repaint();
		}
	}
}
