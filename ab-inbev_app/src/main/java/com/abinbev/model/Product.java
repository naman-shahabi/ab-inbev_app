package com.abinbev.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@NoArgsConstructor
@Data
@Table(name = "product")
@Entity
public class Product extends BaseEntity {
    @Length(max = 256, message = "{validation.product.name.maxLength}")
    @NotBlank(message = "{validation.product.name}")
    @Column(name = "name", nullable = false)
    private String name;

    @Length(max = 1024, message = "{validation.product.description.maxLength}")
    @NotBlank(message = "{validation.product.description}")
    @Column(name = "description", nullable = false)
    private String description;

    @Length(max = 256, message = "{validation.product.brand.maxLength}")
    @NotBlank(message = "{validation.product.brand}")
    @Column(name = "brand", nullable = false)
    private String brand;

    @NotNull(message = "{validation.product.price}")
    @Column(name = "price", nullable = false)
    private BigDecimal price;
}

