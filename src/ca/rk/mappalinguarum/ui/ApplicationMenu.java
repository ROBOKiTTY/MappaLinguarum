package ca.rk.mappalinguarum.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * encapsulates the menu bar and all menu items
 * 
 * @author RK
 *
 */

public class ApplicationMenu extends JMenuBar {

	private ApplicationFrame mainWindow;
	/**
	 * uid for serialization
	 */
	private static final long serialVersionUID = 1;
	private final JMenu menuFile;
	private final JMenuItem fileItemExit;
	private final JMenu menuView;
	private final JMenuItem viewItemTexturedPolygons;
	private final JMenuItem viewItemShowHideControlBox;
	private final JMenuItem viewItemShowHideMap;
	private final JMenuItem viewItemShowHideConsole;
	private final JMenuItem viewMosaicToggle;
	private final JMenuItem viewFamilyToggle;
	private final JMenuItem viewOneOf;
	private final JMenuItem viewAllOf;
	private final JMenu menuHalp;
	private final JMenuItem halpItemAbout;
	
	/**
	 * construct the menu bar and all menu items for the application
	 * 
	 * @param parent the window object on which the menu bar is anchored
	 */
	public ApplicationMenu(ApplicationFrame parent) {
		mainWindow = parent;
		//File
		menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);
		//File->Exit
		fileItemExit = new JMenuItem("Exit");
		fileItemExit.setMnemonic(KeyEvent.VK_X);
		fileItemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		//putting File together
		menuFile.add(fileItemExit);
		
		//View
		menuView = new JMenu("View");
		menuView.setMnemonic(KeyEvent.VK_V);
		//View->Switch area colouring to textured/simple
		viewItemTexturedPolygons = new JMenuItem("Switch area colouring to textured");
		viewItemTexturedPolygons.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Map map = (Map) mainWindow.getMap();
				boolean simpleRender = map.getSimpleRender();
				if (simpleRender) {
					viewItemTexturedPolygons.setText("Switch area colouring to simple");
				}
				else {
					viewItemTexturedPolygons.setText("Switch area colouring to textured");
				}
				map.setSimpleRender(!simpleRender);
			}
		});
		//View->Show/Hide Selection Control Box
		viewItemShowHideControlBox = new JMenuItem("Hide Selection Control Box/Infoboxes");
		viewItemShowHideControlBox.setMnemonic(KeyEvent.VK_S);
		viewItemShowHideControlBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isVisible = mainWindow.getControlPanel().getControlAndInfoSplitPane().isVisible();
				mainWindow.getControlPanel().getControlAndInfoSplitPane().setVisible(!isVisible);
				if (isVisible)
				{
					mainWindow.getMapAndControlSplitPane().setDividerLocation(1d);
					viewItemShowHideControlBox.setText("Show Selection Control Box/Infoboxes");
				}
				else
				{
					mainWindow.getMapAndControlSplitPane().setDividerLocation(0.65);
					viewItemShowHideControlBox.setText("Hide Selection Control Box/Infoboxes");
				}
				mainWindow.validate();
			}
		});
		
		//View->Show/Hide Map
		viewItemShowHideMap = new JMenuItem("Hide Map");
		viewItemShowHideMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isVisible = mainWindow.getMap().isVisible();
				mainWindow.getMap().setVisible(!isVisible);
				if (isVisible)
				{
					mainWindow.getMapAndControlSplitPane().setDividerLocation(0d);
					viewItemShowHideMap.setText("Show Map");
				}
				else
				{
					mainWindow.getMapAndControlSplitPane().setDividerLocation(0.75);
					viewItemShowHideMap.setText("Hide Map");
				}
				mainWindow.validate();
			}
		});
		
		//View->Show/Hide Bottom Console
		viewItemShowHideConsole = new JMenuItem("Hide Bottom Console");
		viewItemShowHideConsole.setMnemonic(KeyEvent.VK_C);
		viewItemShowHideConsole.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isVisible = mainWindow.getTextPane().isVisible();
				mainWindow.getTextPane().setVisible(!isVisible);
				if (isVisible)
				{
					mainWindow.getTopAndBottomSplitPane().setDividerLocation(1d);
					viewItemShowHideConsole.setText("Show Bottom Console");
				}
				else
				{
					mainWindow.getTopAndBottomSplitPane().setDividerLocation(0.8d);
					viewItemShowHideConsole.setText("Hide Bottom Console");
				}
				mainWindow.validate();
			}
		});
		//View->Mosaic View Mode
		viewMosaicToggle = new JMenuItem("✓ Mosaic View Mode");
		viewMosaicToggle.setMnemonic(KeyEvent.VK_M);
		viewMosaicToggle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Map m = (Map) mainWindow.getMap();
				m.setViewMode(ViewMode.MOSAIC);
				viewMosaicToggle.setText("✓ Mosaic View Mode");
				viewFamilyToggle.setText("Family View Mode");
			}
		});
		//View->Family View Mode
		viewFamilyToggle = new JMenuItem("Family View Mode");
		viewFamilyToggle.setMnemonic(KeyEvent.VK_F);
		viewFamilyToggle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Map m = (Map) mainWindow.getMap();
				m.setViewMode(ViewMode.FAMILIES);
				viewMosaicToggle.setText("Mosaic View Mode");
				viewFamilyToggle.setText("✓ Family View Mode");
			}
		});
		//View->Display languages matching any selected option
		viewOneOf = new JMenuItem("✓ Display languages matching any selected option");
		viewOneOf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Map m = (Map) mainWindow.getMap();
				m.setSelectionMode(SelectionMode.ONE_OF);
				viewOneOf.setText("✓ Display languages matching any selected option");
				viewAllOf.setText("Display only languages matching all selected options");
			}
		});
		//View->Display only languages matching all selected options
		viewAllOf = new JMenuItem("Display only languages matching all selected options");
		viewAllOf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Map m = (Map) mainWindow.getMap();
				m.setSelectionMode(SelectionMode.ALL_OF);
				viewOneOf.setText("Display languages matching any selected option");
				viewAllOf.setText("✓ Display only languages matching all selected options");
			}
		});
		//putting View together
		menuView.add(viewItemTexturedPolygons);
		menuView.addSeparator();
		menuView.add(viewItemShowHideControlBox);
		menuView.add(viewItemShowHideMap);
		menuView.add(viewItemShowHideConsole);
		menuView.addSeparator();
		menuView.add(viewMosaicToggle);
		menuView.add(viewFamilyToggle);
		menuView.addSeparator();
		menuView.add(viewOneOf);
		menuView.add(viewAllOf);
		
		//Help
		menuHalp = new JMenu("Help");
		menuHalp.setMnemonic(KeyEvent.VK_H);
		//Help->About
		halpItemAbout = new JMenuItem("About");
		halpItemAbout.setMnemonic(KeyEvent.VK_A);
		halpItemAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(mainWindow,
								//one long string below
								"Mappa Linguārum allāta ā mē.\n\n\n" + 
								"Special thanks, in no particular order:\n\n" +
								"First Peoples' Language Map of British Columbia\n" +
								"(http://maps.fphlcc.ca/)\n" +
								" • Provided invaluable information on the topic\n\n" +
								"FirstVoices (http://www.firstvoices.com/en/home)\n" +
								"The Yinka Déné Language Institute (http://www.ydli.org/)\n" +
								" • Sources of data on First Nations languages in BC\n\n" +
								"OpenStreetMap (http://www.openstreetmap.org/)\n" +
								" • Powers this application\n\n" +
								"\n\n\n" +
								"This application is powered by OpenStreetMap and JMapViewer.\n" +
								" • OSM is licensed under Creative Commons Attribution-ShareAlike.\n" +
								" • JMapViewer is licensed under the GNU General Public Licence." +
								"\n\n\n\n",
								//one long string above
								"About...",	//title
								JOptionPane.INFORMATION_MESSAGE,
								mainWindow.APPLICATION_ICON );
			}
		});
		//putting Help together
		menuHalp.add(halpItemAbout);
		//tying everything together
		this.add(menuFile);
		this.add(menuView);
		this.add(menuHalp);
	}
}
