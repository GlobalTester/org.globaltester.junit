package org.globaltester.junit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.globaltester.cardconfiguration.GtCardConfigProject;
import org.globaltester.core.resources.GtResourceHelper;
import org.globaltester.testrunner.GtTestCampaignProject;
import org.globaltester.testspecification.GtTestSpecProject;
import org.osgi.framework.Bundle;

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
	
	public static IProject createEmptyProject(String name) throws CoreException{
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
		project.create(new NullProgressMonitor());
		project.open(new NullProgressMonitor());
		return project;
	}
	
	/**
	 * Create a simple default TestSpecification for testing.
	 * @throws IOException
	 * @throws CoreException
	 */
	public static IProject createDefaultTestSpec() throws IOException, CoreException{
		IProject project = GtTestSpecProject.createProject(testSpec, null);
		GtResourceHelper.copyPluginFilesToWorkspaceProject(Activator.PLUGIN_ID, project, subfolder + testSpec, "TestCases", "testSpecification.xml");
		ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
		return project;
	}
	
	/**
	 * Create a default CardConfig for testing.
	 * @throws IOException
	 * @throws CoreException
	 */
	public static IProject createDefaultCardConfig() throws IOException, CoreException{
		IProject project = GtCardConfigProject.createProject(cardConfig, null);
		GtResourceHelper.copyPluginFilesToWorkspaceProject(Activator.PLUGIN_ID, project, subfolder + cardConfig, "cardconfig.xml");
		ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
		return project;
	}
	
	/**
	 * Create a default TestCampaign for testing.
	 * @throws IOException
	 * @throws CoreException
	 */
	public static IProject createDefaultCreateDefaultTestCampaign() throws IOException, CoreException{
		IProject project = GtTestCampaignProject.createProject(testCampaign, null);
		GtResourceHelper.copyPluginFilesToWorkspaceProject(Activator.PLUGIN_ID, project, subfolder + testCampaign, "ExecutionState", "TestResults", "TestSpecification", "testCampaign.xml");
		ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
		return project;
	
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
	
	/**
	 * Create a temporary file containing the given bytes.
	 * 
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public static File createTemporaryFile(byte [] content) throws IOException{
		File tempFile = File.createTempFile("GtTempFile", "");
		FileOutputStream out = new FileOutputStream(tempFile);
		out.write(content);
		out.close();
		return tempFile;
	}
	
	public static File createTemporaryFile(String name) throws IOException{
		File result = new File(createTemporaryFolder(), name);
		result.createNewFile();
		return result;
	}
	
	public static File createTemporaryFolder() throws IOException{
		File tempFile = File.createTempFile("GtTempFolder", "");
		tempFile.delete();
		tempFile.mkdir();
		return tempFile;
	}
	
	/**
	 * Compares 2 files using their attributes. If the deep parameter is
	 * specified, the contents are compared.
	 * 
	 * @param first
	 * @param second
	 * @param deep
	 * @return false 
	 * @throws IOException
	 */
	public static boolean compareFiles(File first, File second, boolean deep) throws IOException{
		if (!(first.exists() == second.exists())){
			return false;
		}
		if (!first.getName().equals(second.getName())){
			return false;
		}
		if (!(first.isDirectory() == second.isDirectory())){
			return false;
		}
		if (!(first.length() == second.length())){
			return false;
		}
		if (!(first.canExecute() == second.canExecute())){
			return false;
		}
		if (!(first.canWrite() == second.canWrite())){
			return false;
		}
		if (!(first.canRead() == second.canRead())){
			return false;
		}
		if (!(first.isHidden() == second.isHidden())){
			return false;
		}
		
		if (deep && first.isFile()){
			FileInputStream in = new FileInputStream(first);
			FileInputStream in2 = new FileInputStream(second);
			int data;
			while ((data = in.read() )!= -1){
				if (data != in2.read()){
					return false;
				}
			}
		}		
		
		return true;
	}
	
	public static void copyPluginFiles(File destination, String bundle, String pathToFiles, String ... toCopy) throws IOException{
		Bundle curBundle = Platform.getBundle(bundle);
		URL url = FileLocator.find(curBundle, new Path(pathToFiles), null);
		IPath pluginDir = new Path(FileLocator.toFileURL(url).getPath());
		
		File source = pluginDir.toFile();
		
		for (String filename : toCopy){
			GtResourceHelper.copyFiles(new File(source, filename), destination);
		}
	}
}
