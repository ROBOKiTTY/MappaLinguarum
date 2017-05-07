package ca.rk.mappalinguarum.ui;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

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
		
		textPane = new JScrollPane(textArea);
		textPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		textPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
	 * does nothing if textConsole is null
	 * 
	 * @param text input text message
	 */
	public static void writeLine(String text) {
		if (textConsole == null) { return; }
		
		final String _text = text;
		Runnable r = new Runnable() {
			public void run() {
				textConsole.textArea.append(_text + System.lineSeparator());
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
	/**
	 * clear text console;
	 * does nothing if textConsole is null
	 */
	public static void clear() {
		if (textConsole == null) { return; }

		Runnable r = new Runnable() {
			public void run() {
				textConsole.textArea.setText("");
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
	//object accessors
	public JScrollPane getTextPane() { return textPane; }
}
