package org.globaltester.junit.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.globaltester.core.resources.GtResourceHelper;
import org.globaltester.junit.JUnitHelper;
import org.junit.Test;

public class JUnitHelperTest {

	@Test
	public void testEmptyWorkspace() throws CoreException{
		GtResourceHelper.createEmptyProject("testProject", ResourcesPlugin.getWorkspace().getRoot().getLocationURI());
		IProject [] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		assertTrue("workspace contains projects", projects.length > 0);
		JUnitHelper.emptyWorkspace();
		projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		assertTrue("workspace contains no projects", projects.length == 0);
	}
	
	@Test
	public void testCreateDefaultTestSpec() throws IOException, CoreException{
		JUnitHelper.createDefaultTestSpec();
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(JUnitHelper.testSpec);
		assertTrue(project.exists());
	}
	
	@Test
	public void testCreateDefaultCardConfig() throws IOException, CoreException{
		JUnitHelper.createDefaultCardConfig();
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(JUnitHelper.cardConfig);
		assertTrue(project.exists());
	}
	
	@Test
	public void testCreateDefaultTestCampaign() throws IOException, CoreException{
		JUnitHelper.createDefaultCreateDefaultTestCampaign();
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(JUnitHelper.testCampaign);
		assertTrue(project.exists());
	}
	
	@Test
	public void testRecursiveDelete() throws IOException{
		File tempRoot = File.createTempFile("root", "");
		tempRoot.delete();
		tempRoot.mkdir();
		File tempSub = File.createTempFile("sub", "", tempRoot);
		tempSub.delete();
		tempSub.mkdir();
		File tempFile = File.createTempFile("file", "", tempSub);
		
		assertTrue("root folder exists", tempRoot.exists());
		assertTrue("sub folder exists", tempSub.exists());
		assertTrue("file exists", tempFile.exists());
		
		JUnitHelper.recursiveDelete(tempRoot);
		
		assertTrue("root folder does not exist", !tempRoot.exists());
		assertTrue("sub folder does not exist", !tempSub.exists());
		assertTrue("file does not exist", !tempFile.exists());
		
	}
}
