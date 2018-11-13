package aero.alestis.stresstools.composite.laminate;

public class CompositeLayer extends AbstractLaminateLayer {

	public CompositeLayer(IMaterial4LaminateLayer mat, String orient) {
		super(mat, null, orient);
		this.setThickness(mat.getThickness());
		this.setMatType("Composite");
	}

	@Override
	public boolean isOrientationRequired() {
		return true;
	}


}
