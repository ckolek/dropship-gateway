package me.kolek.ecommerce.dsgw.api.graphql.service;

import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.kolek.ecommerce.dsgw.repository.CarrierRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class CarrierService {

  private CarrierRepository repository;
}
