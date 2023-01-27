package com.bookstore.service

import com.bookstore.exception.AuthenticationException
import com.bookstore.repository.CustomerRepository
import com.bookstore.security.UserCustomDetails
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsCustomService(
    private val customerRepository: CustomerRepository
) : UserDetailsService {
    override fun loadUserByUsername(id: String): UserDetails {
        val customer = customerRepository.findById(id.toInt())
            .orElseThrow { AuthenticationException("Usuário não encontrado!", "999") }

        return UserCustomDetails(customer)
    }
}