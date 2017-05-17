package ca.rk.mappalinguarum.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.rk.mappalinguarum.util.Colour;
import ca.rk.mappalinguarum.util.RandomColourGenerator;

/**
 * Encapsulates a location on the map, which can be made up of more than
 * one contiguous regions
 * 
 * @author RK
 */
public class Location {

	private List<LatLongSet> latlongSets;
	private Language language;
	private Colour colour;
	
	/**
	 * constructs an empty Location, assigning it a colour 
	 */
	public Location() {
		latlongSets = new ArrayList<LatLongSet>();
		language = null;
		colour = RandomColourGenerator.getInstance().generateColour();
	}
	
	/**
	 * add a set of latitudes and longitudes delineating a polygonal area
	 * 
	 * @throws AssertionError if longitudes and latitudes have unequal lengths
	 * @param longitudes array of double representing longitudes
	 * @param latitudes array of double representing latitudes
	 */
	public void addLatLongs(double[] longitudes, double[] latitudes) {
		assert(longitudes.length == latitudes.length);
		latlongSets.add(new LatLongSet(longitudes, latitudes));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colour == null) ? 0 : colour.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((latlongSets == null) ? 0 : latlongSets.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (!(obj instanceof Location)) { return false; }
		Location other = (Location) obj;
		if (colour == null)
		{
			if (other.colour != null) { return false; }
		}
		else if (!colour.equals(other.colour)) { return false; }
		if (language == null)
		{
			if (other.language != null) { return false; }
		}
		else if (!language.equals(other.language)) { return false; }
		if (latlongSets == null)
		{
			if (other.latlongSets != null) { return false; }
		}
		else if (!latlongSets.equals(other.latlongSets)) { return false; }
		return true;
	}

	//accessors
	public List<LatLongSet> getLatLongSets() { return latlongSets; }
	public Language getLanguage() { return language; }
	public Colour getColour() { return colour; }
	
	public Location setLanguage(Language language) { this.language = language; return this; }
	
	public class LatLongSet {
		private double[] longitudes;
		private double[] latitudes;
		
		public LatLongSet(double[] longs, double[] lats) {
			longitudes = longs;
			latitudes = lats;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + Arrays.hashCode(latitudes);
			result = prime * result + Arrays.hashCode(longitudes);
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj) { return true; }
			if (obj == null) { return false; }
			if (!(obj instanceof LatLongSet)) { return false; }
			LatLongSet other = (LatLongSet) obj;
			if (!getOuterType().equals(other.getOuterType())) { return false; }
			if (!Arrays.equals(latitudes, other.latitudes)) { return false; }
			if (!Arrays.equals(longitudes, other.longitudes)) { return false; }
			return true;
		}

		public double[] getLongitudes() { return longitudes; }
		public double[] getLatitudes() { return latitudes; }

		private Location getOuterType()
		{
			return Location.this;
		}
	}
}
