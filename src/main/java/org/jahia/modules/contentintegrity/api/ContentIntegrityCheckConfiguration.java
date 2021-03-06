package org.jahia.modules.contentintegrity.api;

import java.util.Set;

public interface ContentIntegrityCheckConfiguration {
    Set<String> getConfigurationNames();

    void declareDefaultParameter(String name, Object value, String description);

    void setParameter(String name, Object value) throws IllegalArgumentException;

    Object getParameter(String name) throws IllegalArgumentException;

    String getDescription(String name);
}
