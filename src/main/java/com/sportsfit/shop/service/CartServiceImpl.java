package com.sportsfit.shop.service;

import com.sportsfit.shop.repository.CartMapper;
import com.sportsfit.shop.vo.CartItemVo;
import com.sportsfit.shop.vo.CartVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 장바구니 관련 서비스 클래스
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;


    /**
     * 장바구니에 상품을 추가하는 메서드
     * @param memberId 회원 ID
     * @param cartItems 추가할 장바구니 아이템 리스트
     */
    @Override
    public void addToCart(Long memberId, List<CartItemVo> cartItems) {

        log.info("회원id : {}", memberId);
        log.info("장바구니 상품 : {}", cartItems);

        // 장바구니 상품이 없는 경우
        if (cartItems == null || cartItems.isEmpty()) {
            log.warn("빈 장바구니 member ID: {}", memberId);
            throw new IllegalArgumentException("Cart items cannot be empty or null");
        }

        // 회원 장바구니 생성
        CartVo cartVo = getOrCreateCart(memberId);
        log.info("장바구니 cartVo : {}", cartVo);

        if(cartVo == null) {
            log.error("장바구니를 생성하거나 불러오는 중에 오류가 발생했습니다. cartVo : {}", cartVo);
        }

        // 장바구니에 상품 추가
        Long cartId = cartVo.getCartId();
        for(CartItemVo cartItem : cartItems) {
            log.info("장바구니에 상품 추가중....");
            // 장바구니에 이미 존재하는 상품인지 확인
            cartItem.setCartId(cartId);
            CartItemVo existingItem = cartMapper.findCartItemByCartIdAndItemId(cartId, cartItem.getItemId(), cartItem.getOptionId());

            if(existingItem != null) {
                // 장바구니에 이미 존재하는 경우 수량 업데이트
                int newCount = existingItem.getCount() + cartItem.getCount();
                cartMapper.updateCartItemCount(newCount, existingItem.getCartItemId());
                log.info("이미 존재하는 장바구니상품 업데이트. 상품Id : {}, 새로운 수량 : {}",
                        cartItem.getItemId(), cartItem.getCount());
            } else {
                // 장바구니에 존재하지 않는 경우 새로운 상품 추가
                cartMapper.addCartItem(cartItem);
                log.info("새로운 상품 추가 cartItemVo : {}", cartItem);
            }
        }
    }

    /**
     * 회원의 장바구니를 조회하거나 새로 생성하는 메서드
     */
    @Override
    public CartVo getOrCreateCart(Long memberId) {
        try {
            CartVo cartVo = cartMapper.findCartByMemberId(memberId);
            if (cartVo == null) {
                log.info("새로운 장바구니 생성. member ID: {}", memberId);
                cartMapper.createCart(memberId);
                cartVo = cartMapper.findCartByMemberId(memberId);
                if (cartVo == null) {
                    throw new RuntimeException("회원 장바구니를 생성하는 도중 오류 발생. member ID: " + memberId);
                }
            }
            return cartVo;
        } catch (Exception e) {
            log.error("getOrCreateCart 메소드 실행 도중 오류 발생. member ID: {}", memberId, e);
            throw new RuntimeException("장바구니 정보를 가져오거나 생성하는 도중 오류 발생", e);
        }
    }

    /**
     * 장바구니 상품 수량 변경
     */
    @Override
    public void updateCartItemCount(Long memberId, CartItemVo cartItemVo){
        cartMapper.updateCartItemCount(cartItemVo.getCount(), cartItemVo.getCartItemId());
    }

    /**
     * 장바구니 상품 삭제
     */
    @Override
    public void deleteCartItem(Long memberId, Long cartItemId) {
        cartMapper.deleteCartItem(cartItemId);
    }

}
