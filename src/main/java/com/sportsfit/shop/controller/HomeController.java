package com.sportsfit.shop.controller;

import com.sportsfit.shop.service.ItemService;
import com.sportsfit.shop.vo.ItemVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;

/**
 * 홈 컨트롤러
 */
@Controller
@Slf4j
public class HomeController {

		@Autowired
		private ItemService itemService;

		@GetMapping("/")
		public String home(Model model) {
			log.info("여기는 home()");
			return "redirect:/index";
		}

		@GetMapping("/index")
		public String showIndexPage(Model model) {
			log.info("여기는 showIndexPage()");

			// 신상품 목록 가져오기
			List<ItemVo> newItemList = itemService.findItem4ByItemGubun("NEW");
			model.addAttribute("newItemList", newItemList);
			log.info("신상품 목록: {}", newItemList);

			// 베스트셀러 목록 가져오기
			List<ItemVo> bestItemList = itemService.findItem4ByItemGubun("BEST");
			model.addAttribute("bestItemList", bestItemList);
			log.info("베스트셀러 목록: {}", bestItemList);

			return "index";
		}

}