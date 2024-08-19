package com.sportsfit.shop.controller;

import com.sportsfit.shop.dto.MemberFormDto;
import com.sportsfit.shop.service.ItemService;
import com.sportsfit.shop.vo.ItemVo;
import com.sportsfit.shop.vo.MemberVo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final ItemService itemService;

    /**
     * 상품 등록폼
     */
    @GetMapping("/item/create.do")
    public String itemCreateForm(Model model) {
        model.addAttribute("itemVo", new ItemVo());

        return "admin/itemCreateForm";
    }

    /**
     * 상품 등록 처리
     */
    @PostMapping("/item/create.do")
    public String itemCreate(@ModelAttribute("itemVo") ItemVo itemVo,
                             HttpSession session) {


        return "redirect:/admin/item/list.do";
    }
}
