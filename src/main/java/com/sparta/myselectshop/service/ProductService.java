package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.entity.UserRoleEnum;
import com.sparta.myselectshop.naver.dto.ItemDto;
import com.sparta.myselectshop.repository.ProductRepository;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    public static final int MIN_MY_PRICE = 100;

    public ProductResponseDto createProduct(ProductRequestDto requestDto, User user) {
        Product product = (Product)this.productRepository.save(new Product(requestDto, user));
        return new ProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto requestDto) {
        int myPrice = requestDto.getMyprice();
        if (myPrice < 100) {
            throw new IllegalArgumentException("유효하지 않는 관 가격입니다. 최소 100원 이상으로 설정해 주세요.");
        } else {
            Product product = (Product)this.productRepository.findById(id).orElseThrow(() -> new NullPointerException("해당상품을 찾을 수 없습니다."));
            product.update(requestDto);
            return new ProductResponseDto(product);
        }
    }

    @Transactional(
            readOnly = true
    )
    public Page<ProductResponseDto> getProducts(User user, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Direction.ASC : Direction.DESC;
        Sort sort = Sort.by(direction, new String[]{sortBy});
        Pageable pageable = PageRequest.of(page, size, sort);
        UserRoleEnum userRoleEnum = user.getRole();
        Page<Product> productList;
        if (userRoleEnum == UserRoleEnum.USER) {
            productList = this.productRepository.findAllByUser(user, pageable);
        } else {
            productList = this.productRepository.findAll(pageable);
        }

        return productList.map(ProductResponseDto::new);
    }

    @Transactional
    public void updateBySearch(Long id, ItemDto itemDto) {
        Product product = (Product)this.productRepository.findById(id).orElseThrow(() -> new NullPointerException("해당 상품은 존재하지않습니다."));
        product.updateByItemDto(itemDto);
    }

    @Generated
    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}