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

import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.xml.parsers.ParserConfigurationException;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import ca.rk.mappalinguarum.exceptions.InvalidXMLException;
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
	protected final double DEFAULT_LATITUDE = 54.0;
	protected final double DEFAULT_LONGITUDE = -130.0;
	protected final int DEFAULT_ZOOM = 4;

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
		setDisplayPositionByLatLon(latitude, longitude, getZoom() );
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
	private void paintPolygons(Graphics g, Collection<LanguagePolygon> lpCollection) {
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
			paintPolygon(lp, g2d);
		}
		
		if (renderLast != null) {
			paintPolygon(renderLast, g2d);
		}
	}
	
	/**
	 * paint a single LanguagePolygon collection, which cannot be null
	 */
	private void paintPolygon(LanguagePolygon lp, Graphics2D g2d) {
		g2d.setColor(lp.getColor());
		List<Polygon> polys = lp.getPolygons();
		if (polys == null) {
			return;
		}
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
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (isParseFailed) {
			return;
		}
		
		if (criteriaLocations.isEmpty() &&
			criteriaFamilies.isEmpty() &&
			criteriaFeatures.isEmpty() ) {
			paintPolygons(g, langPolygons);
		}
		else {
			paintPolygons(g, selectedPolygons);
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
					Coordinate coord = getPosition(getMousePosition());
					TextConsole.writeLine("{Longitude/Latitude: " + coord.getLon() + "," + coord.getLat() +
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
		 */
		@Override
		public void mouseMoved(MouseEvent e) {
			super.mouseMoved(e);
        	if (isParseFailed) {
        		return;
        	}
			
			Point p = e.getPoint();
			if (mouseoveredLP != null && mouseoveredLP.contains(p) ) {
				return;
			}

			for (LanguagePolygon lp : langPolygons) {
				if (lp.contains(p) ) {
					//only update state if moused-over language polygon is actually visible in the current view
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
