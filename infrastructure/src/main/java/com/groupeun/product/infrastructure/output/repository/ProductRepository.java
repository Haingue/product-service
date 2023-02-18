package com.groupeun.product.infrastructure.output.repository;

import com.groupeun.product.infrastructure.output.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, UUID> {

    List<ProductEntity> findAllByNameLikeOrderByName (String nameLike);
    List<ProductEntity> findAllByNameLikeOrTypeOrCategoryOrderByNameAscTypeAscCategoryAsc (String nameLike, String type, String category);

}
