/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.project.facets.events;

import org.jboss.forge.QueuedEvent;
import org.jboss.forge.project.Facet;

/**
 * Event fired when a Facet has been installed.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@QueuedEvent
public final class FacetInstalled
{
   private final Facet facet;

   public FacetInstalled(final Facet facet)
   {
      this.facet = facet;
   }

   public Facet getFacet()
   {
      return facet;
   }

}
