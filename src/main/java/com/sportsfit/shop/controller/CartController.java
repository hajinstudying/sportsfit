package com.sportsfit.shop.controller;


import com.sportsfit.shop.dto.CartItemDto;
import com.sportsfit.shop.security.dto.MemberSecurityDTO;
import com.sportsfit.shop.service.CartService;
import com.sportsfit.shop.vo.CartVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/cart")
    public String viewCart(Model model,
                           @AuthenticationPrincipal MemberSecurityDTO memberSecurityDTO) {

        // 시큐리티에서 회원id 가져오기
        Long memberId = memberSecurityDTO.getMemberId();

        // 회원아이디로 장바구니 찾기
        CartVo cartVo = cartService.getOrCreateCart(memberId);
        Long cartId = cartVo.getCartId();

        // 장바구니코드로 장바구니상품 목록 가져오기
        List<CartItemDto> cartItemDtos = cartService.findCartItemWithItemInfo(cartId);
        model.addAttribute("cartItemDtos", cartItemDtos);
        return "cart/cartDetail";
    }
}
