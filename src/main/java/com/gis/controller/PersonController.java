package com.gis.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import com.gis.entity.PersonEntity;
import com.gis.service.PersonServiceI;

/**   
 * @Title: Controller
 * @Description: 员工信息
 * @author zhangdaihao
 * @date 2015-11-09 16:41:28
 * @version V1.0   
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/personController")
public class PersonController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PersonController.class);

	@Autowired
	private PersonServiceI personService;
	@Autowired
	private SystemService systemService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * 员工信息列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "person")
	public ModelAndView person(HttpServletRequest request) {
		return new ModelAndView("com/gis/person/personList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(PersonEntity person,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(PersonEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, person, request.getParameterMap());
		this.personService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除员工信息
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(PersonEntity person, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		person = systemService.getEntity(PersonEntity.class, person.getId());
		message = "员工信息删除成功";
		personService.delete(person);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加员工信息
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(PersonEntity person, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(person.getId())) {
			message = "员工信息更新成功";
			PersonEntity t = personService.get(PersonEntity.class, person.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(person, t);
				personService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "员工信息更新失败";
			}
		} else {
			message = "员工信息添加成功";
			personService.save(person);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 员工信息列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(PersonEntity person, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(person.getId())) {
			person = personService.getEntity(PersonEntity.class, person.getId());
			req.setAttribute("personPage", person);
		}
		return new ModelAndView("com/gis/person/person");
	}
}
