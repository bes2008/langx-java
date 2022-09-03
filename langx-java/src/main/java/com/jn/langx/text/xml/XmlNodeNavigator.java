package com.jn.langx.text.xml;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.navigation.Navigator;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;
import org.w3c.dom.*;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.util.List;

/**
 * @since 4.6.10
 */
public class XmlNodeNavigator implements Navigator<Node> {
    private static final Logger logger = Loggers.getLogger(XmlNodeNavigator.class);
    @NonNull
    private XPathFactory xpathFactory;
    @Nullable
    private String namespacePrefix;

    public XmlNodeNavigator() {
        this(null,null);
    }

    public XmlNodeNavigator(@Nullable XPathFactory xpathFactory) {
        this(xpathFactory,null);
    }

    public XmlNodeNavigator(String namespacePrefix) {
        this(null,namespacePrefix);
    }
    public XmlNodeNavigator(@Nullable XPathFactory xpathFactory,
                            @Nullable String namespacePrefix) {
        this.xpathFactory = xpathFactory==null? XPathFactory.newInstance():xpathFactory;
        this.namespacePrefix=namespacePrefix;
    }

    @Override
    public Node get(Node context, String pathExpression) {
        final XPath xpath = xpathFactory.newXPath();
        xpath.setNamespaceContext(new NodeNamespaceContext(context, this.namespacePrefix));
        try {
            final XPathExpression exp = xpath.compile(pathExpression);
            Node node = (Node) exp.evaluate(context, XPathConstants.NODE);
            return node;
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }

    }

    @Override
    public List<Node> getList(Node context, String xpathExpression) {
        final XPath xpath = xpathFactory.newXPath();
        xpath.setNamespaceContext(new NodeNamespaceContext(context, this.namespacePrefix));
        try {
            final XPathExpression exp = xpath.compile(xpathExpression);
            NodeList nodeList = (NodeList) exp.evaluate(context, XPathConstants.NODESET);
            return Pipeline.<Node>of(new NodeListIterator(nodeList)).asList();
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    public <T> void set(Node context, String xpath, T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E> Class<E> getType(Node context, String pathExpression) {
        Node node = get(context,pathExpression);
        if(node!=null) {
            int nodeType = node.getNodeType();
            Class nodeClass = Node.class;
            switch (nodeType) {
                case Node.ELEMENT_NODE:
                    nodeClass = Element.class;
                    break;
                case Node.ATTRIBUTE_NODE:
                    nodeClass = Attr.class;
                    break;
                case Node.TEXT_NODE:
                    nodeClass = Text.class;
                    break;
                case Node.CDATA_SECTION_NODE:
                    nodeClass = CDATASection.class;
                    break;
                case Node.ENTITY_NODE:
                    nodeClass = Entity.class;
                    break;
                case Node.ENTITY_REFERENCE_NODE:
                    nodeClass = EntityReference.class;
                    break;
                case Node.COMMENT_NODE:
                    nodeClass = Comment.class;
                    break;
                case Node.DOCUMENT_NODE:
                    nodeClass = Document.class;
                    break;
            }
            return nodeClass;
        }
        return null;
    }

    @Override
    public String getParentPath(String pathExpression) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getLeaf(String pathExpression) {
        throw new UnsupportedOperationException();
    }
}
