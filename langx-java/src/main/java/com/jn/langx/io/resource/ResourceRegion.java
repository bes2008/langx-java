package com.jn.langx.io.resource;


import com.jn.langx.util.Preconditions;

/**
 * Region of a {@link Resource} implementation, materialized by a {@code position}
 * within the {@link Resource} and a byte {@code count} for the length of that region.
 *
 * @author Arjen Poutsma
 * @since 4.3
 */
public class ResourceRegion {

    private final Resource resource;

    private final long position;

    private final long count;


    /**
     * Create a new {@code ResourceRegion} from a given {@link Resource}.
     * This region of a resource is represented by a start {@code position}
     * and a byte {@code count} within the given {@code Resource}.
     * @param resource a Resource
     * @param position the start position of the region in that resource
     * @param count the byte count of the region in that resource
     */
    public ResourceRegion(Resource resource, long position, long count) {
        Preconditions.checkNotNull(resource, "Resource must not be null");
        Preconditions.checkTrue(position >= 0, "'position' must be larger than or equal to 0");
        Preconditions.checkTrue(count >= 0, "'count' must be larger than or equal to 0");
        this.resource = resource;
        this.position = position;
        this.count = count;
    }


    /**
     * Return the underlying {@link Resource} for this {@code ResourceRegion}.
     */
    public Resource getResource() {
        return this.resource;
    }

    /**
     * Return the start position of this region in the underlying {@link Resource}.
     */
    public long getPosition() {
        return this.position;
    }

    /**
     * Return the byte count of this region in the underlying {@link Resource}.
     */
    public long getCount() {
        return this.count;
    }

}
