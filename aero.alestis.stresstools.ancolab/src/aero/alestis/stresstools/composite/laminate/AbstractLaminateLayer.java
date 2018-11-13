package aero.alestis.stresstools.composite.laminate;

public abstract class AbstractLaminateLayer implements ILaminateLayer{
	private IMaterial4LaminateLayer material;
	private String thickness;
	private String orientation;
	private String matType;
	
	public AbstractLaminateLayer(IMaterial4LaminateLayer mat, String thick, String orient) {
		this.material    = mat;
		this.thickness   = thick;
		this.orientation = orient;
	}

	public String getMatType() {
		return matType;
	}
	protected void setMatType(String matType) {
		this.matType = matType;
	}

	public String getOrientation() {
		return orientation;
	}
	
	public String getThickness() {
		return thickness;
	}
	
	protected void setThickness(String thick) {
		this.thickness = thick;;
	}
	
	
	public IMaterial4LaminateLayer getMaterial4LaminateLayer() {
		return material;
	}
	
	
}
