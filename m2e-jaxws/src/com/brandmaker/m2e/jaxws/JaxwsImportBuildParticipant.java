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

package com.brandmaker.m2e.jaxws;

import java.io.File;
import java.util.Set;

import org.apache.maven.plugin.MojoExecution;
import org.codehaus.plexus.util.Scanner;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.sonatype.plexus.build.incremental.BuildContext;

public class JaxwsImportBuildParticipant extends MojoExecutionBuildParticipant
{
	public JaxwsImportBuildParticipant(MojoExecution execution)
	{
		super(execution, true);
	}

	@Override
	public Set<IProject> build(int kind, IProgressMonitor monitor) throws Exception
	{
		BuildContext buildContext = getBuildContext();
		IMaven       maven        = MavenPlugin.getMaven();

		File wsdlDirectory = maven.getMojoParameterValue(
				getSession(), getMojoExecution(), "wsdlDirectory", File.class);

		Scanner scanner = buildContext.newScanner(wsdlDirectory);
		scanner.scan();

		String[] includedFiles = scanner.getIncludedFiles();

		if (includedFiles == null || includedFiles.length == 0)
		{
			return null;
		}

		Set<IProject> result = super.build(kind, monitor);

		File sourceDestDir = maven.getMojoParameterValue(
				getSession(), getMojoExecution(), "sourceDestDir", File.class);

		if (sourceDestDir != null)
		{
			buildContext.refresh(sourceDestDir);
		}

		return result;
	}
}
