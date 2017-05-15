package ca.rk.mappalinguarum.ui;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.openstreetmap.gui.jmapviewer.OsmFileCacheTileLoader;

/**
 * encapsulates the Swing frame (i.e. window) in which the application runs
 * 
 * @author RK
 *
 */

public class ApplicationFrame extends JFrame {

	/**
	 * uid for serialization
	 */
	private static final long serialVersionUID = 1L;
	public static final Font FONT = new Font("Arial", Font.PLAIN, 12);
	
	private final String APPLICATION_TITLE = "Mappa Linguarum";
	protected final ImageIcon APPLICATION_ICON = new ImageIcon("icons/globe-icon.png");
	private final double MAP_SIZE_MODIFIER = 0.49;
	private final double CONTROL_PANEL_SIZE_MODIFIER = 0.5;
	private final double TEXT_PANE_SIZE_MODIFIER = 0.08;
	
	private final int TOOLTIP_INITIAL_DELAY = 0;
	private final int TOOLTIP_DISMISS_DELAY = 30000;

	private int screenWidth;
	private int screenHeight;
	/**
	 * default application window size;
	 * these numbers are set relational to screen size, but largely arbitrary
	 */
	private int appWidth;
	private int appHeight;
	
	private ApplicationMenu menuBar;
	private JSplitPane mapAndControlSplitPane;
	private JSplitPane topAndBottomSplitPane;
	//placeholder panel until actual map loads
	private JPanel mapPlaceholder;
	private JScrollPane textPane;
	private ControlPanel controlPanel;
	private Map map;
	private boolean isMapInitialized = false;
	
	/**
	 * constructs an ApplicationFrame, setting its dimensions and fields, and calls UI construction methods
	 */
	public ApplicationFrame() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		catch (Exception e) {
			System.out.println("UI Look and Feel not found, defaulting to system L&F.");
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch (ClassNotFoundException ee) {
				ee.printStackTrace();
			}
			catch (InstantiationException ee) {
				ee.printStackTrace();
			}
			catch (IllegalAccessException ee) {
				ee.printStackTrace();
			}
			catch (UnsupportedLookAndFeelException ee) {
				ee.printStackTrace();
			}
		}
		
		initializeUI();
		
		Runnable r = new Runnable() {
			public void run() {
				loadMap();
				addComponentListener( new SizeListener() );
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
	/**
	 * lays out child UI components in the window
	 * 
	 * Modifies: this
	 * Effect: initialize UI elements
	 */
	private void initializeUI() {
		//finish building this frame
		DisplayMode currentDisplay = getGraphicsConfiguration().getDevice().getDisplayMode();
		screenWidth = currentDisplay.getWidth();
		screenHeight = currentDisplay.getHeight();
		appWidth = (int) (screenWidth / 1.5);
		appHeight = (int) (screenHeight / 1.5);
		addWindowListener( new WindowListener() );
		addKeyListener( new KeyTroller() );
		setSize(appWidth, appHeight);
		setLocationRelativeTo(null);	//centres window
		setFont(FONT);
		setTitle(APPLICATION_TITLE);
		setIconImage( APPLICATION_ICON.getImage() );
		ToolTipManager.sharedInstance().setInitialDelay(TOOLTIP_INITIAL_DELAY);
		ToolTipManager.sharedInstance().setDismissDelay(TOOLTIP_DISMISS_DELAY);
		setVisible(true);
		
		//add bottom text console
		TextConsole textConsole = TextConsole.getInstance();
		textPane = textConsole.getTextPane();
		textPane.setPreferredSize(new Dimension(appWidth , (int) (appHeight * TEXT_PANE_SIZE_MODIFIER) ) );
		textPane.setBorder( BorderFactory.createEtchedBorder() );
		
		//add right-side panels
		controlPanel = new ControlPanel();
		controlPanel.setPreferredSize(new Dimension( (int) (appWidth * CONTROL_PANEL_SIZE_MODIFIER), 0) );
		controlPanel.setBorder( BorderFactory.createEtchedBorder() );

		//add menu
		menuBar = new ApplicationMenu(this);
		this.setJMenuBar(menuBar);
		
		//create temp map placeholder
		mapPlaceholder = new JPanel();
		mapPlaceholder.setPreferredSize( new Dimension( (int) (appWidth * MAP_SIZE_MODIFIER), appHeight) );
		
		//set up split panes and add to this frame
		mapAndControlSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mapPlaceholder, controlPanel);
		mapAndControlSplitPane.setDividerLocation(0.5);
		mapAndControlSplitPane.setResizeWeight(0.5);
		this.add(mapAndControlSplitPane);
		
		topAndBottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mapAndControlSplitPane, textPane);
		topAndBottomSplitPane.setDividerLocation(0.75);
		topAndBottomSplitPane.setResizeWeight(0.75);
		this.add(topAndBottomSplitPane);
		
		TextConsole.clear();
		TextConsole.writeLine("Loading map...");

		//force validate now that UI has been layed out
		this.validate();
	}
	
	/**
	 * test connection to OpenStreetMap, blocking execution until connection succeeds or times out
	 * 
	 * @return true if connection is made
	 */
	private boolean testConnection() {
		try {
			URL url = new URL("http://tile.openstreetmap.org");
			URLConnection conn = url.openConnection();
			conn.connect();
			return true;
		}
		//should not happen
		catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		catch (UnknownHostException e) {
			TextConsole.writeLine("Error resolving hostname");
			return false;
		}
		catch (SocketTimeoutException e) {
			TextConsole.writeLine("Connection timed out.");
			return false;
		}
		catch (IOException e) {
			TextConsole.writeLine("Unspecified I/O problem. Please check error logs.");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * initiate the map layer; this is the last stop for uncaught exceptions before main()
	 */
	private void loadMap() {
		if (testConnection()) {
			Runnable r = new Runnable() {
				public void run() {
					//initiate map
					try {
						map = new Map(controlPanel);
						map.setPreferredSize( new Dimension( (int) (appWidth * MAP_SIZE_MODIFIER), appHeight) );
						map.setBorder( BorderFactory.createEtchedBorder() );
						map.setTileLoader( new OsmFileCacheTileLoader(map) );
						mapAndControlSplitPane.remove(mapPlaceholder);
						mapAndControlSplitPane.add(map);
						controlPanel.initiateControlBoxContents();
						isMapInitialized = true;
					}
					catch (SecurityException e) {
						TextConsole.writeLine("Failure to access system property for security reasons. Please check "
								+ "error logs.");
						e.printStackTrace();
					}
					catch (IOException e) {
						TextConsole.writeLine("Cannot access map loader's system cache. Please check error logs.");
						e.printStackTrace();
					}
	
					TextConsole.writeLine("Map loaded!");
					//force validate now that UI has been layed out
					validate();
				}
			};
			SwingUtilities.invokeLater(r);
		}
		else {
			TextConsole.writeLine("Unable to connect to OpenStreetMap. The service may be unavailable right now, "
					+ "your Internet connectivity may be limited, or firewall settings may be blocking the connection. "
					+ "Please restart the application when the problem is resolved.");
		}
	}
	
	//accessors
	/**
	 * if the placeholder is returned, a cast to Map will fail
	 * 
	 * @return map if it's been properly loaded; otherwise returns the placeholder empty component
	 */
	public JPanel getMapComponent() {
		if (isMapInitialized) {
			return map;
		}
		return mapPlaceholder;
	}
	public ControlPanel getControlPanel() { return controlPanel; }
	public JScrollPane getTextPane() { return textPane; }
	public JSplitPane getMapAndControlSplitPane() { return mapAndControlSplitPane; }
	public JSplitPane getTopAndBottomSplitPane() { return topAndBottomSplitPane; }
	
	/**
	 * 
	 * event listener for the application window
	 * 
	 * @author RK
	 * @see WindowAdapter
	 *
	 */
	private class WindowListener extends WindowAdapter {
		/**
		 * exits application when the window is closing
		 * @see WindowAdapter
		 * 
		 */
		@Override
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	/**
	 * inner helper class that listens for key events
	 * 
	 * @author RK
	 * @see KeyAdapter
	 */
	private class KeyTroller extends KeyAdapter {
		/**
		 * releasing alt key highlights first menu item
		 */
		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ALT) {
				Runnable r = new Runnable() {
					@Override
					public void run()
					{
						menuBar.getMenu(0).doClick();
					}
				};
				SwingUtilities.invokeLater(r);
			}
		}
	}
	
	/**
	 * handles resize events for the window
	 * 
	 * @author RK
	 * @see ComponentAdapter
	 *
	 */
	private class SizeListener extends ComponentAdapter {
		/**
		 * resize all components when window gets resized
		 */
		@Override
		public void componentResized(ComponentEvent e) {
			super.componentResized(e);
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					appWidth = getWidth();
					appHeight = getHeight();
					textPane.setPreferredSize(new Dimension(appWidth ,
															(int) (appHeight * TEXT_PANE_SIZE_MODIFIER) ) );
					map.setPreferredSize( new Dimension( (int) (appWidth * MAP_SIZE_MODIFIER), appHeight) );
					controlPanel.setPreferredSize(new Dimension( (int) (appWidth * CONTROL_PANEL_SIZE_MODIFIER ),
																appHeight - textPane.getPreferredSize().height) );
					validate();
				}
			});
		}
	}
}
