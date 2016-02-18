package com.apofig.samples.spring.controller;

import com.apofig.samples.spring.model.QuestionAnswer;
import com.apofig.samples.spring.service.QuestionAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/")
public class RestController {


	@Autowired
	private QuestionAnswerService service;
	
	@RequestMapping
	public String index() {
		return "page";
	}

	@RequestMapping(value = "/qa/{id}", method = RequestMethod.GET)
	public @ResponseBody QuestionAnswer qa(@PathVariable int id) {
		return service.get(id);
	}

	@RequestMapping(value = "/qa/{id}", method = RequestMethod.POST)
	public @ResponseBody String vote(@PathVariable int id, @RequestParam("vote") int vote) {
		service.setVote(id, vote);
        service.saveToFile("data/report-result.csv");
		return "{}";
	}

}
