/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.maven.resources;

import java.util.Collections;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.ResourceFlag;
import org.jboss.forge.resources.VirtualResource;

/**
 * MavenDependencyResource
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class MavenDependencyResource extends VirtualResource<Dependency>
{
   private final Dependency dependency;

   public MavenDependencyResource(Resource<?> parent, Dependency dependency)
   {
      super(null, parent);
      this.dependency = dependency;
      setFlag(ResourceFlag.Leaf);
   }

   /**
    * @return the dependency
    */
   public Dependency getDependency()
   {
      return dependency;
   }

   @Override
   public String getName()
   {
      return dependency.getGroupId() + ":" + dependency.getArtifactId();
   }

   @Override
   protected List<Resource<?>> doListResources()
   {
      return Collections.emptyList();
   }

   @Override
   public Dependency getUnderlyingResourceObject()
   {
      return dependency;
   }

   @Override
   public String toString()
   {
      return dependency.getGroupId() + ":" +
               dependency.getArtifactId() + ":" +
               dependency.getVersion() + ":" +
               dependency.getType() + ":" +
               dependency.getScope() + ":" +
               dependency.getClassifier();
   }

   @Override
   public boolean delete() throws UnsupportedOperationException
   {
      throw new UnsupportedOperationException("not supported");
   }

   @Override
   public boolean delete(boolean recursive) throws UnsupportedOperationException
   {
      throw new UnsupportedOperationException("not supported");
   }
}
