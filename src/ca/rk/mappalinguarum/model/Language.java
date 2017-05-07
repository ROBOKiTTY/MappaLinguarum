package ca.rk.mappalinguarum.model;

import java.util.ArrayList;
import java.util.List;

import ca.rk.mappalinguarum.model.phoneme.PhonemeInventory;


/**
 * Encapsulates a language
 * 
 * @author RK
 */
public class Language {

	private List<String> names;
	private List<Feature> features;
	private List<LanguageFamily> families;
	private List<Dialect> dialects;
	private PhonemeInventory phonemeInventory;
	private Location location;
	private String information;
	private String html;
	private List<String> links;
	
	/**
	 * constructs a Language with empty fields
	 */
	public Language() {
		names = new ArrayList<String>();
		features = new ArrayList<Feature>();
		families = new ArrayList<LanguageFamily>();
		dialects = new ArrayList<Dialect>();
		phonemeInventory = null;
		information = "";
		links = new ArrayList<String>();
		html = null;
	}
	
	/**
	 * add a name to the Language if it's not null/empty or already present
	 * 
	 * @param name a name
	 */
	public void addName(String name) {
		if (name == null || name.isEmpty() || names.contains(name) ) {
			return;
		}
		names.add(name);
	}
	
	/**
	 * add a Dialect to the Language if it's not null/empty or already present
	 * 
	 * @param d a Dialect
	 */
	public void addDialect(Dialect d) {
		if (d == null || dialects.contains(d) ) {
			return;
		}
		dialects.add(d);
	}
	
	/**
	 * add a Feature to the language if it's not null or already present
	 * 
	 * @param f the input Feature to be added
	 */
	public void addFeature(Feature f) {
		if (f == null || features.contains(f) ) {
			return;
		}
		features.add(f);
	}
	
	/**
	 * add a LanguageFamily to the language if it's not null or already present
	 * 
	 * @param f the input LanguageFamily to be added
	 */
	public void addFamily(LanguageFamily lf) {
		if (lf == null || features.contains(lf) ) {
			return;
		}
		families.add(lf);
	}
	
	/**
	 * add a link to a language
	 * Modifies: this
	 * Effect: add input String to this.links
	 * @param s input String to add
	 */
	public void addLink(String s) {
		links.add(s);
	}
	
	/**
	 * @return a HTML message listing all pertinent information
	 */
	public String getHTML() {
		if (html == null) {
			StringBuilder buildar = new StringBuilder();
			buildar.append("<u>Common name</u>: " + getCommonName() );
			buildar.append("<br />");
			if (names.size() > 1) {
				buildar.append("<u>Other names</u>: ");
				int i = 1;
				while (i < names.size() ) {
					buildar.append( names.get(i) );
					if (names.size() - i > 1) {
						buildar.append(", ");
					}
					++i;
				}
				buildar.append("<br />");
			}
			buildar.append("<u>Family</u>: " + getUrFamily().getName() );
			buildar.append("<br />");
			
			buildar.append("<u>Features</u>: ");
			int i = 0;
			for (Feature f : features) {
				buildar.append(f.getName() );
				if (features.size() - i > 1) {
					buildar.append("; ");
				}
				++i;
			}
			buildar.append("<br />");
			
			buildar.append("<p>" + information + "</p>");
			buildar.append("<br />");
			
			if ( !links.isEmpty() ) {
				buildar.append("<u>Links</u>: ");
				for (String s : links) {
					buildar.append("<a href=\"" + s + "\">" + s + "</a>");
					buildar.append("<br />");
				}
			}
			
			html = buildar.toString();
		}
		StringBuilder buildar = new StringBuilder();
		buildar.append(html);
		buildar.append( phonemeInventory.getHTML() );
		if ( !dialects.isEmpty() ) {
			buildar.append("<br />");
			buildar.append("<u>Dialects</u>");
			int i = 1;
			for (Dialect dialect : dialects) {
				buildar.append("<br />" + i + ".");
				buildar.append("<br />");
				buildar.append( dialect.getHTML() );
				++i;
			}
		}
		
		return buildar.toString();
	}
	/**
	 * generated by Eclipse
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((names == null) ? 0 : names.hashCode());
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
		if (obj == null || !(obj instanceof Language) ) {
			return false;
		}

		Language other = (Language) obj;
		if (location == null) {
			if (other.location != null) {
				return false;
			}
		} else if (!location.equals(other.location)) {
			return false;
		}
		if (names == null) {
			if (other.names != null) {
				return false;
			}
		} else if (!names.equals(other.names)) {
			return false;
		}
		return true;
	}

	/**
	 * get the common, i.e. Anglicized name for this language if it exists;
	 * this will be the first element in the name list
	 * 
	 * @return the first element in this.names, null if this.names is empty
	 */
	public String getCommonName() {
		if (names.size() > 0) {
			return names.get(0);
		}
		return null;
	}
	
	/**
	 * get the highest-level language family for this language if it exists
	 * 
	 * @return the first element in this.families, null if this.families is empty
	 */
	public LanguageFamily getUrFamily() {
		if (families.size() > 0) {
			return families.get(0);
		}
		return null;
	}
	
	//accessors
	public List<String> getNames() { return names; }
	public List<Feature> getFeatures() { return features; }
	public List<LanguageFamily> getFamilies() { return families; }
	public List<Dialect> getDialects() { return dialects; }
	public Location getLocation() { return location; }
	public String getInformation() { return information; }
	public List<String> getLinks() {return links; }
	public PhonemeInventory getPhonemeInventory() { return phonemeInventory; }
	
	public void setLocation(Location l) { location = l; }
	public void setInformation(String s) { information = s; }
	public void setPhonemeInventory(PhonemeInventory pi) { phonemeInventory = pi; }

}
