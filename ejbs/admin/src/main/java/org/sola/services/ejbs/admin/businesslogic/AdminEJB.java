/**
 * ******************************************************************************************
 * Copyright (C) 2014 - Food and Agriculture Organization of the United Nations
 * (FAO). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,this
 * list of conditions and the following disclaimer. 2. Redistributions in binary
 * form must reproduce the above copyright notice,this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 3. Neither the name of FAO nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT,STRICT LIABILITY,OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.sola.services.ejbs.admin.businesslogic;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.util.ByteArrayDataSource;
import net.lingala.zip4j.exception.ZipException;
import org.sola.common.FileUtility;
import org.sola.common.RolesConstants;
import org.sola.common.SOLAException;
import org.sola.common.messaging.ClientMessage;
import org.sola.services.common.br.ValidationResult;
import org.sola.services.common.ejbs.AbstractEJB;
import org.sola.services.common.faults.SOLAValidationException;
import org.sola.services.common.repository.CommonSqlProvider;
import org.sola.services.ejb.system.businesslogic.SystemEJBLocal;
import org.sola.services.ejb.system.repository.entities.BrValidation;
import org.sola.services.ejbs.admin.businesslogic.repository.entities.GroupSummary;
import org.sola.services.ejbs.admin.businesslogic.repository.entities.Language;
import org.sola.services.ejbs.admin.businesslogic.repository.entities.Role;
import org.sola.services.ejbs.admin.businesslogic.repository.entities.User;
import org.sola.services.ejbs.admin.businesslogic.repository.entities.Group;
import org.sola.services.ejbs.admin.businesslogic.repository.entities.UserGroup;

/**
 * Contains business logic methods to administer system settings, users and
 * roles.
 */
@Stateless
@EJB(name = "java:global/SOLA/AdminEJBLocal", beanInterface = AdminEJBLocal.class)
public class AdminEJB extends AbstractEJB implements AdminEJBLocal {

    @EJB
    private SystemEJBLocal systemEJB;

    /**
     * Returns the list of all users from the database.
     *
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY} role.</p>
     */
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public List<User> getUsers() {
        return getRepository().getEntityList(User.class);
    }

    /**
     * Returns the details of the user with the specified user name.
     *
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY} role.</p>
     *
     * @param userName The user name of the user to search for.
     */
    @RolesAllowed({RolesConstants.ADMIN_MANAGE_SECURITY, RolesConstants.ADMIN_CHANGE_PASSWORD})
    @Override
    public User getUser(String userName) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, User.QUERY_WHERE_USERNAME);
        params.put(User.PARAM_USERNAME, userName);
        return getRepository().getEntity(User.class, params);
    }

    /**
     * Returns the details for the currently authenticated user.
     *
     * <p>
     * No role is required to execute this method.</p>
     */
    @PermitAll
    @Override
    public User getCurrentUser() {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, User.QUERY_WHERE_USERNAME);
        params.put(User.PARAM_USERNAME, this.getUserName());
        return getRepository().getEntity(User.class, params);
    }

    /**
     * Can be used to create a new user or save any updates to the details of an
     * existing user. Cannot be used to change the users password. This can only
     * be done using the
     * {@linkplain #changePassword(java.lang.String, java.lang.String) changePassword}
     * method.
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY} role. </p>
     *
     * @param user The details of the user to save
     * @return The user details after the save is completed
     */
    @RolesAllowed({RolesConstants.ADMIN_MANAGE_SECURITY, RolesConstants.ADMIN_CHANGE_PASSWORD})
    @Override
    public User saveUser(User user) {
        return getRepository().saveEntity(user);
    }

    
    /**
     * Returns the list of all security roles in SOLA.
     *
     * <p>
     * No role is required to execute this method.</p>
     */
    @PermitAll
    @Override
    public List<Role> getRoles() {
        return getRepository().getEntityList(Role.class);
    }

    /**
     * Returns the role for the specified role code
     * <p>
     * No role is required to execute this method.</p>
     *
     * @param roleCode The role code to retrieve
     */
    @PermitAll
    @Override
    public Role getRole(String roleCode) {
        return getRepository().getEntity(Role.class, roleCode);
    }

    /**
     * Returns the list of all user groups supported by SOLA.
     *
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY} role.</p>
     */
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public List<Group> getGroups() {
        return getRepository().getEntityList(Group.class);
    }

    /**
     * Can be used to create a new user group or save any updates to the details
     * of an existing user group.
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY} role. </p>
     *
     * @param userGroup The details of the user group to save
     * @return The user group after the save is completed
     */
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public Group saveGroup(Group userGroup) {
        return getRepository().saveEntity(userGroup);
    }

    /**
     * Returns the details for the specified group.
     *
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY} role.</p>
     *
     * @param groupId The identifier of the group to retrieve from the SOLA
     * database
     */
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public Group getGroup(String groupId) {
        return getRepository().getEntity(Group.class, groupId);
    }

    /**
     * Can be used to create a new security role or save any updates to the
     * details of an existing security role.
     * <p>
     * Note that security roles are linked to the SOLA code base. Adding a new
     * role also requires updating code before SOLA will recognize the role</p>
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY} role. </p>
     *
     * @param role The details of the security role to save
     * @return The security role after the save is completed
     */
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public Role saveRole(Role role) {
        return getRepository().saveEntity(role);
    }

    /**
     * Returns a summary list of all user groups supported by SOLA.
     *
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY} role.</p>
     */
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public List<GroupSummary> getGroupsSummary() {
        return getRepository().getEntityList(GroupSummary.class);
    }

    /**
     * Allows the users password to be changed
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_CHANGE_PASSWORD} role. </p>
     *
     * @param userName The username to change the password for
     * @param password The users new password
     * @return true if the change is successful.
     */
    @RolesAllowed({RolesConstants.ADMIN_MANAGE_SECURITY, RolesConstants.ADMIN_CHANGE_PASSWORD})
    @Override
    public boolean changePassword(String userName, String password) {
    
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, User.QUERY_SET_PASSWORD);
        params.put(User.PARAM_PASSWORD, getPasswordHash(password));
        params.put(User.PARAM_USERNAME, userName);

        ArrayList<HashMap> list = getRepository().executeFunction(params);

        if (list.size() > 0 && list.get(0) != null && list.get(0).size() > 0) {
            return ((Integer) ((Entry) list.get(0).entrySet().iterator().next()).getValue()) > 0;
        } else {
            return false;
        }
    }

    /**
     * Returns SHA-256 hash for the password.
     *
     * @param password Password string to hash.
     */
    private String getPasswordHash(String password) {
        String hashString = null;

        if (password != null && password.length() > 0) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(password.getBytes("UTF-8"));
                byte[] hash = md.digest();

                // Ticket #410 - Fix password encyption. Ensure 0 is prepended
                // if the hex length is == 1 
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < hash.length; i++) {
                    String hex = Integer.toHexString(0xff & hash[i]);
                    if (hex.length() == 1) {
                        sb.append('0');
                    }
                    sb.append(hex);
                }
                
                hashString = sb.toString();

            } catch (Exception e) {
                e.printStackTrace(System.err);
                return null;
            }
        }

        return hashString;
    }

    /**
     * Returns all roles associated to the specified username.
     *
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY} role.
     * </p>
     *
     * @param userName The username to use for retrieval of the roles.
     */
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public List<Role> getUserRoles(String userName) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, Role.QUERY_GET_ROLES_BY_USER_NAME);
        params.put(User.PARAM_USERNAME, userName);
        isInRole(userName);
        return getRepository().getEntityList(Role.class, params);
    }

    /**
     * Returns the list of all security roles assigned to the current user.
     *
     * <p>
     * No role is required to execute this method.</p>
     */
    @PermitAll
    @Override
    public List<Role> getCurrentUserRoles() {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, Role.QUERY_GET_ROLES_BY_USER_NAME);
        params.put(User.PARAM_USERNAME, this.getUserName());
        return getRepository().getEntityList(Role.class, params);
    }

    /**
     * Checks if the current user has been assigned one or more of the null null
     * null null     {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY},
     * {@linkplain RolesConstants#ADMIN_MANAGE_REFDATA} or
     * {@linkplain RolesConstants#ADMIN_MANAGE_SETTINGS} security roles.
     * <p>
     * No role is required to execute this method.</p>
     *
     * @return true if the user is assigned one of the Admin security roles
     */
    @PermitAll
    @Override
    public boolean isUserAdmin() {
        return isInRole(RolesConstants.ADMIN_MANAGE_SECURITY, RolesConstants.ADMIN_MANAGE_REFDATA,
                RolesConstants.ADMIN_MANAGE_SETTINGS);
    }

    /**
     * Returns the list of languages supported by SOLA for localization in
     * priority order.
     *
     * <p>
     * No role is required to execute this method.</p>
     *
     * @param lang The language code to use to localize the display value for
     * each language.
     */
    @PermitAll
    @Override
    public List<Language> getLanguages(String lang) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_LANGUAGE_CODE, lang);
        params.put(CommonSqlProvider.PARAM_ORDER_BY_PART, "item_order");
        return getRepository().getEntityList(Language.class, params);
    }

    /**
     * It generates a script with the extracted records during the consolidation
     * process. The generated script can be used to consolidate the records to
     * another database.
     *
     * @return The file name as it is saved in the server and ready for
     * download.
     *
     * @throws IOException
     */
    @RolesAllowed(RolesConstants.CONSOLIDATION_EXTRACT)
    @Override
    public String consolidationExtract(boolean everything, String password) {
        String sqlStatement = "select system.consolidation_extract(#{current_user}, #{everything}) as vl";
        String fileName = "consolidation.sql";
        Map params = new HashMap();
        params.put(CommonSqlProvider.PARAM_QUERY, sqlStatement);
        params.put("current_user", getUser(getUserName()).getId());
        params.put("everything", everything);
        String extractedRecords = getRepository().getScalar(String.class, params);
        try {
            DataSource ds = new ByteArrayDataSource(extractedRecords, "text/plain; charset=UTF-8");
            String fileNameInCache = FileUtility.saveFileFromStream(new DataHandler(ds), fileName);
            return FileUtility.compress(fileNameInCache, password);
        } catch (IOException iex) {
            Object[] lstParams = {fileName, iex.getLocalizedMessage()};
            throw new SOLAException(ClientMessage.ERR_FAILED_CREATE_NEW_FILE, lstParams);
        }
    }

    /**
     * It takes a file name that is in the server cache folder which is supposed
     * to be the consolidated records and makes the consolidation.
     *
     * @param languageCode
     * @param fileInServer
     * @return
     */
    @RolesAllowed(RolesConstants.CONSOLIDATION_CONSOLIDATE)
    @Override
    public String consolidationConsolidate(String languageCode, String fileInServer, String password) {
        String resultScript = "";
        String sqlStatementUpload = "select system.script_to_schema(#{script}) as vl";
        String sqlStatementConsolidate = "select system.consolidation_consolidate(#{current_user}) as vl";
        String failingMessage = "Validation encountered serious errors. The consolidation failed.\r\n";
        //Uncompress
        try {
            resultScript = resultScript + "Uncompressing file...";
            String fileUncompressedName = FileUtility.uncompress(fileInServer, password);
            resultScript = resultScript + "done.\r\n";
            //Get script
            resultScript = resultScript + "Retrieving script from uncommpressed file...";
            String script = new String(FileUtility.readFileFromCache(fileUncompressedName));
            resultScript = resultScript + "done.\r\n";

            resultScript = resultScript + "Creating consolidation schema...\r\n";
            //The script has commands that can create the consolidation schema
            Map params = new HashMap();
            params.put(CommonSqlProvider.PARAM_QUERY, sqlStatementUpload);
            params.put("script", script);
            getRepository().getScalar(String.class, params);
            resultScript = resultScript + "Consolidation schema created with success.\r\n";

            resultScript = resultScript + "Validating consolidation schema against the other tables...\r\n";
            //It will check for consistancy before making the consolidation
            List<ValidationResult> validationResultList = validateBeforeConsolidation(languageCode);

            for (ValidationResult vr : validationResultList) {
                String brResult = String.format("    BR:%s\r\n    Severity:%s\r\n    Passed:%s\r\n    Feedback:%s\r\n",
                        vr.getName(), vr.getSeverity(), vr.isSuccessful(), vr.getFeedback());
                resultScript = resultScript + brResult;
            }

            if (!systemEJB.validationSucceeded(validationResultList)) {
                //If the validation fails the whole transaction is rolledback.
                throw new RuntimeException(failingMessage);
            } else {
                resultScript = resultScript + "Validation finished with success.\r\n";
            }

            params = new HashMap();
            params.put(CommonSqlProvider.PARAM_QUERY, sqlStatementConsolidate);
            params.put("current_user", getUser(getUserName()).getId());
            resultScript = resultScript + "Moving records from consolidation schema to the main tables...\r\n";
            resultScript = resultScript + getRepository().getScalar(String.class, params);
        } catch (ZipException ex) {
            resultScript = String.format("%s\r\n%s\r\n%s", resultScript, "Uncompressing failed. Perhaps the password is not correct.", failingMessage);
            throw new RuntimeException(resultScript);
        } catch (RuntimeException ex) {
            resultScript = String.format("%s\r\n%s\r\n%s", resultScript, ex.getMessage(), failingMessage);
            throw new RuntimeException(resultScript);
        } finally {
            return resultScript;
        }
    }

    private List<ValidationResult> validateBeforeConsolidation(String languageCode) {
        List<BrValidation> brValidationList = this.systemEJB.getBrForConsolidation();
        List<ValidationResult> validationResultList
                = this.systemEJB.checkRulesGetValidation(brValidationList, languageCode, null);
        return validationResultList;
    }
}
