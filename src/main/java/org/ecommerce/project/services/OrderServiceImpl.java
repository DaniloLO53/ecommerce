package org.ecommerce.project.services;

import jakarta.transaction.Transactional;
import org.ecommerce.project.exceptions.APIConflictException;
import org.ecommerce.project.exceptions.ResourceNotFoundException;
import org.ecommerce.project.models.*;
import org.ecommerce.project.payloads.DTOs.*;
import org.ecommerce.project.payloads.responses.OrderRequest;
import org.ecommerce.project.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final CartProductMetadataRepository cartProductMetadataRepository;

    public OrderServiceImpl(
            ModelMapper modelMapper,
            CartRepository cartRepository,
            OrderRepository orderRepository,
            AddressRepository addressRepository,
            CartProductMetadataRepository cartProductMetadataRepository) {
        this.modelMapper = modelMapper;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.cartProductMetadataRepository = cartProductMetadataRepository;
    }

    @Override
    @Transactional
    public OrderDTO orderProducts(Long userId, OrderRequest orderRequest, String paymentMethod) {
        Cart cart = cartRepository.findCartByUser_Id(userId);
        Set<CartProductMetadata> cartsProductsMetadata = cart.getCartsProductsMetadata();

        // See AppConfig
        Set<OrderItem> orderItems = cartsProductsMetadata
                .stream()
                .map(cartProductMetadata ->
                        modelMapper.map(cartProductMetadata, OrderItem.class))
                .collect(Collectors.toSet());

        Order order = new Order();
        order.setOrderDate(LocalDate.now());

        Payment payment = new Payment(
                paymentMethod,orderRequest.getPgPaymentId(),
                orderRequest.getPgStatus(),
                orderRequest.getPgResponseMessage(),
                orderRequest.getPgName()
        );
        payment.setOrder(order);
        order.setPayment(payment);

        Optional<Address> address = addressRepository.findById(orderRequest.getAddressId());
        if (address.isPresent()) {
            order.setAddress(address.get());
        } else {
            throw new ResourceNotFoundException("Address", "id", orderRequest.getAddressId());
        }

        Double cartTotalAmount = calculateTotalAmount(cart);
        order.setTotalAmount(cartTotalAmount);

        orderItems.forEach(orderItem -> {
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        });

        order.getOrderItems().forEach(orderItem -> {
            Product product = orderItem.getProduct();

            if (product.getQuantity() < orderItem.getQuantity()) {
                throw new APIConflictException("Please, select a quantity smaller than " + product.getQuantity());
            }
            product.setQuantity(product.getQuantity() - orderItem.getQuantity());
        });

        Order savedOrder = orderRepository.save(order);

        cartProductMetadataRepository.deleteAllByCart_Id(cart.getId());

        return modelMapper.map(savedOrder, OrderDTO.class);
    }

    public Double calculateTotalAmount(Cart cart) {
        return cart.getCartsProductsMetadata().stream()
                .mapToDouble(metadata -> metadata.getQuantity() * metadata.getProduct().getPrice())
                .sum();
    }
}
