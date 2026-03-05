package org.alt.altphase3api.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.alt.altphase3api.domain.enums.AccessRequestStatus;

@Converter(autoApply = true)
public class AccessRequestStatusConverter
        implements AttributeConverter<AccessRequestStatus, String> {

    @Override
    public String convertToDatabaseColumn(AccessRequestStatus status) {
        return status.name().toLowerCase();
    }

    @Override
    public AccessRequestStatus convertToEntityAttribute(String dbData) {
        return AccessRequestStatus.valueOf(dbData.toUpperCase());
    }
}