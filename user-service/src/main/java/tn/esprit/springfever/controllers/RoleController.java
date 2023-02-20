package tn.esprit.springfever.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.Repositories.RoleRepo;
import tn.esprit.springfever.entities.Role;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleRepo roleRepository;

    // Get all roles
    @GetMapping
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Get a role by id
    @GetMapping("/getby/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable(value = "id") Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);

        return ResponseEntity.ok().body(role.get());
    }

    // Create a new role
    @PostMapping("/createRole")
    public Role createRole(@RequestBody Role role) {
        return roleRepository.save(role);
    }

    // Update a role
    @PutMapping("/updateRole/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable(value = "id") Long roleId, @RequestBody Role roleDetails) {
        Optional<Role> role = roleRepository.findById(roleId);

        Role updatedRole = role.get();
        updatedRole.setRolename(roleDetails.getRolename());
        Role savedRole = roleRepository.save(updatedRole);
        return ResponseEntity.ok(savedRole);
    }

    // Delete a role
    @DeleteMapping("/deleteRole/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable(value = "id") Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);

        roleRepository.delete(role.get());
        return ResponseEntity.noContent().build();
    }
}