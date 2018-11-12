package nattable05filter.parts;
import static org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes.CELL_PAINTER;
import static org.eclipse.nebula.widgets.nattable.grid.GridRegion.FILTER_ROW;
import static org.eclipse.nebula.widgets.nattable.style.DisplayMode.NORMAL;

import java.util.Arrays;
import java.util.Comparator;


import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDoubleDisplayConverter;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.ComboBoxCellEditor;

//import org.eclipse.nebula.widgets.nattable.examples.AbstractNatExample;
//import org.eclipse.nebula.widgets.nattable.examples.PersistentNatExampleWrapper;
//import org.eclipse.nebula.widgets.nattable.examples.fixtures.FilterRowExampleGridLayer;
//import org.eclipse.nebula.widgets.nattable.examples.fixtures.PricingTypeBeanDisplayConverter;
//import org.eclipse.nebula.widgets.nattable.examples.runner.StandaloneNatExampleRunner;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterIconPainter;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowDataLayer;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowPainter;
import org.eclipse.nebula.widgets.nattable.filterrow.config.FilterRowConfigAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
public class FilterAncolabMaterialCustomConf extends AbstractRegistryConfiguration{

    final DefaultDoubleDisplayConverter doubleDisplayConverter = new DefaultDoubleDisplayConverter();

    @Override
    public void configureRegistry(IConfigRegistry configRegistry) {
        // override the default filter row configuration for painter
        configRegistry.registerConfigAttribute(
                CELL_PAINTER,
                new FilterRowPainter(
                        new FilterIconPainter(GUIHelper.getImage("filter"))),
                NORMAL, FILTER_ROW);

        // Configure custom comparator on the rating column
       // configRegistry.registerConfigAttribute(
       //         FilterRowConfigAttributes.FILTER_COMPARATOR,
       //         getIngnorecaseComparator(),
       //         DisplayMode.NORMAL,
       //         FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 2);

        // If threshold comparison is used we have to convert the string
        // entered by the user to the correct underlying type (double), so
        // that it can be compared

        // Configure Bid column
       // configRegistry.registerConfigAttribute(
        //        FilterRowConfigAttributes.FILTER_DISPLAY_CONVERTER,
        //        this.doubleDisplayConverter,
         //       DisplayMode.NORMAL,
         //       FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 5);
       // configRegistry.registerConfigAttribute(
        //        FilterRowConfigAttributes.TEXT_MATCHING_MODE,
        //        TextMatchingMode.REGULAR_EXPRESSION,
        //        DisplayMode.NORMAL,
        //        FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 5);

        // Configure Ask column
      //  configRegistry.registerConfigAttribute(
       //         FilterRowConfigAttributes.FILTER_DISPLAY_CONVERTER,
       //         this.doubleDisplayConverter,
       //         DisplayMode.NORMAL,
       //         FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 6);
       // configRegistry.registerConfigAttribute(
       //         FilterRowConfigAttributes.TEXT_MATCHING_MODE,
       //         TextMatchingMode.REGULAR_EXPRESSION,
       //         DisplayMode.NORMAL,
       //         FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 6);

        // Configure a combo box on the pricing type column

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
                FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 1);

        configRegistry.registerConfigAttribute(
                CellConfigAttributes.DISPLAY_CONVERTER,
                new MaterialTypeBeanDisplayConverter(),
                DisplayMode.NORMAL,
                FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 1);

       // configRegistry.registerConfigAttribute(
        //        CellConfigAttributes.DISPLAY_CONVERTER,
         //       new MaterialTypeBeanDisplayConverter(),
         //       DisplayMode.NORMAL,
         //       "PRICING_TYPE_PROP_NAME");
    }
    @SuppressWarnings({ "rawtypes", "unused" })
	private static Comparator getIngnorecaseComparator() {
        return new Comparator() {
			@Override
			public int compare(Object arg0, Object arg1) {
                return ((String) arg0).compareToIgnoreCase((String) arg1);

			}
        };
    };


}
