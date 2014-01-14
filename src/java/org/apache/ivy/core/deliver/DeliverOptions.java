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
operator|.
name|deliver
package|;
end_package

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

begin_comment
comment|/**  * A set of options used to do a deliver.  */
end_comment

begin_class
specifier|public
class|class
name|DeliverOptions
block|{
specifier|private
name|String
name|status
decl_stmt|;
specifier|private
name|Date
name|pubdate
decl_stmt|;
specifier|private
name|PublishingDependencyRevisionResolver
name|pdrResolver
init|=
operator|new
name|DefaultPublishingDRResolver
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|validate
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|resolveDynamicRevisions
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|replaceForcedRevisions
init|=
literal|false
decl_stmt|;
specifier|private
name|String
name|resolveId
decl_stmt|;
specifier|private
name|String
index|[]
name|confs
decl_stmt|;
specifier|private
name|String
name|pubBranch
decl_stmt|;
comment|/**      * True to indicate that the revConstraint attribute should be generated if applicable, false to      * never generate the revConstraint attribute.      */
specifier|private
name|boolean
name|generateRevConstraint
init|=
literal|true
decl_stmt|;
comment|/** true to merge parent descriptor elements into delivered child descriptor */
specifier|private
name|boolean
name|merge
init|=
literal|true
decl_stmt|;
comment|/**      * Returns an instance of DeliverOptions with options corresponding to default values taken from      * the given settings.      *       * @param settings      *            The settings to use to get default option values      * @return a DeliverOptions instance ready to be used or customized      */
specifier|public
specifier|static
name|DeliverOptions
name|newInstance
parameter_list|(
name|IvySettings
name|settings
parameter_list|)
block|{
return|return
operator|new
name|DeliverOptions
argument_list|(
literal|null
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|,
operator|new
name|DefaultPublishingDRResolver
argument_list|()
argument_list|,
name|settings
operator|.
name|doValidate
argument_list|()
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Creates an instance of DeliverOptions which require to be configured using the appropriate      * setters.      */
specifier|public
name|DeliverOptions
parameter_list|()
block|{
block|}
comment|/**      * Creates an instance of DeliverOptions with all options explicitly set.      */
specifier|public
name|DeliverOptions
parameter_list|(
name|String
name|status
parameter_list|,
name|Date
name|pubDate
parameter_list|,
name|PublishingDependencyRevisionResolver
name|pdrResolver
parameter_list|,
name|boolean
name|validate
parameter_list|,
name|boolean
name|resolveDynamicRevisions
parameter_list|,
name|String
index|[]
name|confs
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
name|this
operator|.
name|pubdate
operator|=
name|pubDate
expr_stmt|;
name|this
operator|.
name|pdrResolver
operator|=
name|pdrResolver
expr_stmt|;
name|this
operator|.
name|validate
operator|=
name|validate
expr_stmt|;
name|this
operator|.
name|resolveDynamicRevisions
operator|=
name|resolveDynamicRevisions
expr_stmt|;
name|this
operator|.
name|confs
operator|=
name|confs
expr_stmt|;
block|}
comment|/**      * Return the pdrResolver that will be used during deliver for each dependency to get its      * published information. This can particularly useful when the deliver is made for a release,      * and when we wish to deliver each dependency which is still in integration. The      * PublishingDependencyRevisionResolver can then do the delivering work for the dependency and      * return the new (delivered) dependency info (with the delivered revision). Note that      * PublishingDependencyRevisionResolver is only called for each<b>direct</b> dependency.      *       * @return the pdrResolver that will be used during deliver      */
specifier|public
name|PublishingDependencyRevisionResolver
name|getPdrResolver
parameter_list|()
block|{
return|return
name|pdrResolver
return|;
block|}
comment|/**      * Sets the pdrResolver that will be used during deliver for each dependency to get its      * published information. This can particularly useful when the deliver is made for a release,      * and when we wish to deliver each dependency which is still in integration. The      * PublishingDependencyRevisionResolver can then do the delivering work for the dependency and      * return the new (delivered) dependency info (with the delivered revision). Note that      * PublishingDependencyRevisionResolver is only called for each<b>direct</b> dependency.      *       * @return the instance of DeliverOptions on which the method has been called, for easy method      *         chaining      */
specifier|public
name|DeliverOptions
name|setPdrResolver
parameter_list|(
name|PublishingDependencyRevisionResolver
name|pdrResolver
parameter_list|)
block|{
name|this
operator|.
name|pdrResolver
operator|=
name|pdrResolver
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isResolveDynamicRevisions
parameter_list|()
block|{
return|return
name|resolveDynamicRevisions
return|;
block|}
specifier|public
name|DeliverOptions
name|setResolveDynamicRevisions
parameter_list|(
name|boolean
name|resolveDynamicRevisions
parameter_list|)
block|{
name|this
operator|.
name|resolveDynamicRevisions
operator|=
name|resolveDynamicRevisions
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isReplaceForcedRevisions
parameter_list|()
block|{
return|return
name|replaceForcedRevisions
return|;
block|}
specifier|public
name|DeliverOptions
name|setReplaceForcedRevisions
parameter_list|(
name|boolean
name|replaceForcedRevisions
parameter_list|)
block|{
name|this
operator|.
name|replaceForcedRevisions
operator|=
name|replaceForcedRevisions
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isValidate
parameter_list|()
block|{
return|return
name|validate
return|;
block|}
specifier|public
name|DeliverOptions
name|setValidate
parameter_list|(
name|boolean
name|validate
parameter_list|)
block|{
name|this
operator|.
name|validate
operator|=
name|validate
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Date
name|getPubdate
parameter_list|()
block|{
return|return
name|pubdate
return|;
block|}
specifier|public
name|DeliverOptions
name|setPubdate
parameter_list|(
name|Date
name|pubdate
parameter_list|)
block|{
name|this
operator|.
name|pubdate
operator|=
name|pubdate
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Returns the status to which the module should be delivered, or null if the current status      * should be kept.      *       * @return the status to which the module should be delivered      */
specifier|public
name|String
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
comment|/**      * Sets the status to which the module should be delivered, use null if the current status      * should be kept.      *       * @return the instance of DeliverOptions on which the method has been called, for easy method      *         chaining      */
specifier|public
name|DeliverOptions
name|setStatus
parameter_list|(
name|String
name|status
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Returns the id of a previous resolve to use for delivering.      *       * @return the id of a previous resolve      */
specifier|public
name|String
name|getResolveId
parameter_list|()
block|{
return|return
name|resolveId
return|;
block|}
comment|/**      * Sets the id of a previous resolve to use for delivering.      *       * @param resolveId      *            the id of a previous resolve      * @return the instance of DeliverOptions on which the method has been called, for easy method      *         chaining      */
specifier|public
name|DeliverOptions
name|setResolveId
parameter_list|(
name|String
name|resolveId
parameter_list|)
block|{
name|this
operator|.
name|resolveId
operator|=
name|resolveId
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Return the configurations which must be deliverd. Returns<tt>null</tt> if all configurations      * has to be deliverd. Attention: the returned array can contain wildcards!      *       * @return the configurations to deliver      */
specifier|public
name|String
index|[]
name|getConfs
parameter_list|()
block|{
return|return
name|confs
return|;
block|}
comment|/**      * Sets the configurations to deliver.      *       * @param confs      *            the configurations to deliver      * @return the instance of DeliverOptions on which the method has been called, for easy method      *         chaining      */
specifier|public
name|DeliverOptions
name|setConfs
parameter_list|(
name|String
index|[]
name|confs
parameter_list|)
block|{
name|this
operator|.
name|confs
operator|=
name|confs
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Returns the branch with which the Ivy file should be delivered, or<code>null</code> if      * branch info shouldn't be changed.      *       * @return the branch with which the Ivy file should be delivered      */
specifier|public
name|String
name|getPubBranch
parameter_list|()
block|{
return|return
name|pubBranch
return|;
block|}
comment|/**      * Sets the branch with which the Ivy file should be delivered.      *       * @param pubBranch      *            the branch with which the Ivy file should be delivered      * @return the instance of DeliverOptions on which the method has been called, for easy method      *         chaining      */
specifier|public
name|DeliverOptions
name|setPubBranch
parameter_list|(
name|String
name|pubBranch
parameter_list|)
block|{
name|this
operator|.
name|pubBranch
operator|=
name|pubBranch
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isGenerateRevConstraint
parameter_list|()
block|{
return|return
name|generateRevConstraint
return|;
block|}
specifier|public
name|DeliverOptions
name|setGenerateRevConstraint
parameter_list|(
name|boolean
name|generateRevConstraint
parameter_list|)
block|{
name|this
operator|.
name|generateRevConstraint
operator|=
name|generateRevConstraint
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isMerge
parameter_list|()
block|{
return|return
name|merge
return|;
block|}
specifier|public
name|DeliverOptions
name|setMerge
parameter_list|(
name|boolean
name|merge
parameter_list|)
block|{
name|this
operator|.
name|merge
operator|=
name|merge
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"status="
operator|+
name|status
operator|+
literal|" pubdate="
operator|+
name|pubdate
operator|+
literal|" validate="
operator|+
name|validate
operator|+
literal|" resolveDynamicRevisions="
operator|+
name|resolveDynamicRevisions
operator|+
literal|" merge="
operator|+
name|merge
operator|+
literal|" resolveId="
operator|+
name|resolveId
operator|+
literal|" pubBranch="
operator|+
name|pubBranch
return|;
block|}
block|}
end_class

end_unit

