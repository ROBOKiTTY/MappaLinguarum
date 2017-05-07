package ca.rk.mappalinguarum.model;

import java.util.ArrayList;
import java.util.List;

import ca.rk.mappalinguarum.model.phoneme.PhonemeInventory;


/**
 * encapsulates a dialect, here defined as a variety of a language that is mutually
 * intelligible with other dialects of the same language
 * 
 * @author RK
 *
 */

public class Dialect {

	private List<String> names;
	private List<String> links;
	private Language parentLanguage;
	private PhonemeInventory phonemeInventory;
	private String information;
	private String html;
	
	/**
	 * constructs an empty Dialect belonging to the input language
	 */
	public Dialect(Language lang) {
		names = new ArrayList<String>();
		links = new ArrayList<String>();
		parentLanguage = lang;
		phonemeInventory = null;
		information = "";
		html = null;
	}
	
	/**
	 * add a name to the Dialect if it's not null/empty or already present
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
	 * add a link to the Dialect if it's not null/empty or already present
	 * 
	 * @param link a link
	 */
	public void addLink(String link) {
		if (link == null || link.isEmpty() || links.contains(link) ) {
			return;
		}
		names.add(link);
	}

	/**
	 * Eclipse-generated
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((names == null) ? 0 : names.hashCode());
		result = prime * result
				+ ((parentLanguage == null) ? 0 : parentLanguage.hashCode());
		return result;
	}

	/**
	 * Eclipse-generated
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dialect other = (Dialect) obj;
		if (names == null) {
			if (other.names != null)
				return false;
		} else if (!names.equals(other.names))
			return false;
		if (parentLanguage == null) {
			if (other.parentLanguage != null)
				return false;
		} else if (!parentLanguage.equals(other.parentLanguage))
			return false;
		return true;
	}
	
	public String getHTML() {
		if (html == null) {
			StringBuilder buildar = new StringBuilder();
			if (names.size() > 1) {
				buildar.append("<u>Names</u>: ");
			}
			else {
				buildar.append("<u>Name</u>: ");
			}
	
			int i = 0;
			while (i < names.size() ) {
				buildar.append( names.get(i) );
				++i;
				if (names.size() - i > 0) {
					buildar.append(", ");
				}
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
		
		if ( phonemeInventory.isEmpty() ) {
			return html;
		}
		else {
			return html + phonemeInventory.getHTML();
		}
	}

	public List<String> getNames() { return names; }
	public List<String> getLinks() { return links; }
	public Language getParentLanguage() { return parentLanguage; }
	public PhonemeInventory getPhonemeInventory() { return phonemeInventory; }
	public String getInformation() { return information; }
	
	public void setNames(List<String> list) { names = list; }
	public void setPhonemeInventory(PhonemeInventory pi) { phonemeInventory = pi; }
	public void setInformation(String s) { information = s; }
}
