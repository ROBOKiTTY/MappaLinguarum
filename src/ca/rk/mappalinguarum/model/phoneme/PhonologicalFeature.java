package ca.rk.mappalinguarum.model.phoneme;

/**
 * a feature describes a phonological property of a phoneme;
 * this interface serves as a base for MannerOfArticulation, PlaceOfArticulation,
 * VowelFrontness, VowelHeight
 * 
 * @author RK
 *
 */
public interface PhonologicalFeature {
	/**
	 * returns a string representation
	 */
	public String toString();
	
	public String getFullName();
}
