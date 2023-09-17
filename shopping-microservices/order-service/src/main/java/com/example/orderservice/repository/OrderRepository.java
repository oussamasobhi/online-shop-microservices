package com.example.orderservice.repository;

import com.example.orderservice.model.Order;
import org.hibernate.boot.model.internal.JPAIndexHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

}
