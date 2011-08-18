package com.brandmaker.m2e.jaxws.tests;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.m2e.core.project.ResolverConfiguration;
import org.eclipse.m2e.tests.common.AbstractMavenProjectTestCase;

@SuppressWarnings("restriction")
public class JaxwsImportGenerationTest extends AbstractMavenProjectTestCase
{
	public void test_p001_simple() throws Exception
	{
		IProject project = importProject(
				"projects/jaxws/jaxws-p001/pom.xml",
				new ResolverConfiguration());

		waitForJobsToComplete();

        project.build(IncrementalProjectBuilder.FULL_BUILD,        monitor);
        project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);

        waitForJobsToComplete();
        assertNoErrors(project);

        checkClassPathsPresent(project,
        		"/jaxws-p001/target/jaxws/wsimport/java");

        checkSourcesAccessibleAndSyncronized(project,
        		"target/jaxws/wsimport/java/com/brandmaker/m2e/jaxws/example/M2Example.java",
        		"target/jaxws/wsimport/java/com/brandmaker/m2e/jaxws/example/M2Example_Service.java");
	}

	protected void checkClassPathPresent(IProject project, String path) throws Exception
	{
		IClasspathEntry[] cpes = JavaCore.create(project).getRawClasspath();
		Path              p    = new Path(path);

		for (IClasspathEntry cpe : cpes)
		{
			if (cpe.getPath().equals(p))
			{
				return;
			}
		}

		fail("Classpath entry " + path + " was not found.");
	}

	protected void checkClassPathsPresent(IProject project, String... paths) throws Exception
	{
		for (String path : paths)
		{
			checkClassPathPresent(project, path);
		}
	}

	protected void checkSourceAccessibleAndSyncronized(IProject project, String path)
	{
		IFile file = project.getFile(path);

		assertTrue(file.isAccessible());
		assertTrue(file.isSynchronized(IResource.DEPTH_ZERO));
	}

	protected void checkSourcesAccessibleAndSyncronized(IProject project, String... paths)
	{
		for (String path : paths)
		{
			checkSourceAccessibleAndSyncronized(project, path);
		}
	}
}
