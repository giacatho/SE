/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2b.indexer.model;

/**
 *
 * @author nguyentritin
 */
public class YearVenue {
	String year;
	String venue;

	public YearVenue(String year, String venue) {
		this.year = year;
		this.venue = venue;
	}
	
	public String getYear() {
		return year;
	}

	public String getVenue() {
		return venue;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		YearVenue other = (YearVenue) obj;
		return year.equalsIgnoreCase(other.year) && venue.equalsIgnoreCase(other.venue);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 89 * hash + (this.year != null ? this.year.hashCode() : 0);
		hash = 89 * hash + (this.venue != null ? this.venue.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return String.format("year:%s\nvenue%s\n", this.year, this.venue);
	}
}
