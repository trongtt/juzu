package juzu.impl.plugin.portlet;

import juzu.impl.application.metamodel.ApplicationMetaModel;
import juzu.impl.application.metamodel.ApplicationMetaModelPlugin;
import juzu.impl.compiler.AnnotationData;
import juzu.impl.compiler.CompilationException;
import juzu.impl.compiler.ElementHandle;
import juzu.impl.compiler.MessageCode;
import juzu.impl.compiler.ProcessingContext;
import juzu.impl.utils.FQN;
import juzu.impl.utils.Tools;
import juzu.plugin.portlet.Portlet;
import juzu.portlet.JuzuPortlet;

import javax.annotation.Generated;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class PortletMetaModelPlugin extends ApplicationMetaModelPlugin {

  /** . */
  public static final MessageCode CANNOT_WRITE_PORTLET_CLASS = new MessageCode("CANNOT_WRITE_PORTLET_CLASS", "The portlet class %1$s cannot be written");

  /** . */
  private final HashMap<ElementHandle.Package, String> enabledMap = new HashMap<ElementHandle.Package, String>();

  /** . */
  private HashSet<ElementHandle.Package> toEmit = new HashSet<ElementHandle.Package>();

  public PortletMetaModelPlugin() {
    super("portlet");
  }

  @Override
  public Set<Class<? extends Annotation>> getAnnotationTypes() {
    return Collections.<Class<? extends Annotation>>singleton(Portlet.class);
  }

  @Override
  public void processAnnotation(ApplicationMetaModel application, Element element, String fqn, AnnotationData data) {
    ElementHandle.Package pkg = application.getHandle();
    if (fqn.equals(Portlet.class.getName()) && ElementHandle.create(element).equals(pkg)) {
      enabledMap.put(pkg, application.getBaseName());
      toEmit.add(pkg);
    }
    else {
      // Issue a warning ?
    }
  }

  @Override
  public void preDestroy(ApplicationMetaModel application) {
    enabledMap.remove(application.getHandle());
  }

  @Override
  public void postProcessEvents(ApplicationMetaModel application) {
    // Do GC
    ElementHandle.Package pkg = application.getHandle();
    String baseName = enabledMap.get(pkg);
    if (baseName != null) {
      PackageElement pkgElt = application.model.env.get(pkg);
      AnnotationMirror am = Tools.getAnnotation(pkgElt, Portlet.class.getName());
      if (am == null) {
        enabledMap.remove(pkg);
        toEmit.remove(pkg);
      }
      else {
        if (toEmit.contains(pkg)) {
          toEmit.remove(pkg);
          emitPortlet(application.model.env, pkgElt, baseName);
        }
      }
    }
  }

  private void emitPortlet(
    ProcessingContext env,
    PackageElement pkgElt,
    String baseName) throws CompilationException {
    Writer writer = null;
    FQN fqn = new FQN(pkgElt.getQualifiedName(), baseName + "Portlet");
    try {
      JavaFileObject file = env.createSourceFile(fqn, pkgElt);
      writer = file.openWriter();

      //
      writer.append("package ").append(pkgElt.getQualifiedName()).append(";\n");
      writer.append("import ").append(Tools.getImport(Generated.class)).append(";\n");
      writer.append("@Generated(value={})\n");
      writer.append("public class ").append(baseName).append("Portlet extends ").append(JuzuPortlet.class.getName()).append(" {\n");
      writer.append("@Override\n");
      writer.append("protected String getApplicationName(javax.portlet.PortletConfig config) {\n");
      writer.append("return \"").append(baseName).append("Application\";\n");
      writer.append("}\n");
      writer.append("}\n");
    }
    catch (IOException e) {
      throw CANNOT_WRITE_PORTLET_CLASS.failure(e, pkgElt, fqn);
    }
    finally {
      Tools.safeClose(writer);
    }


  }

}
