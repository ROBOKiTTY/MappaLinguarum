package ca.rk.mappalinguarum.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JCheckBox;

import ca.rk.mappalinguarum.model.Feature;
import ca.rk.mappalinguarum.model.LanguageFamily;
import ca.rk.mappalinguarum.model.Location;
import ca.rk.mappalinguarum.model.MapData;


/**
 * handles selectable checkboxes in the Control Panel, tracking their status
 * 
 * @author RK
 *
 */
public class SelectionTracker implements ItemListener {

	private ca.rk.mappalinguarum.ui.Map map;
	private List<JCheckBox> familyCheckBoxes;
	private List<JCheckBox> featureCheckBoxes;
	private List<JCheckBox> languageCheckBoxes;
	//private List<JCheckBox> phonemeCheckBoxes;	//phoneme not implemented yet
	
	private java.util.Map<JCheckBox, LanguageFamily> familyDictionary;
	private java.util.Map<JCheckBox, Feature> featureDictionary;
	private java.util.Map<JCheckBox, Location> langLocDictionary;
	
	/**
	 * constructs a SelectionTracker associated with input Map object and creates
	 * components from parsed data
	 * 
	 * @param m the Map object this is for
	 */
	public SelectionTracker(ca.rk.mappalinguarum.ui.Map m) {
		map = m;
		createComponents(map.getData() );
	}
	
	/**
	 * load information from input MapData into memory and create checkboxes
	 * 
	 * @param data the MapData object to extract information from
	 */
	private void createComponents(MapData data) {
		List<LanguageFamily> fams = data.getAllFamilies();
		familyCheckBoxes = new ArrayList<JCheckBox>(fams.size() );
		familyDictionary = new HashMap<JCheckBox, LanguageFamily>(fams.size() );
		for (LanguageFamily anLF : fams) {
			JCheckBox lfCheckBox = new JCheckBox(anLF.toString(), false);
			lfCheckBox.addItemListener(this);
			familyDictionary.put(lfCheckBox, anLF);
			familyCheckBoxes.add(lfCheckBox);
		}
				
		List<Feature> features = data.getAllFeatures();
		featureCheckBoxes = new ArrayList<JCheckBox>(features.size() );
		featureDictionary = new HashMap<JCheckBox, Feature>(features.size() );
		for (Feature aFeature : features) {
			JCheckBox fCheckBox = new JCheckBox(aFeature.toString(), false);
			fCheckBox.addItemListener(this);
			featureDictionary.put(fCheckBox, aFeature);
			featureCheckBoxes.add(fCheckBox);
		}
		
		List<Location> locs = data.getLocations();
		languageCheckBoxes = new ArrayList<JCheckBox>(locs.size() );
		langLocDictionary = new HashMap<JCheckBox, Location>(locs.size() );
		for (Location aLoc : locs) {
			JCheckBox langCheckBox = new JCheckBox(aLoc.getLanguage().getCommonName(), false);
			langCheckBox.addItemListener(this);
			langLocDictionary.put(langCheckBox, aLoc);
			languageCheckBoxes.add(langCheckBox);
		}
	}

	/**
	 * when a checkbox gets selected or deselected, change display on the map accordingly
	 * 
	 * @see ItemListener
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		JCheckBox changedItem = (JCheckBox) e.getSource();
		
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if ( familyCheckBoxes.contains(changedItem) ) {
				map.addSelected( familyDictionary.get(changedItem) );
			}
			else if ( featureCheckBoxes.contains(changedItem) ) {
				map.addSelected( featureDictionary.get(changedItem) );
			}
			else if ( languageCheckBoxes.contains(changedItem) ) {
				map.addSelected( langLocDictionary.get(changedItem) );
			}
		}
		else if (e.getStateChange() == ItemEvent.DESELECTED) {
			if ( familyCheckBoxes.contains(changedItem) ) {
				map.removeSelected( familyDictionary.get(changedItem) );
			}
			else if ( featureCheckBoxes.contains(changedItem) ) {
				map.removeSelected( featureDictionary.get(changedItem) );
			}
			else if ( languageCheckBoxes.contains(changedItem) ) {
				map.removeSelected( langLocDictionary.get(changedItem) );
			}
		}
	}

	//accessors
	public List<JCheckBox> getFamilyCheckBoxes() { return familyCheckBoxes; }
	public List<JCheckBox> getFeatureCheckBoxes() { return featureCheckBoxes; }
	public List<JCheckBox> getLanguageCheckBoxes() { return languageCheckBoxes; }
	//public List<JCheckBox> getPhonemeCheckBoxes() { return phonemeCheckBoxes; } //not implemented yet
}
