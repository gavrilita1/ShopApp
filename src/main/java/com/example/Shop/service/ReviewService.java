package com.example.Shop.service;

import com.example.Shop.dto.ReviewRequestDto;
import com.example.Shop.dto.ReviewResposeDto;
import com.example.Shop.entity.Product;
import com.example.Shop.entity.Review;
import com.example.Shop.entity.User;
import com.example.Shop.exceptions.ResourceNotFoundException;
import com.example.Shop.repository.OrderRepository;
import com.example.Shop.repository.ProductRepository;
import com.example.Shop.repository.ReviewRepository;
import com.example.Shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    public ReviewResposeDto create(ReviewRequestDto dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not fount"));
        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not fount"));

        boolean foundProduct = orderRepository.existsOrderWithProduct(user.getId(), product.getId());

        if(!foundProduct){
            throw new ResourceNotFoundException("Product was not ordered");
        }

        Review review = new Review(
                null,
                user,
                product,
                dto.rating(),
                dto.comment(),
                new Date()
        );

        return toDto(reviewRepository.save(review));
    }


    public ReviewResposeDto getById(Long id) {
        return toDto(reviewRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Review not fount"))
        );
    }

    public List< ReviewResposeDto > getAllReviews(){
        return reviewRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public void delete(Long id){
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new  ResourceNotFoundException("Review not fount"));
        reviewRepository.delete(review);
    }


    private ReviewResposeDto toDto(Review review) {
        return new ReviewResposeDto(
                review.getId(),
                review.getUser().getUsername(),
                review.getProduct().getName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedDate()
        );
    }


}
