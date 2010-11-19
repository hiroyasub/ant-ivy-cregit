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
operator|.
name|actions
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
name|action
operator|.
name|IAction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jface
operator|.
name|viewers
operator|.
name|ISelection
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
name|IWorkbenchWindow
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
name|IWorkbenchWindowActionDelegate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jface
operator|.
name|dialogs
operator|.
name|MessageDialog
import|;
end_import

begin_comment
comment|/**  * Our sample action implements workbench action delegate.  * The action proxy will be created by the workbench and  * shown in the UI. When the user tries to use the action,  * this delegate will be created and execution will be   * delegated to it.  * @see IWorkbenchWindowActionDelegate  */
end_comment

begin_class
specifier|public
class|class
name|helloWorldAction
implements|implements
name|IWorkbenchWindowActionDelegate
block|{
specifier|private
name|IWorkbenchWindow
name|window
decl_stmt|;
comment|/** 	 * The constructor. 	 */
specifier|public
name|helloWorldAction
parameter_list|()
block|{
block|}
comment|/** 	 * The action has been activated. The argument of the 	 * method represents the 'real' action sitting 	 * in the workbench UI. 	 * @see IWorkbenchWindowActionDelegate#run 	 */
specifier|public
name|void
name|run
parameter_list|(
name|IAction
name|action
parameter_list|)
block|{
name|MessageDialog
operator|.
name|openInformation
argument_list|(
name|window
operator|.
name|getShell
argument_list|()
argument_list|,
literal|"Helloworld"
argument_list|,
literal|"Hello, Eclipse world"
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Selection in the workbench has been changed. We  	 * can change the state of the 'real' action here 	 * if we want, but this can only happen after  	 * the delegate has been created. 	 * @see IWorkbenchWindowActionDelegate#selectionChanged 	 */
specifier|public
name|void
name|selectionChanged
parameter_list|(
name|IAction
name|action
parameter_list|,
name|ISelection
name|selection
parameter_list|)
block|{
block|}
comment|/** 	 * We can use this method to dispose of any system 	 * resources we previously allocated. 	 * @see IWorkbenchWindowActionDelegate#dispose 	 */
specifier|public
name|void
name|dispose
parameter_list|()
block|{
block|}
comment|/** 	 * We will cache window object in order to 	 * be able to provide parent shell for the message dialog. 	 * @see IWorkbenchWindowActionDelegate#init 	 */
specifier|public
name|void
name|init
parameter_list|(
name|IWorkbenchWindow
name|window
parameter_list|)
block|{
name|this
operator|.
name|window
operator|=
name|window
expr_stmt|;
block|}
block|}
end_class

end_unit

