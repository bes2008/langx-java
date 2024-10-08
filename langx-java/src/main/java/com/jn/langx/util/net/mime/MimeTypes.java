package com.jn.langx.util.net.mime;


import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.io.Charsets;

import java.nio.charset.UnsupportedCharsetException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Miscellaneous {@link MimeType} utility methods.
 */
public class MimeTypes {

    private MimeTypes(){}

    private static final byte[] BOUNDARY_CHARS =
            new byte[]{'-', '_', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
                    'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A',
                    'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
                    'V', 'W', 'X', 'Y', 'Z'};

    /**
     * Comparator used by {@link #sortBySpecificity(List)}.
     */
    public static final Comparator<MimeType> SPECIFICITY_COMPARATOR = new MimeType.SpecificityComparator<MimeType>();

    /**
     * Public constant mime type that includes all media ranges (i.e. "&#42;/&#42;").
     */
    public static final MimeType ALL;

    /**
     * A String equivalent of {@link MimeTypes#ALL}.
     */
    public static final String ALL_VALUE = "*/*";

    /**
     * Public constant mime type for {@code application/json}.
     */
    public static final MimeType APPLICATION_JSON;

    /**
     * A String equivalent of {@link MimeTypes#APPLICATION_JSON}.
     */
    public static final String APPLICATION_JSON_VALUE = "application/json";

    /**
     * Public constant mime type for {@code application/octet-stream}.
     */
    public static final MimeType APPLICATION_OCTET_STREAM;

    /**
     * A String equivalent of {@link MimeTypes#APPLICATION_OCTET_STREAM}.
     */
    public static final String APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream";

    /**
     * Public constant mime type for {@code application/xml}.
     */
    public static final MimeType APPLICATION_XML;

    /**
     * A String equivalent of {@link MimeTypes#APPLICATION_XML}.
     */
    public static final String APPLICATION_XML_VALUE = "application/xml";

    /**
     * Public constant mime type for {@code image/gif}.
     */
    public static final MimeType IMAGE_GIF;

    /**
     * A String equivalent of {@link MimeTypes#IMAGE_GIF}.
     */
    public static final String IMAGE_GIF_VALUE = "image/gif";

    /**
     * Public constant mime type for {@code image/jpeg}.
     */
    public static final MimeType IMAGE_JPEG;

    /**
     * A String equivalent of {@link MimeTypes#IMAGE_JPEG}.
     */
    public static final String IMAGE_JPEG_VALUE = "image/jpeg";

    /**
     * Public constant mime type for {@code image/png}.
     */
    public static final MimeType IMAGE_PNG;

    /**
     * A String equivalent of {@link MimeTypes#IMAGE_PNG}.
     */
    public static final String IMAGE_PNG_VALUE = "image/png";

    /**
     * Public constant mime type for {@code text/html}.
     */
    public static final MimeType TEXT_HTML;

    /**
     * A String equivalent of {@link MimeTypes#TEXT_HTML}.
     */
    public static final String TEXT_HTML_VALUE = "text/html";

    /**
     * Public constant mime type for {@code text/plain}.
     */
    public static final MimeType TEXT_PLAIN;

    /**
     * A String equivalent of {@link MimeTypes#TEXT_PLAIN}.
     */
    public static final String TEXT_PLAIN_VALUE = "text/plain";

    /**
     * Public constant mime type for {@code text/xml}.
     */
    public static final MimeType TEXT_XML;

    /**
     * A String equivalent of {@link MimeTypes#TEXT_XML}.
     */
    public static final String TEXT_XML_VALUE = "text/xml";


    private static final ConcurrentLruCache<String, MimeType> cachedMimeTypes =
            new ConcurrentLruCache<String, MimeType>(64, new Function<String, MimeType>() {
                @Override
                public MimeType apply(String input) {
                    return parseMimeTypeInternal(input);
                }
            });

    @Nullable
    private static volatile Random random;

    static {
        // Not using "parseMimeType" to avoid static init cost
        ALL = new MimeType("*", "*");
        APPLICATION_JSON = new MimeType("application", "json");
        APPLICATION_OCTET_STREAM = new MimeType("application", "octet-stream");
        APPLICATION_XML = new MimeType("application", "xml");
        IMAGE_GIF = new MimeType("image", "gif");
        IMAGE_JPEG = new MimeType("image", "jpeg");
        IMAGE_PNG = new MimeType("image", "png");
        TEXT_HTML = new MimeType("text", "html");
        TEXT_PLAIN = new MimeType("text", "plain");
        TEXT_XML = new MimeType("text", "xml");
    }


    /**
     * Parse the given String into a single {@code MimeType}.
     * Recently parsed {@code MimeType} are cached for further retrieval.
     *
     * @param mimeType the string to parse
     * @return the mime type
     */
    public static MimeType parseMimeType(String mimeType) {
        if (Strings.isBlank(mimeType)) {
            throw new InvalidMimeTypeException(mimeType, "'mimeType' must not be empty");
        }
        // do not cache multipart mime types with random boundaries
        if (mimeType.startsWith("multipart")) {
            return parseMimeTypeInternal(mimeType);
        }
        return cachedMimeTypes.get(mimeType);
    }

    private static MimeType parseMimeTypeInternal(String mimeType) {
        int index = mimeType.indexOf(';');
        String fullType = (index >= 0 ? mimeType.substring(0, index) : mimeType).trim();
        if (fullType.isEmpty()) {
            throw new InvalidMimeTypeException(mimeType, "'mimeType' must not be empty");
        }

        // java.net.HttpURLConnection returns a *; q=.2 Accept header
        if (MimeType.WILDCARD_TYPE.equals(fullType)) {
            fullType = "*/*";
        }
        int subIndex = fullType.indexOf('/');
        if (subIndex == -1) {
            throw new InvalidMimeTypeException(mimeType, "does not contain '/'");
        }
        if (subIndex == fullType.length() - 1) {
            throw new InvalidMimeTypeException(mimeType, "does not contain subtype after '/'");
        }
        String type = fullType.substring(0, subIndex);
        String subtype = fullType.substring(subIndex + 1);
        if (MimeType.WILDCARD_TYPE.equals(type) && !MimeType.WILDCARD_TYPE.equals(subtype)) {
            throw new InvalidMimeTypeException(mimeType, "wildcard type is legal only in '*/*' (all mime types)");
        }

        Map<String, String> parameters = null;
        do {
            int nextIndex = index + 1;
            boolean quoted = false;
            while (nextIndex < mimeType.length()) {
                char ch = mimeType.charAt(nextIndex);
                if (ch == ';') {
                    if (!quoted) {
                        break;
                    }
                } else if (ch == '"') {
                    quoted = !quoted;
                }
                nextIndex++;
            }
            String parameter = mimeType.substring(index + 1, nextIndex).trim();
            if (parameter.length() > 0) {
                if (parameters == null) {
                    parameters = new LinkedHashMap<String, String>(4);
                }
                int eqIndex = parameter.indexOf('=');
                if (eqIndex >= 0) {
                    String attribute = parameter.substring(0, eqIndex).trim();
                    String value = parameter.substring(eqIndex + 1).trim();
                    parameters.put(attribute, value);
                }
            }
            index = nextIndex;
        }
        while (index < mimeType.length());

        try {
            return new MimeType(type, subtype, parameters);
        } catch (UnsupportedCharsetException ex) {
            throw new InvalidMimeTypeException(mimeType, "unsupported charset '" + ex.getCharsetName() + "'");
        } catch (IllegalArgumentException ex) {
            throw new InvalidMimeTypeException(mimeType, ex.getMessage());
        }
    }

    /**
     * Parse the comma-separated string into a list of {@code MimeType} objects.
     *
     * @param mimeTypes the string to parse
     * @return the list of mime types
     */
    public static List<MimeType> parseMimeTypes(String mimeTypes) {
        if (Strings.isBlank(mimeTypes)) {
            return Collections.emptyList();
        }
        return Pipeline.of(tokenize(mimeTypes))
                .filter(Functions.<String>notEmptyPredicate())
                .map(new Function<String, MimeType>() {
                    @Override
                    public MimeType apply(String input) {
                        return parseMimeType(input);
                    }
                })
                .collect(Collects.<MimeType>toList());
    }

    /**
     * Tokenize the given comma-separated string of {@code MimeType} objects
     * into a {@code List<String>}. Unlike simple tokenization by ",", this
     * method takes into account quoted parameters.
     *
     * @param mimeTypes the string to tokenize
     * @return the list of tokens
     * @since 5.1.3
     */
    public static List<String> tokenize(String mimeTypes) {
        if (Strings.isBlank(mimeTypes)) {
            return Collects.emptyArrayList();
        }
        List<String> tokens = new ArrayList<String>();
        boolean inQuotes = false;
        int startIndex = 0;
        int i = 0;
        while (i < mimeTypes.length()) {
            switch (mimeTypes.charAt(i)) {
                case '"':
                    inQuotes = !inQuotes;
                    break;
                case ',':
                    if (!inQuotes) {
                        tokens.add(mimeTypes.substring(startIndex, i));
                        startIndex = i + 1;
                    }
                    break;
                case '\\':
                    i++;
                    break;
                default:
                    break;
            }
            i++;
        }
        tokens.add(mimeTypes.substring(startIndex));
        return tokens;
    }

    /**
     * Return a string representation of the given list of {@code MimeType} objects.
     *
     * @param mimeTypes the string to parse
     * @return the list of mime types
     * @throws IllegalArgumentException if the String cannot be parsed
     */
    public static String toString(Collection<? extends MimeType> mimeTypes) {
        StringBuilder builder = new StringBuilder();
        for (Iterator<? extends MimeType> iterator = mimeTypes.iterator(); iterator.hasNext(); ) {
            MimeType mimeType = iterator.next();
            mimeType.appendTo(builder);
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    /**
     * Sorts the given list of {@code MimeType} objects by specificity.
     * <p>Given two mime types:
     * <ol>
     * <li>if either mime type has a {@linkplain MimeType#isWildcardType() wildcard type},
     * then the mime type without the wildcard is ordered before the other.</li>
     * <li>if the two mime types have different {@linkplain MimeType#getType() types},
     * then they are considered equal and remain their current order.</li>
     * <li>if either mime type has a {@linkplain MimeType#isWildcardSubtype() wildcard subtype}
     * , then the mime type without the wildcard is sorted before the other.</li>
     * <li>if the two mime types have different {@linkplain MimeType#getSubtype() subtypes},
     * then they are considered equal and remain their current order.</li>
     * <li>if the two mime types have a different amount of
     * {@linkplain MimeType#getParameter(String) parameters}, then the mime type with the most
     * parameters is ordered before the other.</li>
     * </ol>
     * <p>For example: <blockquote>audio/basic &lt; audio/* &lt; *&#047;*</blockquote>
     * <blockquote>audio/basic;level=1 &lt; audio/basic</blockquote>
     * <blockquote>audio/basic == text/html</blockquote> <blockquote>audio/basic ==
     * audio/wave</blockquote>
     *
     * @param mimeTypes the list of mime types to be sorted
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.3.2">HTTP 1.1: Semantics
     * and Content, section 5.3.2</a>
     */
    public static void sortBySpecificity(List<MimeType> mimeTypes) {
        Preconditions.checkNotNull(mimeTypes, "'mimeTypes' must not be null");
        if (mimeTypes.size() > 1) {
            Collections.sort(mimeTypes, SPECIFICITY_COMPARATOR);
        }
    }


    /**
     * Lazily initialize the {@link SecureRandom} for {@link #generateMultipartBoundary()}.
     */
    private static Random initRandom() {
        Random randomToUse = random;
        if (randomToUse == null) {
            synchronized (MimeTypes.class) {
                randomToUse = random;
                if (randomToUse == null) {
                    randomToUse = new SecureRandom();
                    random = randomToUse;
                }
            }
        }
        return randomToUse;
    }

    /**
     * Generate a random MIME boundary as bytes, often used in multipart mime types.
     */
    public static byte[] generateMultipartBoundary() {
        Random randomToUse = initRandom();
        byte[] boundary = new byte[randomToUse.nextInt(11) + 30];
        for (int i = 0; i < boundary.length; i++) {
            boundary[i] = BOUNDARY_CHARS[randomToUse.nextInt(BOUNDARY_CHARS.length)];
        }
        return boundary;
    }

    /**
     * Generate a random MIME boundary as String, often used in multipart mime types.
     */
    public static String generateMultipartBoundaryString() {
        return new String(generateMultipartBoundary(), Charsets.US_ASCII);
    }


    /**
     * Simple Least Recently Used cache, bounded by the maximum size given
     * to the class constructor.
     * <p>This implementation is backed by a {@code ConcurrentHashMap} for storing
     * the cached values and a {@code ConcurrentLinkedQueue} for ordering the keys
     * and choosing the least recently used key when the cache is at full capacity.
     *
     * @param <K> the type of the key used for caching
     * @param <V> the type of the cached values
     */
    private static class ConcurrentLruCache<K, V> {

        private final int maxSize;

        private final ConcurrentLinkedQueue<K> queue = new ConcurrentLinkedQueue<K>();

        private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<K, V>();

        private final ReadWriteLock lock;

        private final Function<K, V> generator;

        private volatile int size = 0;

        public ConcurrentLruCache(int maxSize, Function<K, V> generator) {
            Preconditions.checkTrue(maxSize > 0, "LRU max size should be positive");
            Preconditions.checkNotNull(generator, "Generator function should not be null");
            this.maxSize = maxSize;
            this.generator = generator;
            this.lock = new ReentrantReadWriteLock();
        }

        public V get(K key) {
            V cached = this.cache.get(key);
            if (cached != null) {
                if (this.size < this.maxSize) {
                    return cached;
                }
                try {
                    this.lock.readLock().lock();
                    this.queue.remove(key);
                    this.queue.add(key);
                    return cached;
                } finally {
                    this.lock.readLock().unlock();
                }
            }

            try {
                this.lock.writeLock().lock();
                // Retrying in case of concurrent reads on the same key
                cached = this.cache.get(key);
                if (cached != null) {
                    this.queue.remove(key);
                    this.queue.add(key);
                    return cached;
                }
                // Generate value first, to prevent size inconsistency
                V value = this.generator.apply(key);
                int cacheSize = this.size;
                if (cacheSize == this.maxSize) {
                    K leastUsed = this.queue.poll();
                    if (leastUsed != null) {
                        this.cache.remove(leastUsed);
                        cacheSize--;
                    }
                }
                this.queue.add(key);
                this.cache.put(key, value);
                this.size = cacheSize + 1;
                return value;
            } finally {
                this.lock.writeLock().unlock();
            }
        }
    }

}
