package me.kolek.ecommerce.dsgw.model.mapper;

import java.util.IdentityHashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mapstruct.BeforeMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.TargetType;

@RequiredArgsConstructor
public class CycleAvoidingMappingContext {
    @Getter
    private final boolean setParentReferences;

    public CycleAvoidingMappingContext() {
        this(true);
    }

    private final Map<Object, Object> knownInstances = new IdentityHashMap<>();

    @SuppressWarnings("unchecked")
    @BeforeMapping
    public <T> T getMappedInstance(Object source, @TargetType Class<T> targetType) {
        return (T) knownInstances.get( source );
    }

    @BeforeMapping
    public void storeMappedInstance(Object source, @MappingTarget Object target) {
        knownInstances.put( source, target );
    }
}