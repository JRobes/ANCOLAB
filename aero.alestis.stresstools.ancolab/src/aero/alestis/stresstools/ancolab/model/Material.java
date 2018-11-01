package aero.alestis.stresstools.ancolab.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Material")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Material {
	private String type;
	private String materialName;
	private String specificationName;
	private String library;
	private String businessVersion;
	private String libraryVersion;
	private String referenceDoc;
	private String notes;
	private String aircraft;
	private String maturity;
	private String materialFamily;
	private String materialSubFamily;
	private MaterialCharacteristics materialCharacteristics;
	private MaterialProperties materialProperties;
	
	Material(){
		this.materialCharacteristics = new MaterialCharacteristics();
		this.materialProperties = new MaterialProperties();
	}

	public String getType() {
		return type;
	}
	@XmlAttribute
	public void setType(String type) {
		this.type = type;
	}
	public String getMaterialName() {
		return materialName;
	}
	@XmlElement(name = "materialName")
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getLibrary() {
		return library;
	}
	@XmlElement(name = "library")
	public void setLibrary(String library) {
		this.library = library;
	}
	public String getBusinessVersion() {
		return businessVersion;
	}
	@XmlElement(name = "businessVersion")
	public void setBusinessVersion(String businessVersion) {
		this.businessVersion = businessVersion;
	}
	public String getLibraryVersion() {
		return libraryVersion;
	}
	@XmlElement(name = "libraryVersion")
	public void setLibraryVersion(String libraryVersion) {
		this.libraryVersion = libraryVersion;
	}
	public String getReferenceDoc() {
		return referenceDoc;
	}
	@XmlElement(name = "referenceDoc")
	public void setReferenceDoc(String referenceDoc) {
		this.referenceDoc = referenceDoc;
	}
	public String getNotes() {
		return notes;
	}
	@XmlElement(name = "notes")
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getAircraft() {
		return aircraft;
	}
	@XmlElement(name = "aircraft")
	public void setAircraft(String aircraft) {
		this.aircraft = aircraft;
	}
	public String getMaturity() {
		return maturity;
	}
	@XmlElement(name = "maturity")
	public void setMaturity(String maturity) {
		this.maturity = maturity;
	}
	public String getMaterialFamily() {
		return materialFamily;
	}
	@XmlElement(name = "materialFamily")
	public void setMaterialFamily(String materialFamily) {
		this.materialFamily = materialFamily;
	}
	public String getMaterialSubFamily() {
		return materialSubFamily;
	}
	@XmlElement(name = "materialSubFamily")
	public void setMaterialSubFamily(String materialSubFamily) {
		this.materialSubFamily = materialSubFamily;
	}
	public String getSpecificationName() {
		return specificationName;
	}
	@XmlElement(name = "specificationName")
	public void setSpecificationName(String specificationName) {
		this.specificationName = specificationName;
	}
	public MaterialCharacteristics getMaterialCharacteristics() {
		return materialCharacteristics;
	}
	@XmlElement(name = "materialCharacteristics")
	public void setMaterialCharacteristics(MaterialCharacteristics materialCharacteristics) {
		this.materialCharacteristics = materialCharacteristics;
	}

	public MaterialProperties getMaterialProperties() {
		return materialProperties;
	}
	@XmlElement(name = "materialProperties")
	public void setMaterialProperties(MaterialProperties materialProperties) {
		this.materialProperties = materialProperties;
	}

}
