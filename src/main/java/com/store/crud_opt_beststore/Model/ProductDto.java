
package com.store.crud_opt_beststore.Model;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductDto {

    @NotBlank(message = "the name is required")
    private String name;

    @NotBlank(message = "the brand is required")
    private String brand;

    @NotEmpty(message = "the name is required")
    private String category;

    @Min(value = 1)
    @NotNull
    private Long price;

    @NotBlank
    @Size(min = 10, message = "the description should be at least 10 characters")
    @Size(max = 2000, message = "the description cannot exceed 2000 characters")
    private String description;

    private MultipartFile imageFile;

    public ProductDto() {
    }

    public ProductDto(String name, String brand, String category, Long price, String description,
            MultipartFile imageFile) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.price = price;
        this.description = description;
        this.imageFile = imageFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    @Override
    public String toString() {
        return "ProductDto [name=" + name + ", brand=" + brand + ", category=" + category + ", price=" + price
                + ", description=" + description + ", imageFile=" + imageFile + "]";
    }

}
