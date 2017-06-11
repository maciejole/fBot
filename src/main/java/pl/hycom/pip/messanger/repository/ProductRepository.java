/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.hycom.pip.messanger.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.hycom.pip.messanger.repository.model.Keyword;
import pl.hycom.pip.messanger.repository.model.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

    @Query("Select p from Product p")
    public List<Product> findSomeProducts(Pageable pa);

    @Query("Select p from Product p where p not in (:productsForCustomer)")
    public List<Product> findSomeProducts(@Param("productsForCustomer") List<Product> products, Pageable pa);

    @Query("select p from Product p where (:requiredKeyword) member of p.keywords")
    public List<Product> findProductsWithKeyword(@Param("requiredKeyword") Keyword keyword);
}
