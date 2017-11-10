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
name|ant
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Vector
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
name|FileUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|BuildException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|BuildLogger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|DefaultLogger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|DemuxInputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|DemuxOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|Main
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|ProjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|input
operator|.
name|DefaultInputHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|input
operator|.
name|InputHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|AntCallTriggerTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/triggers/ant-call/A/out/foo.txt"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|runAnt
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/triggers/ant-call/A/build.xml"
argument_list|)
argument_list|,
literal|"resolve"
argument_list|)
expr_stmt|;
comment|// should have unzipped foo.zip
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/triggers/ant-call/A/out/foo.txt"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|FileUtil
operator|.
name|forceDelete
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/triggers/ant-call/A/out"
argument_list|)
argument_list|)
expr_stmt|;
name|FileUtil
operator|.
name|forceDelete
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/triggers/ant-call/cache"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|runAnt
parameter_list|(
name|File
name|buildFile
parameter_list|,
name|String
name|target
parameter_list|)
throws|throws
name|BuildException
block|{
name|runAnt
argument_list|(
name|buildFile
argument_list|,
name|target
argument_list|,
name|Project
operator|.
name|MSG_INFO
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|runAnt
parameter_list|(
name|File
name|buildFile
parameter_list|,
name|String
name|target
parameter_list|,
name|int
name|messageLevel
parameter_list|)
throws|throws
name|BuildException
block|{
name|Vector
argument_list|<
name|String
argument_list|>
name|targets
init|=
operator|new
name|Vector
argument_list|<>
argument_list|()
decl_stmt|;
name|targets
operator|.
name|add
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|runAnt
argument_list|(
name|buildFile
argument_list|,
name|targets
argument_list|,
name|messageLevel
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|runAnt
parameter_list|(
name|File
name|buildFile
parameter_list|,
name|Vector
argument_list|<
name|String
argument_list|>
name|targets
parameter_list|,
name|int
name|messageLevel
parameter_list|)
throws|throws
name|BuildException
block|{
name|runBuild
argument_list|(
name|buildFile
argument_list|,
name|targets
argument_list|,
name|messageLevel
argument_list|)
expr_stmt|;
comment|// this exits the jvm at the end of the call
comment|// Main.main(new String[] {"-f", buildFile.getAbsolutePath(), target});
comment|// this does not set the good message level
comment|// Ant ant = new Ant();
comment|// Project project = new Project();
comment|// project.setBaseDir(buildFile.getParentFile());
comment|// project.init();
comment|//
comment|// ant.setProject(project);
comment|// ant.setTaskName("ant");
comment|//
comment|// ant.setAntfile(buildFile.getAbsolutePath());
comment|// ant.setInheritAll(false);
comment|// if (target != null) {
comment|// ant.setTarget(target);
comment|// }
comment|// ant.execute();
block|}
comment|// ////////////////////////////////////////////////////////////////////////////
comment|// miserable copy (updated to simple test cases) from ant Main class:
comment|// the only available way I found to easily run ant exits jvm at the end
specifier|private
name|void
name|runBuild
parameter_list|(
name|File
name|buildFile
parameter_list|,
name|Vector
argument_list|<
name|String
argument_list|>
name|targets
parameter_list|,
name|int
name|messageLevel
parameter_list|)
throws|throws
name|BuildException
block|{
specifier|final
name|Project
name|project
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|project
operator|.
name|setCoreLoader
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Throwable
name|error
init|=
literal|null
decl_stmt|;
try|try
block|{
name|addBuildListeners
argument_list|(
name|project
argument_list|,
name|messageLevel
argument_list|)
expr_stmt|;
name|addInputHandler
argument_list|(
name|project
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|PrintStream
name|err
init|=
name|System
operator|.
name|err
decl_stmt|;
name|PrintStream
name|out
init|=
name|System
operator|.
name|out
decl_stmt|;
name|InputStream
name|in
init|=
name|System
operator|.
name|in
decl_stmt|;
comment|// use a system manager that prevents from System.exit()
name|SecurityManager
name|oldsm
init|=
literal|null
decl_stmt|;
name|oldsm
operator|=
name|System
operator|.
name|getSecurityManager
argument_list|()
expr_stmt|;
comment|// SecurityManager can not be installed here for backwards
comment|// compatibility reasons (PD). Needs to be loaded prior to
comment|// ant class if we are going to implement it.
comment|// System.setSecurityManager(new NoExitSecurityManager());
try|try
block|{
name|project
operator|.
name|setDefaultInputStream
argument_list|(
name|System
operator|.
name|in
argument_list|)
expr_stmt|;
name|System
operator|.
name|setIn
argument_list|(
operator|new
name|DemuxInputStream
argument_list|(
name|project
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setOut
argument_list|(
operator|new
name|PrintStream
argument_list|(
operator|new
name|DemuxOutputStream
argument_list|(
name|project
argument_list|,
literal|false
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setErr
argument_list|(
operator|new
name|PrintStream
argument_list|(
operator|new
name|DemuxOutputStream
argument_list|(
name|project
argument_list|,
literal|true
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|project
operator|.
name|fireBuildStarted
argument_list|()
expr_stmt|;
name|project
operator|.
name|init
argument_list|()
expr_stmt|;
name|project
operator|.
name|setUserProperty
argument_list|(
literal|"ant.version"
argument_list|,
name|Main
operator|.
name|getAntVersion
argument_list|()
argument_list|)
expr_stmt|;
name|project
operator|.
name|setUserProperty
argument_list|(
literal|"ant.file"
argument_list|,
name|buildFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|ProjectHelper
operator|.
name|configureProject
argument_list|(
name|project
argument_list|,
name|buildFile
argument_list|)
expr_stmt|;
comment|// make sure that we have a target to execute
if|if
condition|(
name|targets
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|&&
name|project
operator|.
name|getDefaultTarget
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|targets
operator|.
name|addElement
argument_list|(
name|project
operator|.
name|getDefaultTarget
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|project
operator|.
name|executeTargets
argument_list|(
name|targets
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// put back the original security manager
comment|// The following will never eval to true. (PD)
if|if
condition|(
name|oldsm
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|setSecurityManager
argument_list|(
name|oldsm
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|setOut
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|System
operator|.
name|setErr
argument_list|(
name|err
argument_list|)
expr_stmt|;
name|System
operator|.
name|setIn
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
decl||
name|Error
name|exc
parameter_list|)
block|{
name|error
operator|=
name|exc
expr_stmt|;
throw|throw
name|exc
throw|;
block|}
finally|finally
block|{
name|project
operator|.
name|fireBuildFinished
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Adds the listeners specified in the command line arguments, along with the default listener,      * to the specified project.      *      * @param project      *            The project to add listeners to. Must not be<code>null</code>.      * @param level      *            log level      */
specifier|protected
name|void
name|addBuildListeners
parameter_list|(
name|Project
name|project
parameter_list|,
name|int
name|level
parameter_list|)
block|{
comment|// Add the default listener
name|project
operator|.
name|addBuildListener
argument_list|(
name|createLogger
argument_list|(
name|level
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates the InputHandler and adds it to the project.      *      * @param project      *            the project instance.      * @param inputHandlerClassname      *            String      * @exception BuildException      *                if a specified InputHandler implementation could not be loaded.      */
specifier|private
name|void
name|addInputHandler
parameter_list|(
name|Project
name|project
parameter_list|,
name|String
name|inputHandlerClassname
parameter_list|)
throws|throws
name|BuildException
block|{
name|InputHandler
name|handler
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|inputHandlerClassname
operator|==
literal|null
condition|)
block|{
name|handler
operator|=
operator|new
name|DefaultInputHandler
argument_list|()
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|handler
operator|=
operator|(
name|InputHandler
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|inputHandlerClassname
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
if|if
condition|(
name|project
operator|!=
literal|null
condition|)
block|{
name|project
operator|.
name|setProjectReference
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"The specified input handler class "
operator|+
name|inputHandlerClassname
operator|+
literal|" does not implement the InputHandler interface"
decl_stmt|;
throw|throw
operator|new
name|BuildException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Unable to instantiate specified input handler "
operator|+
literal|"class "
operator|+
name|inputHandlerClassname
operator|+
literal|" : "
operator|+
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|BuildException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
block|}
name|project
operator|.
name|setInputHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
comment|// XXX: (Jon Skeet) Any reason for writing a message and then using a bare
comment|// RuntimeException rather than just using a BuildException here? Is it
comment|// in case the message could end up being written to no loggers (as the
comment|// loggers could have failed to be created due to this failure)?
comment|/**      * Creates the default build logger for sending build events to the ant log.      *      * @return the logger instance for this build.      */
specifier|private
name|BuildLogger
name|createLogger
parameter_list|(
name|int
name|level
parameter_list|)
block|{
name|BuildLogger
name|logger
init|=
literal|null
decl_stmt|;
name|logger
operator|=
operator|new
name|DefaultLogger
argument_list|()
expr_stmt|;
name|logger
operator|.
name|setMessageOutputLevel
argument_list|(
name|level
argument_list|)
expr_stmt|;
name|logger
operator|.
name|setOutputPrintStream
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
name|logger
operator|.
name|setErrorPrintStream
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
return|return
name|logger
return|;
block|}
block|}
end_class

end_unit

