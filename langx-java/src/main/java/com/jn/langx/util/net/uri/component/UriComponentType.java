package com.jn.langx.util.net.uri.component;

/**
 * Enumeration used to identify the allowed characters per URI component.
 * <p>Contains methods to indicate whether a given character is valid in a specific URI component.
 *
 * @see <a href="https://tools.ietf.org/html/rfc3986">RFC 3986</a>
 */
public enum UriComponentType {

    SCHEME {
        @Override
        public boolean isAllowed(int c) {
            return isAlpha(c) || isDigit(c) || '+' == c || '-' == c || '.' == c;
        }
    },
    AUTHORITY {
        @Override
        public boolean isAllowed(int c) {
            return isUnreserved(c) || isSubDelimiter(c) || ':' == c || '@' == c;
        }
    },
    USER_INFO {
        @Override
        public boolean isAllowed(int c) {
            return isUnreserved(c) || isSubDelimiter(c) || ':' == c;
        }
    },
    HOST_IPV4 {
        @Override
        public boolean isAllowed(int c) {
            return isUnreserved(c) || isSubDelimiter(c);
        }
    },
    HOST_IPV6 {
        @Override
        public boolean isAllowed(int c) {
            return isUnreserved(c) || isSubDelimiter(c) || '[' == c || ']' == c || ':' == c;
        }
    },
    PORT {
        @Override
        public boolean isAllowed(int c) {
            return isDigit(c);
        }
    },
    PATH {
        @Override
        public boolean isAllowed(int c) {
            return isPchar(c) || '/' == c;
        }
    },
    PATH_SEGMENT {
        @Override
        public boolean isAllowed(int c) {
            return isPchar(c);
        }
    },
    QUERY {
        @Override
        public boolean isAllowed(int c) {
            return isPchar(c) || '/' == c || '?' == c;
        }
    },
    QUERY_PARAM {
        @Override
        public boolean isAllowed(int c) {
            if ('=' == c || '&' == c) {
                return false;
            } else {
                return isPchar(c) || '/' == c || '?' == c;
            }
        }
    },
    FRAGMENT {
        @Override
        public boolean isAllowed(int c) {
            return isPchar(c) || '/' == c || '?' == c;
        }
    },
    URI {
        @Override
        public boolean isAllowed(int c) {
            return isUnreserved(c);
        }
    };

    /**
     * Indicates whether the given character is allowed in this URI component.
     *
     * @return {@code true} if the character is allowed; {@code false} otherwise
     */
    public abstract boolean isAllowed(int c);

    /**
     * Indicates whether the given character is in the {@code ALPHA} set.
     *
     * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
     */
    protected boolean isAlpha(int c) {
        return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
    }

    /**
     * Indicates whether the given character is in the {@code DIGIT} set.
     *
     * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
     */
    protected boolean isDigit(int c) {
        return (c >= '0' && c <= '9');
    }

    /**
     * Indicates whether the given character is in the {@code gen-delims} set.
     *
     * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
     */
    protected boolean isGenericDelimiter(int c) {
        return (':' == c || '/' == c || '?' == c || '#' == c || '[' == c || ']' == c || '@' == c);
    }

    /**
     * Indicates whether the given character is in the {@code sub-delims} set.
     *
     * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
     */
    protected boolean isSubDelimiter(int c) {
        return ('!' == c || '$' == c || '&' == c || '\'' == c || '(' == c || ')' == c || '*' == c || '+' == c ||
                ',' == c || ';' == c || '=' == c);
    }

    /**
     * Indicates whether the given character is in the {@code reserved} set.
     *
     * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
     */
    protected boolean isReserved(int c) {
        return (isGenericDelimiter(c) || isSubDelimiter(c));
    }

    /**
     * Indicates whether the given character is in the {@code unreserved} set.
     *
     * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
     */
    protected boolean isUnreserved(int c) {
        return (isAlpha(c) || isDigit(c) || '-' == c || '.' == c || '_' == c || '~' == c);
    }

    /**
     * Indicates whether the given character is in the {@code pchar} set.
     *
     * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
     */
    protected boolean isPchar(int c) {
        return (isUnreserved(c) || isSubDelimiter(c) || ':' == c || '@' == c);
    }
}
