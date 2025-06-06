package org.ecommerce.project.service;

import org.ecommerce.project.exception.ResourceNotFoundException;
import org.ecommerce.project.model.Category;
import org.ecommerce.project.model.Product;
import org.ecommerce.project.payload.DTOs.ProductDTO;
import org.ecommerce.project.payload.responses.ProductResponse;
import org.ecommerce.project.repository.CategoryRepository;
import org.ecommerce.project.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService implements ProductServiceInterface {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findAll(pageDetails);
        return getProductResponse(pageNumber, pageSize, page);
    }

    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
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

    public ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
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

            Double specialPrice = product.getPrice() - product.getDiscount();
            product.setSpecialPrice(specialPrice);

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
            existingProduct.setDiscount(productDTO.getDiscount());
            existingProduct.setSpecialPrice(productDTO.getPrice() - productDTO.getDiscount());

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

    public ProductDTO updateProductImage(Long productId, MultipartFile image) {
        Optional<Product> optionalExistingProduct = productRepository.findById(productId);

        if (optionalExistingProduct.isPresent()) {
            Product product = optionalExistingProduct.get();
            String path = "images/";

            try {
                String fileName = uploadImage(path, image);
                product.setImage(fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Product updatedProduct = productRepository.save(product);
            return modelMapper.map(updatedProduct, ProductDTO.class);
        }

        throw new ResourceNotFoundException("Product", "id", productId);
    }

    private String uploadImage(String path, MultipartFile imageFile) throws IOException {
        String randomUUID = UUID.randomUUID().toString();
        String originalFileName = imageFile.getOriginalFilename();

        if (originalFileName == null || originalFileName.lastIndexOf('.') == -1) {
            throw new IllegalArgumentException("Invalid file name or file without extension");
        }

        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String generatedFileName = randomUUID.concat(extension);

        File destinationDirectory = new File(path);
        if (!destinationDirectory.exists()) {
            boolean created = destinationDirectory.mkdirs();
            if (!created) {
                throw new IOException("Failed to create the directory: " + path);
            }
            System.out.println("Directory created at: " + destinationDirectory.getAbsolutePath());
        }

        Path destinationFilePath = Paths.get(path, generatedFileName);
        try (InputStream inputStream = imageFile.getInputStream()) {
            Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Failed to save file: " + generatedFileName, e);
        }

        return generatedFileName;
    }

    private ProductResponse getProductResponse(Integer pageNumber, Integer pageSize, Page<Product> page) {
        List<Product> products = page.getContent();

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageNumber);
        productResponse.setPageSize(pageSize);
        productResponse.setTotalPages(page.getTotalPages());
        productResponse.setTotalElements(page.getTotalElements());
        productResponse.setLastPage(page.isLast());

        return productResponse;
    }
}
