package aero.alestis.stresstools.ancolab.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "key")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Key {
	private String name, value, type, unit;

	public String getName() {
		return name;
	}
	@XmlAttribute
	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}
	@XmlAttribute
	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}
	@XmlAttribute
	public void setType(String type) {
		this.type = type;
	}

	public String getUnit() {
		return unit;
	}
	@XmlAttribute
	public void setUnit(String unit) {
		this.unit = unit;
	}
}
