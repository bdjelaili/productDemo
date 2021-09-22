package com.example.demo.web.mapper;

import com.example.demo.business.model.Phone;
import com.example.demo.business.model.Product;
import com.example.demo.business.model.Subscription;
import com.example.demo.web.dto.ProductDTO;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    private final MapperFacade mapper;

    ProductMapper() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(Product.class, ProductDTO.class)
                .customize(new CustomMapper<>() {
                    @Override
                    public void mapAtoB(Product product, ProductDTO productDTO, MappingContext context) {
                        super.mapAtoB(product, productDTO, context);
                        productDTO.setStoreAddress(product.getStoreAddress() + ", " + product.getCity());

                        if (product instanceof Phone) {
                            productDTO.setType("phone");
                            productDTO.setProperties("color:" + ((Phone) product).getColor());
                        }
                        if (product instanceof Subscription) {
                            productDTO.setType("subscription");
                            productDTO.setProperties("gb_limit:" + ((Subscription) product).getGbLimit());
                        }
                    }
                })
                .byDefault()
                .register();

        mapper = mapperFactory.getMapperFacade();
    }

    public <S, D> D map(S sourceObject, Class<D> destinationClass) {
        return mapper.map(sourceObject, destinationClass);
    }
}
