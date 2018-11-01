package nattable05filter.parts;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.TextCellEditor;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.swt.SWT;

import aero.alestis.stresstools.ancolab.model.AncolabStackingLayer;

public class StackingNatTableEditConfiguration extends AbstractRegistryConfiguration {
	private IDataProvider rowDataProvider;

	public StackingNatTableEditConfiguration(IConfigRegistry configRegistry, IDataProvider rowHeaderDataProvider) {
		
		rowDataProvider = (IDataProvider) rowHeaderDataProvider;
		
	}


	@Override
	public void configureRegistry(IConfigRegistry configRegistry) {
        configRegistry.registerConfigAttribute(
                EditConfigAttributes.CELL_EDITABLE_RULE,
                IEditableRule.ALWAYS_EDITABLE);

		configRegistry.registerConfigAttribute(
				EditConfigAttributes.CELL_EDITABLE_RULE, 
				new ThicknessEditableRule(rowDataProvider), 
				DisplayMode.EDIT, 
				"thickness");
		configRegistry.registerConfigAttribute(
				EditConfigAttributes.CELL_EDITOR, 
				new TextCellEditor(), 
				DisplayMode.EDIT, 
				"myCheckboxLabel");	
		
		TextCellEditor textCellEditor = new TextCellEditor();
		textCellEditor.setErrorDecorationEnabled(true);
		textCellEditor.setErrorDecorationText(
			"Security Id must be 3 alpha characters optionally followed by numbers");
		textCellEditor.setDecorationPositionOverride(SWT.LEFT | SWT.TOP);

		configRegistry.registerConfigAttribute(
			EditConfigAttributes.CELL_EDITOR,
			textCellEditor,
			DisplayMode.NORMAL,
			"thickness");
		
	}

	
	private class ThicknessEditableRule implements IEditableRule{
		private IDataProvider data;
		public ThicknessEditableRule(IDataProvider thedata) {
			this.data= thedata;
		}
		@Override
		public boolean isEditable(ILayerCell cell, IConfigRegistry configRegistry) {
			
			 //this.bodyDataProvider.getRowObject(rowIndex);
			return false;
		}

		@Override
		public boolean isEditable(int columnIndex, int rowIndex) {
			AncolabStackingLayer o = (AncolabStackingLayer) data.getDataValue(columnIndex, rowIndex);
			if(columnIndex == 2 && o.getType().equals("Honeycomb"))
				return true;
			return false;
		}
		
	}

}
