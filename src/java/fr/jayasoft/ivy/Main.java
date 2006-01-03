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
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|cli
operator|.
name|CommandLine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|cli
operator|.
name|CommandLineParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|cli
operator|.
name|GnuParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|cli
operator|.
name|HelpFormatter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|cli
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|cli
operator|.
name|OptionBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|cli
operator|.
name|Options
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|cli
operator|.
name|ParseException
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
name|types
operator|.
name|Path
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
name|report
operator|.
name|ResolveReport
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
name|url
operator|.
name|URLHandlerDispatcher
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
name|url
operator|.
name|URLHandlerRegistry
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
name|util
operator|.
name|DefaultMessageImpl
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
name|util
operator|.
name|Message
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
name|xml
operator|.
name|XmlModuleDescriptorWriter
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
name|xml
operator|.
name|XmlReportParser
import|;
end_import

begin_comment
comment|/**  * class used to launch ivy as a standalone tool  * arguments are :  * -conf<conffile> : indicates the path to the ivy configuration file  *                  ivyconf.xml is assumed if not given  * -cache<cachedir> : indicates the path to the cache directory  *                   cache is assumed if not given  * -ivy<ivyfile> : indicates the path to the ivy file to use  *                  ivy.xml is assumed if not given  * -retrieve<retrievepattern> : when used, retrieve is also done using the given retrievepattern  * -revision<revision> : the revision with which the module should be published, required to publish  * -status<status> :   the status with which the module should be published,   *                      release is assumed if not given  * -publish<publishpattern> :  the pattern used to publish the resolved ivy file,   *                              ivy-[revision].xml is assumed if not given  */
end_comment

begin_class
specifier|public
class|class
name|Main
block|{
specifier|private
specifier|static
name|Options
name|getOptions
parameter_list|()
block|{
name|Option
name|conf
init|=
name|OptionBuilder
operator|.
name|withArgName
argument_list|(
literal|"conffile"
argument_list|)
operator|.
name|hasArg
argument_list|()
operator|.
name|withDescription
argument_list|(
literal|"use given file for configuration"
argument_list|)
operator|.
name|create
argument_list|(
literal|"conf"
argument_list|)
decl_stmt|;
name|Option
name|cache
init|=
name|OptionBuilder
operator|.
name|withArgName
argument_list|(
literal|"cachedir"
argument_list|)
operator|.
name|hasArg
argument_list|()
operator|.
name|withDescription
argument_list|(
literal|"use given directory for cache"
argument_list|)
operator|.
name|create
argument_list|(
literal|"cache"
argument_list|)
decl_stmt|;
name|Option
name|ivyfile
init|=
name|OptionBuilder
operator|.
name|withArgName
argument_list|(
literal|"ivyfile"
argument_list|)
operator|.
name|hasArg
argument_list|()
operator|.
name|withDescription
argument_list|(
literal|"use given file as ivy file"
argument_list|)
operator|.
name|create
argument_list|(
literal|"ivy"
argument_list|)
decl_stmt|;
name|Option
name|dependency
init|=
name|OptionBuilder
operator|.
name|withArgName
argument_list|(
literal|"organisation module revision"
argument_list|)
operator|.
name|hasArgs
argument_list|()
operator|.
name|withDescription
argument_list|(
literal|"use this instead of ivy file to do the rest of the work with this as a dependency."
argument_list|)
operator|.
name|create
argument_list|(
literal|"dependency"
argument_list|)
decl_stmt|;
name|Option
name|confs
init|=
name|OptionBuilder
operator|.
name|withArgName
argument_list|(
literal|"configurations"
argument_list|)
operator|.
name|hasArgs
argument_list|()
operator|.
name|withDescription
argument_list|(
literal|"resolve given configurations"
argument_list|)
operator|.
name|create
argument_list|(
literal|"confs"
argument_list|)
decl_stmt|;
name|Option
name|retrieve
init|=
name|OptionBuilder
operator|.
name|withArgName
argument_list|(
literal|"retrievepattern"
argument_list|)
operator|.
name|hasArg
argument_list|()
operator|.
name|withDescription
argument_list|(
literal|"use given pattern as retrieve pattern"
argument_list|)
operator|.
name|create
argument_list|(
literal|"retrieve"
argument_list|)
decl_stmt|;
name|Option
name|cachepath
init|=
name|OptionBuilder
operator|.
name|withArgName
argument_list|(
literal|"cachepathfile"
argument_list|)
operator|.
name|hasArg
argument_list|()
operator|.
name|withDescription
argument_list|(
literal|"outputs a classpath consisting of all dependencies in cache (including transitive ones) of the given ivy file to the given cachepathfile"
argument_list|)
operator|.
name|create
argument_list|(
literal|"cachepath"
argument_list|)
decl_stmt|;
name|Option
name|revision
init|=
name|OptionBuilder
operator|.
name|withArgName
argument_list|(
literal|"revision"
argument_list|)
operator|.
name|hasArg
argument_list|()
operator|.
name|withDescription
argument_list|(
literal|"use given revision to publish the module"
argument_list|)
operator|.
name|create
argument_list|(
literal|"revision"
argument_list|)
decl_stmt|;
name|Option
name|status
init|=
name|OptionBuilder
operator|.
name|withArgName
argument_list|(
literal|"status"
argument_list|)
operator|.
name|hasArg
argument_list|()
operator|.
name|withDescription
argument_list|(
literal|"use given status to publish the module"
argument_list|)
operator|.
name|create
argument_list|(
literal|"status"
argument_list|)
decl_stmt|;
name|Option
name|deliver
init|=
name|OptionBuilder
operator|.
name|withArgName
argument_list|(
literal|"ivypattern"
argument_list|)
operator|.
name|hasArg
argument_list|()
operator|.
name|withDescription
argument_list|(
literal|"use given pattern as resolved ivy file pattern"
argument_list|)
operator|.
name|create
argument_list|(
literal|"deliverto"
argument_list|)
decl_stmt|;
name|Option
name|publishResolver
init|=
name|OptionBuilder
operator|.
name|withArgName
argument_list|(
literal|"resolvername"
argument_list|)
operator|.
name|hasArg
argument_list|()
operator|.
name|withDescription
argument_list|(
literal|"use given resolver to publish to"
argument_list|)
operator|.
name|create
argument_list|(
literal|"publish"
argument_list|)
decl_stmt|;
name|Option
name|publishPattern
init|=
name|OptionBuilder
operator|.
name|withArgName
argument_list|(
literal|"artpattern"
argument_list|)
operator|.
name|hasArg
argument_list|()
operator|.
name|withDescription
argument_list|(
literal|"use given pattern to find artifacts to publish"
argument_list|)
operator|.
name|create
argument_list|(
literal|"publishpattern"
argument_list|)
decl_stmt|;
name|Option
name|realm
init|=
name|OptionBuilder
operator|.
name|withArgName
argument_list|(
literal|"realm"
argument_list|)
operator|.
name|hasArg
argument_list|()
operator|.
name|withDescription
argument_list|(
literal|"use given realm for HTTP AUTH"
argument_list|)
operator|.
name|create
argument_list|(
literal|"realm"
argument_list|)
decl_stmt|;
name|Option
name|host
init|=
name|OptionBuilder
operator|.
name|withArgName
argument_list|(
literal|"host"
argument_list|)
operator|.
name|hasArg
argument_list|()
operator|.
name|withDescription
argument_list|(
literal|"use given host for HTTP AUTH"
argument_list|)
operator|.
name|create
argument_list|(
literal|"host"
argument_list|)
decl_stmt|;
name|Option
name|username
init|=
name|OptionBuilder
operator|.
name|withArgName
argument_list|(
literal|"username"
argument_list|)
operator|.
name|hasArg
argument_list|()
operator|.
name|withDescription
argument_list|(
literal|"use given username for HTTP AUTH"
argument_list|)
operator|.
name|create
argument_list|(
literal|"username"
argument_list|)
decl_stmt|;
name|Option
name|passwd
init|=
name|OptionBuilder
operator|.
name|withArgName
argument_list|(
literal|"passwd"
argument_list|)
operator|.
name|hasArg
argument_list|()
operator|.
name|withDescription
argument_list|(
literal|"use given password for HTTP AUTH"
argument_list|)
operator|.
name|create
argument_list|(
literal|"passwd"
argument_list|)
decl_stmt|;
name|Options
name|options
init|=
operator|new
name|Options
argument_list|()
decl_stmt|;
name|options
operator|.
name|addOption
argument_list|(
literal|"debug"
argument_list|,
literal|false
argument_list|,
literal|"set message level to debug"
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
literal|"verbose"
argument_list|,
literal|false
argument_list|,
literal|"set message level to verbose"
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
literal|"warn"
argument_list|,
literal|false
argument_list|,
literal|"set message level to warn"
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
literal|"error"
argument_list|,
literal|false
argument_list|,
literal|"set message level to error"
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
literal|"novalidate"
argument_list|,
literal|false
argument_list|,
literal|"do not validate ivy files against xsd"
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
literal|"?"
argument_list|,
literal|false
argument_list|,
literal|"display this help"
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|conf
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|confs
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|ivyfile
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|retrieve
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|cachepath
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|revision
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|status
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|deliver
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|publishResolver
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|publishPattern
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|realm
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|host
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|passwd
argument_list|)
expr_stmt|;
return|return
name|options
return|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|Options
name|options
init|=
name|getOptions
argument_list|()
decl_stmt|;
name|CommandLineParser
name|parser
init|=
operator|new
name|GnuParser
argument_list|()
decl_stmt|;
try|try
block|{
comment|// parse the command line arguments
name|CommandLine
name|line
init|=
name|parser
operator|.
name|parse
argument_list|(
name|options
argument_list|,
name|args
argument_list|)
decl_stmt|;
if|if
condition|(
name|line
operator|.
name|hasOption
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
name|usage
argument_list|(
name|options
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|line
operator|.
name|hasOption
argument_list|(
literal|"debug"
argument_list|)
condition|)
block|{
name|Message
operator|.
name|init
argument_list|(
operator|new
name|DefaultMessageImpl
argument_list|(
name|Message
operator|.
name|MSG_DEBUG
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|line
operator|.
name|hasOption
argument_list|(
literal|"verbose"
argument_list|)
condition|)
block|{
name|Message
operator|.
name|init
argument_list|(
operator|new
name|DefaultMessageImpl
argument_list|(
name|Message
operator|.
name|MSG_VERBOSE
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|line
operator|.
name|hasOption
argument_list|(
literal|"warn"
argument_list|)
condition|)
block|{
name|Message
operator|.
name|init
argument_list|(
operator|new
name|DefaultMessageImpl
argument_list|(
name|Message
operator|.
name|MSG_WARN
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|line
operator|.
name|hasOption
argument_list|(
literal|"error"
argument_list|)
condition|)
block|{
name|Message
operator|.
name|init
argument_list|(
operator|new
name|DefaultMessageImpl
argument_list|(
name|Message
operator|.
name|MSG_ERR
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|init
argument_list|(
operator|new
name|DefaultMessageImpl
argument_list|(
name|Message
operator|.
name|MSG_INFO
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|boolean
name|validate
init|=
name|line
operator|.
name|hasOption
argument_list|(
literal|"novalidate"
argument_list|)
condition|?
literal|false
else|:
literal|true
decl_stmt|;
name|Ivy
name|ivy
init|=
operator|new
name|Ivy
argument_list|()
decl_stmt|;
name|ivy
operator|.
name|addAllVariables
argument_list|(
name|System
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|configureURLHandler
argument_list|(
name|line
operator|.
name|getOptionValue
argument_list|(
literal|"realm"
argument_list|,
literal|null
argument_list|)
argument_list|,
name|line
operator|.
name|getOptionValue
argument_list|(
literal|"host"
argument_list|,
literal|null
argument_list|)
argument_list|,
name|line
operator|.
name|getOptionValue
argument_list|(
literal|"username"
argument_list|,
literal|null
argument_list|)
argument_list|,
name|line
operator|.
name|getOptionValue
argument_list|(
literal|"passwd"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|confPath
init|=
name|line
operator|.
name|getOptionValue
argument_list|(
literal|"conf"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|confPath
argument_list|)
condition|)
block|{
name|ivy
operator|.
name|configure
argument_list|(
name|Ivy
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivyconf.xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|File
name|conffile
init|=
operator|new
name|File
argument_list|(
name|confPath
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|conffile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|error
argument_list|(
name|options
argument_list|,
literal|"ivy configuration file not found: "
operator|+
name|conffile
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|conffile
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|error
argument_list|(
name|options
argument_list|,
literal|"ivy configuration file is not a file: "
operator|+
name|conffile
argument_list|)
expr_stmt|;
block|}
name|ivy
operator|.
name|configure
argument_list|(
name|conffile
argument_list|)
expr_stmt|;
block|}
name|File
name|cache
init|=
operator|new
name|File
argument_list|(
name|ivy
operator|.
name|substitute
argument_list|(
name|line
operator|.
name|getOptionValue
argument_list|(
literal|"cache"
argument_list|,
name|ivy
operator|.
name|getDefaultCache
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|cache
operator|.
name|exists
argument_list|()
condition|)
block|{
name|cache
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
operator|!
name|cache
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|error
argument_list|(
name|options
argument_list|,
name|cache
operator|+
literal|" is not a directory"
argument_list|)
expr_stmt|;
block|}
name|String
index|[]
name|confs
decl_stmt|;
if|if
condition|(
name|line
operator|.
name|hasOption
argument_list|(
literal|"confs"
argument_list|)
condition|)
block|{
name|confs
operator|=
name|line
operator|.
name|getOptionValues
argument_list|(
literal|"confs"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|confs
operator|=
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
expr_stmt|;
block|}
name|File
name|ivyfile
decl_stmt|;
if|if
condition|(
name|line
operator|.
name|hasOption
argument_list|(
literal|"dependency"
argument_list|)
condition|)
block|{
name|String
index|[]
name|dep
init|=
name|line
operator|.
name|getOptionValues
argument_list|(
literal|"dependency"
argument_list|)
decl_stmt|;
if|if
condition|(
name|dep
operator|.
name|length
operator|!=
literal|3
condition|)
block|{
name|error
argument_list|(
name|options
argument_list|,
literal|"dependency should be expressed with exactly 3 arguments: organisation module revision"
argument_list|)
expr_stmt|;
block|}
name|ivyfile
operator|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"ivy"
argument_list|,
literal|".xml"
argument_list|)
expr_stmt|;
name|ivyfile
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|DefaultModuleDescriptor
name|md
init|=
name|DefaultModuleDescriptor
operator|.
name|newDefaultInstance
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|dep
index|[
literal|0
index|]
argument_list|,
literal|"standalone"
argument_list|,
literal|"working"
argument_list|)
argument_list|)
decl_stmt|;
name|DefaultDependencyDescriptor
name|dd
init|=
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|md
argument_list|,
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|dep
index|[
literal|0
index|]
argument_list|,
name|dep
index|[
literal|1
index|]
argument_list|,
name|dep
index|[
literal|2
index|]
argument_list|)
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|confs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"default"
argument_list|,
name|confs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|md
operator|.
name|addDependency
argument_list|(
name|dd
argument_list|)
expr_stmt|;
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|ivyfile
argument_list|)
expr_stmt|;
name|confs
operator|=
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
expr_stmt|;
block|}
else|else
block|{
name|ivyfile
operator|=
operator|new
name|File
argument_list|(
name|ivy
operator|.
name|substitute
argument_list|(
name|line
operator|.
name|getOptionValue
argument_list|(
literal|"ivy"
argument_list|,
literal|"ivy.xml"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|ivyfile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|error
argument_list|(
name|options
argument_list|,
literal|"ivy file not found: "
operator|+
name|ivyfile
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|ivyfile
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|error
argument_list|(
name|options
argument_list|,
literal|"ivy file is not a file: "
operator|+
name|ivyfile
argument_list|)
expr_stmt|;
block|}
block|}
name|Date
name|date
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|ResolveReport
name|report
init|=
name|ivy
operator|.
name|resolve
argument_list|(
name|ivyfile
operator|.
name|toURL
argument_list|()
argument_list|,
literal|null
argument_list|,
name|confs
argument_list|,
name|cache
argument_list|,
name|date
argument_list|,
name|validate
argument_list|)
decl_stmt|;
name|ModuleDescriptor
name|md
init|=
name|report
operator|.
name|getModuleDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|confs
operator|.
name|length
operator|==
literal|1
operator|&&
literal|"*"
operator|.
name|equals
argument_list|(
name|confs
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|confs
operator|=
name|md
operator|.
name|getConfigurationsNames
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|line
operator|.
name|hasOption
argument_list|(
literal|"retrieve"
argument_list|)
condition|)
block|{
name|String
name|retrievePattern
init|=
name|ivy
operator|.
name|substitute
argument_list|(
name|line
operator|.
name|getOptionValue
argument_list|(
literal|"retrieve"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|retrievePattern
operator|.
name|indexOf
argument_list|(
literal|"["
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|retrievePattern
operator|=
name|retrievePattern
operator|+
literal|"/lib/[conf]/[artifact].[ext]"
expr_stmt|;
block|}
name|ivy
operator|.
name|retrieve
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|,
name|confs
argument_list|,
name|cache
argument_list|,
name|retrievePattern
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|line
operator|.
name|hasOption
argument_list|(
literal|"cachepath"
argument_list|)
condition|)
block|{
name|outputCachePath
argument_list|(
name|ivy
argument_list|,
name|cache
argument_list|,
name|md
argument_list|,
name|confs
argument_list|,
name|line
operator|.
name|getOptionValue
argument_list|(
literal|"cachepath"
argument_list|,
literal|"ivycachepath.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|line
operator|.
name|hasOption
argument_list|(
literal|"revision"
argument_list|)
condition|)
block|{
name|ivy
operator|.
name|deliver
argument_list|(
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
argument_list|,
name|ivy
operator|.
name|substitute
argument_list|(
name|line
operator|.
name|getOptionValue
argument_list|(
literal|"revision"
argument_list|)
argument_list|)
argument_list|,
name|cache
argument_list|,
name|ivy
operator|.
name|substitute
argument_list|(
name|line
operator|.
name|getOptionValue
argument_list|(
literal|"deliverto"
argument_list|,
literal|"ivy-[revision].xml"
argument_list|)
argument_list|)
argument_list|,
name|ivy
operator|.
name|substitute
argument_list|(
name|line
operator|.
name|getOptionValue
argument_list|(
literal|"status"
argument_list|,
literal|"release"
argument_list|)
argument_list|)
argument_list|,
name|date
argument_list|,
operator|new
name|DefaultPublishingDRResolver
argument_list|()
argument_list|,
name|validate
argument_list|)
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|hasOption
argument_list|(
literal|"publish"
argument_list|)
condition|)
block|{
name|ivy
operator|.
name|publish
argument_list|(
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
argument_list|,
name|ivy
operator|.
name|substitute
argument_list|(
name|line
operator|.
name|getOptionValue
argument_list|(
literal|"revision"
argument_list|)
argument_list|)
argument_list|,
name|cache
argument_list|,
name|ivy
operator|.
name|substitute
argument_list|(
name|line
operator|.
name|getOptionValue
argument_list|(
literal|"publishpattern"
argument_list|,
literal|"distrib/[type]s/[artifact]-[revision].[ext]"
argument_list|)
argument_list|)
argument_list|,
name|line
operator|.
name|getOptionValue
argument_list|(
literal|"publish"
argument_list|)
argument_list|,
name|ivy
operator|.
name|substitute
argument_list|(
name|line
operator|.
name|getOptionValue
argument_list|(
literal|"deliverto"
argument_list|,
literal|"ivy-[revision].xml"
argument_list|)
argument_list|)
argument_list|,
name|validate
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|ParseException
name|exp
parameter_list|)
block|{
comment|// oops, something went wrong
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Parsing failed.  Reason: "
operator|+
name|exp
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|usage
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|outputCachePath
parameter_list|(
name|Ivy
name|ivy
parameter_list|,
name|File
name|cache
parameter_list|,
name|ModuleDescriptor
name|md
parameter_list|,
name|String
index|[]
name|confs
parameter_list|,
name|String
name|outFile
parameter_list|)
block|{
try|try
block|{
name|String
name|pathSeparator
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"path.separator"
argument_list|)
decl_stmt|;
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|XmlReportParser
name|parser
init|=
operator|new
name|XmlReportParser
argument_list|()
decl_stmt|;
name|Collection
name|all
init|=
operator|new
name|LinkedHashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|confs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Artifact
index|[]
name|artifacts
init|=
name|parser
operator|.
name|getArtifacts
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|,
name|confs
index|[
name|i
index|]
argument_list|,
name|cache
argument_list|)
decl_stmt|;
name|all
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|artifacts
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Iterator
name|iter
init|=
name|all
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|ivy
operator|.
name|getArchiveFileInCache
argument_list|(
name|cache
argument_list|,
name|artifact
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|pathSeparator
argument_list|)
expr_stmt|;
block|}
block|}
name|PrintWriter
name|writer
init|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|outFile
argument_list|)
argument_list|)
decl_stmt|;
name|writer
operator|.
name|println
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"cachepath output to "
operator|+
name|outFile
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"impossible to build ivy cache path: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|void
name|configureURLHandler
parameter_list|(
name|String
name|realm
parameter_list|,
name|String
name|host
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|passwd
parameter_list|)
block|{
name|URLHandlerDispatcher
name|dispatcher
init|=
operator|new
name|URLHandlerDispatcher
argument_list|()
decl_stmt|;
name|dispatcher
operator|.
name|setDownloader
argument_list|(
literal|"http"
argument_list|,
name|URLHandlerRegistry
operator|.
name|getHttp
argument_list|(
name|realm
argument_list|,
name|host
argument_list|,
name|username
argument_list|,
name|passwd
argument_list|)
argument_list|)
expr_stmt|;
name|URLHandlerRegistry
operator|.
name|setDefault
argument_list|(
name|dispatcher
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|error
parameter_list|(
name|Options
name|options
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|usage
argument_list|(
name|options
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|usage
parameter_list|(
name|Options
name|options
parameter_list|)
block|{
comment|// automatically generate the help statement
name|HelpFormatter
name|formatter
init|=
operator|new
name|HelpFormatter
argument_list|()
decl_stmt|;
name|formatter
operator|.
name|printHelp
argument_list|(
literal|"ivy"
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

