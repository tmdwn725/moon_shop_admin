package com.shop.common.enumConvert;

import com.shop.dto.enums.ProductType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

/**
 * ProductType Converter
 */
@Converter(autoApply = true)
public class ProductTypeConverter implements AttributeConverter<ProductType, String> {
    /**
     * enum > DB데이터
     * @param attribute  the entity attribute value to be converted
     * @return
     */
    @Override
    public String convertToDatabaseColumn(ProductType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    /**
     * DB데이터 > enum
     * @param dbData  the data from the database column to be
     *                converted
     * @return
     */
    @Override
    public ProductType convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(ProductType.class).stream()
                .filter(e->e.getCode().equals(dbData))
                .findAny()
                .orElseThrow(()-> new NoSuchElementException());
    }
}

