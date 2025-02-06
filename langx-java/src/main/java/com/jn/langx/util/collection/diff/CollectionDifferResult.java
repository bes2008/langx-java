package com.jn.langx.util.collection.diff;

import com.jn.langx.util.collection.DiffResult;

/**
 * Represents the result of comparing differences between two collections or maps.
 * This interface extends DiffResult, providing methods to obtain the differences in collections or maps,
 * including additions, removals, updates, and unchanged parts.
 *
 * @param <C> the {@link java.util.Collection} or {@link java.util.Map}
 * @author jinuo.fang
 * @see CollectionDiffResult
 * @see MapDiffResult
 */
public interface CollectionDifferResult<C> extends DiffResult {
    /**
     * Gets the elements that were added.
     *
     * @return the added elements
     */
    C getAdds();

    /**
     * Gets the elements that were removed.
     *
     * @return the removed elements
     */
    C getRemoves();

    /**
     * Gets the elements that were updated.
     *
     * @return the updated elements
     */
    C getUpdates();

    /**
     * Gets the elements that remained unchanged.
     *
     * @return the unchanged elements
     */
    C getEquals();
}
