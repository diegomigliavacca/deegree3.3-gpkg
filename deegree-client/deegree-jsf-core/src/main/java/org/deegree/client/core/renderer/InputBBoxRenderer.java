//$HeadURL: svn+ssh://lbuesching@svn.wald.intevation.de/deegree/base/trunk/resources/eclipse/files_template.xml $
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2010 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 Contact information:

 lat/lon GmbH
 Aennchenstr. 19, 53177 Bonn
 Germany
 http://lat-lon.de/

 Department of Geography, University of Bonn
 Prof. Dr. Klaus Greve
 Postfach 1147, 53001 Bonn
 Germany
 http://www.geographie.uni-bonn.de/deegree/

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/
package org.deegree.client.core.renderer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import javax.faces.render.FacesRenderer;

import org.deegree.client.core.component.HtmlInputBBox;
import org.deegree.client.core.model.BBox;

import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.MenuRenderer;

/**
 * TODO add class documentation here
 * 
 * @author <a href="mailto:buesching@lat-lon.de">Lyn Buesching</a>
 * @author last edited by: $Author: lyn $
 * 
 * @version $Revision: $, $Date: $
 */

@FacesRenderer(componentFamily = "javax.faces.SelectOne", rendererType = "org.deegree.InputBBox")
public class InputBBoxRenderer extends MenuRenderer {

    private static final String CRS_ID_SUFFIX = "crs";

    private static final String MINX_ID_SUFFIX = "miny";

    private static final String MINY_ID_SUFFIX = "minx";

    private static final String MAXX_ID_SUFFIX = "maxx";

    private static final String MAXY_ID_SUFFIX = "maxy";

    @Override
    public void decode( FacesContext context, UIComponent component ) {
        HtmlInputBBox bbox = (HtmlInputBBox) component;

        if ( bbox.isDisabled() ) {
            return;
        }

        String clientId = component.getClientId();

        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();
        String crs = null;
        double minx = Double.NaN;
        double miny = Double.NaN;
        double maxx = Double.NaN;
        double maxy = Double.NaN;

        for ( String key : requestMap.keySet() ) {
            try {
                if ( ( clientId + ":" + CRS_ID_SUFFIX ).equals( key ) ) {
                    crs = (String) requestMap.get( key );
                } else if ( ( clientId + ":" + MINX_ID_SUFFIX ).equals( key ) ) {
                    minx = Double.parseDouble( requestMap.get( key ) );
                } else if ( ( clientId + ":" + MINY_ID_SUFFIX ).equals( key ) ) {
                    miny = Double.parseDouble( requestMap.get( key ) );
                } else if ( ( clientId + ":" + MAXX_ID_SUFFIX ).equals( key ) ) {
                    maxx = Double.parseDouble( requestMap.get( key ) );
                } else if ( ( clientId + ":" + MAXY_ID_SUFFIX ).equals( key ) ) {
                    maxy = Double.parseDouble( requestMap.get( key ) );
                }
            } catch ( NumberFormatException e ) {
                // NOTHING TO DO
            }
        }
        bbox.setSubmittedValue( new BBox( crs, minx, miny, maxx, maxy ) );
    }

    @Override
    public Object getConvertedValue( FacesContext context, UIComponent component, Object submittedValue )
                            throws ConverterException {
        return submittedValue;
    }

    @Override
    public void encodeEnd( FacesContext context, UIComponent component )
                            throws IOException {
        HtmlInputBBox bbox = (HtmlInputBBox) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId();

        writer.startElement( "span", component );
        writer.writeAttribute( "id", clientId, "id" );
        writer.writeAttribute( "name", clientId, "id" );
        String styleClass = bbox.getStyleClass();
        if ( styleClass != null ) {
            writer.writeAttribute( "class", styleClass, "styleClass" );
        }

        boolean disabled = bbox.isDisabled();
        if ( bbox.isShowCRS() ) {
            encodeCRSSelect( writer, bbox, clientId, context, disabled );
        }
        encodeCoordFields( writer, bbox, clientId, disabled );

        writer.endElement( "span" );
    }

    private void encodeCRSSelect( ResponseWriter writer, HtmlInputBBox bbox, String clientId, FacesContext context,
                                  boolean disabled )
                            throws IOException {
        writer.startElement( "span", null );
        writer.writeAttribute( "style", "display:block;", null );

        writer.startElement( "span", null );
        writer.writeAttribute( "style", "margin-right: 5px;", null );
        String crsText = bbox.getCrsLabel();
        if ( crsText == null ) {
            writer.writeText( crsText, null );
        }
        writer.endElement( "span" );

        writer.startElement( "select", null );
        writer.writeAttribute( "id", clientId + ":" + CRS_ID_SUFFIX, "id" );
        writer.writeAttribute( "name", clientId + ":" + CRS_ID_SUFFIX, "id" );
        if ( disabled )
            writer.writeAttribute( "disabled", "disabled", "disabled" );
        if ( bbox.getCrsSize() > 0 ) {
            writer.writeAttribute( "size", bbox.getCrsSize(), "crsSize" );
        }

        Iterator<SelectItem> items = RenderKitUtils.getSelectItems( context, bbox );
        renderOptions( context, bbox, items );
        writer.endElement( "select" );

        writer.endElement( "span" );
    }

    private void encodeCoordFields( ResponseWriter writer, HtmlInputBBox bbox, String clientId, boolean disabled )
                            throws IOException {
        BBox value = getCurrentValue( bbox );
        // min X
        double minx = -180;
        if ( value != null && !Double.isNaN( value.getMinx() ) ) {
            minx = value.getMinx();
        }
        addFieldRow( writer, clientId + ":" + MINX_ID_SUFFIX, bbox.getMinxLabel(), minx, disabled, bbox.getOnchange(),
                     false );

        // min y
        double minY = -90;
        if ( value != null && !Double.isNaN( value.getMinY() ) ) {
            minY = value.getMinY();
        }
        addFieldRow( writer, clientId + ":" + MINY_ID_SUFFIX, bbox.getMinyLabel(), minY, disabled, bbox.getOnchange(),
                     false );

        // max x
        double maxx = 180;
        if ( value != null && Double.isNaN( value.getMaxX() ) ) {
            maxx = value.getMaxX();
        }
        addFieldRow( writer, clientId + ":" + MAXX_ID_SUFFIX, bbox.getMaxxLabel(), maxx, disabled, bbox.getOnchange(),
                     false );

        // max y
        double maxy = 90;
        if ( value != null && Double.isNaN( value.getMaxY() ) ) {
            maxy = value.getMaxY();
        }
        addFieldRow( writer, clientId + ":" + MAXY_ID_SUFFIX, bbox.getMaxyLabel(), maxy, disabled, bbox.getOnchange(),
                     true );
    }

    private void addFieldRow( ResponseWriter writer, String id, String label, double value, boolean disabled,
                              String onchange, boolean last )
                            throws IOException {
        writer.startElement( "span", null );
        if ( !last )
            writer.writeAttribute( "style", "display:block;", null );

        writer.startElement( "span", null );
        writer.writeAttribute( "style", "margin-right: 5px;", null );
        writer.writeText( label, null );
        writer.endElement( "span" );

        writer.startElement( "input", null );
        writer.writeAttribute( "id", id, "id" );
        writer.writeAttribute( "name", id, "id" );
        if ( disabled )
            writer.writeAttribute( "disabled", "disabled", "disabled" );
        writer.writeAttribute( "type", "text", "text" );
        writer.writeAttribute( "value", value, "value" );
        if ( onchange != null ) {
            writer.writeAttribute( "onChange", onchange, "onchange" );
        }
        writer.endElement( "input" );

        writer.endElement( "span" );
    }

    @Override
    protected Object getCurrentSelectedValues( UIComponent component ) {
        BBox bbox = ( (HtmlInputBBox) component ).getValue();
        if ( bbox != null && bbox.getCrs() == null ) {
            return new Object[] { bbox.getCrs() };
        }
        return null;
    }

    private BBox getCurrentValue( HtmlInputBBox component ) {
        Object submittedValue = ( (UIInput) component ).getSubmittedValue();
        if ( submittedValue != null ) {
            return (BBox) submittedValue;
        }

        BBox currentValue = null;
        Object currentObj = getValue( component );
        if ( currentObj != null ) {
            currentValue = (BBox) currentObj;
        }
        return currentValue;

    }

}
