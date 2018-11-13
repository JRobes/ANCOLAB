package stackingtable;

import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;

public class StackingTableConfiguration extends AbstractRegistryConfiguration {

	@Override
	public void configureRegistry(IConfigRegistry configRegistry) {
    	Style cellStyle = new Style();
        cellStyle.setAttributeValue(
                CellStyleAttributes.BACKGROUND_COLOR,
                GUIHelper.COLOR_WHITE);
        configRegistry.registerConfigAttribute(
                CellConfigAttributes.CELL_STYLE, cellStyle,
                DisplayMode.NORMAL, StackingNatTableFactory.COLUMN_ONE_LABEL);
        configRegistry.registerConfigAttribute(
                CellConfigAttributes.CELL_STYLE, cellStyle,
                DisplayMode.NORMAL, StackingNatTableFactory.COLUMN_TWO_LABEL);
        configRegistry.registerConfigAttribute(
                CellConfigAttributes.CELL_STYLE, cellStyle,
                DisplayMode.NORMAL, StackingNatTableFactory.COLUMN_THREE_LABEL);
        configRegistry.registerConfigAttribute(
                CellConfigAttributes.CELL_STYLE, cellStyle,
                DisplayMode.NORMAL, StackingNatTableFactory.COLUMN_FOUR_LABEL);
        configRegistry.registerConfigAttribute(
                CellConfigAttributes.CELL_STYLE, cellStyle,
                DisplayMode.NORMAL, StackingNatTableFactory.COLUMN_FIVE_LABEL);




    	configRegistry.registerConfigAttribute(
        		EditConfigAttributes.CELL_EDITABLE_RULE, 
        		//new OrientationEditableRule(selectionProvider, StressToolsConstants.HONEYCOMB_COMPOSITE_TYPE),
        		IEditableRule.ALWAYS_EDITABLE, 
        		DisplayMode.EDIT, 
        		StackingNatTableFactory.COLUMN_FIVE_LABEL);
    	/*
    	configRegistry.registerConfigAttribute(
        		EditConfigAttributes.CELL_EDITABLE_RULE, 
        		new OrientationEditableRule(selectionProvider, StressToolsConstants.HONEYCOMB_COMPOSITE_TYPE),
        		//IEditableRule.ALWAYS_EDITABLE, 
        		DisplayMode.EDIT, 
        		StackingNatTableFactory.COLUMN_FOUR_LABEL);
    	*/
    	
    	
    	
    	/* ESTO TENGO QUE CAMBIARLOS PORQUE BORRE LA PUTA CLASE THICKNESSEDITABLE RULE
    	 * TEGO QUE VOLER A HACERLA....CAGONDIOS...
    	configRegistry.registerConfigAttribute(
        		EditConfigAttributes.CELL_EDITABLE_RULE, 
        		new ThicknessEditableRule(selectionProvider, "Honeycomb", 4),
        		//IEditableRule.ALWAYS_EDITABLE, 
        		DisplayMode.EDIT, 
        		StackingNatTableFactory.COLUMN_FIVE_LABEL);
        		
        */
    	
    	
    	//TextCellEditor textCellEditor = new TextCellEditor();
    	//textCellEditor.setErrorDecorationEnabled(true);
    	
    	
/*               
        configRegistry.registerConfigAttribute(
        		EditConfigAttributes.CELL_EDITOR, 
        		new TextCellEditor(true, true),
                DisplayMode.EDIT,
                NatTableFactory.COLUMN_FIVE_LABEL);
        
        configRegistry.registerConfigAttribute(
                EditConfigAttributes.DATA_VALIDATOR,
                getThicknessValidator(), DisplayMode.EDIT,
                NatTableFactory.COLUMN_FIVE_LABEL);
*/           

     /*
        configRegistry.registerConfigAttribute(
                EditConfigAttributes.DATA_VALIDATOR,
                getOrientationValidator(), DisplayMode.EDIT,
                NatTableFactory.COLUMN_TWO_LABEL);
    */
	}

}
