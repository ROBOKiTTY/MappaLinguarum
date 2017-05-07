package ca.rk.mappalinguarum.model.phoneme;

/**
 * abstract class encapsulating a phoneme and its associated information
 * 
 * @author RK
 *
 */

public abstract class Phoneme {

	private String ipaSymbol;
	
	/**
	 * constructs an empty phoneme
	 */
	public Phoneme() {
	}
	
	/**
	 * constructs a phoneme represented by the input IPA symbol
	 * 
	 * @param symbol an IPA symbol for this phoneme
	 */
	public Phoneme(String symbol) {
		ipaSymbol = symbol;
	}
	
	/**
	 * returns the IPA symbol for this phoneme
	 */
	@Override
	public String toString() {
		return ipaSymbol;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ipaSymbol == null) ? 0 : ipaSymbol.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Phoneme other = (Phoneme) obj;
		if (ipaSymbol == null) {
			if (other.ipaSymbol != null)
				return false;
		} else if (!ipaSymbol.equals(other.ipaSymbol))
			return false;
		return true;
	}
	
	public String getIPASymbol() { return ipaSymbol; }
	
	public void setIPASymbol(String s) { ipaSymbol = s; }
}
