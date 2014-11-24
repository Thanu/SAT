package com.project.traceability.layout;

import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.DependantColor;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.preview.types.EdgeColor;
import java.awt.*;

public class PreviewModelBuilder implements ModelBuilder
{
    public void ApplyModel(PreviewController previewController)
    {
        PreviewModel previewModel = previewController.getModel();

		previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS,
				Boolean.TRUE);
		previewModel.getProperties().putValue(
				PreviewProperty.NODE_LABEL_PROPORTIONAL_SIZE, Boolean.TRUE);
		previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR,
				new DependantOriginalColor(Color.BLACK));
		previewModel.getProperties().putValue(
				PreviewProperty.NODE_BORDER_WIDTH, 10.0f);
		Font f = previewModel.getProperties().getFontValue(
				PreviewProperty.NODE_LABEL_FONT);
		previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT,
				f.deriveFont(Font.BOLD, f.getSize() - 8));
		previewModel.getProperties().putValue(
				PreviewProperty.NODE_BORDER_COLOR,
				new DependantColor(DependantColor.Mode.PARENT));
		previewModel.getProperties()
				.putValue(PreviewProperty.NODE_OPACITY, 100);

		previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED,
				Boolean.FALSE);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_COLOR,
				new EdgeColor(EdgeColor.Mode.ORIGINAL));
		previewModel.getProperties()
				.putValue(PreviewProperty.EDGE_OPACITY, 100);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_THICKNESS,
				3.0);
		previewModel.getProperties()
				.putValue(PreviewProperty.EDGE_RADIUS, 0.1f);// distance between
																// node and edge
																// arrow
		previewModel.getProperties().putValue(PreviewProperty.SHOW_EDGE_LABELS,
				Boolean.TRUE);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_LABEL_COLOR,
				new DependantOriginalColor(Color.BLACK));
		f = previewModel.getProperties().getFontValue(
				PreviewProperty.EDGE_LABEL_FONT);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_LABEL_FONT,
				f.deriveFont(Font.BOLD, f.getSize() + 2));
		previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR,
				Color.LIGHT_GRAY);
    }
}
