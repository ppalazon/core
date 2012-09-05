/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.parser.java.ast;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class TypeDeclarationFinderVisitor extends ASTVisitor
{
   private List<AbstractTypeDeclaration> declarations = new ArrayList<AbstractTypeDeclaration>();

   @Override
   public boolean visit(final TypeDeclaration node)
   {
      declarations.add(node);
      return true;
   }

   @Override
   public boolean visit(final AnnotationTypeDeclaration node)
   {
      declarations.add(node);
      return super.visit(node);
   }

   @Override
   public boolean visit(final EnumDeclaration node)
   {
      declarations.add(node);
      return super.visit(node);
   }

   public List<AbstractTypeDeclaration> getTypeDeclarations()
   {
      return declarations;
   }

}