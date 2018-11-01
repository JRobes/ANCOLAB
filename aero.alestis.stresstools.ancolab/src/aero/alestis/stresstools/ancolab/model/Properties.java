package aero.alestis.stresstools.ancolab.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "properties")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Properties {
	private String name;
	private String type;
	private String rangeType;
	private String TValue;
	private String TUnit;
	private List<Properties> listOfProperties;
	private List<Property> listOfProperty;
		
	public String getName() {
		return name;
	}
	@XmlAttribute(name = "name")
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	@XmlAttribute(name = "type")
	public void setType(String type) {
		this.type = type;
	}
	public String getRangeType() {
		return rangeType;
	}
	@XmlAttribute(name = "rangeType")
	public void setRangeType(String rangeType) {
		this.rangeType = rangeType;
	}
	public String getTValue() {
		return TValue;
	}
	@XmlAttribute(name = "TValue")
	public void setTValue(String tValue) {
		TValue = tValue;
	}
	
	public String getTUnit() {
		return TUnit;
	}
	@XmlAttribute(name = "TUnit")
	public void setTUnit(String tUnit) {
		TUnit = tUnit;
	}
	public List<Properties> getListOfProperties() {
		return listOfProperties;
	}
	@XmlElement(name = "properties")
	public void setListOfProperties(List<Properties> listOfProperties) {
		this.listOfProperties = listOfProperties;
	}
	public List<Property> getListOfProperty() {
		return listOfProperty;
	}
	@XmlElement(name = "property")
	public void setListOfProperty(List<Property> listOfProperty) {
		this.listOfProperty = listOfProperty;
	}


}
