package stackingtable;

import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.TextCellEditor;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;

public class StackingTableConfiguration extends AbstractRegistryConfiguration {

	private IDataProvider bodyDataProvider;
	public StackingTableConfiguration(IDataProvider dp) {
		this.bodyDataProvider = dp;
	}
	
	@Override
	public void configureRegistry(IConfigRegistry configRegistry) {
    	Style cellStyle = new Style();
        cellStyle.setAttributeValue(
                CellStyleAttributes.BACKGROUND_COLOR,
                GUIHelper.COLOR_WHITE);
        configRegistry.registerConfigAttribute(
                CellConfigAttributes.CELL_STYLE, cellStyle,
                DisplayMode.NORMAL, StackingTable.COLUMN_ONE_LABEL);
        configRegistry.registerConfigAttribute(
                CellConfigAttributes.CELL_STYLE, cellStyle,
                DisplayMode.NORMAL, StackingTable.COLUMN_TWO_LABEL);
        configRegistry.registerConfigAttribute(
                CellConfigAttributes.CELL_STYLE, cellStyle,
                DisplayMode.NORMAL, StackingTable.COLUMN_THREE_LABEL);
        configRegistry.registerConfigAttribute(
                CellConfigAttributes.CELL_STYLE, cellStyle,
                DisplayMode.NORMAL, StackingTable.COLUMN_FOUR_LABEL);
        configRegistry.registerConfigAttribute(
                CellConfigAttributes.CELL_STYLE, cellStyle,
                DisplayMode.NORMAL, StackingTable.COLUMN_FIVE_LABEL);

    	
        configRegistry.registerConfigAttribute(
        		EditConfigAttributes.CELL_EDITABLE_RULE, 
        		new ThicknessEditableRule(bodyDataProvider, "Honeycomb", 4),
        		//IEditableRule.ALWAYS_EDITABLE, 
        		DisplayMode.EDIT, 
        		StackingTable.COLUMN_FIVE_LABEL);
       
        configRegistry.registerConfigAttribute(
        		EditConfigAttributes.CELL_EDITABLE_RULE, 
        		new ThicknessEditableRule(bodyDataProvider, "Composite", 3),
        		//IEditableRule.ALWAYS_EDITABLE, 
        		DisplayMode.EDIT, 
        		StackingTable.COLUMN_FOUR_LABEL);        

        
        
        Style cellStyle2 = new Style();
        cellStyle2.setAttributeValue(
                CellStyleAttributes.BACKGROUND_COLOR,
                GUIHelper.COLOR_WIDGET_LIGHT_SHADOW);
        
        configRegistry.registerConfigAttribute(
                CellConfigAttributes.CELL_STYLE, cellStyle2,
                DisplayMode.NORMAL, StackingTable.TEST);
  
        // configure the validation error style
        Style validationErrorStyle = new Style();
        validationErrorStyle.setAttributeValue(
                CellStyleAttributes.BACKGROUND_COLOR,
                GUIHelper.COLOR_RED);
        validationErrorStyle.setAttributeValue(
                CellStyleAttributes.FOREGROUND_COLOR,
                GUIHelper.COLOR_WHITE);        
        configRegistry.registerConfigAttribute(
                CellConfigAttributes.CELL_STYLE,
                validationErrorStyle,
                DisplayMode.NORMAL,
                StackingTable.INVALID_THICKNESS_LABEL);
        
        configRegistry.registerConfigAttribute(
                CellConfigAttributes.CELL_STYLE,
                validationErrorStyle,
                DisplayMode.NORMAL,
                StackingTable.INVALID_ANGLE_LABEL);       
        
	}

}
