/*
 * Copyright (c) 2002-2007, Marc Prud'hommeaux. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */

package org.jboss.forge.shell.console.jline.console.history;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;

/**
 * {@link History} using a file for persistent backing.
 * <p/>
 * Implementers should install shutdown hook to call {@link FileHistory#flush} to save history to disk.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 2.0
 */
public class FileHistory
         extends MemoryHistory
         implements PersistentHistory, Flushable
{
   private final File file;

   public FileHistory(final File file) throws IOException
   {
      assert file != null;
      this.file = file;
      load(file);
   }

   public File getFile()
   {
      return file;
   }

   public void load(final File file) throws IOException
   {
      assert file != null;
      if (file.exists())
      {
         org.jboss.forge.shell.console.jline.internal.Log.trace("Loading history from: ", file);
         load(new FileReader(file));
      }
   }

   public void load(final InputStream input) throws IOException
   {
      assert input != null;
      load(new InputStreamReader(input));
   }

   public void load(final Reader reader) throws IOException
   {
      assert reader != null;
      BufferedReader input = new BufferedReader(reader);

      String item;
      while ((item = input.readLine()) != null)
      {
         add(item);
      }
   }

   @Override
   public void flush() throws IOException
   {
      org.jboss.forge.shell.console.jline.internal.Log.trace("Flushing history");

      if (!file.exists())
      {
         File dir = file.getParentFile();
         if (!dir.exists() && !dir.mkdirs())
         {
            org.jboss.forge.shell.console.jline.internal.Log.warn("Failed to create directory: ", dir);
         }
         if (!file.createNewFile())
         {
            org.jboss.forge.shell.console.jline.internal.Log.warn("Failed to create file: ", file);
         }
      }

      PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)));
      try
      {
         for (Entry entry : this)
         {
            out.println(entry.value());
         }
      }
      finally
      {
         out.close();
      }
   }

   @Override
   public void purge() throws IOException
   {
      org.jboss.forge.shell.console.jline.internal.Log.trace("Purging history");

      clear();

      if (!file.delete())
      {
         org.jboss.forge.shell.console.jline.internal.Log.warn("Failed to delete history file: ", file);
      }
   }
}