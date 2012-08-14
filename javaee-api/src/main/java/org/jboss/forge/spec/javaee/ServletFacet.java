/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.spec.javaee;

import java.util.List;

import org.jboss.forge.project.Facet;
import org.jboss.forge.resources.FileResource;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.ResourceFilter;
import org.jboss.shrinkwrap.descriptor.api.spec.servlet.web.WebAppDescriptor;

/**
 * If installed, this {@link Project} supports features from the Servlet specification.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface ServletFacet extends Facet
{
   /**
    * Parse and return this {@link Project}'s web.xml file as a {@link WebAppDescriptor}. If no web.xml exists
    * (particularly in the case of Servlet 3.0 projects), return a virtual web-descriptor instance. This virtual
    * instance may then be modified in memory, then saved using {@link #saveConfig(WebAppDescriptor)}, at which point a
    * physical web.xml file will be created on disk.
    */
   WebAppDescriptor getConfig();

   /**
    * Save the given {@link WebAppDescriptor} as this {@link Project}'s web.xml file. If no web.xml file exists, a new
    * web.xml file will be created.
    */
   void saveConfig(final WebAppDescriptor descriptor);

   /**
    * Return a reference to this {@link Project}'s web.xml file.
    */
   FileResource<?> getConfigFile();

   /**
    * List all files in this {@link Project}'s WebContent directory, recursively.
    */
   List<Resource<?>> getResources();

   /**
    * List all files in this {@link Project}'s WebContent directory, recursively, only if they match the given
    * {@link ResourceFilter}.
    */
   List<Resource<?>> getResources(final ResourceFilter filter);
}
