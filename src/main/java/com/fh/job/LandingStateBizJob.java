package com.fh.job;

import javax.annotation.Resource;
import com.fh.service.system.appuserlogininfo.AppuserLoginInfoManager;
import com.fh.util.PageData;

public class LandingStateBizJob {
	
	@Resource(name = "appuserlogininfoService")
	private AppuserLoginInfoManager appuserlogininfoService;
	
	
	public void getLandingStateJob(){
		
        PageData pd = new PageData();
        try {
			appuserlogininfoService.changestate(pd);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
     
	}	
	
}
