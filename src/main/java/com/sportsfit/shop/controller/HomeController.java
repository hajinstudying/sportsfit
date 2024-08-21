package com.sportsfit.shop.controller;

import com.sportsfit.shop.service.ItemService;
import com.sportsfit.shop.vo.ItemVo;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Slf4j
public class HomeController {

		private final ItemService itemService;

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

			// 베스트셀러 목록 가져오기
			List<ItemVo> bestItemList = itemService.findItem4ByItemGubun("BEST");
			model.addAttribute("bestItemList", bestItemList);

			// 전체상품 목록 가져오기
			List<ItemVo> allItemList = itemService.findItem4ByItemGubun("ALL");
			model.addAttribute("allItemList", allItemList);

			return "index";
		}

}