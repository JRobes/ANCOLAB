package stackingtable;

import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.validate.DataValidator;

import aero.alestis.stresstools.ancolab.model.AncolabStackingLayer;

public class ThicknessDataValidator extends DataValidator {
	IRowDataProvider<?> bodyDataProvider;
	public ThicknessDataValidator(IRowDataProvider<?> bodyDataProv) {
		this.bodyDataProvider = bodyDataProv;
	}
	@Override
	public boolean validate(int columnIndex, int rowIndex, Object newValue) {
		AncolabStackingLayer rowObject = (AncolabStackingLayer) this.bodyDataProvider.getRowObject(rowIndex);
       
		
		/*if(rowObject.getThickness().equals("2.0"))
    	   return false;
    	*/
		
		//Double.parseDouble(rowObject.getThickness());
		try {
			Double.parseDouble(rowObject.getThickness());
		}catch(NumberFormatException e) {
			//e.printStackTrace();
	        return false;
		}
		
		
		
		return true;
	}
}
