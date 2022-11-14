package com.csdn.biz.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Commons 会帮助接口生成动态代理对象
 *
 * @Author: xiewenfeng
 * @Date: 2022/11/14 13:35
 */
@Repository
@NoRepositoryBean
public interface UserRepository extends CrudRepository {

}
