package app.quantun.auth.config.security;

import jakarta.annotation.PostConstruct;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;


import java.security.Security;

@Component
public class CustomOpenSAMLInitializer {

    @PostConstruct
    public void init() {
        Security.addProvider(new BouncyCastleProvider());
    }
}
