package com.gis.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gis.service.PersonServiceI;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("personService")
@Transactional
public class PersonServiceImpl extends CommonServiceImpl implements PersonServiceI {
	
}