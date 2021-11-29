package com.jn.langx.text.xml;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.iter.Iterables;
import com.jn.langx.util.function.Function;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NodeNamespaceContext implements NamespaceContext {
    /**
     * 当默认的命名空间不是规范定义的命名空间时，即自定义了命名空间时，使用xpath会检索失败。
     * 解决方案是随意的指定一个命名空间前缀，并在 xpath表达式中使用该命名空间前缀。
     */
    private String defaultPrefix;
    /**
     * key: prefix
     */
    private Map<String, Namespace> prefixToNamespaceMap;
    /**
     * key: namespaceURI
     */
    private Map<String, List<Namespace>> uriToNamespacesMap;

    public NodeNamespaceContext(Document document, String defaultPrefix) {
        this(document.getDocumentElement());
        setDefaultPrefix(defaultPrefix);
    }

    public NodeNamespaceContext(Document document) {
        this(document.getDocumentElement());
    }

    public NodeNamespaceContext(Node node, String defaultPrefix) {
        this(node);
        setDefaultPrefix(defaultPrefix);
    }

    public NodeNamespaceContext(Node node) {
        this.prefixToNamespaceMap = Namespaces.findNamespaces(node);
        reGroup();
    }

    public void setDefaultPrefix(String defaultPrefix) {
        this.defaultPrefix = defaultPrefix;
        reGroup();
    }

    private void reGroup() {
        if (prefixToNamespaceMap != null) {
            if(Strings.isNotBlank(defaultPrefix) && !defaultPrefix.equals(XMLConstants.XMLNS_ATTRIBUTE)){
                if(prefixToNamespaceMap.get(XMLConstants.XMLNS_ATTRIBUTE)!=null){
                    this.prefixToNamespaceMap.put(defaultPrefix, prefixToNamespaceMap.get(XMLConstants.XMLNS_ATTRIBUTE));
                }
            }
            this.uriToNamespacesMap = Collects.groupBy(this.prefixToNamespaceMap.values(), new Function<Namespace, String>() {
                @Override
                public String apply(Namespace namespace) {
                    return namespace.getUri();
                }
            });
        }
    }

    @Override
    public String getNamespaceURI(String prefix) {
        Namespace namespace = prefixToNamespaceMap.get(prefix);
        if (namespace != null) {
            return namespace.getUri();
        }
        return XMLConstants.NULL_NS_URI;
    }

    @Override
    public String getPrefix(String namespaceURI) {
        List<Namespace> namespaces = this.uriToNamespacesMap.get(namespaceURI);
        if (!Objs.isEmpty(namespaces)) {
            return namespaces.get(0).getPrefix();
        }
        return XMLConstants.DEFAULT_NS_PREFIX;
    }

    @Override
    public Iterator getPrefixes(String namespaceURI) {
        List<Namespace> namespaces = this.uriToNamespacesMap.get(namespaceURI);
        return Iterables.getIterator(namespaces);
    }
}
