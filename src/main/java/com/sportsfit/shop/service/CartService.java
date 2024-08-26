package com.sportsfit.shop.service;

import com.sportsfit.shop.vo.CartItemVo;
import com.sportsfit.shop.vo.CartVo;
import com.sportsfit.shop.vo.MemberVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartService {

    // 장바구니에 상품 추가
    void addToCart(Long memberId, List<CartItemVo> cartItems);

    // 회원 장바구니 생성 혹은 가져오기
    CartVo getOrCreateCart(Long memberId);

    // 장바구니 상품 수량 변경
    void updateCartItemCount(Long memberId, CartItemVo cartItemVo);

    // 장바구니 상품 삭제
    void deleteCartItem(Long memberId, Long cartItemId);
}