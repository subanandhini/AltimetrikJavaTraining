package com.paypal.erc.grcprocessserv.impl.issue;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.paypal.api.platform.erc.grc.*;
import com.paypal.api.platform.erc.grc.GrcCustomFilter;
import com.paypal.erc.grc.common.dal.models.issue.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import com.paypal.erc.grc.common.lifecycle.bo.context.ExecutionContext;
import com.paypal.erc.grc.common.lifecycle.bo.workflow.LifecycleResponseBO;
import com.paypal.erc.grc.common.lifecycle.processor.ILifecycleMgmtService;
import com.paypal.erc.grc.common.util.cal.CalTransactionUtil;
import com.paypal.erc.grc.common.util.cal.CalUtil;
import com.paypal.erc.grc.common.util.exceptions.ExceptionUtil;
import com.paypal.erc.grcprocessserv.issue.IIssue;
import com.paypal.erc.grcprocessserv.lifecycle.context.issue.IssueUtils;
import com.paypal.erc.grcprocessserv.repositories.constants.IssueConstants;
import com.paypal.erc.grcprocessserv.repositories.impl.issue.CommentImpl;
import com.paypal.erc.grcprocessserv.repositories.impl.issue.CustomFilterImpl;
import com.paypal.erc.grcprocessserv.repositories.impl.issue.IssueImpl;
import com.paypal.erc.grcprocessserv.repositories.mappers.issue.CommentUtilMapper;
import com.paypal.erc.grcprocessserv.repositories.mappers.issue.IssueUtilMapper;
import com.paypal.infra.util.cal.CalStatus;

@Scope("request")
@Service("IssueServiceImpl")
public class IssueServiceImpl implements IIssue {

	private static final Logger logger = LoggerFactory.getLogger(IssueServiceImpl.class);
	@Autowired
	IssueImpl issueImpl;

	@Autowired
	IssueUtilMapper issueUtilMapper;

	@Autowired
    CommentImpl commentImpl;

	@Autowired
    CommentUtilMapper commentUtilMapper;

	@Autowired
	IssueImpl issueMgmt;
	
	@Autowired
    IssueUtils issueUtils;
	
	@Autowired
	ILifecycleMgmtService lifecycleMgmtService;

	@Autowired
    CustomFilterImpl customFilterImpl;
/**
 * This method createIssue was a overrided method
 * this method has a try and catch block and returns response object from master table
 * 
 * @param issue this is the only parameter and it is a GrcIssues type
 * @exception BusinessException This method throws BusinessException 
 * 
 * @return Response This method retruns the response object
 */

	@Override
	public Response createIssue(GrcIssues issue) {
		String corr_id = CalTransactionUtil.getCorrelationId();

		try {
			issueImpl.save(issue);
		} catch (Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_CREATE_ISSUE_CHECK", e,
					"failed at Service level");
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);

		return Response.status(Response.Status.CREATED).build();
	}
/**
 * This method updateIssue is a overrided method it takes in value through the parameter and checks for exception
 *  and returns Response object from master table
 *  
 *  @param id this is the first parameter which is a string type
 *  @param issues This is the Second parameter which is a GrcIssues type
 *  
 *  @exception Business Exception This is the exception comeover here
 *  
 *  @return Response This method returns Response object
 */
    @Override
    public Response updateIssue(String id, GrcIssues issue) {
        String corr_id = CalTransactionUtil.getCorrelationId();
        GrcIssues res = null;
        try {
            issue.setIssueCode(id);
            res = issueImpl.updateIssue(issue);
            CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
        }
        catch(Exception e){
            logger.error("Update Issue Failed",e);
            logger.error("Failed at Service level",e);
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_ISSUE_UPDATE_CHECK",e,"failed at Service level");
        }
        return Response.status(Response.Status.OK).entity(res).build();
    }
	/**
	 * This method getIssue gets its value from the parameter and returns the
	 *  response object from the master table this deals with products, regions and categories
	 *  
	 *  @param id This is the only parameter which is passed over here and it is string type
	 *  
	 *   @exception Business Exception there may be some chance forBusiness Exception 
	 *   
	 *   @return Response This method returns Response object from the master table
	 *  	 */
	@Override
	public Response getIssue(String id) {
		String corr_id = CalTransactionUtil.getCorrelationId();
		GrcIssues returnValue = null;
		try {
			GrcIssue result = issueImpl.findIssue(id);
			returnValue = issueUtilMapper.convertDBTORequest(result);
			String active = IssueConstants.ACTIVE;
			String version = result.getVersion();
			List<GrcIssueSource> issueSourceList= issueImpl.getIssueSource(id, active, version);
			PrimaryIssueSource primaryIssueSource = issueUtilMapper.convertPrimaryIssueSourceListToResponse(issueSourceList);
			SecondaryIssueSource secondaryIssueSource = issueUtilMapper.convertSecondaryIssueSourceListToResponse(issueSourceList);
			returnValue.setPrimaryIssueSource(primaryIssueSource);
			returnValue.setSecondaryIssueSource(secondaryIssueSource);

			//Region
            List<GrcIssueRegionMap> grcIssueRegionMap = result.getGrcIssueRegionMap();
            List<String> regionList = new ArrayList<>(10);
            if(grcIssueRegionMap != null) {
                for (GrcIssueRegionMap map : grcIssueRegionMap) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(map.getGrcIssueProvince().getProvinceCode());
                    sb.append(":");
                    sb.append(map.getGrcIssueProvince().getProvinceName());
                    regionList.add(sb.toString());
                }
            }
            returnValue.setIssueRegion(regionList);

            //Products
            List<GrcIssueProductMap> grcIssueProductMaps = result.getGrcIssueProductMap();
            List<String> productMap = new ArrayList<>(10);
            if(grcIssueProductMaps != null) {
                for (GrcIssueProductMap map : grcIssueProductMaps) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(map.getFeatures().getFeatureCode());
                    sb.append(":");
                    sb.append(map.getFeatures().getFeatureDetail());
                    productMap.add(sb.toString());
                }
            }
            returnValue.setSourceProduct(productMap);

            //Categories
            Set<GrcIssueBpcMap> grcIssueBpcMaps = result.getGrcIssueBcpMap();
            List<String> categoriesMap = new ArrayList<>(10);
            if(grcIssueBpcMaps != null) {
                for (GrcIssueBpcMap map : grcIssueBpcMaps) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(map.getCategoryCode());
                    sb.append(":");
                    sb.append(map.getGrcBusniessProcessCategory().getCategoryDetails());
                    categoriesMap.add(sb.toString());
                }
            }
            returnValue.setBpc(categoriesMap);

            CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		} catch (Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_ISSUE_CHECK", e, "failed at Service level");
		}
		return Response.status(Response.Status.OK).entity(returnValue).build();
	}

	/**
	 * This method getIssueProgress  this is a overrided method which
	 * returns list of issue progress from master table
	 * 
	 * @exception Business Exception was the only exception done over here
	 * 
	 * @author prasubramanian
	 * @return Response This method returns a response object from the master table
	 * 
	 */
	@Override
	public Response getIssueProgress() {
		String corr_id = CalTransactionUtil.getCorrelationId();
		List<IssueProgress> issueProgress = null;
		try {
			String active = IssueConstants.ACTIVE;
			List<GrcIssueProgress> grcIssueProgressList = issueImpl.getIssueProgress(active);
			issueProgress = issueUtilMapper.convertDBToResponse(grcIssueProgressList);
		} catch (Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_ISSUE_PROGRESS", e,
					"failed at Service level");
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(issueProgress).build();
	}

	/**
	 * This method getIssueResponse performs some operations and 
	 * returns Response from master table
	 * 
	 * @exception Business exception There are some chance for this exception
	 *  
	 * 
	 * @author prasubramanian
	 * @return Response This method returns the Response object from the master table
	 */
	@Override
	public Response getIssueResponse() {
		String corr_id = CalTransactionUtil.getCorrelationId();
		List<IssueResponse> issueResponse = null;
		try {
			List<GrcIssueResponse> grcIssueResponseList = issueImpl.getIssueResponse();
			issueResponse = issueUtilMapper.convertIssueResponse(grcIssueResponseList);
		} catch (Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_ISSUE_RESPONSE", e,
					"failed at Service level");
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(issueResponse).build();
	}
	/**
	 * This method getLevelOfEffort is overrided this performs some operations and 
	 * returns Response from master table
	 * 
	 * @exception Business exception There are some chance for this exception
	 *  
	 * 
	 * @author prasubramanian
	 * @return Response This method returns the Response object from the master table
	 */
	@Override
	public Response getLevelOfEffort() {
		String corr_id = CalTransactionUtil.getCorrelationId();
		List<GrcIssueEffortLevel> effortLevelList = null;
		List<IssueLevelOfEffort> respList = new ArrayList<IssueLevelOfEffort>();
		try {
			effortLevelList = issueImpl.getLevelOfEffortList(IssueConstants.ACTIVE);
			respList = issueUtilMapper.convertLevelOfEffort(effortLevelList);
		} catch (Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_ISSUE_LEVEL_OF_EFFORT", e,
					"failed at Service level");
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(respList).build();
	}
	/**
	 * This method getReviewFrequency is overrided this performs some operations and 
	 * returns  Response from master table
	 * 
	 * @exception Business exception There are some chance for this exception
	 *  
	 * @return Response This method returns the Response object from the master table
	 */
	@Override
	public Response getReviewFrequency() {
		String corr_id = CalTransactionUtil.getCorrelationId();
		List<GrcIssueReviewFrequency> entityList = null;
		List<IssueReviewFrequency> respList = new ArrayList<IssueReviewFrequency>();
		try {
			String active = IssueConstants.ACTIVE;
			entityList = issueImpl.getIssueReviewFrequency(active);
			respList = issueUtilMapper.convertReviewFrequency(entityList);
		} catch (Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_ISSUE_REIVEW_FREQUENCY", e,
					"failed at Service level");
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(respList).build();
	}
	/**
	 * This method getIssueHealth is overrided and this is a cacheable annotation is used 
	 * so the return value is cached this performs some operations and 
	 * returns Response from master table
	 * 
	 * @exception Business exception There are some chance for this exception
	 *  
	 * @return Response This method returns the cahched Response object from the master table
	 */
	@Override
	@Cacheable("grc")
	public Response getIssueHealth() {
		String corr_id = CalTransactionUtil.getCorrelationId();
		List<GrcIssueHealth> entityList = null;
		List<IssueHealth> healthList = null;
		try {
			String active = IssueConstants.ACTIVE;
			entityList = issueImpl.getIssueHealth(active);
			healthList = issueUtilMapper.convertIssueHealth(entityList);
		} catch (Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_ISSUE_REIVEW_FREQUENCY", e,
					"failed at Service level");
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(healthList).build();
	}
	/**
	 * This method getAllIssues is overrided this performs some operations and 
	 * returns Response from master table
	 * 
	 * @exception Business exception There are some chance for this exception
	 *  
	 * 
	 * @return Response This method returns the Response object from the master table
	 */
    @Override
    public Response getAllIssues() {
        String corr_id = CalTransactionUtil.getCorrelationId();

        List<GrcIssues> returnValue = null;
        try {
            List<GrcIssue> result = issueImpl.findAllIssues();
            returnValue = result.stream()
                    .map(i -> issueUtilMapper.convertDBTORequest(i))
                    .collect(Collectors.toList());

            CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
        } catch (Exception e) {
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_ALL_ISSUES_CHECK",
                    e,"failed at Service level");
        }
        return Response.status(Response.Status.OK).entity(returnValue).build();
    }
    /**
	 * This method getIssueHistory is overrided this performs some operations and 
	 * returns Response from master table
	 * 
	 * @param issueId this is the first parameter of type String
	 * @param type This is the second parameter of type String
	 * 
	 * @exception Business exception There are some chance for this exception
	 *  
	 * 
	 * @author prasubramanian
	 * @return Response This method returns the Response object from the master table
	 */
    @Override
	public Response getIssueHistory(String issueId, String type) {
		String corr_id = CalTransactionUtil.getCorrelationId();
		List<IssueHistory> historyList = null;
		try {
			historyList = issueImpl.getIssueHistory(issueId, type);
		} catch (Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_ISSUE_HISTORY", e,
					"failed at Service level");
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(historyList).build();
	}
    /**
	 * This method saveIssueHistory is overrided this performs some operations and 
	 * returns issue Response from master table
	 * 
	 * @param dtoList This is the only parameter used of type list
	 * @exception Business exception There are some chance for this exception
	 *  
	 * @return Response This method returns the Response object from the master table
	 */
    @Override
    public Response saveIssueHistory(List<IssuesFieldUpdate> dtoList) {
		String corr_id = CalTransactionUtil.getCorrelationId();
		try {
			issueImpl.saveHistory(dtoList);
		} catch (Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_SAVE_ISSUE_HISTORY", e,
					"failed at Service level");
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).build();
	}
    /**
	 * This method getRiskTypes is overrided this performs some operations and 
	 * this is a method has cacheable anotation so the return value is being cached and it
	 * returns issue Response from master table
	 * 
	 * @exception Business exception There are some chance for this exception
	 *  
	 * 
	 * @author prasubramanian
	 * @return Response This method returns the Response object from the master table
	 */
    @Cacheable("grc")
    public Response getRiskTypes() {
        HashMap<String,List<Object>> jsonRes = new HashMap<>();
        String corr_id = CalTransactionUtil.getCorrelationId();
        try{
            StringBuffer query = new StringBuffer("SELECT p FROM GrcRiskType p where p.riskParent = null");
            List<Object> res = issueMgmt.getLookUps(query.toString());
            jsonRes.put("items",res);
            CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
        } catch(Exception e){
            logger.error("Get Issue Risk Category Failed",e);
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_RISK_TYPE_CHECK",e,"Get risk type Failed");
        }

        return Response.status(Response.Status.OK).entity(jsonRes).build();
    }
    /**
	 * This method getAllRiskTypes is overrided this performs some operations and 
	 * it has cacheable annotation so the return value will be cached
	 * returns issue Response from master table
	 * 
	 * @exception Business exception There are some chance for this exception
	 *  
	 * @return Response This method returns the Response object from the master table
	 */
    @Cacheable("grc")
    public Response getAllRiskTypes() {
        HashMap<String,List<Object>> jsonRes = new HashMap<>();
        String corr_id = CalTransactionUtil.getCorrelationId();
        try{
            StringBuffer query = new StringBuffer("SELECT p FROM GrcRiskType p");
            List<Object> res = issueMgmt.getLookUps(query.toString());
            jsonRes.put("items",res);
            CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
        } catch(Exception e){
            logger.error("Get Issue Risk Category Failed",e);
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_RISK_TYPE_CHECK",e,"Get risk type Failed");
        }

        return Response.status(Response.Status.OK).entity(jsonRes).build();
    }
    /**
	 * This method getRootCauseCategory is overrided this performs some operations and 
	 * it has cacheable annotation so the return value will be cached
	 * returns issue Response from master table
	 * 
	 * @exception Business exception There are some chance for this exception
	 *  
	 * @return Response This method returns the Response object from the master table
	 */
	@Override
	@Cacheable("grc")
	public Response getRootCauseCategory() {
		HashMap<String, List<Object>> jsonRes = new HashMap<>();
		String corr_id = CalTransactionUtil.getCorrelationId();
		try {
			StringBuffer query = new StringBuffer("SELECT p FROM GrcRootCauseCategory p where p.rtcParent=null");
			List<Object> res = issueMgmt.getLookUps(query.toString());
			jsonRes.put("items",res);
            CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		} catch(Exception e){
            logger.error("Get Issue Root Cause Category Failed",e);
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_RISK_TYPE_CHECK",e,"Get Root Cause Category Failed");
        }

		return Response.status(Response.Status.OK).entity(jsonRes).build();
	}
	/**
	 * This method getAllRootCauseCategory is overrided this performs some operations and 
	 * This has cacheable annotation so this method will return a cached 
	 * issue Response from master table
	 * 
	 * @exception Business exception There are some chance for this exception
	 *  
	 * @return Response This method returns the Response object from the master table
	 */
	@Override
	@Cacheable("grc")
	public Response getAllRootCauseCategory() {
		HashMap<String, List<Object>> jsonRes = new HashMap<>();
		String corr_id = CalTransactionUtil.getCorrelationId();
		try {
			StringBuffer query = new StringBuffer("SELECT p FROM GrcRootCauseCategory p");
			List<Object> res = issueMgmt.getLookUps(query.toString());
			jsonRes.put("items",res);
            CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		} catch(Exception e){
            logger.error("Get Issue Root Cause Category Failed",e);
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_RISK_TYPE_CHECK",e,"Get Root Cause Category Failed");
        }

		return Response.status(Response.Status.OK).entity(jsonRes).build();
	}
	/**
	 * This method getIssueRating  is overrided this performs some operations and 
	 * This has cacheable annotation so this method will return a cached 
	 * issue Response from master table
	 * 
	 * @exception Business exception There are some chance for this exception
	 *  
	 * @return Response This method returns the Response object from the master table
	 */
	@Override
	@Cacheable("grc")
	public Response getIssueRating() {
		HashMap<String, List<Object>> jsonRes = new HashMap<>();
		String corr_id = CalTransactionUtil.getCorrelationId();
		try {
			StringBuffer query = new StringBuffer("SELECT p FROM GrcIssueRating p ORDER BY ratingId ASC");
			List<Object> res = issueMgmt.getLookUps(query.toString());
			jsonRes.put("items",res);
            CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		} catch(Exception e){
            logger.error("Get Issue Rating Failed",e);
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_ISSUE_RATING_CHECK",e,"Get Issue Rating Failed");
        }

		return Response.status(Response.Status.OK).entity(jsonRes).build();
	}

   
	/**
	 * This method getBPCHierarchy is overrided this performs some operations it has some 
	 * for each loops and  returns issue Response from master table
	 * 
	 * @exception Business exception There are some chance for this exception
	 *  
	 * @return Response This method returns the Response object from the master table
	 */

    @Override    
    public Response getBPCHierarchy() {
        String corr_id = CalTransactionUtil.getCorrelationId();
        List<Bpc> dtoList=new ArrayList<Bpc>();
        List<GrcBpcBusiness> entityList=new ArrayList<GrcBpcBusiness>();
        try {
            entityList=issueImpl.getAllBPC(IssueConstants.ACTIVE, IssueConstants.DEFAULT_VERSION);
            dtoList = entityList.stream().map(dto -> issueUtilMapper.convertBPCEntityListToDtoList(dto))
                    .collect(Collectors.toList());


        } catch (Exception e) {
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_SAVE_ISSUE_HISTORY", e,
                    "failed at Service level");
        }
        List<FunctionalArea> finalResult = new ArrayList<>(10);
        List<FunctionalArea> bpcResult = new ArrayList<>(10);
        if(dtoList != null)
            for (Bpc bpc: dtoList) {
                List<BusinessProcess> businessProcess = bpc.getBusinessProcess();
                List<FunctionalArea> bpcs = new ArrayList<>(10);
                for(BusinessProcess bp : businessProcess) {
                    List<BusinessProcessCategory> businessProcessCategory = bp.getBusinessProcessCategory();
                    List<FunctionalArea> bps = new ArrayList<>(10);
                    for (BusinessProcessCategory category : businessProcessCategory) {
                        FunctionalArea fa = new FunctionalArea();
                        fa.setName(category.getCategoryDetails());
                        fa.setCode(category.getCategoryCode() + ":" + category.getCategoryDetails());
                        bps.add(fa);
                    }
                    FunctionalArea fa = new FunctionalArea();
                    fa.setName(bp.getProcessDetails());
                    fa.setCode(bp.getProcessCode());
                    fa.setChildren(bps);
                    bpcs.add(fa);
                }
                FunctionalArea fa = new FunctionalArea();
                fa.setName(bpc.getBpcDetails());
                fa.setCode(bpc.getBpcCode());
                fa.setChildren(bpcs);
                bpcResult.add(fa);
            }
        FunctionalArea paypal = new FunctionalArea();
        paypal.setName("PayPal");
        paypal.setCode("PayPal");
        paypal.setChildren(bpcResult);
        finalResult.add(paypal);

        CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
        return Response.status(Response.Status.OK).entity(finalResult).build();
    }
    /**
	 * This method getProducts is overrided this performs some operations and 
	 * This method has a cachable annotation so that it returns cached
	 * issue Response from master table
	 * 
	 * @exception Business exception There are some chance for this exception
	 *  
	 * @return Response This method returns the Response object from the master table
	 */
    @Override
    @Cacheable("grc")
	public Response getProducts(){
		String corr_id = CalTransactionUtil.getCorrelationId();
		List<GrcIssueProduct> productEntityList=new ArrayList<GrcIssueProduct>();
		List<IssueProduct> productDtoList =new ArrayList<IssueProduct>();
			try{
				productEntityList=issueImpl.getProducts();	
				productDtoList = productEntityList.stream().map(productDto -> issueUtilMapper.convertProductEntityListToDtoList(productDto))
                        .collect(Collectors.toList());
			}
			catch(Exception e){
				CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
				ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_SAVE_ISSUE_HISTORY", e,
						"failed at Service level");
			}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(productDtoList).build();
	}
    /**
	 * This method getAreaOfLaw is overrided this performs some operations and 
	 * This method has a cachable annotation so that it returns cached
	 * returns issue Response from master table
	 * 
	 * @exception Business exception There are some chance for this exception
	 *  
	 * 
	 * @author prasubramanian
	 * @return Response This method returns the Response object from the master table
	 */
	@Override
	@Cacheable("grc")
	public Response getAreaOfLaw() {
		String corr_id = CalTransactionUtil.getCorrelationId();
		List<Object>  entityList = null;
		try {
			String query = "SELECT p FROM GrcIssueAreaOfLaw p";
			entityList=issueImpl.getLookUps(query);					
		} catch (Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_SAVE_ISSUE_HISTORY", e,
					"failed at Service level");
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(entityList).build();		
	}
	/**
	 * This method getSubAreaOfLaw is overrided this performs some operations and 
	 * returns issue Response from master table
	 * 
	 * @param aolCode this is the only parameter used here and this is of type long
	 * @exception Business exception There are some chance for this exception
	 *  
	 * @return Response This method returns the Response object from the master table
	 */
	@Override
	public Response getSubAreaOfLaw(Long aolCode) {
		String corr_id = CalTransactionUtil.getCorrelationId();
		logger.info(" Fetch SubAreaOfLaw for aolCode " + aolCode);
		List<Object>  entityList = null;
		try {
			String query = "SELECT p FROM GrcIssueSubAreaOfLaw p where p.aolCode = "+aolCode;
			entityList=issueImpl.getLookUps(query);					
		} catch (Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_SAVE_ISSUE_HISTORY", e,
					"failed at Service level");
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(entityList).build();		
	}
	/**
	 * This method addComment is overrided this performs some operations and 
	 * returns issue Response from master table
	 * 
	 * @param comments This is the first parameter of type CommentCode 
	 * @param id This is the Second parameter of type String
	 * 
	 * @exception Business exception There are some chance for this exception
	 *  
	 * @return Response This method returns the Response object from the master table
	 */
    @Override
    public Response addComment(CommentCode comments, String id) {
        String corr_id = CalTransactionUtil.getCorrelationId();

        comments.setIssuesIssueId(id);

        try {
            commentImpl.save(comments);
        } catch (Exception e) {
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_ADD_COMMENT_CHECK",
                    e,"failed at Service level");
        }
        CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);

        return Response.status(Response.Status.CREATED).build();
    }
    /**
	 * This method getCommentsBasedOnIssueId is overrided this performs some operations and 
	 * returns issue Response from master table
	 * 
	 * @param issueId This is the only parameter of type String passed over here 
	 * @exception Business exception There are some chance for this exception
	 *  
	 * @return Response This method returns the Response object from the master table
	 */
    @Override
    public Response getCommentsBasedOnIssueId(String issueId) {
        String corr_id = CalTransactionUtil.getCorrelationId();

        List<CommentCode> returnValue = null;
        try {
        	String active = IssueConstants.ACTIVE;
        	String version = IssueConstants.DEFAULT_VERSION;
            List<GrcIssueComments> result = commentImpl.findAllComments(issueId, active, version);
            returnValue = result.stream()
                    .map(i -> commentUtilMapper.convertDBToRequest(i))
                    .collect(Collectors.toList());

            CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
        } catch (Exception e) {
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_ALL_COMMENTS_CHECK",
                    e,"failed at Service level");
        }
        return Response.status(Response.Status.OK).entity(returnValue).build();
    }
    /**
	 * This method getLevelOfEffort is overrided this performs some operations and 
	 * returns issue Response from master table
	 * 
	 * @exception Business exception There are some chance for this exception
	 *  
	 * 
	 * @author prasubramanian
	 * @return Response This method returns the Response object from the master table
	 */
    @Override
    public Response updateComment(CommentCode comment, String id) {
        String corr_id = CalTransactionUtil.getCorrelationId();

        comment.setIssuesIssueId(id);

        try {
            commentImpl.update(comment);
        } catch (Exception e) {
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_UPDATE_COMMENT_CHECK",
                    e,"failed at Service level");
        }
        CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);

        return Response.status(Response.Status.CREATED).build();
    }
/**
 * This overrided method getPossibleActions gets value through the parameter and
 *  returns a response object from the master table
 * 
 * @param id  This is the only parameter of type String 
 * 
 * @exception BusinessException This is the exception which happens here
 * 
 * @return Response  This method returns the response object from the master table 
 */
	@Override
	public Response getPossibleActions(String id) {
		
		List<Lifecycleaction> lca = null;
        String corr_id = CalTransactionUtil.getCorrelationId();
        try{
			lca = lifecycleMgmtService.getLifecycleActions(issueUtils.constructExecutionContext(id, null));
	        CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
			
        }catch(Exception e){
            logger.error("Get Possible Actions Failed",e);
            logger.error("Failed at Service level",e);
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_POSSIBLE_ACTIONS_CHECK",e,"failed at Service level");
        }
        return Response.status(Status.OK).entity(lca).build();
	}
	/**
	 * This overrided method executeAction gets value through the parameter and
	 *  returns a response object from the master table
	 * 
	 * @param id  This is the First parameter of type String 
	 * @param action This is the Second parameter of type Lifecycleaction
	 * @param uriInfo This is the third parameter of type UriInfo
	 * 
	 * @exception BusinessException This is the exception which happens here
	 * 
	 * @return Response  This method returns the response object from the master table 
	 */
	@Override
	public Response executeAction(String id, Lifecycleaction action,UriInfo uriInfo) {
		LifecycleResponseBO responseBO = null;
        String corr_id = CalTransactionUtil.getCorrelationId();
        try{		
        	ExecutionContext exc = issueUtils.constructExecutionContext(id, action);
        	exc.setUriInfo(uriInfo);
			responseBO = lifecycleMgmtService.execute(exc);
			responseBO.setId(id);
	        CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);			
			
        }catch(Exception e){
            logger.error("Execute Action Failed",e);
            logger.error("Failed at Service level",e);
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_EXECUTE_ACTION_CHECK",e,"failed at Service level");
        }
        return Response.status(Status.OK).entity(responseBO).build();
	}
	/**
	 * This method executeAction gets value through the parameter and
	 *  returns a response object from the master table
	 * 
	 * @param id  This is the only parameter of type GrcIsssues 
	 * 
	 * @exception BusinessException This is the exception which happens here
	 * 
	 * @return Response  This method returns the response object from the master table 
	 */
	public Response createAuditLog(GrcIssues issue) {
        String corr_id = CalTransactionUtil.getCorrelationId();

        try {
            issueImpl.save(issue);
        } catch (Exception e) {
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_CREATE_ISSUE_CHECK",
                    e,"failed at Service level");
        }
        CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);

		return Response.status(Response.Status.CREATED).build();
	}
	/**
	 * This overrided  method getAuditLog gets value through the parameter and
	 *  returns a response object from the master table
	 * 
	 * @param issueCode  This is the only parameter of type String
	 * 
	 * @exception BusinessException This is the exception which happens here
	 * 
	 * @return Response  This method returns the response object from the master table 
	 */

	@Override
	public Response getAuditLog(String issueCode) {
		String corr_id = CalTransactionUtil.getCorrelationId();

		List<IssuesAuditLog> auditLogs = null;
        try {
        	String active = IssueConstants.ACTIVE;
            List<GrcIssueAuditLog> findIssueAuditLogByIssueCode = issueImpl.findIssueAuditLogByIssueCode(issueCode, active);
            auditLogs = issueUtilMapper.convertDBTORequest(findIssueAuditLogByIssueCode);
        } catch (Exception e) {
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_CREATE_ISSUE_CHECK",
                    e,"failed at Service level");
        }
        CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);

		return Response.status(Response.Status.OK).entity(auditLogs).build();
	}
	/**
	 * This overrided  method getLinkedIssues gets value through the parameter and
	 *  returns a response object from the master table
	 * 
	 * @param issueCode  This is the only parameter of type String
	 * 
	 * @exception BusinessException This is the exception which happens here
	 * 
	 * @return Response  This method returns the response object from the master table 
	 */

	@Override
	public Response getLinkedIssues(String issueCode){
		String corr_id = CalTransactionUtil.getCorrelationId();

        Map<String, Object> returnValue = new HashMap<>();
        try {
            List<Object> result = issueImpl.findAllLinkedIssues(issueCode);
            
            List<Object> issueEntry = new ArrayList<>();
            for (Object obj: result){
            	Object[] objs = (Object[]) obj;
            	Map<String,Object> issueMap = new HashMap<>();
            	issueMap.put("issue_code", objs[0]);
            	issueMap.put("issue_name", objs[1]);
            	issueEntry.add(issueMap);
            }
            returnValue.put("linked_issues", issueEntry);
            CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
        } catch (Exception e) {
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_ALL_ISSUES_CHECK",
                    e,"failed at Service level");
        }
        return Response.status(Response.Status.OK).entity(returnValue).build();
	}

	/**
	 * This overrided  method getCountry gets value through the parameter and
	 *  returns a response object from the master table
	 * 
	 * @param regionCode  This is the only parameter of type String
	 * 
	 * @exception BusinessException This is the exception which happens here
	 * 
	 * @return Response  This method returns the response object from the master table 
	 */


	@Override
	public Response getCountry(Long regionCode) {
		String corr_id = CalTransactionUtil.getCorrelationId();
		logger.info(" Fetch SubAreaOfLaw for aolCode " + regionCode);
		List<Object>  entityList = null;
		try {
			String query = "SELECT p FROM GrcIssueCountry p where p.regionCode = "+regionCode;
			entityList=issueImpl.getLookUps(query);					
		} catch (Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_SAVE_ISSUE_HISTORY", e,
					"failed at Service level");
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(entityList).build();	
	}
	/**
	 * This overrided  method getState gets value through the parameter and
	 * This has a cachable annotation so it 
	 *  returns a cached response object from the master table
	 * 
	 * @param CountryCode  This is the only parameter of type long
	 * 
	 * @exception BusinessException This is the exception which happens here
	 * 
	 * @return Response  This method returns the response object from the master table 
	 */

	@Override
	@Cacheable("grc")
	public Response getState(Long countryCode) {
		String corr_id = CalTransactionUtil.getCorrelationId();
		logger.info(" Fetch SubAreaOfLaw for aolCode " + countryCode);
		List<Object>  entityList = null;
		try {
			String query = "SELECT p FROM GrcIssueState p where p.countryCode = "+countryCode;
			entityList=issueImpl.getLookUps(query);					
		} catch (Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_SAVE_ISSUE_HISTORY", e,
					"failed at Service level");
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(entityList).build();	
	}
	/**
	 * This overrided  method getprovinces gets value through the parameter and
	 * This has a cachable annotation so it 
	 *  returns a cached response object from the master table
	 * 
	 * @param stateCode  This is the only parameter of type long
	 * 
	 * @exception BusinessException This is the exception which happens here
	 * 
	 * @return Response  This method returns the response object from the master table 
	 */

	@Override
	@Cacheable("grc")
	public Response getProvinces(Long stateCode) {
		String corr_id = CalTransactionUtil.getCorrelationId();
		logger.info(" Fetch SubAreaOfLaw for aolCode " + stateCode);
		List<Object>  entityList = null;
		try {
			String query = "SELECT p FROM GrcIssueProvince p where p.stateCode = "+stateCode;
			entityList=issueImpl.getLookUps(query);					
		} catch (Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_SAVE_ISSUE_HISTORY", e,
					"failed at Service level");
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(entityList).build();	
	}
	/**
	 * This overrided  method saveCustomFilter gets value through the parameter and
	 * This has a cachable annotation so it 
	 *  returns a cached response object from the master table
	 * 
	 * @param CustomFilter  This is the only parameter of type GrcCustomFilter
	 * 
	 * @exception BusinessException This is the exception which happens here
	 * 
	 * @return Response  This method returns the response object from the master table 
	 */

    @Override
    public Response saveCustomFilter(GrcCustomFilter customFilter) {
        String corr_id = CalTransactionUtil.getCorrelationId();

        try {
            customFilterImpl.save(customFilter);
        } catch (Exception e) {
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_CREATE_CUSTOM_FILTER_CHECK", e,
                    "failed at Service level");
        }
        CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);

        return Response.status(Response.Status.CREATED).build();
    }
    /**
	 * This overrided  method  fetchCustomFilterByUserName gets value through the parameter and
	 *  returns a response object from the master table
	 * 
	 * @param userName  This is the first parameter of type String
	 * @param formatted 	This is the second parameter of type string
	 * 
	 * @throws BusinessException This is the exception which happens here
	 * 
	 * @return Response  This method returns the response object from the master table 
	 */
    @Override
    public Response fetchCustomFilterByUserName(String userName, String formatted) {
        String corr_id = CalTransactionUtil.getCorrelationId();

        List<GrcCustomFilter> result = null;

        try {
            result = customFilterImpl.fetchAllCustomFilterByUserName(userName);
        } catch (Exception e) {
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_CREATE_CUSTOM_FILTER_CHECK", e,
                    "failed at Service level");
        }
        CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);

        if(formatted.equalsIgnoreCase("true")) {
            for (GrcCustomFilter filter : result) {
                List<CustomFilterRule> rules = filter.getCustomFilterRule();
                StringBuffer formattedValue = new StringBuffer();
                for (CustomFilterRule rule : rules) {
                    if(rule.getName() != null) {
                        formattedValue.append("{ " + rule.getName() + "-");
                    }
                    if(rule.getRule() != null) {
                        formattedValue.append(rule.getRule() + "-");
                    }
                    if(rule.getValue() != null) {
                        formattedValue.append(rule.getValue() + " }");
                    }
                    if(rule.getCondition() != null) {
                        formattedValue.append(" ["+rule.getCondition()+"] ");
                    }
                }
                filter.setCreatedBy(formattedValue.toString());
            }
        }

        return Response.status(Response.Status.OK).entity(result).build();
    }
    /**
  	 * This overrided  method  getAllIssues gets value through the parameter and
  	 *  returns a response object from the master table
  	 * 
  	 * @param userName  This is the first parameter of type String
  	 * @param userType 	This is the second parameter of type string
  	 * @param fa 	This is the third parameter of type String
  	 * @param status This is the fourth parameter of type String
  	 * 
  	 * @throws BusinessException, NullPointerException This is the exception which happens here
  	 * 
  	 * @return Response  This method returns the response object from the master table 
  	 */
	@Override
	public Response getAllIssues(String userName,String userType, String fa, String status) {
		String corr_id = CalTransactionUtil.getCorrelationId();
		final String TABLE_DATA = "TABLE_DATA";
		final String TABLE_COUNT = "TABLE_COUNT";
		GrcIssueSummary grcIssueSummary = null;
		List<IssueDetailList> returnValue = null;
		Map<String, List<Object[]>> result = null;
        try {
        	grcIssueSummary = new GrcIssueSummary();
            result = issueImpl.getAllIssues(userName, userType, fa, status);
            
            if (result != null) {
            	List<Object[]> list = result.get(TABLE_DATA);
				if (list != null && list.size() > 0) {
	                returnValue = list.stream()
	                        .map(i -> issueUtilMapper.convertDBTORequestForDB(i))
	                        .collect(Collectors.toList());
	                returnValue = returnValue.stream() 
	                        .map(i -> issueUtilMapper.convertDBTORequest(userName, i))
	                        .collect(Collectors.toList());
	                
	                grcIssueSummary.setIssueDetailList(returnValue);					
				}
                
                if(!result.get(TABLE_COUNT).isEmpty()) {
            		Object[] countObject= result.get("TABLE_COUNT").get(0);
            		grcIssueSummary.setTotalIssuesCount(countObject[0].toString());
            		grcIssueSummary.setTotalPastDue(countObject[1].toString());
            		grcIssueSummary.setTotalComingDue(countObject[2].toString());
            		grcIssueSummary.setTotalNotApproved(countObject[3].toString());        		
            	}
            }

            CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
        } catch (Exception e) {
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_ALL_ISSUES_FOR_DB_CHECK",
                    e,"failed at Service level");
        }
        return Response.status(Response.Status.OK).entity(grcIssueSummary).build(); 
	}
	 /**
  	 * This overrided  method  getUserFa gets value through the parameter and
  	 *  returns a response object from the master table
  	 * 
  	 * @param userName  This is the only parameter of type String
  	 *
  	 * @throws BusinessException, NullPointerException This is the exception which happens here
  	 * 
  	 * @return Response  This method returns the response object from the master table 
  	 */

	@Override
	public Response getUserFa(String userName) {
		String corr_id = CalTransactionUtil.getCorrelationId();
		String myFa=new String("");
		List<String> userFa=new ArrayList<String>();
		try {
			myFa = issueImpl.getUserFa(userName);
			userFa.add(myFa);
			CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		} catch (Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_ALL_ISSUES_CHECK", e,
					"failed at Service level");
		}
		return Response.status(Response.Status.OK).entity(userFa).build();
	}
	 /**
  	 * This   method  getUserFa is overrided
  	 * This has a cachable annotation it 
  	 *  returns a cached response object from the master table
 
   	 * @throws BusinessException, NullPointerException This is the exception which happens here
  	 * 
  	 * @return Response  This method returns the response object from the master table 
  	 */


    @Override
    @Cacheable("grc")
    public Response getProductsHierarchy() {
        String corr_id = CalTransactionUtil.getCorrelationId();
        List<GrcIssueProduct> productEntityList=new ArrayList<GrcIssueProduct>();
        List<IssueProduct> productDtoList =new ArrayList<IssueProduct>();
        List<FunctionalArea> productList = new ArrayList<>(20);

        List<FunctionalArea> finalResult = new ArrayList<>(10);

        try{
            productEntityList=issueImpl.getProducts();
            productDtoList = productEntityList.stream().map(
                    productDto -> issueUtilMapper.convertProductEntityListToDtoList(productDto))
                    .collect(Collectors.toList());

            for (IssueProduct product : productDtoList) {
                List<GrcCapabilities> capabilities = product.getCapabilities();
                List<FunctionalArea> capabilityList = new ArrayList<>(20);
                for(GrcCapabilities capability : capabilities) {
                    List<Feature> features = capability.getFeature();
                    List<FunctionalArea> featureList = new ArrayList<>(20);
                    for (Feature feature : features) {
                        FunctionalArea fa = new FunctionalArea();
                        fa.setCode(feature.getFeatureCode()+":"+feature.getFeatureDetails());
                        fa.setName(feature.getFeatureDetails());
                        fa.setDescription("feature");
                        featureList.add(fa);
                    }
                    FunctionalArea cb = new FunctionalArea();
                    cb.setCode(capability.getCapabilityCode()+":capability");
                    cb.setName(capability.getCapabilityDetails());
                    cb.setDescription("capability");
                    cb.setChildren(featureList);
                    capabilityList.add(cb);
                }
                FunctionalArea pd = new FunctionalArea();
                pd.setCode(product.getProductCode()+":product");
                pd.setName(product.getProductDetails());
                pd.setDescription("product");
                pd.setChildren(capabilityList);
                productList.add(pd);
            }
            FunctionalArea paypal = new FunctionalArea();
            paypal.setCode("PAYPAL");
            paypal.setName("PayPal");
            paypal.setChildren(productList);
            finalResult.add(paypal);
        }
        catch(Exception e){
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_SAVE_ISSUE_HISTORY", e,
                    "failed at Service level");
        }
        CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
        return Response.status(Response.Status.OK).entity(finalResult).build();
    }

    /**
  	 * This  method getRegionHierarchy is overrided 
  	 * This has a cachable annotation it 
  	 *  returns a cached response object from the master table
 
   	 * @throws BusinessException	 This is the exception which happens here
  	 * 
  	 * @return Response  This method returns the response object from the master table 
  	 */

    @Override
    @Cacheable("grc")
    public Response getRegionHierarchy() {
        String corr_id = CalTransactionUtil.getCorrelationId();
        List<GrcIssueRegion>  entityList = null;
        try {
            entityList=issueImpl.getRegionHierarchy();
        } catch (Exception e) {
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_SAVE_ISSUE_HISTORY", e,
                    "failed at Service level");
        }

        List<FunctionalArea> regionResult = new ArrayList<>(10);

        for (GrcIssueRegion region : entityList) {

            List<FunctionalArea> countriesResult = new ArrayList<>(10);
            Set<GrcIssueCountry> countries =  region.getCountryCode();

            for (GrcIssueCountry country : countries) {
                List<FunctionalArea> stateResult = new ArrayList<>(10);
                Set<GrcIssueState> states = country.getStateCode();

                for (GrcIssueState state : states) {
                    List<FunctionalArea> provincesResult = new ArrayList<>(10);
                    Set<GrcIssueProvince> provinces =  state.getProvinceCode();

                    for (GrcIssueProvince province: provinces) {
                        FunctionalArea pro = new FunctionalArea();
                        pro.setCode(String.valueOf(province.getProvinceCode())+":"+province.getProvinceName());
                        pro.setName(province.getProvinceName());
                        provincesResult.add(pro);
                    }
                    FunctionalArea sta = new FunctionalArea();
                    sta.setCode(String.valueOf(state.getStateCode()));
                    sta.setName(state.getStateName());
                    sta.setChildren(provincesResult);
                    stateResult.add(sta);
                }
                FunctionalArea coun = new FunctionalArea();
                coun.setCode(String.valueOf(country.getCountryCode()));
                coun.setName(country.getCountryName());
                coun.setChildren(stateResult);
                countriesResult.add(coun);
            }
            FunctionalArea re = new FunctionalArea();
            re.setName(region.getRegionName());
            re.setCode(String.valueOf(region.getRegionCode()));
            re.setChildren(countriesResult);
            regionResult.add(re);
        }

        List<FunctionalArea> finalResult = new ArrayList<>(2);
        FunctionalArea paypal = new FunctionalArea();
        paypal.setName("PayPal");
        paypal.setCode("PayPal");
        paypal.setChildren(regionResult);
        finalResult.add(paypal);

        CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
        return Response.status(Response.Status.OK).entity(finalResult).build();
    }
    /**
   	 * This  method getSubRootCause is overrided this gets value through parameters,
   	 * This has a cachable annotation it 
   	 *  returns a cached response object from the master table
   	 *  
   	 * @param rootCause This is the only parameter of type String
   	 *  
   	 * @throws BusinessException	 This is the exception which happens here
   	 * 
   	 * @return Response  This method returns the cached  response object from the master table 
   	 */
 
	@Override
	@Cacheable("grc")
	public Response getSubRootCause(String rootCause) {
		HashMap<String, List<Object>> jsonRes = new HashMap<>();
		String corr_id = CalTransactionUtil.getCorrelationId();
		try {
			StringBuffer query = new StringBuffer("SELECT p FROM GrcRootCauseCategory p where p.rtcParent='"+rootCause+"'");
			List<Object> entityList = issueMgmt.getLookUps(query.toString(),rootCause);
			jsonRes.put("items",entityList);
            CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		} catch(Exception e){
            logger.error("Get Issue Root Cause Category Failed",e);
            CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
            ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_RISK_TYPE_CHECK",e,"Get Root Cause Category Failed");
        }
		return Response.status(Response.Status.OK).entity(jsonRes).build();
	}
	 /**
   	 * This  method getRiskCategory is overrided this gets value through parameters,
   	 * This has a cachable annotation it so it
   	 *  returns a cached response object from the master table
   	 *  
   	 * @param riskCategory This is the only parameter of type String
   	 *  
   	 * @throws BusinessException	 This is the exception which happens here
   	 * 
   	 * @return Response  This method returns the cached  response object from the master table 
   	 */
	@Override
	@Cacheable("grc")
	public Response getRiskCategory(String riskCategory) {
		HashMap<String,List<Object>> jsonRes = new HashMap<>();
		String corr_id = CalTransactionUtil.getCorrelationId();
		logger.info(" Fetch getRiskCategory for riskCategory " + riskCategory);
		List<Object>  entityList = null;
		try {
			String query = "SELECT p FROM GrcRiskType p where p.riskParent = "+riskCategory;
			entityList=issueImpl.getLookUps(query);		
			jsonRes.put("items", entityList);
		} catch (Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_SAVE_ISSUE_HISTORY", e,
					"failed at Service level");
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(jsonRes).build();	
	}
	 /**
   	 * This  method getRiskCategory is overrided and overloaded this gets value through parameters,
   	 * This has a cachable annotation it so it
   	 *  returns a cached response object from the master table
   	 *  
   	 * @param riskCategory This is the first parameter of type String
   	 * @param riskType 	This is the second parameter of type string
   	 *  
   	 * @throws BusinessException	 This is the exception which happens here
   	 * 
   	 * @return Response  This method returns the cached  response object from the master table 
   	 */
	@Override
	@Cacheable("grc")
	public Response getRiskCategory(String riskCategroy, String riskType) {
		HashMap<String,List<Object>> jsonRes = new HashMap<>();
		String corr_id = CalTransactionUtil.getCorrelationId();
		logger.info(" Fetch getRiskCategory for riskCategroy " + riskCategroy);
		List<Object>  entityList = null;
		try {			
			String query = "SELECT p FROM GrcRiskType p where p.riskParent in "
								+ "(SELECT q FROM GrcRiskType q where q.riskParent = "+riskCategroy+") and p.riskParent =  " + riskType;
			
			entityList=issueImpl.getLookUps(query);	
			jsonRes.put("items", entityList);
		} catch (Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_SAVE_ISSUE_HISTORY", e,
					"failed at Service level");
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(jsonRes).build();	
	}
	 /**
   	 * This  method getExceptions is overrided this gets value through parameters,
   	 *  returns a  response object from the master table
   	 *  
   	 * @param issueId This is the only parameter of type String
   	 *  
   	 * @throws BusinessException	 This is the exception which happens here
   	 * 
   	 * @return Response  This method returns the  response object from the master table 
   	 */
	@Override
	public Response getExceptions(String issueId) {
		if(issueId == null){
			return Response.status(Response.Status.BAD_REQUEST).entity("No Issue ID was found").build();
		}
		List<GrcIssueException> grcExceptions = null;
		List<IssueException> issueExceptions = null;
		String corr_id = CalTransactionUtil.getCorrelationId();
		try{
			grcExceptions = issueImpl.getExceptions(issueId);
			issueExceptions = grcExceptions.stream()
					.map(grcException -> issueUtilMapper.convertExceptionEntityToRequest(grcException))
					.collect(Collectors.toList());
		}
		catch(Exception e){
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_ALL_EXCEPTIONS", e,
					"failed at Service level");
		}		
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(issueExceptions).build();
	}
	 /**
   	 * This  method getExceptions is overrided and overloaded this gets value through parameters,
   	 *  returns a  response object from the master table
   	 *  
   	 * @param issueId This is the first parameter of type String
   	 * @param exceptionId This is the second parameter of type String
   	 * 
   	 *  
   	 * @throws BusinessException	 This is the exception which happens here
   	 * 
   	 * @return Response  This method returns the  response object from the master table 
   	 */

	@Override
	public Response getException(String issueId, String exceptionId) {
		if(issueId == null || exceptionId == null){
			return Response.status(Response.Status.BAD_REQUEST).entity("Both Issue ID and Exception ID are required").build();
		}
		GrcIssueException grcException = null;
		IssueException issueException = null;
		String corr_id = CalTransactionUtil.getCorrelationId();
		try{
			grcException = issueImpl.getException(issueId, exceptionId);
			issueException = issueUtilMapper.convertExceptionEntityToRequest(grcException);
		}
		catch(Exception e){
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_GET_EXCEPTION", e,
					"failed at Service level");
		}		
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(issueException).build();
	}
	 /**
   	 * This  method approvalRequiredFields is overrided this gets value through parameters,
   	 *  returns a  response object from the master table
   	 *  
   	 * @param id This is the first parameter of type String
   	 *  @param fieldProps this is the secondParameter of type IssueFieldUpdates
   	 *  
   	 * @throws BusinessException	 This is the exception which happens here
   	 * 
   	 * @return Response  This method returns the  response object from the master table 
   	 */
	@Override
	public Response approvalRequiredFields(String id, IssueFieldUpdates fieldProps) {
		if(id == null){
			return Response.status(Response.Status.BAD_REQUEST).entity("No Issue ID was found").build();
		}
		String corr_id = CalTransactionUtil.getCorrelationId();
		try{
			if(fieldProps.getFields()!=null && !fieldProps.getFields().isEmpty())
				issueImpl.updateFieldUpdate(id, fieldProps.getType(), fieldProps.getFields());
		}
		catch(Exception e){
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_POST_ALL_FIELDS", e,
					"failed at Service level");
		}		
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(fieldProps).build();
	}
	 /**
   	 * This  method approvedFields is overrided this gets value through parameters,
   	 *  returns a  response object from the master table
   	 *  
   	 * @param id This is the first parameter of type String
   	 *  @param fieldProps this is the secondParameter which is a list of type IssueFieldUpdates
   	 *  
   	 * @throws BusinessException	 This is the exception which happens here
   	 * 
   	 * @return Response  This method returns the  response object from the master table 
   	 */
	
	@Override
	public Response approvedFields(String id, List<IssuesFieldUpdate> fieldProps) {
		if(id == null){
			return Response.status(Response.Status.BAD_REQUEST).entity("No Issue ID was found").build();
		}
		String corr_id = CalTransactionUtil.getCorrelationId();
		try{
			issueImpl.updateApprovedFields(id, fieldProps);
		}
		catch(Exception e){
			e.printStackTrace();
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_POST_ALL_FIELDS", e,
					"failed at Service level");
		}		
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(fieldProps).build();
	}
	 /**
   	 * This  method checkIfUserHasBothApprovalAndExceptionalFields
   	 *  is overrided this gets value through parameters,
   	 *  returns a  response object from the master table
   	 *  
   	 * @param issueId This is the only parameter of type String
   	 
   	 * @throws BusinessException	 This is the exception which happens here
   	 * 
   	 * @return Response  This method returns the  response object from the master table 
   	 */
	@Override
	public Response checkIfUserHasBothApprovalAndExceptionalFields(String issueId) {
		String corr_id = CalTransactionUtil.getCorrelationId();
		boolean rValue = false;
		try {
			rValue = issueImpl.checkIfUserHasBothApprovalAndExceptionalFields(issueId);
		}
		catch(Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_CHECK_CONFLICT", e,
					"failed at Service level");
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(rValue).build();
	}
	 /**
   	 * This  method createExceptions is overrided this gets value through parameters,
   	 *  returns a  response object from the master table
   	 *  
   	 * @param issueId This is the only parameter of type String
   	 
   	 * @throws BusinessException	 This is the exception which happens here
   	 * 
   	 * @return Response  This method returns the  response object from the master table 
   	 */

	@Override
	
	public Response createExceptions(String issueId) {
		String corr_id = CalTransactionUtil.getCorrelationId();
		List<GrcIssueException> grcExceptions = null;
		List<IssueException> issueExceptions = null;
		try {
			issueImpl.createExceptions(issueId);
			grcExceptions = issueImpl.getExceptions(issueId);
			issueExceptions = grcExceptions.stream()
					.map(grcException -> issueUtilMapper.convertExceptionEntityToRequest(grcException))
					.collect(Collectors.toList());
		}
		catch(Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_CREATE_EXCEPTIONS", e,
					"failed at Service level");
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(issueExceptions).build();
	}
	 /**
   	 * This  method hasExceptionFlowFields is overrided this gets value through parameters,
   	 *  returns a  response object from the master table
   	 *  
   	 * @param issueId This is the only parameter of type String
   	 
   	 * @throws BusinessException	 This is the exception which happens here
   	 * 
   	 * @return Response  This method returns the  response object from the master table 
   	 */

	@Override
	public Response hasExceptionFlowFields(String issueId) {
		String corr_id = CalTransactionUtil.getCorrelationId();
		boolean rValue = false;
		try {
			rValue = issueImpl.checkIfUserExceptions(issueId);
		}
		catch(Exception e) {
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_CHECK_EXCEPTIONS", e,
					"failed at Service level");
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).entity(rValue).build();
	}
	
	/**
   	 * This  method deleteExceptions is overrided this gets value through parameters,
   	 *  returns a  response object from the master table
   	 *  
   	 * @param issueId This is the only parameter of type String
   	 
   	 * @throws BusinessException	 This is the exception which happens here
   	 * 
   	 * @return Response  This method returns the  response object from the master table 
   	 */

	@Override
	public Response deleteExceptions(String issueId) {
		String corr_id = CalTransactionUtil.getCorrelationId();
		try{
			issueImpl.deleteExceptions(issueId);
		}
		catch(Exception e){
			CalUtil.logRootCalStatus(corr_id, CalStatus.EXCEPTION);
			ExceptionUtil.logAndThrowBusinessException("SERVICE_ERROR_DELETE_EXCEPTIONS", e,
					"failed at Service level");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		CalUtil.logRootCalStatus(corr_id, CalStatus.SUCCESS);
		return Response.status(Response.Status.OK).build();
	}
}
