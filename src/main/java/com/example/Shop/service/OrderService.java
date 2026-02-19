package com.example.Shop.service;

import com.example.Shop.entity.Order;
import com.example.Shop.entity.Product;
import com.example.Shop.entity.User;
import com.example.Shop.repository.OrderReposity;
import com.example.Shop.repository.ProductReposity;
import com.example.Shop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private OrderReposity orderReposity;
    private UserRepository userRepository;
    private ProductReposity productReposity;

    @Transactional
    public Order createOrder(Long user_id, List<Long> product_ids){
        User user = userRepository.findById(user_id).orElseThrow(RuntimeException::new);
        List<Product> products =  productReposity.findAllById(product_ids);

        Double total = products.stream()
                .mapToDouble(Product::getPrice)
                .sum();

        Order order = new Order();
        order.setUser(user);
        order.setProducts(products);
        order.setTotal(total);
        order.setCreatedAt(new Date());

        return orderReposity.save(order);
    }

    public List<Order> getAllOrders(){
        return orderReposity.findAll();
    }

}
