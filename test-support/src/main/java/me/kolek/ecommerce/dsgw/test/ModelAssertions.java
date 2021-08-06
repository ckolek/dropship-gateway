package me.kolek.ecommerce.dsgw.test;

import static org.assertj.core.api.Assertions.assertThat;

import me.kolek.ecommerce.dsgw.api.model.AddressDTO;
import me.kolek.ecommerce.dsgw.api.model.ContactDTO;
import me.kolek.ecommerce.dsgw.model.Address;
import me.kolek.ecommerce.dsgw.model.Contact;

public class ModelAssertions {

  public static void assertThatContactIsEquivalent(Contact contact, ContactDTO contactDTO) {
    assertThat(contact.getName()).describedAs("name").isEqualTo(contactDTO.getName());
    assertThat(contact.getPhone()).describedAs("phone").isEqualTo(contactDTO.getPhone());
    assertThat(contact.getEmail()).describedAs("email").isEqualTo(contactDTO.getEmail());
  }

  public static void assertThatAddressIsEquivalent(Address address, AddressDTO addressDTO) {
    assertThat(address.getLine1()).describedAs("line 1").isEqualTo(addressDTO.getLine1());
    assertThat(address.getLine2()).describedAs("line 2").isEqualTo(addressDTO.getLine2());
    assertThat(address.getLine3()).describedAs("line 3").isEqualTo(addressDTO.getLine3());
    assertThat(address.getCity()).describedAs("city").isEqualTo(addressDTO.getCity());
    assertThat(address.getState()).describedAs("state").isEqualTo(addressDTO.getState());
    assertThat(address.getProvince()).describedAs("province").isEqualTo(addressDTO.getProvince());
    assertThat(address.getPostalCode()).describedAs("postal code")
        .isEqualTo(addressDTO.getPostalCode());
    assertThat(address.getCountry()).describedAs("country").isEqualTo(addressDTO.getCountry());
  }
}
