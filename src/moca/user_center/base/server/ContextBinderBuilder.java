package moca.user_center.base.server;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.net.httpserver.HttpHandler;

public class ContextBinderBuilder {

    private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    public static ContextBinder buildFrom(File xmlFile) throws Exception {
        ContextBinder contextBinder = new ContextBinder();
        Document document = documentBuilderFactory.newDocumentBuilder().parse(new FileInputStream(xmlFile));
        parseXmlNode(contextBinder, document.getFirstChild());
        return contextBinder;
    }

    @SuppressWarnings("unchecked")
    private static void parseXmlNode(ContextBinder contextBinder, Node xmlNode) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        NodeList childNodes = xmlNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            parseXmlNode(contextBinder, childNodes.item(i));
        }
        if (xmlNode.getNodeType() != Node.ELEMENT_NODE) {
            return;
        }
        if (!xmlNode.getNodeName().equals("context")) {
            return;
        }
        NamedNodeMap attribute = xmlNode.getAttributes();
        String alias = attribute.getNamedItem("path").getNodeValue();
        Class<? extends HttpHandler> clz = (Class<? extends HttpHandler>) Class.forName(attribute.getNamedItem("class").getNodeValue());
        contextBinder.bind(alias, clz.newInstance());
    }

}
