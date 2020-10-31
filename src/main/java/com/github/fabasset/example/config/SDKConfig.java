package com.github.fabasset.example.config;

import com.github.fabasset.sdk.chaincode.function.Default;
import com.github.fabasset.sdk.chaincode.function.ERC721;
import com.github.fabasset.sdk.chaincode.function.Extension;
import com.github.fabasset.sdk.chaincode.function.TokenTypeManagement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SDKConfig {

    @Bean
    public Default defaultSDK() {
        return new Default();
    }

    @Bean
    public ERC721 erc721SDK() {
        return new ERC721();
    }

    @Bean
    public Extension extensionSDK() {
        return new Extension();
    }

    @Bean
    public TokenTypeManagement tokenTypeManagementSDK() {
        return new TokenTypeManagement();
    }
}
