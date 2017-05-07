package ca.rk.mappalinguarum.ui;

import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

/**
 * this is the right-side control panel for controlling the map and displaying language information;
 * 
 * @author RK
 *
 */
public class ControlPanel extends JPanel {

	private static final long serialVersionUID = 1;

	private Map map;
	private JTabbedPane controlBox;
	private JPanel familyControlPanel;
	private JPanel featureControlPanel;
	private JPanel languageControlPanel;
	//private JPanel phonemeControlPanel;	//this is for future extension, so not implemented for project
	private JPanel infoPanel;
	private HtmlLinkListener linkListener;
	private InfoBox infoBoxLeft;
	private InfoBox infoBoxRight;
	private JScrollPane infoScrollLeft;
	private JScrollPane infoScrollRight;
	private SelectionTracker selectionTracker;
	
	/**
	 * constructs a ControlPanel by building child UI components
	 * 
	 */
	public ControlPanel() {
		linkListener = new HtmlLinkListener();
		initiateUI();
	}
	
	/**
	 * create and lay out child UI components on Swing's event-dispatch thread
	 * when available
	 */
	private void initiateUI() {
		final BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
		Runnable r = new Runnable() {
			public void run() {
				Dimension topDim = new Dimension(getWidth(), (int) ( getHeight() * 0.3) );
				Dimension bottomDim = new Dimension(getWidth(), (int) ( getHeight() * 0.7) );
				
				controlBox = new JTabbedPane();
				controlBox.setPreferredSize(topDim);
				controlBox.setBorder( BorderFactory.createEtchedBorder() );
				controlBox.setAlignmentY(TOP_ALIGNMENT);
				
				initiateControlBox();
				
				infoPanel = new JPanel();
				infoPanel.setPreferredSize(bottomDim);
				infoPanel.setBorder( BorderFactory.createEtchedBorder() );
				infoPanel.setAlignmentY(BOTTOM_ALIGNMENT);
				
				initiateInfoPanelContents();
				
				setLayout(boxLayout);
				add(controlBox);
				add(infoPanel);
				validate();
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
	/**
	 * define and lay out tabs in the upper controlBox
	 */
	private void initiateControlBox() {
		familyControlPanel = new JPanel();
		featureControlPanel = new JPanel();
		languageControlPanel = new JPanel();
		//phonemeControlPanel = new JPanel();
		
		familyControlPanel.setLayout( new BoxLayout(familyControlPanel, BoxLayout.PAGE_AXIS) );
		featureControlPanel.setLayout( new BoxLayout(featureControlPanel, BoxLayout.PAGE_AXIS) );
		languageControlPanel.setLayout( new BoxLayout(languageControlPanel, BoxLayout.PAGE_AXIS) );
		
		JScrollPane familySP = new JScrollPane(familyControlPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
									JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollPane featureSP = new JScrollPane(featureControlPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
									JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollPane languageSP = new JScrollPane(languageControlPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
									JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//JScrollPane phonemeSP =  new JScrollPane(phonemeControlPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		//							JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		controlBox.addTab("Family", null, familySP, "View language family controls");
		controlBox.addTab("Feature", null, featureSP, "View language feature controls");
		controlBox.addTab("Language", null, languageSP, "View individual languages and controls");
		//controlBox.addTab("Phoneme", null, phonemeSP, "View contrastive sounds.");
	}
	
	/**
	 * construct and set infoPanel's contents at the event-dispatch thread's convenience
	 */
	private void initiateInfoPanelContents() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				infoBoxLeft = new InfoBox();
				infoBoxLeft.setEditable(false);
				infoBoxLeft.setContentType("text/html");
				infoBoxLeft.addHyperlinkListener(linkListener);
				infoBoxLeft.setText("Left-click a language on the map to show its information here.");
				infoScrollLeft = new JScrollPane(infoBoxLeft,
								ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
								ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

				infoBoxRight = new InfoBox();
				infoBoxRight.setEditable(false);
				infoBoxRight.setContentType("text/html");
				infoBoxRight.addHyperlinkListener(linkListener);
				infoBoxRight.setText("Right-click a language on the map to show its information here.");
				infoScrollRight = new JScrollPane(infoBoxRight,
								ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
								ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

				Dimension topDim = new Dimension(infoPanel.getWidth(), (int) (infoPanel.getHeight() / 2.05) );
				Dimension bottomDim = new Dimension(infoPanel.getWidth(), (int) (infoPanel.getHeight() / 2.05) );
				infoScrollLeft.setPreferredSize(topDim);
				infoScrollRight.setPreferredSize(bottomDim);
				infoScrollLeft.setAlignmentY(TOP_ALIGNMENT);
				infoScrollRight.setAlignmentY(BOTTOM_ALIGNMENT);
				
				BoxLayout box = new BoxLayout(infoPanel, BoxLayout.Y_AXIS);
				
				infoPanel.setLayout(box);
				infoPanel.add(infoScrollLeft);
				infoPanel.add(infoScrollRight);
			}
		});
	}
	
	/**
	 * instantiate a SelectionTracker when ready and deploy components in
	 * controlBox on the event-dispatch thread
	 */
	public void initiateControlBoxContents() {
		selectionTracker = new SelectionTracker(map);
		final List<JCheckBox> famCheckBoxes = selectionTracker.getFamilyCheckBoxes();
		final List<JCheckBox> featureCheckBoxes = selectionTracker.getFeatureCheckBoxes();
		final List<JCheckBox> langCheckBoxes = selectionTracker.getLanguageCheckBoxes();
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for (JCheckBox cb : famCheckBoxes) {
					familyControlPanel.add(cb);
				}
				for (JCheckBox cb : featureCheckBoxes) {
					featureControlPanel.add(cb);
				}
				for (JCheckBox cb : langCheckBoxes) {
					languageControlPanel.add(cb);
				}
				validate();
			}
		});
	}

	//accessors
	public JPanel getInfoPanel() { return infoPanel; }
	public JTabbedPane getControlBox() { return controlBox; }
	public InfoBox getInfoBoxLeft() { return infoBoxLeft; }
	public InfoBox getInfoBoxRight() { return infoBoxRight; }
	public JScrollPane getInfoScrollLeft() { return infoScrollLeft; }
	public JScrollPane getInfoScrollRight() { return infoScrollRight; }
	public void setMap(Map m) { map = m; }

	/**
	 * for thread safety, this setter is invoked on Swing's event thread
	 */
	public void setInfoBoxLeftText(String s) {
		final String _s = s;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				infoBoxLeft.setText(_s);
				infoBoxLeft.setCaretPosition(0);
			}
		});
	}

	/**
	 * for thread safety, this setter is invoked on Swing's event thread
	 */
	public void setInfoBoxRightText(String s) {
		final String _s = s;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				infoBoxRight.setText(_s);
				infoBoxRight.setCaretPosition(0);
			}
		});
	}
}
