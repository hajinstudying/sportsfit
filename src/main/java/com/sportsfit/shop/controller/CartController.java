package com.sportsfit.shop.controller;


import com.sportsfit.shop.security.dto.MemberSecurityDTO;
import com.sportsfit.shop.service.CartService;
import com.sportsfit.shop.vo.CartVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/cart")
    public String viewCart(Model model,
                           @AuthenticationPrincipal MemberSecurityDTO memberSecurityDTO) {

        log.info("여기는 /cart 컨트롤러");
        Long memberId = memberSecurityDTO.getMemberId();

        CartVo cartVo = cartService.getOrCreateCart(memberId);
        log.info("사용자 Id : {}, 장바구니Id : {}", memberId, cartVo.getCartId());
        model.addAttribute("cartVo", cartVo);

        return "cart/cartDetail";
    }
}
