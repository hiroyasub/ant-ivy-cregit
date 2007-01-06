begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|repository
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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|Ivy
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
name|event
operator|.
name|IvyEvent
import|;
end_import

begin_comment
comment|/**  * TransferEvent is used to notify TransferListeners about progress in transfer  * of resources form/to the respository  *   * This class is LARGELY inspired by org.apache.maven.wagon.events.TransferEvent  * released under the following copyright license:  *   *<pre>  *   *  Copyright 2001-2005 The Apache Software Foundation.  *   *  Licensed under the Apache License, Version 2.0 (the&quot;License&quot;);  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *   *       http://www.apache.org/licenses/LICENSE-2.0  *   *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an&quot;AS IS&quot; BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *    *</pre>  *   * Orginal class written by Michal Maczka.  *   */
end_comment

begin_class
specifier|public
class|class
name|TransferEvent
extends|extends
name|IvyEvent
block|{
comment|/**      * A transfer was attempted, but has not yet commenced.      */
specifier|public
specifier|static
specifier|final
name|int
name|TRANSFER_INITIATED
init|=
literal|0
decl_stmt|;
comment|/**      * A transfer was started.      */
specifier|public
specifier|final
specifier|static
name|int
name|TRANSFER_STARTED
init|=
literal|1
decl_stmt|;
comment|/**      * A transfer is completed.      */
specifier|public
specifier|final
specifier|static
name|int
name|TRANSFER_COMPLETED
init|=
literal|2
decl_stmt|;
comment|/**      * A transfer is in progress.      */
specifier|public
specifier|final
specifier|static
name|int
name|TRANSFER_PROGRESS
init|=
literal|3
decl_stmt|;
comment|/**      * An error occured during transfer      */
specifier|public
specifier|final
specifier|static
name|int
name|TRANSFER_ERROR
init|=
literal|4
decl_stmt|;
comment|/**      * Indicates GET transfer (from the repository)      */
specifier|public
specifier|final
specifier|static
name|int
name|REQUEST_GET
init|=
literal|5
decl_stmt|;
comment|/**      * Indicates PUT transfer (to the repository)      */
specifier|public
specifier|final
specifier|static
name|int
name|REQUEST_PUT
init|=
literal|6
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TRANSFER_INITIATED_NAME
init|=
literal|"transfer-initiated"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TRANSFER_STARTED_NAME
init|=
literal|"transfer-started"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TRANSFER_PROGRESS_NAME
init|=
literal|"transfer-progress"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TRANSFER_COMPLETED_NAME
init|=
literal|"transfer-completed"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TRANSFER_ERROR_NAME
init|=
literal|"transfer-error"
decl_stmt|;
specifier|private
name|Resource
name|_resource
decl_stmt|;
specifier|private
name|int
name|_eventType
decl_stmt|;
specifier|private
name|int
name|_requestType
decl_stmt|;
specifier|private
name|Exception
name|_exception
decl_stmt|;
specifier|private
name|File
name|_localFile
decl_stmt|;
specifier|private
name|Repository
name|_repository
decl_stmt|;
specifier|private
name|long
name|_length
decl_stmt|;
specifier|private
name|long
name|_totalLength
decl_stmt|;
specifier|private
name|boolean
name|_isTotalLengthSet
init|=
literal|false
decl_stmt|;
specifier|public
name|TransferEvent
parameter_list|(
name|Ivy
name|ivy
parameter_list|,
specifier|final
name|Repository
name|repository
parameter_list|,
specifier|final
name|Resource
name|resource
parameter_list|,
specifier|final
name|int
name|eventType
parameter_list|,
specifier|final
name|int
name|requestType
parameter_list|)
block|{
name|super
argument_list|(
name|ivy
argument_list|,
name|getName
argument_list|(
name|eventType
argument_list|)
argument_list|)
expr_stmt|;
name|_repository
operator|=
name|repository
expr_stmt|;
name|addAttribute
argument_list|(
literal|"repository"
argument_list|,
name|_repository
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|_resource
operator|=
name|resource
expr_stmt|;
name|addAttribute
argument_list|(
literal|"resource"
argument_list|,
name|_resource
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|setEventType
argument_list|(
name|eventType
argument_list|)
expr_stmt|;
name|setRequestType
argument_list|(
name|requestType
argument_list|)
expr_stmt|;
name|addAttribute
argument_list|(
literal|"request-type"
argument_list|,
name|requestType
operator|==
name|REQUEST_GET
condition|?
literal|"get"
else|:
literal|"put"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|TransferEvent
parameter_list|(
name|Ivy
name|ivy
parameter_list|,
specifier|final
name|Repository
name|repository
parameter_list|,
specifier|final
name|Resource
name|resource
parameter_list|,
specifier|final
name|Exception
name|exception
parameter_list|,
specifier|final
name|int
name|requestType
parameter_list|)
block|{
name|this
argument_list|(
name|ivy
argument_list|,
name|repository
argument_list|,
name|resource
argument_list|,
name|TRANSFER_ERROR
argument_list|,
name|requestType
argument_list|)
expr_stmt|;
name|_exception
operator|=
name|exception
expr_stmt|;
block|}
specifier|public
name|TransferEvent
parameter_list|(
name|Ivy
name|ivy
parameter_list|,
specifier|final
name|Repository
name|repository
parameter_list|,
specifier|final
name|Resource
name|resource
parameter_list|,
name|long
name|length
parameter_list|,
specifier|final
name|int
name|requestType
parameter_list|)
block|{
name|this
argument_list|(
name|ivy
argument_list|,
name|repository
argument_list|,
name|resource
argument_list|,
name|TRANSFER_PROGRESS
argument_list|,
name|requestType
argument_list|)
expr_stmt|;
name|_length
operator|=
name|length
expr_stmt|;
name|_totalLength
operator|=
name|length
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|getName
parameter_list|(
name|int
name|eventType
parameter_list|)
block|{
switch|switch
condition|(
name|eventType
condition|)
block|{
case|case
name|TRANSFER_INITIATED
case|:
return|return
name|TRANSFER_INITIATED_NAME
return|;
case|case
name|TRANSFER_STARTED
case|:
return|return
name|TRANSFER_STARTED_NAME
return|;
case|case
name|TRANSFER_PROGRESS
case|:
return|return
name|TRANSFER_PROGRESS_NAME
return|;
case|case
name|TRANSFER_COMPLETED
case|:
return|return
name|TRANSFER_COMPLETED_NAME
return|;
case|case
name|TRANSFER_ERROR
case|:
return|return
name|TRANSFER_ERROR_NAME
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * @return Returns the resource.      */
specifier|public
name|Resource
name|getResource
parameter_list|()
block|{
return|return
name|_resource
return|;
block|}
comment|/**      * @return Returns the exception.      */
specifier|public
name|Exception
name|getException
parameter_list|()
block|{
return|return
name|_exception
return|;
block|}
comment|/**      * Returns the request type.      *       * @return Returns the request type. The Request type is one of      *<code>TransferEvent.REQUEST_GET<code> or<code>TransferEvent.REQUEST_PUT<code>      */
specifier|public
name|int
name|getRequestType
parameter_list|()
block|{
return|return
name|_requestType
return|;
block|}
comment|/**      * Sets the request type      *       * @param requestType      *            The requestType to set. The Request type value should be      *            either      *<code>TransferEvent.REQUEST_GET<code> or<code>TransferEvent.REQUEST_PUT<code>.      * @throws IllegalArgumentException when      */
specifier|protected
name|void
name|setRequestType
parameter_list|(
specifier|final
name|int
name|requestType
parameter_list|)
block|{
switch|switch
condition|(
name|requestType
condition|)
block|{
case|case
name|REQUEST_PUT
case|:
break|break;
case|case
name|REQUEST_GET
case|:
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Illegal request type: "
operator|+
name|requestType
argument_list|)
throw|;
block|}
name|_requestType
operator|=
name|requestType
expr_stmt|;
block|}
comment|/**      * @return Returns the eventType.      */
specifier|public
name|int
name|getEventType
parameter_list|()
block|{
return|return
name|_eventType
return|;
block|}
comment|/**      * @param eventType      *            The eventType to set.      */
specifier|protected
name|void
name|setEventType
parameter_list|(
specifier|final
name|int
name|eventType
parameter_list|)
block|{
switch|switch
condition|(
name|eventType
condition|)
block|{
case|case
name|TRANSFER_INITIATED
case|:
break|break;
case|case
name|TRANSFER_STARTED
case|:
break|break;
case|case
name|TRANSFER_COMPLETED
case|:
break|break;
case|case
name|TRANSFER_PROGRESS
case|:
break|break;
case|case
name|TRANSFER_ERROR
case|:
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Illegal event type: "
operator|+
name|eventType
argument_list|)
throw|;
block|}
name|this
operator|.
name|_eventType
operator|=
name|eventType
expr_stmt|;
block|}
comment|/**      * @param _resource      *            The resource to set.      */
specifier|protected
name|void
name|setResource
parameter_list|(
specifier|final
name|Resource
name|resource
parameter_list|)
block|{
name|_resource
operator|=
name|resource
expr_stmt|;
block|}
comment|/**      * @return Returns the local file.      */
specifier|public
name|File
name|getLocalFile
parameter_list|()
block|{
return|return
name|_localFile
return|;
block|}
comment|/**      * @param localFile      *            The local file to set.      */
specifier|protected
name|void
name|setLocalFile
parameter_list|(
name|File
name|localFile
parameter_list|)
block|{
name|_localFile
operator|=
name|localFile
expr_stmt|;
block|}
specifier|public
name|long
name|getLength
parameter_list|()
block|{
return|return
name|_length
return|;
block|}
specifier|protected
name|void
name|setLength
parameter_list|(
name|long
name|length
parameter_list|)
block|{
name|_length
operator|=
name|length
expr_stmt|;
block|}
specifier|public
name|long
name|getTotalLength
parameter_list|()
block|{
return|return
name|_totalLength
return|;
block|}
specifier|protected
name|void
name|setTotalLength
parameter_list|(
name|long
name|totalLength
parameter_list|)
block|{
name|_totalLength
operator|=
name|totalLength
expr_stmt|;
block|}
specifier|public
name|void
name|setException
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|_exception
operator|=
name|exception
expr_stmt|;
block|}
specifier|public
name|boolean
name|isTotalLengthSet
parameter_list|()
block|{
return|return
name|_isTotalLengthSet
return|;
block|}
specifier|public
name|void
name|setTotalLengthSet
parameter_list|(
name|boolean
name|isTotalLengthSet
parameter_list|)
block|{
name|_isTotalLengthSet
operator|=
name|isTotalLengthSet
expr_stmt|;
block|}
block|}
end_class

end_unit

