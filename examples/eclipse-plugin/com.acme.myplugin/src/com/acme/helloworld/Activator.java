begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|com
operator|.
name|acme
operator|.
name|helloworld
package|;
end_package

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jface
operator|.
name|resource
operator|.
name|ImageDescriptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|ui
operator|.
name|plugin
operator|.
name|AbstractUIPlugin
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_comment
comment|/**  * The activator class controls the plug-in life cycle  */
end_comment

begin_class
specifier|public
class|class
name|Activator
extends|extends
name|AbstractUIPlugin
block|{
comment|// The plug-in ID
specifier|public
specifier|static
specifier|final
name|String
name|PLUGIN_ID
init|=
literal|"com.acme.helloworld"
decl_stmt|;
comment|// The shared instance
specifier|private
specifier|static
name|Activator
name|plugin
decl_stmt|;
comment|/** 	 * The constructor 	 */
specifier|public
name|Activator
parameter_list|()
block|{
block|}
comment|/* 	 * (non-Javadoc) 	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext) 	 */
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|super
operator|.
name|start
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|plugin
operator|=
name|this
expr_stmt|;
block|}
comment|/* 	 * (non-Javadoc) 	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext) 	 */
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|plugin
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|stop
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Returns the shared instance 	 * 	 * @return the shared instance 	 */
specifier|public
specifier|static
name|Activator
name|getDefault
parameter_list|()
block|{
return|return
name|plugin
return|;
block|}
comment|/** 	 * Returns an image descriptor for the image file at the given 	 * plug-in relative path 	 * 	 * @param path the path 	 * @return the image descriptor 	 */
specifier|public
specifier|static
name|ImageDescriptor
name|getImageDescriptor
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|imageDescriptorFromPlugin
argument_list|(
name|PLUGIN_ID
argument_list|,
name|path
argument_list|)
return|;
block|}
block|}
end_class

end_unit

