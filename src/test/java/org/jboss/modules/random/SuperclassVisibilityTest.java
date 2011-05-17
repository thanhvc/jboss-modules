/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.modules.random;

import org.jboss.modules.DependencySpec;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleClassLoader;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleSpec;
import org.jboss.modules.ResourceLoaderSpec;
import org.jboss.modules.util.TestModuleLoader;
import org.jboss.modules.util.TestResourceLoader;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Stuart Douglas
 */
public class SuperclassVisibilityTest {

    private final ModuleIdentifier MODULEA = ModuleIdentifier.create("MODULEA");
    private final ModuleIdentifier MODULEB = ModuleIdentifier.create("MODULEB");
    private final ModuleIdentifier MODULEC = ModuleIdentifier.create("MODULEC");

    private TestModuleLoader moduleLoader;

    @Before
    public void setupModuleLoader() throws Exception {
        moduleLoader = new TestModuleLoader();

        final ModuleSpec.Builder aBuilder = ModuleSpec.build(MODULEA);
        aBuilder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(
                TestResourceLoader.build().addClass(org.jboss.modules.random.a.A.class)
                        .create()

        ));
        aBuilder.addDependency(DependencySpec.createLocalDependencySpec());
        moduleLoader.addModuleSpec(aBuilder.create());


        final ModuleSpec.Builder bBuilder = ModuleSpec.build(MODULEB);
        bBuilder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(
                TestResourceLoader.build().addClass(org.jboss.modules.random.b.B.class)
                        .create()

        ));
        bBuilder.addDependency(DependencySpec.createModuleDependencySpec(MODULEA));
        bBuilder.addDependency(DependencySpec.createLocalDependencySpec());
        moduleLoader.addModuleSpec(bBuilder.create());

        final ModuleSpec.Builder cBuilder = ModuleSpec.build(MODULEC);
        cBuilder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(
                TestResourceLoader.build().addClass(org.jboss.modules.random.c.C.class)
                        .create()

        ));
        cBuilder.addDependency(DependencySpec.createModuleDependencySpec(MODULEB));
        cBuilder.addDependency(DependencySpec.createLocalDependencySpec());
        moduleLoader.addModuleSpec(cBuilder.create());

    }

    @Test
    public void testLocalClassLoad() throws Exception {
        final Module moduleC = moduleLoader.loadModule(MODULEC);
        final Module moduleB = moduleLoader.loadModule(MODULEB);
        final Module moduleA = moduleLoader.loadModule(MODULEA);
        final ModuleClassLoader classLoader = moduleC.getClassLoader();
        Class<?> testClass = classLoader.loadClass("org.jboss.modules.random.c.C");
        assertEquals(testClass.getClassLoader(), moduleC.getClassLoader());
        assertEquals(testClass.getSuperclass().getClassLoader(), moduleB.getClassLoader());
        assertEquals(testClass.getSuperclass().getSuperclass().getClassLoader(), moduleA.getClassLoader());
        try {
            classLoader.loadClass("org.jboss.modules.random.a.A");
            fail("should not have loaded class");
        } catch (ClassNotFoundException e) {

        }
    }


}
