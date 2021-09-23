package com.example.demo.business.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "subscription")
@SuperBuilder
public class Subscription extends Product {

    @Min(value = 0, message = "gbLimit must be positive")
    @Column(name = "gb_limit", nullable = false)
    private Long gbLimit;
}
