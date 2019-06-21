package casia.isiteam.springbootshiro.controller.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import casia.isiteam.springbootshiro.service.analysis.AnalysisService;

/**
 * Created by lnx on 2018/12/5.
 */

@RestController
@RequestMapping("analysis")
public class AnalysisController {
	
	private final static Logger logger = LoggerFactory.getLogger(AnalysisController.class);
	
	@Autowired
	AnalysisService analysisService;
	

}
