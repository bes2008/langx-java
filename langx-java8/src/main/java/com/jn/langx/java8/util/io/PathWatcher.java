package com.jn.langx.java8.util.io;


import com.jn.langx.java8.util.exception.MultiException;
import com.jn.langx.lifecycle.AbstractStatefulLifecycle;
import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.exclusion.IncludeExcludeSet;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.os.Platform;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Watch a Path (and sub directories) for Path changes.
 * <p>
 * Suitable replacement for the old {@link Scanner} implementation.
 * <p>
 * Allows for configured Excludes and Includes using {@link FileSystem#getPathMatcher(String)} syntax.
 * <p>
 * Reports activity via registered {@link PathWatcher.Listener}s
 */
public class PathWatcher extends AbstractStatefulLifecycle implements Runnable {
    public static class Config implements Predicate<Path> {
        public static final int UNLIMITED_DEPTH = -9999;

        private static final String PATTERN_SEP;

        static {
            String sep = File.separator;
            if (File.separatorChar == '\\') {
                sep = "\\\\";
            }
            PATTERN_SEP = sep;
        }

        protected final PathWatcher.Config parent;
        protected final Path path;
        protected final IncludeExcludeSet<PathMatcher, Path> includeExclude;
        protected int recurseDepth = 0; // 0 means no sub-directories are scanned
        protected boolean excludeHidden = false;
        protected long pauseUntil;

        public Config(Path path) {
            this(path, null);
        }

        public Config(Path path, PathWatcher.Config parent) {
            this.parent = parent;
            this.includeExclude = parent == null ? new IncludeExcludeSet<>(PathWatcher.PathMatcherSet.class) : parent.includeExclude;

            Path dir = path;
            if (!Files.exists(path))
                throw new IllegalStateException("Path does not exist: " + path);

            if (!Files.isDirectory(path)) {
                dir = path.getParent();
                includeExclude.addInclusions(new PathWatcher.ExactPathMatcher(path));
                setRecurseDepth(0);
            }

            this.path = dir;
        }

        public PathWatcher.Config getParent() {
            return parent;
        }

        public void setPauseUntil(long time) {
            if (time > pauseUntil)
                pauseUntil = time;
        }

        public boolean isPaused(long now) {
            if (pauseUntil == 0)
                return false;
            if (pauseUntil > now) {
                if (logger.isDebugEnabled())
                    logger.debug("PAUSED {}", this);
                return true;
            }
            if (logger.isDebugEnabled())
                logger.debug("unpaused {}", this);
            pauseUntil = 0;
            return false;
        }

        /**
         * Add an exclude PathMatcher
         *
         * @param matcher the path matcher for this exclude
         */
        public void addExclude(PathMatcher matcher) {
            includeExclude.addExclusions(matcher);
        }

        /**
         * Add an exclude PathMatcher.
         * <p>
         * Note: this pattern is FileSystem specific (so use "/" for Linux and OSX, and "\\" for Windows)
         *
         * @param syntaxAndPattern the PathMatcher syntax and pattern to use
         * @see FileSystem#getPathMatcher(String) for detail on syntax and pattern
         */
        public void addExclude(final String syntaxAndPattern) {
            if (logger.isDebugEnabled())
                logger.debug("Adding exclude: [{}]", syntaxAndPattern);
            addExclude(path.getFileSystem().getPathMatcher(syntaxAndPattern));
        }

        /**
         * Add a <code>glob:</code> syntax pattern exclude reference in a directory relative, os neutral, pattern.
         *
         * <pre>
         *    On Linux:
         *    Config config = new Config(Path("/home/user/example"));
         *    config.addExcludeGlobRelative("*.war") =&gt; "glob:/home/user/example/*.war"
         *
         *    On Windows
         *    Config config = new Config(Path("D:/code/examples"));
         *    config.addExcludeGlobRelative("*.war") =&gt; "glob:D:\\code\\examples\\*.war"
         *
         * </pre>
         *
         * @param pattern the pattern, in unixy format, relative to config.dir
         */
        public void addExcludeGlobRelative(String pattern) {
            addExclude(toGlobPattern(path, pattern));
        }

        /**
         * Exclude hidden files and hidden directories
         */
        public void addExcludeHidden() {
            if (!excludeHidden) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Adding hidden files and directories to exclusions");
                }
                excludeHidden = true;
            }
        }

        /**
         * Add multiple exclude PathMatchers
         *
         * @param syntaxAndPatterns the list of PathMatcher syntax and patterns to use
         * @see FileSystem#getPathMatcher(String) for detail on syntax and pattern
         */
        public void addExcludes(List<String> syntaxAndPatterns) {
            for (String syntaxAndPattern : syntaxAndPatterns) {
                addExclude(syntaxAndPattern);
            }
        }

        /**
         * Add an include PathMatcher
         *
         * @param matcher the path matcher for this include
         */
        public void addInclude(PathMatcher matcher) {
            includeExclude.addInclusion(matcher);
        }

        /**
         * Add an include PathMatcher
         *
         * @param syntaxAndPattern the PathMatcher syntax and pattern to use
         * @see FileSystem#getPathMatcher(String) for detail on syntax and pattern
         */
        public void addInclude(String syntaxAndPattern) {
            if (logger.isDebugEnabled()) {
                logger.debug("Adding include: [{}]", syntaxAndPattern);
            }
            addInclude(path.getFileSystem().getPathMatcher(syntaxAndPattern));
        }

        /**
         * Add a <code>glob:</code> syntax pattern reference in a directory relative, os neutral, pattern.
         *
         * <pre>
         *    On Linux:
         *    Config config = new Config(Path("/home/user/example"));
         *    config.addIncludeGlobRelative("*.war") =&gt; "glob:/home/user/example/*.war"
         *
         *    On Windows
         *    Config config = new Config(Path("D:/code/examples"));
         *    config.addIncludeGlobRelative("*.war") =&gt; "glob:D:\\code\\examples\\*.war"
         *
         * </pre>
         *
         * @param pattern the pattern, in unixy format, relative to config.dir
         */
        public void addIncludeGlobRelative(String pattern) {
            addInclude(toGlobPattern(path, pattern));
        }

        /**
         * Add multiple include PathMatchers
         *
         * @param syntaxAndPatterns the list of PathMatcher syntax and patterns to use
         * @see FileSystem#getPathMatcher(String) for detail on syntax and pattern
         */
        public void addIncludes(List<String> syntaxAndPatterns) {
            for (String syntaxAndPattern : syntaxAndPatterns) {
                addInclude(syntaxAndPattern);
            }
        }

        /**
         * Build a new config from a this configuration.
         * <p>
         * Useful for working with sub-directories that also need to be watched.
         *
         * @param dir the directory to build new Config from (using this config as source of includes/excludes)
         * @return the new Config
         */
        public PathWatcher.Config asSubConfig(Path dir) {
            PathWatcher.Config subconfig = new PathWatcher.Config(dir, this);
            if (dir == this.path)
                throw new IllegalStateException("sub " + dir.toString() + " of " + this);

            if (this.recurseDepth == UNLIMITED_DEPTH)
                subconfig.recurseDepth = UNLIMITED_DEPTH;
            else
                subconfig.recurseDepth = this.recurseDepth - (dir.getNameCount() - this.path.getNameCount());

            if (logger.isDebugEnabled())
                logger.debug("subconfig {} of {}", subconfig, path);
            return subconfig;
        }

        public int getRecurseDepth() {
            return recurseDepth;
        }

        public boolean isRecurseDepthUnlimited() {
            return this.recurseDepth == UNLIMITED_DEPTH;
        }

        public Path getPath() {
            return this.path;
        }

        public Path resolve(Path path) {
            if (Files.isDirectory(this.path))
                return this.path.resolve(path);
            if (Files.exists(this.path))
                return this.path;
            return path;
        }

        @Override
        public boolean test(Path path) {
            if (excludeHidden && isHidden(path)) {
                if (logger.isDebugEnabled())
                    logger.debug("test({}) -> [Hidden]", toShortPath(path));
                return false;
            }

            if (!path.startsWith(this.path)) {
                if (logger.isDebugEnabled())
                    logger.debug("test({}) -> [!child {}]", toShortPath(path), this.path);
                return false;
            }

            if (recurseDepth != UNLIMITED_DEPTH) {
                int depth = path.getNameCount() - this.path.getNameCount() - 1;

                if (depth > recurseDepth) {
                    if (logger.isDebugEnabled())
                        logger.debug("test({}) -> [depth {}>{}]", toShortPath(path), depth, recurseDepth);
                    return false;
                }
            }

            boolean matched = includeExclude.test(path);

            if (logger.isDebugEnabled())
                logger.debug("test({}) -> {}", toShortPath(path), matched);

            return matched;
        }

        /**
         * Set the recurse depth for the directory scanning.
         * <p>
         * -999 indicates arbitrarily deep recursion, 0 indicates no recursion, 1 is only one directory deep, and so on.
         *
         * @param depth the number of directories deep to recurse
         */
        public void setRecurseDepth(int depth) {
            this.recurseDepth = depth;
        }

        private String toGlobPattern(Path path, String subPattern) {
            StringBuilder s = new StringBuilder();
            s.append("glob:");

            boolean needDelim = false;

            // Add root (aka "C:\" for Windows)
            Path root = path.getRoot();
            if (root != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Path: {} -> Root: {}", path, root);
                }
                for (char c : root.toString().toCharArray()) {
                    if (c == '\\') {
                        s.append(PATTERN_SEP);
                    } else {
                        s.append(c);
                    }
                }
            } else {
                needDelim = true;
            }

            // Add the individual path segments
            for (Path segment : path) {
                if (needDelim) {
                    s.append(PATTERN_SEP);
                }
                s.append(segment);
                needDelim = true;
            }

            // Add the sub pattern (if specified)
            if ((subPattern != null) && (subPattern.length() > 0)) {
                if (needDelim) {
                    s.append(PATTERN_SEP);
                }
                for (char c : subPattern.toCharArray()) {
                    if (c == '/') {
                        s.append(PATTERN_SEP);
                    } else {
                        s.append(c);
                    }
                }
            }

            return s.toString();
        }

        PathWatcher.DirAction handleDir(Path path) {
            try {
                if (!Files.isDirectory(path))
                    return PathWatcher.DirAction.IGNORE;
                if (excludeHidden && isHidden(path))
                    return PathWatcher.DirAction.IGNORE;
                if (getRecurseDepth() == 0)
                    return PathWatcher.DirAction.WATCH;
                return PathWatcher.DirAction.ENTER;
            } catch (Exception e) {
                logger.trace("IGNORED", e);
                return PathWatcher.DirAction.IGNORE;
            }
        }

        public boolean isHidden(Path path) {
            try {
                if (!path.startsWith(this.path))
                    return true;
                for (int i = this.path.getNameCount(); i < path.getNameCount(); i++) {
                    if (path.getName(i).toString().startsWith(".")) {
                        return true;
                    }
                }
                return Files.exists(path) && Files.isHidden(path);
            } catch (IOException e) {
                logger.trace("IGNORED", e);
                return false;
            }
        }

        public String toShortPath(Path path) {
            if (!path.startsWith(this.path))
                return path.toString();
            return this.path.relativize(path).toString();
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append(path).append(" [depth=");
            if (recurseDepth == UNLIMITED_DEPTH)
                s.append("UNLIMITED");
            else
                s.append(recurseDepth);
            s.append(']');
            return s.toString();
        }
    }

    public enum DirAction {
        IGNORE, WATCH, ENTER
    }

    /**
     * Listener for path change events
     */
    public interface Listener extends EventListener {
        void onPathWatchEvent(PathWatcher.PathWatchEvent event);
    }

    /**
     * EventListListener
     * <p>
     * Listener that reports accumulated events in one shot
     */
    public interface EventListListener extends EventListener {
        void onPathWatchEvents(List<PathWatcher.PathWatchEvent> events);
    }

    /**
     * PathWatchEvent
     * <p>
     * Represents a file event. Reported to registered listeners.
     */
    public class PathWatchEvent {
        private final Path path;
        private final PathWatcher.PathWatchEventType type;
        private final PathWatcher.Config config;
        long checked;
        long modified;
        long length;

        public PathWatchEvent(Path path, PathWatcher.PathWatchEventType type, PathWatcher.Config config) {
            this.path = path;
            this.type = type;
            this.config = config;
            checked = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
            check();
        }

        public PathWatcher.Config getConfig() {
            return config;
        }

        public PathWatchEvent(Path path, WatchEvent<Path> event, PathWatcher.Config config) {
            this.path = path;
            if (event.kind() == ENTRY_CREATE) {
                this.type = PathWatcher.PathWatchEventType.ADDED;
            } else if (event.kind() == ENTRY_DELETE) {
                this.type = PathWatcher.PathWatchEventType.DELETED;
            } else if (event.kind() == ENTRY_MODIFY) {
                this.type = PathWatcher.PathWatchEventType.MODIFIED;
            } else {
                this.type = PathWatcher.PathWatchEventType.UNKNOWN;
            }
            this.config = config;
            checked = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
            check();
        }

        private void check() {
            if (Files.exists(path)) {
                try {
                    modified = Files.getLastModifiedTime(path).toMillis();
                    length = Files.size(path);
                } catch (IOException e) {
                    modified = -1;
                    length = -1;
                }
            } else {
                modified = -1;
                length = -1;
            }
        }

        public boolean isQuiet(long now, long quietTime) {
            long lastModified = modified;
            long lastLength = length;

            check();

            if (lastModified == modified && lastLength == length)
                return (now - checked) >= quietTime;

            checked = now;
            return false;
        }

        public long toQuietCheck(long now, long quietTime) {
            long check = quietTime - (now - checked);
            if (check <= 0)
                return quietTime;
            return check;
        }

        public void modified() {
            long now = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
            checked = now;
            check();
            config.setPauseUntil(now + getUpdateQuietTimeMillis());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            PathWatcher.PathWatchEvent other = (PathWatcher.PathWatchEvent) obj;
            if (path == null) {
                if (other.path != null) {
                    return false;
                }
            } else if (!path.equals(other.path)) {
                return false;
            }
            return type == other.type;
        }

        public Path getPath() {
            return path;
        }

        public PathWatcher.PathWatchEventType getType() {
            return type;
        }

        @Override
        public int hashCode() {
            return Objs.hash(path, type);
        }

        @Override
        public String toString() {
            return String.format("PathWatchEvent[%8s|%s]", type, path);
        }
    }

    /**
     * PathWatchEventType
     * <p>
     * Type of an event
     */
    public enum PathWatchEventType {
        ADDED, DELETED, MODIFIED, UNKNOWN
    }

    private static final Logger logger = Loggers.getLogger(PathWatcher.class);

    @SuppressWarnings("unchecked")
    protected static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    private static final WatchEvent.Kind<?>[] WATCH_EVENT_KINDS = {ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY};
    private static final WatchEvent.Kind<?>[] WATCH_DIR_KINDS = {ENTRY_CREATE, ENTRY_DELETE};

    private WatchService watchService;

    private final List<PathWatcher.Config> configs = new ArrayList<>();
    private final Map<WatchKey, PathWatcher.Config> keys = new ConcurrentHashMap<>();
    private final List<EventListener> listeners = new CopyOnWriteArrayList<>(); //a listener may modify the listener list directly or by stopping the PathWatcher

    private final Map<Path, PathWatcher.PathWatchEvent> pending = new LinkedHashMap<>(32, (float) 0.75, false);
    private final List<PathWatcher.PathWatchEvent> events = new ArrayList<>();

    /**
     * Update Quiet Time - set to 1000 ms as default (a lower value in Windows is not supported)
     */
    private long updateQuietTimeDuration = 1000;
    private TimeUnit updateQuietTimeUnit = TimeUnit.MILLISECONDS;
    private Thread thread;
    private boolean notifyExistingOnStart = true;

    /**
     * Construct new PathWatcher
     */
    public PathWatcher() {
    }

    public Collection<PathWatcher.Config> getConfigs() {
        return configs;
    }

    /**
     * Request watch on a the given path (either file or dir)
     * using all Config defaults. In the case of a dir,
     * the default is not to recurse into subdirs for watching.
     *
     * @param file the path to watch
     */
    public void watch(final Path file) {
        //Make a config for the dir above it and
        //include a match only for the given path
        //using all defaults for the configuration
        Path abs = file;
        if (!abs.isAbsolute()) {
            abs = file.toAbsolutePath();
        }

        //Check we don't already have a config for the parent directory. 
        //If we do, add in this filename.
        PathWatcher.Config config = null;
        Path parent = abs.getParent();
        for (PathWatcher.Config c : configs) {
            if (c.getPath().equals(parent)) {
                config = c;
                break;
            }
        }

        //Make a new config
        if (config == null) {
            config = new PathWatcher.Config(abs.getParent());
            // the include for the directory itself
            config.addIncludeGlobRelative("");
            //add the include for the file
            config.addIncludeGlobRelative(file.getFileName().toString());
            watch(config);
        } else {
            //add the include for the file
            config.addIncludeGlobRelative(file.getFileName().toString());
        }
    }

    /**
     * Request watch on a path with custom Config
     * provided.
     *
     * @param config the configuration to watch
     */
    public void watch(final PathWatcher.Config config) {
        //Add a custom config
        configs.add(config);
    }


    /**
     * Add a listener for changes the watcher notices.
     *
     * @param listener change listener
     */
    public void addListener(EventListener listener) {
        listeners.add(listener);
    }

    /**
     * Append some info on the paths that we are watching.
     */
    private void appendConfigId(StringBuilder s) {
        List<Path> dirs = new ArrayList<>();

        for (PathWatcher.Config config : keys.values()) {
            dirs.add(config.path);
        }

        Collections.sort(dirs);

        s.append("[");
        if (!dirs.isEmpty()) {
            s.append(dirs.get(0));
            if (dirs.size() > 1) {
                s.append(" (+").append(dirs.size() - 1).append(")");
            }
        } else {
            s.append("<null>");
        }
        s.append("]");
    }

    @Override
    protected void doStart() throws Exception {
        //create a new watch service
        this.watchService = FileSystems.getDefault().newWatchService();

        //ensure setting of quiet time is appropriate now we have a watcher
        setUpdateQuietTime(getUpdateQuietTimeMillis(), TimeUnit.MILLISECONDS);

        // Register all watched paths, walking dir hierarchies as needed, possibly generating
        // fake add events if notifyExistingOnStart is true
        for (PathWatcher.Config c : configs) {
            registerTree(c.getPath(), c, isNotifyExistingOnStart());
        }

        // Start Thread for watcher take/pollKeys loop
        StringBuilder threadId = new StringBuilder();
        threadId.append("PathWatcher@");
        threadId.append(Integer.toHexString(hashCode()));
        if (logger.isDebugEnabled())
            logger.debug("{} -> {}", this, threadId);

        thread = new Thread(this, threadId.toString());
        thread.setDaemon(true);
        thread.start();
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        if (watchService != null)
            watchService.close(); //will invalidate registered watch keys, interrupt thread in take or poll
        watchService = null;
        thread = null;
        keys.clear();
        pending.clear();
        events.clear();
        super.doStop();
    }

    /**
     * Remove all current configs and listeners.
     */
    public void reset() {
        if (!isStopped()) {
            throw new IllegalStateException("PathWatcher must be stopped before reset.");
        }
        configs.clear();
        listeners.clear();
    }

    /**
     * Check to see if the watcher is in a state where it should generate
     * watch events to the listeners. Used to determine if watcher should generate
     * events for existing files and dirs on startup.
     *
     * @return true if the watcher should generate events to the listeners.
     */
    protected boolean isNotifiable() {
        return (isStarted() || (!isStarted() && isNotifyExistingOnStart()));
    }

    /**
     * Get an iterator over the listeners.
     *
     * @return iterator over the listeners.
     */
    public Iterator<EventListener> getListeners() {
        return listeners.iterator();
    }

    /**
     * Change the quiet time.
     *
     * @return the quiet time in millis
     */
    public long getUpdateQuietTimeMillis() {
        return TimeUnit.MILLISECONDS.convert(updateQuietTimeDuration, updateQuietTimeUnit);
    }

    private void registerTree(Path dir, PathWatcher.Config config, boolean notify) throws IOException {
        if (logger.isDebugEnabled())
            logger.debug("registerTree {} {} {}", dir, config, notify);

        if (!Files.isDirectory(dir))
            throw new IllegalArgumentException(dir.toString());

        register(dir, config);

        final MultiException me = new MultiException();
        try (Stream<Path> stream = Files.list(dir)) {
            stream.forEach(p ->
            {
                if (logger.isDebugEnabled())
                    logger.debug("registerTree? {}", p);

                try {
                    if (notify && config.test(p))
                        pending.put(p, new PathWatchEvent(p, PathWatchEventType.ADDED, config));

                    switch (config.handleDir(p)) {
                        case ENTER:
                            registerTree(p, config.asSubConfig(p), notify);
                            break;
                        case WATCH:
                            registerDir(p, config);
                            break;
                        case IGNORE:
                            break;
                        default:
                            break;
                    }
                } catch (IOException e) {
                    me.add(e);
                }
            });
        }
        try {
            me.ifExceptionThrow();
        } catch (IOException e) {
            throw e;
        } catch (Exception th) {
            throw new IOException(th);
        }
    }

    private void registerDir(Path path, PathWatcher.Config config) throws IOException {
        if (logger.isDebugEnabled())
            logger.debug("registerDir {} {}", path, config);

        if (!Files.isDirectory(path))
            throw new IllegalArgumentException(path.toString());

        register(path, config.asSubConfig(path), WATCH_DIR_KINDS);
    }

    protected void register(Path path, PathWatcher.Config config) throws IOException {
        if (logger.isDebugEnabled())
            logger.debug("Registering watch on {}", path);

        register(path, config, WATCH_EVENT_KINDS);
    }

    private void register(Path path, PathWatcher.Config config, WatchEvent.Kind<?>[] kinds) throws IOException {
        // Native Watcher
        WatchKey key = path.register(watchService, kinds);
        keys.put(key, config);
    }

    /**
     * Delete a listener
     *
     * @param listener the listener to remove
     * @return true if the listener existed and was removed
     */
    public boolean removeListener(PathWatcher.Listener listener) {
        return listeners.remove(listener);
    }

    /**
     * Forever loop.
     * <p>
     * Wait for the WatchService to report some filesystem events for the
     * watched paths.
     * <p>
     * When an event for a path first occurs, it is subjected to a quiet time.
     * Subsequent events that arrive for the same path during this quiet time are
     * accumulated and the timer reset. Only when the quiet time has expired are
     * the accumulated events sent. MODIFY events are handled slightly differently -
     * multiple MODIFY events arriving within a quiet time are coalesced into a
     * single MODIFY event. Both the accumulation of events and coalescing of MODIFY
     * events reduce the number and frequency of event reporting for "noisy" files (ie
     * those that are undergoing rapid change).
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        // Start the java.nio watching
        if (logger.isDebugEnabled()) {
            logger.debug("Starting java.nio file watching with {}", watchService);
        }

        long waitTime = getUpdateQuietTimeMillis();

        WatchService watch = watchService;

        while (isRunning() && thread == Thread.currentThread()) {
            WatchKey key;

            try {
                // Reset all keys before watching
                long now = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
                for (Map.Entry<WatchKey, PathWatcher.Config> e : keys.entrySet()) {
                    WatchKey k = e.getKey();
                    PathWatcher.Config c = e.getValue();

                    if (!c.isPaused(now) && !k.reset()) {
                        keys.remove(k);
                        if (keys.isEmpty()) {
                            return; // all done, no longer monitoring anything
                        }
                    }
                }

                if (logger.isDebugEnabled()) {
                    logger.debug("Waiting for poll({})", waitTime);
                }
                key = waitTime < 0 ? watch.take() : waitTime > 0 ? watch.poll(waitTime, updateQuietTimeUnit) : watch.poll();

                // handle all active keys
                while (key != null) {
                    handleKey(key);
                    key = watch.poll();
                }

                waitTime = processPending();

                notifyEvents();
            } catch (ClosedWatchServiceException e) {
                // Normal shutdown of watcher
                return;
            } catch (InterruptedException e) {
                if (isRunning()) {
                    logger.warn("Watch failed", e);
                } else {
                    logger.trace("IGNORED", e);
                }
                Thread.currentThread().interrupt();
            }
        }
    }

    private void handleKey(WatchKey key) {
        PathWatcher.Config config = keys.get(key);
        if (config == null) {
            if (logger.isDebugEnabled())
                logger.debug("WatchKey not recognized: {}", key);
            return;
        }

        for (WatchEvent<?> event : key.pollEvents()) {
            WatchEvent<Path> ev = cast(event);
            Path name = ev.context();
            Path path = config.resolve(name);

            if (logger.isDebugEnabled())
                logger.debug("handleKey? {} {} {}", ev.kind(), config.toShortPath(path), config);

            // Ignore modified events on directories.  These are handled as create/delete events of their contents
            if (ev.kind() == ENTRY_MODIFY && Files.exists(path) && Files.isDirectory(path))
                continue;

            if (config.test(path))
                handleWatchEvent(path, new PathWatcher.PathWatchEvent(path, ev, config));
            else if (config.getRecurseDepth() == -1) {
                // Convert a watched directory into a modify event on its parent
                Path parent = path.getParent();
                PathWatcher.Config parentConfig = config.getParent();
                handleWatchEvent(parent, new PathWatcher.PathWatchEvent(parent, PathWatcher.PathWatchEventType.MODIFIED, parentConfig));
                continue;
            }

            if (ev.kind() == ENTRY_CREATE) {
                try {
                    switch (config.handleDir(path)) {
                        case ENTER:
                            registerTree(path, config.asSubConfig(path), true);
                            break;
                        case WATCH:
                            registerDir(path, config);
                            break;
                        case IGNORE:
                        default:
                            break;
                    }
                } catch (IOException e) {
                    logger.warn("Unable to register", e);
                }
            }
        }
    }

    /**
     * Add an event reported by the WatchService to list of pending events
     * that will be sent after their quiet time has expired.
     *
     * @param path  the path to add to the pending list
     * @param event the pending event
     */
    public void handleWatchEvent(Path path, PathWatcher.PathWatchEvent event) {
        PathWatcher.PathWatchEvent existing = pending.get(path);

        if (logger.isDebugEnabled())
            logger.debug("handleWatchEvent {} {} <= {}", path, event, existing);

        switch (event.getType()) {
            case ADDED:
                if (existing != null && existing.getType() == PathWatcher.PathWatchEventType.MODIFIED)
                    events.add(new PathWatcher.PathWatchEvent(path, PathWatcher.PathWatchEventType.DELETED, existing.getConfig()));
                pending.put(path, event);
                break;

            case MODIFIED:
                if (existing == null)
                    pending.put(path, event);
                else
                    existing.modified();
                break;

            case DELETED:
            case UNKNOWN:
                if (existing != null)
                    pending.remove(path);
                events.add(event);
                break;

            default:
                throw new IllegalStateException(event.toString());
        }
    }

    private long processPending() {
        if (logger.isDebugEnabled())
            logger.debug("processPending> {}", pending.values());

        long now = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        long wait = Long.MAX_VALUE;

        // pending map is maintained in LRU order
        for (PathWatcher.PathWatchEvent event : new ArrayList<>(pending.values())) {
            Path path = event.getPath();
            // for directories, wait until parent is quiet
            if (pending.containsKey(path.getParent()))
                continue;

            // if the path is quiet move to events
            if (event.isQuiet(now, getUpdateQuietTimeMillis())) {
                if (logger.isDebugEnabled())
                    logger.debug("isQuiet {}", event);
                pending.remove(path);
                events.add(event);
            } else {
                long msToCheck = event.toQuietCheck(now, getUpdateQuietTimeMillis());
                if (logger.isDebugEnabled())
                    logger.debug("pending {} {}", event, msToCheck);
                if (msToCheck < wait)
                    wait = msToCheck;
            }
        }
        if (logger.isDebugEnabled())
            logger.debug("processPending< {}", pending.values());
        return wait == Long.MAX_VALUE ? -1 : wait;
    }

    private void notifyEvents() {
        if (logger.isDebugEnabled()) {
            logger.debug("notifyEvents {}", events.size());
        }
        if (events.isEmpty()) {
            return;
        }

        boolean eventListeners = false;
        for (EventListener listener : listeners) {
            if (listener instanceof PathWatcher.EventListListener) {
                try {
                    if (logger.isDebugEnabled()) {
                        logger.debug("notifyEvents {} {}", listener, events);
                    }
                    ((PathWatcher.EventListListener) listener).onPathWatchEvents(events);
                } catch (Exception t) {
                    logger.warn("Unable to notify PathWatch Events", t);
                }
            } else {
                eventListeners = true;
            }
        }

        if (eventListeners) {
            for (PathWatcher.PathWatchEvent event : events) {
                if (logger.isDebugEnabled()) {
                    logger.debug("notifyEvent {} {}", event, listeners);
                }
                for (EventListener listener : listeners) {
                    if (listener instanceof PathWatcher.Listener) {
                        try {
                            ((PathWatcher.Listener) listener).onPathWatchEvent(event);
                        } catch (Exception t) {
                            logger.warn("Unable to notify PathWatch Events", t);
                        }
                    }
                }
            }
        }

        events.clear();
    }

    /**
     * Whether or not to issue notifications for directories and files that
     * already exist when the watcher starts.
     *
     * @param notify true if existing paths should be notified or not
     */
    public void setNotifyExistingOnStart(boolean notify) {
        notifyExistingOnStart = notify;
    }

    public boolean isNotifyExistingOnStart() {
        return notifyExistingOnStart;
    }

    /**
     * Set the quiet time.
     *
     * @param duration the quiet time duration
     * @param unit     the quite time unit
     */
    public void setUpdateQuietTime(long duration, TimeUnit unit) {
        long desiredMillis = unit.toMillis(duration);

        if (Platform.isWindows && (desiredMillis < 1000)) {
            logger.warn("Quiet Time is too low for Microsoft Windows: {} < 1000 ms (defaulting to 1000 ms)", desiredMillis);
            this.updateQuietTimeDuration = 1000;
            this.updateQuietTimeUnit = TimeUnit.MILLISECONDS;
            return;
        }

        // All other OS and watch service combinations can use desired setting
        this.updateQuietTimeDuration = duration;
        this.updateQuietTimeUnit = unit;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(this.getClass().getName());
        appendConfigId(s);
        return s.toString();
    }

    private static class ExactPathMatcher implements PathMatcher {
        private final Path path;

        ExactPathMatcher(Path path) {
            this.path = path;
        }

        @Override
        public boolean matches(Path path) {
            return this.path.equals(path);
        }
    }

    public static class PathMatcherSet extends HashSet<PathMatcher> implements Predicate<Path> {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean test(Path path) {
            for (PathMatcher pm : this) {
                if (pm.matches(path)) {
                    return true;
                }
            }
            return false;
        }
    }
}
