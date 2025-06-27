package org.ecommerce.project.services;

import jakarta.transaction.Transactional;
import org.ecommerce.project.exceptions.APIConflictException;
import org.ecommerce.project.exceptions.ResourceNotFoundException;
import org.ecommerce.project.models.Cart;
import org.ecommerce.project.models.CartProductMetadata;
import org.ecommerce.project.models.Product;
import org.ecommerce.project.payloads.DTOs.CartDTO;
import org.ecommerce.project.repositories.CartProductMetadataRepository;
import org.ecommerce.project.repositories.CartRepository;
import org.ecommerce.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// TODO: change product quantity when user ORDER product
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

        if (product.getQuantity() < quantity) throw new APIConflictException("Please, make an order with less than or equal to the quantity " + product.getQuantity());

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

//        product.setQuantity(product.getQuantity() - quantity);
//        productRepository.save(product);

        Cart savedCart = cartRepository.save(userCart);
        return modelMapper.map(savedCart, CartDTO.class);

//
//        Set<CartProductMetadata> cartsProductsMetadata = userCart.getCartsProductsMetadata();
//        Stream<ProductDTO> productDTOStream = cartsProductsMetadata.stream().map(item -> {
//            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
//            productDTO.setQuantity(item.getQuantity() - quantity);
//            return productDTO;
//        });
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

        // Product quantity = 90
        // Add to cart quantity = 5 -> Product quantity = 85 (90 - 5)
        // Update cart quantity = 10 -> Product quantity =

//        Product product = productRepository.findById(productId).get();

//        product.setQuantity(product.getQuantity() - quantity);
//        productRepository.save(product);

        cartProductMetadata.setQuantity(quantity);
        CartProductMetadata savedCartProductMetadata = cartProductMetadataRepository.save(cartProductMetadata);

        return modelMapper.map(savedCartProductMetadata.getCart(), CartDTO.class);
    }

    // Removing from parents first, then the element itself. Otherwise, it doesn't remove (idk why)
    // TODO: entender qual é a melhor maneira de lidar com operações no db (se é no pai ou no filho, se é no repository ou na entidade (com set, get) etc)
    @Override
    @Transactional
    public String deleteProductFromCart(Long userId, Long productId) {
        CartProductMetadata itemToDelete = cartProductMetadataRepository
                .findByCart_User_idAndProduct_Id(userId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        Product product = itemToDelete.getProduct();
        Cart cart = itemToDelete.getCart();

        product.getCartProductMetadata().remove(itemToDelete);
        if (cart != null) {
            cart.getCartsProductsMetadata().remove(itemToDelete);
        }

        cartProductMetadataRepository.delete(itemToDelete);

        return "Product has been deleted from cart successfully";
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        return carts.stream().map(cart -> modelMapper.map(cart, CartDTO.class)).toList();
    }
}