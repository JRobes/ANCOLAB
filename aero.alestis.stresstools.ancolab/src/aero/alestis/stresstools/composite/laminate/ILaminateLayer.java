package aero.alestis.stresstools.composite.laminate;

public interface ILaminateLayer {
	IMaterial4LaminateLayer getMaterial4LaminateLayer();
	String getOrientation();
	String getThickness();
	boolean isOrientationRequired();
	
}
