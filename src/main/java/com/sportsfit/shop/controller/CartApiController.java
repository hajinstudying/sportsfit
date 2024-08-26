package com.sportsfit.shop.controller;

import com.sportsfit.shop.security.dto.MemberSecurityDTO;
import com.sportsfit.shop.service.CartService;
import com.sportsfit.shop.vo.CartItemVo;
import com.sportsfit.shop.vo.MemberVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 상품 상세 페이지에서 장바구니에 추가 클릭시
 * 장바구니 상품 목록을 전달하는 api
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartApiController {

    private final CartService cartService;

    /**
     * 장바구니에 상품 추가
     */
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody List<CartItemVo> cartItems,
                                       @AuthenticationPrincipal MemberSecurityDTO memberSecurityDTO){

        // 디버깅 문자열
        log.info("전달받은 장바구니아이템들: {}", cartItems);
        if (memberSecurityDTO == null) {
            log.error("사용자 인증 정보가 없습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증이 필요합니다.");
        }

        try{
            Long memberId = memberSecurityDTO.getMemberId();
            if (memberId == null) {
                log.error("회원Id가 null입니다. MemberSecurityDto : {}", memberSecurityDTO);
                return ResponseEntity.badRequest().body("유효하지 않은 회원 정보입니다.");
            }

            cartService.addToCart(memberId, cartItems);
            return ResponseEntity.ok("상품이 장바구니에 추가되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("장바구니에 상품 추가 실패" + e.getMessage());

        }
    }

    /**
     * 장바구니에 상품 수량 변경
     */
    @PutMapping("/update")
    public ResponseEntity<String> updateCartItem(@RequestBody CartItemVo cartItemVo,
                                                 @AuthenticationPrincipal MemberSecurityDTO memberSecurityDTO) {
        try {
            log.info("memberId:{}", memberSecurityDTO.getMemberId());
            cartService.updateCartItemCount(memberSecurityDTO.getMemberId(), cartItemVo);
            return ResponseEntity.ok("장바구니 상품이 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("장바구니 상품 수정 실패: " + e.getMessage());
        }
    }

    /**
     * 장바구니의 상품 제거
     */
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<String> removeCartItem(@PathVariable Long cartItemId,
                                                 @AuthenticationPrincipal MemberSecurityDTO memberSecurityDTO) {
        try {
            cartService.deleteCartItem(memberSecurityDTO.getMemberId(), cartItemId);
            return ResponseEntity.ok("장바구니 상품이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("장바구니 상품 삭제 실패: " + e.getMessage());
        }
    }
}
