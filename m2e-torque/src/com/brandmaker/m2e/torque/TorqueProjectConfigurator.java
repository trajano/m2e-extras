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

package com.brandmaker.m2e.torque;

import java.io.File;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecution;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.AbstractBuildParticipant;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.m2e.jdt.AbstractJavaProjectConfigurator;

public class TorqueProjectConfigurator extends AbstractJavaProjectConfigurator
{
	@Override
	public AbstractBuildParticipant getBuildParticipant(IMavenProjectFacade projectFacade,
			MojoExecution execution, IPluginExecutionMetadata executionMetadata)
	{
		return new TorqueBuildParticipant(execution);
	}

	@Override
	protected File[] getSourceFolders(ProjectConfigurationRequest request,
			MojoExecution mojoExecution) throws CoreException
	{
		MavenSession session = request.getMavenSession();

		return new File[] {
				getParameterValue("baseOutputDir", File.class, session, mojoExecution),
				getParameterValue("outputDir",     File.class, session, mojoExecution) };
	}
}
