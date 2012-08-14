/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.shell.command.fshparser;

/**
 * @author Mike Brock .
 */
public class StringTokenNode extends TokenNode
{
   public StringTokenNode(String value)
   {
      this.value = value;
   }
}
