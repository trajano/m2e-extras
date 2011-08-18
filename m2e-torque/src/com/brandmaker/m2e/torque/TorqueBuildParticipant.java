package com.brandmaker.m2e.torque;

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

public class TorqueBuildParticipant extends MojoExecutionBuildParticipant
{
	public TorqueBuildParticipant(MojoExecution execution)
	{
		super(execution, true);
	}

	@Override
	public Set<IProject> build(int kind, IProgressMonitor monitor) throws Exception
	{
		BuildContext buildContext = getBuildContext();
		IMaven       maven        = MavenPlugin.getMaven();

		File schemaDir = maven.getMojoParameterValue(
				getSession(), getMojoExecution(), "schemaDir", File.class);

//		String schemaIncludes = maven.getMojoParameterValue(
//				getSession(), getMojoExecution(), "schemaIncludes", String.class);

		Scanner scanner = buildContext.newScanner(schemaDir);
		scanner.scan();

		String[] includedFiles = scanner.getIncludedFiles();

		if (includedFiles == null || includedFiles.length == 0)
		{
			return null;
		}

		Set<IProject> result = super.build(kind, monitor);

		File baseOutputDir = maven.getMojoParameterValue(
				getSession(), getMojoExecution(), "baseOutputDir", File.class);

		if (baseOutputDir != null)
		{
			buildContext.refresh(baseOutputDir);
		}

		File outputDir = maven.getMojoParameterValue(
				getSession(), getMojoExecution(), "outputDir", File.class);

		if (outputDir != null)
		{
			buildContext.refresh(outputDir);
		}

		return result;
	}
}
