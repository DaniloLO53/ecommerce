package org.ecommerce.project.services;

import jakarta.transaction.Transactional;
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

import java.util.List;
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

    @Override
    @Transactional // it creates a safe container to ACID transactions on DB
    public CartDTO addProductToCart(Long userId, Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        Cart userCart = cartRepository.findCartByUser_Id(userId);

        Optional<CartProductMetadata> existingMetadata = userCart.getCartsProductsMetadata().stream()
                .filter(metadata -> metadata.getProduct().getId().equals(productId))
                .findFirst();

        if (existingMetadata.isPresent()) {
            CartProductMetadata metadataToUpdate = existingMetadata.get();
            metadataToUpdate.setQuantity(metadataToUpdate.getQuantity() + quantity);
        } else {
            CartProductMetadata newMetadata = new CartProductMetadata();

            newMetadata.setProduct(product);
            newMetadata.setQuantity(quantity);
            newMetadata.setCart(userCart);

            userCart.getCartsProductsMetadata().add(newMetadata);
        }

        Cart savedCart = cartRepository.save(userCart);

        return modelMapper.map(savedCart, CartDTO.class);
    }

    @Override
    public CartDTO getUserCart(Long userId) {
        Cart cart = cartRepository.findCartByUser_Id(userId);
        return modelMapper.map(cart, CartDTO.class);
    }

    @Override
    @Transactional
    public CartDTO updateProductQuantity(Long userId, Long productId, Integer quantity) {
        CartProductMetadata cartProductMetadata = cartProductMetadataRepository
                .findByCart_User_idAndProduct_Id(userId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        cartProductMetadata.setQuantity(quantity);
        CartProductMetadata savedCartProductMetadata = cartProductMetadataRepository.save(cartProductMetadata);

        return modelMapper.map(savedCartProductMetadata.getCart(), CartDTO.class);
    }

    @Override
    @Transactional
    public String deleteProductFromCart(Long userId, Long productId) {
        cartProductMetadataRepository
                .deleteByCart_User_idAndProduct_Id(userId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        return "Product has been deleted successfully";
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        return carts.stream().map(cart -> modelMapper.map(cart, CartDTO.class)).toList();
    }
}