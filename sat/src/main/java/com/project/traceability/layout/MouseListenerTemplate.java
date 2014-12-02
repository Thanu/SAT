package com.project.traceability.layout;

import javax.swing.JOptionPane;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.preview.api.PreviewMouseEvent;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = PreviewMouseListener.class)
public class MouseListenerTemplate implements PreviewMouseListener {

	private boolean clickingInNode(Node node, PreviewMouseEvent event) {
		float xdiff = node.getNodeData().x() - event.x;
		float ydiff = -node.getNodeData().y() - event.y;// Note that y axis is inverse for
											// node coordinates
		float radius = node.getNodeData().r();

		return xdiff * xdiff + ydiff * ydiff < radius * radius;
	}

	public void mouseClicked(PreviewMouseEvent event,
			PreviewProperties properties, Workspace workspace) {
		System.out.println("Clicked");
		for (Node node : Lookup.getDefault().lookup(GraphController.class).getModel().getGraph().getNodes()) {
			if (clickingInNode(node, event)) {
				properties.putValue("display-label.node.id",
						Integer.toString(node.getId()));
				System.err.println("Node "
						+  node.getNodeData().getLabel()+ " clicked!");// System.out is ignored in Netbeans
										// platform applications!!
				JOptionPane.showMessageDialog(null,
						"Node " +  node.getNodeData().getLabel()
								+ " clicked!");
				event.setConsumed(true);// So the renderer is executed and the
										// graph repainted
				return;
			}
		}

		properties.removeSimpleValue("display-label.node.id");
		event.setConsumed(true);// So the renderer is executed and the graph
								// repainted
	}

	public void mousePressed(PreviewMouseEvent event,
			PreviewProperties properties, Workspace workspace) {
		// TODO Auto-generated method stub

	}

	public void mouseDragged(PreviewMouseEvent event,
			PreviewProperties properties, Workspace workspace) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(PreviewMouseEvent event,
			PreviewProperties properties, Workspace workspace) {
		// TODO Auto-generated method stub

	}
}
