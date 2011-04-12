package pellekrogholt.labexercise10_phonebook;

import java.io.StringWriter;

import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

/**
 * @author Rao
 *
 */
public class Utils {

    /**
     * Print out a SOAPMessage to System.out.
     * 
     * @param message
     */
    public static void print(SOAPMessage message) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(message.getSOAPBody().getOwnerDocument());
            transformer.transform(source, result);
            System.out.println(result.getWriter().toString());
        } catch (Exception exception) {
            System.out.println("Failed to print message: " + exception.getLocalizedMessage());
        }
    }
    
    /**
     * Print out a SOAPMessage to System.out.
     * 
     * @param source
     */
    public static void print(final Source source) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamResult result = new StreamResult(new StringWriter());
            transformer.transform(source, result);
            System.out.println(result.getWriter().toString());
        } catch (Exception exception) {
            System.out.println("Failed to print message: " + exception.getLocalizedMessage());
        }
    }
    
    /**
     * Print out a SOAPMessage to System.out.
     * 
     * @param document
     */
    public static void print(final Document document) {
        print(new DOMSource(document));
    }
    
}