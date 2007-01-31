begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|WeakReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|Ivy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|cache
operator|.
name|CacheManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|event
operator|.
name|EventManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|settings
operator|.
name|IvySettings
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|circular
operator|.
name|CircularDependencyStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|MessageImpl
import|;
end_import

begin_comment
comment|/**  * This class represents an execution context of an Ivy action.  * It contains several getters to retrieve information, like the used Ivy instance, the  * cache location...   *   * @see IvyThread  *   * @author Xavier Hanin  * @author Maarten Coene  */
end_comment

begin_class
specifier|public
class|class
name|IvyContext
block|{
specifier|private
specifier|static
name|ThreadLocal
name|_current
init|=
operator|new
name|ThreadLocal
argument_list|()
decl_stmt|;
specifier|private
name|Ivy
name|_defaultIvy
decl_stmt|;
specifier|private
name|WeakReference
name|_ivy
init|=
operator|new
name|WeakReference
argument_list|(
literal|null
argument_list|)
decl_stmt|;
specifier|private
name|File
name|_cache
decl_stmt|;
specifier|private
name|MessageImpl
name|_messageImpl
decl_stmt|;
specifier|private
name|Map
name|_contextMap
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|Thread
name|_operatingThread
decl_stmt|;
specifier|public
specifier|static
name|IvyContext
name|getContext
parameter_list|()
block|{
name|IvyContext
name|cur
init|=
operator|(
name|IvyContext
operator|)
name|_current
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|cur
operator|==
literal|null
condition|)
block|{
name|cur
operator|=
operator|new
name|IvyContext
argument_list|()
expr_stmt|;
name|_current
operator|.
name|set
argument_list|(
name|cur
argument_list|)
expr_stmt|;
block|}
return|return
name|cur
return|;
block|}
comment|/**      * Changes the context associated with this thread.      * This is especially useful when launching a new thread, to associate it with the same context as the initial one.      *       * @param context the new context to use in this thread.      */
specifier|public
specifier|static
name|void
name|setContext
parameter_list|(
name|IvyContext
name|context
parameter_list|)
block|{
name|_current
operator|.
name|set
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the current ivy instance.      * When calling any public ivy method on an ivy instance, a reference to this instance is       * put in this context, and thus accessible using this method, until no code reference      * this instance and the garbage collector collects it.      * Then, or if no ivy method has been called, a default ivy instance is returned      * by this method, so that it never returns null.       * @return the current ivy instance      */
specifier|public
name|Ivy
name|getIvy
parameter_list|()
block|{
name|Ivy
name|ivy
init|=
operator|(
name|Ivy
operator|)
name|_ivy
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
name|ivy
operator|==
literal|null
condition|?
name|getDefaultIvy
argument_list|()
else|:
name|ivy
return|;
block|}
specifier|private
name|Ivy
name|getDefaultIvy
parameter_list|()
block|{
if|if
condition|(
name|_defaultIvy
operator|==
literal|null
condition|)
block|{
name|_defaultIvy
operator|=
name|Ivy
operator|.
name|newInstance
argument_list|()
expr_stmt|;
try|try
block|{
name|_defaultIvy
operator|.
name|configureDefault
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
block|}
block|}
return|return
name|_defaultIvy
return|;
block|}
specifier|public
name|void
name|setIvy
parameter_list|(
name|Ivy
name|ivy
parameter_list|)
block|{
name|_ivy
operator|=
operator|new
name|WeakReference
argument_list|(
name|ivy
argument_list|)
expr_stmt|;
name|_operatingThread
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
expr_stmt|;
block|}
specifier|public
name|File
name|getCache
parameter_list|()
block|{
return|return
name|_cache
operator|==
literal|null
condition|?
name|getSettings
argument_list|()
operator|.
name|getDefaultCache
argument_list|()
else|:
name|_cache
return|;
block|}
specifier|public
name|void
name|setCache
parameter_list|(
name|File
name|cache
parameter_list|)
block|{
name|_cache
operator|=
name|cache
expr_stmt|;
block|}
specifier|public
name|IvySettings
name|getSettings
parameter_list|()
block|{
return|return
name|getIvy
argument_list|()
operator|.
name|getSettings
argument_list|()
return|;
block|}
specifier|public
name|CircularDependencyStrategy
name|getCircularDependencyStrategy
parameter_list|()
block|{
return|return
name|getSettings
argument_list|()
operator|.
name|getCircularDependencyStrategy
argument_list|()
return|;
block|}
specifier|public
name|Object
name|get
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|WeakReference
name|ref
init|=
operator|(
name|WeakReference
operator|)
name|_contextMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|ref
operator|==
literal|null
condition|?
literal|null
else|:
name|ref
operator|.
name|get
argument_list|()
return|;
block|}
specifier|public
name|void
name|set
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|_contextMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
operator|new
name|WeakReference
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Thread
name|getOperatingThread
parameter_list|()
block|{
return|return
name|_operatingThread
return|;
block|}
comment|/* NB : The messageImpl is only used by Message.  It should be better to place it there. 	 * Alternatively, the Message itself could be placed here, bu this is has a major impact 	 * because Message is used at a lot of place. 	 */
specifier|public
name|MessageImpl
name|getMessageImpl
parameter_list|()
block|{
return|return
name|_messageImpl
return|;
block|}
specifier|public
name|void
name|setMessageImpl
parameter_list|(
name|MessageImpl
name|impl
parameter_list|)
block|{
name|_messageImpl
operator|=
name|impl
expr_stmt|;
block|}
specifier|public
name|EventManager
name|getEventManager
parameter_list|()
block|{
return|return
name|getIvy
argument_list|()
operator|.
name|getEventManager
argument_list|()
return|;
block|}
specifier|public
name|CacheManager
name|getCacheManager
parameter_list|()
block|{
return|return
name|CacheManager
operator|.
name|getInstance
argument_list|(
name|getSettings
argument_list|()
argument_list|,
name|getCache
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|checkInterrupted
parameter_list|()
block|{
name|getIvy
argument_list|()
operator|.
name|checkInterrupted
argument_list|()
expr_stmt|;
block|}
comment|// should be better to use context to store this kind of information, but not yet ready to do so...
comment|//    private WeakReference _root = new WeakReference(null);
comment|//    private String _rootModuleConf = null;
comment|//	public IvyNode getRoot() {
comment|//		return (IvyNode) _root.get();
comment|//	}
comment|//
comment|//	public void setRoot(IvyNode root) {
comment|//		_root = new WeakReference(root);
comment|//	}
comment|//
comment|//	public String getRootModuleConf() {
comment|//		return _rootModuleConf;
comment|//	}
comment|//
comment|//	public void setRootModuleConf(String rootModuleConf) {
comment|//		_rootModuleConf = rootModuleConf;
comment|//	}
block|}
end_class

end_unit

