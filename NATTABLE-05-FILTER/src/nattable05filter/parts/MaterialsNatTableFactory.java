package nattable05filter.parts;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import aero.alestis.stresstools.ancolab.model.AncolabConstants;

public class MaterialsNatTableFactory {

	
	public static NatTable createTable(Composite parent, IConfigRegistry configRegistry, FilterAncolabMaterialGridLayer underlyingLayer) {
        //IConfigRegistry configRegistry = new ConfigRegistry();
       // FilterAncolabMaterialGridLayer underlyingLayer = new FilterAncolabMaterialGridLayer(configRegistry);

        NatTable natTable = new NatTable(parent, underlyingLayer, false);
        
        natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
        //natTable.addConfiguration(new HeaderMenuConfiguration(natTable));
        // natTable.addConfiguration(new DebugMenuConfiguration(natTable));
        natTable.addConfiguration(new FilterAncolabMaterialCustomConf() {
            @Override
            public void configureRegistry(IConfigRegistry configRegistry) {
                super.configureRegistry(configRegistry);

                // Shade the row to be slightly darker than the blue background.
                final Style rowStyle = new Style();
                rowStyle.setAttributeValue(
                        CellStyleAttributes.BACKGROUND_COLOR,
                        GUIHelper.getColor(197, 212, 231));
                configRegistry.registerConfigAttribute(
                        CellConfigAttributes.CELL_STYLE,
                        rowStyle,
                        DisplayMode.NORMAL,
                        GridRegion.FILTER_ROW);
            }
        });

        natTable.setConfigRegistry(configRegistry);
        natTable.configure();

        DragAndDropSupport dragSupport =
                new DragAndDropSupport(natTable, underlyingLayer.getSelectionLayer(), underlyingLayer.getAncolabMaterialData());
        Transfer[] transfer = { TextTransfer.getInstance() };
        natTable.addDragSupport(DND.DROP_COPY, transfer, dragSupport);
      
        
		return natTable;
	}
}
