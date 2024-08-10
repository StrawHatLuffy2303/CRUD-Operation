package com.store.crud_opt_beststore.Controller;

import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.store.crud_opt_beststore.Model.Product;
import com.store.crud_opt_beststore.Model.ProductDto;
import com.store.crud_opt_beststore.Repository.ProductsRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductsRepository productsRepo;

    @GetMapping({ "", "/" })
    public String showProductList(Model model) {
        List<Product> products = productsRepo.findAll();
        model.addAttribute("products", products);
        return "ListProduct";
    }

    @GetMapping("/create")
    public String showAddProduct(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        return "CreateProduct";
    }

    @PostMapping("/create")
    public String addProduct(@Valid @ModelAttribute ProductDto productDto,
            BindingResult bindingResult, Model model) {

        if (productDto.getImageFile() == null || productDto.getImageFile().isEmpty()) {
            bindingResult.addError(new FieldError("productDto", "imageFile", "The imagefile is required"));

        }

        if (bindingResult.hasErrors()) {
            // model.addAttribute("errors", bindingResult.getAllErrors());
            return "Createproduct";
        }

        MultipartFile image = productDto.getImageFile();
        String storageFileName = LocalDate.now() + "_" + image.getOriginalFilename();

        try {
            // A hardcoded path to the directory. While this might work for
            // your local development environment, it's not a good practice for several
            // reasons:
            // Portability: The path is specific to your local machine and won't work on
            // other machines or environments.
            // Flexibility: If you need to change the directory location or structure,
            // you'll have to update the hardcoded path.

            // if you want you can do this instead
            // <---- String uploadDir = new
            // FileSystemResource("C:\\Users\\Administrator\\Documents\\CRUD-Operation\\crud-opt-beststore\\src\\main\\resources\\static\\images").getFile().getAbsolutePath();--->

            String uploadDir = new ClassPathResource("static/images").getFile().getAbsolutePath();

            Path uploadPath = Paths.get(uploadDir);

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, uploadPath.resolve(storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);

            }
        } catch (Exception e) {
            System.out.println("Exception" + e.getMessage());
        }

        Product product = new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setLocalDate(LocalDate.now());
        product.setImageFileName(storageFileName);

        productsRepo.save(product);

        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String showEditPage(@RequestParam Long id, Model model) {

        try {
            Product product = productsRepo.findById(id).get();
            model.addAttribute("product", product);

            ProductDto productDto = new ProductDto();
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setCategory(product.getCategory());
            productDto.setPrice(product.getPrice());
            productDto.setDescription(product.getDescription());
            model.addAttribute("productDto", productDto);
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
            return "redirect:/products";
        }

        return "EditProduct";
    }

    @PostMapping("/edit")
    public String updateProduct(@RequestParam Long id, @Valid @ModelAttribute ProductDto productDto,
            BindingResult bindingResult, Model model) {

        try

        {
            Product product = productsRepo.findById(id).get();
            model.addAttribute("product", product);

            if (bindingResult.hasErrors()) {
                return "EditProduct";
            }

            if (!productDto.getImageFile().isEmpty()) {
                // delete old image
                String uploadDir = new ClassPathResource("static/images").getFile().getAbsolutePath();
                Path oldImagePath = Paths.get(uploadDir + "/" + product.getImageFileName());

                try {
                    if (Files.deleteIfExists(oldImagePath)) {
                        // System.out.println("File deleted: " + oldImagePath);
                    } else {
                        // System.out.println("File does not exist or could not be deleted: " +
                        // oldImagePath);
                    }
                } catch (Exception e) {
                    System.out.println("Exception:" + e.getMessage());
                }
                // save new image
                MultipartFile image = productDto.getImageFile();
                String storageFileName = LocalDate.now() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + "/" + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);

                }
                product.setImageFileName(storageFileName);
            }

            product.setName(productDto.getName());
            product.setBrand(productDto.getBrand());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());

            productsRepo.save(product);

        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
        }

        return "redirect:/products";

    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam Long id) {

        try {
            Product product = productsRepo.findById(id).orElse(null);

            if (product != null) {
                String uploadDir = new ClassPathResource("static/images").getFile().getAbsolutePath();
                Path imagePath = Paths.get(uploadDir + "/" + product.getImageFileName());
                try {
                    if (Files.deleteIfExists(imagePath)) {
                        System.out.println("File deleted: " + imagePath);
                    }
                } catch (Exception e) {
                    System.out.println("Exception:" + e.getMessage());
                }
                productsRepo.deleteById(id);
            }
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
        }

        return "redirect:/products";
    }

}
