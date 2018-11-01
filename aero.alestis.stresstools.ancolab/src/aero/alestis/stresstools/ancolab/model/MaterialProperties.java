package aero.alestis.stresstools.ancolab.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "materialProperties")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class MaterialProperties {
	private List<Properties> listOfMaterialProperties = null;

	MaterialProperties(){
		listOfMaterialProperties = new ArrayList<Properties>();
	}
	public List<Properties> getListOfMaterialProperties() {
		return listOfMaterialProperties;
	}
	@XmlElement(name = "properties")
	public void setListOfMaterialProperties(List<Properties> listOfMaterialProperties) {
		this.listOfMaterialProperties = listOfMaterialProperties;
	}
	

}
