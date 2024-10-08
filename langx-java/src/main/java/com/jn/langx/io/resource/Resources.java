package com.jn.langx.io.resource;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Predicate2;
import com.jn.langx.util.io.Channels;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.file.Files;
import com.jn.langx.util.net.URLs;
import com.jn.langx.util.struct.Holder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.List;

public class Resources {
    private Resources() {

    }

    public static <V extends Resource> V loadResource(@NonNull Location location) {
        return Locations.newResource(location.toString());
    }

    public static <V extends Resource> V loadResource(@NonNull String location) {
        return loadResource(location, null);
    }

    public static <V extends Resource> V loadResource(@NonNull String location, @Nullable ClassLoader classLoader) {
        return new DefaultResourceLoader(classLoader).loadResource(location);
    }

    public static FileResource loadFileResource(@NonNull File file) {
        return loadFileResource(Files.getCanonicalPath(file), null);
    }

    public static FileResource loadFileResource(@NonNull String location) {
        return loadFileResource(location, null);
    }

    public static FileResource loadFileResource(@NonNull String location, @Nullable ClassLoader classLoader) {
        Preconditions.checkNotNull(location);
        if (!Strings.startsWith(location, FileResource.PREFIX)) {
            location = FileResource.PREFIX + location;
        }
        return new DefaultResourceLoader(classLoader).loadResource(location);
    }


    public static ClassPathResource loadClassPathResource(@NonNull String location) {
        return loadClassPathResource(location, (ClassLoader) null);
    }

    public static ClassPathResource loadClassPathResource(@NonNull String location, @Nullable ClassLoader classLoader) {
        Preconditions.checkNotNull(location);
        if (!Strings.startsWith(location, ClassPathResource.PREFIX)) {
            location = ClassPathResource.PREFIX + location;
        }
        return new DefaultResourceLoader(classLoader).loadResource(location);
    }

    public static ClassPathResource loadClassPathResource(@NonNull String location, @Nullable Class clazz) {
        Preconditions.checkNotNull(location);
        if (!Strings.startsWith(location, ClassPathResource.PREFIX)) {
            location = ClassPathResource.PREFIX + location;
        }
        return new ClassPathResource(location, clazz);
    }

    public static UrlResource loadUrlResource(@NonNull String location) {
        return loadUrlResource(location, null);
    }

    public static UrlResource loadUrlResource(@NonNull String location, @Nullable ClassLoader classLoader) {
        Preconditions.checkNotNull(URLs.newURL(location), StringTemplates.formatWithPlaceholder("location : {} not a URL", location));
        return new DefaultResourceLoader(classLoader).loadResource(location);
    }

    public static UrlResource loadUrlResource(@NonNull URL url) {
        return new UrlResource(url);
    }


    public static ByteArrayResource asByteArrayResource(@NonNull byte[] byteArray) {
        return new ByteArrayResource(byteArray);
    }

    public static ByteArrayResource asByteArrayResource(@NonNull byte[] byteArray, @Nullable String description) {
        return new ByteArrayResource(byteArray, description);
    }

    public static InputStreamResource asInputStreamResource(@NonNull InputStream inputStream) {
        return new InputStreamResource(inputStream);
    }

    public static InputStreamResource asInputStreamResource(@NonNull InputStream inputStream, @Nullable String description) {
        return new InputStreamResource(inputStream, description);
    }

    /**
     * @param resource the resource location
     * @return whether the resource exists or not
     * @since 3.4.2
     */
    public static boolean exists(String resource) {
        Resource res = loadResource(resource);
        if (res == null) {
            return false;
        }
        return res.exists();
    }

    /**
     * @param resource the resource
     * @return the input stream
     * @since 3.4.2
     */
    public static InputStream getInputStream(String resource) throws IOException {
        Resource res = loadResource(resource);
        if (res == null) {
            throw new IOException(StringTemplates.formatWithPlaceholder("Can't find the resource: {}", resource));
        }
        return res.getInputStream();
    }


    public static void readUsingDelimiter(Resource resource, @NonNull String delimiter, @NonNull final Consumer<byte[]> consumer) {
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            Channels.readUsingDelimiter(inputStream, delimiter, consumer);
        } catch (IOException ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        } finally {
            IOs.close(inputStream);
        }
    }

    public static void readUsingDelimiter(Resource resource, @NonNull String delimiter, @NonNull final Charset charset, @NonNull final Consumer<String> consumer) {
        readUsingDelimiter(resource, delimiter, charset, new Consumer2<Integer, String>() {
            @Override
            public void accept(Integer index, String value) {
                consumer.accept(value);
            }
        });
    }

    public static void readUsingDelimiter(Resource resource, @NonNull String delimiter, @NonNull final Charset charset, @NonNull final Consumer2<Integer, String> consumer) {
        readUsingDelimiter(resource, delimiter, charset, null, consumer, null);
    }

    public static void readUsingDelimiter(Resource resource, @NonNull String delimiter, @NonNull final Charset charset, @Nullable Predicate2<Integer, String> consumePredicate, @NonNull final Consumer2<Integer, String> consumer, @Nullable Predicate2<Integer, String> breakPredicate) {
        ReadableByteChannel channel = null;
        try {
            channel = resource.readableChannel();
            Channels.readUsingDelimiter(channel, delimiter, charset, consumePredicate, consumer, breakPredicate);
        } catch (IOException ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        } finally {
            IOs.close(channel);
        }
    }


    public static void readUsingDelimiter(@NonNull String location, @NonNull String delimiter, @NonNull final Charset charset, @NonNull final Consumer<String> consumer) {
        Preconditions.checkNotNull(location);
        Resource resource = loadResource(location);
        if (resource.exists() && resource.isReadable()) {
            readUsingDelimiter(resource, delimiter, charset, consumer);
        }
    }

    public static void readUsingDelimiter(@NonNull String location, @NonNull String delimiter, @NonNull final Charset charset, @NonNull final Consumer2<Integer, String> consumer) {
        Preconditions.checkNotNull(location);
        Resource resource = loadResource(location);
        if (resource.exists() && resource.isReadable()) {
            readUsingDelimiter(resource, delimiter, charset, consumer);
        }
    }

    public static void readUsingDelimiter(@NonNull String location, @NonNull String delimiter, @NonNull final Charset charset, @Nullable Predicate2<Integer, String> consumePredicate, @NonNull final Consumer2<Integer, String> consumer, @Nullable Predicate2<Integer, String> breakPredicate) {
        Preconditions.checkNotNull(location);
        Resource resource = loadResource(location);
        if (resource.exists() && resource.isReadable()) {
            readUsingDelimiter(resource, delimiter, charset, consumePredicate, consumer, breakPredicate);
        }
    }


    public static void readUsingDelimiter(@NonNull URL url, @NonNull String delimiter, @NonNull final Charset charset, @NonNull final Consumer<String> consumer) {
        readUsingDelimiter(loadUrlResource(url), delimiter, charset, consumer);
    }

    public static void readUsingDelimiter(@NonNull byte[] byteArray, @NonNull String delimiter, @NonNull final Charset charset, @NonNull final Consumer<String> consumer) {
        Resource resource = asByteArrayResource(byteArray);
        if (resource.exists() && resource.isReadable()) {
            readUsingDelimiter(resource, delimiter, charset, consumer);
        }
    }

    public static List<String> readLines(@NonNull Resource resource, @NonNull Charset charset) {
        final List<String> lines = Collects.emptyArrayList();
        readUsingDelimiter(resource, "\n", charset, new Consumer<String>() {
            @Override
            public void accept(String line) {
                lines.add(line);
            }
        });
        return lines;
    }

    public static void readLines(@NonNull Resource resource, @NonNull Charset charset, Consumer<String> consumer) {
        readUsingDelimiter(resource, "\n", charset, consumer);
    }

    public static void readLines(@NonNull Resource resource, @NonNull Charset charset, Consumer2<Integer, String> consumer) {
        readUsingDelimiter(resource, "\n", charset, consumer);
    }


    public static void readLines(@NonNull String location, @NonNull Charset charset, @NonNull final Consumer<String> consumer) {
        readUsingDelimiter(location, "\n", charset, consumer);
    }

    public static void readLines(@NonNull String location, @NonNull Charset charset, @NonNull final Consumer2<Integer, String> consumer) {
        readUsingDelimiter(location, "\n", charset, consumer);
    }

    public static void readLines(@NonNull String location, @NonNull Charset charset, @Nullable Predicate2<Integer, String> consumePredicate, @NonNull final Consumer2<Integer, String> consumer, @Nullable Predicate2<Integer, String> breakPredicate) {
        readUsingDelimiter(location, "\n", charset, consumePredicate, consumer, breakPredicate);
    }

    public static String readFirstLine(@NonNull String location, @NonNull Charset charset) {
        final Holder<String> firstLine = new Holder<String>();
        readLines(location, charset, new Predicate2<Integer, String>() {
            @Override
            public boolean test(Integer index, String line) {
                return index == 0;
            }
        }, new Consumer2<Integer, String>() {
            @Override
            public void accept(Integer index, String line) {
                firstLine.set(line);
            }
        }, new Predicate2<Integer, String>() {
            @Override
            public boolean test(Integer index, String value) {
                return index != 0;
            }
        });
        return firstLine.get();
    }

}
