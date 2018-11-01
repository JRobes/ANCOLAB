package nattable05filter.adddeleterows;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.selection.command.SelectCellCommand;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.ui.action.IMouseAction;
import org.eclipse.nebula.widgets.nattable.ui.menu.MenuItemProviders;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Menu;

class CellPopupMenuAction implements IMouseAction {

    private final Menu menu;

    public CellPopupMenuAction(Menu menu) {
        this.menu = menu;
    }

    @Override
    public void run(NatTable natTable, MouseEvent event) {
        int columnPosition = natTable.getColumnPositionByX(event.x);
        int rowPosition = natTable.getRowPositionByY(event.y);

        ILayerCell cell = natTable.getCellByPosition(columnPosition, rowPosition);

        if (!cell.getDisplayMode().equals(DisplayMode.SELECT)) {
            natTable.doCommand(
                    new SelectCellCommand(
                            natTable,
                            columnPosition,
                            rowPosition,
                            false,
                            false));
        }

        menu.setData(MenuItemProviders.NAT_EVENT_DATA_KEY, event.data);
        menu.setVisible(true);
    }
}