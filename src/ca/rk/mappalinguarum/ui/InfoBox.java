package ca.rk.mappalinguarum.ui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

import ca.rk.mappalinguarum.model.phoneme.PhonemeInventory;
import ca.rk.mappalinguarum.util.BrowserNavigator;
import ca.rk.mappalinguarum.util.OperatingSystem;
import ca.rk.mappalinguarum.util.OperatingSystem.OS;


/**
 * this is a HTML-rendering info box extending JEditorPane
 * 
 * @author RK
 * @see JEditorPane
 */

public class InfoBox extends JEditorPane {

	private static final long serialVersionUID = 1L;
	
	private InfoContextMenu contextMenu;

	/**
	 * constructs an uneditable InfoBox with document model set to HTML
	 */
	public InfoBox() {
		contextMenu = new InfoContextMenu();
		setEditable(false);
		setContentType("text/html");
		setComponentPopupMenu(contextMenu);
		addMouseListener( new InfoBoxMouseListener() );
		ToolTipManager.sharedInstance().registerComponent(this);
	}
	
	/**
	 * display a tooltip over abbreviated text
	 */
	@Override
	public String getToolTipText(MouseEvent event) {
		Point point = event.getPoint();
		int offset = this.viewToModel(point);
		try {
			int startingOffset = Utilities.getWordStart(this, offset);
			int length = Utilities.getWordEnd(this, offset) - startingOffset;
			String sourceText = getText(startingOffset, length);
			return PhonemeInventory.getFullName(sourceText);
		}
		catch (BadLocationException ble) {
			System.out.println("BadLocationException from InfoBox.");
			return null;
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
	 * contains implementation for context menu functionality for InfoBox
	 * 
	 * @author RK
	 *
	 */
	private class InfoContextMenu extends JPopupMenu {

		private static final long serialVersionUID = 1L;
		
		private JMenuItem menuOptionCopy;
		private JMenuItem menuOptionSelectAll;
		private JMenuItem menuOptionViewExternInBrowser;
		private JMenuItem menuOptionSaveAs;
		private BrowserNavigator browserNavigator;
		private JFileChooser fileSaver;
		private File fileForExternView;
		
		/**
		 * construct an InfoContextMenu with items for an InfoBox
		 * 
		 */
		public InfoContextMenu() {
			browserNavigator = new BrowserNavigator();
			fileSaver = new FileSaver(new String[] {".htm", ".html"}, "HTML");
			
			//Copy
			menuOptionCopy = new JMenuItem("Copy");
			menuOptionCopy.setMnemonic(KeyEvent.VK_C);
			menuOptionCopy.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					copy();
				}
			});
			//Select All
			menuOptionSelectAll = new JMenuItem("Select All");
			menuOptionSelectAll.setMnemonic(KeyEvent.VK_A);
			menuOptionSelectAll.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectAll();
				}
			});
			//View Externally In Browser
			menuOptionViewExternInBrowser = new JMenuItem("View Externally In Browser");
			menuOptionViewExternInBrowser.setMnemonic(KeyEvent.VK_X);
			menuOptionViewExternInBrowser.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						fileForExternView = new File("temp.html");
						FileWriter writer = new FileWriter(fileForExternView);
						writer.write( getText() );
						writer.close();
						browserNavigator.navigateToURL( fileForExternView.toURI().toURL() );
					}
					catch (IOException ie) {
						TextConsole.writeLine("There was a read/write error.");
					}
					finally {
						if ( fileForExternView.exists() ) {
							fileForExternView.deleteOnExit();
						}
					}
				}
			});
			//Save Page As...
			menuOptionSaveAs = new JMenuItem("Save Page As...");
			menuOptionSaveAs.setMnemonic(KeyEvent.VK_S);
			menuOptionSaveAs.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int result = fileSaver.showSaveDialog(null);
					if (result == JFileChooser.APPROVE_OPTION) {
						File fileToSave = fileSaver.getSelectedFile().getAbsoluteFile();
						try {
							FileWriter writer = new FileWriter(fileToSave);
							writer.write( getText() );
							writer.close();
						}
						catch (IOException ie) {
							TextConsole.writeLine("Save failed. There might be insufficient disk space, or you" +
									" might lack write permission.");
						}
					}
				}
			});
			
			add(menuOptionCopy);
			add(menuOptionSelectAll);
			addSeparator();
			add(menuOptionViewExternInBrowser);
			add(menuOptionSaveAs);
		}
	}
	
	/**
	 * handles mouse gestures for the context menu
	 * 
	 * @author RK
	 *
	 */
	private class InfoBoxMouseListener extends MouseAdapter {
		
		private OS os;
		
		/**
		 * constructs an InfoBoxMouseListener and notes what OS is running
		 * in order to produce the proper context menu behaviour
		 */
		public InfoBoxMouseListener() {
			os = OperatingSystem.getOS();
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			if ( e.isPopupTrigger() ) {
				if (os != OS.MAC) {
					return;
				}
				contextMenu.show( e.getComponent(), e.getX(), e.getY() );
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if ( e.isPopupTrigger() ) {
				if (os == OS.MAC) {
					return;
				}
				contextMenu.show( e.getComponent(), e.getX(), e.getY() );
			}
		}
	}
}
