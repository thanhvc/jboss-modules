/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.modules;

import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author <a href="mailto:jbailey@redhat.com">John Bailey</a>
 */
public final class Module {

    private final ModuleIdentifier identifier;
    private final List<Module> imports;
    private final List<Module> exports;
    private final ModuleContentLoader contentLoader;

    Module(ModuleSpec spec, List<Module> imports, List<Module> exports) {
        this.identifier = spec.getIdentifier();
        this.contentLoader = spec.getContentLoader();
        this.imports = imports;
        this.exports = exports;
        // do stuff
    }

    public final Class<?> getExportedClass(String className) {
        return null;
    }

    public final Resource getExportedResource(final String resourcePath) {
        return contentLoader.getResource(resourcePath);
    }

    public final Iterable<Resource> getExportedResources(final String resourcePath) {
        // todo filter...
        return contentLoader.getResources(resourcePath);
    }

    public final Resource getExportedResource(final String rootPath, final String resourcePath) {
        // todo filter...
        return contentLoader.getResource(rootPath, resourcePath);
    }

    public static enum Flag {
        // flags here
    }
}