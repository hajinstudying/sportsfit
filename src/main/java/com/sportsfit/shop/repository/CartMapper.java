package com.sportsfit.shop.repository;

import com.sportsfit.shop.vo.CartItemVo;
import com.sportsfit.shop.vo.CartVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CartMapper {

    // 장바구니 존재여부 확인
    boolean existsCartByMemberId(long memberId);

    // 장바구니 추가
    void createCart(long memberId);

    // memberId로 장바구니 조회
    CartVo findCartByMemberId(long memberId);

    // 장바구니상품 추가
    void addCartItem(CartItemVo cartItemVo);

    // 장바구니상품 수량 수정
    void updateCartItemCount(@Param("count") int count, @Param("cartItemId") long cartItemId);

    // 장바구니 상품 삭제
    void deleteCartItem(long cartItemId);

    // 장바구니 비우기
    void clearCart(long memberId);

    // 특정 상품이 장바구니에 있는지 확인
    CartItemVo findCartItemByCartIdAndItemId(@Param("cartId") long cartId,
                                             @Param("itemId") long itemId,
                                             @Param("optionId") long optionId);

}
