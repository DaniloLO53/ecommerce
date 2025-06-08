package org.ecommerce.project.services;

import org.ecommerce.project.exceptions.ResourceNotFoundException;
import org.ecommerce.project.models.Category;
import org.ecommerce.project.repositories.CategoryRepository;
import org.ecommerce.project.repositories.ProductRepository;
import org.ecommerce.project.models.Product;
import org.ecommerce.project.payloads.DTOs.ProductDTO;
import org.ecommerce.project.payloads.responses.PageResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private FileService fileService;
    private ModelMapper modelMapper;
    @Value("${project.image}")
    private String path;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, FileService fileService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
        this.modelMapper = modelMapper;
    }

    @Override
    public PageResponse<ProductDTO> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> page = productRepository.findAll(pageDetails);
        List<Product> products = page.getContent();

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        PageResponse<ProductDTO> productResponse = new PageResponse<>();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(page.getNumber());
        productResponse.setPageSize(page.getSize());
        productResponse.setTotalElements(page.getTotalElements());
        productResponse.setTotalPages(page.getTotalPages());
        productResponse.setLastPage(page.isLast());

        return productResponse;
    }

    public PageResponse<ProductDTO> getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();

            Sort sort = sortOrder.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);
            Page<Product> page = productRepository.findByCategory(category, pageDetails);
            return getProductResponse(pageNumber, pageSize, page);
        }

        throw new ResourceNotFoundException("Category", "id", categoryId);
    }

    public PageResponse<ProductDTO> getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByTitleLikeIgnoreCase('%' + keyword + '%', pageDetails);
        return getProductResponse(pageNumber, pageSize, page);
    }

    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if (optionalCategory.isPresent()) {
            product.setCategory(optionalCategory.get());

            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        }
        throw new ResourceNotFoundException("Category", "id", categoryId);
    }

    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Optional<Product> optionalExistingProduct = productRepository.findById(productId);

        if (optionalExistingProduct.isPresent()) {
            Product existingProduct = optionalExistingProduct.get();

            existingProduct.setTitle(productDTO.getTitle());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setQuantity(productDTO.getQuantity());
            existingProduct.setPrice(productDTO.getPrice());

            Product updatedProduct = productRepository.save(existingProduct);
            return modelMapper.map(updatedProduct, ProductDTO.class);
        }

        throw new ResourceNotFoundException("Product", "id", productId);
    }

    public ProductDTO deleteProduct(Long productId) {
        Optional<Product> optionalExistingProduct = productRepository.findById(productId);

        if (optionalExistingProduct.isPresent()) {
            productRepository.deleteById(productId);
            return modelMapper.map(optionalExistingProduct.get(), ProductDTO.class);
        }

        throw new ResourceNotFoundException("Product", "id", productId);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) {
        Optional<Product> optionalExistingProduct = productRepository.findById(productId);

        if (optionalExistingProduct.isPresent()) {
            Product product = optionalExistingProduct.get();

            try {
                String fileName = fileService.uploadImage(path, image);
                product.setImage(fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Product updatedProduct = productRepository.save(product);
            return modelMapper.map(updatedProduct, ProductDTO.class);
        }

        throw new ResourceNotFoundException("Product", "id", productId);
    }

    private PageResponse<ProductDTO> getProductResponse(Integer pageNumber, Integer pageSize, Page<Product> page) {
        List<Product> products = page.getContent();

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        PageResponse<ProductDTO> productResponse = new PageResponse<>();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageNumber);
        productResponse.setPageSize(pageSize);
        productResponse.setTotalPages(page.getTotalPages());
        productResponse.setTotalElements(page.getTotalElements());
        productResponse.setLastPage(page.isLast());

        return productResponse;
    }
}
