/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.forge.parser.java;

import java.util.List;

import org.jboss.forge.parser.JavaParser;

/**
 * Represents a Java {@link Enum} source file as an in-memory modifiable element. See {@link JavaParser} for various
 * options in generating {@link JavaEnum} instances.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface JavaEnum extends JavaSource<JavaEnum>
{
   /**
    * Add a new {@link EnumConstant}
    */
   EnumConstant<JavaEnum> addEnumConstant();
   
   /**
    * Add a new {@link EnumConstant} using the given declaration.
    */
   EnumConstant<JavaEnum> addEnumConstant(String declaration);
   
   /**
    * Return the {@link EnumConstant} with the given name, or return null if no such constant exists.
    * @param name
    * @return
    */
   EnumConstant<JavaEnum> getEnumConstant(String name);

   /**
    * Return all declared {@link EnumConstant} types for this {@link JavaEnum}
    */
   List<EnumConstant<JavaEnum>> getEnumConstants();
}