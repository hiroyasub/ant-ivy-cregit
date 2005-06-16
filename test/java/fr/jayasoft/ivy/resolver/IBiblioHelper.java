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
operator|.
name|resolver
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|URLHandler
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

begin_comment
comment|/**  * TODO write javadoc  */
end_comment

begin_class
specifier|public
class|class
name|IBiblioHelper
block|{
specifier|private
specifier|static
name|boolean
name|_checked
init|=
literal|false
decl_stmt|;
specifier|private
specifier|static
name|String
name|_mirror
init|=
literal|null
decl_stmt|;
specifier|private
specifier|static
name|URLHandler
name|handler
init|=
name|URLHandlerRegistry
operator|.
name|getHttp
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|String
name|getIBiblioMirror
parameter_list|()
throws|throws
name|Exception
block|{
comment|//        String[] mirrors = new String[] {
comment|//                "http://download.au.kde.org",
comment|//                "http://ftp.up.ac.za",
comment|//                "http://mirrors.sunsite.dk",
comment|//                "http://planetmirror.com",
comment|//                "http://www.ibiblio.org"
comment|//        };
name|String
index|[]
name|mirrors
init|=
operator|new
name|String
index|[]
block|{
literal|"http://ftp.up.ac.za/pub/linux/maven"
block|,
literal|"http://mirrors.sunsite.dk/maven"
block|,
literal|"http://public.planetmirror.com/pub/maven"
block|,
literal|"http://www.ibiblio.org/maven"
block|}
decl_stmt|;
name|String
index|[]
name|mirrorsRoot
init|=
operator|new
name|String
index|[]
block|{
literal|"http://ftp.up.ac.za/pub/linux/maven"
block|,
literal|"http://mirrors.sunsite.dk/maven"
block|,
literal|"http://public.planetmirror.com/pub/maven"
block|,
literal|"http://www.ibiblio.org/maven"
block|}
decl_stmt|;
if|if
condition|(
operator|!
name|_checked
condition|)
block|{
name|long
name|best
init|=
operator|-
literal|1
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
name|mirrors
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
if|if
condition|(
name|handler
operator|.
name|isReachable
argument_list|(
operator|new
name|URL
argument_list|(
name|mirrors
index|[
name|i
index|]
argument_list|)
argument_list|,
literal|300
argument_list|)
condition|)
block|{
name|long
name|took
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"reached "
operator|+
name|mirrors
index|[
name|i
index|]
operator|+
literal|" in "
operator|+
name|took
operator|+
literal|"ms"
argument_list|)
expr_stmt|;
if|if
condition|(
name|best
operator|==
operator|-
literal|1
operator|||
name|took
operator|<
name|best
condition|)
block|{
name|best
operator|=
name|took
expr_stmt|;
name|_mirror
operator|=
name|mirrorsRoot
index|[
name|i
index|]
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|_mirror
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"No ibiblio mirror available: no ibiblio test done"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|_mirror
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
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|String
name|biblioMirror
init|=
name|getIBiblioMirror
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"best mirror is "
operator|+
name|biblioMirror
operator|+
literal|" - found in "
operator|+
operator|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
operator|)
operator|+
literal|"ms"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

