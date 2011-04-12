package pellekrogholt.labexercise10_phonebook;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;


public class PhoneBookServiceClient {



	public static void main(String[] args) 
	{

		QName service =
			new QName("http://hello.samples.smds2011.itu.dk/","phoneBookService");
		QName port =
			new QName("http://hello.samples.smds2011.itu.dk/", "phoneBookServicePort");
		//			String endpointAddress = "http://localhost:8085/labexercises/hello";

		String endpointAddress = "http://localhost:8085/labexercises/person";
		Service webservice = Service.create(service);
		webservice.addPort(port,SOAPBinding.SOAP11HTTP_BINDING, endpointAddress);	
		//webservice.addPort(port,HTTPBinding.HTTP_BINDING, endpointAddress);


		/*
		 * Add person
		 */
		try 
		{
			MessageFactory factory =
				MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
			SOAPMessage request = factory.createMessage();
			SOAPPart soap = request.getSOAPPart();
			SOAPEnvelope envelope = soap.getEnvelope();
			SOAPBody body = envelope.getBody();
			SOAPElement content = body.addBodyElement(
					new QName("http://hello.samples.smds2011.itu.dk/",
							"addPerson",
					"itu"));

			SOAPElement root, person, name, address, phone, zip;

			//name = content.addChildElement("arg0","", "http://hello.samples.smds2010.itu.dk/");
			root = content.addChildElement("arg0");

			person = root.addChildElement("person");
			name = root.addChildElement("name");
			//						name = person.addChildElement("name"); // had expected it to be on root
			name.addTextNode("Name Name");


			address = root.addChildElement("address");
			address.addTextNode("Address dfsdf 8898");

			phone = root.addChildElement("phone");
			phone.addTextNode("90099909");

			zip = root.addChildElement("zip");
			zip.addTextNode("79798");						

			Utils.print(request);

			Dispatch<SOAPMessage> dispatch =
				webservice.createDispatch(port, SOAPMessage.class,
						Service.Mode.MESSAGE);
			SOAPMessage response = dispatch.invoke(request);
//			String text = response.getSOAPBody().getTextContent();
			Utils.print(response);

		} 
		catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * Get person
		 */
		try 
		{
			MessageFactory factory =
				MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
			SOAPMessage request = factory.createMessage();
			SOAPPart soap = request.getSOAPPart();
			SOAPEnvelope envelope = soap.getEnvelope();
			SOAPBody body = envelope.getBody();
			SOAPElement content = body.addBodyElement(
					new QName("http://hello.samples.smds2011.itu.dk/",
							"getPerson",
					"itu"));

			// no root needed for a simple string name adding (still don't get the approach 100%)
			// this is based on the hand out from rao and http://download.oracle.com/docs/cd/E17802_01/webservices/webservices/docs/2.0/tutorial/doc/
			SOAPElement name;
			name = content.addChildElement("arg0");
			name.addTextNode("Name Name");
			
			Utils.print(request);

			Dispatch<SOAPMessage> dispatch =
				webservice.createDispatch(port, SOAPMessage.class,
						Service.Mode.MESSAGE);
			SOAPMessage response = dispatch.invoke(request);
			Utils.print(response);

		} 
		catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}
