package aero.alestis.stresstools.composite.laminate;

public class HoneycombLayer extends AbstractLaminateLayer {

	public HoneycombLayer(IMaterial4LaminateLayer mat, String thick) {
		super(mat, thick, null);
		this.setMatType("Honeycomb");
	}

	@Override
	public boolean isOrientationRequired() {
		return false;
	}


}
