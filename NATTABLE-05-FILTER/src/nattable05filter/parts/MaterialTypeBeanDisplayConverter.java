package nattable05filter.parts;

import org.eclipse.nebula.widgets.nattable.data.convert.DisplayConverter;

public class MaterialTypeBeanDisplayConverter extends DisplayConverter {

	@Override
	public Object canonicalToDisplayValue(Object canonicalValue) {
        if (canonicalValue == null) {
            return null;
        } else {
            return canonicalValue.toString().equals("Composite") ? "Composite" : "Honeycomb";
        }
	}

	@Override
	public Object displayToCanonicalValue(Object displayValue) {
        return displayValue.toString().equals("Composite") ? "Composite" : "Honeycomb";

	}

}
