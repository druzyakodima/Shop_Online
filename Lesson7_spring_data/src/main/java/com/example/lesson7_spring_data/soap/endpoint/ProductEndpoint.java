package com.example.lesson7_spring_data.soap.endpoint;

import com.example.lesson7_spring_data.soap.product.GetAllProductsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import com.example.lesson7_spring_data.soap.product.GetAllProductsRequest;
import com.example.lesson7_spring_data.soap.service.ProductEntityService;

@Endpoint
@RequiredArgsConstructor
public class ProductEndpoint {

    private static final String NAMESPACE_URI = "http://www.soap.com/spring/ws/product";
    private ProductEntityService productEntityService;

    @Autowired
    public ProductEndpoint(ProductEntityService productEntityService) {
        this.productEntityService = productEntityService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllProductsRequest")
    @ResponsePayload
    public GetAllProductsResponse getAllProducts(@RequestPayload GetAllProductsRequest request) {

        GetAllProductsResponse response = new GetAllProductsResponse();
        productEntityService.getAllProducts().forEach(response.getProducts()::add);

        return response;
    }
}
