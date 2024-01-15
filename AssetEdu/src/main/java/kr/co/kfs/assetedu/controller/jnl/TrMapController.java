package kr.co.kfs.assetedu.controller.jnl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.co.kfs.assetedu.model.ApiData;
import kr.co.kfs.assetedu.model.Jnl12Tr;
import kr.co.kfs.assetedu.model.Jnl13TrMap;
import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.service.Jnl12TrService;
import kr.co.kfs.assetedu.service.Jnl13TrMapService;
import kr.co.kfs.assetedu.servlet.exception.AssetJsonException;
import kr.co.kfs.assetedu.servlet.view.JsonView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/jnl/trmap")
public class TrMapController {
	
	@Autowired
	Jnl12TrService jnl12TrService;
	
	@Autowired
	Jnl13TrMapService jnl13TrMapService;
	
	@GetMapping("list")
	public String list(String searchText, Model model) {
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("searchText", searchText);
		List <Jnl12Tr> list12 = jnl12TrService.selectList(queryAttr);
		model.addAttribute("list12", list12);

		return "jnl/tr_map/list";
	}
	
	@ResponseBody
	@GetMapping("find")
	public String find (@RequestParam(value="jnl12TrCd") String jnl12TrCd) {
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("jnl12TrCd", jnl12TrCd);
		List<Jnl13TrMap> list = jnl13TrMapService.selectList(queryAttr);
		ApiData apiData = new ApiData();
		apiData.put("list",list);
		apiData.put("jnl12TrCd", jnl12TrCd);
		
		String json = apiData.toJson();
		return json;
	}
	
	@PostMapping("/tr12/insert")
	public ModelAndView insert(@Valid @RequestBody Jnl12Tr jnl12Tr, String searchText) {
		ModelAndView mav = new ModelAndView();
		mav.setView(new JsonView());
		
		try {
			Jnl12Tr already = jnl12TrService.selectOne(jnl12Tr);
			if(already != null) {
				throw new AssetJsonException("중복오류입니다");
			}
		
			
			int i = jnl12TrService.insert(jnl12Tr);
			
			if(i>=1) {
				mav.addObject("result","OK");
			}else {
				mav.addObject("result","NK");
			}
			
			QueryAttr queryAttr = new QueryAttr();
			queryAttr.put("searchText", searchText);
			
			List<Jnl12Tr> list = jnl12TrService.selectList(queryAttr);
			mav.addObject("list", list);
		
		}catch(Exception e) {
			throw new AssetJsonException(e.getMessage());
		}
		
		return mav;
		
		}
	
	@GetMapping("/jnl12tr/{jnl12TrCd}")
	public ModelAndView getOneJnl12Tr(@PathVariable("jnl12TrCd") String jnl12TrCd) {
		ModelAndView mav = new ModelAndView();
		mav.setView(new JsonView());
		mav.addObject("result","NK");
		Jnl12Tr jnl12 = new Jnl12Tr();
		jnl12.setJnl12TrCd(jnl12TrCd);
		Jnl12Tr jnl12Tr = jnl12TrService.selectOne(jnl12);
		if(jnl12Tr != null) {
			mav.addObject("result", "OK");
			mav.addObject("jnl12Tr",jnl12Tr );
			log.debug("tr12  : {}", jnl12TrCd);
		}
		return mav;
	}
	
	@PostMapping("/tr12/update")
	public ModelAndView update(@Valid @RequestBody Jnl12Tr jnl12Tr,BindingResult bindingResult, String searchText) {
		ModelAndView mav = new ModelAndView();
		mav.setView(new JsonView());
		
		String New12TrCd = jnl12Tr.getJnl12TrCd();
		String Origin12TrCd = jnl12Tr.getOriginTrCd();
		
		if(!New12TrCd.equals(Origin12TrCd)) {
			Jnl12Tr already = jnl12TrService.selectOne(jnl12Tr);
			if(already != null) {
				throw new AssetJsonException("거래코드가 중복되었습니다.");
			}
		}
		
		int i = jnl12TrService.update(jnl12Tr);
		
		if(i>=1) {
			mav.addObject("result","OK");
		}else {
			mav.addObject("result","NK");
		}
		
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("searchText", searchText);
		
		List<Jnl12Tr> list = jnl12TrService.selectList(queryAttr);
		mav.addObject("list", list);
		
		return mav;
	
}
	
	@DeleteMapping("/jnl12tr/{jnl12TrCd}/{searchText}")
	public ModelAndView deleteJnl12Tr(@PathVariable("jnl12TrCd") String jnl12TrCd, @PathVariable("searchText") String searchText) {
		ModelAndView mav = new ModelAndView();
		mav.setView(new JsonView());
		
		int deletedCount = jnl12TrService.delete(jnl12TrCd);
		if(deletedCount > 0) {			
			mav.addObject("result","OK");
			

			QueryAttr queryAttr = new QueryAttr();
			if(!searchText.equals("NULL")) {
				queryAttr.put("searchText", searchText);
			}
			
			System.out.println(searchText+"오잉");

			List<Jnl12Tr> list = jnl12TrService.selectList(queryAttr);
			mav.addObject("list", list);
			log.warn("trp id : {}가 삭제되었습니다", jnl12TrCd);
	}
		
		return mav;
		
}
	
	@PostMapping("/tr13/insert")
	public ModelAndView insertTr13 (@Valid @RequestBody Jnl13TrMap jnl13TrMap, BindingResult bindingResult, String searchText) {
		ModelAndView mav = new ModelAndView();
		
		Jnl13TrMap already = jnl13TrMapService.selectOne(jnl13TrMap);
		if(already != null) {
			throw new AssetJsonException("순번 중복오류입니다");
		}
		mav.setView(new JsonView());
		int i = jnl13TrMapService.insert(jnl13TrMap);
		
		if(i>=1) {
			mav.addObject("result","OK");
		}else {
			mav.addObject("result","NK");
		}
		
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("jnl12TrCd", jnl13TrMap.getJnl13TrCd());
		
		List <Jnl13TrMap> list = jnl13TrMapService.selectList(queryAttr);
		mav.addObject("list", list);
		
		return mav;
	}
	
	@PostMapping("/tr13/update")
	public ModelAndView updateTr13(@Valid @RequestBody Jnl13TrMap jnl13TrMap, BindingResult bindingResult, String searchText) {
		ModelAndView mav = new ModelAndView();
		
		Integer jnl13Seq = jnl13TrMap.getJnl13Seq();
		Integer originTrSeq = jnl13TrMap.getOriginTrSeq();
		if(!jnl13Seq.equals(originTrSeq)) {
			Jnl13TrMap already = jnl13TrMapService.selectOne(jnl13TrMap);
			if(already != null) {
				throw new AssetJsonException("순번 중복오류입니다");
			}
		}
		
		int i = jnl13TrMapService.update(jnl13TrMap);
		String result = "NK";
		if(i > 0) {
			result = "OK";
		}
		mav.setView(new JsonView());
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("jnl12TrCd", jnl13TrMap.getJnl13TrCd());
		List<Jnl13TrMap> list = jnl13TrMapService.selectList(queryAttr);
		mav.addObject("list", list);
		mav.addObject("result", result);
		
		
		
		return mav;
	}
	
	@GetMapping("/jnl13tr/{trCd}/{trSeq}")
	public ModelAndView getOneJnl13Tr (@PathVariable("trCd") String trCd, @PathVariable("trSeq") Integer trSeq) {
		ModelAndView mav = new ModelAndView();
		mav.setView(new JsonView());
		
		Jnl13TrMap jnl13TrMap = new Jnl13TrMap();
		jnl13TrMap.setJnl13TrCd(trCd);
		jnl13TrMap.setJnl13Seq(trSeq);
		Jnl13TrMap jnl13 = jnl13TrMapService.selectOne(jnl13TrMap);
		mav.addObject("jnl13TrMap", jnl13);
		return mav;
	}
	
	@DeleteMapping("/jnl13tr/{jnl13TrCd}/{jnl13Seq}")
	public ModelAndView deleteJnl13(@PathVariable("jnl13TrCd") String jnl13TrCd, @PathVariable("jnl13Seq") Integer jnl13Seq) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("trMap삭제할 id : {},{}",jnl13TrCd, jnl13Seq);
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		ModelAndView mav = new ModelAndView();
		mav.setView(new JsonView());
		
		Jnl13TrMap jnl13 = new Jnl13TrMap();
		jnl13.setJnl13TrCd(jnl13TrCd);
		jnl13.setJnl13Seq(jnl13Seq);
		int deletedCount = jnl13TrMapService.delete(jnl13);
		if(deletedCount > 0) {
			log.warn("trMap id : {}가 삭제되었습니다");
		}
		
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("jnl12TrCd", jnl13TrCd);
		List<Jnl13TrMap>list = jnl13TrMapService.selectList(queryAttr);
		mav.addObject("list", list);
		return mav;
	}
	
}
