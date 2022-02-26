package com.jn.langx.exception;

import com.jn.langx.text.StringTemplates;

/**
 * @since 4.3.2
 */
public class ResourceNotFoundException extends RuntimeException {

    private String resourceType;
    private String resourceId;

    public ResourceNotFoundException(String resourceType, String resourceId) {
        super(StringTemplates.formatWithPlaceholder("not found {} resource, id or name: {}", resourceType, resourceId));
        this.resourceId = resourceId;
        this.resourceType = resourceType;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }
}
