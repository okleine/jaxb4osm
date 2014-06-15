package de.uniluebeck.itm.jaxb4osm.tools;

import de.uniluebeck.itm.jaxb4osm.elements.BoundsElement;
import de.uniluebeck.itm.jaxb4osm.elements.NodeElement;
import de.uniluebeck.itm.jaxb4osm.elements.OsmElement;
import javanet.staxutils.IndentingXMLEventWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by olli on 15.06.14.
 */
public class OsmMarshaller {

    private static Logger log = LoggerFactory.getLogger(OsmMarshaller.class.getName());

    private static Marshaller marshaller;
    static{
        try{
            JAXBContext context = JAXBContext.newInstance(OsmElement.PlainOsmElement.class);
            marshaller = context.createMarshaller();
        }
        catch (Exception ex){
            log.error("This should never happen!", ex);
        }
    }


    public static void marshal(OsmElement osmElement, OutputStream outputStream) throws Exception{

        OsmElement.PlainOsmElement plainOsmElement = new OsmElement.OsmElementAdapter().marshal(osmElement);

        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
        IndentingXMLEventWriter xmlEventWriter =
                new IndentingXMLEventWriter(xmlOutputFactory.createXMLEventWriter(outputStream));

        marshaller.marshal(plainOsmElement, xmlEventWriter);
    }


    public static void main(String[] args) throws Exception{
        String pathToOriginalOsmFile = args[0];
        String pathToNewOsmFile = args[1];

        File newFile = new File(pathToNewOsmFile);
        newFile.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(newFile);

        OsmElement osmElement = OsmUnmarshaller.unmarshal(new File(pathToOriginalOsmFile));
        OsmMarshaller.marshal(osmElement, outputStream);
    }
}
