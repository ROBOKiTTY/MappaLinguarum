package ca.rk.mappalinguarum.model.phoneme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * represents a Language's phonemic inventory
 * 
 * internally, consonants are organized by place of articulation, and vowels by vowel frontness
 * 
 * this class supports for-each for consonants and vowels, using eachConsonant() and eachVowel()
 * 
 * @author RK
 *
 */

public class PhonemeInventory {
	
	private static Map<String, String> shortToFullDictionary;
	
	private List<Consonant> consonants;
	private List<Vowel> vowels;
	
	private boolean isOrganized;
	private Map<PlaceOfArticulation, List<Consonant> > nasals, plosives, fricatives, affricates,
														approximants, trills, flaps, latFricatives,
														latApproximants, latFlaps, coFricatives,
														coApproximants, coStops, clicks, implosives;
	private Map<VowelFrontness, List<Vowel> > highVowels, nearHighVowels, midHighVowels, midVowels,
								midLowVowels, nearLowVowels, lowVowels;
	private Map<MannerOfArticulation, Map<PlaceOfArticulation, List<Consonant> > > moaLookupDictionary;
	private Map<VowelHeight, Map<VowelFrontness, List<Vowel> > > vheightLookupDictionary;
	private boolean isEmpty;
	
	/**
	 * construct a PhonemeInventory with empty fields 
	 */
	public PhonemeInventory() {
		isOrganized = false;
		isEmpty = true;
		consonants = new ArrayList<Consonant>();
		vowels = new ArrayList<Vowel>();
		
		nasals = new HashMap<PlaceOfArticulation, List<Consonant> >();
		plosives = new HashMap<PlaceOfArticulation, List<Consonant> >();
		fricatives = new HashMap<PlaceOfArticulation, List<Consonant> >();
		affricates = new HashMap<PlaceOfArticulation, List<Consonant> >();
		approximants = new HashMap<PlaceOfArticulation, List<Consonant> >();
		trills = new HashMap<PlaceOfArticulation, List<Consonant> >();
		flaps = new HashMap<PlaceOfArticulation, List<Consonant> >();
		latFricatives = new HashMap<PlaceOfArticulation, List<Consonant> >();
		latApproximants = new HashMap<PlaceOfArticulation, List<Consonant> >();
		latFlaps = new HashMap<PlaceOfArticulation, List<Consonant> >();
		coFricatives = new HashMap<PlaceOfArticulation, List<Consonant> >();
		coApproximants = new HashMap<PlaceOfArticulation, List<Consonant> >();
		coStops = new HashMap<PlaceOfArticulation, List<Consonant> >();
		clicks = new HashMap<PlaceOfArticulation, List<Consonant> >();
		implosives = new HashMap<PlaceOfArticulation, List<Consonant> >();
		
		highVowels = new HashMap<VowelFrontness, List<Vowel> >();
		nearHighVowels = new HashMap<VowelFrontness, List<Vowel> >();
		midHighVowels = new HashMap<VowelFrontness, List<Vowel> >();
		midVowels = new HashMap<VowelFrontness, List<Vowel> >();
		midLowVowels = new HashMap<VowelFrontness, List<Vowel> >();
		nearLowVowels = new HashMap<VowelFrontness, List<Vowel> >();
		lowVowels = new HashMap<VowelFrontness, List<Vowel> >();
		
		moaLookupDictionary = new HashMap<MannerOfArticulation, Map<PlaceOfArticulation, List<Consonant> > >();
		vheightLookupDictionary = new HashMap<VowelHeight, Map<VowelFrontness, List<Vowel> > >();
		
		moaLookupDictionary.put(MannerOfArticulation.Nasal, nasals);
		moaLookupDictionary.put(MannerOfArticulation.Plosive, plosives);
		moaLookupDictionary.put(MannerOfArticulation.Fricative, fricatives);
		moaLookupDictionary.put(MannerOfArticulation.Affricate, affricates);
		moaLookupDictionary.put(MannerOfArticulation.Approximant, approximants);
		moaLookupDictionary.put(MannerOfArticulation.Trill, trills);
		moaLookupDictionary.put(MannerOfArticulation.Flap_tap, flaps);
		moaLookupDictionary.put(MannerOfArticulation.Lateral_Fricative, latFricatives);
		moaLookupDictionary.put(MannerOfArticulation.Lateral_Approximant, latApproximants);
		moaLookupDictionary.put(MannerOfArticulation.Lateral_Flap, latFlaps);
		moaLookupDictionary.put(MannerOfArticulation.Coarticulated_Fricative, coFricatives);
		moaLookupDictionary.put(MannerOfArticulation.Coarticulated_Approximant, coApproximants);
		moaLookupDictionary.put(MannerOfArticulation.Coarticulated_Stop, coStops);
		moaLookupDictionary.put(MannerOfArticulation.Click, clicks);
		moaLookupDictionary.put(MannerOfArticulation.Implosive, implosives);
		vheightLookupDictionary.put(VowelHeight.High, highVowels);
		vheightLookupDictionary.put(VowelHeight.Near_High, nearHighVowels);
		vheightLookupDictionary.put(VowelHeight.Mid_High, midHighVowels);
		vheightLookupDictionary.put(VowelHeight.Mid, midVowels);
		vheightLookupDictionary.put(VowelHeight.Mid_Low, midLowVowels);
		vheightLookupDictionary.put(VowelHeight.Near_Low, nearLowVowels);
		vheightLookupDictionary.put(VowelHeight.Low, lowVowels);
	}
	
	/**
	 * add a consonant to the inventory if it's not null or already present
	 * 
	 * @param c a Consonant
	 */
	public void addPhoneme(Consonant c) {
		if (c == null || consonants.contains(c) ) {
			return;
		}
		
		if (isEmpty) {
			isEmpty = false;
		}
		
		consonants.add(c);
	}
	
	/**
	 * add a vowel to the inventory if it's not null or already present
	 * 
	 * @param v a Vowel
	 */
	public void addPhoneme(Vowel v) {
		if (v == null || vowels.contains(v) ) {
			return;
		}
		
		if (isEmpty) {
			isEmpty = false;
		}
		
		vowels.add(v);
	}

	/**
	 * allows for-each on consonants
	 * 
	 * @return an iterator for consonants
	 */
	public Iterator<Consonant> eachConsonant() {
		return consonants.iterator();
	}
	
	/**
	 * allows for-each on vowels
	 * 
	 * @return an iterator for vowels
	 */
	public Iterator<Vowel> eachVowel() {
		return vowels.iterator();
	}
	
	/**
	 * internally organize phonemes into lists by manner of articulation / vowel height
	 */
	private void organize() {
		for (Consonant c : consonants) {
			final PlaceOfArticulation poa = c.getPlaceOfArticulation();
			final MannerOfArticulation moa = c.getMannerOfArticulation();
			addToMap(moa, poa, c);
			if (c.getSecondaryPOA() != null) {
				addToMap(moa, c.getSecondaryPOA(), c);
			}
		}
		
		for (Vowel v : vowels) {
			final VowelFrontness frontness = v.getFrontness();
			final VowelHeight height = v.getHeight();
			addToMap(height, frontness, v);
		}
		
		isOrganized = true;
	}
	
	/**
	 * add an element of a list value to a specified Map using specified key
	 * 
	 * @param mapKey key to the map to use
	 * @param poa a key
	 * @param c an element to be added to the list value
	 */
	private void addToMap(MannerOfArticulation mapKey, PlaceOfArticulation poa, Consonant c) {
		Map<PlaceOfArticulation, List<Consonant> > map = moaLookupDictionary.get(mapKey);
		List<Consonant> list;
		
		if ( map.containsKey(poa) ) {
			list = map.get(poa);
			if (list == null) {
				list = new ArrayList<Consonant>();
			}
			list.add(c);
		}
		else {
			list = new ArrayList<Consonant>();
			list.add(c);
			map.put(poa, list);
		}
	}

	/**
	 * add an element of a list value to a specified Map using specified key
	 * 
	 * @param mapKey key to the map to use
	 * @param vf a key
	 * @param v an element to be added to the list value
	 */
	private void addToMap(VowelHeight mapKey, VowelFrontness vf, Vowel v) {
		Map<VowelFrontness, List<Vowel> > map = vheightLookupDictionary.get(mapKey);
		List<Vowel> list;
		
		if ( map.containsKey(vf) ) {
			list = map.get(vf);
			if (list == null) {
				list = new ArrayList<Vowel>();
			}
			list.add(v);
		}
		else {
			list = new ArrayList<Vowel>();
			list.add(v);
			map.put(vf, list);
		}
	}
	
	/**
	 * build a HTML table of the PhonemeInventory
	 * 
	 * @return a HTML table as a string, empty string if there's no data
	 */
	public String getHTML() {
		if (isEmpty) {
			return "";
		}
		
		if (!isOrganized) {
			this.organize();
		}
		
		StringBuilder buildar = new StringBuilder();
		
		buildar.append("<u>Phonemes</u>: <br />");
		buildar.append("<table border='1' cellspacing='0' cellpadding='1'>");
			buildar.append("<tr>");
				buildar.append("<th>&nbsp;</th>");
				for (PlaceOfArticulation poa : PlaceOfArticulation.values() ) {
					buildar.append("<th><span alt='" + poa.getFullName() + "' title='" +
									poa.getFullName() + "'>" + poa.toString() + "</span></th>");
				}
			buildar.append("</tr>");
			
			Map<PlaceOfArticulation, List<Consonant> > poaToConsonantMap;
			List<Consonant> consonantList;
			for (MannerOfArticulation moa : MannerOfArticulation.values() ) {
				poaToConsonantMap = moaLookupDictionary.get(moa);
				//skip empty rows for consonants
				if ( poaToConsonantMap.isEmpty() ) {
					continue;
				}
				buildar.append("<tr>");
					buildar.append("<th><span alt='" + moa.getFullName() + "' title='" +
									moa.getFullName() + "'>" + moa.toString() + "</span></th>");
					for (PlaceOfArticulation poa : PlaceOfArticulation.values() ) {
						consonantList = poaToConsonantMap.get(poa);
						buildar.append("<td>");
						if ( consonantList == null || consonantList.isEmpty() ) {
							buildar.append("&nbsp;");
						}
						else {
							Iterator<Consonant> iterator = consonantList.iterator();
							while ( iterator.hasNext() ) {
								buildar.append( iterator.next().toString() );
								if ( iterator.hasNext() ) {
									buildar.append("&nbsp;");
								}
							}
						}
						buildar.append("</td>");
					}
				buildar.append("</tr>");
			}
			
		buildar.append("</table>");
		buildar.append("<br />");

		buildar.append("<table cellspacing='3' cellpadding='2'>");
			buildar.append("<tr>");
				buildar.append("<th>&nbsp;</th>");
				buildar.append("<th>&nbsp;</th>");
				for (VowelFrontness vfrontness : VowelFrontness.values() ) {
					buildar.append("<th><span alt='" + vfrontness.getFullName() + "' title='" +
									vfrontness.getFullName() + "'>" + vfrontness.toString() + "</span></th>");
				}
			buildar.append("</tr>");
		
			Map<VowelFrontness, List<Vowel> > vfrontnessToVowelMap;
			List<Vowel> vowelList;
			for (VowelHeight vheight : VowelHeight.values() ) {
				vfrontnessToVowelMap = vheightLookupDictionary.get(vheight);
				//do not skip empty rows for vowels
				buildar.append("<tr>");
					buildar.append("<th><span alt'" + vheight.getFullName() + "' title='" +
									vheight.getFullName() + "'>" + vheight.toString() + "</span></th>");
					buildar.append("<td>&nbsp;</td>");
					for (VowelFrontness vfrontness : VowelFrontness.values() ) {
						vowelList = vfrontnessToVowelMap.get(vfrontness);
						buildar.append("<td>");
						if (vowelList == null || vowelList.isEmpty() ) {
							buildar.append("&nbsp;");
						}
						else {
							Iterator<Vowel> iterator = vowelList.iterator();
							while (iterator.hasNext() ) {
								buildar.append( iterator.next().toString() );
								if ( iterator.hasNext() ) {
									buildar.append("&nbsp;");
								}
							}
						}
						buildar.append("</td>");
					}
				buildar.append("</tr>");
			}
			
		buildar.append("</table><br />");
		
		return buildar.toString();
	}
	
	/**
	 * build a map of table headers with abbreviated short names as keys and full names as values,
	 * then use input key to fish out the value; returns null if key is invalid  
	 * 
	 * @param shortName a short name as a key
	 * @return a value
	 */
	public static String getFullName(String shortName) {
		if (shortToFullDictionary == null) {
			shortToFullDictionary = new HashMap<String, String>();
			for (VowelFrontness vf : VowelFrontness.values() ) {
				shortToFullDictionary.put(vf.toString(), vf.getFullName() );
			}
			for (VowelHeight vh : VowelHeight.values() ) {
				shortToFullDictionary.put(vh.toString(), vh.getFullName() );
			}
			for (PlaceOfArticulation poa : PlaceOfArticulation.values() ) {
				shortToFullDictionary.put(poa.toString(), poa.getFullName() );
			}
			for (MannerOfArticulation moa : MannerOfArticulation.values() ) {
				shortToFullDictionary.put(moa.toString(), moa.getFullName() );
			}
		}
		return shortToFullDictionary.get(shortName);
	}
	
	public boolean isEmpty() { return isEmpty; }
	public List<Consonant> getConsonants() { return consonants; }
	public List<Vowel> getVowels() { return vowels; }
}
