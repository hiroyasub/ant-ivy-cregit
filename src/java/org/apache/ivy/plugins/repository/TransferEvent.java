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
name|plugins
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
name|IvyEvent
import|;
end_import

begin_comment
comment|/**  * TransferEvent is used to notify TransferListeners about progress in transfer of resources form/to  * the respository This class is LARGELY inspired by org.apache.maven.wagon.events.TransferEvent  * released under the following copyright license:  *   *<pre>  *   *  Copyright 2001-2005 The Apache Software Foundation.  *   *  Licensed under the Apache License, Version 2.0 (the&quot;License&quot;);  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *   *       http://www.apache.org/licenses/LICENSE-2.0  *   *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an&quot;AS IS&quot; BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *   *</pre>  *   * Orginal class written by Michal Maczka.  */
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
specifier|static
specifier|final
name|int
name|TRANSFER_STARTED
init|=
literal|1
decl_stmt|;
comment|/**      * A transfer is completed.      */
specifier|public
specifier|static
specifier|final
name|int
name|TRANSFER_COMPLETED
init|=
literal|2
decl_stmt|;
comment|/**      * A transfer is in progress.      */
specifier|public
specifier|static
specifier|final
name|int
name|TRANSFER_PROGRESS
init|=
literal|3
decl_stmt|;
comment|/**      * An error occurred during transfer      */
specifier|public
specifier|static
specifier|final
name|int
name|TRANSFER_ERROR
init|=
literal|4
decl_stmt|;
comment|/**      * Used to check event type validity: should always be 0<= type<= LAST_EVENT_TYPE      */
specifier|private
specifier|static
specifier|final
name|int
name|LAST_EVENT_TYPE
init|=
name|TRANSFER_ERROR
decl_stmt|;
comment|/**      * Indicates GET transfer (from the repository)      */
specifier|public
specifier|static
specifier|final
name|int
name|REQUEST_GET
init|=
literal|5
decl_stmt|;
comment|/**      * Indicates PUT transfer (to the repository)      */
specifier|public
specifier|static
specifier|final
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
name|resource
decl_stmt|;
specifier|private
name|int
name|eventType
decl_stmt|;
specifier|private
name|int
name|requestType
decl_stmt|;
specifier|private
name|Exception
name|exception
decl_stmt|;
specifier|private
name|File
name|localFile
decl_stmt|;
specifier|private
name|Repository
name|repository
decl_stmt|;
specifier|private
name|long
name|length
decl_stmt|;
specifier|private
name|long
name|totalLength
decl_stmt|;
specifier|private
name|boolean
name|isTotalLengthSet
init|=
literal|false
decl_stmt|;
comment|/**      * This attribute is used to store the time at which the event enters a type.      *<p>      * The array should better be seen as a Map from event type (int) to the time at which the event      * entered that type, 0 if it never entered this type.      *</p>      */
specifier|private
name|long
index|[]
name|timeTracking
init|=
operator|new
name|long
index|[
name|LAST_EVENT_TYPE
operator|+
literal|1
index|]
decl_stmt|;
specifier|public
name|TransferEvent
parameter_list|(
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
name|getName
argument_list|(
name|eventType
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|setResource
argument_list|(
name|resource
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
block|}
specifier|public
name|TransferEvent
parameter_list|(
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
name|repository
argument_list|,
name|resource
argument_list|,
name|TRANSFER_ERROR
argument_list|,
name|requestType
argument_list|)
expr_stmt|;
name|this
operator|.
name|exception
operator|=
name|exception
expr_stmt|;
block|}
specifier|public
name|TransferEvent
parameter_list|(
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
name|repository
argument_list|,
name|resource
argument_list|,
name|TRANSFER_PROGRESS
argument_list|,
name|requestType
argument_list|)
expr_stmt|;
name|this
operator|.
name|length
operator|=
name|length
expr_stmt|;
name|this
operator|.
name|totalLength
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
default|default:
return|return
literal|null
return|;
block|}
block|}
comment|/**      * @return Returns the resource.      */
specifier|public
name|Resource
name|getResource
parameter_list|()
block|{
return|return
name|resource
return|;
block|}
comment|/**      * @return Returns the exception.      */
specifier|public
name|Exception
name|getException
parameter_list|()
block|{
return|return
name|exception
return|;
block|}
comment|/**      * Returns the request type.      *       * @return Returns the request type. The Request type is one of      *<code>TransferEvent.REQUEST_GET<code> or<code>TransferEvent.REQUEST_PUT<code>      */
specifier|public
name|int
name|getRequestType
parameter_list|()
block|{
return|return
name|requestType
return|;
block|}
comment|/**      * Sets the request type      *       * @param requestType      *            The requestType to set. The Request type value should be either      *<code>TransferEvent.REQUEST_GET<code> or<code>TransferEvent.REQUEST_PUT<code>.      * @throws IllegalArgumentException      *             when      */
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
name|this
operator|.
name|requestType
operator|=
name|requestType
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
comment|/**      * @return Returns the eventType.      */
specifier|public
name|int
name|getEventType
parameter_list|()
block|{
return|return
name|eventType
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
name|checkEventType
argument_list|(
name|eventType
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|eventType
operator|!=
name|eventType
condition|)
block|{
name|this
operator|.
name|eventType
operator|=
name|eventType
expr_stmt|;
name|timeTracking
index|[
name|eventType
index|]
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
if|if
condition|(
name|eventType
operator|>
name|TRANSFER_INITIATED
condition|)
block|{
name|addAttribute
argument_list|(
literal|"total-duration"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|getElapsedTime
argument_list|(
name|TRANSFER_INITIATED
argument_list|,
name|eventType
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|eventType
operator|>
name|TRANSFER_STARTED
condition|)
block|{
name|addAttribute
argument_list|(
literal|"duration"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|getElapsedTime
argument_list|(
name|TRANSFER_STARTED
argument_list|,
name|eventType
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * @param resource      *            The resource to set.      */
specifier|protected
name|void
name|setResource
parameter_list|(
specifier|final
name|Resource
name|resource
parameter_list|)
block|{
name|this
operator|.
name|resource
operator|=
name|resource
expr_stmt|;
name|addAttribute
argument_list|(
literal|"resource"
argument_list|,
name|this
operator|.
name|resource
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return Returns the local file.      */
specifier|public
name|File
name|getLocalFile
parameter_list|()
block|{
return|return
name|localFile
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
name|this
operator|.
name|localFile
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
name|length
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
name|this
operator|.
name|length
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
name|totalLength
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
name|this
operator|.
name|totalLength
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
name|this
operator|.
name|exception
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
name|isTotalLengthSet
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
name|this
operator|.
name|isTotalLengthSet
operator|=
name|isTotalLengthSet
expr_stmt|;
block|}
specifier|public
name|Repository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
comment|/**      * Returns the elapsed time (in ms) between when the event entered one type until it entered      * another event time.      *<p>      * This is especially useful to get the elapsed transfer time:      *       *<pre>      * getElapsedTime(TransferEvent.TRANSFER_STARTED, TransferEvent.TRANSFER_COMPLETED);      *</pre>      *       *</p>      *<p>      * Special cases:      *<ul>      *<li>returns -1 if the event never entered the fromEventType or the toEventType.</li>      *<li>returns 0 if the event entered toEventType before fromEventType</li>      *</ul>      *</p>      *       * @param fromEventType      *            the event type constant from which time should be measured      * @param toEventType      *            the event type constant to which time should be measured      * @return the elapsed time (in ms) between when the event entered fromEventType until it      *         entered toEventType.      * @throws IllegalArgumentException      *             if either type is not a known constant event type.      */
specifier|public
name|long
name|getElapsedTime
parameter_list|(
name|int
name|fromEventType
parameter_list|,
name|int
name|toEventType
parameter_list|)
block|{
name|checkEventType
argument_list|(
name|fromEventType
argument_list|)
expr_stmt|;
name|checkEventType
argument_list|(
name|toEventType
argument_list|)
expr_stmt|;
name|long
name|start
init|=
name|timeTracking
index|[
name|fromEventType
index|]
decl_stmt|;
name|long
name|end
init|=
name|timeTracking
index|[
name|toEventType
index|]
decl_stmt|;
if|if
condition|(
name|start
operator|==
literal|0
operator|||
name|end
operator|==
literal|0
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
if|else if
condition|(
name|end
operator|<
name|start
condition|)
block|{
return|return
literal|0
return|;
block|}
else|else
block|{
return|return
name|end
operator|-
name|start
return|;
block|}
block|}
comment|/**      * Checks the given event type is a valid event type, throws an {@link IllegalArgumentException}      * if it isn't      *       * @param eventType      *            the event type to check      */
specifier|private
name|void
name|checkEventType
parameter_list|(
name|int
name|eventType
parameter_list|)
block|{
if|if
condition|(
name|eventType
operator|<
literal|0
operator|||
name|eventType
operator|>
name|LAST_EVENT_TYPE
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid event type "
operator|+
name|eventType
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

