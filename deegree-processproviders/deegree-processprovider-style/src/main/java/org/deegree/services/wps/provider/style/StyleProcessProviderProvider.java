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
package org.deegree.services.wps.provider.style;

import java.net.URL;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.deegree.commons.config.DeegreeWorkspace;
import org.deegree.commons.config.ResourceInitException;
import org.deegree.commons.config.ResourceManager;
import org.deegree.services.wps.provider.ProcessProvider;
import org.deegree.services.wps.provider.ProcessProviderProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO add class documentation here
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 * @author last edited by: $Author: lyn $
 * 
 * @version $Revision: $, $Date: $
 */
public class StyleProcessProviderProvider implements ProcessProviderProvider {

    private static final Logger LOG = LoggerFactory.getLogger( StyleProcessProviderProvider.class );

    private static final String CONFIG_NAMESPACE = "http://www.deegree.org/processes/style";

    @Override
    public void init( DeegreeWorkspace workspace ) {

    }

    @Override
    public ProcessProvider create( URL configUrl )
                            throws ResourceInitException {

        LOG.info( "Configuring style process provider using file '" + configUrl + "'." );

        try {
            XMLStreamReader xmlStream = XMLInputFactory.newInstance().createXMLStreamReader( configUrl.openStream() );
            while ( xmlStream.getEventType() != XMLStreamConstants.END_DOCUMENT ) {
                if ( xmlStream.isStartElement() && "Process".equals( xmlStream.getLocalName() ) ) {
                    return new StyleProcessProvider( xmlStream.getAttributeValue( null, "id" ) );
                } else {
                    xmlStream.next();
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new RuntimeException( "Error parsing style process provider configuration '" + configUrl + "': "
                                        + e.getMessage() );
        }
        throw new ResourceInitException( "Could not parse style process configuration." );
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends ResourceManager>[] getDependencies() {
        return new Class[] {};
    }

    @Override
    public String getConfigNamespace() {
        return CONFIG_NAMESPACE;
    }

    @Override
    public URL getConfigSchema() {
        return StyleProcessProviderProvider.class.getResource( "META-INF/schemas/processes/style/0.1.0/styleProvider.xsd" );
    }
}
