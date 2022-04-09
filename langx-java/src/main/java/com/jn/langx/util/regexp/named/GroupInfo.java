package com.jn.langx.util.regexp.named;


import java.io.Serializable;

/**
 * Contains the position and group index of capture groups
 * from a named pattern
 *
 * @since 4.4.7
 */
class GroupInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pos;
    private int groupIndex;

    /**
     * Constructs a {@code GroupInfo} with a group index and string
     * position
     *
     * @param groupIndex the group index
     * @param pos        the position
     */
    public GroupInfo(int groupIndex, int pos) {
        this.groupIndex = groupIndex;
        this.pos = pos;
    }

    /**
     * Gets the string position of the group within a named pattern
     *
     * @return the position
     */
    public int pos() {
        return pos;
    }

    /**
     * Gets the group index of the named capture group
     *
     * @return the group index
     */
    public int groupIndex() {
        return groupIndex;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof GroupInfo)) {
            return false;
        }
        GroupInfo other = (GroupInfo) obj;
        return (pos == other.pos) && (groupIndex == other.groupIndex);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return pos ^ groupIndex;
    }
}
