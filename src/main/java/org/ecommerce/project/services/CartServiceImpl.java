package org.ecommerce.project.services;

import org.ecommerce.project.exceptions.ResourceNotFoundException;
import org.ecommerce.project.models.Cart;
import org.ecommerce.project.models.CartProductMetadata;
import org.ecommerce.project.models.Product;
import org.ecommerce.project.payloads.DTOs.CartDTO;
import org.ecommerce.project.payloads.DTOs.CartProductMetadataDTO;
import org.ecommerce.project.payloads.DTOs.ProductDTO;
import org.ecommerce.project.repositories.CartProductMetadataRepository;
import org.ecommerce.project.repositories.CartRepository;
import org.ecommerce.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartProductMetadataRepository cartProductMetadataRepository;
    private final ModelMapper modelMapper;

    public CartServiceImpl(
            ProductRepository productRepository,
            CartRepository cartRepository,
            CartProductMetadataRepository cartProductMetadataRepository,
            ModelMapper modelMapper
    ) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartProductMetadataRepository = cartProductMetadataRepository;
        this.modelMapper = modelMapper;
    }

    public CartDTO addProductToCart(Long userId, Long productId, Integer quantity) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            Cart userCart = cartRepository.findCartByUser_Id(userId);

            CartProductMetadata cartProductMetadata = new CartProductMetadata();
            cartProductMetadata.setProduct(product);
            cartProductMetadata.setQuantity(quantity);
            cartProductMetadata.setCart(userCart);

            userCart.getCartsProductsMetadata().add(cartProductMetadata);

            Cart savedCart = cartRepository.save(userCart);

            return modelMapper.map(savedCart, CartDTO.class);
        }

        throw new ResourceNotFoundException("Product", "id", productId);
    }
}