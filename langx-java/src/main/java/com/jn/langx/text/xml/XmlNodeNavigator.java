package com.jn.langx.text.xml;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.navigation.Navigator;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
    public Node get(Node context, String xpathExpression) {
        final XPath xpath = xpathFactory.newXPath();
        xpath.setNamespaceContext(new NodeNamespaceContext(context, this.namespacePrefix));
        try {
            final XPathExpression exp = xpath.compile(xpathExpression);
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

    }
}
