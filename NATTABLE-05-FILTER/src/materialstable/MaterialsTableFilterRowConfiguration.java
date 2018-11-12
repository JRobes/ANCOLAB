package materialstable;

import static org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes.CELL_PAINTER;
import static org.eclipse.nebula.widgets.nattable.grid.GridRegion.FILTER_ROW;
import static org.eclipse.nebula.widgets.nattable.style.DisplayMode.NORMAL;

import java.util.Arrays;

import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.ComboBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterIconPainter;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowDataLayer;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowPainter;
import org.eclipse.nebula.widgets.nattable.filterrow.config.FilterRowConfigAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;

import nattable05filter.parts.MaterialTypeBeanDisplayConverter;

/**
 * The configuration to enable the edit mode for the grid and additional
 * edit configurations like converters and validators.
 */
class MaterialsTableFilterRowConfiguration extends AbstractRegistryConfiguration {

    @Override
    public void configureRegistry(IConfigRegistry configRegistry) {
    	// override the default filter row configuration for painter
    	configRegistry.registerConfigAttribute(
                CELL_PAINTER,
                new FilterRowPainter(new FilterIconPainter(GUIHelper.getImage("filter"))),
                NORMAL,
                FILTER_ROW);
        // Register a combo box editor to be displayed in the filter row
        // cell when a value is selected from the combo, the object is
        // converted to a string using the converter (registered below)
        configRegistry.registerConfigAttribute(
                EditConfigAttributes.CELL_EDITOR,
                new ComboBoxCellEditor(Arrays.asList("Composite", "Honeycomb")),
                DisplayMode.NORMAL,
                FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 2);
        // The pricing bean object in column is converted to using this
        // display converter
        // A 'text' match is then performed against the value from the combo
        // box
        configRegistry.registerConfigAttribute(
                FilterRowConfigAttributes.FILTER_DISPLAY_CONVERTER,
                new MaterialTypeBeanDisplayConverter(),
                DisplayMode.NORMAL,
                FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 2);

        configRegistry.registerConfigAttribute(
                CellConfigAttributes.DISPLAY_CONVERTER,
                new MaterialTypeBeanDisplayConverter(),
                DisplayMode.NORMAL,
                FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 2);
    
    
    
    }
}