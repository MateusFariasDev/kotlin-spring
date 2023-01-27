package com.bookstore.security

import com.bookstore.enums.CustomerStatus
import com.bookstore.model.CustomerModel
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserCustomDetails(val customerModel: CustomerModel): UserDetails {
    val id: Int = customerModel.id!!

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = customerModel.roles.map { SimpleGrantedAuthority(it.description) }.toMutableList()

    override fun getPassword(): String = customerModel.password
    override fun getUsername(): String = customerModel.id.toString()

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = customerModel.status == CustomerStatus.ATIVO
}