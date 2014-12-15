/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.db;

import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.preview.api.Item;
import org.gephi.preview.spi.ItemBuilder;

/**
 *
 * @author Thanu
 */
public class ItemBuilderTemplate implements ItemBuilder {

    @Override
    public Item[] getItems(Graph graph, AttributeModel am) {
        return null;
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getType() {
        return null;
        //throw new UnsupportedOperationException("Not supported yet.");
    }

}
