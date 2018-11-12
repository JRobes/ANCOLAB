package materialstable;

import java.io.Serializable;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowIdAccessor;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsEventLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayerListener;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.event.ILayerEvent;
import org.eclipse.nebula.widgets.nattable.layer.event.IVisualChangeEvent;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionModel;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.selection.config.DefaultRowSelectionLayerConfiguration;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;

import aero.alestis.stresstools.ancolab.model.AncolabMaterial;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TransformedList;
import nattable05filter.adddeleterows.AddRowCommandHandler;
import nattable05filter.adddeleterows.DeleteRowCommandHandler;

/**
 * Always encapsulate the body layer stack in an AbstractLayerTransform to
 * ensure that the index transformations are performed in later commands.
 *
 * @param 
 */
public class MaterialsTableBodyLayerStack extends AbstractLayerTransform {

    private final EventList<AncolabMaterial> filterList;
    private final IDataProvider bodyDataProvider;
    private SelectionLayer selectionLayer;
    private DataLayer bodyDataLayer;
    private EventList eventList;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MaterialsTableBodyLayerStack(List<AncolabMaterial> values, IColumnPropertyAccessor<AncolabMaterial> columnPropertyAccessor) {
       // DataLayer dp = new DataLayer((IDataProvider) values);
        //dp.getdat.getda
		// wrapping of the list to show into GlazedLists
        // see http://publicobject.com/glazedlists/ for further information
        this.eventList = GlazedLists.eventList(values);
       
        
        //
        DataLayer dataLayer = new DataLayer(new ListDataProvider<AncolabMaterial>(eventList, columnPropertyAccessor));
       // dataLayer.registerCommandHandler(dataLayer.getDataProvider().g);
        
       // ((EventList<AncolabMaterial>)dataLayer.getDataProvider()).getList();
        
        //dataLayer.setDataProvider(dataProvider);
       // IDataProvider dp = dataLayer.getDataProvider();
        //((EventList<AncolabMaterial>)dp).get
        //DataLayer dataLayer = new DataLayer((IDataProvider) eventList);
        //dataLayer.registerCommandHandler(new AddRowCommandHandler(eventList));
        TransformedList<?, ?> rowObjectsGlazedList = GlazedLists.threadSafeList(eventList);
        // use the SortedList constructor with 'null' for the Comparator
        // because the Comparator will be set by configuration
        SortedList<?> sortedList = new SortedList<>(rowObjectsGlazedList, null);
        // wrap the SortedList with the FilterList
        this.filterList = new FilterList<AncolabMaterial>((EventList<AncolabMaterial>) sortedList);
       // this.filterList.get
        
        //this.bodyDataProvider = new ListDataProvider<AncolabMaterial>(eventList, columnPropertyAccessor);
        //this.bodyDataProvider = new ListDataProvider<AncolabMaterial>(filterList, columnPropertyAccessor);
        this.bodyDataProvider = dataLayer.getDataProvider();;
       
        DataLayer bodyDataLayer = new DataLayer(getBodyDataProvider());
        bodyDataLayer.registerCommandHandler(new DeleteRowCommandHandler<>(((ListDataProvider<AncolabMaterial>) bodyDataProvider).getList()));
        bodyDataLayer.registerCommandHandler(new AddRowCommandHandler(((ListDataProvider<AncolabMaterial>) bodyDataProvider).getList()));
        //bodyDataLayer.registerCommandHandler(new AddRowCommandHandler(eventList));

        //Set cell width
        bodyDataLayer.setColumnWidthByPosition(0, 250);
        bodyDataLayer.setColumnWidthByPosition(1,  90);
        bodyDataLayer.setColumnWidthByPosition(2,  90);
        bodyDataLayer.setColumnWidthByPosition(3,  90);
        bodyDataLayer.setColumnWidthByPosition(4,  65);
        bodyDataLayer.setColumnWidthByPosition(5,  65);
        bodyDataLayer.setColumnWidthByPosition(6,  65);
        bodyDataLayer.setColumnWidthByPosition(7,  65);
        
        bodyDataLayer.setColumnsResizableByDefault(false);
        
        ColumnOverrideLabelAccumulator bodyLabelAccumulator = new ColumnOverrideLabelAccumulator(bodyDataLayer);
        bodyDataLayer.setConfigLabelAccumulator(bodyLabelAccumulator);
       
        // layer for event handling of GlazedLists and PropertyChanges
        GlazedListsEventLayer<?> glazedListsEventLayer =
                new GlazedListsEventLayer<AncolabMaterial>(bodyDataLayer, this.filterList);
          
        this.selectionLayer = new SelectionLayer(glazedListsEventLayer, false);
        
        // use a RowSelectionModel that will perform row selections and is able
        // to identify a row via unique ID
        this.selectionLayer.setSelectionModel(new RowSelectionModel<>(
                selectionLayer, (IRowDataProvider) bodyDataProvider, new IRowIdAccessor() {
					@Override
					public Serializable getRowId(Object rowObject) {
                        return ((AncolabMaterial)rowObject).getName();
					}
                },false));
        this.selectionLayer.addConfiguration(new DefaultRowSelectionLayerConfiguration());

        ViewportLayer viewportLayer = new ViewportLayer(getSelectionLayer());
        setUnderlyingLayer(viewportLayer);
    }

    public SelectionLayer getSelectionLayer() {
        return this.selectionLayer;
    }

    public FilterList<AncolabMaterial> getFilterList() {
        return (FilterList<AncolabMaterial>) this.filterList;
    }

    public IDataProvider getBodyDataProvider() {
        return this.bodyDataProvider;
    }

	public EventList<?> getEventList() {
		return eventList;
	}



}