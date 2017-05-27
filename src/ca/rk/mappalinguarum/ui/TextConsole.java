package ca.rk.mappalinguarum.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;

import ca.rk.mappalinguarum.util.OperatingSystem;
import ca.rk.mappalinguarum.util.OperatingSystem.OS;

/**
 * encapsulates a singleton console that displays messages to users
 * regarding the operations of the application; information regarding the map and languages
 * is displayed in ControlPanel instead
 * 
 * @author RK
 *
 */

public class TextConsole {
	
	private static TextConsole textConsole = null;
	private JScrollPane textPane;
	private JTextArea textArea;
	private ContextMenu contextMenu;

	/**
	 * private constructor, called from static method;
	 * creates a TextConsole with containing scroll pane
	 */
	private TextConsole() {		
		//add bottom text console
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(ApplicationFrame.FONT);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setText("");
		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		textPane = new JScrollPane(textArea);
		textPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		textPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		contextMenu = new ContextMenu();
		textArea.addMouseListener(new TextConsoleMouseListener());
	}
	
	/**
	 * get the singleton console, creating it if it hasn't been created already
	 * 
	 * @return the singleton TextConsole
	 */
	public static TextConsole getInstance() {
		if (textConsole == null) {
			textConsole = new TextConsole();
		}
		return textConsole;
	}
	
	/**
	 * append a text message to the text console, automagically adding a linebreak after;
	 * inits textConsole if it is null; also forwards message to System.out
	 * 
	 * @param text input text message
	 */
	public static void writeLine(String text) {
		if (textConsole == null) { getInstance(); }
		
		final String _text = text;
		Runnable r = new Runnable() {
			public void run() {
				textConsole.textArea.append(_text + System.lineSeparator());
				textConsole.textArea.setCaretPosition(textConsole.textArea.getDocument().getLength());
			}
		};
		SwingUtilities.invokeLater(r);
		System.out.println(_text);
	}
	
	/**
	 * clear text console;
	 * inits textConsole if it is null
	 */
	public static void clear() {
		if (textConsole == null) { getInstance(); }

		Runnable r = new Runnable() {
			public void run() {
				textConsole.textArea.setText("");
				textConsole.textArea.setCaretPosition(textConsole.textArea.getDocument().getLength());
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
	//object accessors
	public JScrollPane getTextPane() { return textPane; }
	
	/**
	 * right-click context menu functionality
	 * 
	 * @author RK
	 *
	 */
	private class ContextMenu extends JPopupMenu {
		private static final long serialVersionUID = 1L;
		
		private JMenuItem menuOptionCopy;
		private JMenuItem menuOptionSelectAll;
		private JMenuItem menuOptionClear;
		
		/**
		 * constructs a ContextMenu with menu items
		 */
		public ContextMenu() {
			//Copy
			menuOptionCopy = new JMenuItem("Copy");
			menuOptionCopy.setMnemonic(KeyEvent.VK_C);
			menuOptionCopy.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					textArea.copy();
				}
			});
			//Select All
			menuOptionSelectAll = new JMenuItem("Select All");
			menuOptionSelectAll.setMnemonic(KeyEvent.VK_A);
			menuOptionSelectAll.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					textArea.selectAll();
				}
			});
			//Clear
			menuOptionClear = new JMenuItem("Clear");
			menuOptionClear.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					clear();
				}
			});
			add(menuOptionCopy);
			add(menuOptionSelectAll);
			add(menuOptionClear);
		}
	}
	
	/**
	 * handles mouse gestures for the context menu
	 * 
	 * @author RK
	 *
	 */
	private class TextConsoleMouseListener extends MouseAdapter {
		
		private OS os;
		
		/**
		 * constructs an TextConsoleMouseListener and notes what OS is running
		 * in order to produce the proper context menu behaviour
		 */
		public TextConsoleMouseListener() {
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
