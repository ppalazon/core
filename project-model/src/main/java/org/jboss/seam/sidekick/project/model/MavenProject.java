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
package org.jboss.seam.sidekick.project.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Typed;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectBuildingResult;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.logging.console.ConsoleLoggerManager;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jboss.seam.sidekick.project.ProjectModelException;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Typed()
public class MavenProject extends AbstractProject
{
   private File projectRoot = null;

   private final ProjectBuildingRequest request = new DefaultProjectBuildingRequest();
   private DefaultPlexusContainer container = null;
   private ProjectBuilder builder = null;

   public MavenProject() throws ProjectModelException
   {
      this(findProjectDir(), false);
   }

   public MavenProject(final boolean create) throws ProjectModelException
   {
      this(findProjectDir(), create);
   }

   public MavenProject(final String directoryPath) throws ProjectModelException
   {
      this(new File(directoryPath).getAbsoluteFile(), false);
   }

   public MavenProject(final String directoryPath, final boolean create) throws ProjectModelException
   {
      this(new File(directoryPath).getAbsoluteFile(), create);
   }

   public MavenProject(final File directory) throws ProjectModelException
   {
      this(directory, false);
   }

   public MavenProject(final File directory, final boolean create) throws ProjectModelException
   {
      if (!directory.isDirectory())
      {
         throw new ProjectModelException("Cannot load project because the directory [" + directory.getAbsolutePath()
                  + "] does not exist.");
      }

      projectRoot = directory.getAbsoluteFile();
      init(create);
   }

   private void init(final boolean create)
   {
      try
      {
         if (create)
         {
            Model pom = createPOM();
            pom.setGroupId("org.jboss.seam");
            pom.setArtifactId("scaffolding");
            pom.setVersion("1.0.0-SNAPSHOT");
            pom.setPomFile(getPOMFile());
            pom.setModelVersion("4.0.0");
            setPOM(pom);
         }
         else if (!exists())
         {
            throw new ProjectModelException("No POM file found at [" + getPOMFile().getAbsolutePath() + "]");
         }

         container = new DefaultPlexusContainer();
         container.setLoggerManager(new ConsoleLoggerManager("ERROR"));

         builder = container.lookup(ProjectBuilder.class);

         // TODO this needs to be configurable via the project/.sidekick file.
         String localRepository = getUserHomeDir().getAbsolutePath() + "/.m2/repository";

         request.setLocalRepository(new MavenArtifactRepository(
                  "local", new File(localRepository).toURI().toURL().toString(),
                  container.lookup(ArtifactRepositoryLayout.class),
                  new ArtifactRepositoryPolicy(true, ArtifactRepositoryPolicy.UPDATE_POLICY_NEVER,
                           ArtifactRepositoryPolicy.CHECKSUM_POLICY_WARN),
                  new ArtifactRepositoryPolicy(true, ArtifactRepositoryPolicy.UPDATE_POLICY_NEVER,
                           ArtifactRepositoryPolicy.CHECKSUM_POLICY_WARN)));
         request.setRemoteRepositories(new ArrayList<ArtifactRepository>());

         request.setProcessPlugins(true);
         request.setResolveDependencies(true);
         request.setOffline(true);

      }
      catch (Exception e)
      {
         throw new ProjectModelException("Could not initialize maven project located in: " + getProjectRoot(), e);
      }
   }

   public boolean exists()
   {
      return getPOMFile().exists();
   }

   /*
    * POM manipulation methods
    */
   public void addDependency(final Dependency dep)
   {
      if (!hasDependency(dep))
      {
         Model pom = getPOM();
         List<Dependency> dependencies = pom.getDependencies();
         dependencies.add(dep);
         setPOM(pom);
      }
   }

   public boolean hasDependency(final Dependency dep)
   {
      try
      {
         ProjectBuildingResult result = builder.build(getPOMFile(), request);
         List<Dependency> dependencies = result.getProject().getDependencies();

         for (Dependency dependency : dependencies)
         {
            if (areEquivalent(dependency, dep))
            {
               return true;
            }
         }
         return false;
      }
      catch (ProjectBuildingException e)
      {
         throw new ProjectModelException(e);
      }
   }

   public void removeDependency(final Dependency dep)
   {
      Model pom = getPOM();
      List<Dependency> dependencies = pom.getDependencies();

      List<Dependency> toBeRemoved = new ArrayList<Dependency>();
      for (Dependency dependency : dependencies)
      {
         if (areEquivalent(dependency, dep))
         {
            toBeRemoved.add(dependency);
         }
      }
      dependencies.removeAll(toBeRemoved);
      setPOM(pom);
   }

   public Model getPOM()
   {
      try
      {
         Model result = new Model();

         MavenXpp3Reader reader = new MavenXpp3Reader();
         FileInputStream stream = new FileInputStream(getPOMFile());
         if (stream.available() > 0)
         {
            result = reader.read(stream);
         }
         stream.close();

         result.setPomFile(getPOMFile());
         return result;
      }
      catch (IOException e)
      {
         throw new ProjectModelException("Could not open POM file: " + getPOMFile(), e);
      }
      catch (XmlPullParserException e)
      {
         throw new ProjectModelException("Could not parse POM file: " + getPOMFile(), e);
      }
   }

   public void setPOM(final Model pom)
   {
      try
      {
         MavenXpp3Writer writer = new MavenXpp3Writer();
         FileWriter fw = new FileWriter(getPOMFile());
         writer.write(fw, pom);
         fw.close();
      }
      catch (IOException e)
      {
         throw new ProjectModelException("Could not write POM file: " + getPOMFile(), e);
      }
   }

   /*
    * File handle methods
    */

   @Override
   public List<File> getResourceFolders()
   {
      List<File> result = new ArrayList<File>();
      result.add(getResourceFolder());
      result.add(getTestResourceFolder());
      return result;
   }

   @Override
   public File getResourceFolder()
   {
      // TODO maven should resolve this
      return new File(getProjectRoot().getAbsolutePath() + "/src/main/resources");
   }

   @Override
   public File getTestResourceFolder()
   {
      // TODO maven should resolve this
      return new File(getProjectRoot().getAbsolutePath() + "/src/test/resources");
   }

   @Override
   public List<File> getSourceFolders()
   {
      List<File> result = new ArrayList<File>();
      result.add(getSourceFolder());
      result.add(getTestSourceFolder());
      return result;
   }

   @Override
   public File getSourceFolder()
   {
      try
      {
         ProjectBuildingResult result = builder.build(getPOMFile(), request);
         String directory = result.getProject().getBuild().getSourceDirectory();
         return new File(directory).getAbsoluteFile();
      }
      catch (ProjectBuildingException e)
      {
         throw new ProjectModelException(e);
      }
   }

   @Override
   public File getTestSourceFolder()
   {
      try
      {
         ProjectBuildingResult result = builder.build(getPOMFile(), request);
         String directory = result.getProject().getBuild().getTestSourceDirectory();
         return new File(directory).getAbsoluteFile();
      }
      catch (ProjectBuildingException e)
      {
         throw new ProjectModelException(e);
      }
   }

   @Override
   public File getProjectRoot()
   {
      return projectRoot;
   }

   /*
    * Helpers
    */
   private static File getCurrentWorkingDir()
   {
      return new File("").getAbsoluteFile();
   }

   private Model createPOM()
   {
      File pomFile = getPOMFile();
      if (!pomFile.exists())
      {
         try
         {
            pomFile.createNewFile();
         }
         catch (IOException e)
         {
            throw new ProjectModelException("Could not create POM: " + pomFile.getAbsolutePath(), e);
         }
      }
      return getPOM();
   }

   private File getPOMFile()
   {
      File file = new File(getProjectRoot() + "/pom.xml");
      return file;
   }

   private File getUserHomeDir()
   {
      return new File(System.getProperty("user.home")).getAbsoluteFile();
   }

   private static File findProjectDir()
   {
      File root = getCurrentWorkingDir();
      File pom = new File(root + "/pom.xml");
      while (!pom.exists() && (root.getParentFile() != null))
      {
         root = root.getParentFile();
         pom = new File(root + "/pom.xml");
      }

      if (!pom.exists())
      {
         root = new File("");
      }

      return root;
   }

   @SuppressWarnings("unchecked")
   private boolean areEquivalent(final Dependency left, final Dependency right)
   {
      boolean result = false;
      if (left.getGroupId().equals(right.getGroupId()) && left.getArtifactId().equals(right.getArtifactId()))
      {
         ArtifactVersion lversion = new DefaultArtifactVersion(left.getVersion());
         ArtifactVersion rversion = new DefaultArtifactVersion(right.getVersion());

         if (lversion.compareTo(rversion) == 0)
         {
            result = true;
         }
      }
      return result;
   }

}
