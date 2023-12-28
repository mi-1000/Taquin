package main.java.model;

import java.io.Serializable;
import java.util.Objects;

public class Case implements Serializable, Cloneable {

	public final static int INDEX_CASE_VIDE = -1;
	private int index;
	private byte[] image;

	/**
	 * 
	 * @param index : index de la case
	 * @param image : image de la case
	 */
	public Case(int index, byte[] image) {
		this.index = index;
		this.image = image;
	}

	/**
	 * 
	 * @param index : index de la case
	 */
	public Case(int index) {
		this.index = index;
	}

	public void setImage(byte[] img) {
		this.image = img;
	}

	public int getIndex() {
		return this.index;
	}

	public byte[] getImage() {
		return this.image;
	}

	@Override
	public String toString() {
		return "[" + this.index + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(index);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Case other = (Case) obj;
		return index == other.index;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		if (this.image == null) {
			return super.clone();
		}
		Case caseClonee = new Case(this.index, image.clone());
		return caseClonee;
	}
}
