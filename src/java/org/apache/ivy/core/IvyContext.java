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
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|java
operator|.
name|util
operator|.
name|Stack
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
name|module
operator|.
name|descriptor
operator|.
name|DependencyDescriptor
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
name|resolve
operator|.
name|ResolveData
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
name|Message
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
name|MessageLogger
import|;
end_import

begin_comment
comment|/**  * This class represents an execution context of an Ivy action. It contains several getters to  * retrieve information, like the used Ivy instance, the cache location...  *  * @see IvyThread  */
end_comment

begin_class
specifier|public
class|class
name|IvyContext
block|{
specifier|private
specifier|static
name|ThreadLocal
argument_list|<
name|Stack
argument_list|<
name|IvyContext
argument_list|>
argument_list|>
name|current
init|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Ivy
name|defaultIvy
decl_stmt|;
specifier|private
name|WeakReference
argument_list|<
name|Ivy
argument_list|>
name|ivy
init|=
operator|new
name|WeakReference
argument_list|<>
argument_list|(
literal|null
argument_list|)
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|contextMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Thread
name|operatingThread
decl_stmt|;
specifier|private
name|ResolveData
name|resolveData
decl_stmt|;
specifier|private
name|DependencyDescriptor
name|dd
decl_stmt|;
specifier|public
name|IvyContext
parameter_list|()
block|{
block|}
specifier|public
name|IvyContext
parameter_list|(
name|IvyContext
name|ctx
parameter_list|)
block|{
name|defaultIvy
operator|=
name|ctx
operator|.
name|defaultIvy
expr_stmt|;
name|ivy
operator|=
name|ctx
operator|.
name|ivy
expr_stmt|;
name|contextMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|ctx
operator|.
name|contextMap
argument_list|)
expr_stmt|;
name|operatingThread
operator|=
name|ctx
operator|.
name|operatingThread
expr_stmt|;
name|resolveData
operator|=
name|ctx
operator|.
name|resolveData
expr_stmt|;
name|dd
operator|=
name|ctx
operator|.
name|dd
expr_stmt|;
block|}
specifier|public
specifier|static
name|IvyContext
name|getContext
parameter_list|()
block|{
name|Stack
argument_list|<
name|IvyContext
argument_list|>
name|cur
init|=
name|getCurrentStack
argument_list|()
decl_stmt|;
if|if
condition|(
name|cur
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|cur
operator|.
name|push
argument_list|(
operator|new
name|IvyContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|cur
operator|.
name|peek
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|Stack
argument_list|<
name|IvyContext
argument_list|>
name|getCurrentStack
parameter_list|()
block|{
name|Stack
argument_list|<
name|IvyContext
argument_list|>
name|cur
init|=
name|current
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
name|Stack
argument_list|<>
argument_list|()
expr_stmt|;
name|current
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
comment|/**      * Creates a new IvyContext and pushes it as the current context in the current thread.      *<p>      * {@link #popContext()} should usually be called when the job for which this context has been      * pushed is finished.      *</p>      *      * @return the newly pushed context      */
specifier|public
specifier|static
name|IvyContext
name|pushNewContext
parameter_list|()
block|{
return|return
name|pushContext
argument_list|(
operator|new
name|IvyContext
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Creates a new IvyContext as a copy of the current one and pushes it as the current context in      * the current thread.      *<p>      * {@link #popContext()} should usually be called when the job for which this context has been      * pushed is finished.      *</p>      *      * @return the newly pushed context      */
specifier|public
specifier|static
name|IvyContext
name|pushNewCopyContext
parameter_list|()
block|{
return|return
name|pushContext
argument_list|(
operator|new
name|IvyContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Changes the context associated with this thread. This is especially useful when launching a      * new thread, to associate it with the same context as the initial one. Do not forget to call      * {@link #popContext()} when done.      *      * @param context      *            the new context to use in this thread.      * @return the pushed context      */
specifier|public
specifier|static
name|IvyContext
name|pushContext
parameter_list|(
name|IvyContext
name|context
parameter_list|)
block|{
name|getCurrentStack
argument_list|()
operator|.
name|push
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
comment|/**      * Pops one context used with this thread. This is usually called after having finished a task      * for which a call to {@link #pushNewContext()} or {@link #pushContext(IvyContext)} was done      * prior to beginning the task.      *      * @return the popped context      */
specifier|public
specifier|static
name|IvyContext
name|popContext
parameter_list|()
block|{
return|return
name|getCurrentStack
argument_list|()
operator|.
name|pop
argument_list|()
return|;
block|}
comment|/**      * Reads the first object from the list saved under given key in the first context from the      * context stack in which this key is defined. If value under key in any of the contexts form      * the stack represents non List object then a RuntimeException is thrown.      *<p>      * This methods does a similar job to {@link #peek(String)}, except that it considers the whole      * context stack and not only one instance.      *</p>      *      * @param key      *            context key for the string      * @return top object from the list (index 0) of the first context in the stack containing this      *         key or null if no key or list empty in all contexts from the context stack      * @see #peek(String)      */
specifier|public
specifier|static
name|Object
name|peekInContextStack
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|Object
name|value
init|=
literal|null
decl_stmt|;
name|Stack
argument_list|<
name|IvyContext
argument_list|>
name|contextStack
init|=
name|getCurrentStack
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|contextStack
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
operator|&&
name|value
operator|==
literal|null
condition|;
name|i
operator|--
control|)
block|{
name|IvyContext
name|ctx
init|=
name|contextStack
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|value
operator|=
name|ctx
operator|.
name|peek
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
comment|/**      * Returns the current ivy instance.      *<p>      * When calling any public ivy method on an ivy instance, a reference to this instance is put in      * this context, and thus accessible using this method, until no code reference this instance      * and the garbage collector collects it.      *</p>      *<p>      * Then, or if no ivy method has been called, a default ivy instance is returned by this method,      * so that it never returns<code>null</code>.      *</p>      *      * @return the current ivy instance      */
specifier|public
name|Ivy
name|getIvy
parameter_list|()
block|{
name|Ivy
name|ivy
init|=
name|peekIvy
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
comment|/**      * Returns the Ivy instance associated with this context, or<code>null</code> if no such      * instance is currently associated with this context.      *<p>      * If you want get a default Ivy instance in case no instance if currently associated, use      * {@link #getIvy()}.      *</p>      *      * @return the current ivy instance, or<code>null</code> if there is no current ivy instance.      */
specifier|public
name|Ivy
name|peekIvy
parameter_list|()
block|{
return|return
name|this
operator|.
name|ivy
operator|.
name|get
argument_list|()
return|;
block|}
specifier|private
name|Ivy
name|getDefaultIvy
parameter_list|()
block|{
if|if
condition|(
name|defaultIvy
operator|==
literal|null
condition|)
block|{
name|defaultIvy
operator|=
name|Ivy
operator|.
name|newInstance
argument_list|()
expr_stmt|;
try|try
block|{
name|defaultIvy
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
name|Message
operator|.
name|debug
argument_list|(
name|e
argument_list|)
expr_stmt|;
comment|// ???
block|}
block|}
return|return
name|defaultIvy
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
name|this
operator|.
name|ivy
operator|=
operator|new
name|WeakReference
argument_list|<>
argument_list|(
name|ivy
argument_list|)
expr_stmt|;
name|operatingThread
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|get
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|WeakReference
argument_list|<
name|T
argument_list|>
name|ref
init|=
operator|(
name|WeakReference
argument_list|<
name|T
argument_list|>
operator|)
name|contextMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|ref
operator|==
literal|null
operator|)
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
parameter_list|<
name|T
parameter_list|>
name|void
name|set
parameter_list|(
name|String
name|key
parameter_list|,
name|T
name|value
parameter_list|)
block|{
name|contextMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
operator|new
name|WeakReference
argument_list|<>
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Reads the first object from the list saved under given key in the context. If value under key      * represents non List object then a RuntimeException is thrown.      *      * @param key      *            context key for the string      * @return top object from the list (index 0) or null if no key or list empty      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|Object
name|peek
parameter_list|(
name|String
name|key
parameter_list|)
block|{
synchronized|synchronized
init|(
name|contextMap
init|)
block|{
name|Object
name|o
init|=
name|contextMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|o
operator|instanceof
name|List
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|List
argument_list|<
name|Object
argument_list|>
operator|)
name|o
operator|)
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
operator|(
name|List
argument_list|<
name|Object
argument_list|>
operator|)
name|o
operator|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot top from non List object "
operator|+
name|o
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Removes and returns first object from the list saved under given key in the context. If value      * under key represents non List object then a RuntimeException is thrown.      *      * @param key      *            context key for the string      * @return top object from the list (index 0) or null if no key or list empty      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|Object
name|pop
parameter_list|(
name|String
name|key
parameter_list|)
block|{
synchronized|synchronized
init|(
name|contextMap
init|)
block|{
name|Object
name|o
init|=
name|contextMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|o
operator|instanceof
name|List
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|List
argument_list|<
name|Object
argument_list|>
operator|)
name|o
operator|)
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
operator|(
name|List
argument_list|<
name|Object
argument_list|>
operator|)
name|o
operator|)
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot pop from non List object "
operator|+
name|o
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Removes and returns first object from the list saved under given key in the context but only      * if it equals the given expectedValue - if not a false value is returned. If value under key      * represents non List object then a RuntimeException is thrown.      *      * @param key      *            context key for the string      * @param expectedValue      *            expected value of the key      * @return true if the r      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|boolean
name|pop
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|expectedValue
parameter_list|)
block|{
synchronized|synchronized
init|(
name|contextMap
init|)
block|{
name|Object
name|o
init|=
name|contextMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|o
operator|instanceof
name|List
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|List
argument_list|<
name|Object
argument_list|>
operator|)
name|o
operator|)
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Object
name|top
init|=
operator|(
operator|(
name|List
argument_list|<
name|Object
argument_list|>
operator|)
name|o
operator|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|top
operator|.
name|equals
argument_list|(
name|expectedValue
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
operator|(
operator|(
name|List
argument_list|<
name|Object
argument_list|>
operator|)
name|o
operator|)
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot pop from non List object "
operator|+
name|o
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Puts a new object at the start of the list saved under given key in the context. If value      * under key represents non List object then a RuntimeException is thrown. If no list exists      * under given key a new LinkedList is created. This is kept without WeakReference in opposite      * to the put() results.      *      * @param key      *            key context key for the string      * @param value      *            value to be saved under the key      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|push
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
synchronized|synchronized
init|(
name|contextMap
init|)
block|{
if|if
condition|(
operator|!
name|contextMap
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|contextMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
operator|new
name|LinkedList
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Object
name|o
init|=
name|contextMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|List
condition|)
block|{
operator|(
operator|(
name|List
argument_list|<
name|Object
argument_list|>
operator|)
name|o
operator|)
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot push to non List object "
operator|+
name|o
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
name|Thread
name|getOperatingThread
parameter_list|()
block|{
return|return
name|operatingThread
return|;
block|}
specifier|public
name|MessageLogger
name|getMessageLogger
parameter_list|()
block|{
comment|// calling getIvy() instead of peekIvy() is not possible here: it will initialize a default
comment|// Ivy instance, with default settings, but settings themselves may log messages and lead to
comment|// a call to this method. So we use the current Ivy instance if any, or the default Ivy
comment|// instance, or the default MessageLogger.
name|Ivy
name|ivy
init|=
name|peekIvy
argument_list|()
decl_stmt|;
if|if
condition|(
name|ivy
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|defaultIvy
operator|==
literal|null
condition|)
block|{
return|return
name|Message
operator|.
name|getDefaultLogger
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|defaultIvy
operator|.
name|getLoggerEngine
argument_list|()
return|;
block|}
block|}
else|else
block|{
return|return
name|ivy
operator|.
name|getLoggerEngine
argument_list|()
return|;
block|}
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
specifier|public
name|void
name|setResolveData
parameter_list|(
name|ResolveData
name|data
parameter_list|)
block|{
name|this
operator|.
name|resolveData
operator|=
name|data
expr_stmt|;
block|}
specifier|public
name|ResolveData
name|getResolveData
parameter_list|()
block|{
return|return
name|resolveData
return|;
block|}
specifier|public
name|void
name|setDependencyDescriptor
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|)
block|{
name|this
operator|.
name|dd
operator|=
name|dd
expr_stmt|;
block|}
specifier|public
name|DependencyDescriptor
name|getDependencyDescriptor
parameter_list|()
block|{
return|return
name|dd
return|;
block|}
block|}
end_class

end_unit

