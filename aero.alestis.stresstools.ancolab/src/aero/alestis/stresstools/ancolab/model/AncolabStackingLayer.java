package aero.alestis.stresstools.ancolab.model;

public class AncolabStackingLayer implements Cloneable {
	private String name;
	private String library;
	private String type;
	private String thickness;
	private String angle;
		
	public AncolabStackingLayer(String name, String library, String type, String thickness) {
		this.name = name;
		this.library = library;
		this.type = type;
		this.thickness = thickness;
		this.angle = "";
	}
	public AncolabStackingLayer() {
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
	public String getAngle() {
		return angle;
	}
	public void setAngle(String angle) {
		this.angle = angle;
	}
	public String getLibrary() {
		return library;
	}
	public void setLibrary(String library) {
		this.library = library;
	}
	
    public Object clone(){
        Object obj=null;
        try{
            obj=super.clone();
        }catch(CloneNotSupportedException ex){
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }

}
