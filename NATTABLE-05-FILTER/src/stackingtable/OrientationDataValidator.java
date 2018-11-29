package stackingtable;

import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.validate.DataValidator;

import aero.alestis.stresstools.ancolab.model.AncolabStackingLayer;

public class OrientationDataValidator extends DataValidator {
	IRowDataProvider<?> bodyDataProvider;
	public OrientationDataValidator(IRowDataProvider<?> bodyDataProv) {
		this.bodyDataProvider = bodyDataProv;
	}
	@Override
	public boolean validate(int columnIndex, int rowIndex, Object newValue) {
		AncolabStackingLayer rowObject = (AncolabStackingLayer) this.bodyDataProvider.getRowObject(rowIndex);
		try {
			Double.parseDouble(rowObject.getAngle());
		}catch(NumberFormatException e) {
	        return false;
		}
		if(Double.parseDouble(rowObject.getThickness()) < 0){
			return false;
		}
		return true;
	}

}
