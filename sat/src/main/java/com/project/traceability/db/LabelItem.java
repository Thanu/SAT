package com.project.traceability.db;


import org.gephi.graph.api.Node;
import org.gephi.preview.api.Item;

/**
 * Basic item without properties but a node.
 */
public class LabelItem implements Item{
    Node node;

    public LabelItem(Node node) {
        this.node = node;
    }

	public Object getSource() {
		return node;
	}

	public String getType() {
		return "label.sometype";
	}

	public <D> D getData(String key) {
		return null;
	}

	public void setData(String key, Object value) {
		// TODO Auto-generated method stub
		
	}

	public String[] getKeys() {
		return new String[0];
	}
    
}
