package com.jn.langx.util.collection.diff;

import com.jn.langx.util.collection.DiffResult;

/**
 * @author jinuo.fang
 * @param <C> the {@link java.util.Collection} or {@link java.util.Map}
 * @see CollectionDiffResult
 * @see MapDiffResult
 */
public interface CollectionDifferResult<C> extends DiffResult {
    C getAdds();
    C getRemoves();
    C getUpdates();
    C getEquals();
}
