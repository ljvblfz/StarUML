package org.star.uml.designer.ui.action;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.parts.DiagramDocumentEditor;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.MetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.uml2.diagram.usecase.edit.helpers.UMLBaseEditHelper;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.internal.impl.ActorImpl;
import org.eclipse.uml2.uml.internal.impl.UMLFactoryImpl;
import org.osgi.framework.Bundle;
import org.star.uml.designer.Activator;
import org.star.uml.designer.base.utils.EclipseUtile;
import org.star.uml.designer.ui.factory.StarUMLCommandFactory;
import org.star.uml.designer.ui.factory.StarUMLEditHelperFactory;

public class ActorCreateAction implements IViewActionDelegate{
	public final String ACTION_ID = "Actor";
	public final String ACTION_URI = "org.eclipse.uml2.diagram.usecase.Actor_2002";
	public final String ACTION_TITLE ="Create Actor";
	public final String ICON_PATH = "/icons/login.gif";
	
	public TransactionalEditingDomain domain = null;
	public DiagramDocumentEditor editor = null;
	public View view = null;
	
	public ActorCreateAction(DiagramDocumentEditor editor){
		this.editor = editor;
		this.domain = editor.getEditingDomain();
		this.view = (View)editor.getDiagramEditPart().getModel();
	}
	
	public EObject createNode(){
		UMLFactory factoryImple = UMLFactoryImpl.init();
		final ActorImpl actor = (ActorImpl)factoryImple.createActor();
		actor.setName("Park Yong Cheon");
		return actor;
	}

	@Override
	public void init(IViewPart view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run(IAction action) {
		action.setText(ACTION_TITLE);
		action.setImageDescriptor(getImageDescriptor());
		
		URL fileURL = getImageURL(); 
		UMLBaseEditHelper helper = StarUMLEditHelperFactory.getEditHelper(ACTION_ID);
    	MetamodelType modelType = new MetamodelType(ACTION_URI,fileURL, ACTION_ID,null,helper);
    	
    	CreateElementRequest request = new CreateElementRequest(domain,view, modelType);
    	EObject eObj = createNode();
    	request.setNewElement(eObj);
    	request.setLabel(ACTION_ID);
    	
    	AbstractTransactionalCommand actorCmd = StarUMLCommandFactory.getCommand(request);
    	EObjectAdapter info = new EObjectAdapter(eObj);
    	EclipseUtile.runCommand(actorCmd, info);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		
	}
	
	public URL getImageURL(){
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		return bundle.getEntry(ICON_PATH);
	}
	
	public ImageDescriptor getImageDescriptor(){
		return Activator.getImageDescriptor(ICON_PATH);
	}
}