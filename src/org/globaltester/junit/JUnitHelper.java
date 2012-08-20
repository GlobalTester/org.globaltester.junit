package org.globaltester.junit;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.globaltester.cardconfiguration.GtCardConfigProject;
import org.globaltester.core.resources.GtResourceHelper;
import org.globaltester.testrunner.GtTestCampaignProject;
import org.globaltester.testspecification.GtTestSpecProject;

/**
 * Convenience class that provides several methods to simplify the creation 
 * of JUnit tests. Especially the creation of standardized workspace contents.
 * 
 * @author mboonk
 * 
 */
public class JUnitHelper {
	
	public static final String testSpec = "Default TestSpecification";
	public static final String cardConfig = "Default CardConfig";
	public static final String testCampaign = "Default TestCampaign";
	static String subfolder = "/files/";
	
	/**
	 * Delete all projects from the workspace.
	 * @throws CoreException
	 */
	public static void emptyWorkspace() throws CoreException{
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		for (IProject project : projects) {
			project.delete(true, true, new NullProgressMonitor());
		}
	}
	
	/**
	 * Create a simple default TestSpecification for testing.
	 * @throws IOException
	 * @throws CoreException
	 */
	public static void createDefaultTestSpec() throws IOException, CoreException{
		IProject project = GtTestSpecProject.createProject(testSpec, null);
		GtResourceHelper.copyPluginFilesToWorkspaceProject(Activator.PLUGIN_ID, project, subfolder + testSpec, "TestCases", "testSpecification.xml");
		ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
	}
	
	/**
	 * Create a default CardConfig for testing.
	 * @throws IOException
	 * @throws CoreException
	 */
	public static void createDefaultCardConfig() throws IOException, CoreException{
		IProject project = GtCardConfigProject.createProject(cardConfig, null);
		GtResourceHelper.copyPluginFilesToWorkspaceProject(Activator.PLUGIN_ID, project, subfolder + cardConfig, "cardconfig.xml");
		ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
	}
	
	/**
	 * Create a default TestCampaign for testing.
	 * @throws IOException
	 * @throws CoreException
	 */
	public static void createDefaultCreateDefaultTestCampaign() throws IOException, CoreException{
		IProject project = GtTestCampaignProject.createProject(testCampaign, null);
		GtResourceHelper.copyPluginFilesToWorkspaceProject(Activator.PLUGIN_ID, project, subfolder + testCampaign, "ExecutionState", "TestResults", "TestSpecification", "testCampaign.xml");
		ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
	
	}
	
	/**
	 * Delete a folder recursively
	 * 
	 * @param toDelete
	 */
	public static void recursiveDelete(File toDelete) {
		if (toDelete.isDirectory()) {
			String[] files = toDelete.list();
			for (String file : files) {
				File current = new File(toDelete, file);
				recursiveDelete(current);
			}
		}
		toDelete.delete();
	}
}
