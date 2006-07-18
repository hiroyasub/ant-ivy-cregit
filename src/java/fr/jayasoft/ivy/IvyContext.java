begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the license found in LICENCE.TXT in the root directory of the project.  *   * #SNAPSHOT#  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|circular
operator|.
name|CircularDependencyStrategy
import|;
end_import

begin_comment
comment|/**  * This class represents an execution context of an Ivy action.  * It contains several getters to retrieve information, like the used Ivy instance, the  * cache location...   *   * @author Xavier Hanin  * @author Maarten Coene  */
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
operator|new
name|Ivy
argument_list|()
expr_stmt|;
try|try
block|{
name|getDefaultIvy
argument_list|()
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
name|getIvy
argument_list|()
operator|.
name|getDefaultCache
argument_list|()
else|:
name|_cache
return|;
block|}
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
name|CircularDependencyStrategy
name|getCircularDependencyStrategy
parameter_list|()
block|{
return|return
name|getIvy
argument_list|()
operator|.
name|getCircularDependencyStrategy
argument_list|()
return|;
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

