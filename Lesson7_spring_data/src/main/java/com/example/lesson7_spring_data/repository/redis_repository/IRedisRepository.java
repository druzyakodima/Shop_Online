package com.example.lesson7_spring_data.repository.redis_repository;

import com.example.lesson7_spring_data.entity.LineItem;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRedisRepository  extends CrudRepository<LineItem, Long> {

}
