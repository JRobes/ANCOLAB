package nattable05filter.adddeleterows;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractUiBindingConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.menu.IMenuItemProvider;
import org.eclipse.nebula.widgets.nattable.ui.menu.MenuItemProviders;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuAction;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import aero.alestis.stresstools.ancolab.model.AncolabMaterial;
import materialstable.AddUserMaterialDialog;

public class MenuConfigurationMaterialsTable extends AbstractUiBindingConfiguration {

	private final Menu debugMenu;

	public MenuConfigurationMaterialsTable(NatTable natTable) {
		// [2] create the menu using the PopupMenuBuilder
		//this.debugMenu = new PopupMenuBuilder(natTable).withInspectLabelsMenuItem().build();
		this.debugMenu = new PopupMenuBuilder(natTable).withMenuItemProvider(new IMenuItemProvider() {

            @Override
            public void addMenuItem(NatTable natTable, Menu popupMenu) {
                Shell shell = natTable.getShell();
            	MenuItem addRow = new MenuItem(popupMenu, SWT.PUSH);
                addRow.setText("Add");
                addRow.setEnabled(true);
                addRow.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent event) {
        				/*
                    	AddUserMaterialDialog dialog = new AddUserMaterialDialog(shell);
        				dialog.create();
        				if (dialog.open() == Window.OK) {
        					AncolabMaterial matToAdd = dialog.getUserMaterial();
        					System.out.println("MATERIAL AÑADIDO DESDE EL DIALOGO " +matToAdd.getName());
        					System.out.println("ANTES DE ACTUALIZAR LA tabla de materiales...@@@@@@@@@2€€€€€€€€€€€€5");
        					System.out.println("Tamaño antiguo" +ancolabMaterialsData.size());
        					ancolabMaterialsData.add(matToAdd);
        					//System.out.println("Antes de añadir material "+filterAncolabMaterialGridLayer.getAncolabMaterialData().size());
        					//filterAncolabMaterialGridLayer.getAncolabMaterialData().add(matToAdd);
        					//System.out.println("Despues de añadir material "+filterAncolabMaterialGridLayer.getAncolabMaterialData().size());
        					natTableMaterials.update();
        					natTableMaterials.refresh();
        					
                    		System.out.println("Aqui deberia haber actualizado la tabla de materiales...@@@@@@@@@2€€€€€€€€€€€€5");
        					System.out.println("Nuevo tamaño" + ancolabMaterialsData.size());

        				}
        				*/
                    }
                });
            	
                MenuItem deleteRow = new MenuItem(popupMenu, SWT.PUSH);
                deleteRow.setText("Delete");
                deleteRow.setEnabled(true);
                deleteRow.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent event) {
                        int rowPosition = MenuItemProviders.getNatEventData(event).getRowPosition();
                        natTable.doCommand(new DeleteRowCommand(natTable, rowPosition));
                    }
                });
            }
        }).build();

	}

	@Override
	public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
		// [3] bind the PopupMenuAction to a right click
		// using GridRegion.COLUMN_HEADER instead of null would
		// for example open the menu only on performing a right
		// click on the column header instead of any region
		uiBindingRegistry.registerMouseDownBinding(	new MouseEventMatcher(SWT.NONE, 
															null,  
															MouseEventMatcher.RIGHT_BUTTON),     
													new PopupMenuAction(this.debugMenu));
	}

}