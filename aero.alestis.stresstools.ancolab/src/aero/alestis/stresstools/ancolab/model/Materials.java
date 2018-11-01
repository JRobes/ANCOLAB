
package aero.alestis.stresstools.ancolab.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Materials", namespace = "http://www.airbus.com/universal-format/material/library")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Materials {
	private List<Material> listOfMaterials = null;

	public List<Material> getListOfMaterials() {
		return listOfMaterials;
	}
	@XmlElement(name = "Material")
	public void setListOfMaterials(List<Material> listOfMaterials) {
		this.listOfMaterials = listOfMaterials;
	}


}
