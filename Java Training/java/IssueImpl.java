package com.paypal.erc.grcprocessserv.repositories.impl.issue;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.paypal.api.platform.erc.grc.GrcIssues;
import com.paypal.api.platform.erc.grc.IssueHistory;
import com.paypal.api.platform.erc.grc.IssuesFieldUpdate;
import com.paypal.erc.grc.common.dal.models.GrcWfStatusHome;
import com.paypal.erc.grc.common.dal.models.issue.GrcBpcBusiness;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssue;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueAolMap;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueAolMapHome;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueAuditLog;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueBpcMap;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueCapabilities;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueDocMapHome;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueEffortLevel;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueException;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueHealth;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueHistory;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueIfaMap;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueIfaMapHome;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueInfoStakeholders;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueInfoStakeholdersHome;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueLinkedMap;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueLinkedMapHome;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueProduct;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueProductMap;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueProgress;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueRating;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueRegion;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueRegionMap;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueResponse;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueReviewFrequency;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueRiskMap;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueRiskMapHome;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueSource;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueUpdate;
import com.paypal.erc.grc.common.dal.models.issue.GrcRootCauseMap;
import com.paypal.erc.grc.common.dal.models.issue.GrcRootCauseMapHome;
import com.paypal.erc.grc.common.util.exceptions.ExceptionUtil;
import com.paypal.erc.grcprocessserv.impl.issue.DashBoardData;
import com.paypal.erc.grcprocessserv.repositories.constants.IssueConstants;
import com.paypal.erc.grcprocessserv.repositories.dao.issue.EffortLevelDao;
import com.paypal.erc.grcprocessserv.repositories.dao.issue.IssueAuditLogDao;
import com.paypal.erc.grcprocessserv.repositories.dao.issue.IssueBPCDao;
import com.paypal.erc.grcprocessserv.repositories.dao.issue.IssueDao;
import com.paypal.erc.grcprocessserv.repositories.dao.issue.IssueHealthDao;
import com.paypal.erc.grcprocessserv.repositories.dao.issue.IssueHistoryDao;
import com.paypal.erc.grcprocessserv.repositories.dao.issue.IssueProductDao;
import com.paypal.erc.grcprocessserv.repositories.dao.issue.IssueProgressDao;
import com.paypal.erc.grcprocessserv.repositories.dao.issue.IssueResponseDao;
import com.paypal.erc.grcprocessserv.repositories.dao.issue.IssueSourceDao;
import com.paypal.erc.grcprocessserv.repositories.dao.issue.ReviewFrequenctDao;
import com.paypal.erc.grcprocessserv.repositories.mappers.issue.IssueUtilMapper;

@Component("IssueImpl")
@Transactional(value = "JpaTxnManager_erc", propagation = Propagation.REQUIRED)
public class IssueImpl {

	private static final Logger logger = LoggerFactory.getLogger(IssueImpl.class);
	BigDecimal time = new BigDecimal(System.currentTimeMillis() / 1000);
	
	@Autowired
	IssueDao issueDao;

	@Autowired
	IssueHealthDao issueHealthDao;
	
	@Autowired
	IssueHistoryDao issueHistoryDao;
	
	@Autowired
	IssueAuditLogDao issueAuditLogDao;
	
	@Autowired
	IssueBPCDao issueBPCDao;
	
	@Autowired
	IssueSourceDao issueSourceDao;
	
	@Autowired
	ReviewFrequenctDao reviewFrequencyDao;

	@Autowired
	EffortLevelDao effortLevelDao;

	@Autowired
	IssueResponseDao issueResponseDao;

	@Autowired
	IssueProgressDao issueProgressDao;

	@Autowired
	IssueUtilMapper issueUtilMapper;

	@Autowired
	IssueProductDao issueProductDao;
	
	@Autowired
	GrcIssueDocMapHome issueDocDao;

	@Autowired
	GrcIssueAolMapHome aolMapDao;

	@Autowired
	GrcIssueInfoStakeholdersHome issueISHDao;
	
	@Autowired
	GrcIssueIfaMapHome ifaDao;

	@Autowired
	GrcIssueRiskMapHome riskCatDao;

	@Autowired
	GrcIssueLinkedMapHome linkedIssueDao;

	@Autowired
	GrcRootCauseMapHome rtcDao;
	
	@Autowired
	InactiveIssueImpl inactiveImpl;
	
	@Autowired
	ActiveIssueImpl activeImpl;

    @Autowired
    GrcWfStatusHome wfStatusHome;
	
    /**
     * This method is for saving GrcIssue to the data base
     * 
     * @param GrcIssues This is the first parameter which contains entire
     * GrcIssues object
     */
	public void save(GrcIssues issue) throws Exception {

	    try {
	    	GrcIssue activeIssue = activeImpl.create(issue);
	    	issue.setIssueCode(activeIssue.getIssueId());
	    	activeImpl.createIssueUpdate(issue);
        } catch (Exception e) {
            logger.error("Error while persisting Issue: IssueImpl:save(GrcIssues issue)",e);
            ExceptionUtil.logAndThrowBusinessException("DB_ISSUE_SAVE",e,"failed at DB level"+e.getMessage());
	    }
	}
	 /**
	  * This method is for updateIsssues returns  GrcIssues object
      *
      *@param GrcIssues issue this is the parameter that contains entire GrcIssues Object
      *@exception BusinessException  this method throws the BusinessException and some other exception through logger
      */

	public GrcIssues updateIssue(GrcIssues issue) throws Exception{

        GrcIssue issueEntity = issueDao.find(issue.getIssueCode());
        GrcIssues response = null;
        try {
        	if (!"Approved".equals(issue.getStatusCode().getName()) && 
        			!"Approved_Pending_Updates".equals(issue.getStatusCode().getName())){
        		inactiveImpl.update(issueEntity, issue.getUpdatedBy());
            	issueDao.detachIssue(issueEntity);
            	GrcIssue activeIssue = activeImpl.create(issue);
            	issueDao.merge(activeIssue);
        	}
        	
        	if ("Approved".equals(issue.getStatusCode().getName())){
        		issueEntity.setGrcWfStatus(wfStatusHome.findByStatusType("Approved_Pending_Updates", "ISSUE_MANAGEMENT"));
            	issueDao.merge(issueEntity);
            	issue.getStatusCode().setName("Approved_Pending_Updates");

    			long time = System.currentTimeMillis()/1000;
    			GrcIssueAuditLog grcIssueAuditLog = new GrcIssueAuditLog();
        		grcIssueAuditLog.setIssueCode(issue.getIssueCode());
        		grcIssueAuditLog.setFromStatusCode("Approved");
        		grcIssueAuditLog.setToStatusCode(issue.getStatusCode().getName());
        		grcIssueAuditLog.setActionCode(issue.getStatusCode().getName());
        		grcIssueAuditLog.setCommentCode(issue.getStatusCode().getName());
        		grcIssueAuditLog.setCreatedBy(issue.getUpdatedBy());
        		grcIssueAuditLog.setTimeCreated(time);
        		grcIssueAuditLog.setActive(IssueConstants.ACTIVE);
        		grcIssueAuditLog.setVersion(issueEntity.getVersion());
    			issueDao.persist(grcIssueAuditLog);
            }
        	String currentVersion = activeImpl.createIssueUpdate(issue);
        	if (currentVersion!=null){
        		if("Draft_Pending_Updates".equals(issue.getStatusCode().getName()) 
        				|| "Approved_Pending_Updates".equals(issue.getStatusCode().getName())){
        			try {
						String previousVersion = issueEntity.getVersion();
						if (!"Approved".equals(issue.getStatusCode().getName()) && 
			        			!"Approved_Pending_Updates".equals(issue.getStatusCode().getName())){
							previousVersion = String.valueOf(Integer.parseInt(issueEntity.getVersion())+1);
						}
						getFieldUpdate(issueEntity.getIssueId(), previousVersion, currentVersion, issue.getUpdatedBy()).logChanges();
					} catch (Exception e) {
						logger.error("Failed to log history for issue id: " + issueEntity.getIssueId());
						logger.error("FAILED_TO_LOG_HISTORY", e.getMessage());
					}
                }
        	}
            response = new GrcIssues();
            response.setIssueCode(issue.getIssueCode());
            response.setIssueName(issue.getIssueName());
            response.setUpdatedBy(issue.getUpdatedBy());
            logger.debug("Issue Succefully Updated. Issue Id: " + issueEntity.getIssueId());
        }
        catch(Exception e){
            logger.error("Update Issue Failed",e);
            logger.error("Failed at DB level",e);
            ExceptionUtil.logAndThrowBusinessException("DB_ERROR_ISSUE_UPDATE_CHECK",e,"failed at DB level"+e.getMessage());
        }
       return response;
    }/**
    
    *  getFieldUpdate method is used for look up  that is to insert values
    
    *  @param issuecode	 This is the first parameter 
    *  @param prevVersion 	This  is the second Parameter
    *  @param currentVersion 	This  is the Third Parameter
    *  @param performedBy 	This is the fourth Parameter
    
    *  @return null 	This method returns null object
    */

	@Lookup
    public IssueFieldUpdateTask getFieldUpdate(String issueCode, String prevVersion, 
    		String currentVersion, String performedBy){
		return null;
	}
	
	/**
	 * This method findAllIssues is used to return all the GrcIssue
	 
	 * @exception  nullpointer This may be thrown sometimes 
	 * 
	 * @return List<GrcIssue> This method returns List of GrcIssue
	 *  
	 * */
	public List<GrcIssue> findAllIssues() throws Exception {
		return issueDao.findAll();
	}
	/**
	 * This method getAllIssues will have some parameters and return some values
	 
	 * @param userName 	This is the first Parameter  of type String
	 * @param userType		 This is the second parameter of type String
	 * @param fa	 This is the third parameter of type String
	 * @param status	This  is the foruth parameter of type String
	 
	 *  @return Map<String, List<Object[]>> this method returns Map of String, List<Object[]>  */
	
	public Map<String, List<Object[]>> getAllIssues(String userName,String userType, String fa, String status) {
		return issueDao.getAllIssues(userName, userType, fa, status); 
	}
	/**
	 * This method findAllLinkedIssues  will have some parameter and return some values
	 
	 * @param  issueCode This is the  only parameter of String type
	 * @exception nullPointerException There are some chances to throw  nullpointerexception 
	 
	 * @return List<Object> This returns List of Object */
	public List<Object> findAllLinkedIssues(String issueCode) throws Exception {
		return issueDao.findAllLinkedIssues(issueCode);
	}
/**
 * This method findIssue has a parameter and a return type
 
 * @param  id as the only parameter and this is String type
 * @return GrcIssue This returns GrcIssue type 
    */
	public GrcIssue findIssue(String id) throws Exception {
		return issueDao.find(id);
	}
	/**
	 * This method getIssueSource  has for parameters and returns a list type 
	 * a list is being created for this type and then it is returning it 
	 
	 * @param issueCode is the first parameter of string type
	 * @param active is the second parameter of string type
	 * @param version is the third parameter of string type
	 * @exception nullPointerException nullPointerException may happen
	 
	 * @return List<GrcIssueSource> GrcIssueSource type of List
	 * */
	public List<GrcIssueSource> getIssueSource(String issueCode, String active, String version) throws Exception {
		List<GrcIssueSource> issueSourceList = issueDao.findIssueSource(issueCode,active,version);
		return issueSourceList;
	}
/**
 * This method getIssueProgress has a parameter and returns a list type 
 
 * @param active This is the only parameter passed
 * 
 * @return List<GrcIssueProgress> This method returns List of GrcIssueProgress
 * */
	public List<GrcIssueProgress> getIssueProgress(String active) {
		return issueProgressDao.getIssueProgressList(active);
	}
	
	/**
	 * This method getIssueResponse  will return List of GrcIssueResponse type
	 
	 * @return List<GrcIssueResponse> list of GrcIssueResponse
	 */
	 
	public List<GrcIssueResponse> getIssueResponse() {
		return issueResponseDao.getIssueResponseList();
	}
/**
 * This method  getLevelEffortList method will get value through parameter and 
 * returns a list
 
 * @param active This is the only parameter
 
 * @return List<GrcIssueEffortLevel> This method returns List of GrcIssueEffortLevel*/
	public List<GrcIssueEffortLevel> getLevelOfEffortList(String active) {
		return effortLevelDao.getLevelOfEffortList(active);
	}
/**
 * This method getIssueReviewFrequency method  will get value through parameter and 
 * returns a list
 
 * @param active This is the only parameter of type string
 * 
 * @return List<GrcIssueReviewFrequency> will return List of GrcIssueReviewFrequency */
	public List<GrcIssueReviewFrequency> getIssueReviewFrequency(String active) {
		return reviewFrequencyDao.getReviewFrequency(active);
	}
/**
 * This method getIssueHealth will get value through parameter and 
 * returns a list
 
 * @param active   This is the only parameter of type string
 * @return List<GrcIssueHealth> This returns List of GrcIssueHealth
 * */
	public List<GrcIssueHealth> getIssueHealth(String active) {
		return issueHealthDao.getIssueHealth(active);

	}
	/**
	 * This method getIssueHistory will get the value through parameter and returns a list in addition to this they 
	 * check for exceptions and if any exceptions happened they catch them and log 
	  
	 *@param issueId This is the First Parameter of this method  of type String
	 *@param type This is the Second Parameter of this method of type String
	 
	 *@exception  BusinessException This is the className exception this is thrown when the history at database level was failed
	 *@return List<IssueHistory> This method returns List of IssueHistory
	  */
	public List<IssueHistory> getIssueHistory(String issueId, String type) {
		List<IssueHistory> historyList = null;
		try {
			Integer fieldType = type != null?Integer.parseInt(type):null;
			String version = null;
			if (fieldType != null){
				version = issueDao.findIssueVersion(issueId);
			}
			List<GrcIssueHistory> logList = issueHistoryDao.getIssueHistory(issueId, fieldType, version!=null?Integer.parseInt(version):null);
			historyList = issueUtilMapper.convertIssueHistoryEntityToDTO(logList, fieldType);
        } catch (Exception e) {
            logger.error("Error while persisting Issue: IssueImpl:getIssueHistory(GrcIssues issue)",e);
            ExceptionUtil.logAndThrowBusinessException("DB_ISSUE_HISTORY_SELECT",e,"failed at DB level"+e.getMessage());
	    }
		return historyList;
	}
	/**
	 *This method saveHistory gets the value as a parameter and creates a list and calls a another method
	 
	 *@param dtoList This is a the only parameter,  list parameter of type IssuesFieldUpdate 
	 
	 **/

	public void saveHistory(List<IssuesFieldUpdate> dtoList) throws Exception {
		List<GrcIssueHistory> issueHistory = issueUtilMapper.convertIssueHistoryDtoToEntity(dtoList);
		issueHistoryDao.persist(issueHistory);
			
	}
	/**
	*This method getAllBPC gets values as a parameter and returns a list 
	*
	*@param active This is the first parameter of type String 
	*@param version This is the Second parameter of type String
	*
	*@throws there may be chance for NoSuchFieldException so we throw Exception
	
	*@return List<GrcBpcBusiness> This method returns List of GrcBpcBusiness
		 */
	
	public List<GrcBpcBusiness> getAllBPC(String active, String version) throws Exception {
		return issueBPCDao.getAllBPC(active, version);
		
	}
	/**
	 * This method getProducts returns a list 
	 * 
	 * @return List<GrcIssueProduct> This method returns a list of GrcIssueProduct 
	 */
	
	public List<GrcIssueProduct> getProducts() throws Exception {
		return issueProductDao.getProducts();
		
	}
	/**
	*This method getCapabilities gets values as a parameter and returns a list 
	*
	*@param productCode This is the first parameter of type String 
	
	*
	*@throws there may be chance for NoSuchFieldException so we throw Exception
	
	*@return List<GrcIssueCapabilities> This method returns List of GrcIssueCapabilities
		 */
	public List<GrcIssueCapabilities> getCapabilities(String productCode) throws Exception {
		return issueProductDao.getCapabilities(productCode);
		
	}
	/**
	*This method getLookUps gets values as a parameter and returns a list 
	*
	*@param query This is the first parameter of type String 
	
	*
	*@throws there may be chance for NoSuchFieldException so we throw Exception
	
	*@return List<object> This method returns List of object
		 */
	
	public List<Object> getLookUps(String query) throws Exception{
		List<Object> listItems = issueDao.getList(query);
		return listItems;
	}
	/**
	*This method getLookUps gets values as a parameters and returns a list 
	*
	*@param query This is the first parameter of type String 
	*@param rootCauseType This is the Second parameter of type String
	*
	*@throws there may be chance for NoSuchFieldException so we throw Exception
	
	@return List<object> This method returns List of object
		 */
	public List<Object> getLookUps(String query, String rootCauseType) throws Exception{
        List<Object> listItems = issueDao.getList(query, rootCauseType);
        return listItems;
	}

	/**
	*This method findIssueAuditLogByIssueCode gets values as a parameters and returns a list 
	*
	*@param  issueCode This  is the first parameter of type String 
	*@param  active    This is the Second parameter of type String
	*
	*@throws There may be chance for NoSuchFieldException so we throw Exception
	
	*@return List<GrcIssueAuditLog> This method returns List of GrcIssueAuditLog
		 */
    public List<GrcIssueAuditLog> findIssueAuditLogByIssueCode(String issueCode, String active) throws Exception {
    	List<GrcIssueAuditLog> temp = issueDao.findIssueAuditLogByIssueCode(issueCode, active);
    	return temp;
    }
    /**
     * This method getUserFa has a parameter 
     * @param userName This is the only parameter passed over here
     * 
     * @return String This method returns a String value 
     */
    public String getUserFa(String userName) throws Exception {
		return issueDao.getUserFa(userName);
	}
    /**
     * This method getRegionHierarchy creates a list and returns it 
     * 
     * @return List<GrcIssueRegion> This method returns List of GrcIssueRegion
     */

	public List<GrcIssueRegion> getRegionHierarchy() throws Exception{
        List<GrcIssueRegion> listItems = issueDao.getRegionHierarchy();
        return listItems;
    }
	/**
	 * This method getSubRootCause gets value through a parameter and creates a list and returns a list 
	 * 
	 * @param rootCause This is the parameter of type String 
	 * 
	 * @return List<GrcIssueRegion> This method returns List  of GrcIssueRegion 
	 */
	
	public List<GrcIssueRegion> getSubRootCause(String rootCause) throws Exception{
        List<GrcIssueRegion> listItems = issueDao.getRegionHierarchy();
        return listItems;
    }
	/**
	 * This method gets value through the parameter and returns the list
	 * 
	 * @param issueId This was the parameter passed of type String
	 * 
	 * @return  List<GrcIssueException> This is the return type of this method it returns  List of GrcIssueException
	 */
	public List<GrcIssueException> getExceptions(String issueId) {
		return issueDao.getExceptions(issueId);
	}
	/**
	*This method findIssueAuditLogByIssueCode gets values as a parameters and returns a list 
	*
	*@param  issueId This  is the first parameter of type String 
	*@param  exceptionId   This is the Second parameter of type String
	*
	*@return  GrcIssueException This method returns GrcIssueException object
		 */
	public GrcIssueException getException(String issueId, String exceptionId) {
		return issueDao.getException(issueId, exceptionId);
	}
	/**
	 * This method has a parameter and does not return anything
	 * 
	 * @param issueId This is the only parameter passed over here
	 * 
	 * @return the return type for this method is void so it does not return any value
	 */
	
	public void deleteExceptions(String issueId){
		issueDao.deleteExceptions(issueId);
	}/**
	* This method updateFieldUpdate has three parameters and has two if condition and a for each loop
	* based on these loops and conditons some operations happen here
	* 
	* @param issueCode This is the First parameter it is string type 
	* @param fieldType This  is the Second Paramter of type String
	* @param fieldName This is the third parameter of type list of type String 
	* 
	* @return This method does not return any value since it is void
	*/
	
	public void updateFieldUpdate(String issueCode, String fieldType, List<String> fieldNames) throws Exception{
		String version = null;
		if (fieldType != null)
			version = issueDao.findIssueVersion(issueCode);
		
		List<GrcIssueHistory> fieldUpdate = issueHistoryDao.findHistoryByTypeAndNames(issueCode, 
				Integer.parseInt(fieldType), fieldNames, Integer.parseInt(version));
		
		if(fieldUpdate == null || fieldUpdate.isEmpty())
			return;
		for (GrcIssueHistory history : fieldUpdate){
			history.setActive(IssueConstants.INACTIVE);
			issueHistoryDao.merge(history);
		}
	}
	/**
	 *This method updateApprovedFields has a try block and catch block and it has two for each  loop
	 *this method checks if the size of the map is zero if it is it executes some functions or else it moves to else part
	 *and  calls some other methods
	 *@param issueCode This is the First parameter of type String 
	 *@param fieldProps This is the Second Parameter it is  list of type IssuesFieldUpdate
	 *
	 *@throws NoSuchmethodFound Exception or NoSuchFieldFound Exception can be formed so we throw exception
	 *
	 *@return The Return type for this method is void so there is no return value 
	 *
	 */
	public void updateApprovedFields(String issueCode, List<IssuesFieldUpdate> fieldProps) throws Exception{
		try{
			GrcIssue oldIssue = issueDao.find(issueCode);
			Map<String, IssuesFieldUpdate> fieldMap = new HashMap<>();
			for (IssuesFieldUpdate field : fieldProps)
				fieldMap.put(field.getFieldName(), field);
			
			List<GrcIssueHistory> fieldUpdate = issueHistoryDao.getIssueHistory(issueCode, IssueConstants.APPROVAL_FIELD, Integer.parseInt(oldIssue.getVersion()));
			if(fieldUpdate == null || fieldUpdate.isEmpty())
				return;
			for (GrcIssueHistory history : fieldUpdate){
				if (fieldMap.get(history.getFieldName())!=null){
					if(fieldMap.get(history.getFieldName()).getStatus().equals("1")){
						if(!IssueConstants.ACTIVE.equals(history.getStatus())){
							history.setApprovedBy(oldIssue.getIssueApprover());
							history.setTimeApproved(String.valueOf(System.currentTimeMillis()/1000));
							history.setCommentCode(fieldMap.get(history.getFieldName()).getCommentCode());
							history.setStatus(IssueConstants.ACTIVE);
							issueHistoryDao.merge(history);
						}
						fieldMap.remove(history.getFieldName());
					}
					else{
						history.setApprovedBy(oldIssue.getIssueApprover());
						history.setStatus("0");
						history.setCommentCode(fieldMap.get(history.getFieldName()).getCommentCode());
						issueHistoryDao.merge(history);
					}
				}
			}
			if (fieldMap.size()==0){
				issueDao.detachIssue(oldIssue);
				GrcIssueUpdate latestUpdate = null;
				try{
					latestUpdate = issueDao.findLatestIssueUpdate(oldIssue.getIssueId());
				}
				catch(Exception nre){
					logger.error("No records found in issue update", nre);
				}
				
				if (latestUpdate==null)
				throw new Exception("Latest issue update not found");
				GrcIssue latestIssue = issueUtilMapper.moveLatestToIssueDB(latestUpdate);
				latestIssue.setGrcWfStatus(wfStatusHome.findByStatusType("APPROVED", "ISSUE_MANAGEMENT"));
				syncIssueFields(oldIssue.getVersion(), latestIssue, latestUpdate);
				issueDao.merge(latestIssue);
				//issueDao.flush();
				//issueDao.merge(latestUpdate);
				//syncOtherFields(oldIssue, latestIssue);
				
				long time = System.currentTimeMillis()/1000;
    			GrcIssueAuditLog grcIssueAuditLog = new GrcIssueAuditLog();
        		grcIssueAuditLog.setIssueCode(oldIssue.getIssueId());
        		grcIssueAuditLog.setFromStatusCode("Approved_Pending_Approval");
        		grcIssueAuditLog.setToStatusCode("Approved");
        		grcIssueAuditLog.setActionCode("Reapprove");
        		grcIssueAuditLog.setCommentCode("Approved");
        		grcIssueAuditLog.setCreatedBy(latestIssue.getIssueApprover());
        		grcIssueAuditLog.setTimeCreated(time);
        		grcIssueAuditLog.setActive(IssueConstants.ACTIVE);
        		grcIssueAuditLog.setVersion(latestIssue.getVersion());
    			issueDao.persist(grcIssueAuditLog);
			}
			else {
				oldIssue.setGrcWfStatus(wfStatusHome.findByStatusType("Approved_Pending_Updates", "ISSUE_MANAGEMENT"));
				issueDao.merge(oldIssue);
				
				long time = System.currentTimeMillis()/1000;
    			GrcIssueAuditLog grcIssueAuditLog = new GrcIssueAuditLog();
        		grcIssueAuditLog.setIssueCode(oldIssue.getIssueId());
        		grcIssueAuditLog.setFromStatusCode("Approved_Pending_Approval");
        		grcIssueAuditLog.setToStatusCode("Approved_Pending_Updates");
        		grcIssueAuditLog.setActionCode("RETURN_TO_OWNER");
        		grcIssueAuditLog.setCommentCode("Required some mofications");
        		grcIssueAuditLog.setCreatedBy(oldIssue.getIssueApprover());
        		grcIssueAuditLog.setTimeCreated(time);
        		grcIssueAuditLog.setActive(IssueConstants.ACTIVE);
        		grcIssueAuditLog.setVersion(oldIssue.getVersion());
    			issueDao.persist(grcIssueAuditLog);
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error("Error while persisting Issue: IssueImpl:updateApprovedFields",e);
            ExceptionUtil.logAndThrowBusinessException("DB_ISSUE_REAPPROVE",e,"failed at DB level"+e.getMessage());
		}
	}
	/** 
	 * This method createExceptions has a parameter and foreach loop it creates a list and calls some other method
	 * 
	 * @param issueId this is the only parameter passed over here it is a  String type variable
	 *  @exception This method can throw  NoSuchFileException.
	 * @return There is no any return type for this issue since it is void method
	 */
	public void createExceptions(String issueId) throws Exception {
		List<GrcIssueHistory> exceptionalFlowfields = issueHistoryDao.findHistoryByType(IssueConstants.EXCEPTIONAL_FIELD, issueId);
		for(GrcIssueHistory grcIssueHistory:exceptionalFlowfields) {			
			GrcIssueException exception = issueUtilMapper.convertToExceptionfromHistory(grcIssueHistory);
			issueDao.persist(exception);
		}		
	}
	/**
	 * This method checkIfUserHasBothApprovalAndExceptionalFields has a parameter and returns based on the parameter
	 * 
	 * @param issueId This method has issueId as the only parameter of type String
	 * 
	 * @return boolean this method returns a boolean value
	 */
	public boolean checkIfUserHasBothApprovalAndExceptionalFields(String issueId) throws Exception{		
		return issueHistoryDao.checkConflict(issueId);		
	}
	/**
	 * This method checkIfUserExceptions checks for the history1 is empty or not  based 
	 * 
	 * @param issueCode is the only parameter used here this is a String variable
	 * 
	 * @throws This method may throw null pointer exception 
	 * 
	 * @return boolean This method returns boolean type 
	 */
	public boolean checkIfUserExceptions(String issueCode) throws Exception{
		boolean rValue = false;
		List<GrcIssueHistory> history1 = issueHistoryDao.findHistoryByType(IssueConstants.EXCEPTIONAL_FIELD, issueCode);
		
		if(!history1.isEmpty()) {
			rValue = true;
		}
		return rValue;		
	}
	/**
	 * Private method syncOtherFields gets value through two parameters and has a try and catch block if condition and a foreach loop
	 * and switch statement with some cases 
	 * 
	 *  @param old This is the First parameter of type GrcIssue
	 *  @param latest This is the Second parameter of type GrcIssue
	 *  
	 *  @throws this method throws Exception because it may have NosuchMethod exception or Nullpointer exception 
	 *  @return  This method has no return value since it is void method
	 */

	private void syncOtherFields(GrcIssue old, GrcIssue latest) throws Exception{
		try{
			List<GrcIssueHistory> issueRejections = issueHistoryDao.findApprovalsByNotInType(old.getIssueId(), 
					IssueConstants.ISSUE_TABLE, Integer.parseInt(old.getVersion()));
			if(issueRejections!=null && !issueRejections.isEmpty()){
				for (GrcIssueHistory history : issueRejections){
					switch(history.getTableName()){
					case "GRC_ISSUE_SOURCE":{
						/*if(history.getFieldType() == 1)
							revertSecondarySourceData(old.getIssueId(), String.valueOf(history.getIssueVersion()), latest.getVersion());
						else
							revertPrimarySourceData(old.getIssueId(), String.valueOf(history.getIssueVersion()), latest.getVersion());*/
					}
						break;
					case "GRC_ISSUE_AOL_MAP":
						revertIssueAOLData(old.getIssueId(), String.valueOf(history.getIssueVersion()), latest.getVersion());
						break;
					case "GRC_ISSUE_REGION_MAP":
						revertIssueRegionData(old.getIssueId(), String.valueOf(history.getIssueVersion()), latest.getVersion());
						break;
					case "GRC_ISSUE_PRODUCTS_MAP":
						revertIssueProductData(old.getIssueId(), String.valueOf(history.getIssueVersion()), latest.getVersion());
						break;
					case "GRC_ISSUE_BPC_MAP":
						revertIssueBPCData(old.getIssueId(), String.valueOf(history.getIssueVersion()), latest.getVersion());
						break;
					case "GRC_ISSUE_LINKED_MAP":
						revertIssueLinkedData(old.getIssueId(), String.valueOf(history.getIssueVersion()), latest.getVersion());
						break;
					case "GRC_ISSUE_IFA_MAP":
						revertIssueIFAData(old.getIssueId(), String.valueOf(history.getIssueVersion()), latest.getVersion());
						break;
					case "GRC_ISSUE_RISK_MAP":
						revertIssueRiskTypeData(old.getIssueId(), String.valueOf(history.getIssueVersion()), latest.getVersion());
						break;
					case "GRC_ISSUE_RCT_MAP":
						revertIssueRCData(old.getIssueId(), String.valueOf(history.getIssueVersion()), latest.getVersion());
						break;
					case "GRC_ISSUE_INFO_STAKEHOLDERS":
						revertIssueInformedData(old.getIssueId(), String.valueOf(history.getIssueVersion()), latest.getVersion());
						break;
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error("Error while validateRejectedFields: IssueImpl:validateRejectedFields",e);
            ExceptionUtil.logAndThrowBusinessException("DB_ISSUE",e,"failed at service level"+e.getMessage());
		}
	}
	/**
	 * This private  method  revertPrimarySourceData  has three if conditions and  three parameters 
	 * 
	 * @param issueCode This is the firstparameter of type String 
	 * @param activeVersion This is the secondparameter of type string
	 * @param surrentVersion This is the Third parameter of type string
	 *  
	 *  @return This method does not return any value since it is void
	 */
	private void revertPrimarySourceData(String issueCode, String activeVersion, String currentVersion) throws Exception{
		if(activeVersion.equals(currentVersion))
			return;
		List<GrcIssueSource> dbPISource= issueDao.findPrimaryIssueSource(issueCode,IssueConstants.ACTIVE, currentVersion);
        if(dbPISource.size()!=0){
        	dbPISource.get(0).setActive(IssueConstants.INACTIVE);
        	issueDao.merge(dbPISource.get(0));
        }
        dbPISource= issueDao.findPrimaryIssueSource(issueCode,IssueConstants.ACTIVE, activeVersion);
        if(dbPISource.size()!=0){
        	issueDao.detach(dbPISource.get(0));
        	dbPISource.get(0).setSourceCode(null);
        	dbPISource.get(0).setIssueVersion(currentVersion);
        	issueDao.persistIssueSource(dbPISource.get(0));
        }
	}
	/**
	 * This private  method  revertSecondarySourceData  has three if conditions and  three parameters 
	 * 
	 * @param issueCode This is the firstparameter of type String 
	 * @param activeVersion This is the secondparameter of type string
	 * @param surrentVersion This is the Third parameter of type string
	 *  
	 *  @return This method does not return any value since it is void
	 */
	private void revertSecondarySourceData(String issueCode, String activeVersion, String currentVersion) throws Exception{
		if(activeVersion.equals(currentVersion))
			return;
		List<GrcIssueSource> dbPISource= issueDao.findSecondaryIssueSource(issueCode,IssueConstants.ACTIVE, currentVersion);
		if(dbPISource.size()!=0){
			for (GrcIssueSource entity : dbPISource){
				entity.setActive(IssueConstants.INACTIVE);
				issueDao.merge(entity);
			}
		}
		dbPISource= issueDao.findSecondaryIssueSource(issueCode,IssueConstants.ACTIVE, activeVersion);
		if(dbPISource.size()!=0){
			for (GrcIssueSource entity : dbPISource){
				issueDao.detach(entity);
				entity.setSourceCode(null);
				entity.setIssueVersion(currentVersion);
				issueDao.persistIssueSource(entity);
			}
		}
	}
	/**
	 * This private  method  revertIssueAOLData  has three if conditions and  three parameters 
	 * 
	 * @param issueCode This is the firstparameter of type String 
	 * @param activeVersion This is the secondparameter of type string
	 * @param surrentVersion This is the Third parameter of type string
	 *  
	 *  @return This method does not return any value since it is void
	 */
	private void revertIssueAOLData(String issueCode, String activeVersion, String currentVersion) throws Exception{
		if(activeVersion.equals(currentVersion))
			return;
		GrcIssueAolMap issueAol = aolMapDao.findByIssueId(issueCode,IssueConstants.ACTIVE, currentVersion);
		if (issueAol!=null){
			issueAol.setTimeUpdated(new BigDecimal(System.currentTimeMillis() / 1000));
			issueAol.setIsActive(IssueConstants.INACTIVE);
    		issueDao.merge(issueAol);
		}
		GrcIssueAolMap issueAolActive = aolMapDao.findByIssueId(issueCode,IssueConstants.ACTIVE, activeVersion);
		if (issueAolActive!=null){
			issueDao.detach(issueAolActive);
			issueAolActive.setAolMapCode(null);
			issueAolActive.setVersion(currentVersion);
    		issueDao.persist(issueAolActive);
		}
	}
	 /**
	  * This method  revertIssueRegionData has three parameters and runs two 
	  * foreach loop based on that some other methods are
	  * being called and this method creates a list 
	  * 
	  * @param issueCode This is the First Parameter of type String 
	  * @param activeVersion This is the Second Parameter of type String 
	  * @param currentVersion This is the Third parameter of type String
	  * 
	  * @throws Here NullPointerException or NoSuchFileExeption or NoSuchFeildException
	  * may happen so Throw was happend here
	  */

	private void revertIssueRegionData(String issueCode, String activeVersion, String currentVersion) throws Exception{
		if(activeVersion.equals(currentVersion))
			return;
		List<GrcIssueRegionMap> dbRegionList = issueDao.findByIssueId(issueCode,IssueConstants.ACTIVE, currentVersion);
        for(GrcIssueRegionMap temp:dbRegionList){
        	temp.setIsActive(IssueConstants.INACTIVE);
        	issueDao.merge(temp);
        }
        dbRegionList= issueDao.findByIssueId(issueCode,IssueConstants.ACTIVE, activeVersion);
        for(GrcIssueRegionMap temp:dbRegionList){
        	issueDao.detach(temp);
        	temp.setRcspMapCode(null);
        	temp.setVersion(currentVersion);
        	issueDao.persist(temp);
        }
	}
	 /**
	  * This method revertIssueProductData has three parameters and runs two 
	  * foreach loop based on that some other methods are
	  * being called and this method creates a list 
	  * 
	  * @param issueCode This is the First Parameter of type String 
	  * @param activeVersion This is the Second Parameter of type String 
	  * @param currentVersion This is the Third parameter of type String
	  * 
	  * @throws Here NullPointerException or NoSuchFileExeption or NoSuchFeildException
	  * may happen so Throw was happend here
	  */

	
	private void revertIssueProductData(String issueCode, String activeVersion, String currentVersion) throws Exception{
		if(activeVersion.equals(currentVersion))
			return;
		List<GrcIssueProductMap> dbProductList=issueDao.getProducts(issueCode,IssueConstants.ACTIVE, currentVersion);
        for(GrcIssueProductMap temp:dbProductList){
    		temp.setActive(IssueConstants.INACTIVE);
    		issueProductDao.merge(temp);
        }
        dbProductList= issueDao.getProducts(issueCode,IssueConstants.ACTIVE, activeVersion);
		for(GrcIssueProductMap temp:dbProductList){
			issueProductDao.detach(temp);
			temp.setMapCode(null);
			temp.setVersion(currentVersion);
			issueProductDao.persist(temp);
		}
	}
	 /**
	  * This method revertIssueRCData has three parameters and runs two 
	  * foreach loop based on that some other methods are
	  * being called and this method creates a list 
	  * 
	  * @param issueCode This is the First Parameter of type String 
	  * @param activeVersion This is the Second Parameter of type String 
	  * @param currentVersion This is the Third parameter of type String
	  * 
	  * @throws Here NullPointerException or NoSuchFileExeption or NoSuchFeildException
	  * may happen so Throw was happend here
	  */
	private void revertIssueLinkedData(String issueCode, String activeVersion, String currentVersion) throws Exception{
		if(activeVersion.equals(currentVersion))
			return;
		List<GrcIssueLinkedMap> dbLinkedIssueList=linkedIssueDao.findByIssueId(issueCode,IssueConstants.ACTIVE, currentVersion);
        for(GrcIssueLinkedMap persistObject: dbLinkedIssueList){
        	persistObject.setActive(IssueConstants.INACTIVE);
        	linkedIssueDao.merge(persistObject);
        }
        dbLinkedIssueList= linkedIssueDao.findByIssueId(issueCode,IssueConstants.ACTIVE, activeVersion);
		for(GrcIssueLinkedMap temp:dbLinkedIssueList){
			linkedIssueDao.detach(temp);
			temp.setId(null);
			temp.setVersion(currentVersion);
			linkedIssueDao.persist(temp);
		}
	}
	 /**
	  * This method revertIssueIFAData has three parameters and runs two 
	  * foreach loop based on that some other methods are
	  * being called and this method creates a list 
	  * 
	  * @param issueCode This is the First Parameter of type String 
	  * @param activeVersion This is the Second Parameter of type String 
	  * @param currentVersion This is the Third parameter of type String
	  * 
	  * @throws Here NullPointerException or NoSuchFileExeption or NoSuchFeildException
	  * may happen so Throw was happend here
	  */
	private void revertIssueIFAData(String issueCode, String activeVersion, String currentVersion) throws Exception{
		if(activeVersion.equals(currentVersion))
			return;
		List<GrcIssueIfaMap> dbifaList=ifaDao.findByIssueId(issueCode,IssueConstants.ACTIVE, currentVersion);
        for(GrcIssueIfaMap temp:dbifaList){
        	temp.setIsActive(IssueConstants.INACTIVE);
        	ifaDao.merge(temp);
        }
        dbifaList= ifaDao.findByIssueId(issueCode,IssueConstants.ACTIVE, activeVersion);
		for(GrcIssueIfaMap temp:dbifaList){
			ifaDao.detach(temp);
			temp.setId(null);
			temp.setVersion(currentVersion);
			ifaDao.persist(temp);
		}
	}
	 /**
	  * This method revertIssueRiskTypeData has three parameters and runs two 
	  * foreach loop based on that some other methods are
	  * being called and this method creates a list 
	  * 
	  * @param issueCode This is the First Parameter of type String 
	  * @param activeVersion This is the Second Parameter of type String 
	  * @param currentVersion This is the Third parameter of type String
	  * 
	  * @throws Here NullPointerException or NoSuchFileExeption or NoSuchFeildException
	  * may happen so Throw was happend here
	  */
	private void revertIssueRiskTypeData(String issueCode, String activeVersion, String currentVersion) throws Exception{
		if(activeVersion.equals(currentVersion))
			return;
		List<GrcIssueRiskMap> dbRiskCatogEntityList=riskCatDao.findByIssueId(issueCode,IssueConstants.ACTIVE, currentVersion);
        for (GrcIssueRiskMap riskMap : dbRiskCatogEntityList) {
        	riskMap.setIsActive(IssueConstants.INACTIVE);
        	riskCatDao.merge(riskMap);
        }
        dbRiskCatogEntityList= riskCatDao.findByIssueId(issueCode,IssueConstants.ACTIVE, activeVersion);
		for(GrcIssueRiskMap temp:dbRiskCatogEntityList){
			riskCatDao.detach(temp);
			temp.setId(null);
			temp.setVersion(currentVersion);
			riskCatDao.persist(temp);
		}
	}
	 /**
	  * This method revertIssueRCData has three parameters and runs two 
	  * foreach loop based on that some other methods are
	  * being called and this method creates a list 
	  * 
	  * @param issueCode This is the First Parameter of type String 
	  * @param activeVersion This is the Second Parameter of type String 
	  * @param currentVersion This is the Third parameter of type String
	  * 
	  * @throws Here NullPointerException or NoSuchFileExeption or NoSuchFeildException
	  * may happen so Throw was happend here
	  */
	private void revertIssueRCData(String issueCode, String activeVersion, String currentVersion) throws Exception{
		if(activeVersion.equals(currentVersion))
			return;
		List<GrcRootCauseMap> grcRTC = rtcDao.findByIssueId(issueCode, currentVersion);
        for(GrcRootCauseMap grcRootCause:grcRTC){
    		grcRootCause.setIsActive(IssueConstants.INACTIVE);
    		rtcDao.merge(grcRootCause);
        }
        grcRTC= rtcDao.findByIssueId(issueCode, activeVersion);
		for(GrcRootCauseMap temp:grcRTC){
			rtcDao.detach(temp);
			temp.setRootCauseSeq(null);
			temp.setVersion(currentVersion);
			rtcDao.persist(temp);
		}
	}
	 /**
	  * This method revertIssueInformedData has three parameters and runs two 
	  * foreach loop based on that some other methods are
	  * being called and this method creates a list 
	  * 
	  * @param issueCode This is the First Parameter of type String 
	  * @param activeVersion This is the Second Parameter of type String 
	  * @param currentVersion This is the Third parameter of type String
	  * 
	  * @throws Here NullPointerException or NoSuchFileExeption or NoSuchFeildException
	  * may happen so Throw was happend here
	  */
	
	private void revertIssueInformedData(String issueCode, String activeVersion, String currentVersion) throws Exception{
		if(activeVersion.equals(currentVersion))
			return;
		List<GrcIssueInfoStakeholders> ishDbList = issueISHDao.findByIssueId(issueCode, IssueConstants.ACTIVE, currentVersion);
        for ( GrcIssueInfoStakeholders ish : ishDbList){
        	ish.setActive(IssueConstants.INACTIVE);
        	issueDao.merge(ish);
        }
        ishDbList= issueISHDao.findByIssueId(issueCode, IssueConstants.ACTIVE, activeVersion);
		for(GrcIssueInfoStakeholders temp:ishDbList){
			issueDao.detach(temp);
			temp.setId(null);
			temp.setVersion(currentVersion);
			issueDao.persistIssueStakeholders(temp);
		}
	}
	 /**
	  * This method revertIssueBPCData has three parameters and runs two foreach loop based on that some other methods are
	  * being called and this method creates a list 
	  * 
	  * @param issueCode This is the First Parameter of type String 
	  * @param activeVersion This is the Second Parameter of type String 
	  * @param currentVersion This is the Third parameter of type String
	  * 
	  * @throws Here NullPointerException or NoSuchFileExeption or NoSuchFeildException
	  * may happen so Throw was happend here
	  */
	
	private void revertIssueBPCData(String issueCode, String activeVersion, String currentVersion) throws Exception{
		if(activeVersion.equals(currentVersion))
			return;
		List<GrcIssueBpcMap> dbBPCList=issueBPCDao.getIssueBPC(issueCode,IssueConstants.ACTIVE, currentVersion);
		for(GrcIssueBpcMap temp:dbBPCList){
        	temp.setActive(IssueConstants.INACTIVE);
        	issueBPCDao.merge(temp);
        }
		dbBPCList=issueBPCDao.getIssueBPC(issueCode,IssueConstants.ACTIVE, activeVersion);
		for (GrcIssueBpcMap entity : dbBPCList){
			issueBPCDao.detach(entity);
			entity.setId(null);
			entity.setVersion(currentVersion);
			issueBPCDao.persist(entity);
		}
	}
	/**
	 * This method syncIssueFields has some parameters and  does some operations based on that 
	 * it has a try and catch block it throws Business Exeption
	 
	 *@param oldVersion This is the first parameter of type String
	 *@param latest 	This is the Second paramter of type GrcIssue
	 *@param latestUpdate 	This  is the third parameter of type GrcIssueUpdate
	 *
	 *@exception BusinessException This is a class Name Exception, this is called when the try block fails 
	 
	  */
	private void syncIssueFields(String oldVersion, GrcIssue latest, GrcIssueUpdate latestUpdate) throws Exception{
		try{
			List<GrcIssueHistory> issueRejections = issueHistoryDao.findApprovalsByType(latest.getIssueId(), 
					IssueConstants.ISSUE_TABLE, Integer.parseInt(oldVersion));
			if(issueRejections!=null && !issueRejections.isEmpty()){
				for (GrcIssueHistory history : issueRejections){
					switch(history.getFieldName()){
						case IssueConstants.ISSUE_NAME:{
							latest.setIssueName(history.getNewValue());
		 					latestUpdate.setIssueName(history.getNewValue());
						}
							break;
						case IssueConstants.ISSUE_SUMMARY:{
							latest.setExecutiveSummary(history.getNewValue());
							latestUpdate.setExecutiveSummary(history.getNewValue());
						}
							break;
						case IssueConstants.IA:{
							latest.setImpactAnalysis(history.getNewValue());
							latestUpdate.setImpactAnalysis(history.getNewValue());
						}
							break;
						case IssueConstants.ISSUE_FA:{
							latest.setFunctionalAreaCode(history.getNewValue());
							latestUpdate.setFunctionalAreaCode(history.getNewValue());
						}
							break;
						case IssueConstants.ISSUE_OWNER:{
							latest.setIssueOwner(history.getNewValue());
							latestUpdate.setIssueOwner(history.getNewValue());
						}
							break;
						case IssueConstants.ISSUE_OWNER_DELEGATE:{
							latest.setIssueOwnerDelegate(history.getNewValue());
							latestUpdate.setIssueOwnerDelegate(history.getNewValue());
						}
							break;
						case IssueConstants.ISSUE_EXECUTIVE_OWNER:{
							latest.setIssueExecutiveOwner(history.getNewValue());
							latestUpdate.setIssueExecutiveOwner(history.getNewValue());
						}
							break;
						case IssueConstants.ISSUE_APPROVER:{
							latest.setIssueApprover(history.getNewValue());
							latestUpdate.setIssueApprover(history.getNewValue());
						}
							break;
						case IssueConstants.ICP_REPRESENTATIVE:{
							latest.setIssueIcp(history.getNewValue());
							latestUpdate.setIssueIcp(history.getNewValue());
						}
							break;
						case IssueConstants.BUSINESS_SME_POC:{
							latest.setBuSme(history.getNewValue());
							latestUpdate.setBuSme(history.getNewValue());
						}
							break;
						case IssueConstants.RCO:{
							latest.setIssueRco(history.getNewValue());
							latestUpdate.setIssueRco(history.getNewValue());
						}
							break;
						case IssueConstants.GGRC_RCO_LEADER:{
							latest.setGrcRcoLeader(history.getNewValue());
							latestUpdate.setGrcRcoLeader(history.getNewValue());
						}
							break;
						case IssueConstants.ISSUE_IDENTIFIED_DATE:{
							latest.setIssueIdentifiedDate(new BigDecimal(history.getNewValue()));
							latestUpdate.setIssueIdentifiedDate(new BigDecimal(history.getNewValue()));
						}
							break;
						case IssueConstants.FIA_DUE_DATE:{
							latest.setFiADueDate(new BigDecimal(history.getNewValue()));
							latestUpdate.setFiADueDate(new BigDecimal(history.getNewValue()));
						}
							break;
						case IssueConstants.CESS_COMP_DATE:{
							latest.setCessationCompletedOnDate(new BigDecimal(history.getNewValue()));
							latestUpdate.setCessationCompletedOnDate(new BigDecimal(history.getNewValue()));
						}
							break;
						case IssueConstants.CESS_DUE_DATE:{
							latest.setCessationDueDate(new BigDecimal(history.getNewValue()));
							latestUpdate.setCessationDueDate(new BigDecimal(history.getNewValue()));
						}
							break;
						case IssueConstants.CURRENT_DUE_DATE:{
							latest.setCurrentDueDate(new BigDecimal(history.getNewValue()));
							latestUpdate.setCurrentDueDate(new BigDecimal(history.getNewValue()));
						}
							break;
						case IssueConstants.ISSUE_RATING:{
							GrcIssueRating rating = issueDao.getIssueRatingByType(history.getNewValue());
							latest.setIssueRating(rating);
							latestUpdate.setIssueRating(rating);
						}
							break;
						case IssueConstants.FIA_COMP_DATE:{
							latest.setFiaCompletedDate(new BigDecimal(history.getNewValue()));
							latestUpdate.setFiaCompletedDate(new BigDecimal(history.getNewValue()));
						}
							break;
						case IssueConstants.FIA_DESC:{
							latest.setFiaDescription(history.getNewValue());
							latestUpdate.setFiaDescription(history.getNewValue());
						}
							break;
						case IssueConstants.RCA:{
							latest.setRootCauseAnalysis(history.getNewValue());
							latestUpdate.setRootCauseAnalysis(history.getNewValue());
						}
							break;
						case IssueConstants.EXTERNAL_DETAILS:{
							latest.setExternalIssueDetails(history.getNewValue());
							latestUpdate.setExternalIssueDetails(history.getNewValue());
						}
							break;
						case IssueConstants.RATING_RATIONALE:{
							latest.setRatingRationale(history.getNewValue());
							latestUpdate.setRatingRationale(history.getNewValue());
						}
							break;
						case IssueConstants.PROGRESS:{
							latest.setIssueProgressPercent(history.getNewValue());
							latestUpdate.setIssueProgressPercent(history.getNewValue());
						}
							break;
						case IssueConstants.REVIEW_FREQUENCY:{
							latest.setReviewFrequency(history.getNewValue());
							latestUpdate.setReviewFrequency(history.getNewValue());
						}
							break;
						case IssueConstants.NEXT_REVIEW_DATE:{
							latest.setNextReviewDate(new BigDecimal(history.getNewValue()));
							latestUpdate.setNextReviewDate(new BigDecimal(history.getNewValue()));
						}
							break;
						case IssueConstants.ISSUE_HEALTH:{
							GrcIssueHealth health = issueHealthDao.getIssueHealthByType(history.getNewValue());
							latest.setIssueHealth(health);
							latestUpdate.setIssueHealth(health);
						}
							break;
						case IssueConstants.ISSUE_LOE:{
							latest.setLoeCode(history.getNewValue());
							latestUpdate.setLoeCode(history.getNewValue());
						}
							break;
					}
				}	
			}
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error("Error while validateRejectedFields: IssueImpl:validateRejectedFields",e);
            ExceptionUtil.logAndThrowBusinessException("DB_ISSUE",e,"failed at service level"+e.getMessage());
		}
	}
}
