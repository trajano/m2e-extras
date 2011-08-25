/*******************************************************************************
 * Copyright (C) 2011 BrandMaker GmbH
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Aliaksei Lahachou (BrandMaker GmbH)
 *******************************************************************************/

package com.brandmaker.m2e.torque.tests;

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
public class TorqueGenerationTest extends AbstractMavenProjectTestCase
{
	public void test_p001_simple() throws Exception
	{
		IProject project = importProject(
				"projects/torque/torque-p001/pom.xml",
				new ResolverConfiguration());

		waitForJobsToComplete();

        project.build(IncrementalProjectBuilder.FULL_BUILD,        monitor);
        project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);

        waitForJobsToComplete();
        assertNoErrors(project);

        checkClassPathsPresent(project,
        		"/torque-p001/src/main/generated-java",
        		"/torque-p001/target/generated-sources/torque");

        checkSourcesAccessibleAndSyncronized(project,
        		"src/main/generated-java/com/brandmaker/m2e/torque/entities/User.java",
        		"src/main/generated-java/com/brandmaker/m2e/torque/entities/UserPeer.java",
        		"target/generated-sources/torque/com/brandmaker/m2e/torque/entities/BaseUser.java",
        		"target/generated-sources/torque/com/brandmaker/m2e/torque/entities/BaseUserPeer.java");
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
