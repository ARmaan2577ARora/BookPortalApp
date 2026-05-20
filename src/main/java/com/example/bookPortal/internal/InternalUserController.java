package com.example.bookPortal.internal;

import com.example.bookPortal.entity.Address;
import com.example.bookPortal.entity.Roles;
import com.example.bookPortal.entity.User;
import com.example.bookPortal.repository.AddressRepo;
import com.example.bookPortal.repository.RolesRepo;
import com.example.bookPortal.repository.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/internal/users")
public class InternalUserController {
    private final UserRepo userRepo;
    private final RolesRepo rolesRepo;
    private final AddressRepo addressRepo;
    private final PasswordEncoder passwordEncoder;

    public InternalUserController(UserRepo userRepo, RolesRepo rolesRepo, AddressRepo addressRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.rolesRepo = rolesRepo;
        this.addressRepo = addressRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, Object> request) {
        String email = string(request.get("email"));
        String password = string(request.get("password"));
        User user = userRepo.findByEmailIgnoreCase(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (Boolean.FALSE.equals(user.getIsActive()) || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        user.setLastLogin(LocalDateTime.now());
        userRepo.save(user);
        return Map.of("user", BackendMapper.user(user));
    }

    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestBody Map<String, Object> request) {
        String fullName = string(request.get("fullName"));
        String email = string(request.get("email"));
        String password = string(request.get("password"));
        String phone = string(request.get("phone"));
        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name, email and password are required");
        }
        if (userRepo.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        Roles customer = rolesRepo.findByRoleNameIgnoreCase("CUSTOMER").orElseThrow();
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(customer);
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user = userRepo.save(user);
        return Map.of("user", BackendMapper.user(user));
    }

    @GetMapping("/{email}")
    public Map<String, Object> user(@PathVariable String email) {
        User user = userRepo.findByEmailIgnoreCase(email).orElseThrow();
        return Map.of("user", BackendMapper.user(user));
    }

    @GetMapping("/{email}/addresses")
    public Map<String, Object> addresses(@PathVariable String email) {
        return Map.of("addresses", addressRepo.findByUser_EmailIgnoreCase(email).stream().map(BackendMapper::address).toList());
    }

    @PostMapping("/{email}/addresses")
    public Map<String, Object> addAddress(@PathVariable String email, @RequestBody Map<String, Object> request) {
        User user = userRepo.findByEmailIgnoreCase(email).orElseThrow();
        String houseAddress = string(request.get("houseAddress"));
        String city = string(request.get("city"));
        if (houseAddress.isBlank() || city.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "House address and city are required");
        }
        boolean makeDefault = bool(request.get("makeDefault"));
        List<Address> addresses = addressRepo.findByUser_UserId(user.getUserId());
        if (makeDefault || addresses.isEmpty()) {
            addresses.forEach(address -> { address.setIsDefault(false); addressRepo.save(address); });
        }
        Address address = new Address();
        address.setUser(user);
        address.setHouseAddress(houseAddress);
        address.setCity(city);
        address.setState(string(request.get("state")));
        address.setZipcode(string(request.get("zipcode")));
        String country = string(request.get("country"));
        address.setCountry(country.isBlank() ? "India" : country);
        address.setIsDefault(makeDefault || addresses.isEmpty());
        return Map.of("address", BackendMapper.address(addressRepo.save(address)));
    }

    private String string(Object o) { return o == null ? "" : String.valueOf(o).trim(); }
    private boolean bool(Object o) { return o != null && (Boolean.TRUE.equals(o) || "true".equalsIgnoreCase(String.valueOf(o))); }
}
