package aero.alestis.stresstools.ancolab.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AncolabMaterial implements Serializable{
	private String name;
	private String type;
	private String library;
	private String thickness;
	private String e1;
	private String e2;
	private String g12;
	private String nu12;
	
	public AncolabMaterial(String name, String library, String type, String thickness, String e1, String e2, String g12, String nu12) {
		this.name= name;
		this.type= type;
		this.thickness = thickness;
		this.setLibrary(library);
		this.e1= e1;
		this.e2= e2;
		this.g12= g12;
		this.nu12= nu12;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getThickness() {
		return thickness;
	}

	public void setThickness(String thickness) {
		this.thickness = thickness;
	}

	public String getE1() {
		return e1;
	}

	public void setE1(String e1) {
		this.e1 = e1;
	}

	public String getE2() {
		return e2;
	}

	public void setE2(String e2) {
		this.e2 = e2;
	}

	public String getG12() {
		return g12;
	}

	public void setG12(String g12) {
		this.g12 = g12;
	}

	public String getNu12() {
		return nu12;
	}

	public void setNu12(String nu12) {
		this.nu12 = nu12;
	}

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}



}

