package org.star.uml.designer.base.utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.image.ImageFileFormat;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramGenerator;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramImageGenerator;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderPlugin;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.l10n.DiagramUIRenderMessages;
import org.eclipse.gmf.runtime.diagram.ui.render.util.CopyToImageUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.uml2.diagram.usecase.part.UMLDiagramEditor;
import org.w3c.dom.Document;
import org.w3c.dom.Document;

public class EclipseUtile {
	
	public static IProject createNewProject(String projectName,Document modelDoc,Document umlDoc) {
		final MultiStatus status = new MultiStatus(DiagramUIRenderPlugin
				.getPluginId(), DiagramUIRenderStatusCodes.OK,
				DiagramUIRenderMessages.CopyToImageAction_Label, null);
		final IProject newProjectHandle =  ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    	IRunnableWithProgress runnable = createNewProjectRunable(status,newProjectHandle,modelDoc,umlDoc);
    	try {
    		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(
    				Display.getCurrent().getActiveShell());
    		progressMonitorDialog.run(false, true, runnable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newProjectHandle;
	}
	
	private static IRunnableWithProgress createNewProjectRunable(final MultiStatus status,final IProject projectHandle,final Document modelDoc,final Document umlDoc) {
		return new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) {
				try {
					projectHandle.create(monitor);
			    	monitor.beginTask("", 6); 
					monitor.worked(1);
					monitor.setTaskName("모델을 생성 합니다.");
					projectHandle.open(monitor);
					String projectPath = projectHandle.getLocation().toOSString();
					XmlUtil.writeXmlFile(modelDoc, projectPath+File.separator+"model.xml");
					XmlUtil.writeXmlFile(umlDoc, projectPath+File.separator+"default.uml");
					projectHandle.refreshLocal(IProject.DEPTH_INFINITE, monitor);
				} catch (CoreException e) {
					e.printStackTrace();
					status.add(e.getStatus());
				}catch (Exception e1) {
					e1.printStackTrace();
				} finally {
					monitor.done();
				}
			}
		};
	}
	
	public static String randomKey(){
		String key = "";
		try{
			String message[]= new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p",
										   "q","r","s","t","u","v","w","x","y","z","a","b","c","d","e","f",
										   "g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v",
										   "w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L",
										   "M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","A","B",
										   "C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R",
										   "S","T","U","V","W","X","Y","Z","1","2","3","4","5","6","7","8",
										   "9","0"};
			int num = 0;
			for(int i = 0; i < 10; i++){
				num = (int)(Math.random() * 113) + 1;
				key = key + message[num];
			}
			System.out.println(key);
		}catch(Exception e){
			e.printStackTrace();
		}
		return key;
	}
	
	public static void openDiagram(String path){
		try{
			IResource workspaceResource = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(path));
			if (workspaceResource instanceof IFile) {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				page.openEditor(new FileEditorInput((IFile) workspaceResource), UMLDiagramEditor.ID);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}