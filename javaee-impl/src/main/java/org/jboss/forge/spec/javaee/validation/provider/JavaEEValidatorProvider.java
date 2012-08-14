/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.spec.javaee.validation.provider;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jboss.forge.project.dependencies.Dependency;
import org.jboss.forge.spec.javaee.descriptor.ValidationDescriptor;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaEEValidatorProvider implements ValidationProvider
{
   public JavaEEValidatorProvider()
   {}

   @Override
   public ValidationDescriptor getDefaultDescriptor()
   {
      return null;
   }

   @Override
   public Set<Dependency> getDependencies()
   {
      return new HashSet<Dependency>();
   }

   @Override
   public Set<Dependency> getAdditionalDependencies()
   {
      return Collections.emptySet();
   }
}
