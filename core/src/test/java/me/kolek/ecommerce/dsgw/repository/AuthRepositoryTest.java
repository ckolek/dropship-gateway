package me.kolek.ecommerce.dsgw.repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import me.kolek.ecommerce.dsgw.model.ClientCredentials;
import me.kolek.ecommerce.dsgw.model.ClientCredentials.ClientType;
import me.kolek.ecommerce.dsgw.model.Organization;
import me.kolek.ecommerce.dsgw.model.Organization.Type;
import me.kolek.ecommerce.dsgw.model.Role;
import me.kolek.ecommerce.dsgw.model.Scope;
import me.kolek.ecommerce.dsgw.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthRepositoryTest extends RepositoryTest {
  @Autowired
  private ClientCredentialsRepository clientCredentialsRepository;

  @Autowired
  private OrganizationRepository organizationRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private ScopeRepository scopeRepository;

  @Test
  public void test() {
    var adminOrg = createOrganization("admin", "Admin", Type.ADMIN);

    var readAdminOrgScope = createScope("org/admin/{orgId}:read");
    var readRetailerOrgScope = createScope("org/retail/{orgId}:read");
    var readSupplierOrgScope = createScope("org/supply/{orgId}:read");
    var writeAdminOrgScope = createScope("org/admin/{orgId}:write");
    var writeRetailerOrgScope = createScope("org/retail/{orgId}:write");
    var writeSupplierOrgScope = createScope("org/supply/{orgId}:write");
    var readAdminUserScope = createScope("org/admin/{orgId}/user/{userId}:read");
    var readRetailerUserScope = createScope("org/retail/{orgId}/user/{userId}:read");
    var readSupplierUserScope = createScope("org/supply/{orgId}/user/{userId}:read");
    var writeAdminUserScope = createScope("org/admin/{orgId}/user/{userId}:write");
    var writeRetailerUserScope = createScope("org/retail/{orgId}/user/{userId}:write");
    var writeSupplierUserScope = createScope("org/supply/{orgId}/user/{userId}:write");

    var adminRole = createRole(adminOrg, "admin",
        readAdminOrgScope,
        writeAdminOrgScope,
        readAdminUserScope,
        writeAdminUserScope);

    var adminReadOnlyRole = createRole(adminOrg, "admin-read-only",
        readAdminOrgScope,
        readAdminUserScope);

    var retailerAdminRole = createRole(adminOrg, "retailer-admin",
        readRetailerOrgScope,
        writeRetailerOrgScope,
        readRetailerUserScope,
        writeRetailerUserScope);

    var retailerReadOnlyRole = createRole(adminOrg, "retailer-read-only",
        readRetailerOrgScope,
        readRetailerUserScope);

    var supplierAdminRole = createRole(adminOrg, "supplier-admin",
        readSupplierOrgScope,
        writeSupplierOrgScope,
        readSupplierUserScope,
        writeSupplierUserScope);

    var supplierReadOnlyRole = createRole(adminOrg, "supplier-read-only",
        readSupplierOrgScope,
        readSupplierUserScope);

    Collections.addAll(adminOrg.getCredentials().getRoles(), adminRole, retailerAdminRole,
        supplierAdminRole);
    clientCredentialsRepository.save(adminOrg.getCredentials());

    createOrganization("retailer-1", "Retailer 1", Type.RETAILER, retailerAdminRole);
    createOrganization("supplier-1", "Supplier 1", Type.SUPPLIER, supplierAdminRole);
  }

  private ClientCredentials createCredentials(String clientId, ClientType clientType,
      Role... roles) {
    ClientCredentials credentials = ClientCredentials.builder()
        .clientId(clientId)
        .clientSecret("abc123".getBytes())
        .clientType(clientType)
        .roles(Arrays.stream(roles).collect(Collectors.toSet()))
        .build();

    return clientCredentialsRepository.save(credentials);
  }

  private Scope createScope(String value) {
    Scope scope = Scope.builder()
        .value(value)
        .build();

    return scopeRepository.save(scope);
  }

  private Role createRole(Organization owner, String name, Scope... scopes) {
    Role role = Role.builder()
        .owner(owner)
        .name(name)
        .scopes(Arrays.stream(scopes).collect(Collectors.toSet()))
        .build();

    return roleRepository.save(role);
  }

  private Organization createOrganization(String clientId, String name,
      Organization.Type type, Role... roles) {
    var credentials = createCredentials(clientId, ClientType.ORGANIZATION, roles);
    return createOrganization(credentials, name, type);
  }

  private Organization createOrganization(ClientCredentials credentials, String name,
      Organization.Type type) {
    Organization organization = Organization.builder()
        .credentials(credentials)
        .name(name)
        .type(type)
        .build();

    return organizationRepository.save(organization);
  }

  private User createUser(String clientId, Organization organization, String name, Role... roles) {
    var credentials = createCredentials(clientId, ClientType.USER, roles);
    return createUser(credentials, organization, name);
  }

  private User createUser(ClientCredentials credentials, Organization organization, String name) {
    User user = User.builder()
        .credentials(credentials)
        .organization(organization)
        .name(name)
        .build();

    return userRepository.save(user);
  }
}
