package com.paypal.erc.grcprocessserv.repositories.mappers.issue;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.paypal.api.platform.erc.GrcUser;
import com.paypal.api.platform.erc.grc.AreaOfLaw;
import com.paypal.api.platform.erc.grc.Bpc;
import com.paypal.api.platform.erc.grc.BusinessProcess;
import com.paypal.api.platform.erc.grc.BusinessProcessCategory;
import com.paypal.api.platform.erc.grc.CountryState;
import com.paypal.api.platform.erc.grc.Document_;
import com.paypal.api.platform.erc.grc.ErcdbaGrcFunctionalArea;
import com.paypal.api.platform.erc.grc.Feature;
import com.paypal.api.platform.erc.grc.GrcCapabilities;
import com.paypal.api.platform.erc.grc.GrcCountry;
import com.paypal.api.platform.erc.grc.GrcIssues;
import com.paypal.api.platform.erc.grc.InformedStakeholder;
import com.paypal.api.platform.erc.grc.IssueDetailList;
import com.paypal.api.platform.erc.grc.IssueException;
import com.paypal.api.platform.erc.grc.IssueHealth;
import com.paypal.api.platform.erc.grc.IssueHistory;
import com.paypal.api.platform.erc.grc.IssueLevelOfEffort;
import com.paypal.api.platform.erc.grc.IssueProduct;
import com.paypal.api.platform.erc.grc.IssueProgress;
import com.paypal.api.platform.erc.grc.IssueRating;
import com.paypal.api.platform.erc.grc.IssueRegion;
import com.paypal.api.platform.erc.grc.IssueResponse;
import com.paypal.api.platform.erc.grc.IssueReviewFrequency;
import com.paypal.api.platform.erc.grc.IssueSourceType;
import com.paypal.api.platform.erc.grc.IssuesAuditLog;
import com.paypal.api.platform.erc.grc.IssuesFieldUpdate;
import com.paypal.api.platform.erc.grc.LinkedIssue;
import com.paypal.api.platform.erc.grc.PrimaryIssueSource;
import com.paypal.api.platform.erc.grc.RiskCategory;
import com.paypal.api.platform.erc.grc.RootCauseCategory;
import com.paypal.api.platform.erc.grc.SecondaryIssueSource;
import com.paypal.api.platform.erc.grc.StateProvince;
import com.paypal.api.platform.erc.grc.Status;
import com.paypal.api.platform.erc.grc.SubAol;
import com.paypal.erc.grc.common.dal.models.GrcDoc;
import com.paypal.erc.grc.common.dal.models.GrcDocHome;
import com.paypal.erc.grc.common.dal.models.GrcFunctionalArea;
import com.paypal.erc.grc.common.dal.models.GrcFunctionalAreaHome;
import com.paypal.erc.grc.common.dal.models.GrcUserHome;
import com.paypal.erc.grc.common.dal.models.GrcWfStatus;
import com.paypal.erc.grc.common.dal.models.GrcWfStatusHome;
import com.paypal.erc.grc.common.dal.models.issue.ComplaintID;
import com.paypal.erc.grc.common.dal.models.issue.DueDiligenceReviewName;
import com.paypal.erc.grc.common.dal.models.issue.ExternalAuditReviewName;
import com.paypal.erc.grc.common.dal.models.issue.GrcBpcBusiness;
import com.paypal.erc.grc.common.dal.models.issue.GrcBpcProcess;
import com.paypal.erc.grc.common.dal.models.issue.GrcBpcCategory;
import com.paypal.erc.grc.common.dal.models.issue.GrcFeatures;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssue;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueAolMap;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueAolMapHome;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueAreaOfLaw;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueAuditLog;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueBpcMap;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueCapabilities;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueCountry;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueDocMap;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueDocMapHome;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueDocuments;
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
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueProvince;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueRating;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueRegion;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueRegionMap;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueResponse;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueResponseMap;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueReviewFrequency;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueRiskMap;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueRiskMapHome;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueSource;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueSourceType;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueState;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueSubAreaOfLaw;
import com.paypal.erc.grc.common.dal.models.issue.GrcIssueUpdate;
import com.paypal.erc.grc.common.dal.models.issue.GrcRootCauseMap;
import com.paypal.erc.grc.common.dal.models.issue.GrcRootCauseMapHome;
import com.paypal.erc.grc.common.dal.models.issue.IncidentID;
import com.paypal.erc.grc.common.dal.models.issue.RegulatoryExamName;
import com.paypal.erc.grc.common.dal.models.issue.SLOD;
import com.paypal.erc.grc.common.dal.models.issue.SOXRName;
import com.paypal.erc.grc.common.dal.models.issue.SourceFunctionalArea;
import com.paypal.erc.grc.common.dal.models.issue.ThirdPartyReviewName;
import com.paypal.erc.grc.common.util.DateUtil;
import com.paypal.erc.grc.common.util.general.StringConstants;
import com.paypal.erc.grcprocessserv.repositories.constants.IssueConstants;
import com.paypal.erc.grcprocessserv.repositories.dao.issue.IssueBPCDao;
import com.paypal.erc.grcprocessserv.repositories.dao.issue.IssueDao;
import com.paypal.erc.grcprocessserv.repositories.dao.issue.IssueHealthDao;
import com.paypal.erc.grcprocessserv.repositories.dao.issue.IssueProductDao;
import com.paypal.erc.grcprocessserv.repositories.mappers.lookups.LookUpMapper;


@Component("IssueUtilMapper")
@Transactional(value="JpaTxnManager_erc" ,propagation = Propagation.REQUIRED)
public class IssueUtilMapper {
	

    @Autowired
    IssueDao issueDao;

    @Autowired
    GrcUserHome userDao;
    
    @Autowired
    GrcWfStatusHome wfStatusHome;
	
    @Autowired
    LookUpMapper lookUpMapper;

    @Autowired
    GrcFunctionalAreaHome faDao;
    
    @Autowired
    GrcIssueDocMapHome issueDocDao;
    
    @Autowired
    IssueHealthDao issueHealthDao;

	@Autowired
    GrcDocHome docHome;

	@Autowired
	GrcIssueAolMapHome aolMapDao;
	
	@Autowired
	GrcIssueInfoStakeholdersHome issueISHDao;
	
	@Autowired
	GrcIssueIfaMapHome issueIfaDao;

	@Autowired
	GrcIssueRiskMapHome riskCatDao;

	@Autowired
	GrcIssueLinkedMapHome linkedIssueDao;

	@Autowired
	GrcRootCauseMapHome rtcDao;
	
	@Autowired
	IssueBPCDao bpcDao;
	
	@Autowired
	IssueProductDao productDao;
	
	
	
	public GrcIssue convertRequestTODB(GrcIssues issue) throws Exception {
		MessageFormat messageFormat = new MessageFormat("ISS_{0}");
		GrcIssue grcIssue = new GrcIssue();
		String issueId = issue.getIssueCode();
		BigDecimal time = new BigDecimal(System.currentTimeMillis() / 1000);
		if (issue.getIssueCode() == null || issue.getIssueCode().trim().isEmpty()) {
        	BigDecimal sequence = issueDao.getNextVal();
        	issueId = messageFormat.format(new Object[] {String.format("%6s", sequence).replace(' ', '0')});
        }
        grcIssue.setIssueId(issueId);
        grcIssue.setIssueName(issue.getIssueName());
        grcIssue.setExecutiveSummary(issue.getExecutiveSummary());
        grcIssue.setIssueDescription(issue.getIssueDescription());
        grcIssue.setHasIFA(issue.getHasImpactedFa());
        grcIssue.setIsRepeated(issue.getIsIssueRepeat());
        grcIssue.setIssueOwner(issue.getIssueOwner().getUserName());
        grcIssue.setFunctionalAreaCode(issue.getFaCode().getFaCode());
        if (issue.getIssueOwnerDelegate()!=null)
        	grcIssue.setIssueOwnerDelegate(issue.getIssueOwnerDelegate().getUserName());
        if (issue.getIssueExecutiveOwner()!=null)
        	grcIssue.setIssueExecutiveOwner(issue.getIssueExecutiveOwner().getUserName());
        if (issue.getIssueApprover()!=null)
        grcIssue.setIssueApprover(issue.getIssueApprover().getUserName());
        if (issue.getIssueIcp()!=null)
        	grcIssue.setIssueIcp(issue.getIssueIcp().getUserName());
        if (issue.getBuSme()!=null)
        	grcIssue.setBuSme(issue.getBuSme().getUserName());
        if (issue.getIssueRco()!=null)
        	grcIssue.setIssueRco(issue.getIssueRco().getUserName());
        if (issue.getGgrcRcoLeader()!=null)
        	grcIssue.setGrcRcoLeader(issue.getGgrcRcoLeader().getUserName());
        grcIssue.setGrcWfStatus(wfStatusHome.findByStatusType(issue.getStatusCode().getName(), "ISSUE_MANAGEMENT"));
        grcIssue.setIsComplianceRelated(issue.getIsComplianceReleated());
        if(issue.getIsComplianceReleated().equals("1")){
            grcIssue.setIsConsumerImpact(issue.getIsConsumerImpact());
        } else if(issue.getIsComplianceReleated().equals("0")){
        	grcIssue.setIsConsumerImpact("0");
        }
        grcIssue.setIssueIdentifiedDate(new BigDecimal(DateUtil.convertToEpoch(issue.getIssueIdentifiedDate())));
        if(issue.getIsConsumerImpact() != null && !issue.getIsConsumerImpact().isEmpty() && grcIssue.getIsConsumerImpact().equals("1")){
        	grcIssue.setFiADueDate(new BigDecimal(DateUtil.convertToEpoch(issue.getFiaDueDate())));
            grcIssue.setFiaDescription(issue.getCfiaDescription());
            grcIssue.setFiaCompletedDate(new BigDecimal(DateUtil.convertToEpoch(issue.getFiaCompDate())));
            grcIssue.setCessationDueDate(new BigDecimal(DateUtil.convertToEpoch(issue.getCessationDueDate())));
            grcIssue.setCessationCompletedOnDate(new BigDecimal(DateUtil.convertToEpoch(issue.getCessationCompDate())));
        } else {
        	grcIssue.setFiADueDate(new BigDecimal("0"));
        	grcIssue.setFiaCompletedDate(new BigDecimal("0"));
        	grcIssue.setCessationDueDate(new BigDecimal("0"));
        	grcIssue.setCessationCompletedOnDate(new BigDecimal("0"));
        }
        grcIssue.setImpactAnalysis(issue.getImpactAnalysis());
        grcIssue.setRootCauseAnalysis(issue.getRootCauseAnalysis());
        grcIssue.setIssueRating(convertToGrcIssueRating(issue.getIssueRating()));
        grcIssue.setExternalIssueDetails(issue.getExtIssueRating());
        grcIssue.setRatingRationale(issue.getRatingRationale());
        grcIssue.setIssueProgressPercent(issue.getIssueProgressPercent());
        grcIssue.setIssueHealth(convertToGrcIssueHealth(issue.getIssueHealth()));
        grcIssue.setCreatedDate(time);
        grcIssue.setCreatedBy(issue.getCreatedBy());
        grcIssue.setApprovedDate(issue.getApprovedDate());
        grcIssue.setClosedDate(issue.getClosedDate());
        grcIssue.setIsRestricted(issue.getIsResricted());
        grcIssue.setCurrentDueDate(new BigDecimal(DateUtil.convertToEpoch(issue.getCurrentDueDate())));
        grcIssue.setIssueResponse(issue.getIssueResponse());
        grcIssue.setLoeCode(issue.getLoeCode());
        grcIssue.setNextReviewDate(new BigDecimal(DateUtil.convertToEpoch(issue.getNextReviewDate())));
        grcIssue.setReviewFrequency(issue.getReviewFrequencyCode());
        grcIssue.setActive(IssueConstants.ACTIVE);
     
		return grcIssue;
	}
	
	public GrcIssueUpdate convertRequestToUpdateDB(GrcIssues issue) throws Exception{

		GrcIssueUpdate grcIssue = new GrcIssueUpdate();
		BigDecimal time = new BigDecimal(System.currentTimeMillis() / 1000);
		grcIssue.setIssueId(issue.getIssueCode());
		grcIssue.setIssueName(issue.getIssueName());
        grcIssue.setExecutiveSummary(issue.getExecutiveSummary());
        grcIssue.setIssueDescription(issue.getIssueDescription());
        grcIssue.setHasIFA(issue.getHasImpactedFa());
        grcIssue.setIsRepeated(issue.getIsIssueRepeat());
        grcIssue.setIssueOwner(issue.getIssueOwner().getUserName());
        grcIssue.setFunctionalAreaCode(issue.getFaCode().getFaCode());
        if (issue.getIssueOwnerDelegate()!=null)
        	grcIssue.setIssueOwnerDelegate(issue.getIssueOwnerDelegate().getUserName());
        if (issue.getIssueExecutiveOwner()!=null)
        	grcIssue.setIssueExecutiveOwner(issue.getIssueExecutiveOwner().getUserName());
        if (issue.getIssueApprover()!=null)
        grcIssue.setIssueApprover(issue.getIssueApprover().getUserName());
        if (issue.getIssueIcp()!=null)
        	grcIssue.setIssueIcp(issue.getIssueIcp().getUserName());
        if (issue.getBuSme()!=null)
        	grcIssue.setBuSme(issue.getBuSme().getUserName());
        if (issue.getIssueRco()!=null)
        	grcIssue.setIssueRco(issue.getIssueRco().getUserName());
        if (issue.getGgrcRcoLeader()!=null)
        	grcIssue.setGrcRcoLeader(issue.getGgrcRcoLeader().getUserName());
        grcIssue.setGrcWfStatus(wfStatusHome.findByStatusType(issue.getStatusCode().getName(), "ISSUE_MANAGEMENT"));
        grcIssue.setIsComplianceRelated(issue.getIsComplianceReleated());
        if(issue.getIsComplianceReleated().equals("1")){
            grcIssue.setIsConsumerImpact(issue.getIsConsumerImpact());
        } else if(issue.getIsComplianceReleated().equals("0")){
        	grcIssue.setIsConsumerImpact("0");
        }
        grcIssue.setIssueIdentifiedDate(new BigDecimal(DateUtil.convertToEpoch(issue.getIssueIdentifiedDate())));
        if(issue.getIsConsumerImpact() != null && !issue.getIsConsumerImpact().isEmpty() && grcIssue.getIsConsumerImpact().equals("1")){
        	grcIssue.setFiADueDate(new BigDecimal(DateUtil.convertToEpoch(issue.getFiaDueDate())));
            grcIssue.setFiaDescription(issue.getCfiaDescription());
            grcIssue.setFiaCompletedDate(new BigDecimal(DateUtil.convertToEpoch(issue.getFiaCompDate())));
            grcIssue.setCessationDueDate(new BigDecimal(DateUtil.convertToEpoch(issue.getCessationDueDate())));
            grcIssue.setCessationCompletedOnDate(new BigDecimal(DateUtil.convertToEpoch(issue.getCessationCompDate())));
        } else {
        	grcIssue.setFiADueDate(new BigDecimal("0"));
        	grcIssue.setFiaCompletedDate(new BigDecimal("0"));
        	grcIssue.setCessationDueDate(new BigDecimal("0"));
        	grcIssue.setCessationCompletedOnDate(new BigDecimal("0"));
        }
        grcIssue.setImpactAnalysis(issue.getImpactAnalysis());
        grcIssue.setRootCauseAnalysis(issue.getRootCauseAnalysis());
        grcIssue.setIssueRating(convertToGrcIssueRating(issue.getIssueRating()));
        grcIssue.setExternalIssueDetails(issue.getExtIssueRating());
        grcIssue.setRatingRationale(issue.getRatingRationale());
        grcIssue.setIssueProgressPercent(issue.getIssueProgressPercent());
        grcIssue.setIssueHealth(convertToGrcIssueHealth(issue.getIssueHealth()));
        grcIssue.setCreatedDate(time);
        grcIssue.setCreatedBy(issue.getCreatedBy());
        grcIssue.setApprovedDate(issue.getApprovedDate());
        grcIssue.setClosedDate(issue.getClosedDate());
        grcIssue.setIsRestricted(issue.getIsResricted());
        grcIssue.setCurrentDueDate(new BigDecimal(DateUtil.convertToEpoch(issue.getCurrentDueDate())));
        grcIssue.setIssueResponse(issue.getIssueResponse());
        grcIssue.setLoeCode(issue.getLoeCode());
        grcIssue.setNextReviewDate(new BigDecimal(DateUtil.convertToEpoch(issue.getNextReviewDate())));
        grcIssue.setReviewFrequency(issue.getReviewFrequencyCode());
        grcIssue.setActive(IssueConstants.ACTIVE);
		return grcIssue;
	
	}

    private GrcIssueHealth convertToGrcIssueHealth(IssueHealth issueHealth) {
    	if(issueHealth == null)
			return null;
    	GrcIssueHealth grcIssueHealth = issueHealthDao.getIssueHealthById(issueHealth.getHealthId());
		return grcIssueHealth;
	}

	private GrcIssueRating convertToGrcIssueRating(IssueRating issueRating) {
		if(issueRating == null)
			return null;
    	GrcIssueRating grcIssueRating = issueDao.getIssueRatingById(issueRating.getRatingId());
    	return grcIssueRating;
	}

	public IssueDetailList convertDBTORequest(String userName, IssueDetailList issue) {
		Map<String, String> roleMap = new HashMap<>();
		if (issue.getIssueOwner() != null) {
			roleMap.put(IssueConstants.OWNER, issue.getIssueOwner().getUserName());
		}
		if (issue.getIssueOwnerDelegate() != null) {
			roleMap.put(IssueConstants.OWNER_DELEGATE, issue.getIssueOwnerDelegate().getUserName());
		}
		if (issue.getIssueApprover() != null) {
			roleMap.put(IssueConstants.APPROVER, issue.getIssueApprover().getUserName());
		}
		if (issue.getIssueRco() != null) {
			roleMap.put(IssueConstants.RCO, issue.getIssueRco().getUserName());
		}
		if (issue.getGgrcRcoLeader() != null) {
			roleMap.put(IssueConstants.GGRC_RCO_LEADER, issue.getGgrcRcoLeader().getUserName());
		}
		if (issue.getIssueExecutiveOwner() != null) {
			roleMap.put(IssueConstants.EXECUTIVE_OWNER, issue.getIssueExecutiveOwner().getUserName());
		}
		if (issue.getIssueIcp() != null) {
			roleMap.put(IssueConstants.ICP_REPRESENTATIVE, issue.getIssueIcp().getUserName());
		}
		if (issue.getBuSme() != null) {
			roleMap.put(IssueConstants.ICP_REPRESENTATIVE, issue.getBuSme().getUserName());
		}		
        issue.setMyRole("-");
        for (String key : roleMap.keySet()){
        	if (userName.equals(roleMap.get(key))){
        		issue.setMyRole(key);
        		break;
        	}
        }
        if (issue.getMyRole()== null && !issue.getInformedStakeholders().isEmpty()){
        	for ( InformedStakeholder users: issue.getInformedStakeholders()){
        		if (userName.equals(users.getUserName())){
        			issue.setMyRole(IssueConstants.INFO_STAKEHOLDERS);
            		break;
        		}
        	}
        }
        return issue;
	}

	public IssueDetailList convertDBTORequestForDB(Object issue[]) {
		IssueDetailList grcIssue = new IssueDetailList();
        int index = 0;
        grcIssue.setIssueCode((String) issue[index++]);
        grcIssue.setIssueName((String) issue[index++]);
        
        if (issue[index] != null) {
    		grcIssue.setFunctionalArea((String) issue[index]);            
        }        
        index++;
        
		if (issue[index] !=null) {
			grcIssue.setIssueOwner(getUser(issue[index]));			
		}
		
		index++;
		if (issue[index] != null) {
			grcIssue.setIssueRating((String) issue[index] );			
		}
		
		index++;		
		if (issue[index] != null) {
			String riskType = (String)issue[index];
			if (!riskType.trim().equals("/"))
				grcIssue.setRiskType(riskType);
		}
		
		index++;
		if (issue[index] !=null) {
			String complianceIndicator = (String) issue[index];
				grcIssue.setComplianceIndicator(complianceIndicator);
		}
		index++;
		if (issue[index] !=null) {
			BigDecimal epoch = new BigDecimal((String) issue[index]);
			grcIssue.setDueDate(DateUtil.convertToDate(epoch));
		}		
		index++;
		if (issue[index] !=null) {
			Status gwStatus = new Status();
	        gwStatus.setName((String) issue[index]);
	        grcIssue.setStatus(gwStatus);
		}        
		index++;
        if (issue[index] != null) {
	        BigDecimal epoch2 = new BigDecimal((String) issue[index]);
			grcIssue.setTimeCreated(DateUtil.convertToDate(epoch2));
	    }
        index++;
        if (issue[index] != null) {
        	grcIssue.setIssueOwnerDelegate(getUser(issue[index]));
        }
        index++;
        if (issue[index] != null) {
        	grcIssue.setIssueApprover(getUser(issue[index]));
        }
        index++;
        if (issue[index] != null) {
        	grcIssue.setIssueRco(getUser(issue[index]));
        }
        index++;
        if (issue[index] != null) {
        	grcIssue.setGgrcRcoLeader(getUser(issue[index]));
        }
        index++;
        if (issue[index] != null) {
        	grcIssue.setIssueExecutiveOwner(getUser(issue[index]));
        }
        index++;
        if (issue[index] != null) {
        	grcIssue.setIssueIcp(getUser(issue[index]));
        }
        index++;
        if (issue[index] != null) {
        	grcIssue.setBuSme(getUser(issue[index]));
        }
        index++;
        
        if (issue[index] != null) {
	        List<InformedStakeholder> informedStakeholdersLst = new ArrayList<InformedStakeholder>();
	        Stream<String> stream = Arrays.stream(((String)issue[index]).split(","));
	        stream.forEach(i -> {
	        	InformedStakeholder informedStakeholder = new InformedStakeholder();
	        	informedStakeholder.setUserName(i);
	        	informedStakeholdersLst.add(informedStakeholder);
	        	});
			grcIssue.setInformedStakeholders(informedStakeholdersLst);
        }
        index++;
        if (issue[index] !=null) {
	        GrcUser grcUser = grcIssue.getIssueOwner();
	        if (grcUser != null)
	        	grcUser.setFullName((String) issue[index]);			
        }
        
        return grcIssue;
	}
	
	private GrcUser getUser(Object user) {
		GrcUser grcUser = new GrcUser();
		grcUser.setUserName((String) user);
		return grcUser;
	}
	public GrcIssues convertDBTORequest(GrcIssue issue) {
        GrcIssues grcIssue = new GrcIssues();
        grcIssue.setIssueCode(issue.getIssueId());
        grcIssue.setIssueName(issue.getIssueName());
        grcIssue.setExecutiveSummary(issue.getExecutiveSummary());
        grcIssue.setIssueDescription(issue.getIssueDescription());
        grcIssue.setHasImpactedFa(issue.getHasIFA());
        if(issue.getIsRepeated() != null){
        	grcIssue.setIsIssueRepeat(issue.getIsRepeated());
        }

        List<GrcIssueLinkedMap> linkedIssues = linkedIssueDao.findByIssueId(issue.getIssueId(),IssueConstants.ACTIVE, issue.getVersion());
        List<LinkedIssue> linkedIssueIds = new ArrayList<LinkedIssue>();
        for(GrcIssueLinkedMap linkedIssue : linkedIssues){
        	LinkedIssue linkedIssueId = new LinkedIssue();
        	linkedIssueId.setId(linkedIssue.getId().intValue());
        	linkedIssueId.setLinkedIssueCode(linkedIssue.getLinkedIssueCode());
        	linkedIssueIds.add(linkedIssueId);
        }
        grcIssue.setLinkedIssues(linkedIssueIds);

        GrcWfStatus grcWf = wfStatusHome.findByStatusCode(issue.getGrcWfStatus().getStatusCode());
        if (grcWf != null){
        	Status gwStatus = new Status();
            gwStatus.setCode(grcWf.getStatusCode());
            gwStatus.setName(grcWf.getStatusName());
            grcIssue.setStatusCode(gwStatus);
        }

        GrcUser issueOwner = new GrcUser();
        if(issue.getIssueOwner() != null && !issue.getIssueOwner().isEmpty()){
	        com.paypal.erc.grc.common.dal.models.GrcUser ownerDetails = userDao.findByUserName(issue.getIssueOwner());
	        issueOwner.setFullName(ownerDetails.getLastName().concat(", ".concat(ownerDetails.getFirstName())));
        }
        issueOwner.setUserName(issue.getIssueOwner());
        grcIssue.setIssueOwner(issueOwner);

        ErcdbaGrcFunctionalArea issueFaCode = new ErcdbaGrcFunctionalArea();
        GrcFunctionalArea faDO = faDao.findById(issue.getFunctionalAreaCode());

        issueFaCode.setFaCode(faDO.getFunctionalAreaCode());
        issueFaCode.setFaName(faDO.getFunctionalAreaName());
        String parentCode = faDO.getDescription();
        String result = parentCode.replaceAll("-"," > ");
        issueFaCode.setParentCode(result);
        grcIssue.setFaCode(issueFaCode);

        List<GrcIssueDocMap> maps = issueDocDao.findById(issue.getIssueId());
        List<Document_> docs = new ArrayList<>();
        for(GrcIssueDocMap docMap:maps){
            if(docMap.getIsActive().equals("1")) {
                Document_ docObj = new Document_();
                GrcDoc grcdoc = docMap.getDocCode();
                docObj.setDocumentId(grcdoc.getDocumentId().toString());
                docObj.setDocumentName(grcdoc.getDocumentName());
                docObj.setDocumentTitle(docMap.getDocTitle());
                docObj.setDocumentType(grcdoc.getDocumentType());
                docObj.setExtDocumentId(grcdoc.getExtDocumentId());
                docObj.setTargetSystem(grcdoc.getTargetSystem());
                docObj.setUrl(grcdoc.getUrl());
                docObj.setAdditionalProperty("performed_by", grcdoc.getPerformedBy());
                docObj.setAdditionalProperty("time_uploaded", DateUtil.convertToDateTime(grcdoc.getTimeCreated()));                        
                docs.add(docObj);
            }

        }
        grcIssue.setIssueDocuments(docs);
        
        List<Object> grcIssueIfas = new ArrayList<>();
		List<GrcIssueIfaMap> ifas = issueIfaDao.findByIssueId(issue.getIssueId(),IssueConstants.ACTIVE, issue.getVersion());
    	for (GrcIssueIfaMap vo: ifas) {
    		grcIssueIfas.add(vo.getIfaCode());
    	}
    	grcIssue.setImpactedFaCode(grcIssueIfas);

    	List<GrcIssueRiskMap> grcRiskCategoryList = riskCatDao.findByIssueId(issue.getIssueId(),IssueConstants.ACTIVE, issue.getVersion());
    	if(grcRiskCategoryList != null){
    		List<RiskCategory> riskCategoryList = new ArrayList<>();
    		for(GrcIssueRiskMap grcRiskCategory:grcRiskCategoryList) {
    			RiskCategory riskCategory = new RiskCategory();
    	    	riskCategory.setRiskCategory(grcRiskCategory.getRiskCode());
    	    	riskCategory.setRiskType(grcRiskCategory.getRiskTypeCode());
    	    	riskCategory.setRiskSubType(grcRiskCategory.getRiskSubTypeCode());
    	    	riskCategory.setIsPrimary(grcRiskCategory.getIsPrimary());
    	    	if(StringConstants.YES.equals(grcRiskCategory.getIsPrimary())){
    	    		//riskCategoryList.add(0,riskCategory);
    	    		grcIssue.setPrimaryRiskCategory(riskCategory);
    	    	}
    	    	else {
    	    		riskCategoryList.add(riskCategory);
    	    	}
    		}
    		grcIssue.setRiskCategory(riskCategoryList);
    	}
    	
    	List<GrcRootCauseMap> grcRootCauseCategoryList = rtcDao.findByIssueId(issue.getIssueId(), issue.getVersion());
    	if(grcRootCauseCategoryList != null){
    		List<RootCauseCategory> rootCauseCategoryList = new ArrayList<>();
    		for(GrcRootCauseMap grcRootCauseMap:grcRootCauseCategoryList) {
    			RootCauseCategory rootCauseCat = new RootCauseCategory();	    	
    			rootCauseCat.setRootCauseCategory(grcRootCauseMap.getRootcauseCategoryRootId());
    			rootCauseCat.setSubRootCauseCategory(grcRootCauseMap.getRootcauseCategorySubRootId());
    			rootCauseCat.setRootStatement(grcRootCauseMap.getRootCauseDetails());
    			rootCauseCat.setIsPrimary(grcRootCauseMap.getIsPrimary());
    			if(StringConstants.YES.equals(grcRootCauseMap.getIsPrimary())) {
    				//rootCauseCategoryList.add(0,rootCauseCat);
    				grcIssue.setPrimaryRootCauseCategory(rootCauseCat);
    			}
    			else {
    				rootCauseCategoryList.add(rootCauseCat);
    			}
    		}
    		grcIssue.setRootCauseCategory(rootCauseCategoryList);
    	}
        GrcUser issueOwnerDelegate = new GrcUser();
        if(issue.getIssueOwnerDelegate() != null && !issue.getIssueOwnerDelegate().isEmpty()){
	        com.paypal.erc.grc.common.dal.models.GrcUser issueDelegateDetails = userDao.findByUserName(issue.getIssueOwnerDelegate());
	        issueOwnerDelegate.setFullName(issueDelegateDetails.getLastName().concat(", ".concat(issueDelegateDetails.getFirstName())));
        }
        issueOwnerDelegate.setUserName(issue.getIssueOwnerDelegate());
        grcIssue.setIssueOwnerDelegate(issueOwnerDelegate);

        GrcUser issueExecutiveOwner = new GrcUser();
        if(issue.getIssueExecutiveOwner() != null && !issue.getIssueExecutiveOwner().isEmpty()){
	        com.paypal.erc.grc.common.dal.models.GrcUser executiveOwnerDetails = userDao.findByUserName(issue.getIssueExecutiveOwner());
	        issueExecutiveOwner.setFullName(executiveOwnerDetails.getLastName().concat(", ".concat(executiveOwnerDetails.getFirstName())));
        }
        issueExecutiveOwner.setUserName(issue.getIssueExecutiveOwner());
        grcIssue.setIssueExecutiveOwner(issueExecutiveOwner);

        GrcUser issueApprover = new GrcUser();
        if(issue.getIssueApprover() != null && !issue.getIssueApprover().isEmpty()){
	        com.paypal.erc.grc.common.dal.models.GrcUser issueApproverDetails = userDao.findByUserName(issue.getIssueApprover());
	        issueApprover.setFullName(issueApproverDetails.getLastName().concat(", ".concat(issueApproverDetails.getFirstName())));
        }
        issueApprover.setUserName(issue.getIssueApprover());
        grcIssue.setIssueApprover(issueApprover);

        GrcUser issueIcp = new GrcUser();
        if(issue.getIssueIcp() != null && !issue.getIssueIcp().isEmpty()){
	        com.paypal.erc.grc.common.dal.models.GrcUser issueIcpDetails = userDao.findByUserName(issue.getIssueIcp());
	        issueIcp.setFullName(issueIcpDetails.getLastName().concat(", ".concat(issueIcpDetails.getFirstName())));
        }
        issueIcp.setUserName(issue.getIssueIcp());
        grcIssue.setIssueIcp(issueIcp);

        GrcUser buSme = new GrcUser();
        if(issue.getBuSme() != null && !issue.getBuSme().isEmpty()){
	        com.paypal.erc.grc.common.dal.models.GrcUser buSmeDetails = userDao.findByUserName(issue.getBuSme());
	        buSme.setFullName(buSmeDetails.getLastName().concat(", ".concat(buSmeDetails.getFirstName())));
        }
        buSme.setUserName(issue.getBuSme());
        grcIssue.setBuSme(buSme);

        GrcUser issueRco = new GrcUser();
        if(issue.getIssueRco() != null && !issue.getIssueRco().isEmpty()){
        	com.paypal.erc.grc.common.dal.models.GrcUser issueRcoDetails = userDao.findByUserName(issue.getIssueRco());
        	issueRco.setFullName(issueRcoDetails.getLastName().concat(", ".concat(issueRcoDetails.getFirstName())));
        }
        issueRco.setUserName(issue.getIssueRco());
        grcIssue.setIssueRco(issueRco);
        
        Set<GrcIssueAolMap> grcIssueAolMap = issue.getGrcIssueAolMap();
		for (GrcIssueAolMap grcIssueAolMap2:  grcIssueAolMap) {
			grcIssue.setIssueAol(convertToAreaOfLaw(grcIssueAolMap2.getGrcIssueAreaOfLaw(), grcIssueAolMap2.getGrcIssueSubAreaOfLaw()));			
		}

        GrcUser issueRcoLeader = new GrcUser();
        if(issue.getGrcRcoLeader() != null && !issue.getGrcRcoLeader().isEmpty()){
	        com.paypal.erc.grc.common.dal.models.GrcUser issueRcoLeaderDetails = userDao.findByUserName(issue.getGrcRcoLeader());
	        issueRcoLeader.setFullName(issueRcoLeaderDetails.getLastName().concat(", ".concat(issueRcoLeaderDetails.getFirstName())));
        }
        issueRcoLeader.setUserName(issue.getGrcRcoLeader());
        grcIssue.setGgrcRcoLeader(issueRcoLeader);

        List<InformedStakeholder> issueStakeholders = new ArrayList<InformedStakeholder>();
        List<GrcIssueInfoStakeholders> stakeholders = issueISHDao.findByIssueId(issue.getIssueId(),IssueConstants.ACTIVE, issue.getVersion());
        for(GrcIssueInfoStakeholders stakeholder : stakeholders){
        	InformedStakeholder issueStakeholder = new InformedStakeholder();
        	issueStakeholder.setUserName(stakeholder.getUserName().getUserName());
        	com.paypal.erc.grc.common.dal.models.GrcUser issueStakeholdersDetails = userDao.findByUserName(issueStakeholder.getUserName());
        	issueStakeholder.setFullName(issueStakeholdersDetails.getLastName().concat(", ".concat(issueStakeholdersDetails.getFirstName())));
        	issueStakeholders.add(issueStakeholder);
        }
        grcIssue.setInformedStakeholders(issueStakeholders);
        if(issue.getIsComplianceRelated() != null){
        	grcIssue.setIsComplianceReleated(issue.getIsComplianceRelated());
        } else {
        	grcIssue.setIsComplianceReleated("0");
        }
        if(issue.getIsConsumerImpact() != null){
    		grcIssue.setIsConsumerImpact(issue.getIsConsumerImpact());
        } else {
        	grcIssue.setIsConsumerImpact("0");
        }
        grcIssue.setIssueIdentifiedDate(DateUtil.convertToDateTime(issue.getIssueIdentifiedDate()));
        if(issue.getCessationDueDate() == null || issue.getCessationDueDate().compareTo(BigDecimal.ZERO) == 0){
        	grcIssue.setCessationDueDate(DateUtil.convertToDateTime(issue.getIssueIdentifiedDate()));
        } else {
        	grcIssue.setCessationDueDate(DateUtil.convertToDateTime(issue.getCessationDueDate()));
        }
        if(issue.getCessationCompletedOnDate() == null || issue.getCessationCompletedOnDate().compareTo(BigDecimal.ZERO) == 0){
        	grcIssue.setCessationCompDate(DateUtil.convertToDateTime(issue.getIssueIdentifiedDate()));
        } else {
        	grcIssue.setCessationCompDate(DateUtil.convertToDateTime(issue.getCessationCompletedOnDate()));
        }
        if(issue.getFiADueDate() == null || issue.getFiADueDate().compareTo(BigDecimal.ZERO) == 0){
        	grcIssue.setFiaDueDate(DateUtil.convertToDateTime(issue.getIssueIdentifiedDate()));
        } else {
            grcIssue.setFiaDueDate(DateUtil.convertToDateTime(issue.getFiADueDate()));
        }
        if(issue.getFiaCompletedDate() == null || issue.getFiaCompletedDate().compareTo(BigDecimal.ZERO) == 0){
        	grcIssue.setFiaCompDate(DateUtil.convertToDateTime(issue.getIssueIdentifiedDate()));
        } else {
        	grcIssue.setFiaCompDate(DateUtil.convertToDateTime(issue.getFiaCompletedDate()));
        }
        grcIssue.setCfiaDescription(issue.getFiaDescription());
        grcIssue.setImpactAnalysis(issue.getImpactAnalysis());
        grcIssue.setRootCauseAnalysis(issue.getRootCauseAnalysis());
        grcIssue.setIssueRating(convertToIssueRating(issue.getIssueRating()));
        grcIssue.setExtIssueRating(issue.getExternalIssueDetails());
        grcIssue.setIssueProgressPercent(issue.getIssueProgressPercent());
        grcIssue.setRatingRationale(issue.getRatingRationale());
        grcIssue.setIssueHealth(convertToIssueHealth(issue.getIssueHealth()));
        grcIssue.setTimeCreated(DateUtil.convertToDateTime(issue.getCreatedDate()));
        grcIssue.setCreatedBy(issue.getCreatedBy());
        grcIssue.setUpdatedBy(issue.getUpdatedBy());
        grcIssue.setTimeUpdated(DateUtil.convertToDateTime(issue.getUpdatedDate()));
        grcIssue.setApprovedDate(issue.getApprovedDate());
        grcIssue.setClosedDate(issue.getClosedDate());
        grcIssue.setIsResricted(issue.getIsRestricted());
        grcIssue.setCurrentDueDate(DateUtil.convertToDateTime(issue.getCurrentDueDate()));
        grcIssue.setReviewFrequencyCode(issue.getReviewFrequency());
        grcIssue.setNextReviewDate(DateUtil.convertToDateTime(issue.getNextReviewDate()));
        grcIssue.setLoeCode(issue.getLoeCode());
        grcIssue.setIssueResponse(issue.getIssueResponse());
        grcIssue.setRootCauseAnalysis(issue.getRootCauseAnalysis());
        GrcIssueResponseMap grcIssueResponseMap = issue.getGrcIssueResponseMap();
        if (grcIssueResponseMap != null) {
			grcIssue.setIssueResponse(grcIssueResponseMap.getResponseCode());
		}
        grcIssue.setVersion(Integer.parseInt(issue.getVersion()));
        return grcIssue;
    }
	
    private List<String> convertToIssueRegionString(GrcIssueRegion grcIssueRegion) {
    	List<String> issueRegionString = new ArrayList<String>();
    	IssueRegion issueRegion = new IssueRegion();
    	issueRegion.setRegionCode(Long.toString(grcIssueRegion.getRegionCode()));
    	issueRegion.setRegionName(grcIssueRegion.getRegionName());
    	issueRegion.setRegionDesc(grcIssueRegion.getRegionDesc());
    	Set<GrcIssueCountry> countryCode = grcIssueRegion.getCountryCode();
    	if (countryCode != null)
    	for (GrcIssueCountry issueCountry : countryCode){
    		GrcCountry regionCountry  = new GrcCountry();
    		regionCountry.setCountryCode(Long.toString(issueCountry.getCountryCode()));
    		regionCountry.setCountryName(issueCountry.getCountryName());
    		regionCountry.setCountryDesc(issueCountry.getCountryDescription());
    		issueRegion.setRegionCountry( regionCountry);
    		for (GrcIssueState grcIssueState : issueCountry.getStateCode()){
    			CountryState countryState  = new CountryState();
    			countryState.setStateCode(Long.toString(grcIssueState.getCountryCode()));
    			countryState.setStateName(grcIssueState.getStateName());
    			countryState.setStateDesc(grcIssueState.getStateDesc());
    			regionCountry.setCountryState( countryState);
    			for (GrcIssueProvince grcIssueProvince : grcIssueState.getProvinceCode()) {
    				StateProvince stateProvince = new StateProvince();
    				Long provinceCode = grcIssueProvince.getProvinceCode();
					String stringStateProvince = Long.toString(provinceCode);
					stateProvince.setProvinceCode(stringStateProvince);
    				stateProvince.setProvinceName(grcIssueProvince.getProvinceName());
    				stateProvince.setProvinceDesc(grcIssueProvince.getProvinceDesc());
    				issueRegionString.add(stringStateProvince);
    			}
        	}
    	}
		return issueRegionString;
	}

    private IssueRegion convertToIssueRegion(GrcIssueRegion grcIssueRegion) {
    	IssueRegion issueRegion = new IssueRegion();
    	issueRegion.setRegionCode(Long.toString(grcIssueRegion.getRegionCode()));
    	issueRegion.setRegionName(grcIssueRegion.getRegionName());
    	issueRegion.setRegionDesc(grcIssueRegion.getRegionDesc());
    	Set<GrcIssueCountry> countryCode = grcIssueRegion.getCountryCode();
    	if (countryCode != null)
    	for (GrcIssueCountry issueCountry : countryCode){
    		GrcCountry regionCountry  = new GrcCountry();
    		regionCountry.setCountryCode(Long.toString(issueCountry.getCountryCode()));
    		regionCountry.setCountryName(issueCountry.getCountryName());
    		regionCountry.setCountryDesc(issueCountry.getCountryDescription());
    		issueRegion.setRegionCountry( regionCountry);
    		for (GrcIssueState grcIssueState : issueCountry.getStateCode()){
    			CountryState countryState  = new CountryState();
    			countryState.setStateCode(Long.toString(grcIssueState.getCountryCode()));
    			countryState.setStateName(grcIssueState.getStateName());
    			countryState.setStateDesc(grcIssueState.getStateDesc());
    			regionCountry.setCountryState( countryState);
    			for (GrcIssueProvince grcIssueProvince : grcIssueState.getProvinceCode()) {
    				StateProvince stateProvince = new StateProvince();
    				stateProvince.setProvinceCode(Long.toString(grcIssueProvince.getProvinceCode()));
    				stateProvince.setProvinceName(grcIssueProvince.getProvinceName());
    				stateProvince.setProvinceDesc(grcIssueProvince.getProvinceDesc());
    			}
        	}
    	}
		return issueRegion;
	}

	private AreaOfLaw convertToAreaOfLaw(GrcIssueAreaOfLaw grcIssueAreaOfLaw,
			GrcIssueSubAreaOfLaw grcIssueSubAreaOfLaw) {
    	if (grcIssueAreaOfLaw == null)
			return null;
    	AreaOfLaw issueAol = new AreaOfLaw();
    	issueAol.setAolCode(grcIssueAreaOfLaw.getAolCode());
    	issueAol.setAolName(grcIssueAreaOfLaw.getAolName());
    	issueAol.setAolDesc(grcIssueAreaOfLaw.getAolDesc());
    	if (grcIssueSubAreaOfLaw!=null) {
	    	SubAol subAol = new SubAol();
	    	subAol.setSaolCode(grcIssueSubAreaOfLaw.getSaolCode());
	    	subAol.setSaolName(grcIssueSubAreaOfLaw.getSaolName());
	    	subAol.setSaolDesc(grcIssueSubAreaOfLaw.getSaolDescription());
			issueAol.setSubAol(subAol);
    	}
		return issueAol;
	}

	private IssueHealth convertToIssueHealth(GrcIssueHealth grcIssueHealth) {
		if (grcIssueHealth == null)
			return null;
    	IssueHealth issueHealth = new IssueHealth();
    	issueHealth.setHealthId(grcIssueHealth.getHealthId());
    	issueHealth.setHealthType(grcIssueHealth.getIssueHealth());
    	issueHealth.setHealthTypeDes(grcIssueHealth.getIssueHealthDes());
    	issueHealth.setCreatedDate(Long.toString(System.currentTimeMillis()));
		return issueHealth;
	}

	private IssueRating convertToIssueRating(GrcIssueRating grcIssueRating) {
		
		if (grcIssueRating == null)
			return null;
		IssueRating issueRating = new IssueRating();
		issueRating.setRatingId(grcIssueRating.getRatingId());
		issueRating.setRatingType(grcIssueRating.getRatingType());
		issueRating.setRatingTypeDes(grcIssueRating.getRatingTypeDes());
		issueRating.setCreatedDate(Long.toString(System.currentTimeMillis()));
		return issueRating;
	}

	public GrcRootCauseMap convertRootCauseRequestTODB(IssueMetaData issueProps, RootCauseCategory rootCauseCategory) throws Exception {

		GrcRootCauseMap grcRootCause = null;
		if (rootCauseCategory != null && rootCauseCategory.getRootCauseCategory() != null) {
			BigDecimal time = new BigDecimal(System.currentTimeMillis() / 1000);
			grcRootCause = new GrcRootCauseMap();
			grcRootCause.setIssuesIssueId(issueProps.getIssueCode());
			grcRootCause.setRootcauseCategoryRootId(rootCauseCategory.getRootCauseCategory());
			grcRootCause.setRootcauseCategorySubRootId(rootCauseCategory.getSubRootCauseCategory());
			grcRootCause.setIsPrimary(rootCauseCategory.getIsPrimary());
			grcRootCause.setRootCauseDetails(rootCauseCategory.getRootStatement());
			grcRootCause.setCreatedBy(issueProps.getCreatedBy());
			grcRootCause.setTimeCreated(time);
			grcRootCause.setIsActive(IssueConstants.ACTIVE);
			grcRootCause.setVersion(issueProps.getVersion());
		}
    	return grcRootCause;
    }

	public List<GrcRootCauseMap> convertRootCauseRequestTODB(GrcIssue issue, List<RootCauseCategory> rootCauseCategoryList, List<GrcRootCauseMap> rootCauseMaps) throws Exception {

		List<GrcRootCauseMap> rtc = new ArrayList<GrcRootCauseMap>();
		BigDecimal time = new BigDecimal(System.currentTimeMillis() / 1000);
		for(GrcRootCauseMap rootCauseMap : rootCauseMaps){
			if(rootCauseMap != null && rootCauseMap.getRootcauseCategoryRootId() != null){
				rootCauseMap.setIsActive(IssueConstants.INACTIVE);
				rootCauseMap.setUpdatedBy(issue.getUpdatedBy());
				rootCauseMap.setTimeUpdated(time);
				rtc.add(rootCauseMap);
			}
		}
		for(RootCauseCategory rootCauseCategory : rootCauseCategoryList){
			if(rootCauseCategory != null && rootCauseCategory.getRootCauseCategory() != null){
				GrcRootCauseMap grcRootCause = new GrcRootCauseMap();
				grcRootCause.setIssuesIssueId(issue.getIssueId());
				grcRootCause.setRootcauseCategoryRootId(rootCauseCategory.getRootCauseCategory());
				grcRootCause.setRootcauseCategorySubRootId(rootCauseCategory.getSubRootCauseCategory());
				grcRootCause.setIsPrimary(rootCauseCategory.getIsPrimary());
				grcRootCause.setRootCauseDetails(rootCauseCategory.getRootStatement());
				grcRootCause.setUpdatedBy(issue.getUpdatedBy());
				grcRootCause.setTimeUpdated(time);
				grcRootCause.setIsActive(IssueConstants.ACTIVE);
				grcRootCause.setVersion(issue.getVersion());
				rtc.add(grcRootCause);
			}
		}
		return rtc;
	}

    public List<GrcIssueInfoStakeholders> getInformedStakeholders(IssueMetaData issueProps, List<InformedStakeholder> stakeholders) throws Exception {
    	List<GrcIssueInfoStakeholders> issueInfoUsers = new ArrayList<>();
    	
    	for (InformedStakeholder vo: stakeholders) {
    		GrcIssueInfoStakeholders issueInformedDO = new GrcIssueInfoStakeholders();
    		issueInformedDO.setIssueId(issueProps.getIssueCode());
    		issueInformedDO.setActive(IssueConstants.ACTIVE);
    		issueInformedDO.setVersion(issueProps.getVersion());
    		issueInformedDO.setUserName(userDao.findByUserName(vo.getUserName()));
    		
    		issueInfoUsers.add(issueInformedDO);
    	}
    	return issueInfoUsers;
    }
    
    public List<GrcIssueLinkedMap> getLinkedIssueDO(IssueMetaData issueProps, List<LinkedIssue> linkedIssues) throws Exception {
    	List<GrcIssueLinkedMap> linkedDOs = new ArrayList<>();
    	
    	for (LinkedIssue vo: linkedIssues) {
    		GrcIssueLinkedMap issueLinkedDO = new GrcIssueLinkedMap();
    		issueLinkedDO.setIssueCode(issueProps.getIssueCode());
    		issueLinkedDO.setLinkedIssueCode(vo.getLinkedIssueCode());
    		issueLinkedDO.setActive(IssueConstants.ACTIVE);
    		issueLinkedDO.setVersion(issueProps.getVersion());
    		issueLinkedDO.setCreatedDate(new BigDecimal(System.currentTimeMillis()));
    		issueLinkedDO.setCreatedBy(issueProps.getCreatedBy());
    		
    		linkedDOs.add(issueLinkedDO);
    	}
    	return linkedDOs;
    }

    /**
     * This method converts the GRCIssueProgress entity to IssueProgress DTO
     * 
     * @author prasubramanian
     * @param entityList
     * @return
     */
    public List<IssueProgress> convertDBToResponse(List<GrcIssueProgress> entityList) {
    	List<IssueProgress> issueProgressList=new ArrayList<IssueProgress>();
    	for(GrcIssueProgress entity : entityList){
    		IssueProgress issueProgress=new IssueProgress();
    		issueProgress.setProgressId(entity.getProgressID());
    		issueProgress.setProgressPercentage(entity.getProgressPercentage());
//    		issueProgress.setProgressPercentageDesc(inTemp.getProgressPercentageDesc());
    		issueProgress.setCreatedBy(entity.getCreatedBy());
    		issueProgress.setUpdatedBy(entity.getUpdatedBy());
    		issueProgress.setTimeCreated(entity.getCreatedDate());
    		issueProgress.setTimeUpdated(entity.getUpdatedDate());
    		issueProgressList.add(issueProgress);
    	}
    	return issueProgressList;
    }

    
    /**
     * This method converts issue response entity to issue response DTO
     * 
     * @author prasubramanian
     * @param GrcIssueResponse entity
     * @return IssueResponse DTO
     */
    public List<IssueResponse> convertIssueResponse(List<GrcIssueResponse> entityList) {
    	List<IssueResponse> issueResponseList=new ArrayList<IssueResponse>();
    	
    	for(GrcIssueResponse entity:entityList){
    		IssueResponse issueResponse=new IssueResponse();
    		issueResponse.setResponseId(entity.getResponseID());
    		issueResponse.setResponseType(entity.getIssueReponse());
    		issueResponse.setResponseTypeDes(entity.getIssueReponseDesc());
    		issueResponse.setCreatedBy(entity.getCreatedBy());
    		issueResponse.setUpdateBy(entity.getUpdatedBy());
    		issueResponse.setCreatedDate(entity.getCreatedDate());
    		issueResponse.setUpdatedDate(entity.getUpdatedDate());
    		issueResponseList.add(issueResponse);
    	}
    	return issueResponseList;
    }
    
    /**
     * This method converts GRCIssueEffortLevel entity into IssueLevelOfEffort DTO
     * 
     * @author prasubramanian
     * @param entityList
     * @return
     */
    public List<IssueLevelOfEffort> convertLevelOfEffort(List<GrcIssueEffortLevel> entityList){
    	List<IssueLevelOfEffort> respList=new ArrayList<IssueLevelOfEffort>();
    	for(GrcIssueEffortLevel entity:entityList){
    		IssueLevelOfEffort effortLevel=new IssueLevelOfEffort();
    		effortLevel.setLevelCode(entity.getEffortLevelId().toString());
    		effortLevel.setLevelName(entity.getEffortLevel());
    		effortLevel.setLevelDes(entity.getEffortLevelDesc());
    		effortLevel.setCreatedBy(entity.getCreatedBy());
    		effortLevel.setUpdatedBy(entity.getUpdatedBy());
    		effortLevel.setTimeCreated(entity.getCreatedDate());
    		effortLevel.setTimeUpdated(entity.getUpdatedDate());
    		respList.add(effortLevel);
    	}
    	return respList;
    }
    
    /**
     * This method converts the GRCIssueReviewFrequency entity into IssueReviewFrequency DTO
     * 
     * @author prasubramanian
     * @param entityList
     * @return
     */
    public List<IssueReviewFrequency> convertReviewFrequency(List<GrcIssueReviewFrequency> entityList){
    	List<IssueReviewFrequency> respList=new ArrayList<IssueReviewFrequency>();
    	
    	for(GrcIssueReviewFrequency entity:entityList){
    		IssueReviewFrequency reviewFrequency=new IssueReviewFrequency();
    		reviewFrequency.setReviewFrequencyId(entity.getReviewFrequencyId());
    		reviewFrequency.setReviewFrequency(entity.getReviewFrequency());
    		reviewFrequency.setCreatedBy(entity.getCreatedBy());
    		reviewFrequency.setUpdatedBy(entity.getUpdatedBy());
    		reviewFrequency.setCreatedDate(entity.getCreatedDate());
    		reviewFrequency.setUpdatedDate(entity.getUpdatedDate());
    		respList.add(reviewFrequency);
    	}
    	return respList;
    }
    
    /**
     * This method converts GRCIssueHealth entity into IssueHealth DTO
     * 
     * @author prasubramanian
     * @param entityList
     * @return
     */
	public List<IssueHealth> convertIssueHealth(List<GrcIssueHealth> entityList) {
		List<IssueHealth> respList = new ArrayList<IssueHealth>();
		for (GrcIssueHealth entity : entityList) {
			IssueHealth issueHealth = new IssueHealth();
			issueHealth.setHealthId(entity.getHealthId());
			issueHealth.setHealthType(entity.getIssueHealth());
			issueHealth.setHealthTypeDes(entity.getIssueHealthDes());
			issueHealth.setCreatedBy(entity.getCreatedBy());
			issueHealth.setUpdateBy(entity.getUpdatedBy());
			issueHealth.setCreatedDate(entity.getCreatedDate());
			issueHealth.setUpdatedDate(entity.getUpdatedDate());
			respList.add(issueHealth);
		}
		return respList;
	}
    
	/**
	 * This method converts the GrcIssueHealth entity to IssueHealth DTO 
	 * 
	 * @author prasubramanian
	 * @param entityList
	 * @return
	 * @throws Exception 
	 */
	public List<IssueHistory> convertIssueHistoryEntityToDTO(
			List<GrcIssueHistory> entityList, Integer fieldType) throws Exception {
		Map<String, String> fieldStatusMap = new HashMap<>();
		fieldStatusMap.put("0", "Rejected");
		fieldStatusMap.put("1", "Approved");
		fieldStatusMap.put("2", "Pending Approval");
		
		List<IssueHistory> respList = new ArrayList<IssueHistory>();
		for (GrcIssueHistory entity : entityList) {
			IssueHistory issueHistory = new IssueHistory();
			issueHistory.setIssueCode(entity.getIssueCode());
			issueHistory.setFieldName(entity.getFieldName());
			if (entity.getFieldName().equals(IssueConstants.ISSUE_RISK_TYPE)){
				List<Object> oldRisk = new ArrayList<>(); 
				List<Object> newRisk = new ArrayList<>();
				if (entity.getOldValue()!=null && !entity.getOldValue().isEmpty()){
					String[] riskTypes = entity.getOldValue().split(";");
					for(String str : riskTypes){
						oldRisk = riskCatDao.findByRiskCode(Arrays.asList(str.split(",")));
						oldRisk.add(";");
					}
				}
				if (entity.getNewValue()!=null && !entity.getNewValue().isEmpty()){
					String[] riskTypes = entity.getNewValue().split(";");
					for(String str : riskTypes){
						newRisk = riskCatDao.findByRiskCode(Arrays.asList(str.split(",")));
						newRisk.add(";");
					}
				}
				issueHistory.setOldValue(oldRisk.isEmpty()?"N/A":oldRisk.toString());
				issueHistory.setNewValue(newRisk.isEmpty()?"N/A":newRisk.toString());
			}
			else if (entity.getFieldName().equals(IssueConstants.ISSUE_RC)){
				List<Object> oldRC = new ArrayList<>(); 
				List<Object> newRC = new ArrayList<>();
				if (entity.getOldValue()!=null && !entity.getOldValue().isEmpty()){
					String[] rootCause = entity.getOldValue().split(";");
					for(String str : rootCause){
						String[] strSplit = str.split(",");
						List<String> listRCCode = new ArrayList<>();
						listRCCode.add(strSplit[0]);
						listRCCode.add(strSplit[1]);
						List<Object> rtcNameList = rtcDao.findByRTCCode(listRCCode);
						oldRC.addAll(rtcNameList);
						if(strSplit.length==3)
							oldRC.add(strSplit[2]);
					}
				}
				if (entity.getNewValue()!=null && !entity.getNewValue().isEmpty()){
					String[] rootCause = entity.getNewValue().split(";");
					for(String str : rootCause){
						String[] strSplit = str.split(",");
						List<String> listRCCode = new ArrayList<>();
						listRCCode.add(strSplit[0]);
						listRCCode.add(strSplit[1]);
						List<Object> rtcNameList = rtcDao.findByRTCCode(listRCCode);
						newRC.addAll(rtcNameList);
						if(strSplit.length==3)
							newRC.add(strSplit[2]);
					}
				}
				issueHistory.setOldValue(oldRC.isEmpty()?"N/A":oldRC.toString());
				issueHistory.setNewValue(newRC.isEmpty()?"N/A":newRC.toString());
			}
			else if (entity.getFieldName().equals(IssueConstants.ISSUE_BPC)){
				List<Object> oldBPC = new ArrayList<>(); 
				List<Object> newBPC = new ArrayList<>();
				if (entity.getOldValue()!=null && !entity.getOldValue().isEmpty()){
					String[] bpc = entity.getOldValue().split(",");
					oldBPC = bpcDao.findByCategoryCode(Arrays.asList(bpc));
				}
				if (entity.getNewValue()!=null && !entity.getNewValue().isEmpty()){
					String[] bpc = entity.getNewValue().split(",");
					newBPC = bpcDao.findByCategoryCode(Arrays.asList(bpc));
				}
				issueHistory.setOldValue(oldBPC.isEmpty()?"N/A":oldBPC.toString());
				issueHistory.setNewValue(newBPC.isEmpty()?"N/A":newBPC.toString());
			}
			else if (entity.getFieldName().equals(IssueConstants.ISSUE_REGION)){
				List<Object> oldRegion = new ArrayList<>(); 
				List<Object> newRegion = new ArrayList<>();
				if (entity.getOldValue()!=null && !entity.getOldValue().isEmpty()){
					List<Long> listRegionCode = new ArrayList<>();
					String[] region = entity.getOldValue().split(";");
					for (String s : region)
						listRegionCode.add(Long.valueOf(s));
					oldRegion = issueDao.findByProvinceCode(listRegionCode);
				}
				if (entity.getNewValue()!=null && !entity.getNewValue().isEmpty()){
					List<Long> listRegionCode = new ArrayList<>();
					String[] region = entity.getNewValue().split(";");
					for (String s : region)
						listRegionCode.add(Long.valueOf(s));
					newRegion = issueDao.findByProvinceCode(listRegionCode);
				}
				issueHistory.setOldValue(oldRegion.isEmpty()?"N/A":oldRegion.toString());
				issueHistory.setNewValue(newRegion.isEmpty()?"N/A":newRegion.toString());
			}
			else if (entity.getFieldName().equals(IssueConstants.ISSUE_PRODUCT)){
				List<Object> oldProduct = new ArrayList<>(); 
				List<Object> newProduct = new ArrayList<>();
				if (entity.getOldValue()!=null && !entity.getOldValue().isEmpty()){
					String[] product = entity.getOldValue().split(";");
					oldProduct = productDao.findByFeatureCode(Arrays.asList(product));
				}
				if (entity.getNewValue()!=null && !entity.getNewValue().isEmpty()){
					String[] product = entity.getNewValue().split(",");
					newProduct = productDao.findByFeatureCode(Arrays.asList(product));
				}
				issueHistory.setOldValue(oldProduct.isEmpty()?"N/A":oldProduct.toString());
				issueHistory.setNewValue(newProduct.isEmpty()?"N/A":newProduct.toString());
			}
			else if (entity.getFieldName().equals(IssueConstants.ISSUE_AOL)){
				List<Object> oldAol = new ArrayList<>(); 
				List<Object> newAol = new ArrayList<>();
				if (entity.getOldValue()!=null && !entity.getOldValue().isEmpty()){
					String[] aol = entity.getOldValue().split(",");
					String result = aolMapDao.fetchNameByAolCode(aol[0]);
					if(result!=null)
						oldAol.add(result);
					result = aolMapDao.fetchNameBySubAolCode(aol[1]);
					if(result!=null)
						oldAol.add(result);
				}
				if (entity.getNewValue()!=null && !entity.getNewValue().isEmpty()){
					String[] aol = entity.getNewValue().split(",");
					String result = aolMapDao.fetchNameByAolCode(aol[0]);
					if(result!=null)
						newAol.add(result);
					result = aolMapDao.fetchNameBySubAolCode(aol[1]);
					if(result!=null)
						newAol.add(result);
				}
				issueHistory.setOldValue(oldAol.isEmpty()?"N/A":oldAol.toString());
				issueHistory.setNewValue(newAol.isEmpty()?"N/A":newAol.toString());
			}
			else{
				issueHistory.setOldValue(entity.getOldValue()==null?"N/A":entity.getOldValue());
				issueHistory.setNewValue(entity.getNewValue()==null ?"N/A":entity.getNewValue());
			}
			issueHistory.setCommentCode(entity.getCommentCode());
			if ("1".equals(entity.getStatus()))
				issueHistory.setStatus(fieldType == null ?
						(entity.getFieldType()==1?"Updated":"Approved"):entity.getStatus());
			else
				issueHistory.setStatus(fieldType == null ? 
					fieldStatusMap.get(entity.getStatus()):entity.getStatus());
			issueHistory.setVersion(entity.getIssueVersion());
			issueHistory.setCreatedBy(entity.getCreatedBy());
			issueHistory.setApprovedBy(entity.getApprovedBy());
			issueHistory.setTimeCreated(DateUtil.convertToDateTime(new BigDecimal(entity.getTimeCreated())));
			respList.add(issueHistory);
		}
		return respList;
	}

	public List<GrcIssueHistory> convertIssueHistoryDtoToEntity(List<IssuesFieldUpdate> dtoList) {
		List<GrcIssueHistory> entityList=new ArrayList<GrcIssueHistory>();
		for(IssuesFieldUpdate dto: dtoList)
		{
			GrcIssueHistory entity = new GrcIssueHistory();
			entity.setIssueCode(dto.getIssueCode());
			entity.setTableName(dto.getTableName());
			entity.setFieldName(dto.getFieldName());
			//entity.setFieldType(dto.getFieldName());
			entity.setOldValue(dto.getOldValue());
			entity.setNewValue(dto.getNewValue());
			entity.setStatus(dto.getStatus());
			entity.setIssueVersion(dto.getIssueVersion());
			entity.setActive(dto.getActive());
			entity.setCommentCode(dto.getCommentCode());
			entity.setApprovedBy(dto.getApprovedBy());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setTimeCreated(dto.getTimeCreated());
			entity.setTimeApproved(dto.getTimeApproved());
			entityList.add(entity);
		}
		return entityList;
	}
	

	
	///product conversion


	
	
    public GrcIssueDocuments convertIssueDocIdTODB1 (GrcIssue issue, Document_ doc) throws Exception {
    	GrcIssueDocuments issueDocIds = new GrcIssueDocuments();

    	issueDocIds.setDocumentId(doc.getDocumentId());
    	issueDocIds.setIssuesIssueId(String.valueOf(issue.getIssueId()));
		return issueDocIds;
    }
    
    public GrcIssueDocuments convertIssueDocIdTODB (IssueMetaData issueProps, Document_ doc) throws Exception {
    	BigDecimal time = new BigDecimal(System.currentTimeMillis() / 1000);
    	GrcIssueDocuments issueDocIds = new GrcIssueDocuments();
    	issueDocIds.setIsActive("1");
    	issueDocIds.setVersion(issueProps.getVersion());
    	issueDocIds.setDocumentTitle(doc.getDocumentTitle());
    	issueDocIds.setDocumentId(doc.getDocumentId());
    	issueDocIds.setCreatedBy(issueProps.getCreatedBy());
    	issueDocIds.setIssuesIssueId(String.valueOf(issueProps.getIssueCode()));
    	issueDocIds.setTimeCreated(time);
		return issueDocIds;
    }

	public List<GrcIssueDocMap> convertUpdateIssueDocIdTODB(GrcIssues issue) {

		List<GrcIssueDocMap> issueDocMaps = issueDocDao.findById(issue.getIssueCode());
		List<BigDecimal> docToRemove = new ArrayList<>();
        List<String> docToAdd = new ArrayList<>();
        //List<GrcIssueDocMap> newIssueDocs = new ArrayList<>();
        List<Document_> documents = issue.getIssueDocuments();
        if(documents != null && !documents.isEmpty()){
        	BigDecimal time = new BigDecimal(System.currentTimeMillis() / 1000);
        	for(Document_ doc : documents){
                docToAdd.add(doc.getDocumentId());
            }
            for (GrcIssueDocMap docMap : issueDocMaps) {
                BigDecimal docId = docMap.getId();
                if (!docToAdd.contains(docId.toString())) {
                    docToRemove.add(docId);
                }

            }
            for (BigDecimal inDocs : docToRemove) {
            	GrcIssueDocMap docMap = issueDocDao.findById(inDocs);
                if (docMap != null) {
                    docMap.setIsActive("0");
                    docMap.setVersion("1");
                    docMap.setUpdatedBy(issue.getUpdatedBy());
                    docMap.setTimeUpdated(time);
                }
                issueDocMaps.add(docMap);
            }
            for(Document_ docs : documents) {
            	GrcIssueDocMap doc = issueDocDao.findByDocumentId(docs.getDocumentId(), issue.getIssueCode());
                if(doc==null) {
                	GrcIssueDocMap docMap = new GrcIssueDocMap();
                   GrcDoc grcDoc = docHome.findById(new BigDecimal(docs.getDocumentId()));
                   docMap.setDocCode(grcDoc);

                   GrcIssue issueCode = new GrcIssue();
                   issueCode.setIssueId(issue.getIssueCode());
                   docMap.setIssueCode(issueCode);
                   docMap.setDocTitle(docs.getDocumentTitle());
                   docMap.setCreatedBy(issue.getCreatedBy());
                   docMap.setUpdatedBy(issue.getUpdatedBy());
                   docMap.setIsActive("1");
                   docMap.setVersion(String.valueOf(issue.getVersion()));
                   issueDocMaps.add(docMap);
                } else {
                	doc.setDocTitle(docs.getDocumentTitle());
                    doc.setIsActive("1");
                    doc.setTimeUpdated(time);
                    doc.setUpdatedBy(issue.getUpdatedBy());
                    issueDocMaps.add(doc);
                }
            }
        }
		return issueDocMaps;
	}

	public List<GrcIssueAuditLog> getAuditLogs(IssueMetaData issueProps, List<IssuesAuditLog> auditLogs) {
		List<GrcIssueAuditLog> grcIssueAuditLogs = new ArrayList<>();
    	for (IssuesAuditLog vo: auditLogs) {
    		GrcIssueAuditLog grcIssueAuditLog = new GrcIssueAuditLog();
    		grcIssueAuditLog.setIssueCode(issueProps.getIssueCode());
    		grcIssueAuditLog.setFromStatusCode(vo.getFromStatusCode());
    		grcIssueAuditLog.setToStatusCode(vo.getToStatusCode());
    		grcIssueAuditLog.setActionCode(vo.getActionCode());
    		grcIssueAuditLog.setCommentCode(vo.getToStatusCode());
    		grcIssueAuditLog.setCreatedBy(issueProps.getCreatedBy());
    		grcIssueAuditLog.setTimeCreated(System.currentTimeMillis()/1000);
    		grcIssueAuditLog.setActive(IssueConstants.ACTIVE);
    		grcIssueAuditLog.setVersion(issueProps.getVersion());
    		grcIssueAuditLogs.add(grcIssueAuditLog);
    	}			
		return grcIssueAuditLogs;
	}
	
	public List<GrcIssueAuditLog> getAuditLogsForUpdate(GrcIssues grcIssue, List<IssuesAuditLog> auditLogs) {
		List<GrcIssueAuditLog> grcIssueAuditLogs = new ArrayList<>();
		
    	for (IssuesAuditLog vo: auditLogs) {
    		GrcIssueAuditLog grcIssueAuditLog = new GrcIssueAuditLog();
    		grcIssueAuditLog.setAuditCode(Long.parseLong(vo.getAuditCode()));
    		grcIssueAuditLog.setIssueCode(grcIssue.getIssueCode());
    		grcIssueAuditLog.setFromStatusCode(vo.getFromStatusCode());
    		grcIssueAuditLog.setToStatusCode(vo.getToStatusCode());
    		grcIssueAuditLog.setActionCode(vo.getToStatusCode());
    		grcIssueAuditLog.setCommentCode(vo.getToStatusCode());		
    		grcIssueAuditLogs.add(grcIssueAuditLog);
    	}			
		return grcIssueAuditLogs;
	}

	public List<IssuesAuditLog> convertDBTORequest(List<GrcIssueAuditLog> findIssueAuditLogByIssueCode) {
		List<IssuesAuditLog> grcIssueAuditLogs = new ArrayList<>();
		
    	for (GrcIssueAuditLog vo: findIssueAuditLogByIssueCode) {
    		IssuesAuditLog grcIssueAuditLog = new IssuesAuditLog();
    		grcIssueAuditLog.setAuditCode(Long.toString(vo.getAuditCode()));
    		grcIssueAuditLog.setIssueCode(vo.getIssueCode());
    		grcIssueAuditLog.setFromStatusCode(vo.getFromStatusCode());
    		grcIssueAuditLog.setToStatusCode(vo.getToStatusCode());
    		grcIssueAuditLog.setActionCode(vo.getToStatusCode());
    		grcIssueAuditLog.setCommentCode(vo.getCommentCode());
    		grcIssueAuditLog.setCreatedBy(vo.getCreatedBy());
    		grcIssueAuditLog.setTimeCreated(DateUtil.convertToDateTime(new BigDecimal(vo.getTimeCreated())));    		
    		grcIssueAuditLogs.add(grcIssueAuditLog);
    	}			
		return grcIssueAuditLogs;
	}

	public GrcIssueRegionMap getRegionMap(IssueMetaData issueProps,
                                          GrcIssueProvince provinceInput,
                                          List<GrcIssueRegion> regions,
                                          String operation) throws Exception {

	    GrcIssueRegionMap map = new GrcIssueRegionMap();

	    boolean flagToBreak = false;

	    if (regions != null) {
            for (GrcIssueRegion region : regions) {
                Set<GrcIssueCountry> countryCode = region.getCountryCode();
                if(countryCode != null)
                for (GrcIssueCountry country : countryCode) {
                    Set<GrcIssueState> stateCode = country.getStateCode();
                    if(stateCode != null)
                    for (GrcIssueState state : stateCode) {
                        Set<GrcIssueProvince> provinceCode = state.getProvinceCode();
                        if(provinceCode != null)
                        for (GrcIssueProvince province : provinceCode) {
                            if (province.getProvinceCode().equals(provinceInput.getProvinceCode())) {
                                map.setGrcIssueProvince(province);
                                //map.setGrcIssue(grcIssue);
                                map.setGrcIssueState(state);
                                map.setGrcIssueCountry(country);
                                map.setGrcIssueRegion(region);
                                map.setRegionCode(String.valueOf(region.getRegionCode()));
                                map.setCountryCode(String.valueOf(country.getCountryCode()));
                                map.setStateCode(String.valueOf(state.getStateCode()));
                                map.setProvinceCode(String.valueOf(province.getProvinceCode()));
                                map.setIssueCode(issueProps.getIssueCode());
                                map.setIsActive(IssueConstants.ACTIVE);
                                map.setVersion(issueProps.getVersion());
                                flagToBreak = true;
                                break;
                            }
                        }
                        if (flagToBreak) break;
                    }
                    if (flagToBreak) break;
                }
                if (flagToBreak) break;
            }
        }
        return map;
	}

	public GrcIssueSource convertPrimaryIssueSourceToDB(IssueMetaData issueProps, PrimaryIssueSource issueSource) {
		GrcIssueSource grcIssueSource = new GrcIssueSource();
		
		grcIssueSource.setIssueCode(issueProps.getIssueCode());
		grcIssueSource.setIsPrimary(StringConstants.YES);
		grcIssueSource.setSourceType((String)issueSource.getSourceCode().get(0));
		grcIssueSource.setIssueVersion(issueProps.getVersion());
		Object additionalValuePrimary = issueSource.getAdditionalValue();
		if (additionalValuePrimary instanceof List<?>) {
			mapOneToManyForIssueSource(grcIssueSource, (String)issueSource.getSourceCode().get(0), additionalValuePrimary);				
		}
		else{
			grcIssueSource.setSourceTypeDesc((String)additionalValuePrimary);
		}		
		grcIssueSource.setCreatedBy(issueProps.getCreatedBy());
		grcIssueSource.setActive(IssueConstants.ACTIVE);
		//grcIssueSource.setTimeCreated(grcIssue.getCreatedBy());
		
		return grcIssueSource;		
	}
	
	public GrcIssueSource convertSecondaryIssueSourceToDB(IssueMetaData issueProps, String sourceCodeString, List<Object> valuesList) {
		GrcIssueSource grcIssueSource = new GrcIssueSource();
		grcIssueSource.setIssueCode(issueProps.getIssueCode());
		grcIssueSource.setIsPrimary(StringConstants.NO);
		grcIssueSource.setSourceType(sourceCodeString);
		grcIssueSource.setIssueVersion(issueProps.getVersion());
		if (isMultiValuedField(sourceCodeString)) {
			mapOneToManyForIssueSource(grcIssueSource, sourceCodeString, valuesList);
		}
		else{
			List<Object> sourceType = valuesList.get(0) instanceof List?(List<Object>)valuesList.get(0):valuesList;
			grcIssueSource.setSourceTypeDesc((String)sourceType.get(0));
		}
		grcIssueSource.setCreatedBy(issueProps.getCreatedBy());
		grcIssueSource.setActive(IssueConstants.ACTIVE);
		//grcIssueSource.setTimeCreated(issueProps.getCreatedBy());
		return grcIssueSource;
	}
	
	public boolean isMultiValuedField(String fieldToCheck) {
		boolean rValue = false;
		if (StringConstants.ISSUE_SOURCE_DUE_DILIGENCE_REVIEW_CODE.equals(fieldToCheck) ||
				StringConstants.ISSUE_SOURCE_OTHER_FIRST_LINE_OF_DEFENSE_CODE.equals(fieldToCheck) ||
				StringConstants.ISSUE_SOURCE_COMPLAINTS_MANAGEMENT_PROGRAM_CODE.equals(fieldToCheck) || 
				StringConstants.ISSUE_SOURCE_THIRD_PARTY_REVIEW_CODE.equals(fieldToCheck) ||
				StringConstants.ISSUE_SOURCE_EXTERNAL_AUDIT_REVIEW_CODE.equals(fieldToCheck) ||
				StringConstants.ISSUE_SOURCE_INCIDENT_MANAGEMENT_CODE.equals(fieldToCheck) ||
				StringConstants.ISSUE_SOURCE_REGULATORY_EXAM_CODE.equals(fieldToCheck) ||
				StringConstants.ISSUE_SOURCE_SOX_CODE.equals(fieldToCheck)||
				StringConstants.ISSUE_SOURCE_SECOND_LINE_OF_DEFENCE_IDENTIFIED_CODE.equals(fieldToCheck)) {
			rValue = true;
		}
		return rValue;
	}
	
	private void mapOneToManyForIssueSource(GrcIssueSource grcIssueSource,String sourceCode, Object additionalValuePrimary) {		
		List arrayValues = (List<?>) additionalValuePrimary;
		
		if (StringConstants.ISSUE_SOURCE_INCIDENT_MANAGEMENT_CODE.equals(sourceCode)) {
			Set<IncidentID> incidentIdList = new HashSet<>();
			IncidentID incidentID = null;
			for(Object incidentCode : arrayValues) {
				incidentID = new IncidentID();
				incidentID.setIncidentCode((String)incidentCode);
				incidentID.setActive(IssueConstants.ACTIVE);
				incidentID.setVersion(grcIssueSource.getIssueVersion());
				incidentIdList.add(incidentID);
			}
			grcIssueSource.setGrcIncidentIDS(incidentIdList);			
		}
		else if(StringConstants.ISSUE_SOURCE_COMPLAINTS_MANAGEMENT_PROGRAM_CODE.equals(sourceCode)) {
			Set<ComplaintID> complaintIdList = new HashSet<>();
			ComplaintID complaintID = null;
			for(Object complaintId : arrayValues) {
				complaintID = new ComplaintID();
				complaintID.setComplaintCode((String)complaintId);
				complaintID.setActive(IssueConstants.ACTIVE);
				complaintID.setVersion(grcIssueSource.getIssueVersion());
				complaintID.setIssueSourceCode(grcIssueSource);
				complaintIdList.add(complaintID);
			}
			grcIssueSource.setGrcComplaintIDS(complaintIdList);			
		}
		else if(StringConstants.ISSUE_SOURCE_THIRD_PARTY_REVIEW_CODE.equals(sourceCode)) {
			Set<ThirdPartyReviewName> TPRNameList = new HashSet<>();
			ThirdPartyReviewName TPRName = null;
			for(Object tprCode : arrayValues) {
				TPRName = new ThirdPartyReviewName();
				TPRName.setTPRCode((String)tprCode);
				TPRName.setActive(IssueConstants.ACTIVE);
				TPRName.setVersion(grcIssueSource.getIssueVersion());
				TPRNameList.add(TPRName);
			}
			grcIssueSource.setGrcTPRNames(TPRNameList);			
		}
		else if(StringConstants.ISSUE_SOURCE_EXTERNAL_AUDIT_REVIEW_CODE.equals(sourceCode)) {
			Set<ExternalAuditReviewName> EARNameList = new HashSet<>();
			ExternalAuditReviewName EARName = null;
			for(Object earCode : arrayValues) {
				EARName = new ExternalAuditReviewName();
				EARName.setEARCode((String)earCode);
				EARName.setActive(IssueConstants.ACTIVE);
				EARName.setVersion(grcIssueSource.getIssueVersion());
				EARNameList.add(EARName);
			}
			grcIssueSource.setGrcEARNames(EARNameList);
		}
		else if (StringConstants.ISSUE_SOURCE_OTHER_FIRST_LINE_OF_DEFENSE_CODE.equals(sourceCode)) {
			Set<SourceFunctionalArea> SFANameList = new HashSet<>();
			SourceFunctionalArea SFAName = null;
			for(Object sfaCode : arrayValues) {
				SFAName = new SourceFunctionalArea();
				SFAName.setSFACode((String)sfaCode);
				SFAName.setActive(IssueConstants.ACTIVE);
				SFAName.setVersion(grcIssueSource.getIssueVersion());
				SFANameList.add(SFAName);
			}
			grcIssueSource.setGrcSFANames(SFANameList);			
		}
		else if (StringConstants.ISSUE_SOURCE_DUE_DILIGENCE_REVIEW_CODE.equals(sourceCode)) {
			Set<DueDiligenceReviewName> DDRNameList = new HashSet<>();
			DueDiligenceReviewName DDRName = null;
			for(Object sfaCode : arrayValues) {
				DDRName = new DueDiligenceReviewName();
				DDRName.setDDRCode((String)sfaCode);
				DDRName.setActive(IssueConstants.ACTIVE);
				DDRName.setVersion(grcIssueSource.getIssueVersion());
				DDRNameList.add(DDRName);
			}
			grcIssueSource.setGrcDDRNames(DDRNameList);
		}
		else if (StringConstants.ISSUE_SOURCE_REGULATORY_EXAM_CODE.equals(sourceCode)) {
			Set<RegulatoryExamName> RENameList = new HashSet<>();
			RegulatoryExamName REName = null;
			for(Object sfaCode : arrayValues) {
				REName = new RegulatoryExamName();
				REName.setRENCode((String)sfaCode);
				REName.setActive(IssueConstants.ACTIVE);
				REName.setVersion(grcIssueSource.getIssueVersion());
				RENameList.add(REName);
			}
			grcIssueSource.setGrcRENames(RENameList);
		}
		else if (StringConstants.ISSUE_SOURCE_SOX_CODE.equals(sourceCode)) {
			Set<SOXRName> SOXRNameList = new HashSet<>();
			SOXRName SOXRName = null;
			for(Object sfaCode : arrayValues) {
				SOXRName = new SOXRName();
				SOXRName.setSOXRCode((String)sfaCode);
				SOXRName.setActive(IssueConstants.ACTIVE);
				SOXRName.setVersion(grcIssueSource.getIssueVersion());
				SOXRNameList.add(SOXRName);
			}
			grcIssueSource.setGrcSOXRNames(SOXRNameList);
		}
		else if (StringConstants.ISSUE_SOURCE_SECOND_LINE_OF_DEFENCE_IDENTIFIED_CODE.equals(sourceCode)) {
			Set<SLOD> slodList = new HashSet<>();
			SLOD slod = null;
			for(Object slodCode : arrayValues) {
				slod = new SLOD();
				slod.setSLODCode((String)slodCode);
				slod.setIssueSourceCode(grcIssueSource);
				slod.setActive(IssueConstants.ACTIVE);
				slod.setVersion(grcIssueSource.getIssueVersion());
				slodList.add(slod);
			}
			grcIssueSource.setGrcSLODIDS(slodList);
		}
	}
	
	
	
	public List<Object> getAdditionalValuesForCode(String sourceCodeString, SecondaryIssueSource secondaryIssueSource) {
		List<Object> valuesList = null;
		switch (sourceCodeString) {
		case StringConstants.ISSUE_SOURCE_COMPLAINTS_MANAGEMENT_PROGRAM_CODE:
			valuesList = secondaryIssueSource.getCmp();
			break;
		case StringConstants.ISSUE_SOURCE_DUE_DILIGENCE_REVIEW_CODE:
			valuesList = secondaryIssueSource.getDdr();
			break;
		case StringConstants.ISSUE_SOURCE_EXTERNAL_AUDIT_REVIEW_CODE:
			valuesList = secondaryIssueSource.getEar();
			break;
		case StringConstants.ISSUE_SOURCE_INCIDENT_MANAGEMENT_CODE:
			valuesList = secondaryIssueSource.getIm();
			break;			
		case StringConstants.ISSUE_SOURCE_OTHER_FIRST_LINE_OF_DEFENSE_CODE:
			valuesList = secondaryIssueSource.getOfld();
			break;
		case StringConstants.ISSUE_SOURCE_REGULATORY_EXAM_CODE:
			valuesList = secondaryIssueSource.getRr();
			break;
		case StringConstants.ISSUE_SOURCE_SOX_CODE:
			valuesList = secondaryIssueSource.getSoxr();
			break;
		case StringConstants.ISSUE_SOURCE_THIRD_PARTY_REVIEW_CODE:
			valuesList = secondaryIssueSource.getTpr();
			break;
		case StringConstants.ISSUE_SOURCE_SELF_IDENTIFIED_CODE:
			valuesList = secondaryIssueSource.getSi();
			break;
		case StringConstants.ISSUE_SOURCE_OTHER_CODE:
			valuesList = secondaryIssueSource.getOth();
			break;
		case StringConstants.ISSUE_SOURCE_OUTSOURCING_MANAGEMENT_CODE:
			valuesList = secondaryIssueSource.getOm();
			break;
		case StringConstants.ISSUE_SOURCE_SECOND_LINE_OF_DEFENCE_IDENTIFIED_CODE:
			valuesList = secondaryIssueSource.getSldi();
			break;
		default:
			valuesList = null;
			break;
		}		
		return valuesList;
	}
	
	public PrimaryIssueSource convertPrimaryIssueSourceListToResponse(List<GrcIssueSource> grcIssueSourceList) {
		PrimaryIssueSource primaryIssueSource = new PrimaryIssueSource();
		
		List<GrcIssueSource> primaryIssueSourceList = grcIssueSourceList.stream().
				filter( grcIssueSource -> StringConstants.YES.equals(grcIssueSource.getIsPrimary())).
				collect(Collectors.toList());
		for(GrcIssueSource issueSource:primaryIssueSourceList){
			String sourceCode = issueSource.getSourceType();
			primaryIssueSource.setSourceCode(Arrays.asList(sourceCode));
			Object list = null;
			if(isMultiValuedField(sourceCode)) {
				switch (sourceCode) {
				case StringConstants.ISSUE_SOURCE_COMPLAINTS_MANAGEMENT_PROGRAM_CODE:
					list = issueSource.getGrcComplaintIDS().stream().map(entity->entity.getComplaintCode()).collect(Collectors.toList());
					break;
				case StringConstants.ISSUE_SOURCE_INCIDENT_MANAGEMENT_CODE:
					list = issueSource.getGrcIncidentIDS().stream().map(entity->entity.getIncidentCode()).collect(Collectors.toList());;
					break;
				case StringConstants.ISSUE_SOURCE_OTHER_FIRST_LINE_OF_DEFENSE_CODE:
					list = issueSource.getGrcSFANames().stream().map(entity->entity.getSFACode()).collect(Collectors.toList());;
					break;
				case StringConstants.ISSUE_SOURCE_THIRD_PARTY_REVIEW_CODE:
					list = issueSource.getGrcTPRNames().stream().map(entity->entity.getTPRCode()).collect(Collectors.toList());;
					break;
				case StringConstants.ISSUE_SOURCE_REGULATORY_EXAM_CODE:
					list = issueSource.getGrcRENames().stream().map(entity->entity.getRENCode()).collect(Collectors.toList());;
					break;
				case StringConstants.ISSUE_SOURCE_EXTERNAL_AUDIT_REVIEW_CODE:
					list = issueSource.getGrcEARNames().stream().map(entity->entity.getEARCode()).collect(Collectors.toList());;
					break;
				case StringConstants.ISSUE_SOURCE_DUE_DILIGENCE_REVIEW_CODE:
					list = issueSource.getGrcDDRNames().stream().map(entity->entity.getDDRCode()).collect(Collectors.toList());;
					break;
				case StringConstants.ISSUE_SOURCE_SOX_CODE:
					list = issueSource.getGrcSOXRNames().stream().map(entity->entity.getSOXRCode()).collect(Collectors.toList());;
					break;
				case StringConstants.ISSUE_SOURCE_SECOND_LINE_OF_DEFENCE_IDENTIFIED_CODE:
					list = issueSource.getGrcSLODIDS().stream().map(entity->entity.getSLODCode()).collect(Collectors.toList());
					break;
				}
				primaryIssueSource.setAdditionalValue(list);
			}
			else {
				primaryIssueSource.setAdditionalValue(issueSource.getSourceTypeDesc());
			}			
		}		
		return primaryIssueSource;		
	}
	
	public SecondaryIssueSource convertSecondaryIssueSourceListToResponse(List<GrcIssueSource> grcIssueSourceList) {
		SecondaryIssueSource secondaryIssueSource = new SecondaryIssueSource();
		List<GrcIssueSource> secondaryIssueSourceList = grcIssueSourceList.stream().
				filter( grcIssueSource -> StringConstants.NO.equals(grcIssueSource.getIsPrimary())).
				collect(Collectors.toList());
		for(GrcIssueSource issueSource:secondaryIssueSourceList){
			String sourceCode = issueSource.getSourceType();			
			if(secondaryIssueSource.getSourceCode()!=null) {
				secondaryIssueSource.getSourceCode().add(sourceCode);
			}
			else {
				List<Object> sourceCodeList = new ArrayList<>();
				sourceCodeList.add(sourceCode);
				secondaryIssueSource.setSourceCode(sourceCodeList);
			}
			List<Object> list = null;
			switch (sourceCode) {
			case StringConstants.ISSUE_SOURCE_COMPLAINTS_MANAGEMENT_PROGRAM_CODE:
				list = issueSource.getGrcComplaintIDS().stream().map(entity->entity.getComplaintCode()).collect(Collectors.toList());
				secondaryIssueSource.setCmp(list);
				break;
			case StringConstants.ISSUE_SOURCE_INCIDENT_MANAGEMENT_CODE:
				list = issueSource.getGrcIncidentIDS().stream().map(entity->entity.getIncidentCode()).collect(Collectors.toList());
				secondaryIssueSource.setIm(list);
				break;
			case StringConstants.ISSUE_SOURCE_OTHER_FIRST_LINE_OF_DEFENSE_CODE:
				list = issueSource.getGrcSFANames().stream().map(entity->entity.getSFACode()).collect(Collectors.toList());
				secondaryIssueSource.setOfld(list);
				break;
			case StringConstants.ISSUE_SOURCE_THIRD_PARTY_REVIEW_CODE:
				list = issueSource.getGrcTPRNames().stream().map(entity->entity.getTPRCode()).collect(Collectors.toList());
				secondaryIssueSource.setTpr(list);
				break;
			case StringConstants.ISSUE_SOURCE_REGULATORY_EXAM_CODE:
				list = issueSource.getGrcRENames().stream().map(entity->entity.getRENCode()).collect(Collectors.toList());
				secondaryIssueSource.setRr(list);
				break;
			case StringConstants.ISSUE_SOURCE_EXTERNAL_AUDIT_REVIEW_CODE:
				list = issueSource.getGrcEARNames().stream().map(entity->entity.getEARCode()).collect(Collectors.toList());
				secondaryIssueSource.setEar(list);
				break;
			case StringConstants.ISSUE_SOURCE_DUE_DILIGENCE_REVIEW_CODE:
				list = issueSource.getGrcDDRNames().stream().map(entity->entity.getDDRCode()).collect(Collectors.toList());
				secondaryIssueSource.setDdr(list);
				break;
			case StringConstants.ISSUE_SOURCE_SOX_CODE:
				list = issueSource.getGrcSOXRNames().stream().map(entity->entity.getSOXRCode()).collect(Collectors.toList());
				secondaryIssueSource.setSoxr(list);
				break;
			case StringConstants.ISSUE_SOURCE_SECOND_LINE_OF_DEFENCE_IDENTIFIED_CODE:
				list = issueSource.getGrcSLODIDS().stream().map(entity->entity.getSLODCode()).collect(Collectors.toList());
				secondaryIssueSource.setSldi(list);
				break;
				
			default:
				list = new ArrayList<>();
				list.add(issueSource.getSourceTypeDesc());
				switch (sourceCode) {
				case StringConstants.ISSUE_SOURCE_SELF_IDENTIFIED_CODE:
					secondaryIssueSource.setSi(list);
					break;
				case StringConstants.ISSUE_SOURCE_OTHER_CODE:
					secondaryIssueSource.setOth(list);
					break;
				case StringConstants.ISSUE_SOURCE_OUTSOURCING_MANAGEMENT_CODE:
					secondaryIssueSource.setOm(list);
					break;
				case StringConstants.ISSUE_SOURCE_SECOND_LINE_OF_DEFENCE_IDENTIFIED_CODE:
					secondaryIssueSource.setSldi(list);
					break;				
				}				
				break;
			}		
		}		
		return secondaryIssueSource;
	}

	public List<GrcIssueAolMap> getAreaOfLaws(IssueMetaData issueProps, AreaOfLaw areaOfLaw, String operation) {
		List<GrcIssueAolMap> grcIssueAolMaps = new ArrayList<GrcIssueAolMap>();
		BigDecimal time = new BigDecimal(System.currentTimeMillis() / 1000);
		if(operation.equals("CREATE")){
			SubAol subAol = areaOfLaw.getSubAol();	

			GrcIssueAolMap grcIssueAolMap = new GrcIssueAolMap();
			grcIssueAolMap.setAolCode(areaOfLaw.getAolCode());
			grcIssueAolMap.setIssueCode(issueProps.getIssueCode()); 
			if (subAol != null) {
				grcIssueAolMap.setSaolCode(subAol.getSaolCode());
			}			
			grcIssueAolMap.setCreatedBy(issueProps.getCreatedBy());
			grcIssueAolMap.setTimeCreated(time);
			grcIssueAolMap.setIsActive(IssueConstants.ACTIVE);
			grcIssueAolMap.setVersion(issueProps.getVersion());
			grcIssueAolMaps.add(grcIssueAolMap);
		} else if (operation.equals("UPDATE")){

			GrcIssueAolMap issueAol = aolMapDao.findByIssueId(issueProps.getIssueCode(),IssueConstants.ACTIVE,IssueConstants.DEFAULT_VERSION);
			GrcIssueAolMap grcIssueAolMap = new GrcIssueAolMap();
			SubAol subAol = areaOfLaw.getSubAol();
			if(areaOfLaw.getAolCode() != issueAol.getAolCode()){
				grcIssueAolMap.setAolCode(areaOfLaw.getAolCode());
				grcIssueAolMap.setIssueCode(issueProps.getIssueCode());
				if (subAol != null) {
					grcIssueAolMap.setSaolCode(subAol.getSaolCode());
				}
				grcIssueAolMap.setUpdatedBy(issueProps.getUpdatedBy());
				grcIssueAolMap.setTimeUpdated(time);
				grcIssueAolMap.setIsActive(IssueConstants.ACTIVE);
				grcIssueAolMap.setVersion(issueProps.getVersion());
				grcIssueAolMaps.add(grcIssueAolMap);

				//For setting active = 0 for previous AOL
				if(issueAol.getAolCode() != null && !issueAol.getAolCode().isEmpty()){
					grcIssueAolMap.setIssueCode(issueAol.getIssueCode());
					grcIssueAolMap.setAolCode(issueAol.getAolCode());
					grcIssueAolMap.setAolMapCode(issueAol.getAolMapCode());
					grcIssueAolMap.setSaolCode(issueAol.getSaolCode());
					grcIssueAolMap.setUpdatedBy(issueProps.getUpdatedBy());
					grcIssueAolMap.setTimeUpdated(time);
					grcIssueAolMap.setIsActive(IssueConstants.INACTIVE);
					grcIssueAolMaps.add(grcIssueAolMap);
				}

			} else if (areaOfLaw.getAolCode() == issueAol.getAolCode() && areaOfLaw.getSubAol().getSaolCode() != issueAol.getSaolCode()) {
				grcIssueAolMap.setAolCode(areaOfLaw.getAolCode());
				grcIssueAolMap.setIssueCode(issueProps.getIssueCode());
				if (subAol != null) {
					grcIssueAolMap.setSaolCode(subAol.getSaolCode());
				}
				grcIssueAolMap.setUpdatedBy(issueProps.getUpdatedBy());
				grcIssueAolMap.setTimeUpdated(time);
				grcIssueAolMap.setIsActive(IssueConstants.ACTIVE);
				grcIssueAolMap.setVersion(issueProps.getVersion());
				grcIssueAolMaps.add(grcIssueAolMap);

				//For setting active = 0 for previous AOL
				if(issueAol.getAolCode() != null && !issueAol.getAolCode().isEmpty()){
					grcIssueAolMap.setIssueCode(issueAol.getIssueCode());
					grcIssueAolMap.setAolCode(issueAol.getAolCode());
					grcIssueAolMap.setAolMapCode(issueAol.getAolMapCode());
					grcIssueAolMap.setSaolCode(issueAol.getSaolCode());
					grcIssueAolMap.setUpdatedBy(issueProps.getUpdatedBy());
					grcIssueAolMap.setTimeUpdated(time);
					grcIssueAolMap.setIsActive(IssueConstants.INACTIVE);
					grcIssueAolMaps.add(grcIssueAolMap);
				}
			}
		}
		return grcIssueAolMaps;
	}
	

	public GrcIssueResponseMap getIssueResponse(GrcIssue grcIssue, String issueResponse) {
		GrcIssueResponseMap grcIssueResponseMap = new GrcIssueResponseMap();
		grcIssueResponseMap.setActive("1");
		grcIssueResponseMap.setVersion("1");
		//grcIssueResponseMap.setGrcIssue(grcIssue);
		grcIssueResponseMap.setResponseCode(issueResponse);
		return grcIssueResponseMap;
	}	
	
	public List<GrcIssueInfoStakeholders> convertUpdateStakeholdersTODB(GrcIssues issue, List<GrcIssueInfoStakeholders> infoSHMembersSet) {

		String issueId = issue.getIssueCode();
		//List<GrcIssueInfoStakeholders> infoSHMembersSet = issueISHDao.findByIssueId(issue.getIssueCode());
		List<InformedStakeholder> addStakeholders = issue.getInformedStakeholders();
        List<String> stakeholderToRemove = new ArrayList<>();
        List<String> stakeholderToAdd = new ArrayList<>();
        
        for(InformedStakeholder stakeholder : addStakeholders){
            stakeholderToAdd.add(stakeholder.getUserName());
        }
        for (GrcIssueInfoStakeholders membersMap: infoSHMembersSet) {
            String membersMapName = membersMap.getUserName().getUserName();
            if (!stakeholderToAdd.contains(membersMapName)) {
                stakeholderToRemove.add(membersMapName);
            }
        }

        for (String stakeholders : stakeholderToRemove) {
            GrcIssueInfoStakeholders ishMembers = issueISHDao.findByUserNameId(stakeholders, issueId);

            if (ishMembers != null) {
            	ishMembers.setActive("0");
            	//ishMembers.setTimeUpdated(time);
            	//ishMembers.setUpdatedBy(updatedBy);
            }
            infoSHMembersSet.add(ishMembers);
        }

        for(InformedStakeholder stakeholder:addStakeholders) {

            if(issueISHDao.findByUserNameId(stakeholder.getUserName(), issueId) == null){
            	GrcIssueInfoStakeholders ishMembers = new GrcIssueInfoStakeholders();

            	GrcIssue grcIssues = new GrcIssue();
            	grcIssues.setIssueId(issue.getIssueCode());

                ishMembers.setIssueId(issueId);
                ishMembers.setUserName(userDao.findByUserName(stakeholder.getUserName()));
                ishMembers.setActive("1");
                //ishMembers.setCreatedBy(updatedBy);
                //ishMembers.setUpdatedBy(updatedBy);
                //ishMembers.setTimeCreated(time);
                //ishMembers.setTimeUpdated(time);
                infoSHMembersSet.add(ishMembers);
            } else {
                GrcIssueInfoStakeholders members = issueISHDao.findByUserNameId(stakeholder.getUserName(), issueId);
                members.setActive("1");
                members.setUserName(userDao.findByUserName(stakeholder.getUserName()));
                //members.setTimeUpdated(time);
                //members.setUpdatedBy(updatedBy);
                infoSHMembersSet.add(members);
            }

        }
		return infoSHMembersSet;
	    
	}
	
	
	public InformedStakeholder convertInfoStaketoDto(GrcIssueInfoStakeholders entity){
		/*InfoStakeHolder info=new InfoStakeHolder();
		info.setActive(entity.getActive());
		info.setId(entity.getId());
		info.setIssueId(entity.getIssueId());
		info.setUserName(entity.getUserName());
		info.setVersion(entity.getVersion());
		return info;*/
		InformedStakeholder info=new InformedStakeholder();
		info.setActive(entity.getActive());
		info.setUserName(entity.getUserName().getEmail());
		info.setIssueCode(entity.getId().toString());
		return info;
	}
	public List<GrcIssueIfaMap> convertIfasTODB(IssueMetaData issueProps, List<Object> ifas) {
		BigDecimal time = new BigDecimal(System.currentTimeMillis() / 1000);
		List<GrcIssueIfaMap> issueIfas = new ArrayList<>();

		for (Object ifa : ifas){
			GrcIssueIfaMap issueIfa = new GrcIssueIfaMap();

			issueIfa.setIssueCode(issueProps.getIssueCode());
			issueIfa.setIfaCode(ifa.toString());
			issueIfa.setCreatedBy(issueProps.getCreatedBy());
			issueIfa.setIsActive(IssueConstants.ACTIVE);
			issueIfa.setVersion(issueProps.getVersion());
			issueIfa.setTimeCreated(time);
			issueIfas.add(issueIfa);
		}
		return issueIfas;
	}

	public GrcIssueRiskMap convertRiskCategoryTODB(IssueMetaData issueProps, RiskCategory riskCategory) {
		BigDecimal time = new BigDecimal(System.currentTimeMillis() / 1000);
		GrcIssueRiskMap issueRiskCat = null;
		if (riskCategory != null && riskCategory.getRiskCategory() != null) {
			issueRiskCat = new GrcIssueRiskMap();
			issueRiskCat.setIssueCode(issueProps.getIssueCode());
			issueRiskCat.setRiskCode(riskCategory.getRiskCategory());
			issueRiskCat.setRiskTypeCode(riskCategory.getRiskType());
			issueRiskCat.setRiskSubTypeCode(riskCategory.getRiskSubType());
			issueRiskCat.setIsPrimary(riskCategory.getIsPrimary());
			issueRiskCat.setCreatedBy(issueProps.getCreatedBy());
			issueRiskCat.setTimeCreated(time);
			issueRiskCat.setVersion(issueProps.getVersion());
			issueRiskCat.setIsActive(IssueConstants.ACTIVE);
		}
		return issueRiskCat;
	}
	
	public IssueSourceType convertIssueSourceTypeEntityToDto(GrcIssueSourceType entity){
		IssueSourceType dto=new IssueSourceType();
		dto.setSourceCode(entity.getIssueSourceID());
		dto.setSourceName(entity.getIssueSource());
		dto.setSourceDesc(entity.getIssueSourceDesc());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setUpdateBy(entity.getUpdatedBy());
		dto.setTimeCreated(entity.getTimeCreated());
		dto.setTimeUpdated(entity.getTimeUpdated());
		return dto;
	}

	public GrcIssueProductMap getProductMap(IssueMetaData issueProps,
                                            List<GrcIssueProduct> products,
                                            GrcFeatures feature) {
	    GrcIssueProductMap map = new GrcIssueProductMap();

	    if (products != null)
	    for (GrcIssueProduct product : products) {
            Set<GrcIssueCapabilities> issueCapabilities = product.getIssueCapabilities();
            if(issueCapabilities != null)
            for (GrcIssueCapabilities capabilities : issueCapabilities) {
                Set<GrcFeatures> issueFeatures = capabilities.getIssueFeatures();
                if(issueFeatures != null)
                for (GrcFeatures temp : issueFeatures) {
                    if (temp.getFeatureCode().equals(feature.getFeatureCode())) {
                        map.setActive(IssueConstants.ACTIVE);
                        map.setVersion(issueProps.getVersion());
                        map.setProductCode(product.getProductCode());
                        map.setCapabilityCode(capabilities.getCapabilityCode());
                        map.setFeatureCode(temp.getFeatureCode());
                        map.setIssueCode(issueProps.getIssueCode());
                    }
                }
            }
        }

	    return map;
    }

    public GrcIssueBpcMap getBPCMap(GrcBpcCategory category, IssueMetaData issueProps, List<GrcBpcBusiness> bpcs) {
	    GrcIssueBpcMap map = new GrcIssueBpcMap();

	    if(bpcs!= null)
	        for(GrcBpcBusiness bpc : bpcs) {
                Set<GrcBpcProcess> businessProcess = bpc.getBusinessProcess();
                if(businessProcess != null)
                for (GrcBpcProcess process : businessProcess) {
                    Set<GrcBpcCategory> categoryList = process.getCategoryList();
                    if(categoryList != null)
                    for (GrcBpcCategory temp : categoryList) {
                        if(temp.getCategoryCode().equals(category.getCategoryCode())) {
                            map.setActive(IssueConstants.ACTIVE);
                            map.setVersion(issueProps.getVersion());
                            map.setBpcCode(bpc.getBpcCode());
                            map.setCategoryCode(temp.getCategoryCode());
                            map.setIssueCode(issueProps.getIssueCode());
                            map.setProcessCode(process.getProcessCode());
                        }
                    }
                }
            }
        return map;
    }

    
	public IssueProduct convertProductEntityListToDtoList(GrcIssueProduct entity){
		IssueProduct dto=new IssueProduct();
		dto.setProductCode(entity.getProductCode());
		dto.setProductDetails(entity.getProductDetails());
		dto.setCapabilities(convertCapabilityEntityToDto(entity.getIssueCapabilities()));
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setUpdatedBy(entity.getUpdatedBy());
		dto.setTimeCreated(entity.getTimeCreated());
		dto.setTimeUpdated(entity.getTimeUpdated());
		return dto;
	}

	public List<GrcCapabilities> convertCapabilityEntityToDto(Set<GrcIssueCapabilities> entitySet){
		List<GrcCapabilities> list=new ArrayList<GrcCapabilities>();
		for(GrcIssueCapabilities entity:entitySet){
			GrcCapabilities dto=new GrcCapabilities();
			dto.setCapabilityCode(entity.getCapabilityCode());
			dto.setCapabilityDetails(entity.getCapabilityDetail());
			dto.setCreatedBy(entity.getCreatedBy());
			dto.setUpdatedBy(entity.getUpdatedBy());
			dto.setTimeCreated(entity.getTimeCreated());
			dto.setTimeUpdated(entity.getTimeUpdated());
			dto.setFeature(convertFeatureEntityToDto(entity.getIssueFeatures()));
			list.add(dto);
		}
		return list;
	}


	public List<Feature> convertFeatureEntityToDto(Set<GrcFeatures> entity){
		List<Feature> list = new ArrayList<>();
		for (GrcFeatures feature : entity){
			Feature dto=new Feature();
			dto.setFeatureCode(feature.getFeatureCode());
			dto.setFeatureDetails(feature.getFeatureDetail());
			list.add(dto);
		}
		return list;
	}

	public Bpc convertBPCEntityListToDtoList(GrcBpcBusiness entity){
		Bpc dto=new Bpc();
		dto.setBpcCode(entity.getBpcCode());
		dto.setBpcDetails(entity.getBpcDetails());
		dto.setBusinessProcess(convertProcessEntityToDto(entity.getBusinessProcess()));
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setUpdatedBy(entity.getUpdatedBy());
		dto.setTimeCreated(entity.getTimeCreated());
		dto.setTimeUpdated(entity.getTimeUpdated());
		return dto;
	}

	public List<BusinessProcess> convertProcessEntityToDto(Set<GrcBpcProcess> entityList){
		List<BusinessProcess> list=new ArrayList<BusinessProcess>();
		for(GrcBpcProcess entity:entityList){
			BusinessProcess dto=new BusinessProcess();
			dto.setProcessCode(entity.getProcessCode());
			dto.setProcessDetails(entity.getProcessDetails());
			dto.setCreatedBy(entity.getCreatedBy());
			dto.setUpdatedBy(entity.getUpdatedBy());
			dto.setTimeCreated(entity.getTimeCreated());
			dto.setTimeUpdated(entity.getTimeUpdated());
			dto.setBusinessProcessCategory(convertCategoryEntityToDto(entity.getCategoryList()));
			list.add(dto);
		}
		return list;
	}

	public List<BusinessProcessCategory> convertCategoryEntityToDto(Set<GrcBpcCategory> entityList){
		List<BusinessProcessCategory> list = new ArrayList<>();
		for (GrcBpcCategory entity : entityList){
			BusinessProcessCategory dto=new BusinessProcessCategory();
			dto.setCategoryCode(entity.getCategoryCode());
			dto.setCategoryDetails(entity.getCategoryDetails());
			list.add(dto);
		}
		return list;
	}

	public GrcIssues convertDBTODashboardRequest(GrcIssue issue) {
		GrcIssues grcIssue = new GrcIssues();
		grcIssue.setIssueCode(issue.getIssueId());
        grcIssue.setIssueName(issue.getIssueName());
        grcIssue.setCurrentDueDate(DateUtil.convertToDate(issue.getCurrentDueDate()));
        grcIssue.setIssueRating(convertToIssueRating(issue.getIssueRating()));

        ErcdbaGrcFunctionalArea issueFaCode = new ErcdbaGrcFunctionalArea();
        GrcFunctionalArea faDO = faDao.findById(issue.getFunctionalAreaCode());
        issueFaCode.setFaName(faDO.getFunctionalAreaName());
        grcIssue.setFaCode(issueFaCode);

        if(issue.getIsComplianceRelated() != null){
        	grcIssue.setIsComplianceReleated(issue.getIsComplianceRelated());
        }

        GrcUser issueOwner = new GrcUser();
        if(issue.getIssueOwner() != null && !issue.getIssueOwner().isEmpty()){
	        com.paypal.erc.grc.common.dal.models.GrcUser ownerDetails = userDao.findByUserName(issue.getIssueOwner());
	        issueOwner.setFullName(ownerDetails.getLastName().concat(", ".concat(ownerDetails.getFirstName())));
        }
        issueOwner.setUserName(issue.getIssueOwner());
        grcIssue.setIssueOwner(issueOwner);

    	List<GrcIssueRiskMap> grcRiskCategoryList = riskCatDao.findPrimaryByIssueId(issue.getIssueId());
    	if(grcRiskCategoryList != null){
    		List<RiskCategory> riskCategoryList = new ArrayList<>();
    		for(GrcIssueRiskMap grcRiskCategory:grcRiskCategoryList) {
    			RiskCategory riskCategory = new RiskCategory();
    	    	riskCategory.setRiskCategory(riskCatDao.findByRiskCode(grcRiskCategory.getRiskCode()).getRiskName());
    	    	riskCategoryList.add(riskCategory);    	    	
    		}
    		grcIssue.setRiskCategory(riskCategoryList);
    	}

        GrcWfStatus grcWf = wfStatusHome.findByStatusCode(issue.getGrcWfStatus().getStatusCode());
        if (grcWf != null){
        	Status gwStatus = new Status();
            gwStatus.setCode(grcWf.getStatusCode());
            gwStatus.setName(grcWf.getStatusName());
            grcIssue.setStatusCode(gwStatus);
        }

        List<InformedStakeholder> issueStakeholders = new ArrayList<InformedStakeholder>();
        List<GrcIssueInfoStakeholders> stakeholders = issueISHDao.findByIssueId(issue.getIssueId(),IssueConstants.ACTIVE, IssueConstants.DEFAULT_VERSION);
        for(GrcIssueInfoStakeholders stakeholder : stakeholders){
        	InformedStakeholder issueStakeholder = new InformedStakeholder();
        	issueStakeholder.setUserName(stakeholder.getUserName().getUserName());
        	issueStakeholders.add(issueStakeholder);
        }
        grcIssue.setInformedStakeholders(issueStakeholders);

		return grcIssue;
	}
	
	public List<GrcBpcCategory> convertRequestToDBBPC(List<String> categories) {
        List<GrcBpcCategory> categoryList = new ArrayList<>(10);
        if (categories != null) {
            for (String feature : categories) {
                String[] values = feature.split(":");
                GrcBpcCategory dbObj = new GrcBpcCategory();
                dbObj.setCategoryCode(values[0]);
                categoryList.add(dbObj);
            }
        }
        return categoryList;
    }
	
	public List<GrcFeatures> convertRequestToDb(List<String> features) {
	    List<GrcFeatures> featuresList = new ArrayList<>(10);
	    if (features != null) {
            for (String feature : features) {
                String[] values = feature.split(":");
                GrcFeatures dbObj = new GrcFeatures();
                dbObj.setFeatureCode(values[0]);
                featuresList.add(dbObj);
            }
        }
        return featuresList;
    }
	
	public List<GrcIssueProvince> convertRequestToDB(List<String> issueRegion) {
		List<GrcIssueProvince> provinces = new ArrayList<>(10);
        if(issueRegion != null) {
            for (String region : issueRegion) {
                String[] values = region.split(":");
                GrcIssueProvince province = new GrcIssueProvince();
                province.setProvinceCode(Long.valueOf(values[0]));
                provinces.add(province);
            }
        }
		return provinces;
	}
	
	public IssueException convertExceptionEntityToRequest(GrcIssueException grcIssueException) {
		IssueException issueException = new IssueException();
		issueException.setIssueCode(grcIssueException.getIssueCode());
		issueException.setExceptionCode(grcIssueException.getExceptionCode());
		
		issueException.setBaaUserOfflineDocCode(grcIssueException.getBaaUserOfflineDocCode());
		issueException.setIsBaaUserOnline(grcIssueException.getIsBAAUserOnline());
		issueException.setGgrcUserOfflineDocCode(grcIssueException.getGgrcUserOfflineDocCode());
		issueException.setIsGgrcUserOnline(grcIssueException.getIsGGRCUserOnline());
		issueException.setExceptionDesc(grcIssueException.getExceptionDescription());
		issueException.setOldValue(grcIssueException.getOldValue());
		issueException.setNewValue(grcIssueException.getNewValue());
		issueException.setIssueVersion(grcIssueException.getIssueVersion());
		issueException.setTimeCreated(grcIssueException.getTimeCreated());
		issueException.setCreatedBy(grcIssueException.getCreatedBy());
		issueException.setTimeUpdated(grcIssueException.getTimeUpdated());
		issueException.setUpdateBy(grcIssueException.getUpdatedBy());
		
		issueException.setStatus(grcIssueException.getStatus());
		issueException.setFieldName(grcIssueException.getExceptionField());
		issueException.setApprovedBy(grcIssueException.getApprovedBy());
		if(grcIssueException.getApprovedDate()!=null)
			issueException.setApprovalDate(DateUtil.convertToDate(new BigDecimal(grcIssueException.getApprovedDate())));
		issueException.setBaaApprover(grcIssueException.getBaaApprover());
		if(grcIssueException.getBaaApproverDate()!=null)
			issueException.setBaaApprovalDate(DateUtil.convertToDate(new BigDecimal(grcIssueException.getBaaApproverDate())));
		issueException.setGgrcApprover(grcIssueException.getGgrcApprover());
		if(grcIssueException.getGgrcApproverDate()!=null)
			issueException.setGgrcApprovalDate(DateUtil.convertToDate(new BigDecimal(grcIssueException.getGgrcApproverDate())));
		
		
		return issueException;
	}
	
	public GrcIssueException convertExceptionRequestToEntity(IssueException issueException) {
		GrcIssueException grcIssueException = new GrcIssueException();
		grcIssueException.setIssueCode(issueException.getIssueCode());
		grcIssueException.setExceptionCode(issueException.getExceptionCode());
		
		if(issueException.getExceptionCode() == null) {
			BigDecimal sequence = null;
			try {
				sequence = issueDao.getNextValForExceptionSeq();
				grcIssueException.setExceptionCode("EX_"+sequence);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		grcIssueException.setBaaUserOfflineDocCode(issueException.getBaaUserOfflineDocCode());
		grcIssueException.setIsBAAUserOnline(issueException.getIsBaaUserOnline());
		grcIssueException.setGgrcUserOfflineDocCode(issueException.getGgrcUserOfflineDocCode());
		grcIssueException.setIsGGRCUserOnline(issueException.getIsGgrcUserOnline());
		grcIssueException.setExceptionDescription(issueException.getExceptionDesc());
		grcIssueException.setOldValue(issueException.getOldValue());
		grcIssueException.setNewValue(issueException.getNewValue());		
		
		grcIssueException.setExceptionField(issueException.getFieldName());
		grcIssueException.setApprovedBy(issueException.getApprovedBy());
//		if(issueException.getApprovalDate()!=null)
//			grcIssueException.setApprovedDate(DateUtil.convertToEpoch(issueException.getApprovalDate()).toString());
		
		Long time = System.currentTimeMillis()/1000;
		if(IssueConstants.APPROVER_ISSUE_APPROVER.equals(issueException.getCurrentActionBy())){
			grcIssueException.setApprovedDate(time.toString());
		}
		grcIssueException.setBaaApprover(issueException.getBaaApprover());
		if(issueException.getBaaApprovalDate()!=null && !IssueConstants.APPROVER_BAA_APPROVER.equals(issueException.getCurrentActionBy()))
			grcIssueException.setBaaApproverDate(DateUtil.convertToEpoch(issueException.getBaaApprovalDate()).toString());
		if(IssueConstants.APPROVER_BAA_APPROVER.equals(issueException.getCurrentActionBy())){
			grcIssueException.setBaaApproverDate(time.toString());
		}
		grcIssueException.setGgrcApprover(issueException.getGgrcApprover());
		if(issueException.getGgrcApprovalDate()!=null && !IssueConstants.APPROVER_GGRC_APPROVER.equals(issueException.getCurrentActionBy()))
			grcIssueException.setGgrcApproverDate(DateUtil.convertToEpoch(issueException.getGgrcApprovalDate()).toString());
		if(IssueConstants.APPROVER_GGRC_APPROVER.equals(issueException.getCurrentActionBy())){
			grcIssueException.setGgrcApproverDate(time.toString());
		}
		
		String status = IssueConstants.PENDING_DESC;
		if("0".equals(issueException.getStatus())) {
			status = IssueConstants.REJECTED_DESC;
		}
		else if("1".equals(issueException.getStatus()) && issueException.getApprovalDate()!=null &&
				issueException.getBaaApprovalDate()!=null && issueException.getGgrcApprovalDate()!=null) {
			status = IssueConstants.APPROVED_DESC;
		}
		grcIssueException.setStatus(status);
		return grcIssueException;
	}
	
	public GrcIssue moveLatestToIssueDB(GrcIssueUpdate issueDO){
		GrcIssue grcIssue = new GrcIssue();
		grcIssue.setIssueId(issueDO.getIssueId());
		 grcIssue.setIssueName(issueDO.getIssueName());
		 grcIssue.setExecutiveSummary(issueDO.getExecutiveSummary());
		 grcIssue.setIssueDescription(issueDO.getIssueDescription());
		 grcIssue.setIsRepeated(issueDO.getIsRepeated());
		 grcIssue.setIsConsumerImpact(issueDO.getIsConsumerImpact());
		 grcIssue.setIssueOwner(issueDO.getIssueOwner());
		 grcIssue.setFunctionalAreaCode(issueDO.getFunctionalAreaCode());
		 grcIssue.setIssueOwnerDelegate(issueDO.getIssueOwnerDelegate());
		 grcIssue.setIssueExecutiveOwner(issueDO.getIssueExecutiveOwner());
		 grcIssue.setIssueApprover(issueDO.getIssueApprover());
		 grcIssue.setIssueIcp(issueDO.getIssueIcp());
		 grcIssue.setBuSme(issueDO.getBuSme());
		 grcIssue.setIssueRco(issueDO.getIssueRco());
		grcIssue.setGrcRcoLeader(issueDO.getGrcRcoLeader());
		 grcIssue.setIsComplianceRelated(issueDO.getIsComplianceRelated());
		 grcIssue.setIssueIdentifiedDate(issueDO.getIssueIdentifiedDate());
		 grcIssue.setFiADueDate(issueDO.getFiADueDate());
		 grcIssue.setFiaDescription(issueDO.getFiaDescription());
		 grcIssue.setFiaCompletedDate(issueDO.getFiaCompletedDate());
		 grcIssue.setCessationDueDate(issueDO.getCessationDueDate());
		 grcIssue.setCessationCompletedOnDate(issueDO.getCessationCompletedOnDate());
		 grcIssue.setImpactAnalysis(issueDO.getImpactAnalysis());
		 grcIssue.setRootCauseAnalysis(issueDO.getRootCauseAnalysis());
		 grcIssue.setIssueRating(issueDO.getIssueRating());
		 grcIssue.setExternalIssueDetails(issueDO.getExternalIssueDetails());
		 grcIssue.setRatingRationale(issueDO.getRatingRationale());
		 grcIssue.setIssueProgressPercent(issueDO.getIssueProgressPercent());
		 grcIssue.setIssueHealth(issueDO.getIssueHealth());
		 grcIssue.setCreatedDate(issueDO.getCreatedDate());
		 grcIssue.setCreatedBy(issueDO.getCreatedBy());
		 grcIssue.setUpdatedBy(issueDO.getUpdatedBy());
		 grcIssue.setUpdatedDate(issueDO.getUpdatedDate());
		 grcIssue.setApprovedDate(issueDO.getApprovedDate());
		 grcIssue.setClosedDate(issueDO.getClosedDate());
		 grcIssue.setIsRestricted(issueDO.getIsRestricted());
		 grcIssue.setCurrentDueDate(issueDO.getCurrentDueDate());
		 grcIssue.setIssueResponse(issueDO.getIssueResponse());
		 grcIssue.setLoeCode(issueDO.getLoeCode());
		 grcIssue.setNextReviewDate(issueDO.getNextReviewDate());
		 grcIssue.setReviewFrequency(issueDO.getReviewFrequency());
		 grcIssue.setHasIFA(issueDO.getHasIFA());
		 grcIssue.setActive(issueDO.getActive());
		 grcIssue.setVersion(issueDO.getVersion());
		 
		 return grcIssue;
	}
	
	public GrcIssueException convertToExceptionfromHistory(GrcIssueHistory grcIssueHistory) throws Exception {
		GrcIssueException issueException = new GrcIssueException();		
		
		BigDecimal sequence = issueDao.getNextValForExceptionSeq();		
		issueException.setExceptionCode("EX."+sequence);
		issueException.setIssueCode(grcIssueHistory.getIssueCode());
		issueException.setOldValue(grcIssueHistory.getOldValue());
		issueException.setNewValue(grcIssueHistory.getNewValue());
		issueException.setTimeCreated(grcIssueHistory.getTimeCreated());
		issueException.setCreatedBy(grcIssueHistory.getCreatedBy());
		return issueException;		
	}
}


