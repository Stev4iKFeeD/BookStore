package com.example.bookstore.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "permission")
public class PermissionEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", unique = true, nullable = false)
    private Permission name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    private List<UserEntity> usersWithPermissions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Permission getName() {
        return name;
    }

    public void setName(Permission name) {
        this.name = name;
    }

    public List<UserEntity> getUsersWithPermissions() {
        return usersWithPermissions;
    }

    public void setUsersWithPermissions(List<UserEntity> usersWithPermissions) {
        this.usersWithPermissions = usersWithPermissions;
    }

    public PermissionEntity() {

    }

    public PermissionEntity(Integer id, Permission name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionEntity that = (PermissionEntity) o;
        return Objects.equals(id, that.id)
                && name == that.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "PermissionEntity{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }

}
