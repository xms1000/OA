package com.oa.action;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.oa.listenner.Persistence;
import com.oa.model.Document;
import com.oa.model.PaymentPlan;
import com.oa.model.PurchaseOrderRegisiter;
import com.oa.model.SalesAgreement;
import com.oa.model.Users;
import com.oa.service.DocumentService;
import com.oa.service.PaymentPlanService;
import com.oa.service.SalesAgreementService;
import com.oa.util.Constant;
import com.opensymphony.xwork2.ActionSupport;

public class PaymentPlanAction extends ActionSupport {

	private PaymentPlan PaymentPlan;
	private PaymentPlanService PaymentPlanService;
	private SalesAgreementService salesAgreementService;
	private int index;
	private Integer pid;
	private String returns;
	
	private DocumentService documentService;
	private Integer workflowId;

	public String PaymentPlanList() {
		String hql="";
		List<PaymentPlan> PaymentPlans = PaymentPlanService.getPagePaymentPlans((index == 0 ? 1
				: index), PaymentPlan.class, hql);
		for (PaymentPlan m : PaymentPlans) {
			System.out.println(m.toString());
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("listObject", PaymentPlans);
		request.setAttribute("currentIndex", (index == 0 ? 1 : index));
		int total = PaymentPlanService.getAllPaymentPlans(PaymentPlan.class, hql).size();
		// request.setAttribute("pid",(PaymentPlan==null ? "": PaymentPlan.getId()));
		request.setAttribute("url", "PaymentPlanAction!PaymentPlanList?");
		request.setAttribute("totalSize", total);
		getSelect();
		return "PaymentPlanList";
	}

	private void getSelect(){
		List<SalesAgreement> salesAgreements=salesAgreementService.getAllsalesAgreements(SalesAgreement.class, "");
		HttpSession session=ServletActionContext.getRequest().getSession();
		session.setAttribute("salesAgreements", salesAgreements);
	}

	public String addPaymentPlan() {
		
		Users users = (Users) ServletActionContext.getRequest().getSession().getAttribute("admin");
		String key=null;
		Serializable flag = null;
		if (PaymentPlan.getId() == null) {
//			flag = PaymentPlanService.addPaymentPlan(PaymentPlan);
			Document document=new Document();
			String temp=users.getAccount()+Constant.paymentPlan;
			document.setTitle(temp);
			document.setDescription(temp);
			key=Persistence.setVariable(PaymentPlan);
			document.setTypePersist(key+"|paymentPlan");
			document.setUrl("showPaymentPlan.jsp");
			flag=documentService.addDocument(document, workflowId, users.getId(), null);
			returns = "PaymentPlanAction!PaymentPlanList";
			return flag == null ? "operator_failure" : "operator_success";
		}
		PaymentPlanService.updatePaymentPlans(PaymentPlan);
		return "operator_success";
		// return "";
	}

	public String deletePaymentPlan() {
		System.out.println("deleteperson");
		returns = "PaymentPlanAction!PaymentPlanList";
		HttpServletRequest request = ServletActionContext.getRequest();
		String[] ids = request.getParameterValues("delid");
		System.out.println(ids.length + "sdfsadf");
		for (int i = 0; i < ids.length; i++) {
			System.out.println(ids[i]);
		}
		PaymentPlanService.deletePaymentPlans(ids);
		returns = "PaymentPlanAction!PaymentPlanList";
		return "operator_success";
	}
	public String edit(){
		PaymentPlan temp=PaymentPlanService.getPaymentPlan(PaymentPlan.getId());
		ServletActionContext.getRequest().setAttribute("paymentPlan", temp);
		return "editView";
	}

	public PaymentPlanService getPaymentPlanService() {
		return PaymentPlanService;
	}

	@Resource
	public void setPaymentPlanService(PaymentPlanService PaymentPlanService) {
		this.PaymentPlanService = PaymentPlanService;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getReturns() {
		return returns;
	}

	public void setReturns(String returns) {
		this.returns = returns;
	}

	public PaymentPlan getPaymentPlan() {
		return PaymentPlan;
	}

	public void setPaymentPlan(PaymentPlan PaymentPlan) {
		this.PaymentPlan = PaymentPlan;
	}
	public SalesAgreementService getSalesAgreementService() {
		return salesAgreementService;
	}
	@Resource
	public void setSalesAgreementService(SalesAgreementService salesAgreementService) {
		this.salesAgreementService = salesAgreementService;
	}

	public DocumentService getDocumentService() {
		return documentService;
	}

	@Resource
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}


}
